package effectivejava.chapter09.item61;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AutoBoxingTest {

    private record AddressKey(long memberNo, long addressSeq) {}

    @SuppressWarnings("all")
    @Test
    public void testComparator() {

        Comparator<Integer> comparator =
                (i, j) -> (i < j)? -1: (i == j)? 0: 1;

        assertEquals(1, comparator.compare(new Integer(42), new Integer(42)));
        assertEquals(0, comparator.compare(Integer.valueOf(42), Integer.valueOf(42)));
    }
}
