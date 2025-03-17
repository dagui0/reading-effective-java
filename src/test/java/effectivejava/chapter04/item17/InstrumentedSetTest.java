package effectivejava.chapter04.item17;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InstrumentedSetTest {

    @Test
    public void testInstrumentedHashSet() {
        InstrumentedHashSet<String> set = new InstrumentedHashSet<>(10);
        set.addAll(List.of("A", "B", "C"));

        // 3이 아니고 6이 된다.
        assertEquals(6, set.getAddCount());
    }


    @Test
    public void testInstrumentedSet() {
        InstrumentedSet<String> set = new InstrumentedSet<>(new HashSet<>(10));
        set.addAll(List.of("A", "B", "C"));

        assertEquals(3, set.getAddCount());
    }
}
