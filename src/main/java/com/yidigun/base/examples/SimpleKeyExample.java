package com.yidigun.base.examples;

import com.yidigun.base.DomainObject;
import com.yidigun.base.PrimaryKey;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Objects;

/// 단순한 키를 가진 도메인 객체 예시.
///
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
