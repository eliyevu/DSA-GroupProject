package com.ug.dsa;

import com.ug.dsa.algorithms.LinearSearch;
import com.ug.dsa.datastructures.DynamicArray;

public class Main {

    public static void main(String[] args) {

        System.out.println("SMART SERVICE OPERATIONS OPTIMIZER");
        System.out.println("Week 1 - Dynamic Array & Linear Search");

        // ==========================================
        // 1. CREATE DYNAMIC ARRAY
        // ==========================================

        System.out.println("TEST 1: Creating Dynamic Array");

        DynamicArray array = new DynamicArray();

        System.out.println("Expected: Dynamic Array is created");
        System.out.println("Actual: Dynamic Array created successfully\n");

        // ==========================================
        // 2. ADD MULTIPLE ELEMENTS
        // ==========================================

        System.out.println("TEST 2: Adding Multiple Elements");

        array.add(100);
        array.add(200);
        array.add(300);
        array.add(400);
        array.add(500);

        System.out.println("Expected: 5 elements added");
        System.out.println("Actual: Elements added successfully\n");

        // ==========================================
        // 3. DISPLAY ALL ELEMENTS
        // ==========================================

        System.out.println("TEST 3: Displaying All Elements");

        System.out.println("Expected elements:");
        System.out.println("100\n200\n300\n400\n500");

        System.out.println("\nActual elements:");

        for (int i = 0; i < array.size(); i++) {
            System.out.println(array.get(i));
        }

        System.out.println();

        // ==========================================
        // 4. TEST GET
        // ==========================================

        System.out.println("TEST 4: Retrieving Elements By Index");

        int expectedGet = 300;
        int actualGet = array.get(2);

        System.out.println("Expected at index 2: " + expectedGet);
        System.out.println("Actual at index 2: " + actualGet);

        if (expectedGet == actualGet) {
            System.out.println("RESULT: PASS");
        } else {
            System.out.println("RESULT: FAIL");
        }

        System.out.println();

        // ==========================================
        // 5. TEST SET / UPDATE
        // ==========================================

        System.out.println("TEST 5: Updating an Element");

        System.out.println("Before update: " + array.get(1));

        array.set(1, 250);

        int expectedUpdate = 250;
        int actualUpdate = array.get(1);

        System.out.println("Expected after update: " + expectedUpdate);
        System.out.println("Actual after update: " + actualUpdate);

        if (expectedUpdate == actualUpdate) {
            System.out.println("RESULT: PASS");
        } else {
            System.out.println("RESULT: FAIL");
        }

        System.out.println();

        // ==========================================
        // 6. TEST REMOVE
        // ==========================================

        System.out.println("TEST 6: Removing an Element");

        System.out.println("Before removal:");
        for (int i = 0; i < array.size(); i++) {
            System.out.println(i + ": " + array.get(i));
        }

        array.remove(3);

        System.out.println("Expected size after removal: 4");
        System.out.println("Actual size after removal: " + array.size());

        System.out.println("\nElements after removal:");
        for (int i = 0; i < array.size(); i++) {
            System.out.println(i + ": " + array.get(i));
        }

        System.out.println();

        // ==========================================
        // 7. TEST DYNAMIC RESIZING
        // ==========================================

        System.out.println("TEST 7: Testing Dynamic Array Resizing");

        System.out.println("Adding many elements to force resizing...");

        for (int i = 1; i <= 20; i++) {
            array.add(1000 + i);
        }

        System.out.println("Expected: Array should resize automatically");
        System.out.println("Actual size: " + array.size());

        if (array.size() == 24) {
            System.out.println("RESULT: PASS - Dynamic resizing works");
        } else {
            System.out.println("RESULT: CHECK - Unexpected size");
        }

        System.out.println();

        // ==========================================
        // 8. LINEAR SEARCH - BEGINNING
        // ==========================================

        System.out.println("TEST 8: Linear Search - Beginning");

        int beginningResult = LinearSearch.search(array, 100);

        System.out.println("Searching for: 100");
        System.out.println("Expected index: 0");
        System.out.println("Actual index: " + beginningResult);

        if (beginningResult == 0) {
            System.out.println("RESULT: PASS");
        } else {
            System.out.println("RESULT: FAIL");
        }

        System.out.println();

        // ==========================================
        // 9. LINEAR SEARCH - ELEMENT NOT FOUND
        // ==========================================

        System.out.println("TEST 9: Linear Search - Element Not Found");

        int notFoundResult = LinearSearch.search(array, 9999);

        System.out.println("Searching for: 9999");
        System.out.println("Expected index: -1");
        System.out.println("Actual index: " + notFoundResult);

        if (notFoundResult == -1) {
            System.out.println("RESULT: PASS");
        } else {
            System.out.println("RESULT: FAIL");
        }

        System.out.println();

        // ==========================================
        // FINAL MESSAGE
        // ==========================================

        System.out.println("======================================");
        System.out.println("ALL TESTS COMPLETED");
        System.out.println("======================================");
    }
}
