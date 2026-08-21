package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.DynamicArray;
import com.ug.dsa.datastructures.Edge;
import com.ug.dsa.datastructures.Graph;
import com.ug.dsa.datastructures.Heap;

/** Dijkstra shortest-path algorithm using the project's custom Heap. */
public class Dijkstra {

    private final Graph graph;
    private final Heap<Integer> heap;

    public Dijkstra(Graph graph, Heap<Integer> heap) {
        if (graph == null || heap == null) throw new IllegalArgumentException("Graph and heap are required.");
        this.graph = graph;
        this.heap = heap;
    }

    public static final class Result {
        private final int[] distances;
        private final int[] previous;

        private Result(int[] distances, int[] previous) {
            this.distances = distances;
            this.previous = previous;
        }

        public int[] getDistances() { return distances; }
        public int[] getPrevious() { return previous; }

        public boolean isReachable(int vertex) {
            return distances[vertex] != Integer.MAX_VALUE;
        }
    }

    public int[] shortestPath(int source) {
        return compute(source).getDistances();
    }

    public Result compute(int source) {
        int n = graph.getNumVertices();
        validateVertex(source, n);

        int[] distance = new int[n];
        int[] previous = new int[n];
        boolean[] visited = new boolean[n];

        for (int i = 0; i < n; i++) {
            distance[i] = Integer.MAX_VALUE;
            previous[i] = -1;
        }

        distance[source] = 0;
        heap.insert(source, 0);

        while (!heap.isEmpty()) {
            int u = heap.extractMin();
            if (visited[u]) continue;
            visited[u] = true;

            for (Edge edge : graph.getNeighbours(u)) {
                int v = edge.getDest();
                int weight = edge.getWeight();
                if (weight < 0 || distance[u] == Integer.MAX_VALUE) continue;

                long candidate = (long) distance[u] + weight;
                if (candidate < distance[v]) {
                    distance[v] = candidate > Integer.MAX_VALUE
                            ? Integer.MAX_VALUE
                            : (int) candidate;
                    previous[v] = u;

                    if (heap.contains(v)) heap.decreaseKey(v, distance[v]);
                    else heap.insert(v, distance[v]);
                }
            }
        }

        return new Result(distance, previous);
    }

    public DynamicArray<Integer> reconstructPath(int source, int destination) {
        Result result = compute(source);
        validateVertex(destination, graph.getNumVertices());

        DynamicArray<Integer> path = new DynamicArray<>();
        if (!result.isReachable(destination)) return path;

        int[] reverse = new int[graph.getNumVertices()];
        int count = 0;
        int current = destination;

        while (current != -1) {
            reverse[count++] = current;
            if (current == source) break;
            current = result.previous[current];
        }

        if (reverse[count - 1] != source) return path;

        for (int i = count - 1; i >= 0; i--) path.add(reverse[i]);
        return path;
    }

    private void validateVertex(int vertex, int n) {
        if (vertex < 0 || vertex >= n) {
            throw new IllegalArgumentException("Invalid vertex: " + vertex);
        }
    }
}
