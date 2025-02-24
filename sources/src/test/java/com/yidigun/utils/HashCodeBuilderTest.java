package com.yidigun.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HashCodeBuilderTest {

    @Test
    public void testCheckingWorked() {

        Test1 t1 = new Test1(1, 2);

        int actual = t1.hashCode();

        int expected = 17;
        expected = 31 * expected + t1.a;
        expected = 31 * expected + t1.b;

        assertEquals(expected, actual);
    }

    @Test
    public void testCheckingEquality() {

        Test1 t1 = new Test1(1, 2);
        Test1 t2 = new Test1(1, 2);

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }
}

class Test1 {
    int a = 0, b = 0;

    public Test1() {}

    public Test1(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Test1))
            return false;
        Test1 t = (Test1)o;
        return t.a == a && t.b == b;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(a)
                .append(b)
                .toHashCode();
    }
}