package effectivejava.chapter03.item13;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HashTableTest {

    @Test
    public void testCloneRecursive() {
        HashTable.setEntryCopyMethod(HashTable.EntryCopyMethod.RECURSIVE);
        HashTable hashTable = new HashTable();
        hashTable.put("foo", "Foo");
        hashTable.put("bar", "Bar");

        HashTable clone = hashTable.clone();
        clone.put("foo", "Foo2");
        clone.put("bar", "Bar2");

        assertEquals("Foo", hashTable.get("foo"));
        assertEquals("Foo2", clone.get("foo"));
        assertEquals("Bar", hashTable.get("bar"));
        assertEquals("Bar2", clone.get("bar"));
    }

    @Test
    public void testCloneLoop() {
        HashTable.setEntryCopyMethod(HashTable.EntryCopyMethod.LOOP);
        HashTable hashTable = new HashTable();
        hashTable.put("foo", "Foo");
        hashTable.put("bar", "Bar");

        HashTable clone = hashTable.clone();
        clone.put("foo", "Foo2");
        clone.put("bar", "Bar2");

        assertEquals("Foo", hashTable.get("foo"));
        assertEquals("Foo2", clone.get("foo"));
        assertEquals("Bar", hashTable.get("bar"));
        assertEquals("Bar2", clone.get("bar"));
    }
}
