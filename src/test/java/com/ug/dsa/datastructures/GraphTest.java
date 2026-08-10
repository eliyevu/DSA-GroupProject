package com.ug.dsa.datastructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    private Graph graph;

    @BeforeEach
    public void setUp() {
        graph = new Graph(4, false); // Undirected graph with 4 vertices
    }

    @Test
    public void testInitialState() {
        assertEquals(4, graph.getNumVertices());
        assertEquals(0, graph.getNumEdges());
        assertFalse(graph.isDirected());
    }

    @Test
    public void testAddEdgeUndirected() {
        graph.addEdge(0, 1, 5);
        assertEquals(1, graph.getNumEdges());

        int[][] matrix = graph.getAdjacencyMatrix();
        assertEquals(5, matrix[0][1]);
        assertEquals(5, matrix[1][0]);

        Edge[] neighbours0 = graph.getNeighbours(0);
        assertEquals(1, neighbours0.length);
        assertEquals(1, neighbours0[0].getDest());
        assertEquals(5, neighbours0[0].getWeight());

        Edge[] neighbours1 = graph.getNeighbours(1);
        assertEquals(1, neighbours1.length);
        assertEquals(0, neighbours1[0].getDest());
        assertEquals(5, neighbours1[0].getWeight());
    }

    @Test
    public void testAddEdgeDirected() {
        Graph directedGraph = new Graph(3, true);
        directedGraph.addEdge(0, 1, 10);

        assertEquals(1, directedGraph.getNumEdges());
        int[][] matrix = directedGraph.getAdjacencyMatrix();
        assertEquals(10, matrix[0][1]);
        assertEquals(0, matrix[1][0]);
    }

    @Test
    public void testAddVertexDynamically() {
        int newV = graph.addVertex();
        assertEquals(4, newV);
        assertEquals(5, graph.getNumVertices());

        graph.addEdge(4, 0, 7);
        assertEquals(1, graph.getNumEdges());
        assertEquals(7, graph.getAdjacencyMatrix()[4][0]);
    }

    @Test
    public void testRemoveEdge() {
        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 3);
        assertEquals(2, graph.getNumEdges());

        boolean removed = graph.removeEdge(0, 1);
        assertTrue(removed);
        assertEquals(1, graph.getNumEdges());
        assertEquals(0, graph.getAdjacencyMatrix()[0][1]);

        boolean removeNonExisting = graph.removeEdge(0, 1);
        assertFalse(removeNonExisting);
    }

    @Test
    public void testRemoveVertex() {
        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 3);
        graph.addEdge(2, 3, 2);

        graph.removeVertex(1); // Remove vertex 1
        assertEquals(3, graph.getNumVertices());
        assertEquals(1, graph.getNumEdges()); // Only (2, 3) remains (now reindexed)
    }

    @Test
    public void testInvalidVertexThrowsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> graph.addEdge(0, 99, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> graph.getNeighbours(-1));
    }
}
