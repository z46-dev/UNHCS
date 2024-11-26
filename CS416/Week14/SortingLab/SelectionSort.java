/**
 * @author Evan Parker
 * @version v0.0.1
 */
public class SelectionSort extends Sort {
    /**
     * Creates a new sort.
     * @param fileName The file to sort
     */
    SelectionSort(String fileName) {
        super(fileName);
    }

    /**
     * Sorts the samples in descending order.
     */
    public void sort() {
        for (int i = 0; i < samples.size() - 1; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < samples.size(); j++) {
                if (samples.get(j).measurement > samples.get(maxIndex).measurement) {
                    maxIndex = j;
                }
            }
            
            Sample temp = samples.get(maxIndex);
            samples.set(maxIndex, samples.get(i));
            samples.set(i, temp);
        }
    }
}
