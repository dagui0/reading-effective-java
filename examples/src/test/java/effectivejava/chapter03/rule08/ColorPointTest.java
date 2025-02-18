package effectivejava.chapter03.rule08;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ColorPointTest {

    @Test
    public void testColorPoint1EqualsWithPointSymmetry() {
        int x = 10, y = 11;
        Point1 point = new Point1(x, y);
        ColorPoint1 redPoint = new ColorPoint1(x, y, Color.RED);

        assertEquals(point, redPoint);      // Point1.equals(ColorPoint1) == true
        assertNotEquals(redPoint, point);   // ColorPoint1.equals(Point1) != true
    }

    @Test
    public void testColorPoint2EqualsWithPointSymmetry() {
        int x = 10, y = 11;
        Point1 point = new Point1(x, y);
        ColorPoint2 redPoint = new ColorPoint2(x, y, Color.RED);

        assertEquals(point, redPoint);      // Point1.equals(ColorPoint2) == true
        assertEquals(redPoint, point);      // ColorPoint2.equals(Point1) == true
    }

    @Test
    public void testColorPoint2EqualsWithPointTransitivity() {
        int x = 10, y = 11;
        Point1 point = new Point1(x, y);
        ColorPoint2 redPoint = new ColorPoint2(x, y, Color.RED);
        ColorPoint2 bluePoint = new ColorPoint2(x, y, Color.BLUE);

        assertEquals(redPoint, point);
        assertEquals(bluePoint, point);
        assertNotEquals(redPoint, bluePoint);
    }
}
