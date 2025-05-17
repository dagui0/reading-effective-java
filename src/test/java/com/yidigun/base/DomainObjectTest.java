package com.yidigun.base;

import com.yidigun.base.examples.*;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DomainObjectTest {

    @Test
    public void testEquals() {

        Instant now = Instant.now();
        ComplexKeyExample original = ComplexKeyExample.builder()
                .keyPart1(1)
                .keyPart2("A")
                .otherField1("field1")
                .otherField2("field2")
                .createDate(now)
                .updateDate(now)
                .build();

        assertDoesNotThrow(() -> {
            Thread.sleep(100); // 0.1초 대기
        });

        Instant now2 = Instant.now();
        ComplexKeyExample modified = original.toBuilder()
                .updateDate(now2)
                .build();

        assertEquals(original, modified);
        assertEquals(original.hashCode(), modified.hashCode());
        assertFalse(original.equalsAllFields(modified));
    }

    @Test
    public void testComparableKeyType() {

        // uncomparable pk
        NonComparableKeyExample example1 = NonComparableKeyExample.builder()
                .no(1)
                .name("example")
                .build();

        // 컴파일 경고 발생하고, 실제 런타임에 예외 발생함
        assertThrows(ClassCastException.class, () -> {
            @SuppressWarnings("SortedCollectionWithNonComparableKeys")
            Map<PrimaryKey, DomainObject<?>> map1 = new TreeMap<>();
            map1.put(example1.getPrimaryKey(), example1);
            assertTrue(map1.containsKey(example1.getPrimaryKey()));
        });

        // comparable pk
        Instant now = Instant.now();
        ComplexKeyExample example2 = ComplexKeyExample.builder()
                .keyPart1(1)
                .keyPart2("A")
                .otherField1("field1")
                .otherField2("field2")
                .createDate(now)
                .updateDate(now)
                .build();

        assertDoesNotThrow(() -> {
            // <K extends PrimaryKey & Comparable<K>, V extends BaseDomainObject<K>>
            testGenericType(example2.getPrimaryKey(), example2);
        });
    }

    private <K extends PrimaryKey & Comparable<K>, V extends DomainObject<K>>
        void testGenericType(K key, V value) {

        Map<K, V> map = new TreeMap<>();
        map.put(key, value);
        assertTrue(map.containsKey(key));
    }

    @Test
    public void testIndependentPrimaryKey() {

        Instant now = Instant.now();
        Member member = Member.builder()
                .memberKey(MemberKey.of(1))
                .name("member1")
                .registerDate(now)
                .createDate(now)
                .updateDate(now)
                .build();

        assertEquals(member.getMemberNo(), member.getPrimaryKey().longValue());

        Address address = Address.builder()
                .memberKey(member.getPrimaryKey())
                .addressNo(1)
                .address("address1")
                .createDate(now)
                .build();

        assertEquals(address.getMemberNo(), address.getPrimaryKey().getMemberKey().longValue());

        Address.Key key = Address.Key.of(member.getPrimaryKey(), 1);
        assertEquals(key, address.getPrimaryKey());

        Post post = Post.builder()
                .memberKey(member.getPrimaryKey())
                .postNo(1)
                .title("title")
                .content("content")
                .createDate(now)
                .updateDate(now)
                .build();

        assertEquals(post.getMemberNo(), post.getMemberKey().longValue());
    }

    @Test
    public void testResidentId() {

        ResidentId residentId = new ResidentId("1111111111118");
        assertTrue(residentId.isMale());
        assertTrue(residentId.isValid());

        String[] s = { "1111111111118", "1234561222331" };
        for (String ss : s) {
            ResidentId id = new ResidentId(ss);
            assertTrue(id.isValid());
        }
    }
}

@Getter
@Builder
class NonComparableKeyExample implements DomainObject<NonComparableKeyExample.Key> {
    private int no;
    private String name;

    public static record Key(int no) implements PrimaryKey {
    }

    @Override
    public Key getPrimaryKey() {
        return new Key(no);
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        NonComparableKeyExample thatObj = (NonComparableKeyExample) that;
        return Objects.equals(getPrimaryKey(), thatObj.getPrimaryKey()) &&
                Objects.equals(name, thatObj.name);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(no);
    }
}
