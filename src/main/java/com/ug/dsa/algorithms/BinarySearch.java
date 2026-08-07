package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.LinkedList;

public class BinarySearch {

    private BinarySearch() {
    }

    public static <T extends Comparable<T>> int search(LinkedList<T> list, T target) {
        int low = 0;
        int high = list.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int comparison = list.get(mid).compareTo(target);
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

    public static <T extends Comparable<T>> int searchRecursive(LinkedList<T> list, T target) {
        return searchRecursive(list, target, 0, list.size() - 1);
    }

    private static <T extends Comparable<T>> int searchRecursive(LinkedList<T> list, T target, int low, int high) {
        if (low > high) {
            return -1;
        }
        int mid = low + (high - low) / 2;
        int comparison = list.get(mid).compareTo(target);
        if (comparison == 0) {
            return mid;
        } else if (comparison < 0) {
            return searchRecursive(list, target, mid + 1, high);
        } else {
            return searchRecursive(list, target, low, mid - 1);
        }
    }
}
