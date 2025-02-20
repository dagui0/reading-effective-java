package effectivejava.chapter03.rule11;

public class HashTable implements Cloneable {
    private Entry[] buckets = new Entry[10];

    private static EntryCopyMethod entryCopyMethod = EntryCopyMethod.RECURSIVE;

    public static EntryCopyMethod getEntryCopyMethod() {
        return entryCopyMethod;
    }

    public static void setEntryCopyMethod(EntryCopyMethod entryCopyMethod) {
        HashTable.entryCopyMethod = entryCopyMethod;
    }

    public enum EntryCopyMethod {
        RECURSIVE {
            @Override
            Entry copy(Entry entry) {
                return entry.deepCopyRecursive();
            }
        },
        LOOP {
            @Override
            Entry copy(Entry entry) {
                return entry.deepCopyLoop();
            }
        };

        abstract Entry copy(Entry entry);
    }

    private static class Entry {
        final Object key;
        Object value;
        Entry next;

        Entry(Object key, Object value, Entry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        Entry deepCopy() {
            return entryCopyMethod.copy(this);
        }

        Entry deepCopyRecursive() {
            return new Entry(key, value, next == null ? null : next.deepCopy());
        }

        Entry deepCopyLoop() {
            Entry result = new Entry(key, value, next);
            for (Entry p = result; p.next != null; p = p.next) {
                p.next = new Entry(p.next.key, p.next.value, p.next.next);
            }
            return result;
        }
    }

    public void put(Object key, Object value) {
        int bucketIndex = key.hashCode() % buckets.length;
        for (Entry e = buckets[bucketIndex]; e != null; e = e.next) {
            if (e.key.equals(key)) {
                e.value = value;
                return;
            }
        }
        buckets[bucketIndex] = new Entry(key, value, buckets[bucketIndex]);
    }

    public Object get(Object key) {
        int bucketIndex = key.hashCode() % buckets.length;
        for (Entry e = buckets[bucketIndex]; e != null; e = e.next) {
            if (e.key.equals(key)) {
                return e.value;
            }
        }
        return null;
    }

    @Override
    public HashTable clone() {
        try {
            HashTable result = (HashTable) super.clone();
            result.buckets = new Entry[buckets.length];
            for (int i = 0; i < buckets.length; i++) {
                if (buckets[i] != null) {
                    result.buckets[i] = buckets[i].deepCopy();
                }
            }
            return result;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
