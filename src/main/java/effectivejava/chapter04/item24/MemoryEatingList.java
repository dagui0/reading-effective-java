package effectivejava.chapter04.item24;

import lombok.NonNull;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

public class MemoryEatingList<E> extends AbstractList<E> {

    private static final int DEFAULT_INITIAL_CAPACITY = 10;

    private E[] elements;
    private int size;
    private final List<Iterator<E>> iterators;

    public MemoryEatingList() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public MemoryEatingList(int initialCapacity) {
        elements = (E[]) new Object[initialCapacity];
        size = 0;
        iterators = new MemoryEatingList<>();
    }

    private void rangeCheck(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    @SuppressWarnings("unchecked")
    private void expandCapacity() {
        int newCapacity = elements.length * 2;
        E[] newElements = (E[]) new Object[newCapacity];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    @Override
    public E get(int index) {
        rangeCheck(index);
        return elements[index];
    }

    @Override
    public boolean add(E e) {
        if (size == elements.length)
            expandCapacity();

        elements[size++] = e;
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    @NonNull
    public Iterator<E> iterator() {
        Iterator<E> i = new Iterator<E>() {
            private static String author;
            public static String getAuthor() {
                return author;
            }
            public static void setAuthor(String author1) {
                author = author1;
            }

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public E next() {
                setAuthor("1");
                if (!hasNext())
                    throw new java.util.NoSuchElementException();
                return elements[currentIndex++];
            }
        };
        iterators.add(i);
        // MemoryEatingList$1.setAuthor("1");
        return i;
    }
}
