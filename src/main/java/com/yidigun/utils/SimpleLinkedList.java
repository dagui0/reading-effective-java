package com.yidigun.utils;

public interface SimpleLinkedList extends AutoCloseable {

    public static SimpleLinkedList newInstance() {
        return new JniSlist();
    }

    public void add(int value);
    public void addAll(int... values);
    public void insertAt(int index, int value);
    public void removeAt(int index);
    public void removeAll();

    public int size();
    public int get(int index);
    public boolean contains(int value);

    @Override
    public void close();
}
