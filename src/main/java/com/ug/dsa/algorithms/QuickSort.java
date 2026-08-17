package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.DynamicArray;

public class QuickSort {

    /**
     * Sorts the entire DynamicArray in ascending order (per T's natural
     * ordering via Comparable), in place.
     */
    public static <T extends Comparable<T>> void sort(DynamicArray<T> arr) {
        if (arr == null || arr.size() <= 1) {
            return; // already sorted: empty or single-element
        }
        quickSort(arr, 0, arr.size() - 1);
    }

    /**
     * Recursively sorts the sub-range [low, high] (inclusive) of arr.
     */
    private static <T extends Comparable<T>> void quickSort(DynamicArray<T> arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);

            // Everything left of pivotIndex is <= pivot,
            // everything right of pivotIndex is > pivot.
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    private static <T extends Comparable<T>> int partition(DynamicArray<T> arr, int low, int high) {
        T pivotValue = arr.get(high);

        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr.get(j).compareTo(pivotValue) <= 0) {
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
    private static <T> void swap(DynamicArray<T> arr, int i, int j) {
        if (i == j) {
            return;
        }
        T temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }

}