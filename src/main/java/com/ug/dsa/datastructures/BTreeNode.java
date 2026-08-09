package com.ug.dsa.datastructures;

/**
 * A single node in the B-Tree.
 *
 * Owner: Roselyn Francis (Member 10)
 */
public class BTreeNode {

    int[] keys;
    int t;

    BTreeNode[] children;

    int numberOfKeys;

    boolean leaf;

    public BTreeNode(int t, boolean leaf) {
        this.t = t;
        this.leaf = leaf;

        keys = new int[2 * t - 1];
        children = new BTreeNode[2 * t];

        numberOfKeys = 0;
    }
}
