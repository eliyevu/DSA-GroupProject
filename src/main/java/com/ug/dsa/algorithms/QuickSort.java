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
     *
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

    /**
     * Simple demo / manual smoke test.
     * Builds a DynamicArray with unsorted values, sorts it, and
     * displays before/after -- including edge cases.
     */
    public static void main(String[] args) {
        // Normal case
        DynamicArray arr = new DynamicArray();
        int[] values = {29, 10, 14, 37, 13, 4, 25, 1, 100, 2};
        for (int v : values) {
            arr.add(v);
        }

        System.out.print("Before sort: ");
        arr.display();

        QuickSort.sort(arr);

        System.out.print("After sort:  ");
        arr.display();

        // Edge case: empty array
        DynamicArray empty = new DynamicArray();
        QuickSort.sort(empty);
        System.out.print("Empty after sort (should print nothing): ");
        empty.display();

        // Edge case: single element
        DynamicArray single = new DynamicArray();
        single.add(42);
        QuickSort.sort(single);
        System.out.print("Single element after sort: ");
        single.display();

        // Edge case: duplicate keys
        DynamicArray duplicates = new DynamicArray();
        int[] dupValues = {5, 3, 5, 1, 3, 5, 2};
        for (int v : dupValues) {
            duplicates.add(v);
        }
        System.out.print("Duplicates before sort: ");
        duplicates.display();
        QuickSort.sort(duplicates);
        System.out.print("Duplicates after sort:  ");
        duplicates.display();

        // Edge case: already sorted input
        DynamicArray sorted = new DynamicArray();
        int[] sortedValues = {1, 2, 3, 4, 5};
        for (int v : sortedValues) {
            sorted.add(v);
        }
        QuickSort.sort(sorted);
        System.out.print("Already-sorted input after sort: ");
        sorted.display();
    }
}