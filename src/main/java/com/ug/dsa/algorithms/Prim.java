package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.Edge;
import com.ug.dsa.datastructures.Graph;
import com.ug.dsa.datastructures.Heap;

/**
 * Implementation of Prim's Minimum Spanning Tree (MST) Algorithm.
 * Uses the team's custom Graph, Edge, and Heap (Min Heap) data structures.
 */
public class Prim {

    /**
     * Encapsulates the results of Prim's Minimum Spanning Tree algorithm execution.
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
            System.out.println("================ PRIM'S MST RESULTS ================");
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
            System.out.println("====================================================");
        }
    }

    /**
     * Solves the Minimum Spanning Tree problem for the given Graph starting at vertex 0.
     *
     * @param graph input Graph instance
     * @return Result containing the MST edges, total weight, and connectivity status
     */
    public static Result findMST(Graph graph) {
        return findMST(graph, 0);
    }

    /**
     * Solves the Minimum Spanning Tree problem for the given Graph starting at the specified vertex.
     *
     * @param graph       input Graph instance
     * @param startVertex vertex index to begin Prim's algorithm
     * @return Result containing the MST edges, total weight, and connectivity status
     */
    public static Result findMST(Graph graph, int startVertex) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null.");
        }

        int numVertices = graph.getNumVertices();
        if (numVertices == 0) {
            return new Result(new Edge[0], 0, true);
        }

        if (startVertex < 0 || startVertex >= numVertices) {
            throw new IllegalArgumentException(
                "Invalid start vertex: " + startVertex + " (graph has " + numVertices + " vertices)"
            );
        }

        int[] key = new int[numVertices];
        Edge[] parentEdge = new Edge[numVertices];
        boolean[] inMST = new boolean[numVertices];

        for (int i = 0; i < numVertices; i++) {
            key[i] = Integer.MAX_VALUE;
        }

        Heap<Integer> minHeap = new Heap<>();
        key[startVertex] = 0;
        minHeap.insert(startVertex, 0);

        int visitedCount = 0;
        int totalWeight = 0;

        while (!minHeap.isEmpty()) {
            int u = minHeap.extractMin();

            if (inMST[u]) {
                continue;
            }

            inMST[u] = true;
            visitedCount++;

            if (parentEdge[u] != null) {
                totalWeight += parentEdge[u].getWeight();
            }

            for (Edge edge : graph.getNeighbours(u)) {
                int v = edge.getDest();
                int weight = edge.getWeight();

                if (!inMST[v] && weight < key[v]) {
                    key[v] = weight;
                    parentEdge[v] = edge;

                    if (minHeap.contains(v)) {
                        minHeap.decreaseKey(v, weight);
                    } else {
                        minHeap.insert(v, weight);
                    }
                }
            }
        }

        // Count MST edges selected
        int edgeCount = 0;
        for (int i = 0; i < numVertices; i++) {
            if (parentEdge[i] != null) {
                edgeCount++;
            }
        }

        Edge[] mstEdges = new Edge[edgeCount];
        int index = 0;
        for (int i = 0; i < numVertices; i++) {
            if (parentEdge[i] != null) {
                mstEdges[index++] = parentEdge[i];
            }
        }

        boolean isConnected = (visitedCount == numVertices);
        return new Result(mstEdges, totalWeight, isConnected);
    }
}
