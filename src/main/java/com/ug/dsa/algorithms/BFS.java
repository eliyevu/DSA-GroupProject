package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.DynamicArray;
import com.ug.dsa.datastructures.Graph;
import com.ug.dsa.datastructures.Queue;

public class BFS {

    public static DynamicArray traverse(Graph graph, int startVertex) {
        boolean[] visited = new boolean[graph.getVertices()];
        DynamicArray order = new DynamicArray();
        Queue queue = new Queue();

        queue.enqueue(startVertex);
        visited[startVertex] = true;

        while (!queue.isEmpty()) {
            int current = queue.dequeue();
            order.add(current);

            DynamicArray neighbours = graph.getNeighbours(current);
            for (int i = 0; i < neighbours.size(); i++) {
                int neighbour = neighbours.get(i);
                if (!visited[neighbour]) {
                    visited[neighbour] = true;
                    queue.enqueue(neighbour);
                }
            }
        }

        return order;
    }
}