package effectivejava.chapter05.item33;

import org.springframework.core.ParameterizedTypeReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Favorites3 {

    private final Map<ParameterizedTypeReference<?>, Object> favorites = new HashMap<>();

    public <T> void put(ParameterizedTypeReference<T> type, T value) {
        favorites.put(Objects.requireNonNull(type), value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(ParameterizedTypeReference<T> type) {
        return (T)favorites.get(type);
    }
}
