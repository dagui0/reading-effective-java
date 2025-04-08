package effectivejava.chapter05.item33;

import java.lang.reflect.*;
import java.util.Objects;

public abstract class TypeReference<T> {
    private final Type type;
    private volatile Constructor<T> constructor;

    protected TypeReference() {
        Type superType = getClass().getGenericSuperclass();
        if (superType instanceof ParameterizedType parameterizedType)
            this.type = parameterizedType.getActualTypeArguments()[0];
        else
            throw new AssertionError("What the hell is this?");
    }

    static <T> TypeReference<T> of() {
        return new TypeReference<>() {};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TypeReference<?> that))
            return false;
        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return "TypeReference{" + type.getTypeName() + "}";
    }
}
