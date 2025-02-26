package com.yidigun.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleLinkedListTest {

    @Test
    public void testSimpleLinkedList() {

        try (SimpleLinkedList list = SimpleLinkedList.newInstance()) {
            list.add(1);
            list.add(2);
            list.add(3);
            list.add(4);
            list.add(5);

            assertEquals(5, list.get(4));

            list.addAll(6, 7, 8, 9);
            assertEquals(9, list.size());
            assertEquals(9, list.get(8));

            assertThrows(IndexOutOfBoundsException.class, () -> list.insertAt(-1, 10000));

            list.insertAt(2, 10);
            assertEquals(10, list.get(2));
            assertEquals(3, list.get(3));

            assertTrue(list.contains(10));
            assertFalse(list.contains(1000));

            list.removeAt(2);
            assertEquals(3, list.get(2));
            assertEquals(4, list.get(3));

            list.removeAll();
            assertEquals(0, list.size());
        }
    }
}
