package com.yidigun.base;

import com.yidigun.base.examples.ComplexKeyExample;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.Objects;

/**
 * 데이터베이스 테이블에 대응되는 값 객체용 인터페이스.
 * {@link PrimaryKey}를 구현한 키 타입을 {@link K}로 지정해야 한다.
 * 
 * <h3>{@link Object#equals(Object)}와 {@link Object#hashCode()}</h3>
 * 
 * {@link EqualsAndHashCode}나 {@link EqualsBuilder#reflectionEquals(Object, Object, String...)}
 * 등을 사용하여 구현하는 것 보다는 의미론적인 동등성을 비교할 수 있도록 주요 필드만 비교에 포함시키는 것이 좋다.
 *
 * <ul>
 *     <li>PK 필드는 반드시 비교에 포함한다.</li>
 *     <li>유의미한 값 필드(이름, 주소 등)만 포함</li>
 *     <li>로그 필드(생성일, 수정일 등)나 캐싱용 필드 등은 제외한다.</li>
 * </ul>
 *
 * PK만 비교할 경우 {@link #getPrimaryKey()}나 {@link #equalsKeyOnly(Object)}를 사용하고,
 * 모든 필드를 포함한 비교가 필요한 경우 {@link #equalsAllFields(Object)}를 사용한다.
 * <p>
 * lombok의 {@link EqualsAndHashCode}를 사용하여 자동 생성을 할 수도 있다.
 * 다만 비교에서 제외할 필드를 분명하게 구분해서 {@link EqualsAndHashCode.Exclude}를 지정해야 한다.
 *
 * <h3>단일 키 예시:</h3>
 *
 * <code>
 * @Getter
 * @Builder(toBuilder = true)
 * @EqualsAndHashCode
 * public final class SimpleKeyExample implements DomainObject<SimpleKeyExample.Key> {
 *     private int no;      // primary key
 *     private String name; // semantic field
 *     @EqualsAndHashCode.Exclude
 *     private Date createDate; // logging field
 *
 *     // 중첩 record를 이용하여 PrimaryKey 구현체를 간편하게 만들 수 있다. (Java 14 이상)
 *     public static record Key(int no) impelements PrimaryKey {}
 *
 *     @Override
 *     public Key getPrimaryKey() { return new Key(no); }
 * }
 * </code>
 *
 * <h3>복합 키 예시:</h3>
 *
 * <code>
 * @Getter
 * @Builder(toBuilder = true)
 * public final class ComplexKeyExample implements DomainObject<ComplexKeyExample.Key> {
 *     private int keyPart1;      // primary key
 *     private String keyPart2;   // primary key
 *     ...
 *     public static record Key(int keyPart1, String keyPart2) {}
 *     @Override
 *     public Key getPrimaryKey() {
 *         return new Key(keyPart1, keyPart2);
 *     }
 *     ...
 * }
 * </code>
 *
 * <h3>{@link Builder}를 사용한 불변 클래스 예시:</h3>
 *
 * 값 객체는 불변 클래스로 만드는 것이 좋다.
 *
 * <ul>
 *     <li>모든 값 필드를 final로 선언한다.</li>
 *     <li>생성자({@link AllArgsConstructor}, {@link RequiredArgsConstructor})나
 *         빌더({@link Builder})를 구현한다. </li>
 *     <li>불변 객체를 수정할 때는 {@link Builder#toBuilder()} = true로 설정하여
 *         toBuilder() 메서드를 사용할 수 있다.</li>
 * </ul>
 *
 * <code>
 *  ComplexKeyExample original = ComplexKeyExample.builder()
 *      .keyPart1(1)
 *      .keyPart2("A")
 *      .otherField1("field1")
 *      .otherField2("field2")
 *      .createDate(new Date())
 *      .updateDate(new Date()).build();
 *
 *  // 필드 값을 수정한 새로운 객체 생성
 *  ComplexKeyExample modified = original.toBuilder()
 *      .otherField1("FIELD1")
 *      .otherField2("FIELD2")
 *      .updateDate(new Date()).build();
 * </code>
 *
 * <h3><code>final</code> 클래스로 만드는 것에 대하여</h3>
 *
 * 원론적으로 도메인 객체용 클래스는 테이블과 1:1 대응되도록 만들고, 테이블이 변경될 경우 클래스도 수정하여 동일하게 일치시켜야 한다.
 * 상속을 하여 클래스를 확장할 이유가 없다. 따라서 (특히 불변이라면) final로 하여 상속을 금지시키는 것이 좋다.
 * <p>
 * 단, ORM이나 특정 테스트 프레임워크에서는 상속을 이용하여 프록시 객체를 생성하여 추가적인 기능을 삽입하는 경우가 있을 수 있는데,
 * 이런 경우 final 클래스를 사용하면 기능이 제한될 수있다.
 *
 * @param <K> Primary Key 필드를 나타내는 타입
 * @see ComplexKeyExample 완전한 도메인 객체 예시
 */
public interface DomainObject<K extends PrimaryKey> {

    /**
     * {@link PrimaryKey}를 구현한 키 객체를 반환한다.
     *
     * @return {@link PrimaryKey}를 구현한 키 객체
     */
    K getPrimaryKey();

    /**
     * {@link Object#equals(Object)}와 {@link Object#hashCode()}는
     * 의미론적인 동등성을 위해 필요한 필드만을 비교에 포함시킨다.
     * 만약 모든 필드를 비교해야 하는 요구사항이 있는 경우 이 메서드를 사용하도록 한다.
     *
     * 기본 메소드는 <a heref="https://mvnrepository.com/artifact/org.apache.commons/commons-lang3">Apache Commons Lang</a>의
     * {@link EqualsBuilder#reflectionEquals(Object, Object, String...)}를
     * 제외 필드 설정 없이 모든 필드를 비교하도록 하여 사용한다.
     * 리플렉션 API의 오버헤드가 문제가 되거나, 일부 필드를 제외해야 하는 경우라면 직접 {@link Override}하여 구현하도록 한다.
     *
     * @param obj 비교할 객체
     * @return 모든 필드가 같으면 true, 아니면 false
     */
    default boolean equalsAllFields(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * {@link Object#equals(Object)}와 {@link Object#hashCode()}는
     * 의미론적인 동등성을 위해 필요한 필드만을 비교에 포함시킨다.
     * 이 메소드는 {@link #getPrimaryKey()}를 사용하여 PK만을 비교한다.
     *
     * @param obj 비교할 객체
     * @return PK가 같으면 true, 아니면 false
     */
    default boolean equalsKeyOnly(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DomainObject<?> that = (DomainObject<?>) obj;
        return Objects.equals(getPrimaryKey(), that.getPrimaryKey());
    }
}
