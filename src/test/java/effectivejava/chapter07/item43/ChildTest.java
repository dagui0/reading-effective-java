package effectivejava.chapter07.item43;

import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class ChildTest {

    public static String invokeGetName(Supplier<String> supplier) {
        return supplier.get();
    }

    @Test
    public void testInInstanceContext() {
        Child child = new Child();
        child.inInstanceContext();
    }
}

class Parent {
    public String getName() { return "Parent"; }
}

class Child extends Parent {
    @Override
    public String getName() { return "Child"; }

    @SuppressWarnings({"Convert2MethodRef"})
    void inInstanceContext() {
        String s = null;

        // "Child"
        assertEquals("Child", ChildTest.invokeGetName(() -> this.getName()));
        assertEquals("Child", ChildTest.invokeGetName(this::getName));

        // "Parent"
        assertEquals("Parent", ChildTest.invokeGetName(() -> super.getName()));
        assertEquals("Parent", ChildTest.invokeGetName(super::getName));
    }
}
