package com.ug.dsa.datastructures;
 
public class CircularQueue {
    private int[] queue;
    private int front;
    private int rear;
    private int size;
    private int capacity;
 
    public CircularQueue(int capacity) {
        this.capacity = capacity;
        queue = new int[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }
 
    public boolean isEmpty() {
        return size == 0;
    }
 
    public boolean isFull() {
        return size == capacity;
    }
 
    // adds a value at the rear, wraps around to the start if needed
    public void enqueue(int value) {
        if (isFull()) {
            System.out.println("Queue is full, cannot add " + value);
            return;
        }
        rear = (rear + 1) % capacity;
        queue[rear] = value;
        size++;
    }
 
    // removes and returns the value at the front, wraps around too
    public int dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty");
            return -1;
        }
        int value = queue[front];
        front = (front + 1) % capacity;
        size--;
        return value;
    }
 
    public int peek() {
        if (isEmpty()) {
            System.out.println("Queue is empty");
            return -1;
        }
        return queue[front];
    }
 
    public int size() {
        return size;
    }
 
    public void display() {
        if (isEmpty()) {
            System.out.println("Queue is empty");
            return;
        }
        System.out.print("Queue: ");
        int i = front;
        for (int count = 0; count < size; count++) {
            System.out.print(queue[i] + " ");
            i = (i + 1) % capacity;
        }
        System.out.println();
    }
}
