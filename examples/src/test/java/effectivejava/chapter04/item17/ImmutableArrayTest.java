package effectivejava.chapter04.item17;

import com.google.common.primitives.ImmutableIntArray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImmutableArrayTest {

    @Test
    public void testImmutableIntArray() {
        ImmutableIntArray arr = ImmutableIntArray.of(1, 2, 3);

        assertEquals(1, arr.get(0));
    }
}
