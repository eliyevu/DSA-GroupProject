package com.ug.dsa.services;

import com.ug.dsa.datastructures.DynamicArray;
import com.ug.dsa.models.ServiceRequest;
import com.ug.dsa.models.Resource;
import com.ug.dsa.algorithms.QuickSort;
import com.ug.dsa.algorithms.Knapsack;

public class OptimizationService {

    public OptimizationService() { }


     // Greedy allocation of resources to requests.

    public DynamicArray<ServiceRequest> allocateResources(DynamicArray<ServiceRequest> requests, Resource resource) {
        DynamicArray<ServiceRequest> selected = new DynamicArray<>();

        // Sort requests by urgency using QuickSort
        QuickSort.sort(requests);

        int remainingCapacity = resource.getCapacity();

        for (int i = 0; i < requests.size(); i++) {
            ServiceRequest req = requests.get(i);

            // Treat distance as cost, urgency as value
            int cost = Math.abs(req.getDestination() - req.getSource());
            int value = req.getUrgency();

            if (cost <= remainingCapacity) {
                selected.add(req);
                remainingCapacity -= cost;
            }
        }

        return selected;
    }


     // Dynamic Programming selection of requests under capacity constraint.
     // Using Knapsack algorithm implementation.

    public DynamicArray<ServiceRequest> selectRequests(DynamicArray<ServiceRequest> requests, int capacity) {
        DynamicArray<Integer> weights = new DynamicArray<>();
        DynamicArray<Integer> values = new DynamicArray<>();

        // Map ServiceRequest fields into weights and values
        for (int i = 0; i < requests.size(); i++) {
            ServiceRequest req = requests.get(i);
            int cost = Math.abs(req.getDestination() - req.getSource());
            int value = req.getUrgency();
            weights.add(cost);
            values.add(value);
        }

        Knapsack.Result result = Knapsack.solveDetailed(weights, values, capacity);

        DynamicArray<ServiceRequest> selected = new DynamicArray<>();
        for (int i = 0; i < result.getSelectedIndices().size(); i++) {
            int index = result.getSelectedIndices().get(i);
            selected.add(requests.get(index));
        }

        return selected;
    }


     // Chooses between Greedy and DP based on problem size and constraints.

    public DynamicArray<ServiceRequest> optimizeUnderConstraint(DynamicArray<ServiceRequest> requests, Resource resource) {
        int n = requests.size();
        int capacity = resource.getCapacity();

        if (n <= 10 && capacity > 100) {
            System.out.println("Using Greedy optimization...");
            return allocateResources(requests, resource);
        } else {
            System.out.println("Using Dynamic Programming optimization...");
            return selectRequests(requests, capacity);
        }
    }


     // Demonstrates a case where Greedy fails compared to DP.

    public void demonstrateGreedyFailure() {
        DynamicArray<ServiceRequest> requests = new DynamicArray<>();
        requests.add(new ServiceRequest(1, 0, 50, "Delivery", 60, "08:00", "10:00", "Pending"));
        requests.add(new ServiceRequest(2, 0, 50, "Delivery", 100, "08:05", "10:30", "Pending"));
        requests.add(new ServiceRequest(3, 0, 25, "Delivery", 100, "08:10", "09:30", "Pending"));

        Resource resource = new Resource(1, "Truck", 0, 75, "Available");

        DynamicArray<ServiceRequest> greedyResult = allocateResources(requests, resource);
        System.out.println("Greedy selected: " + greedyResult);

        DynamicArray<ServiceRequest> dpResult = selectRequests(requests, resource.getCapacity());
        System.out.println("DP selected: " + dpResult);

        System.out.println("Greedy picked only one request with urgency=100.");
        System.out.println("DP picked two requests with combined urgency=200.");
        System.out.println("=> Greedy fails because it ignores combinations that yield higher total value.");
    }
}
