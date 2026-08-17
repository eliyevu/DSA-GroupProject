package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.DynamicArray;

public class QuickSortTest {
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
