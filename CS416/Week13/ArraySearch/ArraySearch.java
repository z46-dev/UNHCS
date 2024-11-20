/**
 * @author Evan Parker
 * @version v0.0.1
 * @param <T> Comparable Type
 */
public class ArraySearch<T extends Comparable<? super T>> implements Search<T> {
    T[] list;
    boolean sorted;
    
    /**
     * Constructor.
     * @param list T[]
     */
    public ArraySearch(T[] list) {
        this.list = list;
        this.sorted = this.isSorted();
    }

    /**
     * @return true if there are no element in the list
     */
    public boolean isEmpty() {
        return this.list.length == 0;
    }

    /**
     * @return the number of elements in the list
     */
    public int size() {
        return this.list.length;
    }

    /**
     * @return true if the list is sorted in ascending order
     * For example: 1, 2, 3, 7, 20
     */
    public boolean isSorted() {
        for (int i = 0; i < this.list.length - 1; i++) {
            if (this.list[i].compareTo(list[i + 1]) > 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param obj object to be searched for
     * @return the location of the element in the list starting at 0 or return -1 if not present
     */
    public int index(Object obj) {
        if (this.sorted) {
            return this.binarySearch(obj);
        }

        for (int i = 0; i < this.list.length; i++) {
            if (this.list[i].equals(obj)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * @param obj Object
     * @return int
     */
    @SuppressWarnings("unchecked")
    private int binarySearch(Object obj) {
        int low = 0;
        int high = this.list.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            int compare = this.list[mid].compareTo((T) obj);

            if (compare == 0) {
                return mid;
            } else if (compare < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -1;
    }
}
