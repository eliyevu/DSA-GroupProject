package com.ug.dsa.services;

import com.ug.dsa.datastructures.CircularQueue;
import com.ug.dsa.datastructures.Deque;
import com.ug.dsa.datastructures.Heap;
import com.ug.dsa.datastructures.Queue;
import com.ug.dsa.models.Resource;
import com.ug.dsa.models.ServiceRequest;

/**
 * SchedulingService: Campus service-request scheduling engine.
 * 
 * Demonstrates four core custom data structures:
 *  - Standard FIFO Queue: First-Come, First-Served request processing.
 *  - Circular Queue: Bounded/fixed-capacity queue for rate-limited buffering.
 *  - Deque: Double-ended queue for high-priority / urgent override insertion at the front.
 *  - Heap / Priority Queue: Min-Heap for urgency/priority-based request processing.
 * 
 * Answers the core operational question: "Which service request should be handled next?"
 */
public class SchedulingService {

    private static final int DEFAULT_CIRCULAR_CAPACITY = 100;

    private final Queue<ServiceRequest> fifoQueue;
    private final CircularQueue<ServiceRequest> circularQueue;
    private final Deque<ServiceRequest> urgentDeque;
    private final Heap<ServiceRequest> priorityHeap;

    public SchedulingService() {
        this(DEFAULT_CIRCULAR_CAPACITY);
    }

    public SchedulingService(int circularCapacity) {
        this.fifoQueue = new Queue<>();
        this.circularQueue = new CircularQueue<>(circularCapacity);
        this.urgentDeque = new Deque<>();
        this.priorityHeap = new Heap<>();
    }

    // =========================================================================
    // 1. Standard FIFO Queue Operations
    // =========================================================================

    /**
     * Enqueues a service request into the standard FIFO queue.
     * Sets status to "SCHEDULED_FIFO" if currently null or empty.
     */
    public void scheduleFIFO(ServiceRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("ServiceRequest cannot be null.");
        }
        if (request.getStatus() == null || request.getStatus().trim().isEmpty()) {
            request.setStatus("SCHEDULED_FIFO");
        }
        fifoQueue.enqueue(request);
    }

    /**
     * Dequeues and returns the next request from the standard FIFO queue.
     * @return Next ServiceRequest, or null if FIFO queue is empty.
     */
    public ServiceRequest getNextFIFORequest() {
        if (fifoQueue.isEmpty()) {
            return null;
        }
        return fifoQueue.dequeue();
    }

    /**
     * Peeks at the front request of the standard FIFO queue without removing it.
     */
    public ServiceRequest peekNextFIFORequest() {
        if (fifoQueue.isEmpty()) {
            return null;
        }
        return fifoQueue.front();
    }

    public int getFifoQueueSize() {
        return fifoQueue.size();
    }

    public boolean isFifoEmpty() {
        return fifoQueue.isEmpty();
    }

    // =========================================================================
    // 2. Circular Queue Operations (Fixed Capacity Buffer)
    // =========================================================================

    /**
     * Enqueues a request into the fixed-capacity circular buffer.
     */
    public void scheduleCircular(ServiceRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("ServiceRequest cannot be null.");
        }
        if (circularQueue.isFull()) {
            throw new IllegalStateException("Circular queue is full. Cannot schedule request.");
        }
        if (request.getStatus() == null || request.getStatus().trim().isEmpty()) {
            request.setStatus("SCHEDULED_CIRCULAR");
        }
        circularQueue.enqueue(request);
    }

    /**
     * Dequeues and returns the next request from the circular queue.
     * @return Next ServiceRequest, or null if circular queue is empty.
     */
    public ServiceRequest getNextCircularRequest() {
        if (circularQueue.isEmpty()) {
            return null;
        }
        return circularQueue.dequeue();
    }

    /**
     * Peeks at the next request in the circular queue without removing it.
     */
    public ServiceRequest peekNextCircularRequest() {
        if (circularQueue.isEmpty()) {
            return null;
        }
        return circularQueue.peek();
    }

    public boolean isCircularQueueFull() {
        return circularQueue.isFull();
    }

    public boolean isCircularQueueEmpty() {
        return circularQueue.isEmpty();
    }

    public int getCircularQueueSize() {
        return circularQueue.size();
    }

    // =========================================================================
    // 3. Deque / Urgent Operations
    // =========================================================================

    /**
     * Adds an urgent service request directly to the FRONT of the Deque.
     * Urgent requests jump ahead of regular requests in the Deque.
     */
    public void scheduleUrgent(ServiceRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("ServiceRequest cannot be null.");
        }
        request.setStatus("URGENT");
        urgentDeque.addFront(request);
    }

    /**
     * Adds a normal service request to the REAR of the Deque.
     */
    public void scheduleDeque(ServiceRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("ServiceRequest cannot be null.");
        }
        if (request.getStatus() == null || request.getStatus().trim().isEmpty()) {
            request.setStatus("SCHEDULED_DEQUE");
        }
        urgentDeque.addRear(request);
    }

    /**
     * Removes and returns the front request from the Deque.
     * @return Next ServiceRequest from Deque, or null if Deque is empty.
     */
    public ServiceRequest getNextDequeRequest() {
        if (urgentDeque.isEmpty()) {
            return null;
        }
        return urgentDeque.removeFront();
    }

    /**
     * Peeks at the front request in the Deque without removing it.
     */
    public ServiceRequest peekNextDequeRequest() {
        if (urgentDeque.isEmpty()) {
            return null;
        }
        return urgentDeque.peekFront();
    }

    public int getDequeSize() {
        return urgentDeque.size();
    }

    public boolean isDequeEmpty() {
        return urgentDeque.isEmpty();
    }

    // =========================================================================
    // 4. Priority Queue / Min-Heap Operations
    // =========================================================================

    /**
     * Inserts a service request into the Min-Heap using the request's urgency score.
     * Lower numeric urgency values indicate higher priority (e.g., 1 = Emergency, 5 = Low).
     */
    public void scheduleByPriority(ServiceRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("ServiceRequest cannot be null.");
        }
        scheduleByPriority(request, request.getUrgency());
    }

    /**
     * Inserts a service request into the Min-Heap with an explicit priority integer.
     */
    public void scheduleByPriority(ServiceRequest request, int priority) {
        if (request == null) {
            throw new IllegalArgumentException("ServiceRequest cannot be null.");
        }
        if (request.getStatus() == null || request.getStatus().trim().isEmpty()) {
            request.setStatus("SCHEDULED_PRIORITY");
        }
        priorityHeap.insert(request, priority);
    }

    /**
     * Extracts and returns the highest priority (minimum urgency score) request.
     * @return Next ServiceRequest with highest priority, or null if priority heap is empty.
     */
    public ServiceRequest getNextPriorityRequest() {
        if (priorityHeap.isEmpty()) {
            return null;
        }
        return priorityHeap.extractMin();
    }

    /**
     * Peeks at the top priority request without extracting it.
     */
    public ServiceRequest peekNextPriorityRequest() {
        if (priorityHeap.isEmpty()) {
            return null;
        }
        return priorityHeap.peek();
    }

    public int getPriorityHeapSize() {
        return priorityHeap.size();
    }

    public boolean isPriorityHeapEmpty() {
        return priorityHeap.isEmpty();
    }

    // =========================================================================
    // 5. Unified Dispatcher / Main Question Answered
    // =========================================================================

    /**
     * Primary answer to: "Which service request should be handled next?"
     * 
     * Evaluation order across modes:
     *  1. Urgent Deque (front items: highest immediate override priority)
     *  2. Priority Min-Heap (sorted by urgency level)
     *  3. Standard FIFO Queue (standard arrival sequence)
     *  4. Circular Queue (buffer arrival sequence)
     * 
     * @return The highest-priority pending ServiceRequest across all queues, or null if all queues are empty.
     */
    public ServiceRequest getNextRequest() {
        if (!urgentDeque.isEmpty()) {
            return urgentDeque.removeFront();
        }
        if (!priorityHeap.isEmpty()) {
            return priorityHeap.extractMin();
        }
        if (!fifoQueue.isEmpty()) {
            return fifoQueue.dequeue();
        }
        if (!circularQueue.isEmpty()) {
            return circularQueue.dequeue();
        }
        return null;
    }

    /**
     * Peeks at the next request that would be returned by getNextRequest() without removing it.
     */
    public ServiceRequest peekNextRequest() {
        if (!urgentDeque.isEmpty()) {
            return urgentDeque.peekFront();
        }
        if (!priorityHeap.isEmpty()) {
            return priorityHeap.peek();
        }
        if (!fifoQueue.isEmpty()) {
            return fifoQueue.front();
        }
        if (!circularQueue.isEmpty()) {
            return circularQueue.peek();
        }
        return null;
    }

    /**
     * Checks if there are any pending service requests across all scheduling structures.
     */
    public boolean hasPendingRequests() {
        return !urgentDeque.isEmpty()
                || !priorityHeap.isEmpty()
                || !fifoQueue.isEmpty()
                || !circularQueue.isEmpty();
    }

    /**
     * Returns the total count of pending service requests across all queues.
     */
    public int getTotalPendingCount() {
        return urgentDeque.size()
                + priorityHeap.size()
                + fifoQueue.size()
                + circularQueue.size();
    }

    // =========================================================================
    // 6. Resource Assignment Helper
    // =========================================================================

    /**
     * Assigns an available Resource to a ServiceRequest and updates their statuses.
     */
    public boolean assignResource(ServiceRequest request, Resource resource) {
        if (request == null || resource == null) {
            return false;
        }
        request.setStatus("ASSIGNED_TO_R" + resource.getResourceId());
        resource.setAvailabilityStatus("BUSY");
        return true;
    }
}
