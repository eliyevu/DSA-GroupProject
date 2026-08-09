package com.ug.dsa;

import com.ug.dsa.algorithms.BinarySearch;
import com.ug.dsa.datastructures.DynamicArray;
import com.ug.dsa.datastructures.LinkedList;

public class WeekOneDemo {

    public static void main(String[] args) {

        System.out.println("================ LINKED LIST DEMO ================");

        LinkedList<Integer> list = new LinkedList<>();
        for (int value = 10; value <= 100; value += 10) {
            list.addLast(value);
        }

        System.out.println("List after addLast(10..100):  " + list);
        System.out.println("Size:                        " + list.size());
        System.out.println("isEmpty:                     " + list.isEmpty());

        System.out.println();
        System.out.println("------------ TRAVERSAL ------------");
        System.out.print("Traversal via get(i):        ");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + " ");
        }
        System.out.println();

        System.out.println();
        System.out.println("------------ INSERTION ------------");

        list.addFirst(5);
        System.out.println("After addFirst(5):           " + list);
        list.addAt(4, 35);
        System.out.println("After addAt(4, 35):          " + list);
        list.addLast(110);
        System.out.println("After addLast(110):          " + list);

        System.out.println();
        System.out.println("------------ DELETION ------------");

        System.out.println("removeFirst() -> " + list.removeFirst());
        System.out.println("List now:                   " + list);
        System.out.println("removeLast()  -> " + list.removeLast());
        System.out.println("List now:                   " + list);
        System.out.println("removeAt(4)   -> " + list.removeAt(4));
        System.out.println("List now:                   " + list);
        System.out.println("remove(60)    -> " + list.remove(60));
        System.out.println("List now:                   " + list);

        System.out.println();
        System.out.println("------------ SEARCH HELPERS ------------");

        System.out.println("contains(80)  -> " + list.contains(80));
        System.out.println("contains(99)  -> " + list.contains(99));
        System.out.println("indexOf(80)   -> " + list.indexOf(80));
        System.out.println("get(0)        -> " + list.get(0));
        System.out.println("get(size-1)   -> " + list.get(list.size() - 1));

        list.set(0, 7);
        System.out.println("After set(0, 7):            " + list);
        System.out.println("Final size:                 " + list.size());

        System.out.println();
        System.out.println("================ DYNAMIC ARRAY DEMO ================");

        DynamicArray<Integer> array = new DynamicArray<>();
        for (int value = 10; value <= 100; value += 10) {
            array.add(value);
        }

        System.out.println("Array after add(10..100):   " + array);
        System.out.println("Size:                      " + array.size());

        System.out.println();
        System.out.println("============ BINARY SEARCH DEMO (DynamicArray) ============");

        int[] targets = {70, 10, 100, 55, 5, 105};
        for (int target : targets) {
            int found = BinarySearch.search(array, target);
            System.out.printf("search(%d)          -> index %d%n", target, found);
        }

        System.out.println();
        int recursiveResult = BinarySearch.searchRecursive(array, 90);
        System.out.println("searchRecursive(90) -> index " + recursiveResult);
        int recursiveMiss = BinarySearch.searchRecursive(array, 45);
        System.out.println("searchRecursive(45) -> index " + recursiveMiss);

        System.out.println();
        System.out.println("------------ DYNAMIC ARRAY OPERATIONS ------------");

        array.add(110);
        System.out.println("After add(110):             " + array);
        array.set(0, 5);
        System.out.println("After set(0, 5):           " + array);
        System.out.println("remove(0) -> " + array.remove(0));
        System.out.println("Array now:                 " + array);
        System.out.println("contains(90) -> " + array.contains(90));
        System.out.println("indexOf(90)  -> " + array.indexOf(90));
        System.out.println("Final size:                " + array.size());
    }
}
