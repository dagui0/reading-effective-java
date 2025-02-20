package effectivejava.chapter03.rule09;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PhoneNumberTest {

    @Test
    public void testPhoneNumber1HashMapInsert() {
        Map<PhoneNumber1, String> map = new HashMap<>();
        PhoneNumber1 n1 = new PhoneNumber1(10, 123, 4567);
        PhoneNumber1 n2 = new PhoneNumber1(10, 123, 4567);

        map.put(n1, "Jenny");

        assertNull(map.get(n2));
        assertFalse(map.containsKey(n2));
    }

    @Test
    public void testPhoneNumberHashMapInsert() {
        Map<PhoneNumber, String> map = new HashMap<>();
        PhoneNumber n1 = new PhoneNumber(10, 123, 4567);
        PhoneNumber n2 = new PhoneNumber(10, 123, 4567);

        map.put(n1, "Jenny");

        assertNotNull(map.get(n2));
        assertTrue(map.containsKey(n2));
    }
}
