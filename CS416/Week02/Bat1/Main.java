/**
 * Bat 1 Lab.
 * @author Evan Parker
 * @version v0.0.2
 */

public class Main {
    /**
     * Sleep in.
     * @param weekday Weekday is true if it is a weekday
     * @param vacation Vacation is true if we are on vacation
     * @return true if we can sleep in, false if we cannot
     */
    public static boolean sleepIn(boolean weekday, boolean vacation) {
        return !weekday || vacation;
    }

    /**
     * Given an int x, return true if it's within 10 of 100 or 200.
     * @param x The number to check
     * @return true if x is within 10 of 100 or 200
     */
    public static boolean nearHundred(int x) {
        return Math.abs(100 - x) <= 10 || Math.abs(200 - x) <= 10;
    }

    /**
     * If array is length 1+, and first elem == last elem.
     * @param nums The array of numbers to check
     * @return true if length is 1+ and first == last
     */
    public static boolean sameFirstLast(int[] nums) {
        return nums.length > 0 && nums[0] == nums[nums.length - 1];
    }

    /**
     * Given an array length 2, return true if it does not contain a 2 or 3.
     * @param nums the array to check
     * @return true if neither nums[0] or nums[1] are 2 or 3
     */
    public static boolean no23(int[] nums) {
        return nums.length == 2 && !(nums[0] == 2 || nums[0] == 3) && !(nums[1] == 2 || nums[1] == 3);
    }

    /**
     * Say "Hello :name:!".
     * @param name The name to use
     * @return string
     */
    public static String helloName(String name) {
        return "Hello " + name + "!";
    }
}