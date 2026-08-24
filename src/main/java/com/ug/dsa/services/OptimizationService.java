package com.ug.dsa.services;

import com.ug.dsa.algorithms.Knapsack;
import com.ug.dsa.datastructures.DynamicArray;
import com.ug.dsa.datastructures.Heap;
import com.ug.dsa.models.Resource;
import com.ug.dsa.models.ServiceRequest;

/**
 * Handles resource allocation using Greedy and 0/1 Knapsack optimization.
 * Greedy:
 * - Processes the most urgent requests first.
 * - Accepts a request if its operational cost fits the remaining resource capacity.
 * Knapsack:
 * - Considers all possible combinations of requests.
 * - Maximizes total urgency value without exceeding the same capacity.
 * Both algorithms use the same operational cost:
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

    // Greedy resource allocation.
    public DynamicArray<ServiceRequest> allocateResources(
            DynamicArray<ServiceRequest> requests,
            Resource resource) {

        DynamicArray<ServiceRequest> selected = new DynamicArray<>();

        if (requests == null || resource == null || requests.isEmpty()) {
            return selected;
        }

        requireRoutingService();

        Heap<ServiceRequest> urgencyHeap = new Heap<>();

        // Add only pending requests.
        for (int i = 0; i < requests.size(); i++) {
            ServiceRequest request = requests.get(i);

            if (request == null) {
                continue;
            }

            if (!"PENDING".equalsIgnoreCase(request.getStatus())) {
                continue;
            }

            urgencyHeap.insert(request, request.getUrgency());
        }

        int remainingCapacity = resource.getCapacity();

        while (!urgencyHeap.isEmpty()) {

            ServiceRequest request = urgencyHeap.extractMin();

            int cost = calculateOperationalCost(resource, request);

            // No route exists.
            if (cost == Integer.MAX_VALUE) {
                continue;
            }

            // Allocate only if the resource has enough capacity.
            if (cost <= remainingCapacity) {
                selected.add(request);
                remainingCapacity -= cost;
            }
        }

        return selected;
    }

    //  0/1 Knapsack resource optimization.
    public DynamicArray<ServiceRequest> selectRequests(
            DynamicArray<ServiceRequest> requests,
            Resource resource) {

        DynamicArray<ServiceRequest> selected = new DynamicArray<>();

        if (requests == null ||
                resource == null ||
                requests.isEmpty() ||
                resource.getCapacity() <= 0) {
            return selected;
        }

        requireRoutingService();

        DynamicArray<ServiceRequest> eligibleRequests =
                new DynamicArray<>();

        DynamicArray<Integer> weights =
                new DynamicArray<>();

        DynamicArray<Integer> values =
                new DynamicArray<>();

        for (int i = 0; i < requests.size(); i++) {

            ServiceRequest request = requests.get(i);

            if (request == null) {
                continue;
            }

            if (!"PENDING".equalsIgnoreCase(request.getStatus())) {
                continue;
            }

            int cost = calculateOperationalCost(resource, request);

            if (cost == Integer.MAX_VALUE) {
                continue;
            }

            // Urgency 1 = highest value.
            // Urgency 5 = lowest value.
            int value = Math.max(
                    1,
                    6 - request.getUrgency()
            );

            eligibleRequests.add(request);
            weights.add(cost);
            values.add(value);
        }

        if (eligibleRequests.isEmpty()) {
            return selected;
        }

        Knapsack.Result result =
                Knapsack.solveDetailed(
                        weights,
                        values,
                        resource.getCapacity()
                );

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

    // Compare Greedy and Knapsack without changing request status.
    public AllocationComparison compareAlgorithms(
            DynamicArray<ServiceRequest> requests,
            Resource resource) {

        DynamicArray<ServiceRequest> greedy =
                allocateResources(requests, resource);

        DynamicArray<ServiceRequest> knapsack =
                selectRequests(requests, resource);

        return new AllocationComparison(
                greedy,
                knapsack
        );
    }

    // Calculates the actual operational cost
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

        if (homeToSource == Integer.MAX_VALUE ||
                sourceToDestination == Integer.MAX_VALUE) {

            return Integer.MAX_VALUE;
        }

        long scaledDistance =
                (long) homeToSource +
                        sourceToDestination;

        // Convert graph's scaled distance back to whole units.
        long distance =
                (scaledDistance + 99L) / 100L;

        if (distance >= Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }

        return (int) distance;
    }

    private void requireRoutingService() {

        if (routingService == null) {
            throw new IllegalStateException(
                    "RoutingService must be configured before optimization."
            );
        }
    }

    /**
     * Simple result object used when comparing algorithms.
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