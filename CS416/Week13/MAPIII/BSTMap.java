/**
 * @author Evan Parker
 * @version v0.0.1
 * @param <K> Comparable Key
 * @param <V> Value
 */
public class BSTMap<K extends Comparable<K>, V> implements Map<K, V> {
    /**
     * @author Evan Parker
     * @version v0.0.1
     * @param <K> Comparable Key
     * @param <V> Value
     */
    public class Entry<K extends Comparable<K>, V> implements Map.Entry<K, V>, Comparable<Entry<K, V>> {
        K key;
        V value;

        /**
         * Constructor.
         * @param key K
         * @param value V
         */
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * @return K
         */
        public K getKey() {
            return key;
        }

        /**
         * @return V
         */
        public V getValue() {
            return value;
        }

        /**
         * @return String
         */
        public String toString() {
            return String.format("(%s, %s)", key, value);
        }

        /**
         * @param o Object
         * @return boolean
         */
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }

            if (o == this) {
                return true;
            }

            if (o.getClass() != this.getClass()) {
                return false;
            }

            Entry<K, V> e = (Entry<K, V>) o;

            return key.equals(e.key) && value.equals(e.value);
        }

        /**
         * @param e Entry
         * @return int
         */
        public int compareTo(Entry<K, V> e) {
            return key.compareTo(e.key);
        }

        /**
         * @param k K
         * @return int
         */
        public int compareTo(K k) {
            return key.compareTo(k);
        }
    }

    private BST<BSTMap<K, V>.Entry<K, V>> bst;

    /**
     * Constructor.
     */
    public BSTMap() {
        this.bst = new BST<BSTMap<K, V>.Entry<K, V>>();
    }

    /**
     * Returns thr tree.
     * @return BST
     */
    public BST<BSTMap<K, V>.Entry<K, V>> getTree() {
        return this.bst;
    }

    /**
     * Puts a key and value into the tree.
     * 
     * @param key K
     * @param value V
     * @return value
     */
    public V put(K key, V value) {
        BSTMap<K, V>.Entry<K, V> entry = new BSTMap.Entry(key, value);
        BSTMap<K, V>.Entry<K, V> oldEntry = this.bst.get(entry);

        if (oldEntry != null) {
            V oldValue = oldEntry.value;
            oldEntry.value = value;
            return oldValue;
        }

        this.bst.add(entry);
        return null;
    }

    /**
     * Puts a key and value into the tree if the key is not already in the tree.
     * @param key K
     * @param value V
     * @return value
     */
    public V putIfAbsent(K key, V value) {
        BSTMap<K, V>.Entry<K, V> entry = new BSTMap.Entry(key, value);
        
        BSTMap<K, V>.Entry<K, V> oldEntry = this.bst.get(entry);

        if (oldEntry != null && oldEntry.value != null) {
            return oldEntry.value;
        }

        this.bst.add(entry);
        return value;
    }

    /**
     * Returns a string representation of the tree.
     * @return String
     */
    public String toString() {
        return this.bst.toString();
    }

    /**
     * Clears the tree.
     */
    public void clear() {
        this.bst.clear();
    }

    /**
     * Returns the value of a key.
     * @param key K
     * @return V
     */
    public V get(K key) {
        BSTMap<K, V>.Entry<K, V> entry = new BSTMap.Entry(key, null);
        BSTMap<K, V>.Entry<K, V> oldEntry = this.bst.get(entry);

        if (oldEntry != null) {
            return oldEntry.value;
        }

        return null;
    }

    /**
     * Checks if the tree contains a key.
     * @param key K
     * @return boolean
     */
    public boolean containsKey(K key) {
        BSTMap<K, V>.Entry<K, V> entry = new BSTMap.Entry(key, null);
        BSTMap<K, V>.Entry<K, V> oldEntry = this.bst.get(entry);

        if (oldEntry != null) {
            return true;
        }

        return false;
    }

    /**
     * Removes a key from the tree.
     * @param key K
     * @return V
     */
    public V remove(K key) {
        BSTMap<K, V>.Entry<K, V> entry = new BSTMap.Entry(key, null);
        BSTMap<K, V>.Entry<K, V> oldEntry = this.bst.get(entry);

        if (oldEntry != null) {
            this.bst.remove(oldEntry);
            return oldEntry.value;
        }

        return null;
    }

    /**
     * Returns the size of the tree.
     * @return int
     */
    public int size() {
        return this.bst.size();
    }

    /**
     * Checks if the tree is empty.
     * @return boolean
     */
    public boolean isEmpty() {
        return this.bst.isEmpty();
    }
}