package effectivejava.chapter03.item13;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class PhoneNumberTest {

    @Test
    public void testClone() {
        PhoneNumber pn = new PhoneNumber(10, 123, 4567);
        PhoneNumber pn2 = pn.clone();
        assertNotSame(pn, pn2);
        assertEquals(pn, pn2);
    }
}
