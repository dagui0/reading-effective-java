package effectivejava.chapter05.item28;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class NonReifiableTypeTest {

    @SuppressWarnings("unused")
    @Test
    public void testNonReifiableType() {

        // List<?> list = new ArrayList<?>();  // compile error
        List<?> list = new ArrayList<>();

        List<?>[] listArray = createGenericArray(List.class, 10);

        assertInstanceOf(List.class, list);
        assertEquals(List[].class, listArray.getClass());
    }

    @SafeVarargs
    private <T> int getVarargsCount(T... args) {
        return args.length;
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] createGenericArray(Class<T> clazz, int size) {
        return (T[])Array.newInstance(clazz, size);
    }
}
