package com.ug.dsa.services;

import com.ug.dsa.datastructures.DynamicArray;
import com.ug.dsa.datastructures.Graph;
import com.ug.dsa.datastructures.HashTable;
import com.ug.dsa.models.AlgorithmRun;
import com.ug.dsa.models.AuditEvent;
import com.ug.dsa.models.Location;
import com.ug.dsa.models.Resource;
import com.ug.dsa.models.Road;
import com.ug.dsa.models.ServiceRequest;
import com.ug.dsa.database.DatabaseConnection;
import com.ug.dsa.database.DatabaseInitializer;
import com.ug.dsa.database.LocationDAO;
import com.ug.dsa.database.RoadDAO;
import com.ug.dsa.database.ServiceRequestDAO;
import com.ug.dsa.database.ResourceDAO;
import com.ug.dsa.database.AlgorithmRunDAO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * DataLoaderService: Bridges the database / CSV data with the custom DSA layer.
 *
 * Main flow:
 *   CSV / PostgreSQL  →  Java Models  →  Custom Data Structures  →  Algorithms
 *
 * On every loadAll() call:
 *   1. Attempts to connect to PostgreSQL.  If it succeeds, the schema is initialised
 *      and data is loaded via DAOs; then data is synced from CSV (upsert).
 *   2. If PostgreSQL is unavailable the service falls back gracefully to CSV parsing
 *      so the system always has data available.
 */
public class DataLoaderService {

    // ── Paths to CSV seed files ───────────────────────────────────────────────
    private static final String LOCATIONS_CSV       = "data/locations.csv";
    private static final String ROADS_CSV           = "data/roads.csv";
    private static final String REQUESTS_CSV        = "data/service_requests.csv";
    private static final String RESOURCES_CSV       = "data/resources.csv";
    private static final String ALGORITHM_RUNS_CSV  = "data/algorithm_runs.csv";

    // ── In-memory stores (always populated regardless of DB availability) ─────
    private DynamicArray<Location>      locations;
    private DynamicArray<Road>          roads;
    private DynamicArray<ServiceRequest> serviceRequests;
    private DynamicArray<Resource>      resources;
    private DynamicArray<AlgorithmRun>  algorithmRuns;
    private DynamicArray<AuditEvent>    auditEvents;

    // ── Graph + mapping helpers ───────────────────────────────────────────────
    private Graph                        networkGraph;
    private HashTable<Integer, Integer>  locationIdToIndex;   // locationId → graph vertex index
    private HashTable<Integer, Location> indexToLocation;     // graph vertex index → Location

    private boolean dbAvailable = false;

    // ── DAOs ──────────────────────────────────────────────────────────────────
    private final LocationDAO       locationDAO       = new LocationDAO();
    private final RoadDAO           roadDAO           = new RoadDAO();
    private final ServiceRequestDAO requestDAO        = new ServiceRequestDAO();
    private final ResourceDAO       resourceDAO       = new ResourceDAO();
    private final AlgorithmRunDAO   algorithmRunDAO   = new AlgorithmRunDAO();

    public DataLoaderService() {
        this.locations       = new DynamicArray<>();
        this.roads           = new DynamicArray<>();
        this.serviceRequests = new DynamicArray<>();
        this.resources       = new DynamicArray<>();
        this.algorithmRuns   = new DynamicArray<>();
        this.auditEvents     = new DynamicArray<>();
        this.locationIdToIndex = new HashTable<>();
        this.indexToLocation   = new HashTable<>();
    }

    // =========================================================================
    //  Public API
    // =========================================================================

    /**
     * Loads all data from the database (if available) or from CSV files.
     * Also builds the graph and populates the indexing / scheduling structures.
     * Returns a summary string for the console.
     */
    public String loadAll() {
        // Reset
        locations       = new DynamicArray<>();
        roads           = new DynamicArray<>();
        serviceRequests = new DynamicArray<>();
        resources       = new DynamicArray<>();
        algorithmRuns   = new DynamicArray<>();
        auditEvents     = new DynamicArray<>();
        locationIdToIndex = new HashTable<>();
        indexToLocation   = new HashTable<>();

        // 1. Check DB
        dbAvailable = checkDatabaseAvailability();

        if (dbAvailable) {
            System.out.println("  [DB] PostgreSQL connection successful.");
            tryInitDb();
            loadFromDatabase();
        } else {
            System.out.println("  [DB] PostgreSQL unavailable – falling back to CSV files.");
            loadFromCSV();
        }

        // 2. Build graph
        buildGraph();

        return String.format(
            "Loaded: %d locations, %d roads, %d service requests, %d resources, %d algorithm runs.",
            locations.size(), roads.size(), serviceRequests.size(),
            resources.size(), algorithmRuns.size()
        );
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public DynamicArray<Location>       getLocations()       { return locations; }
    public DynamicArray<Road>           getRoads()           { return roads; }
    public DynamicArray<ServiceRequest> getServiceRequests() { return serviceRequests; }
    public DynamicArray<Resource>       getResources()       { return resources; }
    public DynamicArray<AlgorithmRun>   getAlgorithmRuns()   { return algorithmRuns; }
    public DynamicArray<AuditEvent>     getAuditEvents()     { return auditEvents; }
    public Graph                        getNetworkGraph()    { return networkGraph; }
    public HashTable<Integer, Integer>  getLocationIdToIndex() { return locationIdToIndex; }
    public HashTable<Integer, Location> getIndexToLocation()   { return indexToLocation; }
    public boolean                      isDbAvailable()      { return dbAvailable; }

    /**
     * Adds an audit event to the in-memory store (and persists to DB if available).
     */
    public void addAuditEvent(AuditEvent event) {
        auditEvents.add(event);
        if (dbAvailable) {
            try {
                new com.ug.dsa.database.AuditEventDAO().create(event);
            } catch (SQLException e) {
                // best-effort
            }
        }
    }

    /**
     * Adds an algorithm-run record to the in-memory store (and persists to DB if available).
     */
    public void addAlgorithmRun(AlgorithmRun run) {
        algorithmRuns.add(run);
        if (dbAvailable) {
            try {
                algorithmRunDAO.create(run);
            } catch (SQLException e) {
                // best-effort
            }
        }
    }

    // =========================================================================
    //  Private – Database helpers
    // =========================================================================

    private boolean checkDatabaseAvailability() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return conn != null;
        } catch (SQLException e) {
            return false;
        }
    }

    private void tryInitDb() {
        try {
            DatabaseInitializer.initializeDatabase();
        } catch (Exception e) {
            System.out.println("  [DB] Schema init warning: " + e.getMessage());
        }
    }

    private void loadFromDatabase() {
        try {
            List<Location> locs = locationDAO.findAll();
            if (locs.isEmpty()) {
                // DB is empty – seed from CSV then reload
                System.out.println("  [DB] Tables empty – seeding from CSV files...");
                loadFromCSV();
                persistToDatabase();
            } else {
                for (Location l : locs) locations.add(l);
                for (Road r : roadDAO.findAll()) roads.add(r);
                for (ServiceRequest sr : requestDAO.findAll()) serviceRequests.add(sr);
                for (Resource res : resourceDAO.findAll()) resources.add(res);
                for (AlgorithmRun ar : algorithmRunDAO.findAll()) algorithmRuns.add(ar);
            }
        } catch (SQLException e) {
            System.out.println("  [DB] Read error – falling back to CSV: " + e.getMessage());
            loadFromCSV();
        }
    }

    private void persistToDatabase() {
        try {
            for (int i = 0; i < locations.size(); i++) {
                try { locationDAO.create(locations.get(i)); } catch (SQLException ignored) {}
            }
            for (int i = 0; i < roads.size(); i++) {
                try { roadDAO.create(roads.get(i)); } catch (SQLException ignored) {}
            }
            for (int i = 0; i < serviceRequests.size(); i++) {
                try { requestDAO.create(serviceRequests.get(i)); } catch (SQLException ignored) {}
            }
            for (int i = 0; i < resources.size(); i++) {
                try { resourceDAO.create(resources.get(i)); } catch (SQLException ignored) {}
            }
            for (int i = 0; i < algorithmRuns.size(); i++) {
                try { algorithmRunDAO.create(algorithmRuns.get(i)); } catch (SQLException ignored) {}
            }
            System.out.println("  [DB] Seed data persisted to PostgreSQL.");
        } catch (Exception e) {
            System.out.println("  [DB] Persist warning: " + e.getMessage());
        }
    }

    // =========================================================================
    //  Private – CSV helpers
    // =========================================================================

    private void loadFromCSV() {
        loadLocationsFromCSV();
        loadRoadsFromCSV();
        loadRequestsFromCSV();
        loadResourcesFromCSV();
        loadAlgorithmRunsFromCSV();
    }

    private void loadLocationsFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(LOCATIONS_CSV))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] f = splitCSV(line);
                if (f.length < 6) continue;
                locations.add(new Location(
                    parseInt(f[0]), f[1].trim(), f[2].trim(), f[3].trim(),
                    parseDouble(f[4]), parseDouble(f[5])
                ));
            }
        } catch (IOException e) {
            System.out.println("  [CSV] Could not read " + LOCATIONS_CSV + ": " + e.getMessage());
        }
    }

    private void loadRoadsFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(ROADS_CSV))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] f = splitCSV(line);
                if (f.length < 6) continue;
                roads.add(new Road(
                    parseInt(f[0]), parseInt(f[1]), parseInt(f[2]),
                    parseDouble(f[3]), parseDouble(f[4]), parseDouble(f[5])
                ));
            }
        } catch (IOException e) {
            System.out.println("  [CSV] Could not read " + ROADS_CSV + ": " + e.getMessage());
        }
    }

    private void loadRequestsFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(REQUESTS_CSV))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] f = splitCSV(line);
                if (f.length < 8) continue;
                serviceRequests.add(new ServiceRequest(
                    parseInt(f[0]), parseInt(f[1]), parseInt(f[2]),
                    f[3].trim(), parseInt(f[4]),
                    f[5].trim(), f[6].trim(), f[7].trim()
                ));
            }
        } catch (IOException e) {
            System.out.println("  [CSV] Could not read " + REQUESTS_CSV + ": " + e.getMessage());
        }
    }

    private void loadResourcesFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(RESOURCES_CSV))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] f = splitCSV(line);
                if (f.length < 5) continue;
                resources.add(new Resource(
                    parseInt(f[0]), f[1].trim(), parseInt(f[2]),
                    parseInt(f[3]), f[4].trim()
                ));
            }
        } catch (IOException e) {
            System.out.println("  [CSV] Could not read " + RESOURCES_CSV + ": " + e.getMessage());
        }
    }

    private void loadAlgorithmRunsFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(ALGORITHM_RUNS_CSV))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] f = splitCSV(line);
                if (f.length < 6) continue;
                algorithmRuns.add(new AlgorithmRun(
                    parseInt(f[0]), f[1].trim(), parseInt(f[2]),
                    parseLong(f[3]), parseDouble(f[4]), f[5].trim()
                ));
            }
        } catch (IOException e) {
            System.out.println("  [CSV] Could not read " + ALGORITHM_RUNS_CSV + ": " + e.getMessage());
        }
    }

    // =========================================================================
    //  Private – Graph construction
    // =========================================================================

    /**
     * Builds the campus road network Graph from the loaded Location and Road data.
     * Assigns each Location (by insertion order) a contiguous graph vertex index,
     * and populates locationIdToIndex / indexToLocation for O(1) lookups.
     *
     * Edge weight = (int)(distance * 100) to preserve two decimal places as an
     * integer, since the Graph only stores int weights.
     */
    private void buildGraph() {
        int n = locations.size();
        networkGraph = new Graph(n, false); // undirected campus network

        // Map locationId → vertex index (0-based, insertion order)
        for (int i = 0; i < n; i++) {
            Location loc = locations.get(i);
            locationIdToIndex.put(loc.getLocationId(), i);
            indexToLocation.put(i, loc);
        }

        // Add edges
        for (int i = 0; i < roads.size(); i++) {
            Road road = roads.get(i);
            Integer fromIdx = locationIdToIndex.get(road.getFromLocationId());
            Integer toIdx   = locationIdToIndex.get(road.getToLocationId());
            if (fromIdx == null || toIdx == null) continue;

            // Use distance * 100 as integer weight (avoids float rounding)
            int weight = Math.max(1, (int) Math.round(road.getDistance() * 100));
            try {
                networkGraph.addEdge(fromIdx, toIdx, weight);
            } catch (Exception ignored) {
                // skip duplicate or invalid edges
            }
        }
    }

    // =========================================================================
    //  Private – Utility
    // =========================================================================

    private String[] splitCSV(String line) {
        return line.split(",", -1);
    }

    private int parseInt(String s) {
        try { return Integer.parseInt(s.trim()); } catch (NumberFormatException e) { return 0; }
    }

    private long parseLong(String s) {
        try { return Long.parseLong(s.trim()); } catch (NumberFormatException e) { return 0L; }
    }

    private double parseDouble(String s) {
        try { return Double.parseDouble(s.trim()); } catch (NumberFormatException e) { return 0.0; }
    }
}
