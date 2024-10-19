
/**
 * LinkedList lab.
 * 
 * @author Evan Parker
 * @version v0.0.1
 */
public class LinkedList {

    /**
     * Internal Node class used for the linked list.
     */
    public class Node {
        String key;
        int value;
        Node next;

        /**
         * Node Constructor.
         * 
         * @param key   The Key
         * @param value The Value
         */
        public Node(String key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node head;
    private Node tail;

    /**
     * The default constructor.
     */
    public LinkedList() {
        head = null;
        tail = null;
    }

    /**
     * Add the key, value pair to the head of the linkedlist.
     * 
     * @param key The Key
     * @param val The Value
     *
     */
    public void addHead(String key, int val) {
        Node n = new Node(key, val);

        if (head == null) {
            head = n;
            tail = n;
        } else {
            n.next = head;
            head = n;
        }
    }

    /**
     * Add the key, val pair to the tail of the linkedlist.
     * 
     * @param key The Key
     * @param val The Value
     */
    public void addTail(String key, int val) {
        Node n = new Node(key, val);

        if (tail == null) {
            head = n;
            tail = n;
        } else {
            tail.next = n;
            tail = n;
        }
    }

    /**
     * Returns the String format of the linkedlist.
     * 
     * @return String The String format.
     */
    public String toString() {
        String ret = "";

        Node curr = head;

        while (curr != null) {
            if (curr.next != null) {
                ret += curr.key + ":" + curr.value + ", ";
            } else {
                ret += curr.key + ":" + curr.value;
            }

            curr = curr.next;
        }

        return ret;
    }

    /**
     * Locate the Node in the linkedlist with the given key.
     * 
     * @param key The key to find in the LinkedList
     * @return Node Returns the Node with the given key or null if non-existent.
     */
    private Node find(String key) {
        Node curr = head;

        while (curr != null) {
            if (curr.key.equals(key)) {
                return curr;
            }

            curr = curr.next;
        }

        return null;
    }

    //////////////////////// YOUR CODE HERE ////////////////////////

    /**
     * Gets the average of a linked list.
     * 
     * @param list The list to average
     * @return double the average
     */
    public static double average(LinkedList list) {
        Node curr = list.head;
        double sum = 0;
        int count = 0;

        while (curr != null) {
            sum += curr.value;
            count++;
            curr = curr.next;
        }

        return sum / count;
    }

    /**
     * Checks if a list is in order by the key.
     * 
     * @param list The list to check
     * @return boolean if the list is ordered
     */
    public static boolean ordered(LinkedList list) {
        Node curr = list.head;

        while (curr.next != null) {
            if (curr.key.compareTo(curr.next.key) > 0) {
                return false;
            }

            curr = curr.next;
        }

        return true;
    }

    /**
     * Reverses a linked list.
     * 
     * @param list The list to reverse
     * @return LinkedList the reversed list
     */
    public static LinkedList reversed(LinkedList list) {
        LinkedList reverse = new LinkedList();
        Node curr = list.head;

        while (curr != null) {
            reverse.addHead(curr.key, curr.value);
            curr = curr.next;
        }

        return reverse;
    }

    /**
     * Compresses a linked list.
     * 
     * @param list The list to compress
     * @return LinkedList the compressed list
     */
    public static LinkedList compressList(LinkedList list) {
        LinkedList compressed = new LinkedList();
        Node curr = list.head;

        while (curr != null) {
            Node found = compressed.find(curr.key);

            if (found == null) {
                compressed.addTail(curr.key, curr.value);
            } else {
                found.value += curr.value;
            }

            curr = curr.next;
        }

        return compressed;
    }

    ///////////////////////////////////////////////////////////////

}