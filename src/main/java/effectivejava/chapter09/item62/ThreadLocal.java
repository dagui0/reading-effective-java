package effectivejava.chapter09.item62;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadLocal<T> {

    private static final Map<Thread, Map<ThreadLocal<?>, Object>> values = new ConcurrentHashMap<>();

    public ThreadLocal() {}

    public void set(T value) {
        Thread currentThread = Thread.currentThread();
        values.computeIfAbsent(currentThread, k -> new ConcurrentHashMap<>())
             .put(this, value);
    }

    @SuppressWarnings("unchecked")
    public T get() {
        Thread currentThread = Thread.currentThread();
        Map<ThreadLocal<?>, Object> threadValues = values.get(currentThread);
        if (threadValues != null) {
            return (T) threadValues.get(this);
        }
        return null; // or throw an exception if preferred
    }
}
