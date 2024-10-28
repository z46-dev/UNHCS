import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Evan Parker, CS416
 * @version v0.0.1
 */
public class Multiples {

    private int[] result;
    private ArrayList<Integer> numbers;

    /**
     * Constructor.
     * 
     * @param numbers Values to distribute into multiples location
     */
    public Multiples(ArrayList<Integer> numbers) {
        this.numbers = numbers;
        this.result = new int[numbers.size()];
    }

    // HELPER METHODS GO HERE
    /**
     * Method to check if a number is a multiple of another.
     * 
     * @param num The number to check
     * @param div The divisor to check against
     * @return True if num is a multiple of div, false otherwise
     */
    public boolean isMultiple(int num, int div) {
        return num % div == 0;
    }

    /**
     * Method to assign a value to the result array and remove it from the numbers
     * list.
     * 
     * @param i The index of the number to assign
     * @param n The index in the result array to assign the value to
     */
    public void assignValue(int n, int i) {
        result[n] = numbers.get(i);
        numbers.remove(i);
    }

    /**
     * Method to unassign a value from the result array and add it back to the
     * numbers list.
     * 
     * @param n The index in the result array to unassign
     */
    public void unassignValue(int n) {
        numbers.add(0, result[n]);
        result[n] = 0;
    }

    /**
     * DO NOT CHANGE!
     * Method to begin solving the problem for the first index in results.
     *
     * @return True if solution was found, false if none was possible.
     */
    public boolean solve() {
        return solve(0);
    }

    /**
     * Recursive method to solve the problem from a given index onward. Assumes
     * that any index before n was already assigned a valid value.
     *
     * @param n The current index in the results array to find a value for.
     * @return True if solution was found, false if none was possible from this
     *         index.
     */
    private boolean solve(int n) {
        if (n >= result.length) {
            return true;
        }

        for (int i = 0; i < numbers.size(); i++) {
            if (isMultiple(numbers.get(i), n + 1)) {
                assignValue(n, i);
                if (solve(n + 1)) {
                    return true;
                }

                unassignValue(n);
            }
        }

        return false;
    }

    /**
     * returns the retult array.
     * 
     * @return int[]
     */
    public int[] getResult() {
        return result;
    }

    /**
     * return the numbers ArrayList.
     * 
     * @return ArrayList
     */
    public ArrayList<Integer> getNumbers() {
        return numbers;
    }

    /**
     * main function.
     * 
     * @param args Command line args
     */
    public static void main(String[] args) {
        Integer[] numbers;
        Multiples m;

        // FIRST TEST (SOLUTION POSSIBLE WITHOUT BACKTRACKING)
        numbers = new Integer[] {1, 6, 21, 40, 25};
        m = new Multiples(new ArrayList<Integer>(Arrays.asList(numbers)));

        if (m.solve()) {
            System.out.println("Found solution for " + Arrays.toString(numbers));
        } else {
            System.out.println("No solution possible!");
        }
        System.out.println(Arrays.toString(m.getResult()));
        System.out.println();

        // SECOND TEST (SOLUTION POSSIBLE WITH BACKTRACKING)
        numbers = new Integer[] {15, 10, 6, 12, 16};
        m = new Multiples(new ArrayList<Integer>(Arrays.asList(numbers)));

        if (m.solve()) {
            System.out.println("Found solution for " + Arrays.toString(numbers));
        } else {
            System.out.println("No solution possible!");
        }
        System.out.println(Arrays.toString(m.getResult()));
        System.out.println();

        // THIRD TEST (NO SOLUTION)
        numbers = new Integer[] {15, 10, 6, 12, 17};
        m = new Multiples(new ArrayList<Integer>(Arrays.asList(numbers)));

        if (m.solve()) {
            System.out.println("Found solution for " + Arrays.toString(numbers));
        } else {
            System.out.println("No solution possible!");
        }
        System.out.println(Arrays.toString(m.getResult()));
    }

}