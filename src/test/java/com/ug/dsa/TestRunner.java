package com.ug.dsa;

import com.ug.dsa.algorithms.Kruskal;
import com.ug.dsa.algorithms.Prim;
import com.ug.dsa.datastructures.Edge;
import com.ug.dsa.datastructures.Graph;
import com.ug.dsa.datastructures.HashTable;

public class TestRunner {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   RUNNING GRAPH, KRUSKAL, PRIM & HASHTABLE TESTS");
        System.out.println("=================================================");

        int passed = 0;
        int failed = 0;

        // Test 1: Graph creation and edge addition
        try {
            Graph g = new Graph(3);
            g.addEdge(0, 1, 5);
            g.addEdge(1, 2, 10);
            assert g.getNumVertices() == 3;
            assert g.getNumEdges() == 2;
            assert g.getAdjacencyMatrix()[0][1] == 5;
            assert g.getAdjacencyMatrix()[1][0] == 5;
            assert g.getAdjacencyMatrix()[1][2] == 10;
            System.out.println("[PASS] Test 1: Graph creation and matrix/list population");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] Test 1: " + t.getMessage());
            failed++;
        }

        // Test 2: Dynamic vertex addition
        try {
            Graph g = new Graph(2);
            int v2 = g.addVertex();
            assert v2 == 2;
            assert g.getNumVertices() == 3;
            g.addEdge(2, 0, 7);
            assert g.getAdjacencyMatrix()[2][0] == 7;
            System.out.println("[PASS] Test 2: Dynamic vertex addition");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] Test 2: " + t.getMessage());
            failed++;
        }

        // Test 3: Edge removal
        try {
            Graph g = new Graph(3);
            g.addEdge(0, 1, 4);
            g.addEdge(1, 2, 8);
            boolean removed = g.removeEdge(0, 1);
            assert removed;
            assert g.getNumEdges() == 1;
            assert g.getAdjacencyMatrix()[0][1] == 0;
            System.out.println("[PASS] Test 3: Edge removal");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] Test 3: " + t.getMessage());
            failed++;
        }

        // Test 4: Kruskal MST calculation
        try {
            Graph g = new Graph(4);
            g.addEdge(0, 1, 10);
            g.addEdge(0, 2, 6);
            g.addEdge(0, 3, 5);
            g.addEdge(1, 3, 15);
            g.addEdge(2, 3, 4);

            Kruskal.Result result = Kruskal.findMST(g);
            assert result.getTotalWeight() == 19;
            assert result.getMstEdges().length == 3;
            assert result.isConnected();
            System.out.println("[PASS] Test 4: Kruskal MST standard calculation");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] Test 4: " + t.getMessage());
            failed++;
        }

        // Test 5: Kruskal Disconnected Graph
        try {
            Graph g = new Graph(4);
            g.addEdge(0, 1, 2);
            g.addEdge(2, 3, 3);
            Kruskal.Result result = Kruskal.findMST(g);
            assert result.getTotalWeight() == 5;
            assert !result.isConnected();
            System.out.println("[PASS] Test 5: Kruskal disconnected graph handling");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] Test 5: " + t.getMessage());
            failed++;
        }

        // Test 6: HashTable put, get, update, and remove
        try {
            HashTable<String, Integer> map = new HashTable<>();
            map.put("Key1", 100);
            map.put("Key2", 200);
            assert map.size() == 2;
            assert map.get("Key1") == 100;
            assert map.put("Key1", 150) == 100;
            assert map.get("Key1") == 150;
            assert map.remove("Key2") == 200;
            assert map.size() == 1;
            assert !map.containsKey("Key2");
            System.out.println("[PASS] Test 6: HashTable put, get, update, and remove");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] Test 6: " + t.getMessage());
            failed++;
        }

        // Test 7: HashTable collision handling & resizing
        try {
            HashTable<Integer, String> map = new HashTable<>(4, 0.75f);
            map.put(0, "A");
            map.put(4, "B"); // Collides with 0
            map.put(8, "C"); // Collides with 0 & 4, triggers resize
            assert map.get(0).equals("A");
            assert map.get(4).equals("B");
            assert map.get(8).equals("C");
            assert map.size() == 3;
            System.out.println("[PASS] Test 7: HashTable collision handling & dynamic resizing");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] Test 7: " + t.getMessage());
            failed++;
        }

        // Test 8: Prim MST calculation
        try {
            Graph g = new Graph(4);
            g.addEdge(0, 1, 10);
            g.addEdge(0, 2, 6);
            g.addEdge(0, 3, 5);
            g.addEdge(1, 3, 15);
            g.addEdge(2, 3, 4);

            Prim.Result result = Prim.findMST(g);
            assert result.getTotalWeight() == 19;
            assert result.getMstEdges().length == 3;
            assert result.isConnected();
            System.out.println("[PASS] Test 8: Prim MST standard calculation");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] Test 8: " + t.getMessage());
            failed++;
        }

        // Test 9: Prim Disconnected Graph
        try {
            Graph g = new Graph(4);
            g.addEdge(0, 1, 2);
            g.addEdge(2, 3, 3);
            Prim.Result result = Prim.findMST(g);
            assert result.getTotalWeight() == 2;
            assert !result.isConnected();
            System.out.println("[PASS] Test 9: Prim disconnected graph handling");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] Test 9: " + t.getMessage());
            failed++;
        }

        System.out.println("=================================================");
        System.out.printf("   SUMMARY: %d PASSED, %d FAILED%n", passed, failed);
        System.out.println("=================================================");
    }
}

