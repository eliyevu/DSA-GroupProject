package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.Graph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PrimTest {

    private Graph graph;

    @BeforeEach
    public void setUp() {
        graph = new Graph(4, false); // Undirected graph with 4 vertices
    }

    @Test
    public void testStandardMST() {
        // Graph with 4 vertices:
        // 0-1 (w=10), 0-2 (w=6), 0-3 (w=5), 1-3 (w=15), 2-3 (w=4)
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 6);
        graph.addEdge(0, 3, 5);
        graph.addEdge(1, 3, 15);
        graph.addEdge(2, 3, 4);

        Prim.Result result = Prim.findMST(graph);
        assertNotNull(result);
        assertEquals(19, result.getTotalWeight()); // Edges: (2-3 w=4), (0-3 w=5), (0-1 w=10) -> total 19
        assertEquals(3, result.getMstEdges().length);
        assertTrue(result.isConnected());
    }

    @Test
    public void testPrimMatchesKruskalTotalWeight() {
        Graph g = new Graph(5, false);
        g.addEdge(0, 1, 2);
        g.addEdge(0, 3, 6);
        g.addEdge(1, 2, 3);
        g.addEdge(1, 3, 8);
        g.addEdge(1, 4, 5);
        g.addEdge(2, 4, 7);
        g.addEdge(3, 4, 9);

        Prim.Result primResult = Prim.findMST(g);
        Kruskal.Result kruskalResult = Kruskal.findMST(g);

        assertEquals(kruskalResult.getTotalWeight(), primResult.getTotalWeight());
        assertEquals(kruskalResult.getMstEdges().length, primResult.getMstEdges().length);
        assertTrue(primResult.isConnected());
    }

    @Test
    public void testDifferentStartVertex() {
        graph.addEdge(0, 1, 4);
        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 3, 3);
        graph.addEdge(0, 3, 8);

        Prim.Result resultFrom0 = Prim.findMST(graph, 0);
        Prim.Result resultFrom2 = Prim.findMST(graph, 2);

        assertEquals(9, resultFrom0.getTotalWeight());
        assertEquals(9, resultFrom2.getTotalWeight());
        assertTrue(resultFrom0.isConnected());
        assertTrue(resultFrom2.isConnected());
    }

    @Test
    public void testDisconnectedGraph() {
        // Vertices 0-1 connected, vertices 2-3 connected
        graph.addEdge(0, 1, 3);
        graph.addEdge(2, 3, 7);

        Prim.Result result = Prim.findMST(graph, 0);
        assertEquals(3, result.getTotalWeight());
        assertEquals(1, result.getMstEdges().length);
        assertFalse(result.isConnected());
    }

    @Test
    public void testSingleVertexGraph() {
        Graph singleV = new Graph(1, false);
        Prim.Result result = Prim.findMST(singleV);

        assertEquals(0, result.getTotalWeight());
        assertEquals(0, result.getMstEdges().length);
        assertTrue(result.isConnected());
    }

    @Test
    public void testEmptyGraph() {
        Graph emptyG = new Graph(0, false);
        Prim.Result result = Prim.findMST(emptyG);

        assertEquals(0, result.getTotalWeight());
        assertEquals(0, result.getMstEdges().length);
        assertTrue(result.isConnected());
    }

    @Test
    public void testInvalidInputThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Prim.findMST(null));
        assertThrows(IllegalArgumentException.class, () -> Prim.findMST(graph, -1));
        assertThrows(IllegalArgumentException.class, () -> Prim.findMST(graph, 10));
    }

    @Test
    public void testResultDisplay() {
        graph.addEdge(0, 1, 5);
        Prim.Result result = Prim.findMST(graph);
        assertDoesNotThrow(result::display);
    }
}
