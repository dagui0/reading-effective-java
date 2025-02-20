package com.yidigun.utils;

import java.util.Comparator;

public class CompareToBuilder {
    private int result;

    public CompareToBuilder() {
        this.result = 0;
    }

    public int toComparison() {
        return result;
    }

    public <T extends Comparable<T>> CompareToBuilder compare(T a, T b) {
        if (result == 0) {
            int r = a.compareTo(b);
            if (r != 0)
                result = r;
        }
        return this;
    }

    public <T> CompareToBuilder compare(T a, T b, Comparator<T> c) {
        if (result == 0) {
            int r = c.compare(a, b);
            if (r != 0)
                result = r;
        }
        return this;
    }

    public CompareToBuilder compare(boolean a, boolean b) {
        if (result == 0) {
            if (a && !b)
                result = 1;
            else if (!a && b)
                result = -1;
        }
        return this;
    }

    public CompareToBuilder compare(byte a, byte b) {
        if (result == 0) {
            if (a > b)
                result = 1;
            else if (a < b)
                result = -1;
        }
        return this;
    }

    public CompareToBuilder compare(char a, char b) {
        if (result == 0) {
            if (a > b)
                result = 1;
            else if (a < b)
                result = -1;
        }
        return this;
    }

    public CompareToBuilder compare(short a, short b) {
        if (result == 0) {
            if (a > b)
                result = 1;
            else if (a < b)
                result = -1;
        }
        return this;
    }

    public CompareToBuilder compare(int a, int b) {
        if (result == 0) {
            if (a > b)
                result = 1;
            else if (a < b)
                result = -1;
        }
        return this;
    }

    public CompareToBuilder compare(long a, long b) {
        if (result == 0) {
            if (a > b)
                result = 1;
            else if (a < b)
                result = -1;
        }
        return this;
    }

    public CompareToBuilder compare(float a, float b) {
        if (result == 0) {
            int r = Float.compare(a, b);
            if (r != 0)
                result = r;
        }
        return this;
    }

    public CompareToBuilder compare(double a, double b) {
        if (result == 0) {
            int r = Double.compare(a, b);
            if (r != 0)
                result = r;
        }
        return this;
    }

}
