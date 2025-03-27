package effectivejava.chapter05.item29;

public class Stack1 {

    private final Object[] elements;
    private int size;

    private static final int DEFAULT_CAPACITY = 16;

    public Stack1(int capacity) {
        elements = new Object[capacity];
        size = 0;
    }
    public Stack1() {
        this(DEFAULT_CAPACITY);
    }

    public void push(Object e) {
        if (size == elements.length)
            throw new StackOverflowException("Stack is full: " + size);
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0) {
            throw new IllegalStateException("Stack is empty");
        }
        Object result = elements[--size];
        elements[size] = null; // Prevent memory leak
        return result;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
