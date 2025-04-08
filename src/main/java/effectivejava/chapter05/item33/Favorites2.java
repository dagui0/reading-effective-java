package effectivejava.chapter05.item33;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Favorites2 {

    private final Map<TypeReference<?>, Object> favorites = new HashMap<>();

    public <T> void put(TypeReference<T> type, T value) {
        favorites.put(Objects.requireNonNull(type), value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(TypeReference<T> type) {
        return (T)favorites.get(type);
    }
}
