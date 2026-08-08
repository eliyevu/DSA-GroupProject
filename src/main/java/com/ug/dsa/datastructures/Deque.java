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

    /**
     * Inserts an element at the front of the deque. O(1).
     */
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

    /**
     * Inserts an element at the rear of the deque. O(1).
     */
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

    /**
     * Removes and returns the element at the front of the deque. O(1).
     *
     * @throws java.util.NoSuchElementException if the deque is empty
     */
    public T removeFront() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("removeFront: deque is empty");
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

    /**
     * Removes and returns the element at the rear of the deque. O(1).
     *
     * @throws java.util.NoSuchElementException if the deque is empty
     */
    public T removeRear() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("removeRear: deque is empty");
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

    /**
     * Returns (without removing) the element at the front of the deque.
     *
     * @throws java.util.NoSuchElementException if the deque is empty
     */
    public T peekFront() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("peekFront: deque is empty");
        }
        return head.data;
    }

    /**
     * Returns (without removing) the element at the rear of the deque.
     *
     * @throws java.util.NoSuchElementException if the deque is empty
     */
    public T peekRear() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("peekRear: deque is empty");
        }
        return tail.data;
    }

    /**
     * Returns true if the deque contains no elements.
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Returns the number of elements currently in the deque.
     * (Not in the required method list, but genuinely useful for
     * debugging/demo output.)
     */
    public int size() {
        return size;
    }

    /**
     * Returns a front-to-rear string representation, e.g. [A, B, C]
     * where A is the front and C is the rear. Useful for demoing
     * and sanity-checking behaviour.
     */
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

    /**
     * Simple demo / manual smoke test.
     * Run this directly (java Deque) to see the deque in action.
     */
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();

        System.out.println("Is empty? " + deque.isEmpty()); // true

        deque.addRear("Request-B");
        deque.addRear("Request-C");
        deque.addFront("Request-A"); // urgent request jumps to the front
        System.out.println("After adds: " + deque);          // [Request-A, Request-B, Request-C]
        System.out.println("Front: " + deque.peekFront());    // Request-A
        System.out.println("Rear: " + deque.peekRear());      // Request-C
        System.out.println("Size: " + deque.size());          // 3

        String removedFront = deque.removeFront();
        System.out.println("Removed from front: " + removedFront); // Request-A
        System.out.println("After removeFront: " + deque);          // [Request-B, Request-C]

        String removedRear = deque.removeRear();
        System.out.println("Removed from rear: " + removedRear); // Request-C
        System.out.println("After removeRear: " + deque);        // [Request-B]

        deque.removeFront();
        System.out.println("Is empty? " + deque.isEmpty()); // true

        // Edge case: operating on an empty deque should throw, not crash silently.
        try {
            deque.removeFront();
        } catch (java.util.NoSuchElementException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }
    }
}