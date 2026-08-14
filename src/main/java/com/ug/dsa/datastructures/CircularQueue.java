package com.ug.dsa.datastructures;

public class CircularQueue<T> {

    private DynamicArray<T> queue;
    private int front;
    private int size;
    private int capacity;

    public CircularQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException(
                    "Capacity must be greater than 0"
            );
        }

        this.capacity = capacity;
        this.queue = new DynamicArray<>(capacity);

        // Fill the array with null values so that
        // positions can be accessed using circular indexing.
        for (int i = 0; i < capacity; i++) {
            queue.add(null);
        }

        front = 0;
        size = 0;
    }

    // Check whether the queue is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Check whether the queue is full
    public boolean isFull() {
        return size == capacity;
    }

    // Add an element at the rear
    public void enqueue(T value) {
        if (isFull()) {
            throw new IllegalStateException("Queue is full");
        }

        int rear = (front + size) % capacity;
        queue.set(rear, value);
        size++;
    }

    // Remove and return the front element
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }

        T value = queue.get(front);

        // Clear the position after removing
        queue.set(front, null);

        front = (front + 1) % capacity;
        size--;

        // Reset front when queue becomes empty
        if (size == 0) {
            front = 0;
        }

        return value;
    }

    // Return the front element without removing it
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }

        return queue.get(front);
    }

    // Return current number of elements
    public int size() {
        return size;
    }

    // Display elements from front to rear
    public void display() {
        if (isEmpty()) {
            System.out.println("Queue is empty");
            return;
        }

        System.out.print("Queue: ");

        for (int i = 0; i < size; i++) {
            int index = (front + i) % capacity;
            System.out.print(queue.get(index) + " ");
        }

        System.out.println();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");

        for (int i = 0; i < size; i++) {
            int index = (front + i) % capacity;
            result.append(queue.get(index));

            if (i < size - 1) {
                result.append(", ");
            }
        }

        result.append("]");
        return result.toString();
    }
}
