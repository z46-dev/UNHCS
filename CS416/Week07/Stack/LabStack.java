import java.util.ArrayList;
import java.util.Arrays;

/**
 * The main LabStack class.
 * @author Evan Parker
 * @version v0.0.1
 */
public class LabStack {
    /**
     * The main method.
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        int[] vals = new int[]{10, 20, 30, 40, 50};

        System.out.println(Arrays.toString(reverse(vals)));
    }

    /**
     * Pop N amount of values from the stack.
     * @param stack the stack to pop values from
     * @param n the amount of values to pop
     * @return the popped values
     */
    public static ArrayList<Integer> popN(Stack stack, int n) {
        ArrayList<Integer> popped = new ArrayList<Integer>();

        for (int i = 0; i < n; i++) {
            popped.add(stack.pop());
        }
        
        return popped;
    }

    /**
     * Pop all values from the stack.
     * @param stack the stack to pop values from
     * @return the popped values
     */
    public static ArrayList<Integer> popAll(Stack stack) {
        ArrayList<Integer> popped = new ArrayList<Integer>();

        while (!stack.isEmpty()) {
            popped.add(stack.pop());
        }

        return popped;
    }

    /**
     * Reverse an array using a stack.
     * @param arr the array to reverse
     * @return the reversed array
     */
    public static int[] reverse(int[] arr) {
        Stack stack = new Stack();

        for (int i = 0; i < arr.length; i++) {
            stack.push(arr[i]);
        }

        for (int i = 0; i < arr.length; i++) {
            arr[i] = stack.pop();
        }

        return arr;
    }
}
