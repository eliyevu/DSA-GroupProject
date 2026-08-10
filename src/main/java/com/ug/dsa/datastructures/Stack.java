package com.ug.dsa.datastructures;

public class Stack<T> {
    private T[] data;
    private int top;
    private int capacity;

    @SuppressWarnings("unchecked")
    public Stack(int capacity) {
        this.capacity = capacity;
        data = (T[]) new Object[capacity]; // manual array allocation
        top = -1;
    }

    // Push an element onto the stack
    public void push(T value) {
        if (top == capacity - 1) {
            throw new RuntimeException("Stack Overflow");
        }
        data[++top] = value;
    }

    // Pop the top element
    public T pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack Underflow");
        }
        return data[top--];
    }

    public void printStack() {
        if (isEmpty()) {
            System.out.println("Stack is empty");
            return;
        }
        System.out.print("Stack elements: ");
        for (int i = 0; i <= top; i++) {
            System.out.print(data[i] + ", ");
        }
        System.out.println();
    }

    // Peek at the top element
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        return data[top];
    }

    // Check if stack is empty
    public boolean isEmpty() {
        return top == -1;
    }

    // Get current size
    public int size() {
        return top + 1;
    }



}
