package com.yidigun.utils;

public interface SimpleLinkedList {

    public void add(int value);
    public void addAll(int... values);
    public void insertAt(int index, int value);
    public void removeAt(int index);
    public void removeAll();

    public int size();
    public int get(int index);
    public boolean contains(int value);

    public void dispose();
    public String dump();
    public void printDump();
}
