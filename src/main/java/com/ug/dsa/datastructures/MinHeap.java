package com.ug.dsa.datastructures;

public class MinHeap<T extends Comparable<T>> {

    private Object[] data;
    private int size;

    private static final int DEFAULT_CAPACITY = 16;

    public MinHeap() {
        data = new Object[DEFAULT_CAPACITY];
        size = 0;
    }


    private int parentIndex(int i) {
        return (i - 1) / 2;
    }

    private int leftChildIndex(int i) {
        return 2 * i + 1;
    }

    private int rightChildIndex(int i) {
        return 2 * i + 2;
    }

    @SuppressWarnings("unchecked")
    private T get(int i) {
        return (T) data[i];
    }

    private void swap(int i, int j) {
        Object temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    private void resizeIfNeeded() {
        if (size == data.length) {
            Object[] newData = new Object[data.length * 2];
            for (int i = 0; i < size; i++) {
                newData[i] = data[i];
            }
            data = newData;
        }
    }

    private void siftUp(int i) {
        while (i > 0 && get(i).compareTo(get(parentIndex(i))) < 0) {
            swap(i, parentIndex(i));
            i = parentIndex(i);
        }
    }

    private void siftDown(int i) {
        while (true) {
            int smallest = i;
            int left = leftChildIndex(i);
            int right = rightChildIndex(i);

            if (left < size && get(left).compareTo(get(smallest)) < 0) {
                smallest = left;
            }
            if (right < size && get(right).compareTo(get(smallest)) < 0) {
                smallest = right;
            }

            if (smallest == i) {
                break;
            }

            swap(i, smallest);
            i = smallest;
        }
    }

    public void insert(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot insert null into MinHeap");
        }
        resizeIfNeeded();
        data[size] = value;
        size++;
        siftUp(size - 1);
    }

    public T deleteMin() {
        if (isEmpty()) {
            throw new IllegalStateException("deleteMin() called on an empty heap");
        }

        T min = get(0);
        size--;
        data[0] = data[size];
        data[size] = null;

        if (size > 0) {
            siftDown(0);
        }

        return min;
    }


    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("peek() called on an empty heap");
        }
        return get(0);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(get(i));
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}