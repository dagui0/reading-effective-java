package com.yidigun.utils;

import lombok.Builder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.Objects;

/**
 * 데이터베이스 테이블에 대응되는 값 객체용 인터페이스.
 * {@link PrimaryKey}를 구현한 키 타입을 {@link K}로 지정해야 한다.
 * 
 * <h3>{@link Object#equals(Object)}와 {@link Object#hashCode()}</h3>
 * 
 * {@link lombok.EqualsAndHashCode}나 {@link EqualsBuilder#reflectionEquals(Object, Object, String...)}
 * 등을 사용하여 구현하는 것 보다는 의미론적인 동등성을 비교할 수 있도록 주요 필드만 비교에 포함시키는 것이 좋다.
 *
 * <ul>
 *     <li>PK 필드 포함</li>
 *     <li>값 필드(이름, 주소 등) 포함</li>
 *     <li>로그 필드(생성일, 수정일 등)은 제외</li>
 * </ul>
 *
 * PK만 비교할 경우 {@link #getPrimaryKey()}나 {@link #equalsKeyOnly(Object)}를 사용하고,
 * 모든 필드를 포함한 비교가 필요한 경우 {@link #equalsAllFields(Object)}를 사용한다.
 *
 * <h3>단일 키 예시:</h3>
 *
 * <code>
 *   @Getter
 *   @Builder(toBuilder = true)
 *   public final class SimpleKeyExample implements BaseDomainObject<SimpleKeyExample.Key> {
 *     private int no;      // primary key
 *     private String name; // semantic field
 *     private Date createDate; // logging field
 *
 *     public static record Key(int no) {}
 *     @Override
 *     public Key getPrimaryKey() {
 *         return new Key(no);
 *     }
 *
 *     @Override
 *     public boolean equals(Object obj) {
 *         if (this == obj) return true;
 *         if (obj == null || getClass() != obj.getClass()) return false;
 *         SimpleExample that = (SimpleKeyExample) obj;
 *         return Object.equals(getPriamryKey(), that.getPrimaryKey()) &&
 *            Objects.equals(name, that.name);
 *         // createDate 같은 부차적 필드는 비교하지 않음
 *     }
 *     @Override
 *     public int hashCode() {
 *         return Objects.hash(getPrimaryKey(), name);
 *     }
 *   }
 * </code>
 *
 * <h3>복합 키 예시:</h3>
 *
 * <code>
 *   @Getter
 *   @Builder(toBuilder = true)
 *   public final class ComplexKeyExample implements BaseDomainObject<ComplexKeyExample.Key> {
 *     private int keyPart1;      // primary key
 *     private String keyPart2;   // primary key
 *     ...
 *     public static record Key(int keyPart1, String keyPart2) {}
 *     @Override
 *     public Key getPrimaryKey() {
 *         return new Key(keyPart1, keyPart2);
 *     }
 *     ...
 *   }
 * </code>
 *
 * <h3>{@link Builder}를 사용한 불변 클래스 예시:</h3>
 *
 * 값 객체는 불변 클래스로 만드는 것이 좋다.
 *
 * <ul>
 *     <li>모든 값 필드를 final로 선언한다.</li>
 *     <li>생성자({@link lombok.AllArgsConstructor}, {@link lombok.RequiredArgsConstructor})나
 *          빌더({@link lombok.Builder})를 구현한다. </li>
 *     <li>불변 객체를 수정할 때는 {@link Builder#toBuilder()} = true로 설정하여 toBuilder() 메서드를 사용할 수 있다.</li>
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
 *  // to create modified object
 *  ComplexKeyExample modified = original.toBuilder()
 *      .otherField1("FIELD1")
 *      .otherField2("FIELD2")
 *      .updateDate(new Date()).build();
 * </code>
 *
 * <h3><code>final</code> 클래스로 만드는 것에 대하여</h3>
 *
 *  <ul>
 *  <li>원론적으로 도메인 객체는 (특히 불변이라면) final로 하여 상속을 금지시키는 것이 좋다.</li>
 *  <li></li>단, ORM이나 특정 테스트 프레임워크에서는 상속을 이용하여 프록시 객체를 생성하는 경우가 있으므로
 *    이런 경우 final 객체를 사용하면 기능이 제한될 수있다.</li>
 *  </ul>
 * @param <K> Primary Key 필드를 나타내는 타입
 * @see ComplexKeyExample 완전한 도메인 객체 예시
 */
public interface BaseDomainObject<K extends PrimaryKey> {

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
     * 기본 메소드는 Apache Commons Lang의 {@link EqualsBuilder#reflectionEquals(Object, Object, String...)}를 사용한다.
     * 리플렉션 API의 오버헤드가 문제가 되거나, 일부 필드를 제외해야 하는 경우라면 직접 구현하도록 한다.
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
     * @param obj 비교할 객체
     * @return PK가 같으면 true, 아니면 false
     */
    default boolean equalsKeyOnly(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BaseDomainObject<?> that = (BaseDomainObject<?>) obj;
        return Objects.equals(getPrimaryKey(), that.getPrimaryKey());
    }
}
