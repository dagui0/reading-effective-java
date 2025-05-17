package com.yidigun.base.examples;

import com.yidigun.base.DomainObject;
import com.yidigun.base.PrimaryKey;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Comparator;
import java.util.Objects;

/// 복합 키를 가진 도메인 객체 예시.
/// 
/// [Key] 레코드는 [Comparable] 인터페이스를구현하여 정렬에 사용할 수 있다.
/// 
@Getter
@Builder(toBuilder = true)
public final class ComplexKeyExample implements DomainObject<ComplexKeyExample.Key> {
    private final int keyPart1;         // primary key
    private final String keyPart2;      // primary key
    private final String otherField1;   // semantic field
    private final String otherField2;   // semantic field
    private final Instant createDate;   // logging field
    private final Instant updateDate;   // logging field

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

    private boolean equalsCoreFields(ComplexKeyExample that) {
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
        return equalsCoreFields(that);
    }

    @Override
    public boolean equalsAllFields(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ComplexKeyExample that = (ComplexKeyExample) obj;
        return equalsCoreFields(that) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(updateDate, that.updateDate);
    }
}
