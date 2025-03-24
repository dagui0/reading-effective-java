package effectivejava.chapter04.item21;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyPreciousTest {

    @Test
    public void testMyPrecious() {
        MyPrecious myPrecious = new MyPrecious("John Doe", new Date());

        assertEquals("Precious", myPrecious.getOrigin());
        assertEquals("MyPrecious", myPrecious.getWhatIs());
    }

    @Test
    public void testTreasureOrigin() {
        Tresure tresure = new Tresure("Gold Ring", "John Doe");

        assertEquals("Precious", tresure.getOrigin());
        assertEquals("Tresure", tresure.getWhatIs());
    }
}
