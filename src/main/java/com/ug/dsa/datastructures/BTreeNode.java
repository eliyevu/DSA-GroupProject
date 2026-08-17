package com.ug.dsa.datastructures;

/**
 * A single node in the B-Tree.
 *
 * Original owner: Roselyn Francis (Member 10)
 * Modified by: Amoah Edward Junior (Member 7) - made generic (key + value)
 * to support Team 4's Indexing/Search service, which needs each key to
 * carry an associated value (e.g. a Location or Resource object), not
 * just a raw int. Algorithm/structure is otherwise unchanged from the
 * original.
 *
 * @param <K> key type, must be Comparable so keys can be ordered
 * @param <V> value type associated with each key
 */
public class BTreeNode<K extends Comparable<K>, V> {

    K[] keys;
    V[] values;
    int t;

    BTreeNode<K, V>[] children;

    int numberOfKeys;

    boolean leaf;

    @SuppressWarnings("unchecked")
    public BTreeNode(int t, boolean leaf) {
        this.t = t;
        this.leaf = leaf;

        keys = (K[]) new Comparable[2 * t - 1];
        values = (V[]) new Object[2 * t - 1];
        children = new BTreeNode[2 * t];

        numberOfKeys = 0;
    }
}
