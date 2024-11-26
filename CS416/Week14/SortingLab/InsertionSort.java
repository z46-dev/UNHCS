/**
 * @author Evan Parker
 * @version v0.0.1
 */
public class InsertionSort extends Sort {
    /**
     * Creates a new sort.
     * @param fileName The file to sort
     */
    InsertionSort(String fileName) {
        super(fileName);
    }

    /**
     * Sorts the samples in descending order.
     */
    public void sort() {
        for (int i = 1; i < samples.size(); i++) {
            Sample key = samples.get(i);
            int j = i - 1;

            while (j >= 0 && samples.get(j).measurement < key.measurement) {
                samples.set(j + 1, samples.get(j));
                j = j - 1;
            }
            
            samples.set(j + 1, key);
        }
    }
}
