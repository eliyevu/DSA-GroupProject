package com.ug.dsa.datastructures;

import java.util.Objects;

/**
 * Represents a weighted edge between two vertices in a graph.
 * Implements Comparable to support sorting edges by weight.
 */
public class Edge implements Comparable<Edge> {
    private final int src;
    private final int dest;
    private final int weight;

    /**
     * Constructs a weighted edge from src to dest.
     *
     * @param src    source vertex index
     * @param dest   destination vertex index
     * @param weight edge weight
     */
    public Edge(int src, int dest, int weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    /**
     * Constructs an unweighted edge (default weight 1) from src to dest.
     *
     * @param src  source vertex index
     * @param dest destination vertex index
     */
    public Edge(int src, int dest) {
        this(src, dest, 1);
    }

    public int getSrc() {
        return src;
    }

    public int getDest() {
        return dest;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return src == edge.src && dest == edge.dest && weight == edge.weight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, dest, weight);
    }

    @Override
    public String toString() {
        return "(" + src + " - " + dest + ", weight=" + weight + ")";
    }
}
