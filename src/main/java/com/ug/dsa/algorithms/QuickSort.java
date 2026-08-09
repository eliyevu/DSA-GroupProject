package com.ug.dsa.algorithms;
import com.ug.dsa.datastructures.DynamicArray;

public class QuickSort {

    public static void sort(DynamicArray arr) {
        if (arr == null || arr.size() <= 1) {
            return; // already sorted: empty or single-element
        }
        quickSort(arr, 0, arr.size() - 1);
    }

    /**
     * Recursively sorts the sub-range [low, high] (inclusive) of arr.
     */
    private static void quickSort(DynamicArray arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);

            // Everything left of pivotIndex is <= pivot,
            // everything right of pivotIndex is > pivot.
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    /**
     * Lomuto partition scheme.
     * <p>
     * Chooses arr[high] as the pivot, then rearranges [low, high] so
     * that all elements <= pivot come before it and all elements > pivot
     * come after it. Returns the pivot's final resting index.
     */
    private static int partition(DynamicArray arr, int low, int high) {
        int pivotValue = arr.get(high);

        // i marks the boundary: everything in [low, i] is <= pivotValue so far.
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr.get(j) <= pivotValue) {
                i++;
                swap(arr, i, j);
            }
        }

        // Place the pivot right after the last "small" element.
        swap(arr, i + 1, high);
        return i + 1;
    }

    /**
     * Swaps the elements at indices i and j using DynamicArray's
     * public get/set methods (no direct array access is possible
     * or allowed, since the backing array is private).
     */
    private static void swap(DynamicArray arr, int i, int j) {
        if (i == j) {
            return;
        }
        int temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }

}