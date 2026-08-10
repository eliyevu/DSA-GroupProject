package com.ug.dsa.datastructures;

public class RedBlackTree {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        int key;
        boolean color;
        Node left, right, parent;

        Node(int key) {
            this.key = key;
            this.color = RED;
        }
    }

    private final Node NIL = new Node(-1);
    private Node root;

    public RedBlackTree() {
        NIL.color = BLACK;
        NIL.left = NIL.right = NIL.parent = NIL;
        root = NIL;
    }

    private void leftRotate(Node x) {
        Node y = x.right;
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

    private void rightRotate(Node x) {
        Node y = x.left;
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

    public void insert(int key) {
        Node new_node = new Node(key);
        new_node.left = NIL;
        new_node.right = NIL;

        Node y = NIL;
        Node x = root;

        while (x != NIL) {
            y = x;
            if (new_node.key < x.key) {
                x = x.left;
            } else if (new_node.key > x.key) {
                x = x.right;
            } else {
                return;
            }
        }

        new_node.parent = y;
        if (y == NIL) {
            root = new_node;
        } else if (new_node.key < y.key) {
            y.left = new_node;
        } else {
            y.right = new_node;
        }

        fixInsert(new_node);
    }

    private void fixInsert(Node z) {
        while (z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                Node uncle = z.parent.parent.right;
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
                Node uncle = z.parent.parent.left;
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


    boolean isValid() {
        if (root.color != BLACK) return false;
        return computeBlackHeight(root) != -1;
    }

    private int computeBlackHeight(Node node) {
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

    java.util.List<Integer> inorderKeys() {
        java.util.List<Integer> result = new java.util.ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

    private void inorderHelper(Node node, java.util.List<Integer> result) {
        if (node == NIL) return;
        inorderHelper(node.left, result);
        result.add(node.key);
        inorderHelper(node.right, result);
    }

    boolean contains(int key) {
        Node cur = root;
        while (cur != NIL) {
            if (key == cur.key) return true;
            cur = key < cur.key ? cur.left : cur.right;
        }
        return false;
    }

    Node getRoot() {
        return root;
    }

    boolean isRed(Node n) {
        return n.color == RED;
    }

    int size() {
        return inorderKeys().size();
    }

    public boolean isEmpty() {
        return root == NIL;
    }

    int height() {
        return heightHelper(root);
    }

    private int heightHelper(Node node) {
        if (node == NIL) return -1;
        return 1 + Math.max(heightHelper(node.left), heightHelper(node.right));
    }


    java.util.List<String> levelOrderByDepth() {
        java.util.List<String> lines = new java.util.ArrayList<>();
        if (root == NIL) return lines;

        java.util.Queue<Node> queue = new java.util.LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < levelSize; i++) {
                Node n = queue.poll();
                sb.append(n.key).append(n.color == RED ? "R" : "B").append(" ");
                if (n.left != NIL) queue.add(n.left);
                if (n.right != NIL) queue.add(n.right);
            }
            lines.add(sb.toString().trim());
        }
        return lines;
    }


    String getRootKeyColor() {
        if (root == NIL) return "(empty)";
        return root.key + (root.color == RED ? "R" : "B");
    }

    @Override
    public String toString() {
        return inorderKeys().toString();
    }
}