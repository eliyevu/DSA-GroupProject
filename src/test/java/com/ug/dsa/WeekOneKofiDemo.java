package com.ug.dsa;

import com.ug.dsa.algorithms.Kruskal;
import com.ug.dsa.datastructures.Graph;

public class WeekOneKofiDemo {

    public static void main(String[] args) {
        System.out.println("=======================================================================");
        System.out.println("   WEEK ONE DEMO - GRAPH DATA STRUCTURE & KRUSKAL'S ALGORITHM");
        System.out.println("   Lead Developer: Nana Kofi Agyin");
        System.out.println("=======================================================================\n");

        // 1. CREATE GRAPH
        System.out.println("1. CREATING GRAPH (6 Vertices)");
        Graph graph = new Graph(6);

        // Add weighted edges representing service network distances/costs
        // Vertices 0 to 5
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 4);
        graph.addEdge(1, 2, 2);
        graph.addEdge(1, 3, 6);
        graph.addEdge(2, 3, 8);
        graph.addEdge(2, 4, 9);
        graph.addEdge(3, 4, 7);
        graph.addEdge(3, 5, 5);
        graph.addEdge(4, 5, 10);

        System.out.println("Added 9 weighted edges to graph.");
        System.out.println("Total Vertices: " + graph.getNumVertices());
        System.out.println("Total Edges:    " + graph.getNumEdges() + "\n");

        // 2. DISPLAY REPRESENTATIONS
        System.out.println("2. GRAPH DUAL REPRESENTATIONS");
        graph.displayAdjacencyMatrix();
        System.out.println();
        graph.displayAdjacencyList();
        System.out.println();

        // 3. DYNAMIC VERTEX & EDGE OPERATIONS
        System.out.println("3. TESTING DYNAMIC VERTEX & EDGE OPERATIONS");
        int newVertexIndex = graph.addVertex();
        System.out.println("Dynamically added new Vertex with index: " + newVertexIndex);
        graph.addEdge(5, newVertexIndex, 3);
        graph.addEdge(4, newVertexIndex, 1);
        System.out.println("Added edges connecting to new Vertex " + newVertexIndex);
        System.out.println("Updated Total Vertices: " + graph.getNumVertices());
        System.out.println("Updated Total Edges:    " + graph.getNumEdges());

        System.out.println("\nRemoving edge (3, 4)...");
        boolean removed = graph.removeEdge(3, 4);
        System.out.println("Edge (3, 4) removal status: " + removed);
        System.out.println("Final Edges Count: " + graph.getNumEdges() + "\n");

        // 4. RUN KRUSKAL'S MST ALGORITHM
        System.out.println("4. EXECUTING KRUSKAL'S MINIMUM SPANNING TREE ALGORITHM");
        Kruskal.Result result = Kruskal.findMST(graph);

        // Display results
        result.display();

        System.out.println("=======================================================================");
        System.out.println("   DEMO COMPLETED SUCCESSFULLY");
        System.out.println("=======================================================================");
    }
}
