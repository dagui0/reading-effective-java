package effectivejava.chapter03.rule11;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StackTest {

    @Test
    public void testClone() {
        Stack stack = new Stack();
        stack.push("1");
        stack.push("2");
        stack.push("3");
        Stack clone = stack.clone();
        assertEquals("3", clone.pop());
        assertEquals("2", clone.pop());
        assertEquals("1", clone.pop());
        assertEquals("3", stack.pop());
        assertEquals("2", stack.pop());
        assertEquals("1", stack.pop());
    }
}
