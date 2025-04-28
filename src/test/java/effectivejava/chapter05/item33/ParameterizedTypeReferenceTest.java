package effectivejava.chapter05.item33;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParameterizedTypeReferenceTest {

    @Test
    public void testParameterizedTypeReference() {
        // Create a ParameterizedTypeReference for List<String>
        ParameterizedTypeReference<List<String>> stringListType = new ParameterizedTypeReference<>() {};
        // Create another ParameterizedTypeReference for List<String>
        ParameterizedTypeReference<List<String>> stringListType2 = new ParameterizedTypeReference<>() {};
        // Create a ParameterizedTypeReference for List<Integer>
        ParameterizedTypeReference<List<Integer>> integerListType = new ParameterizedTypeReference<>() {};

        // Check if the two string list types are equal
        assertEquals(stringListType, stringListType2);
        // Check if the string list type is not equal to the integer list type
        assertNotEquals(stringListType, integerListType);
    }

    @Test
    public void testCreateInstance() {

        ParameterizedTypeReference<Favorites> favType = new ParameterizedTypeReference<>() {};

        Type type = favType.getType();
        assertInstanceOf(Class.class, type);

        assertDoesNotThrow(() -> {
            Class<?> favClass = (Class<?>) type;
            Constructor<?> constructor = favClass.getConstructor();
            Favorites newInstance = (Favorites) constructor.newInstance();
        });
    }

    @SuppressWarnings("unchecked")
    private <T> T createInstance(ParameterizedTypeReference<? extends T> typeReference) {
        try {
            Class<? extends T> clazz;

            if (typeReference.getType() instanceof Class<?> clazz1) {
                clazz = (Class<? extends T>)clazz1;
            }
            else if (typeReference.getType() instanceof ParameterizedType parameterizedType) {
                Type rawType = parameterizedType.getRawType();
                if (rawType instanceof Class<?> clazz2) {
                    clazz = (Class<? extends T>)clazz2;
                }
                else {
                    throw new IllegalArgumentException("Unsupported type: " + rawType);
                }
            }
            else {
                throw new IllegalArgumentException("Unsupported type: " + typeReference.getType());
            }

            if (clazz.isInterface()) {
                if (clazz == List.class) {
                    clazz = (Class<? extends T>)ArrayList.class;
                }
            }

            Constructor<? extends T> constructor = clazz.getDeclaredConstructor();
            // constructor.setAccessible(true);
            return constructor.newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to create instance", e);
        }
    }

    @Test
    public void testCreateSimpleType() {
        Favorites fav = createInstance(new ParameterizedTypeReference<Favorites>() {});
        assertNotNull(fav);
    }

    @Test
    public void testCreateParameterizedType() {
        List<Favorites> favList = createInstance(new ParameterizedTypeReference<List<Favorites>>() {});
        assertNotNull(favList);
        assertEquals(0, favList.size());
    }
}
