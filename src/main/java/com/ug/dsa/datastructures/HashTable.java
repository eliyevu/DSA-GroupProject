package com.ug.dsa.datastructures;

import java.util.Objects;

/**
 * Custom Hash Table implementation using Separate Chaining for collision resolution.
 * Supports insertion, searching, deletion, dynamic resizing (rehashing), and null keys.
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public class HashTable<K, V> {

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
     * Constructs a HashTable with default initial capacity (16) and load factor (0.75).
     */
    public HashTable() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs a HashTable with a specified initial capacity and default load factor (0.75).
     *
     * @param initialCapacity initial bucket capacity
     */
    public HashTable(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs a HashTable with specified initial capacity and load factor.
     *
     * @param initialCapacity initial bucket capacity
     * @param loadFactor      threshold ratio for dynamic resizing
     */
    @SuppressWarnings("unchecked")
    public HashTable(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be greater than 0.");
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Load factor must be greater than 0.");
        }
        this.capacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.size = 0;
        this.table = new Entry[this.capacity];
    }

    /**
     * Computes the bucket index for a given key.
     *
     * @param key target key
     * @return bucket index in [0, capacity - 1]
     */
    private int getBucketIndex(Object key) {
        if (key == null) {
            return 0;
        }
        int hash = key.hashCode();
        return Math.abs(hash % capacity);
    }

    /**
     * Inserts or updates a key-value pair in the Hash Table.
     * Handles collisions using separate chaining and resizes table if load factor threshold is met.
     *
     * @param key   key to insert or update
     * @param value value associated with key
     * @return previous value associated with key, or null if key was not previously present
     */
    public V put(K key, V value) {
        int index = getBucketIndex(key);
        Entry<K, V> head = table[index];

        // Check if key already exists in bucket chain
        Entry<K, V> curr = head;
        while (curr != null) {
            if (Objects.equals(curr.key, key)) {
                V oldValue = curr.value;
                curr.value = value;
                return oldValue;
            }
            curr = curr.next;
        }

        // Key not found: prepend new node to bucket chain
        Entry<K, V> newEntry = new Entry<>(key, value, head);
        table[index] = newEntry;
        size++;

        // Dynamic resizing check
        if ((float) size / capacity >= loadFactor) {
            resize(capacity * 2);
        }

        return null;
    }

    /**
     * Searches for and returns the value associated with the specified key.
     *
     * @param key key to search for
     * @return value associated with key, or null if key does not exist
     */
    public V get(K key) {
        int index = getBucketIndex(key);
        Entry<K, V> curr = table[index];

        while (curr != null) {
            if (Objects.equals(curr.key, key)) {
                return curr.value;
            }
            curr = curr.next;
        }

        return null;
    }

    /**
     * Deletes the key-value pair associated with the specified key.
     *
     * @param key key to remove
     * @return removed value associated with key, or null if key was not found
     */
    public V remove(K key) {
        int index = getBucketIndex(key);
        Entry<K, V> curr = table[index];
        Entry<K, V> prev = null;

        while (curr != null) {
            if (Objects.equals(curr.key, key)) {
                if (prev == null) {
                    table[index] = curr.next;
                } else {
                    prev.next = curr.next;
                }
                size--;
                return curr.value;
            }
            prev = curr;
            curr = curr.next;
        }

        return null;
    }

    /**
     * Checks if the specified key exists in the Hash Table.
     *
     * @param key key to check
     * @return true if key exists, false otherwise
     */
    public boolean containsKey(K key) {
        int index = getBucketIndex(key);
        Entry<K, V> curr = table[index];

        while (curr != null) {
            if (Objects.equals(curr.key, key)) {
                return true;
            }
            curr = curr.next;
        }

        return false;
    }

    /**
     * Checks if the specified value exists in any entry of the Hash Table.
     *
     * @param value value to search for
     * @return true if value exists, false otherwise
     */
    public boolean containsValue(V value) {
        for (int i = 0; i < capacity; i++) {
            Entry<K, V> curr = table[i];
            while (curr != null) {
                if (Objects.equals(curr.value, value)) {
                    return true;
                }
                curr = curr.next;
            }
        }
        return false;
    }

    /**
     * Returns the total number of key-value pairs stored in the Hash Table.
     *
     * @return current size
     */
    public int size() {
        return size;
    }

    /**
     * Checks if the Hash Table is empty.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the current array bucket capacity.
     *
     * @return capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Removes all key-value entries from the Hash Table.
     */
    @SuppressWarnings("unchecked")
    public void clear() {
        this.table = new Entry[capacity];
        this.size = 0;
    }

    /**
     * Resizes the Hash Table bucket array and rehashes all existing entries.
     *
     * @param newCapacity new bucket array size
     */
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Entry<K, V>[] oldTable = table;
        int oldCapacity = capacity;

        this.capacity = newCapacity;
        this.table = new Entry[newCapacity];

        for (int i = 0; i < oldCapacity; i++) {
            Entry<K, V> curr = oldTable[i];
            while (curr != null) {
                Entry<K, V> next = curr.next;
                int newIndex = getBucketIndex(curr.key);
                curr.next = table[newIndex];
                table[newIndex] = curr;
                curr = next;
            }
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (int i = 0; i < capacity; i++) {
            Entry<K, V> curr = table[i];
            while (curr != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(curr.key).append("=").append(curr.value);
                first = false;
                curr = curr.next;
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
