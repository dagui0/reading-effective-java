package effectivejava.chapter05.item31;

import effectivejava.chapter05.item30.GenericUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class BoundedWildcardTest {

    @Test
    public void testBoundedWildcard() {

        List<ScheduledFuture<?>> list1 = new ArrayList<>();
        list1.add(new MyScheduledFuture<>());

        // ScheduledFuture<?> maxValue = GenericUtils.max(list1); // compile error
        ScheduledFuture<?> maxValue = GenericUtils2.max(list1);
    }
}

class MyScheduledFuture<V> implements ScheduledFuture<V> {

    /*
     * implements java.lang.Comparable<T>
     */

    @Override
    public int compareTo(@NotNull Delayed o) {
        return 0;
    }

    /*
     * implements java.util.concurrent.Delayed<V>
     */


    @Override
    public long getDelay(@NotNull TimeUnit unit) {
        return 0;
    }

    /*
     * implements java.util.concurrent.Future<V>
     */

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public V get(long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
