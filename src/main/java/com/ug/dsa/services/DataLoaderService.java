package com.ug.dsa.services;

import com.ug.dsa.database.AlgorithmRunDAO;
import com.ug.dsa.database.DatabaseConnection;
import com.ug.dsa.database.DatabaseInitializer;
import com.ug.dsa.database.LocationDAO;
import com.ug.dsa.database.ResourceDAO;
import com.ug.dsa.database.RoadDAO;
import com.ug.dsa.database.ServiceRequestDAO;
import com.ug.dsa.datastructures.DynamicArray;
import com.ug.dsa.datastructures.Graph;
import com.ug.dsa.datastructures.HashTable;
import com.ug.dsa.models.AlgorithmRun;
import com.ug.dsa.models.AuditEvent;
import com.ug.dsa.models.Location;
import com.ug.dsa.models.Resource;
import com.ug.dsa.models.Road;
import com.ug.dsa.models.ServiceRequest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Bridges PostgreSQL/CSV data with the custom DSA layer.
 *
 * PostgreSQL / CSV
 *       -> Java models
 *       -> custom data structures
 *       -> algorithms/services
 */
public class DataLoaderService {

    private static final String LOCATIONS_CSV = "data/locations.csv";
    private static final String ROADS_CSV = "data/roads.csv";
    private static final String REQUESTS_CSV = "data/service_requests.csv";
    private static final String RESOURCES_CSV = "data/resources.csv";
    private static final String ALGORITHM_RUNS_CSV = "data/algorithm_runs.csv";

    private DynamicArray<Location> locations = new DynamicArray<>();
    private DynamicArray<Road> roads = new DynamicArray<>();
    private DynamicArray<ServiceRequest> serviceRequests = new DynamicArray<>();
    private DynamicArray<Resource> resources = new DynamicArray<>();
    private DynamicArray<AlgorithmRun> algorithmRuns = new DynamicArray<>();
    private DynamicArray<AuditEvent> auditEvents = new DynamicArray<>();

    private Graph networkGraph;
    private HashTable<Integer, Integer> locationIdToIndex = new HashTable<>();
    private HashTable<Integer, Location> indexToLocation = new HashTable<>();

    private final LocationDAO locationDAO = new LocationDAO();
    private final RoadDAO roadDAO = new RoadDAO();
    private final ServiceRequestDAO requestDAO = new ServiceRequestDAO();
    private final ResourceDAO resourceDAO = new ResourceDAO();
    private final AlgorithmRunDAO algorithmRunDAO = new AlgorithmRunDAO();

    private boolean dbAvailable;

    public String loadAll() {
        reset();
        dbAvailable = checkDatabaseAvailability();

        if (dbAvailable) {
            try {
                DatabaseInitializer.initializeDatabase();
                loadFromDatabase();
            } catch (Exception e) {
                System.out.println("  [DB] Database load failed - using CSV: " + e.getMessage());
                loadFromCSV();
                dbAvailable = false;
            }
        } else {
            System.out.println("  [DB] PostgreSQL unavailable - using CSV fallback.");
            loadFromCSV();
        }

        buildGraph();

        return String.format(
                "Loaded: %d locations, %d roads, %d service requests, %d resources, %d algorithm runs.",
                locations.size(), roads.size(), serviceRequests.size(), resources.size(), algorithmRuns.size()
        );
    }

    private void reset() {
        locations = new DynamicArray<>();
        roads = new DynamicArray<>();
        serviceRequests = new DynamicArray<>();
        resources = new DynamicArray<>();
        algorithmRuns = new DynamicArray<>();
        auditEvents = new DynamicArray<>();
        locationIdToIndex = new HashTable<>();
        indexToLocation = new HashTable<>();
        networkGraph = null;
    }

    private void loadFromDatabase() throws SQLException {
        List<Location> dbLocations = locationDAO.findAll();

        if (dbLocations == null || dbLocations.isEmpty()) {
            System.out.println("  [DB] Database is empty - importing CSV seed data.");
            loadFromCSV();
            persistToDatabase();
            return;
        }

        addAll(locations, dbLocations);
        addAll(roads, roadDAO.findAll());
        addAll(serviceRequests, requestDAO.findAll());
        addAll(resources, resourceDAO.findAll());
        addAll(algorithmRuns, algorithmRunDAO.findAll());
    }

    private <T> void addAll(DynamicArray<T> target, List<T> source) {
        if (source == null) return;
        for (T value : source) {
            if (value != null) target.add(value);
        }
    }

    private void persistToDatabase() {
        for (int i = 0; i < locations.size(); i++) safeCreateLocation(locations.get(i));
        for (int i = 0; i < roads.size(); i++) safeCreateRoad(roads.get(i));
        for (int i = 0; i < serviceRequests.size(); i++) safeCreateRequest(serviceRequests.get(i));
        for (int i = 0; i < resources.size(); i++) safeCreateResource(resources.get(i));
        for (int i = 0; i < algorithmRuns.size(); i++) safeCreateAlgorithmRun(algorithmRuns.get(i));
        System.out.println("  [DB] CSV seed data persisted to PostgreSQL.");
    }

    private void safeCreateLocation(Location value) {
        try { locationDAO.create(value); } catch (SQLException ignored) { }
    }
    private void safeCreateRoad(Road value) {
        try { roadDAO.create(value); } catch (SQLException ignored) { }
    }
    private void safeCreateRequest(ServiceRequest value) {
        try { requestDAO.create(value); } catch (SQLException ignored) { }
    }
    private void safeCreateResource(Resource value) {
        try { resourceDAO.create(value); } catch (SQLException ignored) { }
    }
    private void safeCreateAlgorithmRun(AlgorithmRun value) {
        try { algorithmRunDAO.create(value); } catch (SQLException ignored) { }
    }

    private boolean checkDatabaseAvailability() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    private void loadFromCSV() {
        loadLocationsFromCSV();
        loadRoadsFromCSV();
        loadRequestsFromCSV();
        loadResourcesFromCSV();
        loadAlgorithmRunsFromCSV();
    }

    private void loadLocationsFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(LOCATIONS_CSV))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] f = splitCSV(line);
                if (f.length < 6) continue;
                locations.add(new Location(parseInt(f[0]), f[1].trim(), f[2].trim(), f[3].trim(),
                        parseDouble(f[4]), parseDouble(f[5])));
            }
        } catch (IOException e) {
            System.out.println("  [CSV] Could not read " + LOCATIONS_CSV + ": " + e.getMessage());
        }
    }

    private void loadRoadsFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ROADS_CSV))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] f = splitCSV(line);
                if (f.length < 6) continue;
                roads.add(new Road(parseInt(f[0]), parseInt(f[1]), parseInt(f[2]),
                        parseDouble(f[3]), parseDouble(f[4]), parseDouble(f[5])));
            }
        } catch (IOException e) {
            System.out.println("  [CSV] Could not read " + ROADS_CSV + ": " + e.getMessage());
        }
    }

    private void loadRequestsFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(REQUESTS_CSV))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] f = splitCSV(line);
                if (f.length < 8) continue;
                serviceRequests.add(new ServiceRequest(parseInt(f[0]), parseInt(f[1]), parseInt(f[2]),
                        f[3].trim(), parseInt(f[4]), f[5].trim(), f[6].trim(), f[7].trim()));
            }
        } catch (IOException e) {
            System.out.println("  [CSV] Could not read " + REQUESTS_CSV + ": " + e.getMessage());
        }
    }

    private void loadResourcesFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(RESOURCES_CSV))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] f = splitCSV(line);
                if (f.length < 5) continue;
                resources.add(new Resource(parseInt(f[0]), f[1].trim(), parseInt(f[2]),
                        parseInt(f[3]), f[4].trim()));
            }
        } catch (IOException e) {
            System.out.println("  [CSV] Could not read " + RESOURCES_CSV + ": " + e.getMessage());
        }
    }

    private void loadAlgorithmRunsFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ALGORITHM_RUNS_CSV))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] f = splitCSV(line);
                if (f.length < 6) continue;
                algorithmRuns.add(new AlgorithmRun(parseInt(f[0]), f[1].trim(), parseInt(f[2]),
                        parseLong(f[3]), parseDouble(f[4]), f[5].trim()));
            }
        } catch (IOException e) {
            System.out.println("  [CSV] Could not read " + ALGORITHM_RUNS_CSV + ": " + e.getMessage());
        }
    }

    /** Builds the graph and keeps domain Location IDs separate from graph indexes. */
    private void buildGraph() {
        networkGraph = new Graph(locations.size(), false);

        for (int i = 0; i < locations.size(); i++) {
            Location location = locations.get(i);
            locationIdToIndex.put(location.getLocationId(), i);
            indexToLocation.put(i, location);
        }

        for (int i = 0; i < roads.size(); i++) {
            Road road = roads.get(i);
            Integer from = locationIdToIndex.get(road.getFromLocationId());
            Integer to = locationIdToIndex.get(road.getToLocationId());
            if (from == null || to == null) continue;

            int weight = Math.max(1, (int) Math.round(road.getDistance() * 100.0));
            networkGraph.addEdge(from, to, weight);
        }
    }

    /** Populates the supplied IndexingService using the loaded models. */
    public void loadIntoIndexes(IndexingService indexingService) {
        if (indexingService == null) throw new IllegalArgumentException("IndexingService cannot be null.");
        for (int i = 0; i < locations.size(); i++) indexingService.indexLocation(locations.get(i));
        for (int i = 0; i < resources.size(); i++) indexingService.indexResource(resources.get(i));
        for (int i = 0; i < serviceRequests.size(); i++) indexingService.indexRequest(serviceRequests.get(i));
    }

    public void addAuditEvent(AuditEvent event) {
        if (event == null) return;
        auditEvents.add(event);
        if (dbAvailable) {
            try { new com.ug.dsa.database.AuditEventDAO().create(event); }
            catch (SQLException e) { System.out.println("  [DB] Audit event not persisted: " + e.getMessage()); }
        }
    }

    public void addAlgorithmRun(AlgorithmRun run) {
        if (run == null) return;
        algorithmRuns.add(run);
        if (dbAvailable) {
            try { algorithmRunDAO.create(run); }
            catch (SQLException e) { System.out.println("  [DB] Algorithm run not persisted: " + e.getMessage()); }
        }
    }

    private String[] splitCSV(String line) { return line.split(",", -1); }

    private int parseInt(String value) {
        try { return Integer.parseInt(value.trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    private long parseLong(String value) {
        try { return Long.parseLong(value.trim()); }
        catch (NumberFormatException e) { return 0L; }
    }

    private double parseDouble(String value) {
        try { return Double.parseDouble(value.trim()); }
        catch (NumberFormatException e) { return 0.0; }
    }

    public DynamicArray<Location> getLocations() { return locations; }
    public DynamicArray<Road> getRoads() { return roads; }
    public DynamicArray<ServiceRequest> getServiceRequests() { return serviceRequests; }
    public DynamicArray<Resource> getResources() { return resources; }
    public DynamicArray<AlgorithmRun> getAlgorithmRuns() { return algorithmRuns; }
    public DynamicArray<AuditEvent> getAuditEvents() { return auditEvents; }
    public Graph getNetworkGraph() { return networkGraph; }
    public HashTable<Integer, Integer> getLocationIdToIndex() { return locationIdToIndex; }
    public HashTable<Integer, Location> getIndexToLocation() { return indexToLocation; }
    public boolean isDbAvailable() { return dbAvailable; }
}
