package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.DynamicArray;
import com.ug.dsa.datastructures.Graph;

public class BfsTest {
    public static void main(String[] args) {
        Graph graph = new Graph(6);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);

        DynamicArray result = BFS.traverse(graph, 0);

        System.out.print("BFS order: ");
        result.display();
    }
}