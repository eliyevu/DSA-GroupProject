package com.ug.dsa.datastructures;

/**
 * Custom implementation of a Disjoint Set (Union-Find) data structure.
 * Supports path compression and union by rank for near-constant time operations.
 */
public class DisjointSet {

    private int[] parent;
    private int[] rank;
    private int size;

    /**
     * Initializes a disjoint set of a given size.
     *
     * @param size the number of elements in the disjoint set
     */
    public DisjointSet(int size) {
        makeSet(size);
    }

    /**
     * Creates a new set for each element from 0 to size - 1.
     * Each element is initially its own parent with a rank of 0.
     *
     * @param size the number of elements
     */
    public void makeSet(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Size cannot be negative");
        }
        this.size = size;
        this.parent = new int[size];
        this.rank = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    /**
     * Finds the representative/root of the set containing element i.
     * Applies path compression to flatten the structure for faster future lookups.
     *
     * @param i the element to find the set representative for
     * @return the representative of the set containing i
     */
    public int find(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("Element index " + i + " is out of bounds for size " + size);
        }
        if (parent[i] == i) {
            return i;
        }
        // Path compression: update the parent to point directly to the representative root
        parent[i] = find(parent[i]);
        return parent[i];
    }

    /**
     * Merges the set containing element i with the set containing element j.
     * Applies union by rank to keep the tree depth minimal.
     *
     * @param i first element
     * @param j second element
     * @return true if the sets were successfully merged, false if they were already in the same set
     */
    public boolean union(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);

        if (rootI == rootJ) {
            return false; // Already in the same set, no merge occurred
        }

        // Union by rank: attach smaller depth tree under the root of the deeper tree
        if (rank[rootI] < rank[rootJ]) {
            parent[rootI] = rootJ;
        } else if (rank[rootI] > rank[rootJ]) {
            parent[rootJ] = rootI;
        } else {
            parent[rootJ] = rootI;
            rank[rootI]++;
        }
        return true;
    }

    /**
     * Returns the total number of elements in the disjoint set.
     *
     * @return size of the disjoint set
     */
    public int getSize() {
        return size;
    }
}
