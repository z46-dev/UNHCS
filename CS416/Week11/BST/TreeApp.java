import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import java.util.Iterator;

/**
 * TreeApp Generator.
 * 
 * @author cs416
 * @version 1
 */
public class TreeApp {

    ArrayList<Data> list;
    BinarySearchTree bst = null;
    int dataSize = 8;
    int minDataSize = 0;
    int maxDataSize = 20;
    int defaultDataSize = 8;
    int maxVal = 9;
    Integer expectedMaxVal = null;
    String expectedMaxKey = null;
    int expectedHeight = -1;

    int rngSeed = 2; // random seed

    private Random rng = null;

    /**
     * Update list size and generate a new list of that size.
     * 
     * @param sz int size
     */
    public void makeData(int sz) {
        this.dataSize = sz;
        list = generateData(this.dataSize, this.rngSeed);

        bst = new BinarySearchTree();

        for (Data d : list) {
            if (expectedMaxVal == null || expectedMaxVal < d.value) {
                expectedMaxVal = d.value;
            }
            if (expectedMaxKey == null || expectedMaxKey.compareTo(d.key) < 0) {
                expectedMaxKey = d.key;
            }
            bst.add(d);
        }
    }

    /**
     * Generate data for the tree.
     * 
     * @param numItems int number items to generate
     * @param seed     int random number seed; -1: system picks
     * @return ArrayList the data
     */
    private ArrayList<Data> generateData(int numItems, int seed) {
        bst = new BinarySearchTree();
        ArrayList<Data> dl = new ArrayList<Data>();
        ArrayList<String> keys = new ArrayList<String>();

        if (rng == null) {
            rng = new Random(seed);
        }

        String letters = "abcdefghijklmnopqrstuvwxyz";
        StringBuffer keybuf = new StringBuffer("12");

        while (dl.size() < numItems) {
            // generate a key
            char letter1 = letters.charAt(rng.nextInt(letters.length()));
            char letter2 = letters.charAt(rng.nextInt(letters.length()));

            keybuf.setCharAt(0, letter1);
            keybuf.setCharAt(1, letter2);
            String key = keybuf.toString();

            int found = keys.indexOf(key);
            if (found < 0) { // keys must be unique so only add if not there
                keys.add(key); // add key to key list
                // generate a value from 0 to maxVal
                int val = rng.nextInt(maxVal);
                dl.add(new Data(key, val));
            }
        }
        return dl;
    }

    /**
     * Show results to "user". Print to standard output.
     * 
     * @param msg String string to print
     */
    public void showResults(String msg) {
        System.out.println(msg);
    }

    /**
     * Print the current tree.
     * 
     * @param title String title
     */
    public void printTree(String title) {
        String dashes = "------------------------ ";
        System.out.println("\n" + dashes + title + " " + dashes);
        if (bst == null || bst.size() == 0) {
            System.out.println("---Empty---");
        } else {
            System.out.println(bst);
            System.out.println("Tree has " + bst.size() + " nodes.");
        }
    }

    /**
     * Validate String Data.
     * 
     * @return String
     */
    public String validateStringData() {

        String actual = bst.inOrder();
        String ret = "";

        for (Data d : list) {
            if (!actual.contains(d.toString())) {
                ret += d.toString() + ", ";

            }
        }

        if (ret.equals("")) {
            return ret;
        } else {
            return ret.substring(0, ret.length() - 2);
        }
    }

    /**
     * Validate String Order.
     * 
     * @return String
     */
    public String validateStringOrder() {
        String actual = bst.inOrder();
        String ret = "";

        ArrayList<Data> sortedList = new ArrayList<Data>(list);
        Collections.sort(sortedList);

        for (Data d : sortedList) {
            if (!actual.startsWith(d.toString())) {
                ret += d.toString() + ", ";
            }
            if (actual.contains(d.toString() + ", ")) {
                actual = actual.replace(d.toString() + ", ", "");
            } else {
                actual = actual.replace(d.toString(), "");
            }
        }

        if (ret.equals("")) {
            return ret;
        } else {
            return ret.substring(0, ret.length() - 2);
        }
    }

    /**
     * Validate the Iterator.
     * 
     * @return String
     */
    public String validateIterator() {
        Iterator<Data> iter = bst.iterator();
        String ret = "";

        ArrayList<Data> sortedList = new ArrayList<Data>(list);
        Collections.sort(sortedList);

        int i = 0;
        while (iter.hasNext()) {
            Object d = iter.next();
            if (!d.equals(sortedList.get(i))) {
                return "First incorrect item was "
                        + d + " at index " + i + ". Expected "
                        + sortedList.get(i);
            }
            i++;
        }

        if (i != sortedList.size()) {
            ret = "Iterator stopped early at index " + i;
        }

        return ret;
    }

    /**
     * Main Function.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        TreeApp t = new TreeApp();

        for (int i = 1; i < 32; i *= 2) {
            t.makeData(i);
            t.printTree("current tree");

            System.out.println();

            ArrayList<Data> sortedList = new ArrayList<Data>(t.list);
            Collections.sort(sortedList);

            t.showResults("-------Expected order: ");
            System.out.println(sortedList);

            try {
                String inOrder = t.bst.inOrder();
                String missing = t.validateStringData();
                String outOfOrder = t.validateStringOrder();

                if (!missing.equals("")
                        || !outOfOrder.equals("")) {
                    t.showResults("-------Invalid result from inOrder: ");
                    t.showResults("-------Result: " + inOrder);
                    t.showResults("-------Out of order: "
                            + outOfOrder);
                    t.showResults("-------Missing: "
                            + missing);
                } else {
                    t.showResults("-------Correct result from inOrder.");
                }
            } catch (Exception ex) {
                t.showResults("-------Error calling inOrder!");
            }

            System.out.println();

            try {
                String iterResult = t.validateIterator();

                if (!iterResult.equals("")) {
                    t.showResults("-------Invalid result from iterator: ");
                    t.showResults("-------" + iterResult);
                } else {
                    t.showResults("-------Correct result from iterator.");
                }
            } catch (Exception ex) {
                t.showResults("-------Error using iterator!");
            }
        }
    }
}