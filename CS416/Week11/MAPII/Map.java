/**
 * Specification for a Map ADT.
 * @author Shubham Chatterjee
 * @version 03/07/2019
 * @param <K> Key type
* @param <V> Value type
 */
public interface Map<K, V> {
    /**
     * Removes all of the mappings from this map.
     * The map will be empty after this call returns.
     */
    void clear();

    /**
     * Returns true if this map contains a mapping for the specified key.
     * @param key key whose presence in this map is to be tested
     * @return true if this map contains a mapping for the specified key
     */
    boolean containsKey(K key);

    /**
     * Returns the value to which the specified key is mapped,
     * or null if this map contains no mapping for the key.
     * If this map permits null values, then a return value of null does not necessarily indicate
     * that the map contains no mapping for the key;
     * it's also possible that the map explicitly maps the key to null.
     * The Map#containsKey operation may be used to distinguish these two cases.
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped,
     * or null if this map contains no mapping for the key
     */
    V get(K key);


    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced by the specified value.
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with key, or null if there was 
     * no mapping for key.
     * (A null return can also indicate that the map previously associated 
     * null with key, if the implementation supports null values.)
     */
    V put(K key, V value);


    /**
     * If the specified key is not already associated with a value 
     * (or is mapped to null) associates it with the given value and 
     * returns null, else returns the current value.
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with key, or null if there 
     * was no mapping for key.
     * (A null return can also indicate that the map previously associated 
     * null with key,
     * if the implementation supports null values.)
     */
    V putIfAbsent(K key, V value);

    /**
     * Returns true if this map contains no key-value mappings.
     * @return true if this map contains no key-value mappings
     */
    boolean isEmpty();

    /**
     * Removes the mapping for a key from this map if it is present.
     * Returns the value to which this map previously associated the key,
     * or null if the map contained no mapping for the key.
     * If this map permits null values, then a return value of null does not necessarily indicate
     * that the map contained no mapping for the key;
     * it's also possible that the map explicitly mapped the key to null.
     * The map will not contain a mapping for the specified key once the call returns.
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with key, or null if there was no mapping for key.
     */
    V remove(K key);

    /**
     * Returns the number of key-value mappings in this map.
     * @return the number of key-value mappings in this map
     */
    int size();

    /**
     * Specification of an entry in the map.
     * @param <K> Key type
     * @param <V> Value type
     */

    interface Entry<K, V> {
        /**
         * Returns the key corresponding to this entry.
         * @return the key corresponding to this entry
         */
        K getKey();

        /**
         * Returns the value corresponding to this entry.
         * @return the value corresponding to this entry
         */
        V getValue();
    }
}