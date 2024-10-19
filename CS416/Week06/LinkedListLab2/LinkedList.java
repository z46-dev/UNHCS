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
    public static class Node {
        String key;
        int value;
        Node next;
        Node prev;

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

        /**
         * Gets the next Node in the List.
         * 
         * @return Node
         */
        public Node getNext() {
            return next;
        }

        /**
         * Gets the previous Node in the List.
         * 
         * @return Node
         */
        public Node getPrev() {
            return prev;
        }

        /**
         * Gets the String Key.
         * 
         * @return String
         */
        public String getKey() {
            return key;
        }

        /**
         * Gets the Value.
         * 
         * @return int
         */
        public int getValue() {
            return value;
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
     * Gets the head of the List.
     * 
     * @return Node Head Node
     */
    public Node getHead() {
        return head;
    }

    /**
     * Gets the tail of the List.
     * 
     * @return Node Tail Node
     */
    public Node getTail() {
        return tail;
    }

    /**
     * Add the key, value pair to the head of the linkedlist.
     * 
     * @param key The Key
     * @param val The Value
     */
    public void addHead(String key, int val) {
        Node n = new Node(key, val);

        if (head == null) {
            head = n;
            tail = n;
        } else {
            head.prev = n;
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
            n.prev = tail;
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
    public Node find(String key) {
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
     * Unlinks a node from the list.
     * 
     * @param n node
     */
    public void unlinkNode(Node n) {
        if (n == null) {
            return;
        }

        if (n == this.head) {
            this.head = n.next;
        }

        if (n == this.tail) {
            this.tail = n.prev;
        }

        if (n.prev != null) {
            n.prev.next = n.next;
        }

        if (n.next != null) {
            n.next.prev = n.prev;
        }

        n.next = null;
        n.prev = null;
    }

    /**
     * Adds a node after another node.
     * 
     * @param n      node
     * @param before node
     */
    public void addAfter(Node n, Node before) {
        if (before == null) {
            return;
        }

        n.next = before.next;
        n.prev = before;
        before.next = n;

        if (n.next != null) {
            n.next.prev = n;
        }

        if (before == this.tail) {
            this.tail = n;
        }
    }

    /**
     * Swaps a node if it needs to.
     * 
     * @param n node
     * @return boolean
     */
    public boolean swapIfNeeded(Node n) {
        if (n == null || n.next == null) {
            return false;
        }

        if (n.key.compareTo(n.next.key) > 0) {
            Node temp = n.next;
            n.next = temp.next;
            temp.next = n;

            if (n.next != null) {
                n.next.prev = n;
            }

            temp.prev = n.prev;
            n.prev = temp;

            if (temp.prev != null) {
                temp.prev.next = temp;
            } else {
                this.head = temp;
            }

            if (n.next == null) {
                this.tail = n;
            }

            return true;
        }

        return false;
    }

    /**
     * Sorts the linked list.
     */
    public void sort() {
        boolean swapped = true;

        while (swapped) {
            swapped = false;
            Node curr = this.head;

            while (curr != null) {
                if (swapIfNeeded(curr)) {
                    swapped = true;
                }

                curr = curr.next;
            }
        }
    }

    ///////////////////////////////////////////////////////////////

}