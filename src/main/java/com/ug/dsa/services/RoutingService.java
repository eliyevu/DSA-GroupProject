package com.ug.dsa.services;

import com.ug.dsa.models.Location;
import com.ug.dsa.models.Road;

// Custom Data Structures
import com.ug.dsa.datastructures.Graph;
import com.ug.dsa.datastructures.DynamicArray;
import com.ug.dsa.datastructures.HashTable;
import com.ug.dsa.datastructures.Edge;
import com.ug.dsa.datastructures.Heap;

// Custom Algorithms
import com.ug.dsa.algorithms.BFS;
import com.ug.dsa.algorithms.Dijkstra;
import com.ug.dsa.algorithms.Kruskal;

public class RoutingService {
    
    private Graph networkGraph;
    
    // Maps domain Location.locationId to internal Graph index
    private HashTable<Integer, Integer> locationIdToIndexMap;
    // Maps internal Graph index back to the domain Location object
    private HashTable<Integer, Location> indexToLocationMap;

    public RoutingService(Graph networkGraph, 
                          HashTable<Integer, Integer> idToIndexMap, 
                          HashTable<Integer, Location> indexToLocMap) {
        this.networkGraph = networkGraph;
        this.locationIdToIndexMap = idToIndexMap;
        this.indexToLocationMap = indexToLocMap;
    }

    /**
     * Finds all locations reachable from a starting point using BFS.
     */
    public DynamicArray<Location> findReachableLocations(int startLocationId) {
        int startIndex = locationIdToIndexMap.get(startLocationId);
        
        // Assumes BFS.traverse returns a DynamicArray of integers (graph indices)
        DynamicArray<Integer> reachableIndices = BFS.traverse(networkGraph, startIndex);
        
        DynamicArray<Location> reachableLocations = new DynamicArray<>();
        
        for (int i = 0; i < reachableIndices.size(); i++) {
            Location loc = indexToLocationMap.get(reachableIndices.get(i));
            // Fixed: Changed from insert() to add() based on teammate's DynamicArray.java
            reachableLocations.add(loc); 
        }
        
        return reachableLocations;
    }

    /**
     * Finds the shortest route utilizing Dijkstra's algorithm.
     */
    public DynamicArray<Location> findShortestRoute(int startLocationId, int endLocationId) {
        int startIndex = locationIdToIndexMap.get(startLocationId);
        int endIndex = locationIdToIndexMap.get(endLocationId);
        
        // Fixed: Dijkstra requires instantiation with a Heap based on teammate's Dijkstra.java
        Heap<Integer> minHeap = new Heap<>(); 
        Dijkstra dijkstra = new Dijkstra(networkGraph, minHeap);
        
        // This returns distances, but the algorithm currently doesn't track the predecessor path
        int[] distances = dijkstra.shortestPath(startIndex); 
        
        DynamicArray<Location> pathLocations = new DynamicArray<>();
        
        // TODO for Team: Dijkstra.java needs to be updated to maintain a 'parent[]' or 'predecessor[]' 
        // array so the actual route can be reconstructed here. 
        // For now, we return just the start and end to make it compile.
        pathLocations.add(indexToLocationMap.get(startIndex));
        pathLocations.add(indexToLocationMap.get(endIndex));
        
        return pathLocations;
    }

    /**
     * Builds a minimum spanning tree representing the core campus network.
     */
    public DynamicArray<Road> buildMinimumNetwork() {
        // Fixed: Call findMST and get the array of edges based on teammate's Kruskal.java
        Kruskal.Result result = Kruskal.findMST(networkGraph);
        Edge[] mstEdges = result.getMstEdges();
        
        DynamicArray<Road> minimumNetwork = new DynamicArray<>();
        
        // Convert the returned Edges back into our domain Road objects
        for (int i = 0; i < mstEdges.length; i++) {
            Edge edge = mstEdges[i];
            Road road = new Road();
            
            // Map the graph indices back to domain location IDs
            Location fromLoc = indexToLocationMap.get(edge.getSrc());
            Location toLoc = indexToLocationMap.get(edge.getDest());
            
            if (fromLoc != null && toLoc != null) {
                road.setFromLocationId(fromLoc.getLocationId());
                road.setToLocationId(toLoc.getLocationId());
                road.setDistance(edge.getWeight()); // Mapping weight to distance
                minimumNetwork.add(road);
            }
        }
        
        return minimumNetwork;
    }
}