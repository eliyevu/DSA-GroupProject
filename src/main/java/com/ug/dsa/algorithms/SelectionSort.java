package com.ug.dsa.algorithms;
import com.ug.dsa.datastructures.DynamicArray;

public class SelectionSort {
    public static  <T extends Comparable<T>>  void selectionSort(DynamicArray<T> array){
        int n = array.size();

        for(int i = 0; i<n-1; i++){
            int minIndex = i;

            for(int j = i + 1; j < n; j++){
                if(array.get(j).compareTo(array.get(minIndex)) < 0){
                    minIndex = j;
                }
            }

            T temp = array.get(minIndex);
            array.set(minIndex, array.get(i));
            array.set(i, temp);
        }
    }

    public static <T> void printArray(DynamicArray<T> array) {
        for (int i = 0; i < array.size(); i++) {
            System.out.print(array.get(i) + " ");
        }
        System.out.println();
    }

}
