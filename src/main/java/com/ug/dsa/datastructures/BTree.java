package com.ug.dsa.datastructures;

/**
 * B-Tree implementation following the standard CLRS algorithm.
 *
 * Original owner: Roselyn Francis (Member 10)
 * Modified by: Amoah Edward Junior (Member 7) - made generic (key + value)
 * to support Team 4's Indexing/Search service (findLocation, findResource).
 *
 * WHY THIS CHANGED: the original BTree only stored raw int keys, with no
 * way to attach an associated object (e.g. a Location or Resource record)
 * to a key. Indexing needs "find the record with this id", not just
 * "does this id exist". The insert/search/split algorithm itself is
 * UNCHANGED from the original - only the data each node carries changed
 * (a value alongside each key).
 *
 * Public API: insert(K key, V value), search(K key), traverse()
 * Private methods: splitChild(), insertNonFull(), search(node, key)
 */
public class BTree<K extends Comparable<K>, V> {

    private BTreeNode<K, V> root;

    private int t;

    public BTree(int degree) {

        if (degree < 2) {
            throw new IllegalArgumentException("B-Tree degree must be at least 2.");
        }

        this.t = degree;

        root = null;
    }

    // ---------------------------------------------------------------
    // Search
    // ---------------------------------------------------------------

    /**
     * Return the value associated with the given key, or null if the
     * key is not present.
     */
    public V search(K key) {

        BTreeNode<K, V> node = searchNode(root, key);

        if (node == null) {
            return null;
        }

        int i = 0;
        while (i < node.numberOfKeys && key.compareTo(node.keys[i]) > 0) {
            i++;
        }

        return node.values[i];
    }

    /**
     * Check whether a key exists in the tree.
     */
    public boolean contains(K key) {
        return searchNode(root, key) != null;
    }

    private BTreeNode<K, V> searchNode(BTreeNode<K, V> node, K key) {

        if (node == null)
            return null;

        int i = 0;

        while (i < node.numberOfKeys && key.compareTo(node.keys[i]) > 0)
            i++;

        if (i < node.numberOfKeys && key.compareTo(node.keys[i]) == 0)
            return node;

        if (node.leaf)
            return null;

        return searchNode(node.children[i], key);

    }

    // ---------------------------------------------------------------
    // Insert
    // ---------------------------------------------------------------

    public void insert(K key, V value) {

        // Reject duplicate keys - this B-Tree stores each key at most once.
        if (contains(key)) {
            return;
        }

        if (root == null) {

            root = new BTreeNode<>(t, true);

            root.keys[0] = key;
            root.values[0] = value;

            root.numberOfKeys = 1;

            return;

        }

        if (root.numberOfKeys == 2 * t - 1) {

            BTreeNode<K, V> newRoot = new BTreeNode<>(t, false);

            newRoot.children[0] = root;

            splitChild(newRoot, 0);

            root = newRoot;

        }

        insertNonFull(root, key, value);

    }

    /**
     * Inserts a key/value into a subtree rooted at {@code node}, which is
     * guaranteed to NOT be full when this is called.
     */
    private void insertNonFull(BTreeNode<K, V> node, K key, V value) {

        int i = node.numberOfKeys - 1;

        if (node.leaf) {

            // shift keys/values right to find the insertion point, then insert
            while (i >= 0 && key.compareTo(node.keys[i]) < 0) {
                node.keys[i + 1] = node.keys[i];
                node.values[i + 1] = node.values[i];
                i--;
            }

            node.keys[i + 1] = key;
            node.values[i + 1] = value;
            node.numberOfKeys++;

        } else {

            // find child
            while (i >= 0 && key.compareTo(node.keys[i]) < 0) {
                i--;
            }
            i++;

            // if child full, split, then continue
            if (node.children[i].numberOfKeys == 2 * t - 1) {

                splitChild(node, i);

                if (key.compareTo(node.keys[i]) > 0) {
                    i++;
                }
            }

            insertNonFull(node.children[i], key, value);
        }
    }

    /**
     * Splits the full child at index {@code i} of {@code parent}.
     * Moves the median key/value up into parent, and the right half of the
     * keys/values/children into a brand new node.
     */
    private void splitChild(BTreeNode<K, V> parent, int i) {

        BTreeNode<K, V> fullChild = parent.children[i];
        BTreeNode<K, V> newChild = new BTreeNode<>(t, fullChild.leaf);

        newChild.numberOfKeys = t - 1;

        // move right keys/values into the new node
        for (int j = 0; j < t - 1; j++) {
            newChild.keys[j] = fullChild.keys[j + t];
            newChild.values[j] = fullChild.values[j + t];
        }

        // move right children into the new node, if not a leaf
        if (!fullChild.leaf) {
            for (int j = 0; j < t; j++) {
                newChild.children[j] = fullChild.children[j + t];
            }
        }

        K medianKey = fullChild.keys[t - 1];
        V medianValue = fullChild.values[t - 1];
        fullChild.numberOfKeys = t - 1;

        // shift parent's children right to make room for newChild
        for (int j = parent.numberOfKeys; j >= i + 1; j--) {
            parent.children[j + 1] = parent.children[j];
        }
        parent.children[i + 1] = newChild;

        // shift parent's keys/values right to make room for the median
        for (int j = parent.numberOfKeys - 1; j >= i; j--) {
            parent.keys[j + 1] = parent.keys[j];
            parent.values[j + 1] = parent.values[j];
        }
        parent.keys[i] = medianKey;
        parent.values[i] = medianValue;
        parent.numberOfKeys++;
    }

    // ---------------------------------------------------------------
    // Traverse (useful for demonstrations)
    // ---------------------------------------------------------------

    public void traverse() {

        traverse(root);

    }

    /** Recursive inorder traversal, prints keys in ascending order. */
    private void traverse(BTreeNode<K, V> node) {

        if (node == null) {
            return;
        }

        int i;
        for (i = 0; i < node.numberOfKeys; i++) {

            if (!node.leaf) {
                traverse(node.children[i]);
            }

            System.out.print(node.keys[i] + " ");
        }

        if (!node.leaf) {
            traverse(node.children[i]);
        }
    }
}
