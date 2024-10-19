import java.util.Scanner;

/**
 * A program to do stuff with IO.
 * @author Evan Parker
 * @version v0.0.2
 */

public class BasicIOProgram {
    /**
     * The main method.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Enter two whole numbers\nseparated by one or more spaces:");

        Scanner input = new Scanner(System.in);

        System.out.println("You entered " + input.nextInt() + " and " + input.nextInt());
        System.out.println("Next enter two numbers.\nA decimal point is OK.");
        System.out.println("You entered " + input.nextFloat() + " and " + input.nextFloat());
        System.out.println("Next enter two words:");
        System.out.println("You entered \"" + input.next() + "\" and \"" + input.next() + "\"");
        System.out.println("Next enter a line of text:");
        input.nextLine(); // don't ask
        System.out.println("You entered: \"" + input.nextLine() + "\"");

        input.close();
    }
}
