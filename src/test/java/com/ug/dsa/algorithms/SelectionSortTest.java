package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.DynamicArray;

public class SelectionSortTest {

    public static void main(String[] args) {
        testSelectionSortIntegers();
        testSelectionSortStrings();
        testSelectionSortEmpty();
        testSelectionSortDuplicates();
    }

    private static void testSelectionSortIntegers() {
        DynamicArray<Integer> array = new DynamicArray<>();
        array.add(64);
        array.add(25);
        array.add(12);
        array.add(22);
        array.add(11);


        System.out.println("Original array:");
        SelectionSort.printArray(array);


        System.out.println("\n");
        SelectionSort.selectionSort(array);
        System.out.println("Sorted array:");
        SelectionSort.printArray(array);



        System.out.println("\n");
        Integer[] expected = {11, 12, 22, 25, 64};
        checkResult("Integers", array, expected);
    }

    private static void testSelectionSortStrings() {
        DynamicArray<String> array = new DynamicArray<>();
        array.add("banana");
        array.add("apple");
        array.add("cherry");

        System.out.println("\n");
        System.out.println("Original array:");
        SelectionSort.printArray(array);


        System.out.println("\n");
        SelectionSort.selectionSort(array);
        System.out.println("Sorted array:");
        SelectionSort.printArray(array);



        System.out.println("\n");

        String[] expected = {"apple", "banana", "cherry"};
        checkResult("Strings", array, expected);
    }

    private static void testSelectionSortEmpty() {
        DynamicArray<Integer> array = new DynamicArray<>();
        SelectionSort.selectionSort(array);

        Integer[] expected = {};
        checkResult("Empty array", array, expected);
    }

    private static void testSelectionSortDuplicates() {
        DynamicArray<Integer> array = new DynamicArray<>();
        array.add(5);
        array.add(3);
        array.add(5);
        array.add(3);

        SelectionSort.selectionSort(array);

        Integer[] expected = {3, 3, 5, 5};
        checkResult("Duplicates", array, expected);
    }

    private static <T> void checkResult(String testName, DynamicArray<T> array, T[] expected) {
        boolean passed = true;
        if (array.size() != expected.length) {
            passed = false;
        } else {
            for (int i = 0; i < expected.length; i++) {
                if (!array.get(i).equals(expected[i])) {
                    passed = false;
                    break;
                }
            }
        }

        if (passed) {
            System.out.println(testName + " test PASSED ");
        } else {
            System.out.println(testName + " test FAILED ");
        }
    }
}
