package effectivejava.chapter05.item32;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

public class GenericVarargsTest {

    @Test
    public void testVarargsHeapPollution() {

        @SuppressWarnings("unchecked")
        List<String>[] stringLists = new List[1];

        assertThrows(ClassCastException.class, () -> {
            dangerous(stringLists);
        });
    }

    @SafeVarargs
    private static void dangerous(List<String>... stringLists) {
        List<Integer> intList = List.of(42);

        @SuppressWarnings("UnnecessaryLocalVariable")
        Object[] objects = stringLists;

        objects[0] = intList; // heap pollution, unchecked warning
        String s = stringLists[0].getFirst(); // ClassCastException at runtime

        System.out.println(s);
    }

    @Test
    public void testVarargsHeapPollution2() {

        @SuppressWarnings("unchecked")
        List<String>[] stringLists = new List[1];

        @SuppressWarnings("UnnecessaryLocalVariable")
        Object[] objects = stringLists;

        List<Integer> intList = List.of(42);
        objects[0] = intList; // heap pollution, unchecked warning

        assertThrows(ClassCastException.class, () -> {
            //noinspection SequencedCollectionMethodCanBeUsed
            stringLists[0].get(0).getClass();
        });
    }

    private static void finalArgsTest(final int a, int b) {
        // a = 1;  // compile error
        b = 2;
    }

    private static void finalVarargsTest(final int... a) {
        // a = new int[1]; // compile error
        a[0] = 2;   // no error
    }

    @Test
    public void testPickTwo() {

        assertThrows(ClassCastException.class, () -> {
            String[] attributes = pickTwo("예쁜", "귀여운", "아름다운");
            System.out.println(String.join(", ", attributes) + " 스컬리님");
        });
    }

    @SuppressWarnings({"SameParameterValue", "unchecked", "RedundantSuppression"})
    private static <T> T[] pickTwo(T a, T b, T c) {
        return switch (ThreadLocalRandom.current().nextInt(3)) {
            case 0 -> toArray(a, b);
            case 1 -> toArray(b, c);
            case 2 -> toArray(c, a);
            default -> throw new AssertionError();
        };
    }

    @SafeVarargs
    private static <T> T[] toArray(T... args) {
        return args;
    }


    @Test
    public void testPickTwoSafe() {

        List<String> attributes = pickTwoSafe("예쁜", "귀여운", "아름다운");

        String s = String.join(", ", attributes) + " 스컬리님";

        assertTrue(s.matches("^(:?예쁜|귀여운|아름다운), (:?예쁜|귀여운|아름다운) 스컬리님$"));
    }

    private static <T> List<T> pickTwoSafe(T a, T b, T c) {
        return switch (ThreadLocalRandom.current().nextInt(3)) {
            case 0 -> List.of(a, b);
            case 1 -> List.of(b, c);
            case 2 -> List.of(c, a);
            default -> throw new AssertionError();
        };
    }

    public static enum AppearanceType {
        PRETTY, CUTE, BEAUTIFUL
    }

    public static String getAppearanceTypeName(AppearanceType type) {
        return switch (type) {
            case PRETTY -> "예쁜";
            case CUTE -> "귀여운";
            case BEAUTIFUL -> "아름다운";
        };
    }
}

