package com.ug.dsa.datastructures;


public class BTree<K extends Comparable<K>, V> {

    private BTreeNode<K, V> root;

    private int t;

    // Creates an empty B-Tree with the specified minimum degree.
    public BTree(int degree) {

        if (degree < 2) {
            throw new IllegalArgumentException(
                "B-Tree degree must be at least 2."
            );
        }

        this.t = degree;
        this.root = null;
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

    /**
     * Recursive search operation - returns the node containing the key,
     * or null if not found.
     */
    private BTreeNode<K, V> searchNode(BTreeNode<K, V> node, K key) {

        if (node == null) {
            return null;
        }

        int i = 0;

        while (i < node.numberOfKeys && key.compareTo(node.keys[i]) > 0) {
            i++;
        }

        if (i < node.numberOfKeys && key.compareTo(node.keys[i]) == 0) {
            return node;
        }

        if (node.leaf) {
            return null;
        }

        return searchNode(node.children[i], key);
    }

    // ---------------------------------------------------------------
    // Insert
    // ---------------------------------------------------------------

    /**
     * Inserts a key/value pair into the B-Tree.
     *
     * Duplicate keys are ignored.
     */
    public void insert(K key, V value) {

        // Reject duplicate keys - this B-Tree stores each key at most once.
        if (contains(key)) {
            return;
        }

        // Empty tree.
        if (root == null) {

            root = new BTreeNode<>(t, true);

            root.keys[0] = key;
            root.values[0] = value;
            root.numberOfKeys = 1;

            return;
        }

        // If root is full, create a new root and split it.
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

            // Shift keys/values right to make room, then insert.
            while (i >= 0 && key.compareTo(node.keys[i]) < 0) {
                node.keys[i + 1] = node.keys[i];
                node.values[i + 1] = node.values[i];
                i--;
            }

            node.keys[i + 1] = key;
            node.values[i + 1] = value;
            node.numberOfKeys++;

        } else {

            // Find the appropriate child.
            while (i >= 0 && key.compareTo(node.keys[i]) < 0) {
                i--;
            }
            i++;

            // If child is full, split it first.
            if (node.children[i].numberOfKeys == 2 * t - 1) {

                splitChild(node, i);

                if (key.compareTo(node.keys[i]) > 0) {
                    i++;
                }
            }

            insertNonFull(node.children[i], key, value);
        }
    }

    // ---------------------------------------------------------------
    // Split Child
    // ---------------------------------------------------------------

    /**
     * Splits the full child at index {@code i} of {@code parent}.
     * Moves the median key/value up into parent, and the right half of
     * the keys/values/children into a brand new node.
     */
    private void splitChild(BTreeNode<K, V> parent, int i) {

        BTreeNode<K, V> fullChild = parent.children[i];
        BTreeNode<K, V> newChild = new BTreeNode<>(t, fullChild.leaf);

        newChild.numberOfKeys = t - 1;

        // Move the right half of the keys/values into the new node.
        for (int j = 0; j < t - 1; j++) {
            newChild.keys[j] = fullChild.keys[j + t];
            newChild.values[j] = fullChild.values[j + t];
        }

        // Move the right half of the children, if not a leaf.
        if (!fullChild.leaf) {
            for (int j = 0; j < t; j++) {
                newChild.children[j] = fullChild.children[j + t];
            }
        }

        K medianKey = fullChild.keys[t - 1];
        V medianValue = fullChild.values[t - 1];
        fullChild.numberOfKeys = t - 1;

        // Shift parent's children right to make room for newChild.
        for (int j = parent.numberOfKeys; j >= i + 1; j--) {
            parent.children[j + 1] = parent.children[j];
        }
        parent.children[i + 1] = newChild;

        // Shift parent's keys/values right to make room for the median.
        for (int j = parent.numberOfKeys - 1; j >= i; j--) {
            parent.keys[j + 1] = parent.keys[j];
            parent.values[j + 1] = parent.values[j];
        }
        parent.keys[i] = medianKey;
        parent.values[i] = medianValue;
        parent.numberOfKeys++;
    }

    // ---------------------------------------------------------------
    // Traverse
    // ---------------------------------------------------------------

    /**
     * Traverses the B-Tree in ascending order.
     * Provided primarily for demonstration purposes.
     */
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

    // ---------------------------------------------------------------
    // Nested B-Tree Node
    // ---------------------------------------------------------------

    /**
     * Node used internally by the B-Tree. Kept as a private static
     * nested class (Roselyn's structural choice) rather than a
     * separate BTreeNode.java file. Stores a value alongside each key
     * so the tree can be used as a key-value index, not just a
     * membership-check structure.
     */
    private static class BTreeNode<K extends Comparable<K>, V> {

        private K[] keys;
        private V[] values;
        private BTreeNode<K, V>[] children;
        private int numberOfKeys;
        private boolean leaf;

        /**
         * Creates a B-Tree node. Arrays are created manually - no Java
         * collection classes are used.
         */
        @SuppressWarnings("unchecked")
        private BTreeNode(int degree, boolean leaf) {

            this.leaf = leaf;
            this.numberOfKeys = 0;

            this.keys = (K[]) new Comparable[2 * degree - 1];
            this.values = (V[]) new Object[2 * degree - 1];
            this.children = (BTreeNode<K, V>[]) new BTreeNode[2 * degree];
        }
    }
}
