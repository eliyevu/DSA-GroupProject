package com.ug.dsa.services;

import com.ug.dsa.algorithms.Knapsack;
import com.ug.dsa.datastructures.DynamicArray;
import com.ug.dsa.datastructures.Heap;
import com.ug.dsa.models.Resource;
import com.ug.dsa.models.ServiceRequest;

/**
 * Resource allocation service.
 *
 * Greedy uses the custom Heap to process the most urgent requests first.
 * The cost is based on actual graph shortest-path distance rather than
 * subtracting arbitrary location IDs.
 *
 * Knapsack uses the same operational cost/value model for comparison.
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

    /**
     * Greedy resource allocation:
     * 1. Put requests into the custom Min-Heap using urgency as priority.
     * 2. Extract the most urgent request.
     * 3. Accept it if its operational route cost fits the remaining capacity.
     */
    public DynamicArray<ServiceRequest> allocateResources(
            DynamicArray<ServiceRequest> requests,
            Resource resource) {

        DynamicArray<ServiceRequest> selected = new DynamicArray<>();
        if (requests == null || resource == null) return selected;
        requireRoutingService();

        Heap<ServiceRequest> urgencyHeap = new Heap<>();
        for (int i = 0; i < requests.size(); i++) {
            ServiceRequest request = requests.get(i);
            if (request == null) continue;
            urgencyHeap.insert(request, request.getUrgency());
        }

        int remainingCapacity = resource.getCapacity();

        while (!urgencyHeap.isEmpty()) {
            ServiceRequest request = urgencyHeap.extractMin();
            int cost = calculateOperationalCost(resource, request);

            if (cost == Integer.MAX_VALUE) continue;
            if (cost <= remainingCapacity) {
                selected.add(request);
                remainingCapacity -= cost;
            }
        }

        return selected;
    }

    /**
     * 0/1 Knapsack comparison using the same route cost and urgency value.
     * Urgency 1 is treated as the highest value, so it receives the largest value.
     */
    public DynamicArray<ServiceRequest> selectRequests(
            DynamicArray<ServiceRequest> requests,
            int capacity) {

        DynamicArray<ServiceRequest> selected = new DynamicArray<>();
        if (requests == null || requests.isEmpty() || capacity <= 0) return selected;
        requireRoutingService();

        DynamicArray<Integer> weights = new DynamicArray<>();
        DynamicArray<Integer> values = new DynamicArray<>();

        for (int i = 0; i < requests.size(); i++) {
            ServiceRequest request = requests.get(i);
            int cost = calculateOperationalCost(request);
            if (cost == Integer.MAX_VALUE) cost = capacity + 1;

            // Urgency 1 is more valuable than urgency 5.
            int value = Math.max(1, 6 - request.getUrgency());
            weights.add(cost);
            values.add(value);
        }

        Knapsack.Result result = Knapsack.solveDetailed(weights, values, capacity);
        for (int i = 0; i < result.getSelectedIndices().size(); i++) {
            int index = result.getSelectedIndices().get(i);
            selected.add(requests.get(index));
        }

        return selected;
    }

    public DynamicArray<ServiceRequest> optimizeUnderConstraint(
            DynamicArray<ServiceRequest> requests,
            Resource resource) {

        if (requests == null || resource == null) return new DynamicArray<>();

        if (requests.size() <= 10) {
            return allocateResources(requests, resource);
        }
        return selectRequests(requests, resource.getCapacity());
    }

    private int calculateOperationalCost(Resource resource, ServiceRequest request) {
        int toSource = routingService.findShortestDistance(
                resource.getHomeLocation(), request.getSource());
        int sourceToDestination = routingService.findShortestDistance(
                request.getSource(), request.getDestination());

        if (toSource == Integer.MAX_VALUE || sourceToDestination == Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }

        // Graph weights are distance * 100. Convert to whole distance units.
        long scaled = (long) toSource + sourceToDestination;
        long distance = (scaled + 99L) / 100L;
        return distance >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) distance;
    }

    private int calculateOperationalCost(ServiceRequest request) {
        int sourceToDestination = routingService.findShortestDistance(
                request.getSource(), request.getDestination());

        if (sourceToDestination == Integer.MAX_VALUE) return Integer.MAX_VALUE;
        return (sourceToDestination + 99) / 100;
    }

    private void requireRoutingService() {
        if (routingService == null) {
            throw new IllegalStateException("RoutingService must be configured before optimization.");
        }
    }
}
