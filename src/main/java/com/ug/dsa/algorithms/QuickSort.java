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

    /**
     * Lomuto partition scheme.
     *
     * Chooses arr.get(high) as the pivot, then rearranges [low, high] so
     * that all elements <= pivot (by compareTo) come before it and all
     * elements > pivot come after it. Returns the pivot's final resting
     * index.
     */
    private static <T extends Comparable<T>> int partition(DynamicArray<T> arr, int low, int high) {
        T pivotValue = arr.get(high);

        // i marks the boundary: everything in [low, i] is <= pivotValue so far.
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

    /**
     * Simple demo / manual smoke test.
     * Demonstrates that the SAME sort method now works on multiple
     * types, plus the required edge cases.
     */
    public static void main(String[] args) {
        // Normal case: Integers
        DynamicArray<Integer> intArr = new DynamicArray<>();
        int[] values = {29, 10, 14, 37, 13, 4, 25, 1, 100, 2};
        for (int v : values) {
            intArr.add(v);
        }
        System.out.println("Integers before sort: " + intArr);
        QuickSort.sort(intArr);
        System.out.println("Integers after sort:  " + intArr);

        // Normal case: Strings (proves genericity -- not just "still ints")
        DynamicArray<String> strArr = new DynamicArray<>();
        String[] words = {"Accra", "Kumasi", "Tamale", "Takoradi", "Ho", "Cape Coast"};
        for (String w : words) {
            strArr.add(w);
        }
        System.out.println("Strings before sort: " + strArr);
        QuickSort.sort(strArr);
        System.out.println("Strings after sort:  " + strArr);

        // Edge case: empty array
        DynamicArray<Integer> empty = new DynamicArray<>();
        QuickSort.sort(empty);
        System.out.println("Empty after sort: " + empty);

        // Edge case: single element
        DynamicArray<Integer> single = new DynamicArray<>();
        single.add(42);
        QuickSort.sort(single);
        System.out.println("Single element after sort: " + single);

        // Edge case: duplicate keys
        DynamicArray<Integer> duplicates = new DynamicArray<>();
        int[] dupValues = {5, 3, 5, 1, 3, 5, 2};
        for (int v : dupValues) {
            duplicates.add(v);
        }
        System.out.println("Duplicates before sort: " + duplicates);
        QuickSort.sort(duplicates);
        System.out.println("Duplicates after sort:  " + duplicates);

        // Edge case: already-sorted input (QuickSort's worst case
        // with a last-element pivot)
        DynamicArray<Integer> sorted = new DynamicArray<>();
        int[] sortedValues = {1, 2, 3, 4, 5};
        for (int v : sortedValues) {
            sorted.add(v);
        }
        QuickSort.sort(sorted);
        System.out.println("Already-sorted input after sort: " + sorted);
    }
}