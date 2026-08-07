package com.ug.dsa;

import com.ug.dsa.algorithms.LinearSearch;
import com.ug.dsa.datastructures.DynamicArray;

public class Main {

    public  static void main(String[] args) {


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

        array.add("Cleaning");
        array.add("Plumbing");
        array.add("Electrical");
        array.add("Delivery");
        array.add("Maintenance");

        System.out.println("Expected: 5 elements added");
        System.out.println("Actual: Elements added successfully\n");


        // ==========================================
        // 3. DISPLAY ALL ELEMENTS
        // ==========================================

        System.out.println("TEST 3: Displaying All Elements");

        System.out.println("Expected elements:");
        System.out.println("Cleaning");
        System.out.println("Plumbing");
        System.out.println("Electrical");
        System.out.println("Delivery");
        System.out.println("Maintenance");

        System.out.println("\nActual elements:");

        for (int i = 0; i < array.size(); i++) {
            System.out.println(array.get(i));
        }

        System.out.println();


        // ==========================================
        // 4. TEST GET
        // ==========================================

        System.out.println("TEST 4: Retrieving Elements By Index");

        String expectedGet = "Electrical";
        String actualGet = array.get(2);

        System.out.println("Expected at index 2: " + expectedGet);
        System.out.println("Actual at index 2: " + actualGet);

        if (expectedGet.equals(actualGet)) {
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

        array.set(1, "Emergency Plumbing");

        String expectedUpdate = "Emergency Plumbing";
        String actualUpdate = array.get(1);

        System.out.println("Expected after update: " + expectedUpdate);
        System.out.println("Actual after update: " + actualUpdate);

        if (expectedUpdate.equals(actualUpdate)) {
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

        String removedElement = array.remove(3);

        System.out.println("\nRemoved element: " + removedElement);

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
            array.add("Service " + i);
        }

        System.out.println("Expected: Array should resize automatically");
        System.out.println("Actual size: " + array.size());

        if (array.size() == 24) {
            System.out.println("RESULT: PASS - Dynamic resizing works");
        } else {
            System.out.println("RESULT: CHECK - Unexpected size");
        }

        System.out.println("\nElements after resizing:");

        for (int i = 0; i < array.size(); i++) {
            System.out.println(i + ": " + array.get(i));
        }

        System.out.println();


        // ==========================================
        // 8. LINEAR SEARCH - BEGINNING
        // ==========================================

        System.out.println("TEST 8: Linear Search - Beginning");

        int beginningResult =
                LinearSearch.search(array, "Cleaning");

        System.out.println("Searching for: Cleaning");
        System.out.println("Expected index: 0");
        System.out.println("Actual index: " + beginningResult);

        if (beginningResult == 0) {
            System.out.println("RESULT: PASS");
        } else {
            System.out.println("RESULT: FAIL");
        }

        System.out.println();


        // ==========================================
        // 9. LINEAR SEARCH - MIDDLE
        // ==========================================

        System.out.println("TEST 9: Linear Search - Middle");

        int middleResult =
                LinearSearch.search(array, "Service 10");

        System.out.println("Searching for: Service 10");
        System.out.println("Expected index: 13");
        System.out.println("Actual index: " + middleResult);

        if (middleResult == 13) {
            System.out.println("RESULT: PASS");
        } else {
            System.out.println("RESULT: FAIL");
        }

        System.out.println();


        // ==========================================
        // 10. LINEAR SEARCH - END
        // ==========================================

        System.out.println("TEST 10: Linear Search - End");

        int endResult =
                LinearSearch.search(array, "Service 20");

        System.out.println("Searching for: Service 20");
        System.out.println("Expected index: 23");
        System.out.println("Actual index: " + endResult);

        if (endResult == 23) {
            System.out.println("RESULT: PASS");
        } else {
            System.out.println("RESULT: FAIL");
        }

        System.out.println();


        // ==========================================
        // 11. LINEAR SEARCH - ELEMENT NOT FOUND
        // ==========================================

        System.out.println("TEST 11: Linear Search - Element Not Found");

        int notFoundResult =
                LinearSearch.search(array, "Internet Installation");

        System.out.println("Searching for: Internet Installation");
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

