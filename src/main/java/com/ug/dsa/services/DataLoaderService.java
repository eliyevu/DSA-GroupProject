package com.ug.dsa.services;

import com.ug.dsa.database.LocationDAO;
import com.ug.dsa.database.ResourceDAO;
import com.ug.dsa.database.RoadDAO;
import com.ug.dsa.database.ServiceRequestDAO;

import com.ug.dsa.datastructures.DynamicArray;
import com.ug.dsa.datastructures.Graph;

import com.ug.dsa.models.Location;
import com.ug.dsa.models.Resource;
import com.ug.dsa.models.Road;
import com.ug.dsa.models.ServiceRequest;

import java.sql.SQLException;
import java.util.List;

/**
 * Loads persistent application data into the custom data structures
 * used by the Smart Service Operations Optimizer.
 * This class is responsible for loading and preparing data.
 * It does NOT implement scheduling, routing, indexing, or optimization
 * logic itself.
 */
public class DataLoaderService {

    private final LocationDAO locationDAO;
    private final RoadDAO roadDAO;
    private final ServiceRequestDAO serviceRequestDAO;
    private final ResourceDAO resourceDAO;

    private final IndexingService indexingService;

    private DynamicArray<Location> locations;
    private DynamicArray<Road> roads;
    private DynamicArray<ServiceRequest> requests;
    private DynamicArray<Resource> resources;

    private Graph graph;

    // Creates a DataLoaderService with the required DAOs and indexing service
    public DataLoaderService() {
        this.locationDAO = new LocationDAO();
        this.roadDAO = new RoadDAO();
        this.serviceRequestDAO = new ServiceRequestDAO();
        this.resourceDAO = new ResourceDAO();

        this.indexingService = new IndexingService();

        this.locations = new DynamicArray<>();
        this.roads = new DynamicArray<>();
        this.requests = new DynamicArray<>();
        this.resources = new DynamicArray<>();

        this.graph = null;
    }

     // Loads all required data from PostgreSQL
    public void loadAll() throws SQLException {
        loadLocations();
        loadRoads();
        loadRequests();
        loadResources();

        loadIntoGraph();
        loadIntoIndexes();
    }

     //  Loads all locations from the database
    public void loadLocations() throws SQLException {

        locations.clear();

        List<Location> databaseLocations = locationDAO.findAll();

        if (databaseLocations == null) {
            return;
        }

        for (Location location : databaseLocations) {

            if (location == null) {
                continue;
            }

            locations.add(location);
        }
    }

    // Loads all roads from the database
    public void loadRoads() throws SQLException {

        roads.clear();

        List<Road> databaseRoads = roadDAO.findAll();

        if (databaseRoads == null) {
            return;
        }

        for (Road road : databaseRoads) {

            if (road == null) {
                continue;
            }

            roads.add(road);
        }
    }

    // Loads all service requests from the database
    public void loadRequests() throws SQLException {

        requests.clear();

        List<ServiceRequest> databaseRequests = serviceRequestDAO.findAll();

        if (databaseRequests == null) {
            return;
        }

        for (ServiceRequest request : databaseRequests) {

            if (request == null) {
                continue;
            }

            requests.add(request);
        }
    }

    // Loads all resources from the database
    public void loadResources() throws SQLException {

        resources.clear();

        List<Resource> databaseResources = resourceDAO.findAll();

        if (databaseResources == null) {
            return;
        }

        for (Resource resource : databaseResources) {

            if (resource == null) {
                continue;
            }

            resources.add(resource);
        }
    }

    // Builds the project's custom Graph from the loaded locations and roads
    public void loadIntoGraph() {

        int maxLocationId = findMaximumLocationId();

        if (maxLocationId < 0) {
            graph = new Graph(0, false);
            return;
        }

        graph = new Graph(maxLocationId + 1, false);

        for (int i = 0; i < roads.size(); i++) {

            Road road = roads.get(i);

            if (road == null) {
                continue;
            }

            int source = road.getFromLocationId();
            int destination = road.getToLocationId();

            validateGraphLocation(source);
            validateGraphLocation(destination);

            int weight = convertToGraphWeight(
                    road.getRoadConditionWeight()
            );

            graph.addEdge(source, destination, weight);
        }
    }

    // Populates the existing IndexingService with loaded entities
    public void loadIntoIndexes() {

        for (int i = 0; i < locations.size(); i++) {

            Location location = locations.get(i);

            if (location != null) {
                indexingService.indexLocation(location);
            }
        }

        for (int i = 0; i < resources.size(); i++) {

            Resource resource = resources.get(i);

            if (resource != null) {
                indexingService.indexResource(resource);
            }
        }

        for (int i = 0; i < requests.size(); i++) {

            ServiceRequest request = requests.get(i);

            if (request != null) {
                indexingService.indexRequest(request);
            }
        }
    }

    // Finds the largest location ID in the loaded location data
    private int findMaximumLocationId() {

        if (locations.isEmpty()) {
            return -1;
        }

        int maximum = locations.get(0).getLocationId();

        for (int i = 1; i < locations.size(); i++) {

            Location location = locations.get(i);

            if (location != null
                    && location.getLocationId() > maximum) {

                maximum = location.getLocationId();
            }
        }

        return maximum;
    }

    // Ensures a location ID can be represented by the current Graph
    private void validateGraphLocation(int locationId) {

        if (locationId < 0
                || graph == null
                || locationId >= graph.getNumVertices()) {

            throw new IllegalArgumentException(
                    "Road references invalid location ID: "
                            + locationId
            );
        }
    }

    // Converts the model's double road-condition weight to the integer weight required by the current Graph implementation
    private int convertToGraphWeight(double weight) {

        if (Double.isNaN(weight)
                || Double.isInfinite(weight)
                || weight <= 0) {

            throw new IllegalArgumentException(
                    "Road condition weight must be positive: "
                            + weight
            );
        }

        return Math.max(1, (int) Math.round(weight));
    }

    // ---------------------------------------------------------------
    // Getters
    // ---------------------------------------------------------------

    public DynamicArray<Location> getLocations() {
        return locations;
    }

    public DynamicArray<Road> getRoads() {
        return roads;
    }

    public DynamicArray<ServiceRequest> getRequests() {
        return requests;
    }

    public DynamicArray<Resource> getResources() {
        return resources;
    }

    public Graph getGraph() {
        return graph;
    }

    public IndexingService getIndexingService() {
        return indexingService;
    }

    public int getLocationCount() {
        return locations.size();
    }

    public int getRoadCount() {
        return roads.size();
    }

    public int getRequestCount() {
        return requests.size();
    }

    public int getResourceCount() {
        return resources.size();
    }
}
