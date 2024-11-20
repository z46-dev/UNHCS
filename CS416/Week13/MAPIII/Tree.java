/**
 * Specification for a Tree ADT.
 * @author Sofia Lemons
 * @version 04/11/2019
 * @param <T> Type
 */
public interface Tree<T> {

    /**
     * Appends the specified value to this tree. Does not allow duplicates.
     *
     * @param value T The value to add
     * @return boolean True if the value is inserted, false otherwise
     */
    boolean add(T value);

    /**
     * Removes all of the elements from this tree.
     */

    void clear();

    /**
     * Returns true if this tree contains the specified element.
     *
     * @param o Object The element to check if present in the tree
     * @return boolean
     */

    boolean contains(Object o);

    /**
     * Returns the element that matches the object parameter in this tree.
     *
     * @param o Object to search the tree for matching elements.
     * @return T
     */

    T get(Object o);

    /**
     * Removes the first occurrence of the specified element from this
     * tree, if it is present.
     * If tree does not contain the element, it is unchanged.
     * Returns true if this tree contained the specified element
     * (or equivalently, if this tree changed as a result of the call).
     *
     * @param o element to be removed from this tree, if present
     * @return true if this tree contained the specified element
     */

    boolean remove(Object o);

    /**
     * Returns true if this tree contains no elements.
     *
     * @return true if this tree contains no elements
     */

    boolean isEmpty();


    /**
     * Returns the number of elements in this tree.
     * @return int
     */

    int size();

    /**
     * Inner interface to represent a Tree node.
     *  @param <T> Type
     */
    interface Node<T> {

        /**
         * Set the value of this node.
         * @param value Value
         */
        void setValue(T value);

        /**
         * Get the value of this node.
         *
         * @return T Value
         */
        T getValue();
    }
}