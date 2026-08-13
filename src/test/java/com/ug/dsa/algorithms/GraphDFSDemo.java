package com.ug.dsa.algorithms;

import java.util.List;

public class GraphDFSDemo {

    public static void main(String[] args) {


        GraphDFS<Integer> graph = new GraphDFS<>();

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(2, 5);
        graph.addEdge(3, 5);
        graph.addEdge(6, 7);

        System.out.println("Vertices:                     " + graph.vertices());
        System.out.println("Vertex count:                 " + graph.vertexCount());
        System.out.println("Edge count (directed pairs):  " + graph.edgeCount());

        System.out.println();
        System.out.println("------------ TRAVERSAL ------------");
        System.out.println("Recursive DFS from 1:        " + graph.dfsRecursive(1));
        System.out.println("Iterative DFS from 1:        " + graph.dfsIterative(1));

        System.out.println();
        System.out.println("------------ ADJACENCY (per vertex) ------------");
        for (int v : graph.vertices()) {
            System.out.println("  " + v + " -> " + graph.neighbors(v));
        }

        System.out.println();
        System.out.println("------------ PATH CHECKS ------------");
        int[][] checks = {{1, 4}, {1, 5}, {1, 7}, {6, 1}};
        for (int[] c : checks) {
            System.out.println("hasPath(" + c[0] + " -> " + c[1] + "):" + " ".repeat(3)
                + graph.hasPath(c[0], c[1]));
        }

        System.out.println();
        System.out.println("------------ CONNECTED COMPONENTS ------------");
        List<List<Integer>> components = graph.connectedComponents();
        for (int i = 0; i < components.size(); i++) {
            System.out.println("  Component " + i + ": " + components.get(i));
        }

        System.out.println();
        System.out.println("========== DIRECTED GRAPH + CYCLE-LIKE STRUCTURE (Integer) ==========");
        GraphDFS<Integer> directed = new GraphDFS<>();
        directed.addDirectedEdge(1, 2);
        directed.addDirectedEdge(2, 3);
        directed.addDirectedEdge(3, 1); 
        directed.addDirectedEdge(3, 4);

        System.out.println("Recursive DFS from 1:        " + directed.dfsRecursive(1));
        System.out.println("Iterative DFS from 1:        " + directed.dfsIterative(1));
        System.out.println("hasPath(1 -> 4):             " + directed.hasPath(1, 4));
        System.out.println("hasPath(4 -> 1):             " + directed.hasPath(4, 1));

        System.out.println();
        System.out.println("========== GENERIC TYPE CHECK: GraphDFS<String> (city map) ==========");
        GraphDFS<String> cities = new GraphDFS<>();
        cities.addEdge("Accra", "Kumasi");
        cities.addEdge("Accra", "Tema");
        cities.addEdge("Kumasi", "Tamale");
        cities.addEdge("Tema", "Aflao");
        cities.addEdge("Takoradi", "Cape Coast"); 

        System.out.println("Vertices:                    " + cities.vertices());
        System.out.println("Recursive DFS from Accra:    " + cities.dfsRecursive("Accra"));
        System.out.println("Iterative DFS from Accra:    " + cities.dfsIterative("Accra"));
        System.out.println("hasPath(Accra -> Tamale):    " + cities.hasPath("Accra", "Tamale"));
        System.out.println("hasPath(Accra -> Takoradi):  " + cities.hasPath("Accra", "Takoradi"));
        System.out.println("Connected components:        " + cities.connectedComponents());

        System.out.println();
        System.out.println("========== LARGER GRAPH (recursive vs iterative order) ==========");
        GraphDFS<Integer> larger = new GraphDFS<>();
        int[][] edges = {
            {1, 2}, {1, 3}, {1, 4},
            {2, 5}, {2, 6},
            {4, 7}, {4, 8},
            {7, 9}, {8, 9},
            {10, 11} 
        };
        for (int[] e : edges) larger.addEdge(e[0], e[1]);

        System.out.println("Recursive DFS from 1:        " + larger.dfsRecursive(1));
        System.out.println("Iterative DFS from 1:        " + larger.dfsIterative(1));
        System.out.println("Connected components:        " + larger.connectedComponents());

    }
}