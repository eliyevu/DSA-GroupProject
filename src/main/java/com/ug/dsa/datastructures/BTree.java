package com.ug.dsa.datastructures;

/**
 * B-Tree implementation following the standard CLRS algorithm.
 *
 * Owner: Roselyn Francis (Member 10)
 * Week One scope: insertion + searching (traverse included for demo purposes).
 *
 * Public API: insert(int key), search(int key), traverse()
 * Private methods: splitChild(), insertNonFull(), search(node, key)
 */
public class BTree {

    private BTreeNode root;

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

    public boolean search(int key) {

        return search(root, key) != null;

    }

    private BTreeNode search(BTreeNode node, int key) {

        if (node == null)
            return null;

        int i = 0;

        while (i < node.numberOfKeys && key > node.keys[i])
            i++;

        if (i < node.numberOfKeys && node.keys[i] == key)
            return node;

        if (node.leaf)
            return null;

        return search(node.children[i], key);

    }

    // ---------------------------------------------------------------
    // Insert
    // ---------------------------------------------------------------

    public void insert(int key) {

        // Reject duplicates - this B-Tree stores each key at most once.
        if (search(key)) {
            return;
        }

        if (root == null) {

            root = new BTreeNode(t, true);

            root.keys[0] = key;

            root.numberOfKeys = 1;

            return;

        }

        if (root.numberOfKeys == 2 * t - 1) {

            BTreeNode newRoot = new BTreeNode(t, false);

            newRoot.children[0] = root;

            splitChild(newRoot, 0);

            root = newRoot;

        }

        insertNonFull(root, key);

    }

    /**
     * Inserts a key into a subtree rooted at {@code node}, which is
     * guaranteed to NOT be full when this is called.
     */
    private void insertNonFull(BTreeNode node, int key) {

        int i = node.numberOfKeys - 1;

        if (node.leaf) {

            // shift keys right to find the insertion point, then insert
            while (i >= 0 && key < node.keys[i]) {
                node.keys[i + 1] = node.keys[i];
                i--;
            }

            node.keys[i + 1] = key;
            node.numberOfKeys++;

        } else {

            // find child
            while (i >= 0 && key < node.keys[i]) {
                i--;
            }
            i++;

            // if child full, split, then continue
            if (node.children[i].numberOfKeys == 2 * t - 1) {

                splitChild(node, i);

                if (key > node.keys[i]) {
                    i++;
                }
            }

            insertNonFull(node.children[i], key);
        }
    }

    /**
     * Splits the full child at index {@code i} of {@code parent}.
     * Moves the median key up into parent, and the right half of the
     * keys/children into a brand new node.
     */
    private void splitChild(BTreeNode parent, int i) {

        BTreeNode fullChild = parent.children[i];
        BTreeNode newChild = new BTreeNode(t, fullChild.leaf);

        newChild.numberOfKeys = t - 1;

        // move right keys into the new node
        for (int j = 0; j < t - 1; j++) {
            newChild.keys[j] = fullChild.keys[j + t];
        }

        // move right children into the new node, if not a leaf
        if (!fullChild.leaf) {
            for (int j = 0; j < t; j++) {
                newChild.children[j] = fullChild.children[j + t];
            }
        }

        int medianKey = fullChild.keys[t - 1];
        fullChild.numberOfKeys = t - 1;

        // shift parent's children right to make room for newChild
        for (int j = parent.numberOfKeys; j >= i + 1; j--) {
            parent.children[j + 1] = parent.children[j];
        }
        parent.children[i + 1] = newChild;

        // shift parent's keys right to make room for the median
        for (int j = parent.numberOfKeys - 1; j >= i; j--) {
            parent.keys[j + 1] = parent.keys[j];
        }
        parent.keys[i] = medianKey;
        parent.numberOfKeys++;
    }

    // ---------------------------------------------------------------
    // Traverse (useful for demonstrations)
    // ---------------------------------------------------------------

    public void traverse() {

        traverse(root);

    }

    /** Recursive inorder traversal, prints keys in ascending order. */
    private void traverse(BTreeNode node) {

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
