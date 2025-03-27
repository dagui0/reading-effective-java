package effectivejava.chapter05.item28;

import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CovariantTest {

    @SuppressWarnings("MismatchedReadAndWriteOfArray")
    @Test
    public void testArrayCovariantAssignment() {

        // 하위 타입 배열로 상위 타입 참조에 대입
        Object[] objArray = new Long[10];

        assertThrows(ArrayStoreException.class, () -> {
            //noinspection DataFlowIssue
            objArray[0] = "Hello";
        });
    }

    @SuppressWarnings({"TypeParameterExplicitlyExtendsObject", "unused", "CommentedOutCode"})
    @Test
    public void testListCovariantAssignment() {

        // List<Object> objList = new ArrayList<Long>(10); // compile error

        List<? extends Object> objList = new ArrayList<Long>();
        // objList.add("Hello"); // compile error
        // objList.add(tryConvertToParameteredType("Hello")); // compile error

        assertTrue(true);
    }

    @SuppressWarnings({"TypeParameterExplicitlyExtendsObject", "unused"})
    private static <T extends Object> T tryConvertToParameteredType(T value) {
        return value;
    }

    private final Sub[] SUB_ARRAY = new Sub[] {
            new Sub("Alejandro", 18),
            new Sub("Scully", 17),
            new Sub("Leeturn", 16),
            new Sub("Lucie", 15),
    };

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Test
    public void testArrayCovariant() {

        // Covariant array assignment
        Super[] superArray = SUB_ARRAY;

        assertEquals("Sub(Alejandro, 18)", superArray[0].toString());
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Test
    public void testListCovariant() {
        List<Sub> subList = List.of(SUB_ARRAY);

        //List<Super> superList = subList;    // compile error

        List<? extends Super> wildcardList = subList;

        assertEquals("Sub(Alejandro, 18)", wildcardList.getFirst().toString());
    }
}

@SuppressWarnings("LombokGetterMayBeUsed")
class Super {

    @Getter
    private final String name;

    public Super(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Super(" + name + ")";
    }
}

@SuppressWarnings("LombokGetterMayBeUsed")
class Sub extends Super {

    @Getter
    private final int age;

    public Sub(String name, int age) {
        super(name);
        this.age = age;
    }

    @Override
    public String toString() {
        return "Sub(" + getName() + ", " + age + ")";
    }
}
