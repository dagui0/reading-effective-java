package effectivejava.chapter03.item13;

import java.util.Arrays;
import java.util.EmptyStackException;

public class CloneableStack<T extends RealyCloneable> implements RealyCloneable {
    private T[] elements;
    private int size;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public CloneableStack() {
        elements = (T[])new RealyCloneable[DEFAULT_INITIAL_CAPACITY];
    }

    public CloneableStack<T> push(T e) {
        ensureCapacity();
        elements[size++] = e;
        return this;
    }

    public T pop() {
        if (size == 0)
            throw new EmptyStackException();
        return elements[--size];
    }

    /**
     * 적어도 하나 이상의 원소를 담을 공간을 보장한다.
     * 배열의 길이를 늘려야 할 때마다 대략 두배가 늘어난다.
     */
    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public CloneableStack<T> clone() {
        try {
            CloneableStack<T> result = (CloneableStack<T>) super.clone();
            result.elements = (T[])new RealyCloneable[elements.length];
            for (int i = 0; i < size; i++) {
                result.elements[i] = (T)elements[i].clone();
            }
            return result;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
