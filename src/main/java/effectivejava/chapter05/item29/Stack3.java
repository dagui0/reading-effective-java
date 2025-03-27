package effectivejava.chapter05.item29;

public class Stack3<T> {

    private final Object[] elements;
    private int size;

    private static final int DEFAULT_CAPACITY = 16;

    public Stack3(int capacity) {
        elements = new Object[capacity];
        size = 0;
    }
    public Stack3() {
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
}
