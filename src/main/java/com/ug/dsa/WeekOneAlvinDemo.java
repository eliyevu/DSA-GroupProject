package com.ug.dsa;

import com.ug.dsa.algorithms.Knapsack;
import com.ug.dsa.datastructures.DisjointSet;
import com.ug.dsa.datastructures.DynamicArray;

public class WeekOneAlvinDemo {

    public static void main(String[] args) {
        runDisjointSetDemo();
        System.out.println();
        runKnapsackDemo();
    }

    private static void runDisjointSetDemo() {
        System.out.println("================ DISJOINT SET DEMO ================");
        int size = 6;
        System.out.println("Creating Disjoint Set with size: " + size);
        DisjointSet ds = new DisjointSet(size);

        System.out.println("------------ INITIALIZATION ------------");
        for (int i = 0; i < size; i++) {
            System.out.printf("Element %d representative: %d%n", i, ds.find(i));
        }

        System.out.println();
        System.out.println("------------ UNIONS ------------");
        System.out.println("union(0, 1) -> " + ds.union(0, 1));
        System.out.println("union(2, 3) -> " + ds.union(2, 3));
        System.out.println("union(1, 2) -> " + ds.union(1, 2));

        System.out.println("\nChecking connections after union operations:");
        System.out.println("Are 0 and 3 in the same set? (Expected: true)  -> " + (ds.find(0) == ds.find(3)));
        System.out.println("Are 0 and 4 in the same set? (Expected: false) -> " + (ds.find(0) == ds.find(4)));
        System.out.println("Are 2 and 3 in the same set? (Expected: true)  -> " + (ds.find(2) == ds.find(3)));

        System.out.println("\nUnioning remaining elements:");
        System.out.println("union(4, 5) -> " + ds.union(4, 5));
        System.out.println("union(3, 5) -> " + ds.union(3, 5));
        System.out.println("Are 0 and 5 in the same set? (Expected: true)  -> " + (ds.find(0) == ds.find(5)));

        System.out.println("\nFinal reps for all elements:");
        for (int i = 0; i < size; i++) {
            System.out.printf("Element %d representative: %d%n", i, ds.find(i));
        }
    }

    private static void runKnapsackDemo() {
        System.out.println("============ 0/1 KNAPSACK DEMO ============");
        DynamicArray weights = new DynamicArray();
        DynamicArray values = new DynamicArray();

        // Add items:
        // Item 0: weight = 2, value = 3
        // Item 1: weight = 3, value = 4
        // Item 2: weight = 4, value = 5
        // Item 3: weight = 5, value = 6
        weights.add(2); values.add(3);
        weights.add(3); values.add(4);
        weights.add(4); values.add(5);
        weights.add(5); values.add(6);

        int capacity = 5;

        System.out.println("Items available:");
        for (int i = 0; i < weights.size(); i++) {
            System.out.printf("  Item %d: Weight = %d, Value = %d%n", i, weights.get(i), values.get(i));
        }
        System.out.println("Knapsack Capacity: " + capacity);

        // Solve
        Knapsack.Result result = Knapsack.solveDetailed(weights, values, capacity);

        System.out.println("\nOptimization Results:");
        System.out.println("  Maximum Value Achievable: " + result.getMaxValue());
        
        System.out.print("  Selected Item Indices (0-based): ");
        DynamicArray selected = result.getSelectedIndices();
        for (int i = 0; i < selected.size(); i++) {
            System.out.print(selected.get(i) + " ");
        }
        System.out.println();
    }
}

