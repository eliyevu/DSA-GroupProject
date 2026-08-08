package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.DisjointSet;
import com.ug.dsa.datastructures.Edge;
import com.ug.dsa.datastructures.Graph;

/**
 * Implementation of Kruskal's Minimum Spanning Tree (MST) Algorithm.
 * Uses the team's custom Graph and DisjointSet data structures and custom QuickSort for edges.
 */
public class Kruskal {

    /**
     * Encapsulates the results of Kruskal's Minimum Spanning Tree algorithm execution.
     */
    public static class Result {
        private final Edge[] mstEdges;
        private final int totalWeight;
        private final boolean isConnected;

        public Result(Edge[] mstEdges, int totalWeight, boolean isConnected) {
            this.mstEdges = mstEdges;
            this.totalWeight = totalWeight;
            this.isConnected = isConnected;
        }

        public Edge[] getMstEdges() {
            return mstEdges;
        }

        public int getTotalWeight() {
            return totalWeight;
        }

        public boolean isConnected() {
            return isConnected;
        }

        public void display() {
            System.out.println("================ KRUSKAL'S MST RESULTS ================");
            System.out.println("Total MST Weight: " + totalWeight);
            System.out.println("Is Graph Connected: " + isConnected);
            System.out.println("Edges in Minimum Spanning Tree:");
            if (mstEdges.length == 0) {
                System.out.println("  (No edges in MST)");
            } else {
                for (int i = 0; i < mstEdges.length; i++) {
                    System.out.println("  " + (i + 1) + ". " + mstEdges[i]);
                }
            }
            System.out.println("======================================================");
        }
    }

    /**
     * Solves the Minimum Spanning Tree problem for the given Graph using Kruskal's algorithm.
     *
     * @param graph input Graph instance
     * @return Result containing the MST edges, total weight, and connectivity status
     */
    public static Result findMST(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null.");
        }

        int numVertices = graph.getNumVertices();
        if (numVertices == 0) {
            return new Result(new Edge[0], 0, true);
        }

        // 1. Extract all unique edges from the graph
        Edge[] edges = graph.getAllEdges();

        // 2. Sort edges by weight ascending using custom QuickSort
        quickSortEdges(edges, 0, edges.length - 1);

        // 3. Initialize custom DisjointSet for cycle detection
        DisjointSet ds = new DisjointSet(numVertices);

        // 4. Select edges for MST
        Edge[] tempMst = new Edge[numVertices > 0 ? numVertices - 1 : 0];
        int mstEdgeCount = 0;
        int totalWeight = 0;

        for (int i = 0; i < edges.length; i++) {
            Edge edge = edges[i];
            int u = edge.getSrc();
            int v = edge.getDest();

            // Check if adding edge creates a cycle
            if (ds.find(u) != ds.find(v)) {
                ds.union(u, v);
                if (mstEdgeCount < tempMst.length) {
                    tempMst[mstEdgeCount++] = edge;
                }
                totalWeight += edge.getWeight();

                // Stop early if we have V - 1 edges
                if (mstEdgeCount == numVertices - 1) {
                    break;
                }
            }
        }

        // 5. Trim result array to exact number of MST edges selected
        Edge[] mstEdges = new Edge[mstEdgeCount];
        System.arraycopy(tempMst, 0, mstEdges, 0, mstEdgeCount);

        boolean isConnected = (numVertices == 0 || numVertices == 1 || mstEdgeCount == numVertices - 1);
        return new Result(mstEdges, totalWeight, isConnected);
    }

    /**
     * Custom QuickSort implementation for sorting Edge objects by weight ascending.
     *
     * @param arr  array of Edge objects
     * @param low  starting index
     * @param high ending index
     */
    private static void quickSortEdges(Edge[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSortEdges(arr, low, pi - 1);
            quickSortEdges(arr, pi + 1, high);
        }
    }

    private static int partition(Edge[] arr, int low, int high) {
        Edge pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j].compareTo(pivot) <= 0) {
                i++;
                Edge temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        Edge temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }
}
