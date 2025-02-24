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
        Member m1 = new Member("Alejandro", 20, "무직");
        Member m3 = new Member("Scully", 20, "프로그래머");

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
        Member[] members = new Member[] {
                new Member("Alejandro", 20, "무직"),
                new Member("Leeturn", 30, "프로그래머"),
                new Member("Scully", 20, "프로그래머"),
                new Member("Lucie", 30, "프로그래머")
        };

        Arrays.sort(members);

        assertEquals("Alejandro", members[0].getName());
        assertEquals("Leeturn", members[1].getName());
        assertEquals("Lucie", members[2].getName());
        assertEquals("Scully", members[3].getName());


        Arrays.sort(members, new Comparator<Member>() {
            @Override
            public int compare(Member o1, Member o2) {
                return new CompareToBuilder()
                        .compare(o1.getAge(), o2.getAge())
                        .compare(o1.getName(), o2.getName())
                        .toComparison();
            }
        });

        assertEquals("Alejandro", members[0].getName());
        assertEquals("Scully", members[1].getName());
        assertEquals("Leeturn", members[2].getName());
        assertEquals("Lucie", members[3].getName());
    }
}


class Member implements Cloneable, Serializable, Comparable<Member> {
    private String name;
    private int age;
    private String job;

    public Member(String name, int age, String job) { this.name = name; this.age = age; this.job = job; }
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
        if (!(o instanceof Member t))
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
    public int compareTo(Member o) {
        return new CompareToBuilder()
                .compare(name, o.name)
                .compare(age, o.age)
                .compare(job, o.job)
                .toComparison();
    }
}
