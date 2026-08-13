package com.ug.dsa.datastructures;

import java.util.ArrayList;
import java.util.List;


public class RedBlackTree<T extends Comparable<T>> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;


    static class Node<T> {
        T key;
        boolean color;
        Node<T> left, right, parent;

        Node(T key) {
            this.key = key;
            this.color = RED; 
        }
    }

    private final Node<T> NIL = new Node<>(null);
    private Node<T> root;

    public RedBlackTree() {
        NIL.color = BLACK;
        NIL.left = NIL.right = NIL.parent = NIL;
        root = NIL;
    }


    private void leftRotate(Node<T> x) {
        Node<T> y = x.right;
        x.right = y.left;
        if (y.left != NIL) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == NIL) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rightRotate(Node<T> x) {
        Node<T> y = x.left;
        x.left = y.right;
        if (y.right != NIL) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == NIL) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }


    public void insert(T key) {
        Node<T> z = new Node<>(key);
        z.left = NIL;
        z.right = NIL;

        Node<T> y = NIL;
        Node<T> x = root;

        while (x != NIL) {
            y = x;
            int cmp = z.key.compareTo(x.key);
            if (cmp < 0) {
                x = x.left;
            } else if (cmp > 0) {
                x = x.right;
            } else {
                return; 
            }
        }

        z.parent = y;
        if (y == NIL) {
            root = z;
        } else if (z.key.compareTo(y.key) < 0) {
            y.left = z;
        } else {
            y.right = z;
        }

        fixInsert(z);
    }

    private void fixInsert(Node<T> z) {
        while (z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                Node<T> uncle = z.parent.parent.right;

                if (uncle.color == RED) {
                    z.parent.color = BLACK;
                    uncle.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        leftRotate(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rightRotate(z.parent.parent);
                }
            } else {
                Node<T> uncle = z.parent.parent.left;

                if (uncle.color == RED) {
                    z.parent.color = BLACK;
                    uncle.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rightRotate(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    leftRotate(z.parent.parent);
                }
            }

            if (z == root) {
                break;
            }
        }
        root.color = BLACK;
    }


    public boolean contains(T key) {
        Node<T> cur = root;
        while (cur != NIL) {
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) return true;
            cur = cmp < 0 ? cur.left : cur.right;
        }
        return false;
    }

    public boolean isEmpty() {
        return root == NIL;
    }

    public int size() {
        return inorderKeys().size();
    }

    public int height() {
        return heightHelper(root);
    }

    private int heightHelper(Node<T> node) {
        if (node == NIL) return -1;
        return 1 + Math.max(heightHelper(node.left), heightHelper(node.right));
    }


    public List<T> inorderKeys() {
        List<T> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

    private void inorderHelper(Node<T> node, List<T> result) {
        if (node == NIL) return;
        inorderHelper(node.left, result);
        result.add(node.key);
        inorderHelper(node.right, result);
    }


    public List<String> levelOrderByDepth() {
        List<String> lines = new ArrayList<>();
        if (root == NIL) return lines;

        Queue<Node<T>> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < levelSize; i++) {
                Node<T> n = queue.poll();
                sb.append(n.key).append(n.color == RED ? "R" : "B").append(" ");
                if (n.left != NIL) queue.add(n.left);
                if (n.right != NIL) queue.add(n.right);
            }
            lines.add(sb.toString().trim());
        }
        return lines;
    }


    public boolean isValid() {
        if (root.color != BLACK) return false; 
        return computeBlackHeight(root) != -1;
    }

    private int computeBlackHeight(Node<T> node) {
        if (node == NIL) return 1; 

        if (node.color == RED) {
            if (node.left.color == RED || node.right.color == RED) {
                return -1; 
            }
        }

        int leftBH = computeBlackHeight(node.left);
        int rightBH = computeBlackHeight(node.right);

        if (leftBH == -1 || rightBH == -1 || leftBH != rightBH) {
            return -1; 
        }

        return leftBH + (node.color == BLACK ? 1 : 0);
    }

    Node<T> getRoot() {
        return root;
    }

    boolean isRed(Node<T> n) {
        return n.color == RED;
    }

    public String getRootKeyColor() {
        if (root == NIL) return "(empty)";
        return root.key + (root.color == RED ? "R" : "B");
    }

    @Override
    public String toString() {
        return inorderKeys().toString();
    }
}