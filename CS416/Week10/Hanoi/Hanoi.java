/**
 * @author Evan
 * @version v0.0.1
 */
public class Hanoi {
    public static int count = 0;

    /**
     * Main method.
     * @param args args
     */
    public static void main(String[] args) {

        int amount = 10;

        towersOfHanoi(amount, 0, 1, 2);
        System.out.printf("Total moves: %d, expected: %d\n", count, (int) Math.pow(2, amount) - 1);
    }

    /**
     * Solve the Towers of Hanoi problem.
     * @param disks number of disks
     * @param from starting tower
     * @param to ending tower
     * @param spare spare tower
     */
    public static void towersOfHanoi(int disks, int from, int to, int spare) {
        count++;
        if (disks == 1) {
            System.out.println("Move disk 1 from " + from + " to " + to);
        } else {
            towersOfHanoi(disks - 1, from, spare, to);
            System.out.println("Move disk " + disks + " from " + from + " to " + to);
            towersOfHanoi(disks - 1, spare, to, from);
        }
    }
}