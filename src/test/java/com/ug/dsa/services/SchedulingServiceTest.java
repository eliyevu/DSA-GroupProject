package com.ug.dsa.services;

import com.ug.dsa.models.Resource;
import com.ug.dsa.models.ServiceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SchedulingServiceTest {

    private SchedulingService scheduler;

    @BeforeEach
    public void setUp() {
        scheduler = new SchedulingService(5);
    }

    @Test
    public void testInitialState() {
        assertFalse(scheduler.hasPendingRequests());
        assertEquals(0, scheduler.getTotalPendingCount());
        assertNull(scheduler.getNextRequest());
        assertNull(scheduler.peekNextRequest());
    }

    @Test
    public void testFifoScheduling() {
        ServiceRequest req1 = new ServiceRequest(1, 10, 20, "Medical", 2, "10:00", "11:00", "PENDING");
        ServiceRequest req2 = new ServiceRequest(2, 10, 30, "Security", 1, "10:05", "11:05", "PENDING");

        scheduler.scheduleFIFO(req1);
        scheduler.scheduleFIFO(req2);

        assertEquals(2, scheduler.getFifoQueueSize());
        assertEquals(req1, scheduler.peekNextFIFORequest());

        assertEquals(req1, scheduler.getNextFIFORequest());
        assertEquals(req2, scheduler.getNextFIFORequest());
        assertNull(scheduler.getNextFIFORequest());
        assertTrue(scheduler.isFifoEmpty());
    }

    @Test
    public void testCircularQueueScheduling() {
        for (int i = 1; i <= 5; i++) {
            ServiceRequest req = new ServiceRequest(i, 1, 2, "Maintenance", 3, "12:00", "13:00", "PENDING");
            scheduler.scheduleCircular(req);
        }

        assertTrue(scheduler.isCircularQueueFull());
        assertEquals(5, scheduler.getCircularQueueSize());

        // Attempting to exceed circular capacity should throw exception
        ServiceRequest overflow = new ServiceRequest(6, 1, 2, "Maintenance", 3, "12:00", "13:00", "PENDING");
        assertThrows(IllegalStateException.class, () -> scheduler.scheduleCircular(overflow));

        ServiceRequest firstOut = scheduler.getNextCircularRequest();
        assertEquals(1, firstOut.getRequestId());
        assertFalse(scheduler.isCircularQueueFull());
        assertEquals(4, scheduler.getCircularQueueSize());
    }

    @Test
    public void testUrgentDequeScheduling() {
        ServiceRequest normalReq = new ServiceRequest(100, 5, 6, "General", 4, "08:00", "09:00", "PENDING");
        ServiceRequest urgentReq = new ServiceRequest(999, 1, 1, "Fire Emergency", 1, "08:01", "08:15", "PENDING");

        scheduler.scheduleDeque(normalReq);
        scheduler.scheduleUrgent(urgentReq); // Urgent should jump to front

        assertEquals(2, scheduler.getDequeSize());
        assertEquals(urgentReq, scheduler.peekNextDequeRequest());

        ServiceRequest nextOut = scheduler.getNextDequeRequest();
        assertEquals(999, nextOut.getRequestId());
        assertEquals("URGENT", nextOut.getStatus());

        assertEquals(normalReq, scheduler.getNextDequeRequest());
        assertNull(scheduler.getNextDequeRequest());
    }

    @Test
    public void testPriorityQueueScheduling() {
        ServiceRequest lowPriority = new ServiceRequest(1, 1, 2, "Info", 5, "09:00", "10:00", "PENDING");
        ServiceRequest highPriority = new ServiceRequest(2, 1, 3, "Escort", 1, "09:01", "09:30", "PENDING");
        ServiceRequest mediumPriority = new ServiceRequest(3, 1, 4, "Transport", 3, "09:02", "10:00", "PENDING");

        scheduler.scheduleByPriority(lowPriority);
        scheduler.scheduleByPriority(highPriority);
        scheduler.scheduleByPriority(mediumPriority);

        assertEquals(3, scheduler.getPriorityHeapSize());

        // Min-Heap extracts lowest numeric urgency score first (1 -> 3 -> 5)
        assertEquals(2, scheduler.getNextPriorityRequest().getRequestId());
        assertEquals(3, scheduler.getNextPriorityRequest().getRequestId());
        assertEquals(1, scheduler.getNextPriorityRequest().getRequestId());
        assertNull(scheduler.getNextPriorityRequest());
    }

    @Test
    public void testUnifiedDispatcherPriorityOrder() {
        ServiceRequest fifoReq = new ServiceRequest(1, 1, 2, "FIFO", 3, "09:00", "10:00", "PENDING");
        ServiceRequest circularReq = new ServiceRequest(2, 1, 2, "Circular", 3, "09:01", "10:00", "PENDING");
        ServiceRequest priorityReq = new ServiceRequest(3, 1, 2, "Priority", 1, "09:02", "10:00", "PENDING");
        ServiceRequest urgentReq = new ServiceRequest(4, 1, 2, "Urgent", 1, "09:03", "10:00", "PENDING");

        scheduler.scheduleFIFO(fifoReq);
        scheduler.scheduleCircular(circularReq);
        scheduler.scheduleByPriority(priorityReq);
        scheduler.scheduleUrgent(urgentReq);

        assertEquals(4, scheduler.getTotalPendingCount());

        // Unified dispatch evaluation sequence: Urgent Deque -> Priority Heap -> FIFO Queue -> Circular Queue
        assertEquals(urgentReq, scheduler.getNextRequest());
        assertEquals(priorityReq, scheduler.getNextRequest());
        assertEquals(fifoReq, scheduler.getNextRequest());
        assertEquals(circularReq, scheduler.getNextRequest());
        assertNull(scheduler.getNextRequest());
    }

    @Test
    public void testAssignResource() {
        ServiceRequest req = new ServiceRequest(50, 1, 2, "Shuttle", 2, "14:00", "15:00", "PENDING");
        Resource res = new Resource(101, "Shuttle Van", 1, 10, "AVAILABLE");

        boolean assigned = scheduler.assignResource(req, res);
        assertTrue(assigned);
        assertEquals("ASSIGNED_TO_R101", req.getStatus());
        assertEquals("BUSY", res.getAvailabilityStatus());
    }

    @Test
    public void testNullInputsHandling() {
        assertThrows(IllegalArgumentException.class, () -> scheduler.scheduleFIFO(null));
        assertThrows(IllegalArgumentException.class, () -> scheduler.scheduleCircular(null));
        assertThrows(IllegalArgumentException.class, () -> scheduler.scheduleUrgent(null));
        assertThrows(IllegalArgumentException.class, () -> scheduler.scheduleByPriority(null));

        assertFalse(scheduler.assignResource(null, new Resource()));
        assertFalse(scheduler.assignResource(new ServiceRequest(), null));
    }
}
