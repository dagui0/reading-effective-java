package com.yidigun.base;

import com.yidigun.base.examples.ComplexKeyExample;
import com.yidigun.base.examples.SimpleKeyExample;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.lang.reflect.Proxy;
import java.util.Objects;

/// 데이터베이스 테이블에 대응되는 값 객체용 인터페이스.
/// [PrimaryKey]를 구현한 키 타입을 `<K>`로 지정해야 한다.
/// 
/// ## [Object#equals(Object)]와 [Object#hashCode()]
/// 
/// [EqualsAndHashCode]나 [EqualsBuilder#reflectionEquals(Object, Object, String...)]
/// 등을 사용하여 구현하는 것 보다는 의미론적인 동등성을 비교할 수 있도록 주요 필드만 비교에 포함시키는 것이 좋다.
///
/// * PK 필드는 반드시 비교에 포함한다.
/// * 유의미한 값 필드(이름, 주소 등)만 포함
/// * 로그 필드(생성일, 수정일 등)나 캐싱용 필드 등은 제외한다.
///
/// PK만 비교할 경우 [#getPrimaryKey()]나 [#equalsKeyOnly(Object)]를 사용하고,
/// 모든 필드를 포함한 비교가 필요한 경우 [#equalsAllFields(Object)]를 사용한다.
///
/// lombok의 [EqualsAndHashCode]를 사용하여 자동 생성을 할 수도 있다.
/// 다만 비교에서 제외할 필드를 분명하게 구분해서 [EqualsAndHashCode.Exclude]를 지정해야 한다.
///
/// ## 단순 키 예시
///
/// [PrimaryKey]인터페이스를 구현한 중첩 클래스(record) `SimpleKeyExample.Key`를 사용하여 구현하였다.
/// [#getPrimaryKey()]에 대응하는 `setPrimaryKey(SimpleKeyExample.Key key)` 메소드를 구현해 주었다.
///
/// ```
/// @Getter
/// @Setter
/// @EqualsAndHashCode
/// public final class SimpleKeyExample implements DomainObject<SimpleKeyExample.Key> {
///     private int no;      // primary key
///     private String name; // semantic field
///     @EqualsAndHashCode.Exclude
///     private Date createDate; // logging field
///
///     // 중첩 record를 이용하여 PrimaryKey 구현체를 간편하게 만들 수 있다. (Java 14 이상)
///     public record Key(int no) implements PrimaryKey, Comparable<Key> {
///        @Override
///        public int compareTo(@NotNull Key o) { return Integer.compare(this.no, o.no()); }
///     }
///     @Override
///     public Key getPrimaryKey() { return new Key(no); }
///     public void setPrimaryKey(Key key) { this.no = key.no(); }
/// }
/// ```
///
/// ### 복합 키 예시
///
/// 단순 키의 경우도 마찬가지지만,
/// 복합 키의 경우는 PK 타입인 [ComplexKeyExample.Key]는 단일 값으로 취급되어야 하므로
/// setter 메소드나 빌더 메소드를 반드시 추가해주는 것이 필요하다.
///
/// ```
/// @Getter
/// @Builder(toBuilder = true)
/// public final class ComplexKeyExample implements DomainObject<ComplexKeyExample.Key> {
///     private final int keyPart1;      // primary key
///     private final String keyPart2;   // primary key
///     ...
///     public record Key(int keyPart1, String keyPart2) {}
///     ...
///     /// @Builder 용 빌더클래스
///     public static class ComplexKeyExampleBuilder {
///         public ComplexKeyExampleBuilder primaryKey(Key key) {
///             return keyPart1(key.keyPart1())
///                   .keyPart2(key.keyPart2());
///         }
///     }
///     @Override
///     public Key getPrimaryKey() {
///         return new Key(keyPart1, keyPart2);
///     }
///     ...
/// }
///
/// ComplexKeyExample ex1 = ComplexKeyExample.builder()
///         .keyPart1(1)     // PK 개별 값으로 생성
///         .keyPart2("A")
///         .otherField1("field1")
///         .otherField2("field2").build();
/// ComplexKeyExample ex2 = ComplexKeyExample.builder()
///         .primaryKey(ex1.getPrimaryKey())  // PK 객체로 생성
///         .otherField1("field1")
///         .otherField2("field2").build();
/// ```
///
/// ## [Builder]를 사용한 불변 클래스
///
/// [SimpleKeyExample]은 불변객체가 아니고 setter 메소드를 제공하지만,
/// [ComplexKeyExample]은 불변객체(모든 필드가 `final`)이고 빌더 패턴을 통해서 초기화하도록 하고 있다.
///
/// 기본적으로 값 객체는 불변 클래스로 만드는 것이 좋다.
///
/// * 모든 값 필드를 final로 선언한다.</li>
/// * 생성자([AllArgsConstructor], [RequiredArgsConstructor])나
///   빌더([Builder])를 구현한다. </li>
/// * 불변 객체를 수정할 때는 [Builder#toBuilder()] = true로 설정하여
///   toBuilder() 메서드를 사용할 수 있다.</li>
///
/// ```
/// ComplexKeyExample original = ComplexKeyExample.builder()
///     .keyPart1(1)
///     .keyPart2("A")
///     .otherField1("field1")
///     .otherField2("field2")
///     .createDate(new Date())
///     .updateDate(new Date()).build();
///
/// // 필드 값을 수정한 새로운 객체 생성
/// ComplexKeyExample modified = original.toBuilder()
///     .otherField1("FIELD1")
///     .otherField2("FIELD2")
///     .updateDate(new Date()).build();
/// ```
///
/// ## `final` 클래스로 만드는 것에 대하여
///
/// 원론적으로 도메인 객체용 클래스는 테이블과 1:1 대응되도록 만들고,
/// 테이블이 변경될 경우 클래스도 수정하여 동일하게 일치시켜야 한다.
/// 상속을 하여 클래스를 확장할 이유가 없다.
///
/// 따라서 (특히 불변이라면) `final` 선언하여 상속을 금지시키는 것이 좋다.
///
/// 단, ORM이나 특정 테스트 프레임워크에서는 상속을 이용하여 프록시 객체([Proxy])를
/// 생성하여 추가적인 기능을 삽입하는 경우가 있을 수 있는데,
/// 이런 경우 final 클래스를 사용하면 기능이 제한될 수있다.
///
/// @param <K> Primary Key 필드를 나타내는 타입
/// @see PrimaryKey
/// @see ComplexKeyExample ComplexKeyExample(복합키 도메인 객체 예시)
/// @see SimpleKeyExample SimpleKeyExample(단순키 도메인 객체 예시)
public interface DomainObject<K extends PrimaryKey> {

    /// [PrimaryKey]를 구현한 키 객체를 반환한다.
    ///
    /// @return [PrimaryKey]를 구현한 키 객체
    ///
    K getPrimaryKey();

    /// 모든 필드를 비교해야 하는 요구사항이 있는 경우 이 메서드를 사용하도록 한다.
    ///
    /// [Object#equals(Object)]와 [Object#hashCode()]는
    /// 의미론적인 동등성을 위해 필요한 필드만을 비교에 포함시킨다.
    ///
    /// 기본 메소드는 [Apache Commons Lang](https://mvnrepository.com/artifact/org.apache.commons/commons-lang3)의
    /// [EqualsBuilder#reflectionEquals(Object, Object, String...)]를
    /// 제외 필드 설정 없이 모든 필드를 비교하도록 하여 사용한다.
    /// 리플렉션 API의 오버헤드가 문제가 되거나, 일부 필드를 제외해야 하는 경우라면 직접 [Override]하여 구현하도록 한다.
    ///
    /// @param obj 비교할 객체
    /// @return 모든 필드가 같으면 true, 아니면 false
    default boolean equalsAllFields(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /// 이 메소드는 [#getPrimaryKey()]를 사용하여 PK만을 비교한다.
    ///
    /// [Object#equals(Object)]와 [Object#hashCode()]는
    /// 의미론적인 동등성을 위해 필요한 필드만을 비교에 포함시킨다.
    ///
    /// @param obj 비교할 객체
    /// @return PK가 같으면 true, 아니면 false
    default boolean equalsKeyOnly(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DomainObject<?> that = (DomainObject<?>) obj;
        return Objects.equals(getPrimaryKey(), that.getPrimaryKey());
    }
}
