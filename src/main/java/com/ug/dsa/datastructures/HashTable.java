package com.ug.dsa.datastructures;

/**
 * Custom generic Hash Table implementation using Separate Chaining
 * for collision resolution.
 */
public class HashTable<K, V> {

    /**
     * Node used to store a key-value pair in a bucket chain.
     */
    private static class Entry<K, V> {

        final K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Entry<K, V>[] table;
    private int size;
    private int capacity;
    private final float loadFactor;

    /**
     * Creates a Hash Table with the default capacity and load factor.
     */
    public HashTable() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Creates a Hash Table with a specified initial capacity.
     *
     * @param initialCapacity initial number of buckets
     */
    public HashTable(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Creates a Hash Table with specified capacity and load factor.
     */
    @SuppressWarnings("unchecked")
    public HashTable(int initialCapacity, float loadFactor) {

        if (initialCapacity <= 0) {
            throw new IllegalArgumentException(
                    "Initial capacity must be greater than 0."
            );
        }

        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException(
                    "Load factor must be greater than 0."
            );
        }

        this.capacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.size = 0;

        table = new Entry[capacity];
    }

    /**
     * Compares two objects without using java.util.Objects.
     */
    private boolean areEqual(Object first, Object second) {

        if (first == second) {
            return true;
        }

        if (first == null || second == null) {
            return false;
        }

        return first.equals(second);
    }

    /**
     * Calculates the bucket index for a key.
     */
    private int getBucketIndex(Object key) {

        if (key == null) {
            return 0;
        }

        int hash = key.hashCode();
        int index = hash % capacity;

        // Prevent negative bucket indexes.
        if (index < 0) {
            index += capacity;
        }

        return index;
    }

    /**
     * Inserts a key-value pair.
     *
     * If the key already exists, its value is updated.
     */
    public V put(K key, V value) {

        int index = getBucketIndex(key);
        Entry<K, V> current = table[index];

        // Search for an existing key.
        while (current != null) {

            if (areEqual(current.key, key)) {

                V oldValue = current.value;
                current.value = value;

                return oldValue;
            }

            current = current.next;
        }

        // Key does not exist, so create a new entry.
        Entry<K, V> newEntry =
                new Entry<>(key, value, table[index]);

        table[index] = newEntry;
        size++;

        // Resize when load factor threshold is reached.
        if ((float) size / capacity >= loadFactor) {
            resize(capacity * 2);
        }

        return null;
    }

    /**
     * Returns the value associated with a key.
     *
     * @return value if found, otherwise null
     */
    public V get(K key) {

        int index = getBucketIndex(key);
        Entry<K, V> current = table[index];

        while (current != null) {

            if (areEqual(current.key, key)) {
                return current.value;
            }

            current = current.next;
        }

        return null;
    }

    /**
     * Removes the entry associated with a key.
     *
     * @return removed value, or null if key was not found
     */
    public V remove(K key) {

        int index = getBucketIndex(key);

        Entry<K, V> current = table[index];
        Entry<K, V> previous = null;

        while (current != null) {

            if (areEqual(current.key, key)) {

                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }

                size--;

                return current.value;
            }

            previous = current;
            current = current.next;
        }

        return null;
    }

    /**
     * Checks whether a key exists.
     */
    public boolean containsKey(K key) {

        int index = getBucketIndex(key);
        Entry<K, V> current = table[index];

        while (current != null) {

            if (areEqual(current.key, key)) {
                return true;
            }

            current = current.next;
        }

        return false;
    }

    /**
     * Checks whether a value exists.
     */
    public boolean containsValue(V value) {

        for (int i = 0; i < capacity; i++) {

            Entry<K, V> current = table[i];

            while (current != null) {

                if (areEqual(current.value, value)) {
                    return true;
                }

                current = current.next;
            }
        }

        return false;
    }

    /**
     * Returns the number of key-value pairs.
     */
    public int size() {
        return size;
    }

    /**
     * Checks whether the Hash Table is empty.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the current number of buckets.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Removes all entries from the Hash Table.
     */
    @SuppressWarnings("unchecked")
    public void clear() {

        table = new Entry[capacity];
        size = 0;
    }

    /**
     * Resizes the table and rehashes all existing entries.
     */
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {

        Entry<K, V>[] oldTable = table;
        int oldCapacity = capacity;

        capacity = newCapacity;
        table = new Entry[newCapacity];

        for (int i = 0; i < oldCapacity; i++) {

            Entry<K, V> current = oldTable[i];

            while (current != null) {

                Entry<K, V> next = current.next;

                int newIndex = getBucketIndex(current.key);

                current.next = table[newIndex];
                table[newIndex] = current;

                current = next;
            }
        }
    }

    /**
     * Displays the contents of the Hash Table.
     */
    @Override
    public String toString() {

        if (isEmpty()) {
            return "{}";
        }

        StringBuilder result = new StringBuilder("{");
        boolean first = true;

        for (int i = 0; i < capacity; i++) {

            Entry<K, V> current = table[i];

            while (current != null) {

                if (!first) {
                    result.append(", ");
                }

                result.append(current.key)
                        .append("=")
                        .append(current.value);

                first = false;
                current = current.next;
            }
        }

        result.append("}");

        return result.toString();
    }
}