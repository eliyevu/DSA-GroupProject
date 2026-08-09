package com.ug.dsa;

import com.ug.dsa.algorithms.InsertionSort;
import com.ug.dsa.datastructures.Queue;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Testing Custom Queue ===");
        Queue<String> taskQueue = new Queue<>();

        System.out.println("Is queue empty? " + taskQueue.isEmpty());

        // Enqueue Operations
        System.out.println("\nEnqueuing elements...");
        taskQueue.enqueue("Process A");
        taskQueue.enqueue("Process B");
        taskQueue.enqueue("Process C");

        System.out.println("Front element: " + taskQueue.front());
        System.out.println("Queue size: " + taskQueue.size());

        // Dequeue Operations
        System.out.println("\nDequeuing elements:");
        while (!taskQueue.isEmpty()) {
            System.out.println("Dequeued: " + taskQueue.dequeue());
        }

        System.out.println("Is queue empty now? " + taskQueue.isEmpty());


        System.out.println("\n=== Testing Insertion Sort ===");
        Integer[] numbers = {29, 10, 14, 37, 13, 2, 88};
        
        System.out.println("Before sorting: " + Arrays.toString(numbers));
        InsertionSort.sort(numbers);
        System.out.println("After sorting:  " + Arrays.toString(numbers));

        // String array test
        String[] names = {"Razak", "Abdul", "Rafiu", "Member"};
        System.out.println("\nBefore sorting strings: " + Arrays.toString(names));
        InsertionSort.sort(names);
        System.out.println("After sorting strings:  " + Arrays.toString(names));
    }
}
