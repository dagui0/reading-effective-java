package effectivejava.chapter08.item52;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MiscTest {

    @Test
    public void testCollection() {

        Set<Integer> set = new TreeSet<>();
        List<Integer> list = new ArrayList<>();

        for (int i = -3; i < 3; i++) {
            set.add(i);
            list.add(i);
        }

        for (int i = 0; i < 3; i++) {
            set.remove(i);
            list.remove(i);
        }

        assertEquals(Set.of(-3, -2, -1), set);
        assertEquals(List.of(-2, 0, 2), list);
    }

    @Test
    public void testRunnable() {

        new Thread(System.out::println).start();

        try (ExecutorService executor = Executors.newCachedThreadPool()) {
            // executor.submit(System.out::println);  // 컴파일 오류
        }

        // executor.submit(Runnable), executor.submit(Callable<T>) 존재
        // * Runnable: void -> void
        // * Callable<T>: void -> T
        // System.out::println -> System.out::println(), System.out::println(boolean), ...
    }

    @Test
    public void testRunnable2() {

        class MyClass {
            public void run() {
                System.out.println("Hello, World!");
            }
        }

        MyClass o = new MyClass();
        try (ExecutorService executor = Executors.newCachedThreadPool()) {
            executor.submit(o::run);
        }
    }

    @Test
    public void testContentEquals() {

        StringBuffer sb1 = new StringBuffer("Hello");

        String s = "Hello";
        s.contentEquals(sb1); // StringBuffer의 내용을 String과 비교
    }

    @Test
    public void testStringValueOf() {

        char[] chars = {'H', 'e', 'l', 'l', 'o'};

        assertEquals("Hello", String.valueOf(chars));
        assertEquals("[C@37fb0bed", String.valueOf((Object)chars));
    }

    @Test
    public void testVoid() {

        Void v = Void.TYPE.cast(null);

        System.out.println(v); // null

        assertNull(v);
    }

    @Test
    public void testEnumSet() {

        EnumSet<TestEnum> enumSet = EnumSet.of(TestEnum.A, TestEnum.B);
    }
}

enum TestEnum {
    A, B, C
}
