package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.DynamicArray;

public class MergeSort {

    public static <T extends Comparable<T>> void sort(DynamicArray<T> array) {
        if (array == null || array.size() < 2) {
            return;
        }

        int mid = array.size() / 2;

        DynamicArray<T> left = new DynamicArray<>();
        DynamicArray<T> right = new DynamicArray<>();

        // Copy elements into left half
        for (int i = 0; i < mid; i++) {
            left.add(array.get(i));
        }

        // Copy elements into right half
        for (int i = mid; i < array.size(); i++) {
            right.add(array.get(i));
        }

        // Recursively sort both halves
        sort(left);
        sort(right);

        // Merge the sorted halves back into the original array
        merge(array, left, right);
    }

    private static <T extends Comparable<T>> void merge(
            DynamicArray<T> array,
            DynamicArray<T> left,
            DynamicArray<T> right) {

        int i = 0;
        int j = 0;
        int k = 0;

        // Merge while both arrays have elements
        while (i < left.size() && j < right.size()) {

            if (left.get(i).compareTo(right.get(j)) <= 0) {
                array.set(k, left.get(i));
                i++;
            } else {
                array.set(k, right.get(j));
                j++;
            }

            k++;
        }

        // Copy remaining elements from left
        while (i < left.size()) {
            array.set(k, left.get(i));
            i++;
            k++;
        }

        // Copy remaining elements from right
        while (j < right.size()) {
            array.set(k, right.get(j));
            j++;
            k++;
        }
    }
}