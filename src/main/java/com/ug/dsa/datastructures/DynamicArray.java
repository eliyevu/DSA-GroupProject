package com.ug.dsa.datastructures;

public class DynamicArray {

    private int[] data;
    private int size;

    // Constructor
    public DynamicArray() {
        data = new int[5];
        size = 0;
    }

    // Add an element
    public void add(int value) {
        if (size == data.length) {
            resize();
        }

        data[size] = value;
        size++;
    }

    // Remove an element at a given index
    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }

        // Shift elements to the left
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }

        size--;
    }

    // Get an element
    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }

        return data[index];
    }

    // Update an element
    public void set(int index, int value) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }

        data[index] = value;
    }

    // Resize the array
    private void resize() {
        int newCapacity = data.length * 2;
        int[] newData = new int[newCapacity];

        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }

        data = newData;
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
}