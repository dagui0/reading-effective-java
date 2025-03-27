package effectivejava.chapter05.item29;

public class Stack2<T> {

    private final T[] elements;
    private int size;

    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public Stack2(int capacity) {
        elements = (T[])new Object[capacity];
        size = 0;
    }
    public Stack2() {
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
        T result = elements[--size];
        elements[size] = null; // Prevent memory leak
        return result;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
