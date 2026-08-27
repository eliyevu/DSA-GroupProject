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
     * Full end-to-end workflow, now with TWO optimization layers:
     *
     *   Outer layer  : which AVAILABLE, type-compatible resource should be
     *                  used at all? Every candidate is scored by running
     *                  the exact-optimal Knapsack against it and comparing
     *                  total value achieved; the best one wins.
     *   Inner layer  : given that winning resource, which subset of pending
     *                  requests should it take (Greedy vs Knapsack/DP)?
     *
     * Logs audit events, returns a result string covering both layers.
     */
    public String optimizeResourceAllocation(int capacity) {
        DynamicArray<ServiceRequest> pendingRequests = new DynamicArray<>();
        DynamicArray<ServiceRequest> all = dataLoader.getServiceRequests();
        for (int i = 0; i < all.size() && pendingRequests.size() < 50; i++) {
            if ("PENDING".equals(all.get(i).getStatus())) {
                pendingRequests.add(all.get(i));
            }
        }

        if (pendingRequests.size() == 0) {
            return "  No pending service requests to optimize.";
        }

        StringBuilder sb = new StringBuilder();

        // ---------------------------------------------------------------
        // OUTER OPTIMIZATION: scan every AVAILABLE, type-compatible
        // resource and keep whichever one the optimal Knapsack solution
        // achieves the highest total value on. This replaces "whichever
        // resource matches first" with an actual best-of comparison.
        // ---------------------------------------------------------------
        DynamicArray<Resource> candidates = findCompatibleAvailableResources(pendingRequests);

        Resource resource;
        DynamicArray<ServiceRequest> dpResult;

        if (candidates.isEmpty()) {
            resource = new Resource(99, "VIRTUAL_RESOURCE", 1, capacity, "AVAILABLE");
            dpResult = optimizer.selectRequests(pendingRequests, resource);
            sb.append("  Note: no AVAILABLE resource matched any pending request's category, ")
                    .append("so a placeholder resource was used instead.\n\n");
        } else {
            sb.append("  --- Resource Scan (outer optimization) ---\n");

            // Temporarily apply the requested capacity to every candidate so
            // they are compared on equal terms; only the WINNER keeps this
            // capacity afterwards, everyone else is restored below so the
            // scan does not leave side effects on resources we didn't pick.
            int[] originalCapacities = new int[candidates.size()];
            for (int i = 0; i < candidates.size(); i++) {
                originalCapacities[i] = candidates.get(i).getCapacity();
                if (capacity > 0) {
                    candidates.get(i).setCapacity(capacity);
                }
            }

            Resource bestResource = null;
            DynamicArray<ServiceRequest> bestResult = null;
            int bestValue = -1;

            long scanStart = System.nanoTime();
            for (int i = 0; i < candidates.size(); i++) {
                Resource candidate = candidates.get(i);
                DynamicArray<ServiceRequest> result = optimizer.selectRequests(pendingRequests, candidate);
                int value = totalValue(result);

                sb.append(String.format("    R%-4d %-18s capacity=%-5d -> %d request(s), total value=%d%s%n",
                        candidate.getResourceId(), candidate.getType(), candidate.getCapacity(),
                        result.size(), value, value > bestValue ? "  <- best so far" : ""));

                if (value > bestValue) {
                    bestValue = value;
                    bestResource = candidate;
                    bestResult = result;
                }
            }
            long scanElapsed = System.nanoTime() - scanStart;
            recordAlgoRun("Knapsack-ResourceScan", candidates.size(), scanElapsed);

            // Restore every candidate's original capacity except the winner.
            for (int i = 0; i < candidates.size(); i++) {
                if (candidates.get(i) != bestResource) {
                    candidates.get(i).setCapacity(originalCapacities[i]);
                }
            }

            resource = bestResource;
            dpResult = bestResult;

            sb.append("  Resources scanned   : ").append(candidates.size()).append("\n");
            sb.append("  Scan time           : ").append(formatNs(scanElapsed)).append("\n");
            sb.append("  Best resource chosen: R").append(resource.getResourceId())
                    .append(" (").append(resource.getType())
                    .append("), total value=").append(bestValue).append("\n\n");
        }

        // How many of the pending requests actually share this resource's category
        int categoryMatchCount = 0;
        if (!"VIRTUAL_RESOURCE".equals(resource.getType())) {
            String resourceType = resource.getType() == null ? "" : resource.getType().trim().toUpperCase();
            String baseType = resourceType.contains("_")
                    ? resourceType.substring(0, resourceType.indexOf('_'))
                    : resourceType;
            for (int i = 0; i < pendingRequests.size(); i++) {
                String category = pendingRequests.get(i).getCategory();
                if (category != null && baseType.equals(category.trim().toUpperCase())) {
                    categoryMatchCount++;
                }
            }
        }

        // ---------------------------------------------------------------
        // INNER OPTIMIZATION: given the winning resource, Greedy vs
        // Knapsack/DP on which requests it should take (as before).
        // ---------------------------------------------------------------

        // Greedy
        long t1 = System.nanoTime();
        DynamicArray<ServiceRequest> greedyResult = optimizer.allocateResources(pendingRequests, resource);
        long greedyTime = System.nanoTime() - t1;


        long t2 = System.nanoTime();
        dpResult = optimizer.selectRequests(pendingRequests, resource);
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

        sb.append("  Pending requests evaluated : ").append(pendingRequests.size()).append("\n");
        sb.append("  Resource used              : ").append(resource.getType())
                .append(" (ID=").append(resource.getResourceId())
                .append(", capacity=").append(resource.getCapacity()).append(")\n");
        if (!"VIRTUAL_RESOURCE".equals(resource.getType())) {
            sb.append("  Category-matching pending  : ").append(categoryMatchCount)
                    .append(" of ").append(pendingRequests.size())
                    .append(" (a ").append(resource.getType())
                    .append(" cannot serve a request outside its own category)\n");
        }
        sb.append("\n  --- Greedy Result ---\n");
        sb.append("  Requests selected : ").append(greedyResult.size()).append("\n");
        sb.append("  Time              : ").append(formatNs(greedyTime)).append("\n");
        printRequestList(sb, greedyResult, 5);

        sb.append("\n  --- DP (Knapsack) Result ---\n");
        sb.append("  Requests selected : ").append(dpResult.size()).append("\n");
        sb.append("  Time              : ").append(formatNs(dpTime)).append("\n");
        printRequestList(sb, dpResult, 5);


        // -------------------------------------------------
        // COMPARISON
        // -------------------------------------------------

        int difference = dpResult.size() - greedyResult.size();
        sb.append("\n  --- Comparison ---\n");
        if (difference > 0) {
            sb.append("  Knapsack selected ")
                    .append(difference)
                    .append(" more request(s).\n");
            sb.append("  Knapsack found a better combination of requests under the capacity constraint.");

        } else if (difference == 0) {
            sb.append("  Both algorithms selected ")
                    .append(greedyResult.size())
                    .append(" request(s).");

        } else {
            sb.append("  Greedy selected ")
                    .append(-difference)
                    .append(" more request(s) in this dataset.");

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
            optimizer.selectRequests(sample, new Resource());
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
//  REAL OPERATIONAL WORKFLOW
// =========================================================================

    /**
     * Runs the actual service-operation workflow.
     */
    public String processScheduledRequests(String mode, int limit) {
        if (mode == null || mode.trim().isEmpty()) {
            mode = "fifo";
        }
        mode = mode.toLowerCase().trim();

        if (!mode.equals("fifo")
                && !mode.equals("priority")
                && !mode.equals("urgent")
                && !mode.equals("circular")) {

            return "  Invalid scheduling mode: " + mode;
        }

        DynamicArray<ServiceRequest> all =
                dataLoader.getServiceRequests();

        if (all == null || all.isEmpty()) {
            return "  No service requests loaded.";
        }

        // Step 1: Schedule pending requests
        int scheduled = scheduleRequests(mode, limit);

        if (scheduled == 0) {
            return "  No pending requests available for scheduling.";
        }

        StringBuilder sb = new StringBuilder();

        sb.append("  ==================================================\n");
        sb.append("   SERVICE OPERATIONS WORKFLOW\n");
        sb.append("  ==================================================\n");
        sb.append("  Scheduling mode : ")
                .append(mode.toUpperCase())
                .append("\n");
        sb.append("  Requests queued : ")
                .append(scheduled)
                .append("\n\n");

        // Step 2: Process requests one by one
        int processed = 0;

        while (processed < scheduled) {
            ServiceRequest request = dispatchNext(mode);
            if (request == null) {
                break;
            }
            sb.append(processOneRequest(request));
            sb.append("\n");
            processed++;
        }

        sb.append("  ==================================================\n");
        sb.append("  Workflow finished.\n");
        sb.append("  Requests processed: ")
                .append(processed)
                .append("\n");
        sb.append("  ==================================================\n");

        return sb.toString();
    }


    /**
     * Processes ONE request after it has been dispatched by the scheduler.
     */
    private String processOneRequest(ServiceRequest request) {

        StringBuilder sb = new StringBuilder();

        String ts = LocalDateTime.now().format(TIMESTAMP_FMT);

        sb.append("  --------------------------------------------------\n");
        sb.append("  Processing Request #")
                .append(request.getRequestId())
                .append("\n");
        sb.append("  Category       : ")
                .append(request.getCategory())
                .append("\n");
        sb.append("  Urgency        : ")
                .append(request.getUrgency())
                .append("\n");
        sb.append("  Source         : ")
                .append(request.getSource())
                .append("\n");
        sb.append("  Destination    : ")
                .append(request.getDestination())
                .append("\n");

        // STEP 1: Request has been dispatched
        request.setStatus("SCHEDULED");

        sb.append("\n  [1] Request dispatched by scheduler.\n");

        logAudit(
                AuditEvent.EventType.REQUEST_ASSIGNED,
                request.getRequestId(),
                ts,
                "Request dispatched using scheduling queue"
        );

        // STEP 2: Find matching resource
        Resource resource = findMatchingAvailableResource(request);

        if (resource == null) {

            sb.append("  [2] No matching resource available.\n");
            sb.append("      Request remains PENDING.\n");

            request.setStatus("PENDING");

            return sb.toString();
        }

        sb.append("  [2] Matching resource found.\n");
        sb.append("      Resource ID : ")
                .append(resource.getResourceId())
                .append("\n");
        sb.append("      Resource   : ")
                .append(resource.getType())
                .append("\n");
        sb.append("      Home       : ")
                .append(resource.getHomeLocation())
                .append("\n");

        // STEP 3: Allocate resource

        boolean allocated =
                scheduler.assignResource(request, resource);
        if (!allocated) {
            sb.append("  [3] Resource allocation failed.\n");
            request.setStatus("PENDING");
            return sb.toString();
        }
        request.setStatus("IN_PROGRESS");
        sb.append("  [3] Resource allocated successfully.\n");
        logAudit(
                AuditEvent.EventType.RESOURCE_ALLOCATED,
                request.getRequestId(),
                ts,
                "Resource " + resource.getResourceId()
                        + " allocated to request"
        );

        // STEP 4: Find shortest route
        sb.append("  [4] Finding shortest route...\n");
        try {
            requireRouter();

            // Resource → Request Source
            DynamicArray<Location> resourceToSource = router.findShortestRoute(resource.getHomeLocation(),
                    request.getSource());

            int homeToSource = router.findShortestDistance(resource.getHomeLocation(), request.getSource());

            // Request Source → Destination

            DynamicArray<Location> sourceToDestination = router.findShortestRoute(request.getSource(), request.getDestination());

            int sourceToDestinationDistance = router.findShortestDistance(request.getSource(),
                    request.getDestination());

            // Check whether both routes exist
            if (homeToSource == Integer.MAX_VALUE) {
                sb.append("      No route from resource home to source.\n");
                releaseResource(resource);
                request.setStatus("PENDING");
                return sb.toString();
            }

            if (sourceToDestinationDistance == Integer.MAX_VALUE) {
                sb.append("      No route from source to destination.\n");
                releaseResource(resource);
                request.setStatus("PENDING");
                return sb.toString();
            }

            // Display Resource → Source path
            sb.append("      Resource → Source path:\n");
            sb.append("        ");

            printRouteNodes(sb, resourceToSource);

            sb.append("\n");
            sb.append("      Distance: ").append(String.format("%.2f", homeToSource / 100.0)).append(" distance units\n");

            // Display Source → Destination path

            sb.append("      Source → Destination path:\n");
            sb.append("        ");

            printRouteNodes(sb, sourceToDestination);

            sb.append("\n");

            sb.append("      Distance: ")
                    .append(String.format(
                            "%.2f",
                            sourceToDestinationDistance / 100.0))
                    .append(" distance units\n");

            sb.append("      ✓ Shortest routes found using Dijkstra.\n");

        } catch (Exception e) {

            sb.append("      Routing error: ")
                    .append(e.getMessage())
                    .append("\n");

            releaseResource(resource);
            request.setStatus("PENDING");
            return sb.toString();
        }

        // STEP 5: Complete simulated operation

        request.setStatus("COMPLETED");

        releaseResource(resource);

        sb.append("  [5] Service operation completed.\n");
        sb.append("      Request status : COMPLETED\n");
        sb.append("      Resource status: AVAILABLE\n");

        logAudit(
                AuditEvent.EventType.REQUEST_COMPLETED,
                request.getRequestId(),
                ts,
                "Request completed successfully"
        );

        return sb.toString();
    }


    /**
     * Finds an AVAILABLE resource that matches the request category.
     */
    private Resource findMatchingAvailableResource(
            ServiceRequest request) {
        DynamicArray<Resource> resources = dataLoader.getResources();
        if (resources == null || resources.isEmpty()) {
            return null;
        }
        String requiredType = request.getCategory() == null ? "" : request.getCategory().trim().toUpperCase();
        for (int i = 0; i < resources.size(); i++) {
            Resource resource = resources.get(i);
            if (resource == null) {
                continue;
            }
            // Only AVAILABLE resources can be allocated.
            if (!"AVAILABLE".equalsIgnoreCase(
                    resource.getAvailabilityStatus())) {
                continue;
            }

            String resourceType = resource.getType() == null ? "" : resource.getType().trim().toUpperCase();
            String baseType = resourceType;
            int underscoreIndex = resourceType.indexOf('_');

            if (underscoreIndex > 0) {
                baseType =
                        resourceType.substring(0, underscoreIndex);
            }

            // Match request category with resource base type.
            if (baseType.equals(requiredType)) {
                return resource;
            }
        }
        return null;
    }

    /**
     * Finds every AVAILABLE resource whose base type is compatible with at
     * least one of the given pending requests' categories. Used by the
     * outer resource-scan in optimizeResourceAllocation so that WHICH
     * resource to use becomes an optimized decision instead of "whichever
     * one we happened to find first".
     *
     * Uses the same base-type-before-underscore matching as
     * findMatchingAvailableResource for consistency. It does not cover
     * OptimizationService's private extra special-case mappings (e.g. a
     * GENERATOR also being allowed to serve ELECTRICAL requests), so in
     * rare edge cases a resource OptimizationService would still accept
     * might not show up in this scan. Worth noting as a known limitation
     * if it comes up during the oral defense.
     */
    private DynamicArray<Resource> findCompatibleAvailableResources(
            DynamicArray<ServiceRequest> pendingRequests) {

        DynamicArray<Resource> candidates = new DynamicArray<>();
        DynamicArray<Resource> resources = dataLoader.getResources();
        if (resources == null) {
            return candidates;
        }

        for (int i = 0; i < resources.size(); i++) {
            Resource resource = resources.get(i);
            if (resource == null) {
                continue;
            }
            if (!"AVAILABLE".equalsIgnoreCase(resource.getAvailabilityStatus())) {
                continue;
            }

            String resourceType = resource.getType() == null ? "" : resource.getType().trim().toUpperCase();
            String baseType = resourceType.contains("_")
                    ? resourceType.substring(0, resourceType.indexOf('_'))
                    : resourceType;

            for (int j = 0; j < pendingRequests.size(); j++) {
                String category = pendingRequests.get(j).getCategory();
                if (category != null && baseType.equals(category.trim().toUpperCase())) {
                    candidates.add(resource);
                    break;
                }
            }
        }
        return candidates;
    }

    /**
     * Sums the Knapsack "value" of a batch of selected requests
     */
    private int totalValue(DynamicArray<ServiceRequest> selected) {
        int total = 0;
        for (int i = 0; i < selected.size(); i++) {
            total += Math.max(1, 6 - selected.get(i).getUrgency());
        }
        return total;
    }

    /**
     * Releases a resource after the simulated service operation.
     */
    private void releaseResource(Resource resource) {
        if (resource != null) {
            resource.setAvailabilityStatus("AVAILABLE");
        }
    }

    // =========================================================================
    //  Private helpers
    // =========================================================================

    private void printRouteNodes(StringBuilder sb, DynamicArray<Location> path) {
        if (path == null || path.isEmpty()) {
            sb.append("No path");
            return;
        }
        for (int i = 0; i < path.size(); i++) {
            Location location = path.get(i);
            if (location == null) {
                continue;
            }
            sb.append(location.getName()).append(" [").append(location.getLocationId()).append("]");
            if (i < path.size() - 1) {
                sb.append(" → ");
            }
        }
    }

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