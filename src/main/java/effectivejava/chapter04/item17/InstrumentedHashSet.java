package effectivejava.chapter04.item17;

import java.io.Serial;
import java.util.Collection;
import java.util.HashSet;

public class InstrumentedHashSet<E> extends HashSet<E> {

    @Serial
    private static final long serialVersionUID = 6315764044396280345L;

    private int addCount = 0;
    public InstrumentedHashSet(int capacity) {
        super(capacity);
    }
    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }
    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);     // 알고 보면 내부적으로 add()를 사용하므로 문제가 된다.
    }
    public int getAddCount() { return addCount; }
}
