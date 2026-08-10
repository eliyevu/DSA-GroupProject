package com.ug.dsa.datastructures;

import com.ug.dsa.algorithms.MergeSort;

public class CircularQueueMergeSortDemo {
    public static void main(String[] args) {
        System.out.println("Circular Queue Test");
        CircularQueue cq = new CircularQueue(4);
        cq.enqueue(10);
        cq.enqueue(20);
        cq.enqueue(30);
        cq.display();

        System.out.println("Removed: " + cq.dequeue());
        cq.enqueue(40);
        cq.enqueue(50);
        cq.display();

        System.out.println();
        System.out.println("Merge Sort Test");
        int[] numbers = {38, 27, 43, 3, 9, 82, 10};

        System.out.print("Before: ");
        for (int n : numbers) {
            System.out.print(n + " ");
        }
        System.out.println();

        MergeSort.sort(numbers);

        System.out.print("After: ");
        for (int n : numbers) {
            System.out.print(n + " ");
        }
        System.out.println();
    }
}
