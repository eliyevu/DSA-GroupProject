```java
package com.ug.dsa.datastructures;

public class DynamicArray<T> {

    private Object[] data;
    private int size;

    private static final int DEFAULT_CAPACITY = 5;

    // Constructor
    public DynamicArray() {
        data = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    // Constructor with custom initial capacity
    public DynamicArray(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException(
                    "Initial capacity must be at least 1"
            );
        }

        data = new Object[initialCapacity];
        size = 0;
    }

    // Add an element
    public void add(T value) {
        if (size == data.length) {
            resize();
        }

        data[size] = value;
        size++;
    }

    // Remove an element at a given index
    public T remove(int index) {
        checkIndex(index);

        T removed = get(index);

        // Shift elements to the left
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }

        data[size - 1] = null;
        size--;

        return removed;
    }

    // Get an element
    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) data[index];
    }

    // Update an element
    public void set(int index, T value) {
        checkIndex(index);
        data[index] = value;
    }

    // Check whether the array contains a value
    public boolean contains(T value) {
        return indexOf(value) != -1;
    }

    // Return the index of the first occurrence
    public int indexOf(T value) {
        for (int i = 0; i < size; i++) {
            if (data[i].equals(value)) {
                return i;
            }
        }

        return -1;
    }

    // Check whether the array is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Remove all elements
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }

        size = 0;
    }

    // Return number of elements
    public int size() {
        return size;
    }

    // Display all elements
    public void display() {
        for (int i = 0; i < size; i++) {
            System.out.print(data[i] + " ");
        }

        System.out.println();
    }

    // Resize the array
    private void resize() {
        int newCapacity = data.length * 2;
        Object[] newData = new Object[newCapacity];

        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }

        data = newData;
    }

    // Validate an index
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Invalid index: " + index
            );
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < size; i++) {
            sb.append(data[i]);

            if (i < size - 1) {
                sb.append(", ");
            }
        }

        sb.append("]");
        return sb.toString();
    }
}
```
