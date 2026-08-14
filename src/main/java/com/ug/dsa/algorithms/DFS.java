package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.DynamicArray;
import com.ug.dsa.datastructures.Edge;
import com.ug.dsa.datastructures.Graph;
import com.ug.dsa.datastructures.Stack;

public class DFS {

    /**
     * Performs recursive Depth-First Search.
     */
    public static DynamicArray recursive(Graph graph, int startVertex) {

        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null.");
        }

        if (startVertex < 0 || startVertex >= graph.getNumVertices()) {
            throw new IllegalArgumentException(
                    "Invalid start vertex: " + startVertex
            );
        }

        boolean[] visited = new boolean[graph.getNumVertices()];
        DynamicArray order = new DynamicArray();

        dfsRecursive(graph, startVertex, visited, order);

        return order;
    }

    private static void dfsRecursive(
            Graph graph,
            int vertex,
            boolean[] visited,
            DynamicArray order) {

        visited[vertex] = true;
        order.add(vertex);

        Edge[] neighbours = graph.getNeighbours(vertex);

        for (int i = 0; i < neighbours.length; i++) {

            int neighbour = neighbours[i].getDest();

            if (!visited[neighbour]) {
                dfsRecursive(
                        graph,
                        neighbour,
                        visited,
                        order
                );
            }
        }
    }

    /**
     * Performs iterative Depth-First Search using
     * the project's custom Stack.
     */
    public static DynamicArray iterative(
            Graph graph,
            int startVertex) {

        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null.");
        }

        if (startVertex < 0 || startVertex >= graph.getNumVertices()) {
            throw new IllegalArgumentException(
                    "Invalid start vertex: " + startVertex
            );
        }

        boolean[] visited = new boolean[graph.getNumVertices()];
        DynamicArray order = new DynamicArray();

        Stack<Integer> stack = new Stack<>(graph.getNumVertices());

        stack.push(startVertex);

        while (!stack.isEmpty()) {

            int current = stack.pop();

            if (visited[current]) {
                continue;
            }

            visited[current] = true;
            order.add(current);

            Edge[] neighbours = graph.getNeighbours(current);

            // Push in reverse order so that traversal follows
            // the same order as the adjacency list.
            for (int i = neighbours.length - 1; i >= 0; i--) {

                int neighbour = neighbours[i].getDest();

                if (!visited[neighbour]) {
                    stack.push(neighbour);
                }
            }
        }

        return order;
    }

    /**
     * Checks whether a path exists between two vertices.
     */
    public static boolean hasPath(
            Graph graph,
            int startVertex,
            int targetVertex) {

        DynamicArray order = iterative(graph, startVertex);

        for (int i = 0; i < order.size(); i++) {
            if (order.get(i).equals(targetVertex)) {
                return true;
            }
        }

        return false;
    }
}
