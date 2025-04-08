package effectivejava.chapter05.item33;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TypeSafeHeterogeneousContainerTest {

    @Test
    public void testFavoriteClass() {
        Favorites f = new Favorites();

        f.put(String.class, "Java");
        f.put(Integer.class, 0xcafebabe);
        f.put(Class.class, Favorites.class);

        String favoriteString = f.get(String.class);
        int favoriteInt = f.get(Integer.class);
        Class<?> favoriteClass = f.get(Class.class);

        assertEquals("Java cafebabe Favorites",
            "%s %x %s".formatted(favoriteString, favoriteInt, favoriteClass.getSimpleName()));
    }
}
