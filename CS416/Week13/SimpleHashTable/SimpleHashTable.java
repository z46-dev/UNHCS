/**
 * @author Evan Parker
 * @version v0.0.1
 */
public class SimpleHashTable {
    private static final int R = 2;
    private String[] vals;

    /**
     * Constructor.
     */
    public SimpleHashTable() {
        this.vals = new String[10];
    }

    /**
     * Put value.
     * @param key key
     * @param value value
     */
    public void put(int key, String value) {
        this.vals[SimpleHashTable.hash(key, vals.length, R)] = value;
    }

    /**
     * Gets value.
     * @param key key
     * @return value
     */
    public String get(int key) {
        return this.vals[SimpleHashTable.hash(key, vals.length, R)];
    }

    /**
     * To string method.
     * @return string
     */
    public String toString() {
        String str = "";
        
        for (int i = 0; i < this.vals.length; i++) {
            str += i + " " + this.vals[i] + "\n";
        }

        return str;
    }

    /**
     * Hash thingy.
     * @param key k
     * @param size s
     * @param r r
     * @return o
     */
    public static int hash(int key, int size, int r) {
        String midStr = Integer.toString(key * key);
        int midLen = midStr.length();
        midLen -= (midLen % 2 == 0) ? 1 : 0;
        return Integer.parseInt(midStr.substring(midLen / 2, midLen / 2 + r)) % size;
    }
}