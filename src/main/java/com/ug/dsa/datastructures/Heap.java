package com.ug.dsa.datastructures;

public class Heap<T> {

    private static class HeapEntry<T> {
        T vertex;
        int priority;

        HeapEntry(T vertex, int priority) {
            this.vertex = vertex;
            this.priority = priority;
        }

        @Override
        public String toString() {
            return "(" + vertex + ", " + priority + ")";
        }
    }

    private final DynamicArray<HeapEntry<T>> entries;

    public Heap() {
        entries = new DynamicArray<>();
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

    private void swap(int i, int j) {
        HeapEntry<T> temp = entries.get(i);
        entries.set(i, entries.get(j));
        entries.set(j, temp);
    }

    private void siftUp(int i) {
        while (i > 0 && entries.get(i).priority < entries.get(parentIndex(i)).priority) {
            swap(i, parentIndex(i));
            i = parentIndex(i);
        }
    }

    private void siftDown(int i) {
        int size = entries.size();
        while (true) {
            int smallest = i;
            int left = leftChildIndex(i);
            int right = rightChildIndex(i);

            if (left < size && entries.get(left).priority < entries.get(smallest).priority) {
                smallest = left;
            }
            if (right < size && entries.get(right).priority < entries.get(smallest).priority) {
                smallest = right;
            }

            if (smallest == i) {
                break;
            }

            swap(i, smallest);
            i = smallest;
        }
    }

    private int indexOfVertex(T vertex) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).vertex.equals(vertex)) {
                return i;
            }
        }
        return -1;
    }

    public void insert(T vertex, int priority) {
        if (vertex == null) {
            throw new IllegalArgumentException("Cannot insert a null vertex into Heap");
        }
        entries.add(new HeapEntry<>(vertex, priority));
        siftUp(entries.size() - 1);
    }

    public T extractMin() {
        if (isEmpty()) {
            throw new IllegalStateException("extractMin() called on an empty heap");
        }

        T minVertex = entries.get(0).vertex;
        int lastIndex = entries.size() - 1;

        HeapEntry<T> last = entries.remove(lastIndex);

        if (lastIndex > 0) {
            entries.set(0, last);
            siftDown(0);
        }

        return minVertex;
    }

 
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("peek() called on an empty heap");
        }
        return entries.get(0).vertex;
    }

    public boolean contains(T vertex) {
        return indexOfVertex(vertex) != -1;
    }

    public void decreaseKey(T vertex, int newPriority) {
        int i = indexOfVertex(vertex);
        if (i == -1) {
            throw new IllegalArgumentException("Vertex not found in heap: " + vertex);
        }

        HeapEntry<T> entry = entries.get(i);
        if (newPriority > entry.priority) {
            throw new IllegalArgumentException("New priority is greater than current priority");
        }

        entry.priority = newPriority;
        siftUp(i);
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public int size() {
        return entries.size();
    }

    @Override
    public String toString() {
        return entries.toString();
    }
}