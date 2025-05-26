package lambdaspecial.java;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FactorialTest {

    @Test
    public void testSimpleExecution() {
        assertEquals(120L, Factorial.factorial(5));
    }

    @Disabled
    @Test
    public void testCheckStackTrace() {
        try {
            Factorial.factorialDebug(5);
        }
        catch (RuntimeException e) {
            System.out.println("Stack trace for testCheckStackTrace:");
            e.printStackTrace();
        }
    }

    static class ValueHolder {
        long value = 0L;
    }

    @Test
    public void testVirtualThreadExecution() {
        ValueHolder holder = new ValueHolder();

        Thread virtualThread = Thread.ofVirtual().start(() -> {
            holder.value = Factorial.factorial(5);
        });
        try {
            virtualThread.join();
        }
        catch (InterruptedException e) {
            fail("Virtual thread was interrupted");
        }

        assertEquals(120L, holder.value);
    }

    @Disabled
    @Test
    public void testCheckVirtualThreadStackTrace() {
        ValueHolder holder = new ValueHolder();

        Thread virtualThread = Thread.ofVirtual().start(() -> {
            try {
                holder.value = Factorial.factorialDebug(5);
            }
            catch (RuntimeException e) {
                System.out.println("Stack trace for testCheckVirtualThreadStackTrace:");
                e.printStackTrace();
            }
        });

        try {
            virtualThread.join();
        }
        catch (InterruptedException e) {
            fail("Virtual thread was interrupted");
        }
    }
}
