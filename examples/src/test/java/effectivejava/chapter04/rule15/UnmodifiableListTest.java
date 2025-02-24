package effectivejava.chapter04.rule15;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UnmodifiableListTest {

    @Test
    public void testImmutable() {

        List<String> immutable = List.of("a", "b", "c");

        assertThrows(UnsupportedOperationException.class, () -> immutable.add("d"));
        assertThrows(UnsupportedOperationException.class, () -> immutable.remove("a"));
    }

    @Test
    public void testIteratorImmutable() {

        List<String> immutable = List.of("a", "b", "c");
        Iterator<String> iterator = immutable.iterator();

        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }
}
