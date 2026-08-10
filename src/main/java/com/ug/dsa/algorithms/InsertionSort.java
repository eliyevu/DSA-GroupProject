package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.DynamicArray;

public class InsertionSort {

    public static <T extends Comparable<T>> void sort(DynamicArray<T> array) {
        if (array == null || array.size() <= 1) {
            return;
        }

        for (int i = 1; i < array.size(); i++) {
            T key = array.get(i);
            int j = i - 1;

            // Shift elements that are greater than key
            // to one position ahead of their current position
            while (j >= 0 && array.get(j).compareTo(key) > 0) {
                array.set(j + 1, array.get(j));
                j--;
            }
            array.set(j + 1, key);
        }
    }
}
