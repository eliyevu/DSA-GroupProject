package com.ug.dsa.datastructures;

public class BST {

    private class Node {
        int value;
        Node left, right;

        Node(int value) {
            this.value = value;
        }
    }

    private Node root;

    private Node insertRecursive(Node node, int value) {
        if (node == null) {
            return new Node(value);
        }
        if (value < node.value) {
            node.left = insertRecursive(node.left, value);
        } else if (value > node.value) {
            node.right = insertRecursive(node.right, value);
        }
        return node;
    }   public void insert(int value) {
        root = insertRecursive(root, value);
    }

    public boolean search(int value) {
        return searchRecursive(root, value);
    }

    private boolean searchRecursive(Node node, int value) {
        if (node == null) {
            return false;
        }
        if (value == node.value) {
            return true;
        }
        return value < node.value
                ? searchRecursive(node.left, value)
                : searchRecursive(node.right, value);
    }
    public java.util.List<Integer> inorderTraversal() {
        java.util.List<Integer> result = new java.util.ArrayList<>();
        inorderRecursive(root, result);
        return result;
    }

    private void inorderRecursive(Node node, java.util.List<Integer> result) {
        if (node != null) {
            inorderRecursive(node.left, result);
            result.add(node.value);
            inorderRecursive(node.right, result);
        }
    }
    public static void main(String[] args) {
        BST bst = new BST();
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int v : values) {
            bst.insert(v);
        }

        System.out.println("Inorder traversal: " + bst.inorderTraversal());
        System.out.println("Search 40: " + bst.search(40));
        System.out.println("Search 100: " + bst.search(100));
    }
}//<- First } closes main, second } closes public class BST
