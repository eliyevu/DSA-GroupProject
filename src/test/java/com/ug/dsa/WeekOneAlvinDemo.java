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
        DisjointSet<Integer> ds = new DisjointSet<>(size);

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
        System.out.println("Are 0 and 3 in the same set? (Expected: true)  -> " + (ds.find(0).equals(ds.find(3))));
        System.out.println("Are 0 and 4 in the same set? (Expected: false) -> " + (ds.find(0).equals(ds.find(4))));
        System.out.println("Are 2 and 3 in the same set? (Expected: true)  -> " + (ds.find(2).equals(ds.find(3))));

        System.out.println("\nUnioning remaining elements:");
        System.out.println("union(4, 5) -> " + ds.union(4, 5));
        System.out.println("union(3, 5) -> " + ds.union(3, 5));
        System.out.println("Are 0 and 5 in the same set? (Expected: true)  -> " + (ds.find(0).equals(ds.find(5))));

        System.out.println("\nFinal reps for all elements:");
        for (int i = 0; i < size; i++) {
            System.out.printf("Element %d representative: %d%n", i, ds.find(i));
        }
    }

    private static void runKnapsackDemo() {
        System.out.println("============ 0/1 KNAPSACK DEMO ============");
        DynamicArray<Integer> weights = new DynamicArray<>();
        DynamicArray<Integer> values = new DynamicArray<>();

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
        DynamicArray<Integer> selected = result.getSelectedIndices();
        for (int i = 0; i < selected.size(); i++) {
            System.out.print(selected.get(i) + " ");
        }
        System.out.println();

        System.out.println("\n------------ INPUT VALIDATION TESTS ------------");

        // Test Negative Capacity
        try {
            System.out.print("Testing Knapsack with negative capacity (-5)... ");
            Knapsack.solve(weights, values, -5);
            System.out.println("FAILED (No exception thrown)");
        } catch (IllegalArgumentException e) {
            System.out.println("PASSED (Caught expected exception: " + e.getMessage() + ")");
        }

        // Test Negative Weights
        DynamicArray<Integer> invalidWeights = new DynamicArray<>();
        invalidWeights.add(2);
        invalidWeights.add(-3); // invalid
        invalidWeights.add(4);
        invalidWeights.add(5);

        try {
            System.out.print("Testing Knapsack with negative weight (-3)... ");
            Knapsack.solve(invalidWeights, values, capacity);
            System.out.println("FAILED (No exception thrown)");
        } catch (IllegalArgumentException e) {
            System.out.println("PASSED (Caught expected exception: " + e.getMessage() + ")");
        }
    }
}

