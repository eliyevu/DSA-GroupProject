package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.DynamicArray;

public class BinarySearch {

    private BinarySearch() {
    }

    public static <T extends Comparable<T>> int search(DynamicArray<T> array, T target) {
        int low = 0;
        int high = array.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int comparison = array.get(mid).compareTo(target);
            if (comparison == 0) {
                return mid;
            } else if (comparison < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    public static <T extends Comparable<T>> int searchRecursive(DynamicArray<T> array, T target) {
        return searchRecursive(array, target, 0, array.size() - 1);
    }

    private static <T extends Comparable<T>> int searchRecursive(DynamicArray<T> array, T target, int low, int high) {
        if (low > high) {
            return -1;
        }
        int mid = low + (high - low) / 2;
        int comparison = array.get(mid).compareTo(target);
        if (comparison == 0) {
            return mid;
        } else if (comparison < 0) {
            return searchRecursive(array, target, mid + 1, high);
        } else {
            return searchRecursive(array, target, low, mid - 1);
        }
    }
}
