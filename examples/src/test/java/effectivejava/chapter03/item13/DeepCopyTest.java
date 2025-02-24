package effectivejava.chapter03.item13;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class DeepCopyTest {

    @Test
    public void testCloneObjectArray() {
        Member[] members = new Member[] {
                new Member("Alejandro"),
                new Member("Leeturn"),
                new Member("Scully"),
                new Member("Lucie")
        };

        Member[] cloned = members.clone();
        cloned[0].setName("Gonzalez");

        // shallow copy
        assertEquals(members[0].getName(), cloned[0].getName());
        assertEquals("Gonzalez", members[0].getName());
        assertEquals("Gonzalez", cloned[0].getName());
    }

    @Test
    public void testCopyOfObjectArray() {
        Member[] members = new Member[] {
                new Member("Alejandro"),
                new Member("Leeturn"),
                new Member("Scully"),
                new Member("Lucie")
        };

        Member[] copied = Arrays.copyOf(members, members.length);
        copied[0] = new Member("Gonzalez");

        // deep copy
        assertNotEquals(members[0].getName(), copied[0].getName());
        assertEquals("Alejandro", members[0].getName());
        assertEquals("Gonzalez", copied[0].getName());
    }

    @Test
    public void testCloneArrayList() {
        ArrayList<Member> members = new ArrayList<>();
        members.add(new Member("Alejandro"));
        members.add(new Member("Leeturn"));
        members.add(new Member("Scully"));
        members.add(new Member("Lucie"));

        @SuppressWarnings("unchecked")
        ArrayList<Member> cloned = (ArrayList<Member>)members.clone();

        cloned.get(0).setName("Gonzalez");

        // shallow copy
        assertEquals(members.get(0).getName(), cloned.get(0).getName());
        assertEquals("Gonzalez", members.get(0).getName());
        assertEquals("Gonzalez", cloned.get(0).getName());
    }

    @Test
    public void testDeepCopyArrayList() {
        ArrayList<Member> members = new ArrayList<>();
        members.add(new Member("Alejandro"));
        members.add(new Member("Leeturn"));
        members.add(new Member("Scully"));
        members.add(new Member("Lucie"));

        ArrayList<Member> newList = new ArrayList<>();
        for (Member member : members) {
            newList.add(member.clone());
        }
        newList.get(0).setName("Gonzalez");

        // deep copy
        assertNotEquals(members.get(0).getName(), newList.get(0).getName());
        assertEquals("Alejandro", members.get(0).getName());
        assertEquals("Gonzalez", newList.get(0).getName());
    }

    /**
     * 이 문제를 해결할 수 있을까?
     */
    @Test
    public void testDeepCopyArrayListHavingDuplicatedElements() {

        // 원본 리스트는 같은 객체가 2번 추가 되어 있음
        ArrayList<Member> members = new ArrayList<>();
        members.add(new Member("Alejandro"));
        members.add(members.get(0));

        // 복제된 리스트는 복제되면서 다른 객체가 2번 추가됨
        ArrayList<Member> newList = new ArrayList<>();
        for (Member member : members) {
            newList.add(member.clone());
        }

        // 원본 리스트와 카피본 리스트의 원소는 다른 객체
        assertNotSame(members.get(0), newList.get(0));
        assertNotSame(members.get(1), newList.get(1));

        members.get(1).setName("Gonzalez");
        newList.get(1).setName("Gonzalez");

        // 원본: 같은 객체
        assertEquals(members.get(0).getName(), members.get(1).getName());
        assertSame(members.get(0).getName(), members.get(1).getName());
        assertEquals("Gonzalez", members.get(0).getName());
        assertEquals("Gonzalez", members.get(1).getName());

        // 카피본: 다른 객체
        assertNotEquals(newList.get(0).getName(), newList.get(1).getName());
        assertNotSame(newList.get(0).getName(), newList.get(1).getName());
        assertEquals("Alejandro", newList.get(0).getName());
        assertEquals("Gonzalez", newList.get(1).getName());
    }

    /**
     * Creates a deep copy of the given Serializable object by utilizing
     * serialization and deserialization mechanisms. This method serializes
     * the provided object into a byte stream and deserializes it back into a
     * new object, effectively creating a deep copy.
     *
     * {@link T} and all the members and elements objects of {@link T} must
     * implements {@link Serializable} interface.
     *
     * @param <T> The type of object being copied, which must implement the
     *            Serializable interface.
     * @param o   The object to be deeply copied. This object should implement
     *            Serializable, and all of its non-transient fields should also
     *            be serializable.
     * @return A deep copy of the input object.
     */
    private <T extends Serializable> T deepCopyUsingSerialize(T o) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(o);
            out.close();

            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));

            @SuppressWarnings("unchecked")
            T copy = (T) in.readObject();
            in.close();

            return copy;
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void testDeepCopyArrayListUsingSerialize() throws IOException, ClassNotFoundException {

        // 원본 리스트는 같은 객체가 2번 추가 되어 있음
        ArrayList<Member> members = new ArrayList<>();
        members.add(new Member("Alejandro"));
        members.add(members.get(0));

        // 시리얼라이즈를 이용하여 복제 수행
        ArrayList<Member> newList = deepCopyUsingSerialize(members);

        // 원본 리스트와 카피본 리스트의 원소는 다른 객체
        assertNotSame(members.get(0), newList.get(0));
        assertNotSame(members.get(1), newList.get(1));

        members.get(1).setName("Gonzalez");
        newList.get(1).setName("Gonzalez");

        // 원본: 같은 객체
        assertEquals(members.get(0).getName(), members.get(1).getName());
        assertSame(members.get(0).getName(), members.get(1).getName());
        assertEquals("Gonzalez", members.get(0).getName());
        assertEquals("Gonzalez", members.get(1).getName());

        // 카피본: 같은 객체
        assertEquals(newList.get(0).getName(), newList.get(1).getName());
        assertSame(newList.get(0).getName(), newList.get(1).getName());
        assertEquals("Gonzalez", newList.get(0).getName());
        assertEquals("Gonzalez", newList.get(1).getName());
    }
}

class Member implements Cloneable, Serializable {
    private String name;

    public Member(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Member clone() {
        try {
            return (Member)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Member t))
            return false;
        return t.name.equals(name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Test1{\"" + name + "\"}";
    }
}
