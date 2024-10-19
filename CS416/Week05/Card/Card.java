/**
 * @author Evan Parker
 * @version v0.0.6
 */
public class Card implements Comparable<Card> {
    private String value;
    private String suit;

    /**
     * Creates a card.
     * 
     * @param suit  the suit
     * @param value the value
     */
    public Card(String suit, String value) {
        this.suit = suit;
        this.value = value;
    }

    /**
     * Check equality.
     * 
     * @param obj the input to check
     * @return boolean
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Card)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        Card other = (Card) obj;

        return this.value.equals(other.getValue()) && this.suit.equals(other.getSuit());
    }

    /**
     * Gets value.
     * 
     * @return value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Gets suit.
     * 
     * @return suit
     */
    public String getSuit() {
        return this.suit;
    }

    /**
     * Compares to other.
     * 
     * @param other other
     * @return int
     */
    public int compareTo(Card other) {
        return Math.min(1, Math.max(-1, parseValue(this.value) - parseValue(other.getValue())));
    }

    /**
     * To string.
     * 
     * @return String
     */
    public String toString() {
        return Card.getValueEquiv(this.value) + " of " + this.suit;
    }

    /**
     * Main func.
     * 
     * @param args arguments
     */
    public static void main(String[] args) {
        System.out.println("exec");
    }

    /**
     * Parses a string value to an int for comparison.
     * 
     * @param value value
     * @return int
     */
    public static int parseValue(String value) {
        switch (value) {
            case "Ace":
                return 14;
            case "Jack":
                return 11;
            case "Queen":
                return 12;
            case "King":
                return 13;
            default:
                return Integer.parseInt(value);
        }
    }

    /**
     * Gets value of a string for the gradescope.
     * @param value value
     * @return String
     */
    public static String getValueEquiv(String value) {
        switch (value) {
            case "2":
                return "Two";
            case "3":
                return "Three";
            case "4":
                return "Four";
            case "5":
                return "Five";
            case "6":
                return "Six";
            case "7":
                return "Seven";
            case "8":
                return "Eight";
            case "9":
                return "Nine";
            case "10":
                return "Ten";
            default:
                return value;
        }
    }
}
