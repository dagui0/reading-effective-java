package effectivejava.chapter04.item24;

public class AnonymousExampleMap<K,V> {
    public static class Entry<K,V> {
        private K key;
        private V value;
        public Entry(Entry<K,V> entry) {
            this.key = entry.key;
            this.value = entry.value;
        }
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
        public K getKey() {
            return key;
        }
        public void setKey(K key) {
            this.key = key;
        }
        public V getValue() {
            return value;
        }
        public void setValue(V value) {
            this.value = value;
        }
    }

    public static <K,V> Entry<K,V> readonlyEntry(Entry<K,V> entry) {
        return new Entry<>(entry) {
            @Override
            public void setKey(K key) {
                throw new UnsupportedOperationException("setKey");
            }
            @Override
            public void setValue(V value) {
                throw new UnsupportedOperationException("setValue");
            }
        };
    }
}
