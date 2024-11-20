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
    public class Entry<K extends Comparable<K>, V> implements Map.Entry<K, V> {
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
         * @param o Object
         * @return int
         */
        @SuppressWarnings("unchecked")
        public int compareTo(Object o) {
            if (o instanceof Entry) {
                Entry<K, V> e = (Entry<K, V>) o;
                return key.compareTo(e.key);
            }

            K k = (K) o;
            return key.compareTo(k);
        }
    }

    
}
