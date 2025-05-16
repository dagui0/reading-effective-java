package com.yidigun.utils;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

/**
 * {@link BaseDomainObject}의 복합키 예시
 */
@Getter
@Builder(toBuilder = true)
public final class ComplexKeyExample implements BaseDomainObject<ComplexKeyExample.Key> {
    private final int keyPart1;      // primary key
    private final String keyPart2;   // primary key
    private final String otherField1;// semantic field
    private final String otherField2;// semantic field
    private final Date createDate;   // logging field
    private final Date updateDate;   // logging field

    public static record Key(int keyPart1, String keyPart2)
            implements PrimaryKey, Comparable<Key> {
        @Override
        public int compareTo(@NotNull Key key) {
            int cmp = Integer.compare(this.keyPart1, key.keyPart1());
            if (cmp != 0) return cmp;
            return Objects.compare(this.keyPart2, key.keyPart2(),
                    Comparator.nullsFirst(Comparator.naturalOrder()));
        }
    }

    @Override
    public Key getPrimaryKey() {
        return new Key(keyPart1, keyPart2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrimaryKey(), otherField1, otherField2);
    }

    private boolean coreFieldsEqual(ComplexKeyExample that) {
        return keyPart1 == that.keyPart1 &&
                Objects.equals(keyPart2, that.keyPart2) &&
                Objects.equals(otherField1, that.otherField1) &&
                Objects.equals(otherField2, that.otherField2);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ComplexKeyExample that = (ComplexKeyExample) obj;
        return coreFieldsEqual(that);
    }

    @Override
    public boolean equalsAllFields(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ComplexKeyExample that = (ComplexKeyExample) obj;
        return coreFieldsEqual(that) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(updateDate, that.updateDate);
    }
}
