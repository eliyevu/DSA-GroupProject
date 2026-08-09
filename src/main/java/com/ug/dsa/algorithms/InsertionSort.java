/**
 * Generic implementation of the Insertion Sort algorithm from scratch.
 */
public class InsertionSort {

    /**
     * Sorts an array of comparable elements in ascending order.
     *
     * @param <T>   the type of elements in the array
     * @param array the array to be sorted
     */
    public static <T extends Comparable<T>> void sort(T[] array) {
        if (array == null || array.length <= 1) {
            return;
        }

        for (int i = 1; i < array.length; i++) {
            T key = array[i];
            int j = i - 1;

            // Shift elements of array[0..i-1] that are greater than key
            // to one position ahead of their current position
            while (j >= 0 && array[j].compareTo(key) > 0) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }
}
