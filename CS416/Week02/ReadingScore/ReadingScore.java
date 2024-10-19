import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * ~.
 * @author Evan Parker
 * @version v0.0.1
 */
public class ReadingScore {
    static String[] stuff = {
        "Extremely difficult to read.",
        "Very difficult to read.",
        "Difficult to read.",
        "Fairly difficult to read.",
        "Plain English.",
        "Fairly easy to read.",
        "Easy to read.",
        "Very easy to read."
    };
    
    static int[][] threshes = {
        {0, 10},
        {11, 30},
        {31, 50},
        {51, 60},
        {61, 70},
        {71, 80},
        {81, 90},
        {91, 100}
    };

    /**
     * ~.
     * @param args ~
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int score = getReadingScore(input.nextLine());
        input.close();

        System.out.println(score);

        int i = 0;
        for (int[] thresh : threshes) {
            if (score >= thresh[0] && score <= thresh[1]) {
                System.out.println(stuff[i]);
                break;
            }

            i++;
        }
    }

    /**
     * ~.
     * @param token ~
     * @return ~
     */
    public static String formatToken(String token) {
        Pattern pattern = Pattern.compile("[^A-Za-z]");
        return pattern.matcher(token).replaceAll("");
    }

    /**
     * ~.
     * @param input ~
     * @return ~
     */
    public static String[] tokenize(String input) {
        return input.split("\\s+");
    }

    /**
     * ~.
     * @param tokens ~
     * @return ~
     */
    public static int getSentenceCount(String[] tokens) {
        int out = 0;

        /**
         * CHECKSTYLE IS RUDE.
         * 
         * if (
         *     a || b || c || d ||
         *     e || f || g
         * ) {}
         * 
         * IS SO MUCH CLEANER
         */
        for (String token : tokens) {
            if (
                token.endsWith(".") || token.endsWith(":") || token.endsWith(";")
                    || token.endsWith("?") || token.endsWith("!")
            ) {
                out++;
            }
        }

        return out;
    }

    /**
     * ~.
     * @param tokens ~
     * @return ~
     */
    public static int getTokenCount(String[] tokens) {
        return tokens.length;
    }

    /**
     * ~.
     * @param word ~
     * @return ~
     */
    public static int getSyllableCount(String word) {
        if (word.length() == 0) {
            return 1;
        }

        int out = 0;

        char[] vowels = {
            'A', 'E', 'I', 'O', 'U', 'Y',
            'a', 'e', 'i', 'o', 'u', 'y'
        };

        for (int i = 0, j = word.length(); i < j; i++) {
            char ch = word.charAt(i);
            
            in: for (char vowel : vowels) {
                if (ch == vowel) {
                    out++;
                    break in;
                }
            }
        }

        if (word.toLowerCase().charAt(word.length() - 1) == 'e') {
            out--;
        }

        return Math.max(out, 1);
    }

    /**
     * ~.
     * @param tokens ~
     * @return ~
     */
    public static double getTotalSyllableCount(String[] tokens) {
        double out = 0;

        for (String token : tokens) {
            out += (double) getSyllableCount(formatToken(token));
        }

        return out * .88;
    }

    /**
     * ~.
     * @param text ~
     * @return ~
     */
    public static int getReadingScore(String text) {
        String[] tokens = tokenize(text);
        double wordCount = (double) getTokenCount(tokens);

        return Math.min(100, Math.max(0, (int) Math.round(
            206.835 - 1.015 * (wordCount / getSentenceCount(tokens))
            - 84.6 * (getTotalSyllableCount(tokens) / wordCount))));
    }
}
