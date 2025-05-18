package com.yidigun.base;

import com.yidigun.base.examples.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DomainObjectTest {

    @Test
    public void testSemanticEquality() {

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

        // semantic equality
        assertEquals(original, modified);
        assertEquals(original.hashCode(), modified.hashCode());

        // technical equality
        assertFalse(original.equalsAllFields(modified));
    }

    @Test
    public void testComparableKeyType() {

        // uncomparable pk
        ComplexKeyExample example1 = ComplexKeyExample.builder()
                .keyPart1(1)
                .keyPart2("A")
                .otherField1("field1")
                .otherField2("field2")
                .createDate(Instant.now())
                .updateDate(Instant.now()).build();

        // 컴파일 경고 발생하고, 실제 런타임에 예외 발생함
        assertThrows(ClassCastException.class, () -> {
            @SuppressWarnings("SortedCollectionWithNonComparableKeys")
            Map<PrimaryKey, DomainObject<?>> map1 = new TreeMap<>();
            map1.put(example1.getPrimaryKey(), example1);
            assertTrue(map1.containsKey(example1.getPrimaryKey()));
        });

        // comparable pk
        Instant now = Instant.now();
        SimpleKeyExample example2 = new SimpleKeyExample();
        example2.setNo(1);
        example2.setName("example");
        example2.setCreateDate(now);

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
    public void testIndependentPrimaryKeyType() {

        MemberKey someMemberKey = MemberKey.of(1);

        // Master table
        Instant now = Instant.now();
        Member member = Member.builder()
                .memberKey(someMemberKey)
                .name("member1")
                .registerDate(now)
                .createDate(now)
                .updateDate(now)
                .build();

        assertEquals(someMemberKey.longValue(), member.getPrimaryKey().longValue());
        assertEquals(someMemberKey.longValue(), member.getMemberKey().longValue());

        // Child table
        Address address = Address.builder()
                .memberKey(someMemberKey)
                .addressNo(1)
                .address("address1")
                .createDate(now)
                .build();

        assertEquals(someMemberKey, address.getMemberKey());
        // Address.Key is also MemberKey.Aware
        assertEquals(someMemberKey.longValue(), address.getPrimaryKey().getMemberKey().longValue());

        // Address.Key type is a standalone value type.
        Address.Key key = Address.Key.of(member.getPrimaryKey(), 1);
        assertEquals(key, address.getPrimaryKey());

        // Post table has reference to Member table
        Post post = Post.builder()
                .postNo(1)
                .title("title")
                .content("content")
                .memberKey(someMemberKey)
                .createDate(now)
                .updateDate(now)
                .build();

        assertEquals(someMemberKey, post.getMemberKey());
        assertEquals(someMemberKey.longValue(), post.getMemberKey().longValue());
    }

    @Test
    public void testResidentId() {

        ResidentId residentId = ResidentId.of("1111111111118");
        assertTrue(residentId.isMale());
        assertTrue(residentId.isValid());

        String[] valids = { "1111111111118", "1234561222331" };
        for (String s : valids) {
            ResidentId id = ResidentId.of(s);
            assertTrue(id.isValid());
        }

        String[] nonValids = { "1234561234567", "9553179792331" };
        for (String s : nonValids) {
            ResidentId id = ResidentId.ofUnchecked(s);
            assertFalse(id.isValid());
        }

        // parse alt pattern
        ResidentId id1 = ResidentId.of("1234561222331");
        ResidentId id2 = ResidentId.of("123456-1222331");
        assertEquals(id1, id2);

        // Birthday
        ResidentId id3 = ResidentId.of("1111111111118");

        assertEquals("111111", id3.subSequence(0, 6));

        Calendar cal = Calendar.getInstance();
        cal.set(1911, 10, 11);
        assertEquals(cal.toInstant(), id3.getBirthday());
    }
}
