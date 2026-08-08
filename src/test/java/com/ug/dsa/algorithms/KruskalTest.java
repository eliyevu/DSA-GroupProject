package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.Edge;
import com.ug.dsa.datastructures.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KruskalTest {

    @Test
    public void testStandardMST() {
        Graph g = new Graph(4);
        g.addEdge(0, 1, 10);
        g.addEdge(0, 2, 6);
        g.addEdge(0, 3, 5);
        g.addEdge(1, 3, 15);
        g.addEdge(2, 3, 4);

        Kruskal.Result result = Kruskal.findMST(g);

        assertEquals(19, result.getTotalWeight()); // (2-3 w=4) + (0-3 w=5) + (0-1 w=10) = 19
        assertEquals(3, result.getMstEdges().length);
        assertTrue(result.isConnected());
    }

    @Test
    public void testDisconnectedGraph() {
        Graph g = new Graph(4);
        g.addEdge(0, 1, 2);
        g.addEdge(2, 3, 3);

        Kruskal.Result result = Kruskal.findMST(g);

        assertEquals(5, result.getTotalWeight());
        assertEquals(2, result.getMstEdges().length);
        assertFalse(result.isConnected());
    }

    @Test
    public void testSingleVertexGraph() {
        Graph g = new Graph(1);
        Kruskal.Result result = Kruskal.findMST(g);

        assertEquals(0, result.getTotalWeight());
        assertEquals(0, result.getMstEdges().length);
        assertTrue(result.isConnected());
    }

    @Test
    public void testCompleteGraphMST() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 2);
        g.addEdge(0, 2, 3);

        Kruskal.Result result = Kruskal.findMST(g);

        assertEquals(3, result.getTotalWeight()); // Edges 1 and 2
        assertEquals(2, result.getMstEdges().length);
        assertTrue(result.isConnected());
    }
}
