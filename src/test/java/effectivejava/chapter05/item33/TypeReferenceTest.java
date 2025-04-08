package effectivejava.chapter05.item33;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class TypeReferenceTest {

    @Test
    public void testTypeReference() {
        TypeReference<List<String>> stringListType = new TypeReference<>() {};
        TypeReference<List<String>> stringListType2 = new TypeReference<>() {};
        TypeReference<List<Integer>> integerListType = new TypeReference<>() {};

        assertEquals(stringListType, stringListType2);
        assertNotEquals(stringListType, integerListType);
    }

    @Test
    public void testTypeReferenceBasedTHC() {
        Favorites2 f = new Favorites2();

        TypeReference<String> stringType = new TypeReference<>() {};
        TypeReference<Integer> integerType = new TypeReference<>() {};
        TypeReference<Class<?>> classType = new TypeReference<>() {};

        f.put(stringType, "Java");
        f.put(integerType, 0xcafebabe);
        f.put(classType, Favorites.class);

        String favoriteString = f.get(stringType);
        int favoriteInt = f.get(integerType);
        Class<?> favoriteClass = f.get(classType);

        assertEquals("Java cafebabe Favorites",
                "%s %x %s".formatted(favoriteString, favoriteInt, favoriteClass.getSimpleName()));
    }

    @Test
    public void testUsingAnonymousClassSyntax() {
        Favorites2 f = new Favorites2();

        // Scalar 값은 잘 된다.
        f.put(new TypeReference<>() {}, "Java");
        String favoriteString = f.get(new TypeReference<>() {});
        assertEquals("Java", favoriteString);

        // 하지만 와일드카드 타입은 잘 안되는데...
        f.put(new TypeReference<>() {}, Favorites.class);   // 이 때 Class<Favorites>에 대한 TypeReference가 만들어진다.
        Class<?> favoriteClass = f.get(new TypeReference<>() {});
        assertNull(favoriteClass);         // Class<?>에 대한 값은 없는 상태다.

        Class<Favorites> favoriteClass2 = f.get(new TypeReference<>() {});
        assertEquals(Favorites.class, favoriteClass2);   // Class<Favorites>로 하면 찾아진다.

        // 명시적으로 와일드카드 타입 Class<?>를 지정하면 된다.
        f.put(new TypeReference<Class<?>>() {}, Favorites.class);
        Class<?> favoriteClass3 = f.get(new TypeReference<>() {});
        assertEquals(Favorites.class, favoriteClass3);
    }

    @Test
    public void testTypeReferenceInStaticContext() {
        TypeReference<List<String>> stringListType = TypeReference.of();
        TypeReference<List<String>> stringListType2 = new TypeReference<>() {};

        assertNotEquals(stringListType, stringListType2);
        assertEquals("TypeReference{T}", stringListType.toString());
        assertEquals("TypeReference{java.util.List<java.lang.String>}", stringListType2.toString());
    }

    @Test
    public void testGenericType() {

        Favorites3 f = new Favorites3();

        List<String> stringList = List.of("Java", "Spring");
        List<Integer> integerList = List.of(1, 2);

        f.put(new ParameterizedTypeReference<>() {}, stringList);
        f.put(new ParameterizedTypeReference<>() {}, integerList);

        List<String> favoriteStringList = f.get(new ParameterizedTypeReference<>() {});
        List<Integer> favoriteIntList = f.get(new ParameterizedTypeReference<>() {});

        assertSame(stringList, favoriteStringList);
        assertSame(integerList, favoriteIntList);
    }
}
