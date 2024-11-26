import java.util.ArrayList;

/**
 * @author Evan Parker
 * @version v0.0.1
 */
public class MergeSort extends Sort {
    /**
     * Creates a new sort.
     * @param fileName The file to sort
     */
    MergeSort(String fileName) {
        super(fileName);
    }

    /**
     * Sorts the samples in descending order.
     */
    public void sort() {
        mergeSort(samples, 0, samples.size() - 1);
    }

    /**
     * Merge the two halves of the array.
     * @param samples The array to merge
     * @param l The left index
     * @param r The right index
     */
    private void mergeSort(ArrayList<Sample> samples, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;

            mergeSort(samples, l, m);
            mergeSort(samples, m + 1, r);

            merge(samples, l, m, r);
        }
    }

    /**
     * Merge the two halves of the array.
     * @param samples The array to merge
     * @param l The left index
     * @param m The middle index
     * @param r The right index
     */
    private void merge(ArrayList<Sample> samples, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;

        ArrayList<Sample> left = new ArrayList<>();
        ArrayList<Sample> right = new ArrayList<>();

        for (int i = 0; i < n1; i++) {
            left.add(samples.get(l + i));
        }

        for (int j = 0; j < n2; j++) {
            right.add(samples.get(m + 1 + j));
        }

        int i = 0;
        int j = 0;
        int k = l;

        while (i < n1 && j < n2) {
            if (left.get(i).measurement >= right.get(j).measurement) {
                samples.set(k, left.get(i));
                i++;
            } else {
                samples.set(k, right.get(j));
                j++;
            }
            
            k++;
        }

        while (i < n1) {
            samples.set(k, left.get(i));
            i++;
            k++;
        }

        while (j < n2) {
            samples.set(k, right.get(j));
            j++;
            k++;
        }
    }
}
