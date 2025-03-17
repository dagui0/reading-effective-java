package effectivejava.chapter04.item17;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConcurrentModificationTest {

    @Test
    public void testHashMapConcurrentModification() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        assertThrows(ConcurrentModificationException.class, () -> {
            for (String key : map.keySet()) {
                map.put(key + "-1", map.get(key) + "-1");
            }
        });
    }

    @Test
    public void testHashtableConcurrentModification() {
        Hashtable<String, String> hashtable = new Hashtable<>();
        hashtable.put("key1", "value1");
        hashtable.put("key2", "value2");

        Enumeration keys = hashtable.keys();
        while (keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            hashtable.put(key + "-1", hashtable.get(key) + "-1");
        }

        assertEquals("value1-1", hashtable.get("key1-1"));
        assertEquals("value2-1", hashtable.get("key2-1"));
    }

    @Test
    public void testSynchronizedMapConcurrentModification() {
        Map<String, String> map = Collections.synchronizedMap(new HashMap<>());
        map.put("key1", "value1");
        map.put("key2", "value2");

        assertThrows(ConcurrentModificationException.class, () -> {
            for (String key : map.keySet()) {
                map.put(key + "-1", map.get(key) + "-1");
            }
        });
    }

    @Test
    public void testConcurrentMapConcurrentModification() {
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        for (String key : map.keySet()) {
            map.put(key + "-1", map.get(key) + "-1");
        }

        assertEquals("value1-1", map.get("key1-1"));
        assertEquals("value2-1", map.get("key2-1"));
    }
}
