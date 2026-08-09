/**
 * A custom Queue implementation built from scratch using generic nodes.
 * Follows FIFO (First-In, First-Out) principle.
 *
 * @param <T> the type of elements stored in this queue
 */
public class CustomQueue<T> {

    private static class Node<T> {
        private final T data;
        private Node<T> next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> front;
    private Node<T> rear;
    private int size;

    public CustomQueue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    /**
     * Adds an item to the back of the queue.
     *
     * @param element the item to be added
     */
    public void enqueue(T element) {
        Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            front = newNode;
        } else {
            rear.next = newNode;
        }
        rear = newNode;
        size++;
    }

    /**
     * Removes and returns the item from the front of the queue.
     *
     * @return the item at the front
     * @throws IllegalStateException if the queue is empty
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot dequeue from an empty queue.");
        }
        T data = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return data;
    }

    /**
     * Retrieves, but does not remove, the head of this queue.
     *
     * @return the item at the front
     * @throws IllegalStateException if the queue is empty
     */
    public T front() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }
        return front.data;
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue contains no elements, false otherwise
     */
    public boolean isEmpty() {
        return front == null;
    }

    /**
     * Returns the current size of the queue.
     */
    public int size() {
        return size;
    }
}
