/**
 * @author Evan Parker
 * @version v0.0.1
 * @param <K> Key type
 * @param <V> Value type
 */
public class LinkedMap<K, V> implements Map<K, V> {

    /**
     * Inner class.
     * @param <K> Key type
     * @param <V> Value type
     */
    public class Entry<K, V> implements Map.Entry<K, V> {
        K key;
        V value;
        
        /**
         * Constructor.
         * @param key Key
         * @param value Value
         */
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Get the key.
         * @return K
         */
        public K getKey() {
            return key;
        }

        /**
         * Get the value.
         * @return V
         */
        public V getValue() {
            return value;
        }

        /**
         * Returns a string representation of the object.
         * @return String
         */
        public String toString() {
            return String.format("(%s, %s)", this.key, this.value);
        }

        /**
         * Compares the specified object with this entry for equality.
         * @param o Object
         * @return boolean
         */
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }

            if (o == null) {
                return false;
            }

            if (o instanceof Entry) {
                Entry<K, V> e = (Entry<K, V>) o;
                return this.key.equals(e.key) && this.value.equals(e.value);
            }

            if (o instanceof String) {
                return this.key.equals(o);
            }

            return false;
        }
    }

    LinkedList<Entry<K, V>> list = new LinkedList<Entry<K, V>>();

    /**
     * Associates the specified value with the specified key in this map.
     */
    public LinkedMap() {
        this.list = new LinkedList<Entry<K, V>>();
    }

    /**
     * Returns the list.
     * @return The list that checkstyle won't let me format right.
     */
    public LinkedList<Entry<K, V>> getList() {
        return list;
    }

    /**
     * Puts a value into the map. If the key already exists, the value is updated.
     * @param key Key
     * @param value Value
     * @return V
     */
    public V put(K key, V value) {
        List.Node current = list.getHead();

        while (current != null) {
            Entry<K, V> e = (Entry<K, V>) current.value;

            if (e.key.equals(key)) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }

            current = current.next;
        }

        list.add(new Entry<K, V>(key, value));
        return value;
    }

    /**
     * Puts a value into the map if the key does not already exist.
     * @param key Key
     * @param value Value
     * @return V
     */
    public V putIfAbsent(K key, V value) {
        List.Node current = list.getHead();

        while (current != null) {
            Entry<K, V> e = (Entry<K, V>) current.value;

            if (e.key.equals(key)) {
                return e.value;
            }

            current = current.next;
        }

        list.add(new Entry<K, V>(key, value));
        return value;
    }

    /**
     * Returns a string representation of the object.
     * @return String
     */
    public String toString() {
        return list.toString();
    }

    /**
     * Clear the "map".
     */
    public void clear() {
        list.clear();
    }

    /**
     * Gets the value of the key if it exists in this "map".
     * @param key Key
     * @return V
     */
    public V get(K key) {
        List.Node current = list.getHead();

        while (current != null) {
            Entry<K, V> e = (Entry<K, V>) current.value;

            if (e.key.equals(key)) {
                return e.value;
            }

            current = current.next;
        }

        return null;
    }

    /**
     * Checks if the "map" has a specific key.
     * @param key Key
     * @return boolean
     */
    public boolean containsKey(K key) {
        List.Node current = list.getHead();

        while (current != null) {
            Entry<K, V> e = (Entry<K, V>) current.value;

            if (e.key.equals(key)) {
                return true;
            }

            current = current.next;
        }

        return false;
    }

    /**
     * Checks if the "map" is empty.
     * @return boolean
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Removes a key from the "map".
     * @param key Key
     * @return V
     */
    public V remove(K key) {
        List.Node current = list.getHead();

        while (current != null) {
            Entry<K, V> e = (Entry<K, V>) current.value;

            if (e.key.equals(key)) {
                list.remove(e);
                return e.value;
            }

            current = current.next;
        }

        return null;
    }

    /**
     * Returns the number of elements in this "map".
     * @return int
     */
    public int size() {
        return list.size();
    }
}
