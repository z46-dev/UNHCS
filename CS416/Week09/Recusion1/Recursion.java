/**
 * @author Evan Parker
 * @version v0.0.1
 */
public class Recursion {

    /**
     * Checks if the input string is a palindrome.
     * @param input the string to check
     * @return true if the input string is a palindrome, false otherwise
     */
    public static boolean isPalindrome(String input) {
        if (input.length() < 2) {
            return true;
        }

        if (input.charAt(0) != input.charAt(input.length() - 1)) {
            return false;
        }

        return isPalindrome(input.substring(1, input.length() - 1));
    }

    /**
     * Counts the number of times the letter appears in the input string.
     * @param input the string to check
     * @param letter the letter to count
     * @return the number of times the letter appears in the input string
     */
    public static int countLetter(String input, char letter) {
        if (input.length() == 0) {
            return 0;
        }

        if (input.charAt(0) == letter) {
            return 1 + countLetter(input.substring(1), letter);
        }

        return countLetter(input.substring(1), letter);
    }

    /**
     * Returns the max value in the list.
     * @param list the list of integers
     * @param n the number of elements in the list
     * @return the max value in the list
     */
    public static int maxValue(int[] list, int n) {
        if (n == 1) {
            return list[0];
        }

        return Math.max(list[n - 1], maxValue(list, n - 1));
    }
}
