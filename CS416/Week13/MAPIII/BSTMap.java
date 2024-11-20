public class BSTMap<K extends Comparable<K>, V> implements Map<K, V> {
    public class Entry<K extends Comparable<K>, V> implements Map.Entry<K,V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public String toString() {
            return String.format("(%s, %s)", key, value);
        }

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
