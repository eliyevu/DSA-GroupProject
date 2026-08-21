package com.ug.dsa.services;

import com.ug.dsa.algorithms.BFS;
import com.ug.dsa.algorithms.DFS;
import com.ug.dsa.algorithms.Dijkstra;
import com.ug.dsa.algorithms.Kruskal;
import com.ug.dsa.algorithms.Prim;
import com.ug.dsa.datastructures.DynamicArray;
import com.ug.dsa.datastructures.Edge;
import com.ug.dsa.datastructures.Graph;
import com.ug.dsa.datastructures.HashTable;
import com.ug.dsa.datastructures.Heap;
import com.ug.dsa.models.Location;
import com.ug.dsa.models.Road;

/**
 * Routing facade for the graph algorithms required by the project.
 */
public class RoutingService {

    private final Graph networkGraph;
    private final HashTable<Integer, Integer> locationIdToIndexMap;
    private final HashTable<Integer, Location> indexToLocationMap;

    public RoutingService(Graph networkGraph,
                          HashTable<Integer, Integer> idToIndexMap,
                          HashTable<Integer, Location> indexToLocMap) {
        if (networkGraph == null || idToIndexMap == null || indexToLocMap == null) {
            throw new IllegalArgumentException("Graph and location maps are required.");
        }
        this.networkGraph = networkGraph;
        this.locationIdToIndexMap = idToIndexMap;
        this.indexToLocationMap = indexToLocMap;
    }

    public DynamicArray<Location> findReachableLocations(int startLocationId) {
        int start = indexOf(startLocationId);
        DynamicArray<Integer> indices = BFS.traverse(networkGraph, start);
        return mapLocations(indices);
    }

    public DynamicArray<Location> depthFirstLocations(int startLocationId) {
        int start = indexOf(startLocationId);
        DynamicArray<Integer> indices = DFS.iterative(networkGraph, start);
        return mapLocations(indices);
    }

    public DynamicArray<Location> findShortestRoute(int startLocationId, int endLocationId) {
        int start = indexOf(startLocationId);
        int end = indexOf(endLocationId);

        Dijkstra dijkstra = new Dijkstra(networkGraph, new Heap<>());
        DynamicArray<Integer> indices = dijkstra.reconstructPath(start, end);
        return mapLocations(indices);
    }

    public int findShortestDistance(int startLocationId, int endLocationId) {
        int start = indexOf(startLocationId);
        int end = indexOf(endLocationId);
        Dijkstra.Result result = new Dijkstra(networkGraph, new Heap<>()).compute(start);
        return result.getDistances()[end];
    }

    public DynamicArray<Road> buildMinimumNetwork() {
        return convertEdges(Kruskal.findMST(networkGraph).getMstEdges());
    }

    public DynamicArray<Road> buildMinimumNetworkPrim() {
        if (networkGraph.getNumVertices() == 0) return new DynamicArray<>();
        return convertEdges(Prim.findMST(networkGraph).getMstEdges());
    }

    public boolean isConnected() {
        if (networkGraph.getNumVertices() == 0) return true;
        return Prim.findMST(networkGraph).isConnected();
    }

    public Graph getNetworkGraph() { return networkGraph; }

    private int indexOf(int locationId) {
        Integer index = locationIdToIndexMap.get(locationId);
        if (index == null) throw new IllegalArgumentException("Unknown location ID: " + locationId);
        return index;
    }

    private DynamicArray<Location> mapLocations(DynamicArray<Integer> indices) {
        DynamicArray<Location> locations = new DynamicArray<>();
        for (int i = 0; i < indices.size(); i++) {
            Location location = indexToLocationMap.get(indices.get(i));
            if (location != null) locations.add(location);
        }
        return locations;
    }

    private DynamicArray<Road> convertEdges(Edge[] edges) {
        DynamicArray<Road> roads = new DynamicArray<>();
        for (int i = 0; i < edges.length; i++) {
            Edge edge = edges[i];
            Location from = indexToLocationMap.get(edge.getSrc());
            Location to = indexToLocationMap.get(edge.getDest());
            if (from == null || to == null) continue;

            // Graph stores distance * 100 as an integer weight.
            roads.add(new Road(0, from.getLocationId(), to.getLocationId(),
                    edge.getWeight() / 100.0, 0.0, edge.getWeight() / 100.0));
        }
        return roads;
    }
}
