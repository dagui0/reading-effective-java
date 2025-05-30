package effectivejava.chapter07.item47;

import java.util.*;

public class PowerSet<E> extends AbstractList<Set<E>> {

    private final List<E> src;

    public PowerSet(List<E> src) {
        if (src.size() > 30) {
            throw new IllegalArgumentException("Set too large: " + src.size());
        }
        this.src = new ArrayList<>(src);
    }

    @Override
    public int size() {
        return 1 << src.size();
    }

    @SuppressWarnings({"SlowListContainsAll", "unchecked"})
    @Override
    public boolean contains(Object o) {
        return o instanceof Set &&
                src.containsAll((Set<E>) o);
    }

    @Override
    public Set<E> get(int index) {
        Set<E> result = new HashSet<>();
        for (int i = 0; index != 0; i++, index >>= 1) {
            if ((index & (1 << i)) != 0) {
                result.add(src.get(i));
            }
        }
        return result;
    }
}
