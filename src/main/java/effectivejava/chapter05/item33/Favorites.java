package effectivejava.chapter05.item33;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Favorites implements TypeSafeHeterogeneousContainer {

    private final Map<Class<?>, Object> favorites = new HashMap<>();

    @Override
    public <T> void put(Class<T> type, T value) {
        favorites.put(Objects.requireNonNull(type), type.cast(value));
    }

    @Override
    public <T> T get(Class<T> type) {
        return type.cast(favorites.get(type));
    }
}
