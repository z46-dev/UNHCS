/**
 * @author Evan Parker
 * @version v0.0.1
 * @param <T> Type
 */
public class LinkedList<T> extends List<T> {
    private Node head;
    private Node tail;

    /**
     * Constructor.
     */
    public LinkedList() {
        this.head = null;
        this.tail = null;
    }

    /**
     * Gets the head of the linked list.
     * @return The head
     */
    public Node getHead() {
        return this.head;
    }

    /**
     * Gets the tail of the linked list.
     * @return The tail
     */
    public Node getTail() {
        return this.tail;
    }

    /**
     * Appends a value to end of list. Will not add duplicates.
     * @param value The value to add
     * @return True if added, false otherwise
     */
    public boolean add(T value) {
        Node node = new Node(value);

        if (this.head == null) {
            this.head = node;
            this.tail = node;
        } else {
            Node current = this.head;
            while (current != null) {
                if (current.value.equals(value)) {
                    return false;
                }

                current = current.next;
            }

            this.tail.next = node;
            node.prev = this.tail;
            this.tail = node;
        }

        return true;
    }

    /**
     * Adds a value at an index to the list. Will not add duplicates.
     * @param index The index to add the value
     * @param value The value to add
     */
    public void add(int index, T value) {
        Node node = new Node(value);

        if (this.head == null) {
            this.head = node;
            this.tail = node;
        } else {
            Node current = this.head;
            while (current != null) {
                if (current.value.equals(value)) {
                    return;
                }

                current = current.next;
            }

            current = this.head;
            int i = 0;
            while (current != null) {
                if (i == index) {
                    if (current.prev != null) {
                        current.prev.next = node;
                        node.prev = current.prev;
                    } else {
                        this.head = node;
                    }

                    node.next = current;
                    current.prev = node;
                    return;
                }

                current = current.next;
                i++;
            }

            this.tail.next = node;
            node.prev = this.tail;
            this.tail = node;
        }
    }

    /**
     * Stringifies the linked list.
     * @return The stringified linked list
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node current = this.head;
        sb.append("[");

        while (current != null) {
            sb.append(current.value.toString());
            current = current.next;

            if (current != null) {
                sb.append(", ");
            }
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * Clears the linked list.
     */
    public void clear() {
        this.head = null;
        this.tail = null;
    }

    /**
     * Gets the value in the list at the index.
     * @param index The index to get the value
     * @return The value at the index
     */
    public T get(int index) {
        Node current = this.head;
        int i = 0;
        while (current != null) {
            if (i == index) {
                return current.value;
            }

            current = current.next;
            i++;
        }

        return null;
    }

    /**
     * Gets an object from the list.
     * @param o The object to get
     * @return The object if found, null otherwise
     */
    public T get(Object o) {
        Node current = this.head;
        while (current != null) {
            if (current.value.equals(o)) {
                return current.value;
            }

            current = current.next;
        }

        return null;
    }

    /**
     * Checks if a value is in the list.
     * @param o The value to check
     * @return True if the value is in the list, false otherwise
     */
    public boolean contains(Object o) {
        return this.get(o) != null;
    }

    /**
     * Checks if the list is empty.
     * @return True if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return this.head == null;
    }

    /**
     * Removes a value from the list.
     * @param index The index to remove the value
     * @return The value if removed, null otherwise
     */
    public T remove(int index) {
        Node current = this.head;
        int i = 0;
        while (current != null) {
            if (i == index) {
                if (current.prev != null) {
                    current.prev.next = current.next;
                } else {
                    this.head = current.next;
                }

                if (current.next != null) {
                    current.next.prev = current.prev;
                } else {
                    this.tail = current.prev;
                }

                return current.value;
            }

            current = current.next;
            i++;
        }

        return null;
    }

    /**
     * Removes a value from the list.
     * @param o The value to remove
     * @return True if removed, false otherwise
     */
    public boolean remove(Object o) {
        Node current = this.head;
        while (current != null) {
            if (current.value.equals(o)) {
                if (current.prev != null) {
                    current.prev.next = current.next;
                } else {
                    this.head = current.next;
                }

                if (current.next != null) {
                    current.next.prev = current.prev;
                } else {
                    this.tail = current.prev;
                }

                return true;
            }

            current = current.next;
        }

        return false;
    }

    /**
     * Gets the size of the list.
     * @return The size of the list
     */
    public int size() {
        Node current = this.head;
        int size = 0;
        while (current != null) {
            size++;
            current = current.next;
        }

        return size;
    }
}