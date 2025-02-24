package com.yidigun.utils;

public class JniSimpleLinkedList implements SimpleLinkedList {

    private long pointer;

    public JniSimpleLinkedList() {
        pointer = native_create();
    }
    private native long native_create();

    /**
     * finalizer is deprecated from java 9.
     *
     * protected void finalize() {
     *     dispose();
     * }
     */
    @Override
    public void dispose() {
        if (pointer != 0L)
            native_delete(pointer);
        pointer = 0L;
    }
    private native void native_delete(long pointer);

    @Override
    public void add(int value) {
        if (pointer == 0L)
            throw new IllegalStateException();
        native_add(pointer, value);
    }
    private native void native_add(long pointer, int value);

    @Override
    public void addAll(int... values) {
        if (pointer == 0L)
            throw new IllegalStateException();
        native_addAll(pointer, values);
    }
    private native void native_addAll(long pointer, int[] values);

    @Override
    public void insertAt(int index, int value) {
        if (pointer == 0L)
            throw new IllegalStateException();
        native_insertAt(pointer, index, value);
    }
    private native void native_insertAt(long pointer, int index, int value);

    @Override
    public void removeAt(int index) {
        if (pointer == 0L)
            throw new IllegalStateException();
        native_removeAt(pointer, index);
    }
    private native void native_removeAt(long pointer, int index);

    @Override
    public void removeAll() {
        if (pointer == 0L)
            throw new IllegalStateException();
        native_removeAll(pointer);
    }
    private native void native_removeAll(long pointer);

    @Override
    public int size() {
        if (pointer == 0L)
            throw new IllegalStateException();
        return native_size(pointer);
    }
    private native int native_size(long pointer);

    @Override
    public int get(int index) {
        if (pointer == 0L)
            throw new IllegalStateException();
        return native_get(pointer, index);
    }
    private native int native_get(long pointer, int index);

    @Override
    public boolean contains(int value) {
        if (pointer == 0L)
            throw new IllegalStateException();
        return native_contains(pointer, value);
    }
    private native boolean native_contains(long pointer, int value);

    @Override
    public String dump() {
        if (pointer == 0L)
            throw new IllegalStateException();
        return native_dump(pointer);
    }
    private native String native_dump(long pointer);

    @Override
    public void printDump() {
        System.out
            .append("SimpleLinkedList(")
            .append(String.valueOf(size()))
            .append("): ")
            .append(dump())
            .println();
    }
}
