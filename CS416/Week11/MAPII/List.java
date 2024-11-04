/**
 * Specification for a List ADT.
 * @author Shubham Chatterjee
 * @version 04/05/2019
 * @param <T> Type
 */
public abstract class List<T> {

    /**
     * Appends the specified value to the end of this list.
     *
     * @param value T The value to add
     * @return boolean True if the value is inserted, false otherwise
     */
    abstract boolean add(T value);

    /**
     * Inserts the specified value at the specified position in this list.
     *
     * @param index Integer The index at which to insert
     * @param value T The value to insert
     */
    abstract void add(int index, T value);

    /**
     * Removes all of the elements from this list.
     */

    abstract void clear();

    /**
     * Returns true if this list contains the specified element.
     *
     * @param t T The element to check if present in the list
     * @return boolean
     */

    abstract boolean contains(T t);

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index Integer The index at which to insert
     * @return T
     */

    abstract T get(int index);

    /**
     * Removes the element at the specified position in this list.
     * Returns the element from the list or null if index is invalid.
     *
     * @param index the index of the element to be removed
     * @return the element previously at the specified position or null
     */

    /**
     * Get the list entry corresponding to the value provided in the parameter.
     * @param o to search for
     * @return T matching data in the list
     */
    abstract T get(Object o);

    /**
     * Removes the element at the specified position in this list.
     * @param index the index of the element to be removed
     * @return T the element previously at the specified position
     */
    abstract T remove(int index);

    /**
     * Removes the first occurrence of the specified element from this
     * list, if it is present.
     * If this list does not contain the element, it is unchanged.
     * Returns true if this list contained the specified element
     * (or equivalently, if this list changed as a result of the call).
     *
     * @param t element to be removed from this list, if present
     * @return true if this list contained the specified element
     */

    abstract boolean remove(T t);

    /**
     * Returns true if this list contains no elements.
     *
     * @return true if this list contains no elements
     */

    abstract boolean isEmpty();


    /**
     * Returns the number of elements in this list.
     * @return int
     */

    abstract int size();

    /**
     * Inner class to represent a List node.
     */
    public class Node {
        T value;
        Node prev;
        Node next;

        /**
         * Constructor.
         *
         * @param value V The value
         */

        public Node(T value) {
            this.value = value;
            this.prev = null;
            this.next = null;
        }

        /**
         * Set the value of this node.
         * @param value Value
         */
        public void setValue(T value) {
            this.value = value;
        }
    }
}