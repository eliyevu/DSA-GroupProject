package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.DynamicArray;
import com.ug.dsa.datastructures.Edge;
import com.ug.dsa.datastructures.Graph;
import com.ug.dsa.datastructures.Queue;

public class BFS {

    public static DynamicArray<Integer> traverse(Graph graph, int startVertex) {

        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }

        if (startVertex < 0 || startVertex >= graph.getNumVertices()) {
            throw new IndexOutOfBoundsException(
                    "Invalid start vertex: " + startVertex
            );
        }

        boolean[] visited = new boolean[graph.getNumVertices()];
        DynamicArray<Integer> order = new DynamicArray<>();
        Queue<Integer> queue = new Queue<>();

        queue.enqueue(startVertex);
        visited[startVertex] = true;

        while (!queue.isEmpty()) {
            int current = queue.dequeue();
            order.add(current);

            Edge[] neighbours = graph.getNeighbours(current);

            for (int i = 0; i < neighbours.length; i++) {
                int neighbour = neighbours[i].getDest();

                if (!visited[neighbour]) {
                    visited[neighbour] = true;
                    queue.enqueue(neighbour);
                }
            }
        }

        return order;
    }
}