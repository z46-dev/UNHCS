import java.util.Random;

/**
* ListApp.
* @author cs416
* @version 1
*/
public class ListApp {

    /**
    * Generate some data.
    * @param numKeys number of keys
    * @param numItems number of items
    * @param seed Seed value for Random Number Generator
    * @return LinkedList The List generated from the specified parameters
    */
    static LinkedList generateData(int numKeys, int numItems, int seed) {
        LinkedList l = new LinkedList();
        Random rng;
        if (seed < 0) {
            rng = new Random();
        } else {
            rng = new Random(seed);
        }

        String letters = "abcdefghijkloaeipr";
        String[] keys = new String[ numKeys ];

        String uniqueKeys = "";
        for (int i = 0; i < numKeys; i++) {
            int letter1 = rng.nextInt(letters.length());
            int letter2 = rng.nextInt(letters.length());
            keys[ i ] =   letters.substring(letter1, letter1 + 1)
                    + letters.substring(letter2, letter2 + 1);
            uniqueKeys += keys[ i ]  + " ";
        }
        System.err.println(keys.length + "  unique keys: " + uniqueKeys);

        for (int i = 0; i < numItems; i++) {
            String key = keys[ rng.nextInt(keys.length)];
            int val = rng.nextInt(100);
            l.addHead(key, val);
        }
        return l;

    }

    /**
    * Main function for some simple testing.
    * @param args Command line arguments
    */
    public static void main(String[] args) {
        LinkedList list;
        list = ListApp.generateData(5, 9, 43);

        System.out.println("List:          " + list);

        list.unlinkNode(list.getHead().getNext());
        System.out.println("Unlinked head next: " + list);

        list.unlinkNode(list.getHead());
        System.out.println("Unlinked head:  " + list);

        list.unlinkNode(list.getTail());
        System.out.println("Unlinked tail:  " + list);

        list.addAfter(new LinkedList.Node("te", 42), list.getHead().getNext());
        System.out.println("Added: " + list);

        list.swapIfNeeded(list.getHead());
        System.out.println("After swap:    " + list);

        list.sort();
        System.out.println("Sorted list: " + list);
    }
}