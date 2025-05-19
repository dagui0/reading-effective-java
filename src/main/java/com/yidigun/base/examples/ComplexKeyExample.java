package com.yidigun.base.examples;

import com.yidigun.base.DomainObject;
import com.yidigun.base.PrimaryKey;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

/// 복합 키를 가진 도메인 객체 예시.
///
/// 이 예시 클래스는 setter 메소드를 지원하지 않는 불변 클래스이며
/// lombok의 [Builder]를 사용하여 빌더 패턴으로 객체를 생성한다.
///
/// 또한 [EqualsAndHashCode]를 사용하지 않고 직접 [#equals(Object)]와 [#hashCode()]를 구현하였다.
/// 
@Getter
@Builder(toBuilder = true)
public final class ComplexKeyExample implements DomainObject<ComplexKeyExample.Key> {

    /// Primary Key field #1
    private final int keyPart1;
    /// Primary Key field #2
    private final String keyPart2;
    /// Semantic field
    private final String otherField1;
    /// Semantic field
    private final String otherField2;
    /// Logging field
    private final Instant createDate;
    /// Logging field
    private final Instant updateDate;

    /// [ComplexKeyExample]의 PK 타입 클래스.
    /// 복합키 이지만 `record`를 이용하여 매우 단순한 코드로 정의 가능하다.
    /// @param keyPart1 PK 필드 #1
    /// @param keyPart2 PK 필드 #2
    public record Key(int keyPart1, String keyPart2) implements PrimaryKey {}

    /// lombok @Builder 용 빌더클래스
    ///
    /// [ComplexKeyExample]의 은 setter를 제공하지 않는 불변 클래스이므로,
    /// 빌더 클래스에 [#primaryKey(Key)] 메소드를 추가해줄 필요가 있다.
    ///
    public static class ComplexKeyExampleBuilder {
        public ComplexKeyExampleBuilder primaryKey(Key key) {
            return keyPart1(key.keyPart1())
                  .keyPart2(key.keyPart2());
        }
    }

    @Override
    public Key getPrimaryKey() {
        return new Key(keyPart1, keyPart2);
    }

    /// [#equals(Object)]와 동일한 기준(의미론적 동등성에 관련된 필드)만의 해시코드를 계산한다.
    /// @return 해시코드
    @Override
    public int hashCode() {
        return Objects.hash(getPrimaryKey(), otherField1, otherField2);
    }

    /// PK와 값데이터만 비교하는 메소드
    /// @param that 비교할 대상 객체
    /// @return true: 동등(의미론적), false: 비동등
    private boolean equalsCoreFields(ComplexKeyExample that) {
        return keyPart1 == that.keyPart1 &&
                Objects.equals(keyPart2, that.keyPart2) &&
                Objects.equals(otherField1, that.otherField1) &&
                Objects.equals(otherField2, that.otherField2);
    }

    /// 의미론적 동등성을 위한 비교 메소드. PK와 값데이터만 비교 대상으로 포함하고
    /// 로그, 캐시 용 필드는 비교에서 제외한다.
    ///
    /// 비교 로직은 [#equalsCoreFields(ComplexKeyExample)] private 메소드로 분리되어
    /// [#equalsAllFields(Object)]에서 재활용 할 수 있다.
    /// @param obj 비교할 대상 객체
    /// @return true: 동등(의미론적), false: 비동등
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ComplexKeyExample that = (ComplexKeyExample) obj;
        return equalsCoreFields(that);
    }

    /// 모든 필드를 비교하는 메소드.
    /// [#equalsCoreFields(ComplexKeyExample)]를 이용하여 의미론적 동등성을 평가한 후
    /// 로그 필드(생성일, 수정일 등)도 비교한다.
    /// @param obj 비교할 대상 객체
    /// @return true: 동등(기술적), false: 비동등
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
