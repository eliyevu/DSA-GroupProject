package com.ug.dsa.datastructures;

/**
 * Custom generic Red-Black Tree implementation.
 */
public class RedBlackTree<T extends Comparable<T>> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node<T> {
        T key;
        boolean color;
        Node<T> left;
        Node<T> right;
        Node<T> parent;

        Node(T key) {
            this.key = key;
            this.color = RED;
        }
    }

    private final Node<T> NIL;
    private Node<T> root;
    private int size;

    public RedBlackTree() {
        NIL = new Node<>(null);
        NIL.color = BLACK;
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.parent = NIL;

        root = NIL;
        size = 0;
    }

    // ROTATIONS
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

    // INSERT

    public void insert(T key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }

        Node<T> newNode = new Node<>(key);
        newNode.left = NIL;
        newNode.right = NIL;

        Node<T> parent = NIL;
        Node<T> current = root;

        while (current != NIL) {
            parent = current;

            int comparison = key.compareTo(current.key);

            if (comparison < 0) {
                current = current.left;
            } else if (comparison > 0) {
                current = current.right;
            } else {
                // Duplicate values are not allowed.
                return;
            }
        }

        newNode.parent = parent;

        if (parent == NIL) {
            root = newNode;
        } else if (key.compareTo(parent.key) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        size++;
        fixInsert(newNode);
    }

    private void fixInsert(Node<T> node) {
        while (node.parent.color == RED) {

            if (node.parent == node.parent.parent.left) {

                Node<T> uncle = node.parent.parent.right;

                // Case 1: Uncle is red
                if (uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;

                    node = node.parent.parent;

                } else {

                    // Case 2: Triangle
                    if (node == node.parent.right) {
                        node = node.parent;
                        leftRotate(node);
                    }

                    // Case 3: Line
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    rightRotate(node.parent.parent);
                }

            } else {

                Node<T> uncle = node.parent.parent.left;

                // Case 1: Uncle is red
                if (uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;

                    node = node.parent.parent;

                } else {

                    // Case 2: Triangle
                    if (node == node.parent.left) {
                        node = node.parent;
                        rightRotate(node);
                    }

                    // Case 3: Line
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    leftRotate(node.parent.parent);
                }
            }

            if (node == root) {
                break;
            }
        }

        root.color = BLACK;
    }

    // SEARCH

    public boolean contains(T key) {
        if (key == null) {
            return false;
        }

        Node<T> current = root;

        while (current != NIL) {
            int comparison = key.compareTo(current.key);

            if (comparison == 0) {
                return true;
            }

            if (comparison < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        return false;
    }

    // INORDER TRAVERSAL

    /**
     * Uses the project's custom DynamicArray instead of java.util.List.
     */
    public DynamicArray inorderKeys() {
        DynamicArray result = new DynamicArray();
        inorderHelper(root, result);
        return result;
    }

    private void inorderHelper(Node<T> node, DynamicArray result) {
        if (node == NIL) {
            return;
        }

        inorderHelper(node.left, result);
        result.add(node.key);
        inorderHelper(node.right, result);
    }

    // LEVEL ORDER TRAVERSAL

    /**
     * Uses the project's custom Queue and DynamicArray.
     */
    public DynamicArray levelOrderByDepth() {
        DynamicArray result = new DynamicArray();

        if (root == NIL) {
            return result;
        }

        Queue<Node<T>> queue = new Queue<>();

        queue.enqueue(root);

        while (!queue.isEmpty()) {

            int levelSize = queue.size();

            DynamicArray level = new DynamicArray();

            for (int i = 0; i < levelSize; i++) {

                Node<T> node = queue.dequeue();

                level.add(
                        node.key + (node.color == RED ? "R" : "B")
                );

                if (node.left != NIL) {
                    queue.enqueue(node.left);
                }

                if (node.right != NIL) {
                    queue.enqueue(node.right);
                }
            }

            result.add(level);
        }

        return result;
    }

    // SIZE / EMPTY

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return root == NIL;
    }

    // HEIGHT

    public int height() {
        return heightHelper(root);
    }

    private int heightHelper(Node<T> node) {
        if (node == NIL) {
            return -1;
        }

        int leftHeight = heightHelper(node.left);
        int rightHeight = heightHelper(node.right);

        return 1 + Math.max(leftHeight, rightHeight);
    }

    // VALIDATION

    public boolean isValid() {
        if (root == NIL) {
            return true;
        }

        // Root must be black.
        if (root.color != BLACK) {
            return false;
        }

        // Verify BST ordering.
        if (!isValidBST(root, null, null)) {
            return false;
        }

        // Verify Red-Black properties.
        return computeBlackHeight(root) != -1;
    }

    private boolean isValidBST(
            Node<T> node,
            T min,
            T max) {

        if (node == NIL) {
            return true;
        }

        if (min != null && node.key.compareTo(min) <= 0) {
            return false;
        }

        if (max != null && node.key.compareTo(max) >= 0) {
            return false;
        }

        return isValidBST(node.left, min, node.key)
                && isValidBST(node.right, node.key, max);
    }

    private int computeBlackHeight(Node<T> node) {
        if (node == NIL) {
            return 1;
        }

        // Red nodes cannot have red children.
        if (node.color == RED) {
            if (node.left.color == RED || node.right.color == RED) {
                return -1;
            }
        }

        int leftBlackHeight = computeBlackHeight(node.left);
        int rightBlackHeight = computeBlackHeight(node.right);

        if (leftBlackHeight == -1
                || rightBlackHeight == -1
                || leftBlackHeight != rightBlackHeight) {
            return -1;
        }

        return leftBlackHeight
                + (node.color == BLACK ? 1 : 0);
    }

    // ROOT INFORMATION

    public T getRootKey() {
        if (root == NIL) {
            return null;
        }

        return root.key;
    }

    public boolean isRootRed() {
        return root != NIL && root.color == RED;
    }

    // STRING REPRESENTATION

    @Override
    public String toString() {
        return inorderKeys().toString();
    }
}
