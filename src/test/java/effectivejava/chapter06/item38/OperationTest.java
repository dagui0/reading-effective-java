package effectivejava.chapter06.item38;

import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OperationTest {

    @Test
    public void testOperations1() {
        double x = 6.0;
        double y = 3.0;

        assertEquals("6.0 + 3.0 = 9.0\n6.0 - 3.0 = 3.0\n6.0 * 3.0 = 18.0\n6.0 / 3.0 = 2.0\n",
                        testOperation(BasicOperation.class, x, y));
        assertEquals("6.0 ^ 3.0 = 216.0\n6.0 % 3.0 = 0.0\n",
                        testOperation(ExtendedOperation.class, x, y));
    }

    @Test
    public void testOperations2() {
        double x = 6.0;
        double y = 3.0;

        Collection<Operation> allOperations = Stream.concat(
                Stream.of(BasicOperation.values()),
                Stream.of(ExtendedOperation.values())
        ).collect(Collectors.toCollection(ArrayList::new));

        assertEquals("6.0 + 3.0 = 9.0\n6.0 - 3.0 = 3.0\n6.0 * 3.0 = 18.0\n"
                        + "6.0 / 3.0 = 2.0\n6.0 ^ 3.0 = 216.0\n6.0 % 3.0 = 0.0\n",
                testOperation(allOperations, x, y));
    }

    private <T extends Enum<T> & Operation> String testOperation(Class<T> opClass, double x, double y) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        for (Operation op : opClass.getEnumConstants()) {
            pw.printf("%.1f %s %.1f = %.1f\n", x, op, y, op.apply(x, y));
        }
        return sw.toString();
    }

    private String testOperation(Collection<? extends Operation> operations, double x, double y) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        for (Operation op : operations) {
            pw.printf("%.1f %s %.1f = %.1f\n", x, op, y, op.apply(x, y));
        }
        return sw.toString();
    }
}
