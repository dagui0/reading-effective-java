package effectivejava.chapter03.rule12;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ComparableTest {

    @Test
    public void testBigDecimalEquality() {
        BigDecimal bd1 = new BigDecimal("1.0");
        BigDecimal bd2 = new BigDecimal("1.00");

        assertNotEquals(bd1, bd2);
        assertEquals(0, bd1.compareTo(bd2));
    }

    @Test
    public void testIntegerCompareToOverflow() {
        int a = Integer.MAX_VALUE;
        int b = Integer.MIN_VALUE;

        int compareTo = a - b;

        assertTrue(a > b);
        assertFalse(compareTo > 0);
    }
}
