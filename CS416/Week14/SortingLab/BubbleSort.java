/**
 * @author Evan Parker
 * @version v0.0.1
 */
public class BubbleSort extends Sort {
    /**
     * Creates a new sort.
     * @param fileName The file to sort
     */
    BubbleSort(String fileName) {
        super(fileName);
    }

    /**
     * Sorts the samples in descending order.
     */
    public void sort() {
        byte swapped = 0;

        for (int i = 0; i < samples.size(); i++) {
            swapped = 0;
            
            for (int j = 0; j < samples.size() - i - 1; j++) {
                if (samples.get(j).measurement < samples.get(j + 1).measurement) {
                    Sample temp = samples.get(j);
                    samples.set(j, samples.get(j + 1));
                    samples.set(j + 1, temp);
                    swapped = 1;
                }
            }

            if (swapped == 0) {
                break;
            }
        }
    }
}
