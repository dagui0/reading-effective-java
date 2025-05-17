package com.yidigun.base.examples;

import com.yidigun.base.DomainObject;
import com.yidigun.base.PrimaryKey;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Objects;

/**
 * 단순한 키를 가진 도메인 객체 예시.
 * <p>
 * 이 예시는 단순한 키를 가진 도메인 객체의 구조를 보여줍니다.
 * <p>
 * 이 객체는 기본 키로 정수형 필드를 사용하며, 추가적인 필드로 이름과 생성일을 포함합니다.
 * <p>
 * 이 객체는 {@link DomainObject} 인터페이스를 구현하고 있으며, {@link PrimaryKey} 인터페이스를 사용하여
 * 기본 키를 정의합니다.
 */
@EqualsAndHashCode
public class SimpleKeyExample implements DomainObject<SimpleKeyExample.Key> {

    private int no;             // primary key
    private String name;        // semantic field
    @EqualsAndHashCode.Exclude
    private Instant createDate; // logging field

    public static record Key(int no) implements PrimaryKey, Comparable<Key> {

        @Override
        public int compareTo(@NotNull SimpleKeyExample.Key o) {
            return Integer.compare(this.no, o.no());
        }
    }

    @Override
    public Key getPrimaryKey() {
        return new Key(no);
    }
}
