package com.yidigun.utils;

import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class CompareToBuilderTest {

    @Test
    public void testCompareToBuilder() {
        MemberExample m1 = new MemberExample("Alejandro", 20, "무직");
        MemberExample m3 = new MemberExample("Scully", 20, "프로그래머");

        int result = 0;

        // order by age asc, name asc
        // Alejandro(20세, 무직) < Scully(20세, 프로그래머)
        result = new CompareToBuilder()
                .compare(m1.getAge(), m3.getAge())
                .compare(m1.getName(), m3.getName())
                .toComparison();
        assertTrue(result < 0);

        // order by age asc, job desc
        // Alejandro(20세, 무직) > Scully(20세, 프로그래머)
        result = new CompareToBuilder()
                .compare(m1.getAge(), m3.getAge())
                .compare(m1.getJob(), m3.getJob(), new Comparator<String>() {
                    @Override
                    public int compare(String a, String b) {
                        return a.compareTo(b) * -1;
                    }
                })
                .toComparison();
        assertTrue(result > 0);
    }

    @Test
    public void testSorting() {
        MemberExample[] memberExamples = new MemberExample[] {
                new MemberExample("Alejandro", 20, "무직"),
                new MemberExample("Leeturn", 30, "프로그래머"),
                new MemberExample("Scully", 20, "프로그래머"),
                new MemberExample("Lucie", 30, "프로그래머")
        };

        Arrays.sort(memberExamples);

        assertEquals("Alejandro", memberExamples[0].getName());
        assertEquals("Leeturn", memberExamples[1].getName());
        assertEquals("Lucie", memberExamples[2].getName());
        assertEquals("Scully", memberExamples[3].getName());


        Arrays.sort(memberExamples, new Comparator<MemberExample>() {
            @Override
            public int compare(MemberExample o1, MemberExample o2) {
                return new CompareToBuilder()
                        .compare(o1.getAge(), o2.getAge())
                        .compare(o1.getName(), o2.getName())
                        .toComparison();
            }
        });

        assertEquals("Alejandro", memberExamples[0].getName());
        assertEquals("Scully", memberExamples[1].getName());
        assertEquals("Leeturn", memberExamples[2].getName());
        assertEquals("Lucie", memberExamples[3].getName());
    }
}


class MemberExample implements Cloneable, Serializable, Comparable<MemberExample> {
    private String name;
    private int age;
    private String job;

    public MemberExample(String name, int age, String job) { this.name = name; this.age = age; this.job = job; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MemberExample t))
            return false;
        return (Objects.equals(name, t.name)) &&
                age == t.age &&
                (Objects.equals(job, t.job));
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .append(age)
                .append(job)
                .toHashCode();
    }

    @Override
    public String toString() {
        return name + "(" + age + "세," + job + ")";
    }

    @Override
    public int compareTo(MemberExample o) {
        return new CompareToBuilder()
                .compare(name, o.name)
                .compare(age, o.age)
                .compare(job, o.job)
                .toComparison();
    }
}
