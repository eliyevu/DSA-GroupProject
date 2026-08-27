package com.ug.dsa.services;

import com.ug.dsa.algorithms.Knapsack;
import com.ug.dsa.datastructures.DynamicArray;
import com.ug.dsa.datastructures.Heap;
import com.ug.dsa.models.Resource;
import com.ug.dsa.models.ServiceRequest;

/**
 * Handles resource allocation using Greedy and 0/1 Knapsack optimization.
 *
 * Resource allocation considers TWO important constraints:
 *
 * 1. Resource TYPE:
 *    The resource must be suitable for the service request category.
 *
 * 2. Resource CAPACITY:
 *    The operational cost of the selected requests must fit within
 *    the available capacity of the resource.
 */
public class OptimizationService {

    private RoutingService routingService;

    public OptimizationService() {
    }

    public OptimizationService(RoutingService routingService) {
        this.routingService = routingService;
    }

    public void setRoutingService(RoutingService routingService) {
        this.routingService = routingService;
    }

    // =========================================================================
    // GREEDY RESOURCE ALLOCATION
    // =========================================================================

    /**
     * Greedy allocation.
     *
     * Processes the most urgent compatible pending requests first.
     * A request is selected only when:
     *
     * 1. The request is PENDING.
     * 2. The resource type is compatible with the request category.
     * 3. A route exists.
     * 4. The operational cost fits the remaining capacity.
     */
    public DynamicArray<ServiceRequest> allocateResources(
            DynamicArray<ServiceRequest> requests,
            Resource resource) {

        DynamicArray<ServiceRequest> selected = new DynamicArray<>();

        if (requests == null ||
                resource == null ||
                requests.isEmpty()) {

            return selected;
        }

        requireRoutingService();

        /*
         * Do not allocate a resource that is not available.
         */
        if (!"AVAILABLE".equalsIgnoreCase(
                resource.getAvailabilityStatus())) {

            return selected;
        }

        /*
         * Priority queue based on urgency.
         * Urgency 1 = highest priority.
         */
        Heap<ServiceRequest> urgencyHeap = new Heap<>();

        for (int i = 0; i < requests.size(); i++) {

            ServiceRequest request = requests.get(i);

            if (request == null) {
                continue;
            }

            if (!"PENDING".equalsIgnoreCase(
                    request.getStatus())) {
                continue;
            }

            if (!isResourceCompatible(resource, request)) {
                continue;
            }

            urgencyHeap.insert(request, request.getUrgency());
        }

        int remainingCapacity = resource.getCapacity();

        while (!urgencyHeap.isEmpty()) {

            ServiceRequest request = urgencyHeap.extractMin();

            int cost = calculateOperationalCost(resource, request);

            if (cost == Integer.MAX_VALUE) {
                continue;
            }

            if (cost <= remainingCapacity) {
                selected.add(request);
                remainingCapacity -= cost;
            }
        }

        return selected;
    }

    // =========================================================================
    // 0/1 KNAPSACK RESOURCE OPTIMIZATION
    // =========================================================================

    /**
     * Uses 0/1 Knapsack to select the best combination of
     * compatible pending requests.
     *
     * Resource TYPE is checked first.
     * Resource CAPACITY is then used as the optimization constraint.
     */
    public DynamicArray<ServiceRequest> selectRequests(
            DynamicArray<ServiceRequest> requests,
            Resource resource) {

        DynamicArray<ServiceRequest> selected =
                new DynamicArray<>();

        if (requests == null ||
                resource == null ||
                requests.isEmpty() ||
                resource.getCapacity() <= 0) {

            return selected;
        }

        requireRoutingService();

        if (!"AVAILABLE".equalsIgnoreCase(
                resource.getAvailabilityStatus())) {

            return selected;
        }

        DynamicArray<ServiceRequest> eligibleRequests =
                new DynamicArray<>();

        DynamicArray<Integer> weights =
                new DynamicArray<>();

        DynamicArray<Integer> values =
                new DynamicArray<>();

        for (int i = 0; i < requests.size(); i++) {

            ServiceRequest request =
                    requests.get(i);

            if (request == null) {
                continue;
            }

            if (!"PENDING".equalsIgnoreCase(
                    request.getStatus())) {
                continue;
            }


            if (!isResourceCompatible(resource, request)) {
                continue;
            }

            int cost = calculateOperationalCost(resource, request);

            if (cost == Integer.MAX_VALUE) {
                continue;
            }

            eligibleRequests.add(request);
            weights.add(cost);

            int value = Math.max(
                    1,
                    6 - request.getUrgency()
            );

            values.add(value);
        }

        if (eligibleRequests.isEmpty()) {
            return selected;
        }

        /*
         * Optimize only compatible requests.
         */
        Knapsack.Result result =
                Knapsack.solveDetailed(
                        weights,
                        values,
                        resource.getCapacity()
                );

        /*
         * Convert selected indexes back into requests.
         */
        for (int i = 0;
             i < result.getSelectedIndices().size();
             i++) {

            int index =
                    result.getSelectedIndices().get(i);

            if (index >= 0 &&
                    index < eligibleRequests.size()) {

                selected.add(
                        eligibleRequests.get(index)
                );
            }
        }

        return selected;
    }

    // =========================================================================
    // COMPARE ALGORITHMS
    // =========================================================================

    /**
     * Compares Greedy and Knapsack using the SAME compatible
     * requests and resource.
     */
    public AllocationComparison compareAlgorithms(
            DynamicArray<ServiceRequest> requests,
            Resource resource) {

        DynamicArray<ServiceRequest> greedy =
                allocateResources(
                        requests,
                        resource
                );

        DynamicArray<ServiceRequest> knapsack =
                selectRequests(
                        requests,
                        resource
                );

        return new AllocationComparison(
                greedy,
                knapsack
        );
    }

    // =========================================================================
    // RESOURCE TYPE MATCHING
    // =========================================================================

    /**
     * Determines whether a resource can handle a service request.
     */
    private boolean isResourceCompatible(
            Resource resource,
            ServiceRequest request) {

        if (resource == null || request == null) {
            return false;
        }

        String resourceType =
                resource.getType();

        String requestCategory =
                request.getCategory();

        if (resourceType == null ||
                requestCategory == null) {

            return false;
        }

        resourceType =
                resourceType.trim().toUpperCase();

        requestCategory =
                requestCategory.trim().toUpperCase();

        String resourceCategory =
                resourceType.contains("_")
                        ? resourceType.substring(
                        0,
                        resourceType.indexOf("_")
                )
                        : resourceType;

        /*
         * Direct match.
         */
        if (resourceCategory.equals(
                requestCategory)) {

            return true;
        }

        /*
         * Special resource mappings.
         */

        // GENERATOR handles electrical requests.
        if ("GENERATOR".equals(resourceCategory)
                && "ELECTRICAL".equals(requestCategory)) {

            return true;
        }

        // SHUTTLE handles transport requests.
        if ("SHUTTLE".equals(resourceCategory)
                && "TRANSPORT".equals(requestCategory)) {

            return true;
        }

        /*
         * Utility trucks can support general maintenance.
         */
        if ("UTILITY".equals(resourceCategory)
                && "MAINTENANCE".equals(requestCategory)) {

            return true;
        }

        /*
         * No compatible resource.
         */
        return false;
    }

    // =========================================================================
    // OPERATIONAL COST
    // =========================================================================

    /**
     * Calculates the operational cost of sending the resource
     * from its home location to the request source and then
     * from the source to the destination.
     */
    private int calculateOperationalCost(
            Resource resource,
            ServiceRequest request) {

        int homeToSource =
                routingService.findShortestDistance(
                        resource.getHomeLocation(),
                        request.getSource()
                );

        int sourceToDestination =
                routingService.findShortestDistance(
                        request.getSource(),
                        request.getDestination()
                );

        /*
         * No valid route.
         */
        if (homeToSource == Integer.MAX_VALUE ||
                sourceToDestination == Integer.MAX_VALUE) {

            return Integer.MAX_VALUE;
        }

        long scaledDistance =
                (long) homeToSource +
                        sourceToDestination;

        /*
         * Convert graph's scaled distance
         * back into whole distance units.
         */
        long distance =
                (scaledDistance + 99L) / 100L;

        if (distance >= Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }

        return (int) distance;
    }

    // =========================================================================
    // VALIDATION
    // =========================================================================

    private void requireRoutingService() {

        if (routingService == null) {

            throw new IllegalStateException(
                    "RoutingService must be configured before optimization."
            );
        }
    }

    // =========================================================================
    // RESULT OBJECT
    // =========================================================================

    /**
     * Simple result object used when comparing
     * Greedy and Knapsack.
     */
    public static class AllocationComparison {

        private final DynamicArray<ServiceRequest> greedyResult;
        private final DynamicArray<ServiceRequest> knapsackResult;

        public AllocationComparison(
                DynamicArray<ServiceRequest> greedyResult,
                DynamicArray<ServiceRequest> knapsackResult) {

            this.greedyResult = greedyResult;
            this.knapsackResult = knapsackResult;
        }

        public DynamicArray<ServiceRequest> getGreedyResult() {
            return greedyResult;
        }

        public DynamicArray<ServiceRequest> getKnapsackResult() {
            return knapsackResult;
        }
    }
}