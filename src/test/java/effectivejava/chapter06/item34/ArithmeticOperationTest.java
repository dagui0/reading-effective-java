package effectivejava.chapter06.item34;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArithmeticOperationTest {

    @Test
    public void testArithmeticOperations() {
        double x = 10;
        double y = 5;

        assertEquals(15.0, ArithmeticOperation2.PLUS.apply(x, y));
        assertEquals(5.0, ArithmeticOperation2.MINUS.apply(x, y));
        assertEquals(50.0, ArithmeticOperation2.TIMES.apply(x, y));
        assertEquals(2.0, ArithmeticOperation2.DIVIDE.apply(x, y));
    }

    @Test
    public void testToString() {

        double x = 10;
        double y = 5;

        assertEquals("10.0 + 5.0 = 15.0", "%.1f %s %.1f = %.1f".formatted(x, ArithmeticOperation2.PLUS, y, ArithmeticOperation2.PLUS.apply(x, y)));
        assertEquals("10.0 - 5.0 = 5.0", "%.1f %s %.1f = %.1f".formatted(x, ArithmeticOperation2.MINUS, y, ArithmeticOperation2.MINUS.apply(x, y)));
        assertEquals("10.0 * 5.0 = 50.0", "%.1f %s %.1f = %.1f".formatted(x, ArithmeticOperation2.TIMES, y, ArithmeticOperation2.TIMES.apply(x, y)));
        assertEquals("10.0 / 5.0 = 2.0", "%.1f %s %.1f = %.1f".formatted(x, ArithmeticOperation2.DIVIDE, y, ArithmeticOperation2.DIVIDE.apply(x, y)));
    }
}
