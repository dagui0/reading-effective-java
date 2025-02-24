package effectivejava.chapter03.item12;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PhoneNumberTest {

    @Test
    public void testToString() {
        PhoneNumber pn = new PhoneNumber(10, 123, 4567);
        String expected = "010-0123-4567";

        assertEquals(expected, pn.toString());
    }
}
