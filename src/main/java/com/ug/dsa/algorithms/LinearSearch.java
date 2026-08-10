package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.DynamicArray;

public class LinearSearch {

    public static <T> int search(DynamicArray<T> array, T target) {

        for (int i = 0; i < array.size(); i++) {
            T current = array.get(i);

            if (current == null && target == null) {
                return i;
            }

            if (current != null && current.equals(target)) {
                return i;
            }
        }

        return -1;
    }
}

