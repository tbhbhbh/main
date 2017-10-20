package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.commons.core.index.Index;

/**
 * Helper functions for handling arrays containing one or more Index.
 */
public class IndexArrayUtil {

    /**
     * Compare two arrays {@code arr1} & {@code arr2} if they have the same Index number(s).
     * Index in both arrays do not have to be in ascending or descending order.
     * @param arr1 cannot be null, cannot be empty, must be of same size as arr2
     * @param arr2 cannot be null, cannot be empty, must be of same size as arr1
     * @return true if two arrays have the same Index number(s)
     */
    public static boolean compareIndexArrays(Index[] arr1, Index[] arr2) {
        requireNonNull(arr1);
        requireNonNull(arr2);

        checkArgument(arr1.length > 0 && arr2.length > 0,
                "Both arrays must have at least one Index");
        checkArgument(arr1.length == arr2.length, "Both arrays must have same number of Index");

        Index[] sortedArr1 = sortArray(arr1);
        Index[] sortedArr2 = sortArray(arr2);

        for (int i = 0; i < sortedArr1.length; i++) {
            if (!sortedArr1[i].equals(sortedArr2[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Swap elements in an array by its position.
     * @param arr is a given array, it cannot be null
     * @param pos is a valid position to be swap with the next element.
     */
    private static void swapElements(Index[] arr, int pos) {

        Index temp = arr[pos];
        arr[pos] = arr[pos + 1];
        arr[pos + 1] = temp;
    }

    /**
     * Sort Index elements in a Index array by its index value in one-based.
     * @param arr is a valid Index array.
     * @return a sorted Index array.
     */
    private static Index[] sortArray(Index[] arr) {

        for (int k = 0; k < arr.length; k++) {
            for (int i = 0; i < arr.length - 1; i++) {
                if (arr[i].getOneBased() > arr[i + 1].getOneBased()) {
                    swapElements(arr, i);
                }
            }
        }
        return arr;
    }
}
