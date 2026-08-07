package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.DynamicArray;

/**
 * 0/1 Knapsack algorithm implementation using Dynamic Programming.
 * Leverages the team's custom DynamicArray data structure.
 */
public class Knapsack {

    /**
     * Represents the detailed result of the Knapsack optimization,
     * including the maximum value obtained and the indices of the selected items.
     */
    public static class Result {
        private final int maxValue;
        private final DynamicArray selectedIndices;

        public Result(int maxValue, DynamicArray selectedIndices) {
            this.maxValue = maxValue;
            this.selectedIndices = selectedIndices;
        }

        public int getMaxValue() {
            return maxValue;
        }

        public DynamicArray getSelectedIndices() {
            return selectedIndices;
        }
    }

    /**
     * Solves the 0/1 Knapsack problem and returns the maximum value.
     *
     * @param weights  DynamicArray of item weights
     * @param values   DynamicArray of item values
     * @param capacity maximum capacity of the knapsack
     * @return maximum value achievable within the capacity
     */
    public static int solve(DynamicArray weights, DynamicArray values, int capacity) {
        return solveDetailed(weights, values, capacity).getMaxValue();
    }

    /**
     * Solves the 0/1 Knapsack problem and returns a detailed Result containing
     * the maximum value and the specific items selected.
     *
     * @param weights  DynamicArray of item weights
     * @param values   DynamicArray of item values
     * @param capacity maximum capacity of the knapsack
     * @return a Result object detailing the maximum value and selected items
     */
    public static Result solveDetailed(DynamicArray weights, DynamicArray values, int capacity) {
        if (weights == null || values == null || weights.size() != values.size() || capacity <= 0) {
            return new Result(0, new DynamicArray());
        }

        int n = weights.size();
        int[][] dp = new int[n + 1][capacity + 1];

        // Fill the DP table
        for (int i = 1; i <= n; i++) {
            int weight = weights.get(i - 1);
            int value = values.get(i - 1);

            for (int w = 0; w <= capacity; w++) {
                if (weight <= w) {
                    dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - weight] + value);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        int maxValue = dp[n][capacity];

        // Backtrack to find the selected items
        DynamicArray selectedIndices = new DynamicArray();
        int w = capacity;
        for (int i = n; i > 0 && w > 0; i--) {
            // If the value differs from the row above, the i-th item was included
            if (dp[i][w] != dp[i - 1][w]) {
                selectedIndices.add(i - 1); // 0-based index of item
                w -= weights.get(i - 1);
            }
        }

        return new Result(maxValue, selectedIndices);
    }
}
