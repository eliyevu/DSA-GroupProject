package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.Edge;
import com.ug.dsa.datastructures.Graph;
import com.ug.dsa.datastructures.MinHeap;


public class Dijkstra {

    private Graph graph;
    private MinHeap heap;

    public Dijkstra(Graph graph, MinHeap heap) {
        this.graph = graph;
        this.heap = heap;
    }

    /**
     * Computes the shortest distance from source to every vertex
     * in the graph. Assumes all edge weights are non-negative.
     *
     * @return an array where result[v] is the shortest distance from
     * source to vertex v, or Integer.MAX_VALUE if unreachable.
     */
    public int[] shortestPath(int source) {

        int n = graph.getNumVertices();

        if (source < 0 || source >= n) {
            throw new IllegalArgumentException(
                "Invalid source vertex: " + source
                + " (graph has " + n + " vertices, 0.." + (n - 1) + ")"
            );
        }

        int[] distance = new int[n];

        for (int i = 0; i < n; i++) {
            distance[i] = Integer.MAX_VALUE;
        }

        boolean[] visited = new boolean[n];

        distance[source] = 0;
        heap.insert(source, 0);

        while (!heap.isEmpty()) {

            int u = heap.extractMin();

            if (visited[u]) {
                continue;
            }

            visited[u] = true;

            for (Edge edge : graph.getNeighbours(u)) {

                int v = edge.getDest();
                int weight = edge.getWeight();

                if (visited[v]) {
                    continue;
                }

                if (distance[u] != Integer.MAX_VALUE
                        && distance[u] + weight < distance[v]) {

                    distance[v] = distance[u] + weight;

                    if (heap.contains(v)) {
                        heap.decreaseKey(v, distance[v]);
                    } else {
                        heap.insert(v, distance[v]);
                    }
                }
            }
        }

        return distance;
    }
}