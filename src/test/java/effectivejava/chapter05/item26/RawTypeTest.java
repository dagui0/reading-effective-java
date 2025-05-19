package effectivejava.chapter05.item26;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RawTypeTest {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void testRawTypeConverting() {

        List rawList = new ArrayList();
        rawList.add("Hello");
        rawList.add(1234);

        assertEquals(2, getSizeOfStringList(rawList));
        assertThrows(ClassCastException.class, () -> {
            @SuppressWarnings("unchecked")
            String str = getStringElementFromStringList(rawList, 1);
            System.out.println(str);
        });

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        List<Object> objectList = new ArrayList<>();
        objectList.add("Hello");
        objectList.add(1234);

        // Compile error
        // assertEquals(2, getSizeOfStringList(objectList));

        @SuppressWarnings("unused")
        List<?> wildcardList = objectList;

        // Compile error
        // wildcardList.add("World");
    }

    private static int getSizeOfStringList(List<String> list) {
        return list.size();
    }

    private static String getStringElementFromStringList(List<String> list,
                                                         int index) {
        return list.get(index);
    }

    @Test
    public void testRawTypeCasting() {
        List<String> stringList = new ArrayList<>();
        stringList.add("Hello");
        stringList.add("World");

        List<String> castedList = checkTypeAndCast(stringList);

        assertEquals(2, castedList.size());
    }

    private static List<String> checkTypeAndCast(Object o) {
        if (o instanceof List<?> list) {
            if (!list.isEmpty() && list.getFirst() instanceof String) {
                // 요소를 꺼내서 검사까지 했으므로 절대적으로 완전 킹갓 안전한 형 변환임
                @SuppressWarnings("unchecked")
                List<String> stringList = (List<String>) list;
                return stringList;
            }
        }
        throw new ClassCastException("Invalid type");
    }
}
