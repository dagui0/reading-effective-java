package com.yidigun.utils;

import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class BaseDomainObjectTest {

    @Test
    public void testEquals() {

        Date now = new Date();
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

        Date now2 = new Date();
        ComplexKeyExample modified = original.toBuilder()
                .updateDate(now2)
                .build();

        assertEquals(original, modified);
        assertEquals(original.hashCode(), modified.hashCode());
        assertFalse(original.equalsAllFields(modified));
    }

    @SuppressWarnings({"SortedCollectionWithNonComparableKeys", "MismatchedQueryAndUpdateOfCollection"})
    @Test
    public void testComparableKeyType() {

        // uncomparable pk
        NonComparableDomainObject example1 = NonComparableDomainObject.builder()
                .no(1)
                .name("example")
                .build();

        assertThrows(ClassCastException.class, () -> {
            Map<PrimaryKey, BaseDomainObject<?>> map1 = new TreeMap<>();
            map1.put(example1.getPrimaryKey(), example1);
        });

        // comparable pk
        Date now = new Date();
        ComplexKeyExample example2 = ComplexKeyExample.builder()
                .keyPart1(1)
                .keyPart2("A")
                .otherField1("field1")
                .otherField2("field2")
                .createDate(now)
                .updateDate(now)
                .build();

        assertDoesNotThrow(() -> {
            Map<ComplexKeyExample.Key, ComplexKeyExample> map2 = new TreeMap<>();
            map2.put(example2.getPrimaryKey(), example2);
            assertTrue(map2.containsKey(new ComplexKeyExample.Key(1, "A")));
        });
    }
}

@Getter
@Builder
class NonComparableDomainObject implements BaseDomainObject<NonComparableDomainObject.Key> {
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
        NonComparableDomainObject thatObj = (NonComparableDomainObject) that;
        return Objects.equals(getPrimaryKey(), thatObj.getPrimaryKey()) &&
                Objects.equals(name, thatObj.name);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(no);
    }
}
