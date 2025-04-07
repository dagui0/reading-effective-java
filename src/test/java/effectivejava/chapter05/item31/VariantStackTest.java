package effectivejava.chapter05.item31;

import effectivejava.chapter05.item29.Stack3;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VariantStackTest {

    @Test
    public void testStack3PushAll() {

        Stack3<Number> stack = new Stack3<>();
        List<Integer> intList = List.of(1, 2, 3);
        // stack.pushAll(intList); // compile error
        stack.pushAll(intList.stream().map(i -> (Number) i)::iterator);

        assertEquals(intList.get(2), stack.pop());
        assertEquals(intList.get(1), stack.pop());
        assertEquals(intList.get(0), stack.pop());
    }

    @Test
    public void testStack4PushAll() {

        Stack4<Number> stack = new Stack4<>();
        List<Integer> intList = List.of(1, 2, 3);
        stack.pushAll(intList);

        assertEquals(intList.get(2), stack.pop());
        assertEquals(intList.get(1), stack.pop());
        assertEquals(intList.get(0), stack.pop());
    }

    @Test void testStack4PopAll() {

        Stack4<Integer> stack = new Stack4<>();
        List<Integer> intList = List.of(1, 2, 3);
        stack.pushAll(intList);

        List<Number> numList = new ArrayList<>();
        stack.popAll(numList);

        assertEquals(intList.reversed(), numList);
    }
}
