package com.yidigun.base.examples;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ResidentKeyTest {

    @Test
    public void testMale() {
        ResidentKey key1 = ResidentKey.of("1111111111118");
        assertTrue(key1.isMale());

        ResidentKey key2 = ResidentKey.ofUnchecked("2222222222222");
        assertTrue(key2.isFemale());
    }

    @Test
    public void testIsValid() {

        assertThrows(IllegalArgumentException.class, () -> {
            ResidentKey key = ResidentKey.of("1234561234567");
        });

        String[] valids = { "1111111111118", "1234561222331" };
        for (String s : valids) {
            ResidentKey key = ResidentKey.of(s);
            assertTrue(key.isValid());
        }

        String[] nonValids = { "1234561234567", "9553179792331" };
        for (String s : nonValids) {
            ResidentKey key = ResidentKey.ofUnchecked(s);
            assertFalse(key.isValid());
        }

        Optional<ResidentKey> key1 = ResidentKey.ofNullable("1234561234567");
        assertTrue(key1.isEmpty());

        Optional<ResidentKey> key2 = ResidentKey.ofNullable("1111111111118");
        assertTrue(key2.isPresent());

        // null residentId?
        assertTrue(ResidentKey.isValid("0000000000001"));
    }

    @Test
    public void testParseAltPattern() {

        ResidentKey key1 = ResidentKey.of("1234561222331");
        ResidentKey key2 = ResidentKey.of("123456-1222331");
        assertEquals(key1, key2);
    }

    @Test
    public void testToString() {
        ResidentKey key = ResidentKey.of("1234561222331");

        assertEquals("1234561222331", key.toString());
        assertEquals("1234561222331", key.toString(false));
        assertEquals("1234561222331", key.toString(ResidentKey.PATTERN));
        assertEquals("123456-1222331", key.toString(true));
        assertEquals("123456-1222331", key.toString(ResidentKey.PATTERN_WITH_DASH));
    }

    @Test
    public void testBirthday() {
        ResidentKey key = ResidentKey.of("1111111111118");

        assertEquals("111111", key.subSequence(0, 6));

        Calendar cal = Calendar.getInstance();
        cal.set(1911, 10, 11);
        assertEquals(cal.toInstant(), key.getBirthday());
    }
}
