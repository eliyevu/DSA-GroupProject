package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.DynamicArray;

public class LinearSearch {

    public static int search(DynamicArray array, int target) {

        for (int i = 0; i < array.size(); i++) {

            if (array.get(i) == target) {
                return i;
            }
        }

        return -1;
    }
}