package effectivejava.chapter05.item30;

import java.util.Collection;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class GenericSingleton {

    private static final UnaryOperator<Object> IDENTITY = (t) -> t;

    @SuppressWarnings("unchecked")
    public static <T> UnaryOperator<T> identity() {
        return (UnaryOperator<T>) IDENTITY;
    }
}
