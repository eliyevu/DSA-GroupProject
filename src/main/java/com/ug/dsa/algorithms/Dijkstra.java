package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.Graph;
import com.ug.dsa.datastructures.MinHeap;

/**
 * Dijkstra's Shortest Path Algorithm, built from scratch.
 *
 * Owner: Roselyn Francis (Member 10)
 * Required data structures (per TASKS.md collaboration table): Graph, Heap.
 *
 * This class does NOT create its own Graph or MinHeap - both are received
 * from the outside via the constructor, so this stays decoupled from
 * whichever concrete Graph/Heap implementation ends up merged into the
 * team's dev branch.
 *
 * EXPECTED API CONTRACT (per TASKS.md's "API Design Requirement" section -
 * confirm these exact signatures with Nana Kofi Agyin and Amoah Edward
 * Junior before they finalize their implementations, to avoid this class
 * breaking on integration):
 *
 *   Graph:
 *     int vertexCount()
 *     Graph.Edge[] getNeighbours(int vertex)   // each Edge has .target and .weight
 *
 *   MinHeap:
 *     boolean isEmpty()
 *     boolean contains(int vertex)
 *     void insert(int vertex, int priority)
 *     int extractMin()
 *     void decreaseKey(int vertex, int newPriority)
 *
 * If Member 12 or Member 7's published API differs from this (e.g. a
 * different method name, or a Heap that doesn't support decreaseKey),
 * this file will need small adjustments - but the algorithm itself
 * (below) won't change.
 */
public class Dijkstra {

    private Graph graph;

    private MinHeap heap;

    public Dijkstra(Graph graph, MinHeap heap) {

        this.graph = graph;

        this.heap = heap;

    }

    /**
     * Computes the shortest distance from {@code source} to every vertex
     * in the graph. Assumes all edge weights are non-negative.
     *
     * @return an array where result[v] is the shortest distance from
     *         source to vertex v, or Integer.MAX_VALUE if unreachable.
     */
    public int[] shortestPath(int source) {

        int n = graph.vertexCount();

        if (source < 0 || source >= n) {
            throw new IllegalArgumentException(
                "Invalid source vertex: " + source + " (graph has " + n + " vertices, 0.." + (n - 1) + ")");
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

            for (Graph.Edge edge : graph.getNeighbours(u)) {

                int v = edge.target;
                int weight = edge.weight;

                if (visited[v]) {
                    continue;
                }

                if (distance[u] != Integer.MAX_VALUE && distance[u] + weight < distance[v]) {

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
