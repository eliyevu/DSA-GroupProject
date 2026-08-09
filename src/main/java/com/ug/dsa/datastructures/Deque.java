package com.ug.dsa.datastructures;
import java.util.NoSuchElementException;

public class Deque<T> {
    private static class Node<T> {
        T data;
        Node<T> prev;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        }
    }

    private Node<T> head; // front of the deque
    private Node<T> tail; // rear of the deque
    private int size;

    public Deque() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    //Inserts an element at the front of the deque. O(1).
    public void addFront(T value) {
        Node<T> newNode = new Node<>(value);

        if (isEmpty()) {
            // First element: it is simultaneously head and tail.
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }


     //Inserts an element at the rear of the deque. O(1).
    public void addRear(T value) {
        Node<T> newNode = new Node<>(value);

        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public T removeFront() {
        if (isEmpty()) {
            throw new NoSuchElementException("removeFront: deque is empty");
        }

        T removedData = head.data;

        if (head == tail) {
            // Only one element was present; deque becomes fully empty.
            head = null;
            tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        size--;
        return removedData;
    }


    public T removeRear() {
        if (isEmpty()) {
            throw new NoSuchElementException("removeRear: deque is empty");
        }

        T removedData = tail.data;

        if (head == tail) {
            head = null;
            tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        size--;
        return removedData;
    }

    public T peekFront() {
        if (isEmpty()) {
            throw new NoSuchElementException("peekFront: deque is empty");
        }
        return head.data;
    }

    public T peekRear() {
        if (isEmpty()) {
            throw new NoSuchElementException("peekRear: deque is empty");
        }
        return tail.data;
    }


    public boolean isEmpty() {
        return head == null;
    }


    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

}