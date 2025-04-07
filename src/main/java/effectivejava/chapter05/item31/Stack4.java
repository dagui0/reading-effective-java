package effectivejava.chapter05.item31;

import effectivejava.chapter05.item29.StackOverflowException;

import java.util.Collection;

public class Stack4<T> {

    private final Object[] elements;
    private int size;

    private static final int DEFAULT_CAPACITY = 16;

    public Stack4(int capacity) {
        elements = new Object[capacity];
        size = 0;
    }
    public Stack4() {
        this(DEFAULT_CAPACITY);
    }

    public void push(T e) {
        if (size == elements.length)
            throw new StackOverflowException("Stack is full: " + size);
        elements[size++] = e;
    }

    public T pop() {
        if (size == 0) {
            throw new IllegalStateException("Stack is empty");
        }
        @SuppressWarnings("unchecked")
        T result = (T)elements[--size];
        elements[size] = null; // Prevent memory leak
        return result;
    }

    public boolean isEmpty() {
        return size == 0;
    }


    public void pushAll(Iterable<? extends T> src) {
        for (T e : src) {
            push(e);
        }
    }

    public void popAll(Collection<? super T> dst) {
        while (!isEmpty()) {
            dst.add(pop());
        }
    }
}
