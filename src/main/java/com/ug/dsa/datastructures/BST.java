package com.ug.dsa.datastructures;

/**
 * Custom integer Binary Search Tree used by the indexing layer.
 */
public class BST {

    private static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
        }
    }

    private Node root;

    public void insert(int value) {
        root = insertRecursive(root, value);
    }

    private Node insertRecursive(Node node, int value) {
        if (node == null) return new Node(value);
        if (value < node.value) node.left = insertRecursive(node.left, value);
        else if (value > node.value) node.right = insertRecursive(node.right, value);
        return node;
    }

    public boolean search(int value) {
        Node current = root;
        while (current != null) {
            if (value == current.value) return true;
            current = value < current.value ? current.left : current.right;
        }
        return false;
    }

    public DynamicArray<Integer> inorderTraversal() {
        DynamicArray<Integer> result = new DynamicArray<>();
        inorderRecursive(root, result);
        return result;
    }

    private void inorderRecursive(Node node, DynamicArray<Integer> result) {
        if (node == null) return;
        inorderRecursive(node.left, result);
        result.add(node.value);
        inorderRecursive(node.right, result);
    }

    public boolean isEmpty() {
        return root == null;
    }
}
