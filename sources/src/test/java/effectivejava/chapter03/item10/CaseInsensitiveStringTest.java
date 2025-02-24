package effectivejava.chapter03.item10;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CaseInsensitiveStringTest {

    @Test
    public void testCaseInsensitiveString1Equals() {
        CaseInsensitiveString1 cis1 = new CaseInsensitiveString1("abc");
        CaseInsensitiveString1 cis2 = new CaseInsensitiveString1("ABC");
        assertEquals(cis1, cis2);
        assertEquals(cis2, cis1);
    }

    @Test
    public void testCaseInsensitiveString1EqualsWithString() {
        String s1 = "ABC";
        CaseInsensitiveString1 cis1 = new CaseInsensitiveString1("abc");
        assertEquals(cis1, s1);
        assertNotEquals(s1, cis1);
    }

    @Test
    public void testCaseInsensitiveStringEquals() {
        CaseInsensitiveString cis = new CaseInsensitiveString("abc");
        String s = "ABC";
        assertNotEquals(cis, s);
    }
}
