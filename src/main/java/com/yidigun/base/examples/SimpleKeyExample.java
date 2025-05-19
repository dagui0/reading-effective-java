package com.yidigun.base.examples;

import com.yidigun.base.DomainObject;
import com.yidigun.base.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/// 단순한 키를 가진 도메인 객체 예시.
///
/// 이 예시 클래스는 lombok의 [Setter]를 사용하여 setter 메소드를 자동으로 생성하는데,
/// [DomainObject]의 [DomainObject#getPrimaryKey()]에 대응하는 [#setPrimaryKey(Key)] 메소드를 수동으로 추가해주어야 한다.
/// 
/// 또한 [EqualsAndHashCode]를 사용하여 [#equals(Object)]와 [#hashCode()]를 자동으로 생성한다.
///
@Getter
@Setter
@EqualsAndHashCode
public class SimpleKeyExample implements DomainObject<SimpleKeyExample.Key> {

    /// primary key
    private int no;
    /// semantic field
    private String name;
    /// logging field
    @EqualsAndHashCode.Exclude
    private Instant createDate;

    /// [SimpleKeyExample]의 PK 타입 클래스.
    ///
    /// [Comparable]을 구현하여 크기 비교 및 정렬에 사용할 수 있도록 확장되어있다.
    /// @param no PK 필드
    public record Key(int no) implements PrimaryKey, Comparable<Key> {
        @Override
        public int compareTo(@NotNull Key o) {
            return Integer.compare(this.no, o.no());
        }
    }

    @Override
    public Key getPrimaryKey() {
        return new Key(no);
    }

    /// [DomainObject]의 [#getPrimaryKey()]에 대응하는 setter 메소드.
    /// @param key PK 값
    /// @see DomainObject#getPrimaryKey()
    public void setPrimaryKey(Key key) {
        this.no = key.no();
    }
}
