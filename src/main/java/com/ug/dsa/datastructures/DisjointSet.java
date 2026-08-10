package com.ug.dsa.datastructures;

public class DisjointSet<T> {

    // Custom helper node to represent each element's set properties
    private static class Node<T> {
        private final T data;
        private Node<T> parent;
        private int rank;

        Node(T data) {
            this.data = data;
            this.parent = this;
            this.rank = 0;
        }
    }

    // Custom simple Entry for the custom hash table
    private static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    // Custom, Java-collection-free HashTable implementation for mapping T to Node<T>
    private static class CustomMap<K, V> {
        private Entry<K, V>[] table;
        private int size;
        private static final int INITIAL_CAPACITY = 16;
        private static final double LOAD_FACTOR_THRESHOLD = 0.75;

        @SuppressWarnings("unchecked")
        CustomMap() {
            table = new Entry[INITIAL_CAPACITY];
            size = 0;
        }

        private int getBucketIndex(K key) {
            if (key == null) {
                return 0;
            }
            return Math.abs(key.hashCode()) % table.length;
        }

        public void put(K key, V value) {
            if (size >= table.length * LOAD_FACTOR_THRESHOLD) {
                resize();
            }

            int index = getBucketIndex(key);
            Entry<K, V> head = table[index];
            Entry<K, V> current = head;

            while (current != null) {
                if (current.key == key || (current.key != null && current.key.equals(key))) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }

            Entry<K, V> newEntry = new Entry<>(key, value);
            newEntry.next = head;
            table[index] = newEntry;
            size++;
        }

        public V get(K key) {
            int index = getBucketIndex(key);
            Entry<K, V> current = table[index];

            while (current != null) {
                if (current.key == key || (current.key != null && current.key.equals(key))) {
                    return current.value;
                }
                current = current.next;
            }
            return null;
        }

        public boolean containsKey(K key) {
            return get(key) != null;
        }

        public int size() {
            return size;
        }

        @SuppressWarnings("unchecked")
        private void resize() {
            Entry<K, V>[] oldTable = table;
            table = new Entry[oldTable.length * 2];
            size = 0;

            for (Entry<K, V> head : oldTable) {
                Entry<K, V> current = head;
                while (current != null) {
                    put(current.key, current.value);
                    current = current.next;
                }
            }
        }
    }

    private final CustomMap<T, Node<T>> nodeMap;

    /**
     * Default constructor for the Disjoint Set.
     */
    public DisjointSet() {
        this.nodeMap = new CustomMap<>();
    }

    /**
     * Initializes a disjoint set of a given size with elements from 0 to size - 1.
     */
    @SuppressWarnings("unchecked")
    public DisjointSet(int size) {
        this();
        if (size < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        for (int i = 0; i < size; i++) {
            makeSet((T) Integer.valueOf(i));
        }
    }

    /**
     * Creates a new set containing the specified element.
     */
    public void makeSet(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null.");
        }
        if (nodeMap.containsKey(element)) {
            return; // Element already has a set
        }
        nodeMap.put(element, new Node<>(element));
    }

    /**
     * Finds the representative node of the set containing node.
     * Applies path compression recursively.
     */
    private Node<T> findNode(Node<T> node) {
        if (node.parent == node) {
            return node;
        }
        // Path compression
        node.parent = findNode(node.parent);
        return node.parent;
    }

    /**
     * Finds the representative element of the set containing the given element.
     * Applies path compression to flatten the structure.
     */
    public T find(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null.");
        }
        Node<T> node = nodeMap.get(element);
        if (node == null) {
            throw new IllegalArgumentException("Element not found in disjoint set: " + element);
        }
        return findNode(node).data;
    }

    /**
     * Merges the set containing element1 with the set containing element2.
     * Applies union by rank to keep tree depths minimal.
     */
    public boolean union(T element1, T element2) {
        if (element1 == null || element2 == null) {
            throw new IllegalArgumentException("Elements cannot be null.");
        }
        Node<T> node1 = nodeMap.get(element1);
        Node<T> node2 = nodeMap.get(element2);

        if (node1 == null || node2 == null) {
            throw new IllegalArgumentException("Both elements must exist in the disjoint set before union.");
        }

        Node<T> root1 = findNode(node1);
        Node<T> root2 = findNode(node2);

        if (root1 == root2) {
            return false; // Already in the same set
        }

        // Union by rank
        if (root1.rank < root2.rank) {
            root1.parent = root2;
        } else if (root1.rank > root2.rank) {
            root2.parent = root1;
        } else {
            root2.parent = root1;
            root1.rank++;
        }
        return true;
    }

    /**
     * Returns the total number of elements registered in the disjoint set.
     */
    public int getSize() {
        return nodeMap.size();
    }
}
