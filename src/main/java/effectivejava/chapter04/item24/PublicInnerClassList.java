package effectivejava.chapter04.item24;

import java.util.Iterator;

public class PublicInnerClassList<E> implements Iterable<E> {

    private E[] elements;
    private int currentSize;

    @SuppressWarnings("unchecked")
    public PublicInnerClassList(int size) {
        elements = (E[]) new Object[size];
        this.currentSize = 0;
    }

    public int size() { return currentSize;}

    public E get(int index) {
        if (index < 0 || index >= currentSize) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + currentSize);
        }
        return elements[index];
    }

    public boolean add(E e) {
        if (currentSize == elements.length) {
            expandCapacity();
        }
        elements[currentSize++] = e;
        return true;
    }

    @SuppressWarnings("unchecked")
    private void expandCapacity() {
        int newCapacity = elements.length * 2;
        E[] newElements = (E[]) new Object[newCapacity];
        System.arraycopy(elements, 0, newElements, 0, currentSize);
        elements = newElements;
    }

    public class MyIterator implements Iterator<E> {
        private int index;
        public MyIterator() {
            this(0);
        }
        public MyIterator(int index) {
            this.index = index;
        }
        public boolean hasNext() { return index < size(); }
        public E next() {
            return get(index++);
        }
    }

    @Override
//    @javax.annotation.Nonnull
//    @lombok.NonNull
//    @org.jetbrains.annotations.NotNull
//    @org.eclipse.jdt.annotation.NonNull
//    @org.springframework.lang.NonNull
//    @org.checkerframework.checker.nullness.qual.NonNull
//    @javax.validation.constraints.NotNull
    public Iterator<E> iterator() {
        return new MyIterator();
    }
}
