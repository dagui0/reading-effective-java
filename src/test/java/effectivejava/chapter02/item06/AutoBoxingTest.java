package effectivejava.chapter02.item06;

import org.junit.jupiter.api.Test;

public class AutoBoxingTest {

    @Test
    public void testObjectInLoop() {
        Long sum = 0L;
        for (long i = 0L; i < Integer.MAX_VALUE; i++ ) {
            sum += i;       // Long 객체가 2억개 생긴다.
        }
        System.out.println("testObjectInLoop: " + sum);
    }

    @Test
    public void testIntegerInLoop() {
        long sum = 0L;
        for (long i = 0L; i < Integer.MAX_VALUE; i++ ) {
            sum += i;       // Long 객체가 2억개 생긴다.
        }
        System.out.println("testObjectInLoop: " + sum);
    }
}
