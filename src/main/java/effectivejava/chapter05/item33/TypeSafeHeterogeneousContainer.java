package effectivejava.chapter05.item33;

public interface TypeSafeHeterogeneousContainer {
    <T> T get(Class<T> type);
    <T> void put(Class<T> type, T value);
}
