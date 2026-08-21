package com.ug.dsa.services;

import com.ug.dsa.datastructures.DynamicArray;
import com.ug.dsa.datastructures.Graph;
import com.ug.dsa.datastructures.HashTable;
import com.ug.dsa.datastructures.Heap;
import com.ug.dsa.algorithms.BFS;
import com.ug.dsa.algorithms.Dijkstra;
import com.ug.dsa.algorithms.DFS;
import com.ug.dsa.algorithms.Kruskal;
import com.ug.dsa.algorithms.Prim;
import com.ug.dsa.models.AlgorithmRun;
import com.ug.dsa.models.AuditEvent;
import com.ug.dsa.models.Location;
import com.ug.dsa.models.Resource;
import com.ug.dsa.models.Road;
import com.ug.dsa.models.ServiceRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * SmartOperationsEngine
 *
 * Coordinates all major services and provides the overall operational workflow:
 *
 *   Service Request
 *         ↓
 *   Search / Validate   (IndexingService)
 *         ↓
 *   Schedule            (SchedulingService)
 *         ↓
 *   Allocate Resource   (OptimizationService)
 *         ↓
 *   Find Route          (RoutingService)
 *         ↓
 *   Produce Result
 *         ↓
 *   Save Audit Event &amp; Algorithm Run  (DataLoaderService)
 */
public class SmartOperationsEngine {

    // ── Services ──────────────────────────────────────────────────────────────
    private final DataLoaderService   dataLoader;
    private SchedulingService   scheduler;
    private IndexingService     indexer;
    private       RoutingService      router;
    private final OptimizationService optimizer;

    // ── Counters for stable ID generation ────────────────────────────────────
    private int nextAuditId    = 1000;
    private int nextAlgoRunId  = 500;

    private static final DateTimeFormatter TIMESTAMP_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // =========================================================================
    //  Construction
    // =========================================================================

    public SmartOperationsEngine() {
        this.dataLoader = new DataLoaderService();
        this.scheduler  = new SchedulingService();
        this.indexer    = new IndexingService();
        this.optimizer  = new OptimizationService();
        this.router     = null; // built after data is loaded
    }

    // =========================================================================
    //  1. Load / Reload data
    // =========================================================================

    /**
     * Loads all data from PostgreSQL (with CSV fallback), builds the graph,
     * and populates the indexing + scheduling structures.
     *
     * @return Human-readable summary line.
     */
    public String loadOrReloadData() {
        String summary = dataLoader.loadAll();

        // Rebuild state on every reload so queues and indexes do not contain
        // references to the previous dataset.
        this.scheduler = new SchedulingService();
        this.indexer = new IndexingService();

        Graph graph = dataLoader.getNetworkGraph();
        HashTable<Integer, Integer> idToIdx = dataLoader.getLocationIdToIndex();
        HashTable<Integer, Location> idxToLoc = dataLoader.getIndexToLocation();
        this.router = new RoutingService(graph, idToIdx, idxToLoc);
        this.optimizer.setRoutingService(this.router);

        // DataLoader bridges the loaded models into the custom index structures.
        dataLoader.loadIntoIndexes(indexer);

        // Requests are intentionally not preloaded into every queue. The user
        // chooses FIFO, priority, urgent deque, or circular scheduling explicitly.
        return summary;
    }

    // =========================================================================
    //  2. View service requests
    // =========================================================================

    public DynamicArray<ServiceRequest> getServiceRequests() {
        return dataLoader.getServiceRequests();
    }

    // =========================================================================
    //  3. Search service requests
    // =========================================================================

    /**
     * Searches requests by id, category, status, or source/destination location.
     *
     * @param field "id" | "category" | "status" | "source" | "destination"
     * @param value search value
     */
    public DynamicArray<ServiceRequest> searchServiceRequests(String field, String value) {
        DynamicArray<ServiceRequest> results = new DynamicArray<>();
        DynamicArray<ServiceRequest> all = dataLoader.getServiceRequests();

        if (all == null || all.size() == 0) return results;

        String lf = field.toLowerCase().trim();
        String lv = value.toLowerCase().trim();

        switch (lf) {
            case "id": {
                try {
                    int id = Integer.parseInt(lv);
                    ServiceRequest r = indexer.findRequestById(id);
                    if (r != null) results.add(r);
                } catch (NumberFormatException ignored) {}
                break;
            }
            case "category": {
                DynamicArray<?> found = indexer.findRequestsByCategory(value.trim().toUpperCase());
                for (int i = 0; i < found.size(); i++) {
                    results.add((ServiceRequest) found.get(i));
                }
                break;
            }
            default: {
                // Linear scan for status / source / destination
                for (int i = 0; i < all.size(); i++) {
                    ServiceRequest r = all.get(i);
                    boolean match = false;
                    switch (lf) {
                        case "status":      match = r.getStatus()   != null && r.getStatus().toLowerCase().contains(lv); break;
                        case "source":      match = String.valueOf(r.getSource()).equals(lv); break;
                        case "destination": match = String.valueOf(r.getDestination()).equals(lv); break;
                        default:
                            // search across all text fields
                            match = String.valueOf(r.getRequestId()).equals(lv)
                                    || (r.getCategory() != null && r.getCategory().toLowerCase().contains(lv))
                                    || (r.getStatus() != null && r.getStatus().toLowerCase().contains(lv));
                    }
                    if (match) results.add(r);
                }
            }
        }
        return results;
    }

    // =========================================================================
    //  4. Schedule requests
    // =========================================================================

    /**
     * Schedules pending service requests into the chosen queue mode.
     *
     * @param mode "fifo" | "priority" | "urgent" | "circular"
     * @param limit max number of requests to schedule (0 = all pending)
     * @return number of requests scheduled
     */
    public int scheduleRequests(String mode, int limit) {
        DynamicArray<ServiceRequest> all = dataLoader.getServiceRequests();
        int count = 0;
        for (int i = 0; i < all.size(); i++) {
            if (limit > 0 && count >= limit) break;
            ServiceRequest r = all.get(i);
            if (!"PENDING".equals(r.getStatus())) continue;
            switch (mode.toLowerCase()) {
                case "priority": scheduler.scheduleByPriority(r); break;
                case "urgent":   scheduler.scheduleUrgent(r);     break;
                case "circular":
                    if (!scheduler.isCircularQueueFull())
                        scheduler.scheduleCircular(r);
                    break;
                default:         scheduler.scheduleFIFO(r);       break;
            }
            count++;
        }
        return count;
    }

    /**
     * Dispatches (dequeues) the next request from the chosen mode.
     */
    public ServiceRequest dispatchNext(String mode) {
        switch (mode.toLowerCase()) {
            case "priority": return scheduler.getNextPriorityRequest();
            case "urgent":   return scheduler.getNextDequeRequest();
            case "circular": return scheduler.getNextCircularRequest();
            default:         return scheduler.getNextFIFORequest();
        }
    }

    public SchedulingService getScheduler() { return scheduler; }

    // =========================================================================
    //  5 & 6. Routing
    // =========================================================================

    /**
     * Runs Dijkstra between two location IDs.
     * Returns a formatted result string describing the route.
     */
    public String findShortestRoute(int srcLocationId, int destLocationId) {
        requireRouter();
        long start = System.nanoTime();

        DynamicArray<Location> path = router.findShortestRoute(srcLocationId, destLocationId);
        int scaledDistance = router.findShortestDistance(srcLocationId, destLocationId);

        long elapsed = System.nanoTime() - start;
        recordAlgoRun("Dijkstra", dataLoader.getLocations().size(), elapsed);

        Location srcLoc  = indexer.findLocation(srcLocationId);
        Location destLoc = indexer.findLocation(destLocationId);
        String srcName  = srcLoc  != null ? srcLoc.getName()  : "Location " + srcLocationId;
        String destName = destLoc != null ? destLoc.getName() : "Location " + destLocationId;

        StringBuilder sb = new StringBuilder();
        sb.append("  Route: ").append(srcName).append(" → ").append(destName).append("\n");
        sb.append("  Path nodes: ");
        for (int i = 0; i < path.size(); i++) {
            Location loc = path.get(i);
            if (loc != null) {
                sb.append(loc.getName());
            } else {
                sb.append("?");
            }
            if (i < path.size() - 1) sb.append(" → ");
        }
        if (path.isEmpty()) {
            sb.append("\n  No route exists between the selected locations.");
        } else if (scaledDistance != Integer.MAX_VALUE) {
            sb.append("\n  Shortest distance: ")
                    .append(String.format("%.2f", scaledDistance / 100.0))
                    .append(" distance units");
        }
        sb.append("\n  Algorithm: Dijkstra | Time: ").append(formatNs(elapsed));
        return sb.toString();
    }

    /**
     * Runs BFS from a starting location and returns all reachable locations.
     */
    public DynamicArray<Location> findReachableLocations(int startLocationId) {
        requireRouter();
        long start = System.nanoTime();

        DynamicArray<Location> reachable = router.findReachableLocations(startLocationId);

        long elapsed = System.nanoTime() - start;
        recordAlgoRun("BFS", dataLoader.getLocations().size(), elapsed);

        return reachable;
    }

    /** Runs DFS from a starting location. */
    public DynamicArray<Location> findDepthFirstLocations(int startLocationId) {
        requireRouter();
        long start = System.nanoTime();
        DynamicArray<Location> result = router.depthFirstLocations(startLocationId);
        recordAlgoRun("DFS", dataLoader.getLocations().size(), System.nanoTime() - start);
        return result;
    }

    /** Builds the campus minimum network using Prim's algorithm. */
    public DynamicArray<Road> buildMinimumNetworkPrim() {
        requireRouter();
        long start = System.nanoTime();
        DynamicArray<Road> result = router.buildMinimumNetworkPrim();
        recordAlgoRun("Prim MST", dataLoader.getNetworkGraph().getNumEdges(), System.nanoTime() - start);
        return result;
    }

    /** Builds the campus minimum network using Kruskal's algorithm. */
    public DynamicArray<Road> buildMinimumNetworkKruskal() {
        requireRouter();
        long start = System.nanoTime();
        DynamicArray<Road> result = router.buildMinimumNetwork();
        recordAlgoRun("Kruskal MST", dataLoader.getNetworkGraph().getNumEdges(), System.nanoTime() - start);
        return result;
    }

    // =========================================================================
    //  7. Optimize resource allocation
    // =========================================================================

    /**
     * Full end-to-end workflow: picks first available resource, runs Greedy vs DP,
     * logs audit events, returns result string.
     */
    public String optimizeResourceAllocation(int capacity) {
        DynamicArray<ServiceRequest> pendingRequests = new DynamicArray<>();
        DynamicArray<ServiceRequest> all = dataLoader.getServiceRequests();
        for (int i = 0; i < all.size() && pendingRequests.size() < 20; i++) {
            if ("PENDING".equals(all.get(i).getStatus())) {
                pendingRequests.add(all.get(i));
            }
        }

        if (pendingRequests.size() == 0) {
            return "  No pending service requests to optimize.";
        }

        // Find an available resource
        Resource resource = null;
        DynamicArray<Resource> resources = dataLoader.getResources();
        for (int i = 0; i < resources.size(); i++) {
            if ("AVAILABLE".equals(resources.get(i).getAvailabilityStatus())) {
                resource = resources.get(i);
                break;
            }
        }
        if (resource == null) {
            resource = new Resource(99, "VIRTUAL_RESOURCE", 1, capacity, "AVAILABLE");
        }

        // Greedy
        long t1 = System.nanoTime();
        DynamicArray<ServiceRequest> greedyResult = optimizer.allocateResources(pendingRequests, resource);
        long greedyTime = System.nanoTime() - t1;

        // DP (Knapsack)
        long t2 = System.nanoTime();
        DynamicArray<ServiceRequest> dpResult = optimizer.selectRequests(pendingRequests, capacity);
        long dpTime = System.nanoTime() - t2;

        recordAlgoRun("GreedyAllocation", pendingRequests.size(), greedyTime);
        recordAlgoRun("Knapsack",         pendingRequests.size(), dpTime);

        // Audit events for allocations
        String ts = LocalDateTime.now().format(TIMESTAMP_FMT);
        if (greedyResult.size() > 0) {
            logAudit(AuditEvent.EventType.RESOURCE_ALLOCATED,
                    greedyResult.get(0).getRequestId(), ts,
                    "Greedy allocated resource " + resource.getResourceId() + " to " + greedyResult.size() + " request(s)");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("  Pending requests evaluated : ").append(pendingRequests.size()).append("\n");
        sb.append("  Resource used              : ").append(resource.getType())
                .append(" (ID=").append(resource.getResourceId())
                .append(", capacity=").append(resource.getCapacity()).append(")\n");
        sb.append("\n  --- Greedy Result ---\n");
        sb.append("  Requests selected : ").append(greedyResult.size()).append("\n");
        sb.append("  Time              : ").append(formatNs(greedyTime)).append("\n");
        printRequestList(sb, greedyResult, 5);

        sb.append("\n  --- DP (Knapsack) Result ---\n");
        sb.append("  Requests selected : ").append(dpResult.size()).append("\n");
        sb.append("  Time              : ").append(formatNs(dpTime)).append("\n");
        printRequestList(sb, dpResult, 5);

        int diff = dpResult.size() - greedyResult.size();
        if (diff > 0) {
            sb.append("\n  ★ DP selected ").append(diff).append(" more request(s) than Greedy.");
            sb.append("\n  ⇒ Greedy failure: it ignores combinations that yield higher total value.");
        } else if (diff == 0) {
            sb.append("\n  Both algorithms selected the same number of requests.");
        } else {
            sb.append("\n  Greedy selected ").append(-diff).append(" more request(s) this time.");
        }
        return sb.toString();
    }

    // =========================================================================
    //  8. Run algorithm performance test
    // =========================================================================

    /**
     * Runs timed benchmarks on sorting, searching, and graph algorithms and
     * records results in algorithmRuns.
     *
     * @return formatted report string
     */
    public String runPerformanceTests() {
        StringBuilder sb = new StringBuilder();
        sb.append("  Running algorithm benchmarks...\n\n");

        DynamicArray<ServiceRequest> requests = dataLoader.getServiceRequests();
        int n = requests.size();
        if (n == 0) {
            return "  No data loaded. Please load data first (option 1).";
        }

        // -- Scheduling / Queue benchmark (SchedulingService) --
        {
            long t = System.nanoTime();
            SchedulingService bench = new SchedulingService();
            for (int i = 0; i < n; i++) bench.scheduleFIFO(requests.get(i));
            for (int i = 0; i < n; i++) bench.getNextFIFORequest();
            long elapsed = System.nanoTime() - t;
            recordAlgoRun("FIFO Queue (enqueue+dequeue)", n, elapsed);
            sb.append(String.format("  %-42s n=%-6d  %s%n", "FIFO Queue (enqueue+dequeue)", n, formatNs(elapsed)));
        }

        // -- Priority Heap benchmark --
        {
            long t = System.nanoTime();
            SchedulingService bench = new SchedulingService();
            for (int i = 0; i < n; i++) bench.scheduleByPriority(requests.get(i));
            for (int i = 0; i < n; i++) bench.getNextPriorityRequest();
            long elapsed = System.nanoTime() - t;
            recordAlgoRun("Priority Heap (insert+extractMin)", n, elapsed);
            sb.append(String.format("  %-42s n=%-6d  %s%n", "Priority Heap (insert+extractMin)", n, formatNs(elapsed)));
        }

        // -- Indexing benchmark (HashTable) --
        {
            long t = System.nanoTime();
            IndexingService bench = new IndexingService();
            for (int i = 0; i < n; i++) bench.indexRequest(requests.get(i));
            for (int i = 0; i < n; i++) bench.findRequestById(requests.get(i).getRequestId());
            long elapsed = System.nanoTime() - t;
            recordAlgoRun("HashTable Index (index+lookup)", n, elapsed);
            sb.append(String.format("  %-42s n=%-6d  %s%n", "HashTable Index (index+lookup)", n, formatNs(elapsed)));
        }

        // -- BFS benchmark --
        Graph graph = dataLoader.getNetworkGraph();
        if (graph != null && graph.getNumVertices() > 0) {
            long t = System.nanoTime();
            BFS.traverse(graph, 0);
            long elapsed = System.nanoTime() - t;
            recordAlgoRun("BFS", graph.getNumVertices(), elapsed);
            sb.append(String.format("  %-42s n=%-6d  %s%n", "BFS", graph.getNumVertices(), formatNs(elapsed)));
        }

        // -- DFS benchmark --
        if (graph != null && graph.getNumVertices() > 0) {
            long t = System.nanoTime();
            DFS.iterative(graph, 0);
            long elapsed = System.nanoTime() - t;
            recordAlgoRun("DFS", graph.getNumVertices(), elapsed);
            sb.append(String.format("  %-42s n=%-6d  %s%n", "DFS", graph.getNumVertices(), formatNs(elapsed)));
        }

        // -- Dijkstra benchmark --
        if (graph != null && graph.getNumVertices() > 1) {
            long t = System.nanoTime();
            Heap<Integer> heap = new Heap<>();
            Dijkstra dijkstra = new Dijkstra(graph, heap);
            dijkstra.shortestPath(0);
            long elapsed = System.nanoTime() - t;
            recordAlgoRun("Dijkstra", graph.getNumVertices(), elapsed);
            sb.append(String.format("  %-42s n=%-6d  %s%n", "Dijkstra", graph.getNumVertices(), formatNs(elapsed)));
        }

        // -- Prim benchmark --
        if (graph != null && graph.getNumVertices() > 0) {
            long t = System.nanoTime();
            Prim.findMST(graph);
            long elapsed = System.nanoTime() - t;
            recordAlgoRun("Prim MST", graph.getNumEdges(), elapsed);
            sb.append(String.format("  %-42s n=%-6d  %s%n", "Prim MST", graph.getNumEdges(), formatNs(elapsed)));
        }

        // -- Kruskal benchmark --
        if (graph != null && graph.getNumEdges() > 0) {
            long t = System.nanoTime();
            Kruskal.findMST(graph);
            long elapsed = System.nanoTime() - t;
            recordAlgoRun("Kruskal MST", graph.getNumEdges(), elapsed);
            sb.append(String.format("  %-42s n=%-6d  %s%n", "Kruskal MST", graph.getNumEdges(), formatNs(elapsed)));
        }

        // -- Greedy Allocation benchmark --
        {
            DynamicArray<ServiceRequest> sample = new DynamicArray<>();
            for (int i = 0; i < Math.min(50, n); i++) sample.add(requests.get(i));
            Resource r = new Resource(0, "BENCH", 1, 1000, "AVAILABLE");
            long t = System.nanoTime();
            optimizer.allocateResources(sample, r);
            long elapsed = System.nanoTime() - t;
            recordAlgoRun("GreedyAllocation", sample.size(), elapsed);
            sb.append(String.format("  %-42s n=%-6d  %s%n", "GreedyAllocation", sample.size(), formatNs(elapsed)));
        }

        // -- Knapsack benchmark --
        {
            DynamicArray<ServiceRequest> sample = new DynamicArray<>();
            for (int i = 0; i < Math.min(50, n); i++) sample.add(requests.get(i));
            long t = System.nanoTime();
            optimizer.selectRequests(sample, 500);
            long elapsed = System.nanoTime() - t;
            recordAlgoRun("Knapsack", sample.size(), elapsed);
            sb.append(String.format("  %-42s n=%-6d  %s%n", "Knapsack", sample.size(), formatNs(elapsed)));
        }

        return sb.toString();
    }

    // =========================================================================
    //  9. View algorithm runs
    // =========================================================================

    public DynamicArray<AlgorithmRun> getAlgorithmRuns() {
        return dataLoader.getAlgorithmRuns();
    }

    // =========================================================================
    //  10. View audit events
    // =========================================================================

    public DynamicArray<AuditEvent> getAuditEvents() {
        return dataLoader.getAuditEvents();
    }

    // =========================================================================
    //  Full end-to-end workflow helper
    // =========================================================================

    /**
     * Demonstrates the complete workflow for a given service request ID:
     * Validate → Schedule → Allocate → Route → Log.
     */
    public String processServiceRequestWorkflow(int requestId) {
        StringBuilder sb = new StringBuilder();
        String ts = LocalDateTime.now().format(TIMESTAMP_FMT);

        // Step 1: Search / validate
        sb.append("  [1] Search & Validate request #").append(requestId).append("...\n");
        ServiceRequest request = indexer.findRequestById(requestId);
        if (request == null) {
            return "  ✗ Request #" + requestId + " not found.";
        }
        sb.append("      Found: ").append(request.getCategory())
                .append(" | Urgency=").append(request.getUrgency())
                .append(" | Status=").append(request.getStatus()).append("\n");
        logAudit(AuditEvent.EventType.REQUEST_CREATED, requestId, ts, "Request validated");

        // Step 2: Schedule
        sb.append("  [2] Scheduling (priority queue)...\n");
        scheduler.scheduleByPriority(request, request.getUrgency());
        ServiceRequest next = scheduler.getNextPriorityRequest();
        sb.append("      Dispatched: #").append(next != null ? next.getRequestId() : "none").append("\n");

        // Step 3: Allocate resource
        sb.append("  [3] Allocating resource...\n");
        Resource resource = null;
        DynamicArray<Resource> resources = dataLoader.getResources();
        for (int i = 0; i < resources.size(); i++) {
            if ("AVAILABLE".equals(resources.get(i).getAvailabilityStatus())) {
                resource = resources.get(i);
                break;
            }
        }
        if (resource == null) {
            sb.append("      ✗ No available resources.\n");
        } else {
            scheduler.assignResource(request, resource);
            sb.append("      Assigned: ").append(resource.getType())
                    .append(" (ID=").append(resource.getResourceId()).append(")\n");
            logAudit(AuditEvent.EventType.RESOURCE_ALLOCATED, requestId, ts,
                    "Resource " + resource.getResourceId() + " assigned");
        }

        // Step 4: Find route
        sb.append("  [4] Finding route...\n");
        try {
            requireRouter();
            String route = findShortestRoute(request.getSource(), request.getDestination());
            sb.append(route.replace("  ", "      ")).append("\n");
        } catch (Exception e) {
            sb.append("      Route: (source=").append(request.getSource())
                    .append(" → dest=").append(request.getDestination()).append(")\n");
        }

        // Step 5: Mark complete and log
        sb.append("  [5] Completing & logging...\n");
        request.setStatus("IN_PROGRESS");
        logAudit(AuditEvent.EventType.REQUEST_ASSIGNED, requestId, ts, "Request set to IN_PROGRESS");
        sb.append("      Status updated. Audit event logged.\n");

        return sb.toString();
    }

    // =========================================================================
    //  Private helpers
    // =========================================================================

    private void requireRouter() {
        if (router == null) {
            throw new IllegalStateException("Data not loaded. Please choose option 1 first.");
        }
    }

    private void logAudit(AuditEvent.EventType type, int requestId, String ts, String desc) {
        AuditEvent event = new AuditEvent(nextAuditId++, type, requestId, ts, desc);
        dataLoader.addAuditEvent(event);
    }

    private void recordAlgoRun(String name, int n, long timeNs) {
        Runtime rt = Runtime.getRuntime();
        double memKb = (rt.totalMemory() - rt.freeMemory()) / 1024.0;
        String today = java.time.LocalDate.now().toString();
        dataLoader.addAlgorithmRun(new AlgorithmRun(nextAlgoRunId++, name, n, timeNs, memKb, today));
    }

    private String formatNs(long ns) {
        if (ns < 1_000L)         return ns + " ns";
        if (ns < 1_000_000L)     return String.format("%.2f µs", ns / 1_000.0);
        if (ns < 1_000_000_000L) return String.format("%.2f ms", ns / 1_000_000.0);
        return String.format("%.3f s", ns / 1_000_000_000.0);
    }

    private void printRequestList(StringBuilder sb, DynamicArray<ServiceRequest> list, int max) {
        int shown = Math.min(list.size(), max);
        for (int i = 0; i < shown; i++) {
            ServiceRequest r = list.get(i);
            sb.append(String.format("    #%-4d %-15s urgency=%-2d  src=%-3d → dest=%-3d  %s%n",
                    r.getRequestId(), r.getCategory(), r.getUrgency(),
                    r.getSource(), r.getDestination(), r.getStatus()));
        }
        if (list.size() > max) {
            sb.append("    ... and ").append(list.size() - max).append(" more.\n");
        }
    }
}
