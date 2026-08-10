package com.ug.dsa.datastructures;

/**
 * Generic B-Tree implementation following the standard CLRS algorithm.
 *
 * Owner: Roselyn Francis (Member 10)
 *
 * Week One scope:
 * - Insertion
 * - Searching
 * - Traversal for demonstration purposes
 *
 * The implementation is fully custom and uses arrays only.
 * No Java collection classes are used.
 *
 * @param <T> the type of values stored in the B-Tree.
 *            T must implement Comparable<T>.
 */
public class BTree<T extends Comparable<T>> {

    private BTreeNode<T> root;

    private int t;

    /**
     * Creates an empty B-Tree with the specified minimum degree.
     *
     * @param degree minimum degree of the B-Tree
     */
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
     * Searches for a value in the B-Tree.
     *
     * @param key value to search for
     * @return true if the value exists, false otherwise
     */
    public boolean search(T key) {
        return search(root, key) != null;
    }

    /**
     * Recursive search operation.
     */
    private BTreeNode<T> search(BTreeNode<T> node, T key) {

        if (node == null) {
            return null;
        }

        int i = 0;

        while (i < node.numberOfKeys
                && key.compareTo(node.keys[i]) > 0) {
            i++;
        }

        if (i < node.numberOfKeys
                && key.compareTo(node.keys[i]) == 0) {
            return node;
        }

        if (node.leaf) {
            return null;
        }

        return search(node.children[i], key);
    }

    // ---------------------------------------------------------------
    // Insert
    // ---------------------------------------------------------------

    /**
     * Inserts a value into the B-Tree.
     *
     * Duplicate values are ignored.
     *
     * @param key value to insert
     */
    public void insert(T key) {

        // Reject duplicates.
        if (search(key)) {
            return;
        }

        // Empty tree.
        if (root == null) {

            root = new BTreeNode<>(t, true);

            root.keys[0] = key;
            root.numberOfKeys = 1;

            return;
        }

        // If root is full, create a new root and split it.
        if (root.numberOfKeys == 2 * t - 1) {

            BTreeNode<T> newRoot = new BTreeNode<>(t, false);

            newRoot.children[0] = root;

            splitChild(newRoot, 0);

            root = newRoot;
        }

        insertNonFull(root, key);
    }

    /**
     * Inserts a value into a subtree whose root is not full.
     */
    private void insertNonFull(BTreeNode<T> node, T key) {

        int i = node.numberOfKeys - 1;

        if (node.leaf) {

            // Shift keys to the right to make room.
            while (i >= 0
                    && key.compareTo(node.keys[i]) < 0) {

                node.keys[i + 1] = node.keys[i];
                i--;
            }

            node.keys[i + 1] = key;
            node.numberOfKeys++;

        } else {

            // Find the appropriate child.
            while (i >= 0
                    && key.compareTo(node.keys[i]) < 0) {
                i--;
            }

            i++;

            // If child is full, split it.
            if (node.children[i].numberOfKeys == 2 * t - 1) {

                splitChild(node, i);

                if (key.compareTo(node.keys[i]) > 0) {
                    i++;
                }
            }

            insertNonFull(node.children[i], key);
        }
    }

    // ---------------------------------------------------------------
    // Split Child
    // ---------------------------------------------------------------

    /**
     * Splits a full child of the parent node.
     *
     * The median value moves into the parent.
     * The right half moves into a newly created node.
     */
    private void splitChild(BTreeNode<T> parent, int i) {

        BTreeNode<T> fullChild = parent.children[i];

        BTreeNode<T> newChild =
                new BTreeNode<>(t, fullChild.leaf);

        newChild.numberOfKeys = t - 1;

        // Move the right half of the keys.
        for (int j = 0; j < t - 1; j++) {

            newChild.keys[j] =
                    fullChild.keys[j + t];
        }

        // Move the right half of the children.
        if (!fullChild.leaf) {

            for (int j = 0; j < t; j++) {

                newChild.children[j] =
                        fullChild.children[j + t];
            }
        }

        // Move the median key to the parent.
        T medianKey = fullChild.keys[t - 1];

        fullChild.numberOfKeys = t - 1;

        // Shift parent's children to make room.
        for (int j = parent.numberOfKeys;
             j >= i + 1;
             j--) {

            parent.children[j + 1] =
                    parent.children[j];
        }

        parent.children[i + 1] = newChild;

        // Shift parent's keys to make room for median.
        for (int j = parent.numberOfKeys - 1;
             j >= i;
             j--) {

            parent.keys[j + 1] =
                    parent.keys[j];
        }

        parent.keys[i] = medianKey;

        parent.numberOfKeys++;
    }

    // ---------------------------------------------------------------
    // Traverse
    // ---------------------------------------------------------------

    /**
     * Traverses the B-Tree in ascending order.
     *
     * This method is provided primarily for demonstration purposes.
     */
    public void traverse() {
        traverse(root);
    }

    /**
     * Recursive traversal.
     */
    private void traverse(BTreeNode<T> node) {

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
     * Node used internally by the B-Tree.
     *
     * This is a static nested class so BTreeNode is no longer
     * maintained as a separate public class/file.
     */
    private static class BTreeNode<T extends Comparable<T>> {

        private T[] keys;

        private BTreeNode<T>[] children;

        private int numberOfKeys;

        private boolean leaf;

        /**
         * Creates a B-Tree node.
         *
         * The arrays are created manually and no Java collection
         * classes are used.
         */
        @SuppressWarnings("unchecked")
        private BTreeNode(int degree, boolean leaf) {

            this.leaf = leaf;

            this.numberOfKeys = 0;

            this.keys = (T[]) new Comparable[2 * degree - 1];

            this.children =
                    (BTreeNode<T>[]) new BTreeNode[2 * degree];
        }
    }
}