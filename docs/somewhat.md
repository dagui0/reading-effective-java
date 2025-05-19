# Somewhat Effective Java

## 목차

* [**아이템 0.01**: 닥치고 Java 23으로 업그레이드 하라](#아이템-001-닥치고-java-23으로-업그레이드-하라)
* [**아이템 0.02**: Primary Key는 별도 타입을 만들라](#아이템-002-primary-key는-별도-타입을-만들라)
* [**아이템 0.03**: 마스터 테이블의 PK는 독립 클래스로 만들라](#아이템-003-마스터-테이블의-pk는-독립-클래스로-만들라)
* [**아이템 0.04**: 원시 타입을 사용하는 경우 `null`처리를 위해 `Optional`을 고려하라](#아이템-004-원시-타입을-사용하는-경우-null처리를-위해-optional을-고려하라)

## 아이템 0.01: 닥치고 Java 23으로 업그레이드 하라

Java 23 부터 javadoc에서 Markdown을 지원한다. **빡! 끗!**

> [Markdown in Java Docs? Shut Up and Take My Comments!](https://blog.jetbrains.com/idea/2025/04/markdown-in-java-docs-shut-up-and-take-my-comments/)

```java
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
///     public record Key(int no) impelements PrimaryKey, Comparable<Key> {
///        @Override
///        public int compareTo(@NotNull Key o) { return Integer.compare(this.no, o.no()); }
///     }
///     @Override
///     public Key getPrimaryKey() { return new Key(no); }
///     public void setPrimaryKey(Key key) { this.no = key.no(); }
/// }
/// ```
/// @param <K> Primary Key 필드를 나타내는 타입
/// @see PrimaryKey
/// @see ComplexKeyExample ComplexKeyExample(복합키 도메인 객체 예시)
/// @see SimpleKeyExample SimpleKeyExample(단순키 도메인 객체 예시)
public interface DomainObject<K extends PrimaryKey> {  }
```

* Java 23은 일반 릴리즈(2024년 9월 17일)이고 현재 최신 릴리즈는 24(2025년 3월 18일)이다.
* 다음 LTS버전은 Java 25이며 25년 9월 출시될 예정임.
* 참고로 Java 23 미만이라고 하더라도 javadoc주석을 `/// markdown` 형식으로 작성해도 아무 문제 없다.
  * 그냥 javadoc 주석이 없는 것으로 취급됨. 어짜피 javadoc 주석을 달아도 html로 빌드는 안하잖아?
  * 그러니 지금부터 markdown 주석을 적극 사용하자.

## 아이템 0.02: Primary Key는 별도 타입을 만들라

```markdown
### [추가] `equals()`관련 토론 주제: 어디까지 비교할 것인가?

...

스터디 멤버 의견:

* Alejandro: 비교하면 안된다. 관리상 기록을 위한 것이지 객체의 속성이라고 볼 수 없음
* Leeturn: 모두 비교해야 pure java지만 관리용 날짜는 빼는것이 적절함
* Scully: 생성 수정 날짜는 비교에서 빼야함
* Lucie: 오딧 날짜는 빼고 비교해야함
```

### 동등성(Equality)의 기준

두 객체가 같다(`a.equals(b)`)고 할 때 무엇이 같은 것인가?

1. 식별자 기반 동등성(Identifier-based equality): 같은 대상을 나타낸다.
   * PK 필드만 비교해야 한다.
2. 의미론적 동등성(Content-based equality): 같은 값을 가진다.
   * 유의미한 필드만 비교해야 한다.
   * 수정일시 등 로그성 필드, 캐시를 위한 필드는 제외해야 한다.
3. 완전한 상태 동등성(Full state equality): 상태를 포함한 모든 필드가 같다.
   * 모든 필드를 비교해야 한다.

DB테이블의 데이터를 나타내는 도메인 객체의 `equals()`와 `hashCode()`는 어떤 동등성을 적용해야 하는가?

1. 컬렉션에서 객체를 찾을 때 사용한다. (해시 알고리즘 기반 컬렉션: `HashMap`, `HashSet`)
   * 이 경우 식별자 기반 동등성이 적절하다.
2. 두 객체가 같은 값을 가지고 있는지 확인: DB 업데이트가 필요한 지를 확인 
   * 의미론적 동등성이 적절하다. (수정일시 같은 필드는 제외해야 함)
3. 만약 DB에 저장이 완료된 상태인지를 포함한 비교가 필요하다면?
   * 상태 동등성이 필요하지만, 이런 경우는 흔하지는 않다.

### 3가지 동등성을 모두 지원하기 위한 방법은?

ORM 프레임워크가 자동 생성하는 도메인 클래스의 경우, `equals()`는 식별자 기반 동등성인 경우가 대부분이다.
이런 경우 PK동등성만 확인되면 의미론적 동등성과 상태 동등성은 ORM이 보장해준다는 의미로 볼 수 있다.
즉, ORM 내부 처리 메커니즘과정에서 알아서 처리된다.

하지만, SQL mapper나 JDBC를 사용하여 개발자가 직접 도메인 객체의 `equals()`를 직접 호출하는 경우가 있다면,
그 대부분은 의미론적 동등성을 추구하는 상황일 가능성이 높다.

```java
/// DB테이블에 대응하는 도메인 객체용 인터페이스
interface DomainObject {
    boolean equalsKeyOnly(Object o);    // 식별자 동등성
    boolean equals(Object o);           // 의미론적 동등성
    boolean equalsAllFields(Object o);  // 상태 동등성
}
```

`equals()`를 의미론적 동등성 확인용으로 설정하고 다른 메소드를 추가한 이유는,
의미론적 동등성을 구현하려면 개발자가 직접 필요한 필드를 선택하는 코딩이 필요하고, 이는 lombok과 같은
도구의 도움을 받을 수 있기 때문이다.

`equalsKeyOnly()`와 `equalsAllFields()`는 미리 `default` 메소드로 구현해서
개발자가 매번 구현하지 않도록 할 수 있다.


```java
/// Primary Key를 담은 DTO용 마커 인터페이스
interface PrimaryKey {}

interface DomainObject<K extends PrimaryKey> {
    K getPrimaryKey();          // 직접 구현 필요
    boolean equals(Object o);   // 직접 구현 필요
    default boolean equalsKeyOnly(Object o) {
        // 타입 검사는 일단 생략함
        return Objects.equals(getPrimaryKey(), ((DomainObject<?>) o).getPrimaryKey());
    }
    default boolean equalsAllFields(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }
}
```

### 가장 단순한 형태

```java
@Getter
@Setter
@EqualsAndHashCode // `equals()` 직접 구현
public final class SimpleKeyExample implements DomainObject<SimpleKeyExample.Key> {
    private int no;      // primary key
    private String name; // semantic field
    @EqualsAndHashCode.Exclude // 의미론적 동등성에서 제외할 필드 지정 
    private Date createDate; // logging field

    public record Key(int no) implements PrimaryKey {}

    @Override
    public Key getPrimaryKey() { return new Key(no); } // 직접 구현
    public void setPrimaryKey(Key key) { this.no = key.no(); }
}
```

`PrimaryKey`는 중첩 `record`를 이용하면 단 한줄로 구현이 가능하다. (Java 14+) 

이렇게 `PrimaryKey` 인터페이스를 도입하는 경우 부가적인 장점이 있는데, 타입 안정성을 더 보장할 수 있게 된다.

`int`는 범용 자료형이므로 다른 테이블의 식별자일 수도 있고, 심지어 식별자가 아닌 값일 수도 있다.
반면에 `SimpleKeyExample.Key`는 `SimpleKeyExample` 테이블의 식별자인 것이 타입으로 드러난다.

```java
public <K extends PrimaryKey, V extends DomainObject<K>> void addCache(K key, V value) {
    Map<K, V> map = new HashMap<>();
    map.put(key, value);
}
public void addCache(SimpleKeyExample.Key key, SimpleKeyExample value) {
    Map<SimpleKeyExample.Key, SimpleKeyExample> map = new HashMap<>();
    map.put(key, value);
}

void main() {
    SimpleKeyExample example = new SimpleKeyExample();
    example.setNo(1);
    example.setName("이름");
    example.setCreateDate(new Date());

    addCache(example.getPrimaryKey(), example);
}
```

필요하다면 `PrimaryKey` 타입을 확장해서 추가적인 기능을 제공하는 것도 가능하다.

```java
public record Key(int no) implements PrimaryKey, Comparable<Key> {
    @Override
    public int compareTo(@NotNull Key o) {
        return Integer.compare(this.no, o.no());
    }
}

void main() {
    Map<SimpleKeyExample.Key, SimpleKeyExample> map = new TreeMap<>();
    SimpleKeyExample example = new SimpleKeyExample();
    example.setNo(1);
    map.put(example.getPrimaryKey(), example);
}
```

### 복합 키 예시

테이블의 PK가 한개 이상의 컬럼으로 구성된 경우도 흔하다. 이런 경우 `PrimaryKey`가 단일 값으로 취급될 수 있도록
`getPrimaryKey()`에 대응하는 `setPrimaryKey()` 메소드를 반드시 구현해 주어야 할 것이다.

불변 객체라면 `@Builder`가 생성하는 `클래스명+Builder` 클래스에 빌드 메소드를 미리 추가해주면 된다.

```java
@Getter
@Builder // 불변 객체로 하고 @Setter, @AllArgsConstructor 대신 @Builder 사용
public final class ComplexKeyExample implements DomainObject<ComplexKeyExample.Key> {
    private final int keyPart1;      // primary key
    private final String keyPart2;   // primary key
    // ...
    public record Key(int keyPart1, String keyPart2) {}
    // ...
    /// @Builder 용 빌더클래스
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
    // ...
}

void main() {
    ComplexKeyExample example = ComplexKeyExample.builder()
            .primaryKey(otherExample.getPrimaryKey())  // PK 객체로 객체 생성
            .otherField1("field1")
            .otherField2("field2").build();
}
```

### 불변객체와 값 객체 수정 방법

도메인 객체는 값 객체(Value Object)의 일종이고 현대 자바에서는 값 객체는 불변 객체로
구현하는 것이 일반적인 추세다. (Java 14+ `record` 도입 등)

하지만 도메인 객체는 DB 데이터 관리를 위한 것으로 값을 변경하는 기능이 반드시 필요하다.
이럴 때 불변객체를 사용하면서도 복사 빌더를 통해 요구사항을 달성할 수 있다.

lombok에는 `@Builder(toBuilder = true)`를 사용해서 복사 빌더를 구현할 수 있다.

```java
@Builder(toBuilder = true)
public final class ComplexKeyExample implements DomainObject<ComplexKeyExample.Key> {
    // ...
}

void main() {
    ComplexKeyExample example = ComplexKeyExample.builder()
            .keyPart1(1)
            .keyPart2("keyPart2")
            .otherField1("field1")
            .otherField2("field2")
            .createDate(new Date())
            .updateDate(new Date()).build();

    // 복사 빌더 사용 수정된 객체 생성
    ComplexKeyExample copy = example.toBuilder()
            .otherField2("field2_1")
            .updateDate(new Date()).build();
}
```

### `final` 클래스 관련

도메인 클래스는 테이블과 1:1 매핑되도록 생성되고, 필드도 테이블의 컬럼과 1:1 매핑되도록 설계된다.
만약 테이블에 새로운 컬럼이 추가되거나 해서 구조가 변경되면, 도메인 클래스도 수정해서 일치시켜야 한다.

따라서 도메인 클래스를 상속해서 새로운 클래스를 만들 일은 없고,
만약 그렇게 개발한다면 안좋은 패턴일 수 있다.
통상적인 경우라면 `final`로 선언해서 상속을 금시시키는 것이 좋은 코드일 것이다.

다만, ORM 이나 테스트 프레임워크에서는 객체의 상속 메커니즘을 이용한 `Proxy`객체를 만들어서
추가적인 기능을 삽입하는 방법을 사용할 수 있다. 이럴 때는 `final` 클래스를 사용한다면
상속이 불가능하므로 `Proxy`를 이용한 기능을 사용할 수 없게 된다. (JPA는 `final` 클래스 지원 안함)

## 아이템 0.03: 마스터 테이블의 PK는 독립 클래스로 만들라

회원테이블의 PK인 회원번호는 수많은 테이블에서 FK로 사용된다.

* 부모 테이블의 PK가 PK의 일부분인 경우(식별 관계, Identifying Relationship)
* 부모 테이블의 PK가 일반 필드인 경우(비식별 관계, Non-identifying Relationship)

DB입장에서는 두 엔티티(테이블)의 관계에 따라서 두가지로 구분될 수 있지만,
프로그램 코드의 모습은 크게 다르지 않다.
도메인 객체가 상위 테이블의 PK를 멤버 필드로 가지고 있는 형태이다.

이런 경우 PK를 중첩 `record`로 단순하게 구현하지 말고 독립 클래스(top-level)로 구현하면
편의성과 코드의 명확성이 한층 높아질 수 있다.

```java
// 재사용 가능성이 높은 PK는 밖으로 꺼내서 독립적으로 사용이 쉽도록 한다.
public record MemberKey(long memberNo) extends Primarykey {}

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode
public final class Member implements DomainObject<MemberKey> {
    private final long memberNo; // PK
    private String name;         // semantic field
    private Date registerDate;   // semantic field
    @EqualsAndHashCode.Exclude
    private Date createDate;     // logging field

    @Override
    public MemberKey getPrimaryKey() { return getMemberKey(); }

    public static class MemberBuilder {
        public MemberBuilder primaryKey(MemberKey key) {
            return memberKey(key);
        }
        public MemberBuilder memberKey(MemberKey key) {
            return memberNo(key.memberNo());
        }
    }
    
    public MemberKey getMemberKey() {
        return new MemberKey(memberNo);
    }
}
```

실제 데이터인 `memberNo` 대신에 `memberKey`라는 가상의 프로퍼티를 사용할 수 있도록
`getMemberKey()`와 `MemberBuilder.memberKey()`를 추가해 주면
`MemberKey`가 회원번호를 대표하는 객체 타입으로 확실하게 자리매김 할 수 있다.

### 부속 테이블에 적용하기 쉽게하기 

이 작업은 회원번호를 가진 모든 테이블에 해줘야 하는 작업이므로, `MemberKey` 내부에
중첩 인터페이스를 구현해서 개별 도메인 객체를 설계하는 개발자들의 작업을 도울 수 있다.

```java
public record MemberKey(long memberNo) extends Primarykey {

    public interface Aware {
        default MemberKey getMemberKey() { return new MemberKey(getMemberNo()); }
        long getMemberNo(); // lombok @Getter 가 생성해줄 것이다.

        interface Builder<B> extends Builder<B> {
            default B memberKey(MemberKey key) {
                return memberNo(key.memberNo());
            }
            B memberNo(long memberNo); // lombok @Builder 가 생성해줄 것이다.
        }
    }
}

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode
public final class Member implements DomainObject<MemberKey>, MemberKey.Aware {
    private final long memberNo; // PK
    private String name;         // semantic field
    private Date registerDate;   // semantic field
    @EqualsAndHashCode.Exclude
    private Date createDate;     // logging field

    @Override
    public MemberKey getPrimaryKey() { return getMemberKey(); }

    public static class MemberBuilder implements MemberKey.Aware.Builder<MemberBuilder> {
        public MemberBuilder primaryKey(MemberKey key) {
            return memberKey(key);
        }
    }
}
```

`MemberKey.Aware` 인터페이스는 `MemberKey`를 가지고 있는 클래스라는 의미를 전달하고,
`MemberKey.Aware.Builder` 인터페이스는 `MemberKey.Aware` 객체를 만드는 빌더라는 의미를 전달한다.

코드의 의미도 직관적으로 이해하기 쉬워 지고, 코드의 양도 줄었을 뿐만 아니라
더해서 `getMemberKey()`라는 메소드명도 통일시킬 수 있게 되었다.

`MemberKey.Aware`에서 실제로 구현해줘야 하는 `getMemberNo()`와
`MemberKey.Aware.Builder`의 `memberNo()`는 적절한 어노테이션을 붙여주면 lombok이 생성해준다.

```java
@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode
public final class Address implements DomainObject<MemberKey>, MemberKey.Aware {
    private final long memberNo;    // PK/FK
    private final long addressNo;   // PK
    private String address;         // semantic field
    @EqualsAndHashCode.Exclude
    private Date createDate;        // logging field

    @Override
    public Key getPrimaryKey() { return new Key(memberNo, addressNo); }
   
    // Address.Key도 memberNo를 가지고 있으므로, MemberKey.Aware를 구현 할 수 있다.
    public record Key(long memberNo, long addressNo)
            implements PrimaryKey, MemberKey.Aware {
        @Override
        public long getMemberNo() { return memberNo; }
    }
    
    public static class AddressBuilder implements MemberKey.Aware.Builder<AddressBuilder> {
        public AddressBuilder primaryKey(Key key) {
            return memberNo(key.memberNo())
                  .addressNo(key.addressNo());
        }
    }
}
```

`Member`와 식별관계인 자식 테이블 `Address`의 경우 `MemberKey`는 PK의 일부분을 구성한다.
따라서 `Address.Key`는 `memberNo`필드를 가지고 있으며 `MemberKey.Aware`하다.

물론 `Address.Key`가 이 인터페이스를 반드시 구현해야 할 필요는 없고
구현한다고 해서 활용도가 크게 높아지는 않을 수 있다.

비 식별관계의 `Post` 테이블의 경우 간단하게 `MemberKey.Aware`와
`MemberKey.Aware.Builder`만 구현하도록 해주면 더 추가로 할 일이 없다.

```java
@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode
public final class Post implements DomainObject<MemberKey>, MemberKey.Aware {
    private final long postNo;      // PK/FK
    private final String content;   // semantic field
    private long memberNo;          // semantic field from Member
    @EqualsAndHashCode.Exclude
    private Date createDate;        // logging field

    @Override
    public Key getPrimaryKey() { return new Key(postNo); }
   
    public record Key(long postNo) implements PrimaryKey {}
    
    public static class PostBuilder implements MemberKey.Aware.Builder<PostBuilder> {
        public PostBuilder primaryKey(Key key) {
            return postNo(key.postNo());
        }
    }
}
```

## 아이템 0.04: 원시 타입을 사용하는 경우 `null`처리를 위해 `Optional`을 고려하라

`MemberKey`는 회원테이블의 PK이므로 `Member`나 `Address`에서는 `null`이 있을 수 없다.
하지만 입력받은 데이터를 DB에 저장하기 전 단계이거나, `Post`처럼 PK가 아닌 곳에 사용되는 경우는
`null`이 있을 수 있다.

`memberNo`의 자료형을 `Long`으로 하면 `null`을 자연스럽게 처리할 수 있다.
하지만 `memberNo`는 거의 모든 테이블에서 나타날 수 있는 필드이고,
`MemberKey`는 매우 많이 사용될 객체가 될 수 있다.

조금이라도 성능 향상을 노리기 위해 원시 `long`을 사용한다고 가정하면,
`null`값을 처리하는 방법에 대해서 고민을 할 필요가 있다.
원시 자료형으로 `null`을 처리하는 방법은 여러가지가 있을 수 있지만
다음과 같은 방식으로 정책을 정했다고 가정하자.

* `memberNo`는 DB의 자동증가 시퀀스로 채번되므로 `<= 0`인 값은 있을 수 없다.
* 따라서 `memberNo <= 0`인 경우 `null`로 간주한다.
* `memberKey == null`인 경우를 표현하기 위해서는 `memberNo == 0`으로 설정한다.

`memberNo` 값에 이러한 비즈니스 로직이 들어간 이상 `MemberKey`는 더이상 단순한 값 객체가 될 수 없다.
그래서 `record`를 포기하고 `class`로 구현하게된다.
`record`는 생성자를 재정의하더라도 `private`으로 설정할 수 없다.

### `null` 처리 로직을 추가한 `MemberKey` 예시

```java
@EqualsAndHashCode
public class MemberKey implements PrimaryKey {
    public static final long NULL_MEMBER_NO = 0L;
    private long memberNo; // PK

    // 생성자 올바르지 않은 memberNo에 예외를 던진다.
    private MemberKey(long memberNo) {
        if (memberNo <= NULL_MEMBER_NO) {
            throw new IllegalArgumentException("memberNo must be greater than 0");
        }
        this.memberNo = memberNo;
    }

    // 마찬가지로 올바르지 않은 memberNo에 예외를 던진다.
    public static MemberKey of(long memberNo) {
        return new MemberKey(memberNo);
    }
    
    // 올바르지 않는 memberNo에 대해서는 Optional.empty()를 리턴한다.
    public static Optional<MemberKey> tryOf(long memberNo) {
       return Optional.ofNullable(
               (memberNo <= NULL_MEMBER_NO)? null: new MemberKey(memberNo));
    }
    
    public long longValue() {
        return memberNo;
    }
}
```

`MemberKey.of()`와 `MemberKey.longValue()`는
`MemberKey`객체와 `long`값을 서로 변환하는 캐스팅 연산자 처럼 사용할 수 있다.

다만 `MemberKey`의 경우는 `memberNo <= 0`인 `MemberKey`객체가 아예 생성될 수 없도록 한다.
이것은 엄격한 타입 관리를 위해서 좋은 선택이다.

잘못된 값이 입력되었는지 검사를 안전하게 처리하기 위해서는 `tryOf()`를 사용해서
`Optional<MemberKey>`를 받아 추가적으로 처리할 수 있다.

```java
void main() {
    MemberKey.tryOf(0).ifPresentOrElse(
        key -> System.out.println("key: " + key.longValue()),
        () -> System.out.println("key is null")
    );
    
    MemberKey keyOrNull = MemberKey.tryOf(0).orElse(null);
}
```

만약 `MemberKey.of()`가 `null`을 리턴할 수도 있고, 객체를 리턴할 수도 있다면,
필수적으로 `null`체크를 해야 한다. 하지만 업무가 바쁘면 안하게 되기 마련이다.
`Optional`을 사용하면 바빠도 할 수 밖에 없지만 `null`체크가 간편해지기 때문에 큰 부담이 없다.

이제 `MemberKey.Aware` 인터페이스의 `getMemberKey()`를 다음과 같이 변경하면
DB에서 `memberNo == null`인 데이터가 로드된 경우에 `long`타입에는 `memberNo == 0`을 입력하고,
`memberKey == null`로 자연스럽게 처리가 가능해진다.

```java
public class MemberKey {
    public interface Aware {
       default MemberKey getMemberKey() {
          return tryOf(getMemberNo()).orElse(null);
       }
    }
}
```

### 잘못된 값을 허용해야 하는 경우

하지만 DB에 이미 잘못된 값이 들어가 있고, 이를 어찌할 방법이 없는 경우가 있을 수도 있다.

```java
public class ResidentKey implements PrimaryKey, CharSequence, Comparable<ResidentKey> {

    private final String reidentId;

    /// 주민번호의 유효성을 검사하지 않고 생성한다.
    private ResidentKey(String residentId) { this.residentId = residentId; }

    /// 주민번호의 유효성을 검사하여 올바른 경우만 생성한다.
    public static ResidentKey of(String residentId) {
        return ofNullable(residentId)
           .orElseThrow(() -> new IllegalArgumentException("주민등록번호 형식이 잘못되었습니다."));
    }

    /// 유효성 검사 없이 생성한다. 기존에 DB에 저장된 값을 로드하는 경우 등 검사가 필요없는 경우 사용한다.
    public static ResidentKey ofUnchecked(String residentId) {
        return new ResidentKey(residentId);
    }

    /// 주민등록번호 유효성을 검사하여 성공적일 경우, Optional로 감싸서 반환한다.
    /// 실패할 경우 Optional.empty()를 반환한다.
    public static Optional<ResidentKey> ofNullable(String residentId) {
        if (residentId == null)
            return Optional.empty();
        else {
            String r = residentId.trim();
            if (r.length() == 14 && PATTERN_WITH_DASH.matcher(r).matches()) {
                r = r.replace("-", "");
                return (isValid(r))? Optional.of(new ResidentKey(r)): Optional.empty();
            }
            else if (r.length() == 13 && PATTERN.matcher(r).matches() && isValid(r))
                return Optional.of(new ResidentKey(r));
            else
                return Optional.empty();
        }
    }
    // ...
}
```

이럴때는 `ofUnchecked()` 같이 검사를 하지 않는 메소드를 제공하여,
잘못된 값을 가진 객체라도 생성하는 방법을 지원해야 할 수 있다.
이때 `ofUnchecked()`는 단순한 생성자의 래퍼에 불과한데 왜 생성자를 `public`으로 하지 않는가?

가장 중요한 것은 `of()`, `ofNullable()`과 라임을 맞추기 위해서이다.

라임 맞추는 것에 비하면 아주 사소하지만, 또다른 이유가 하나 더 있다.
개발자가 처음 접했을때 생성자가 `public` 이라면, 생성자를 사용하는 것이 더 정석적인 방법이라고 생각할 수 있다.
하지만 이 사례에서는 생성자를 직접 사용하는 것은 예외적인 경우이고 권장되지 않는 방법이다.

그리고 참고로 눈치 챈 사람이 있을지 모르겠으나 `ResidentKey`는 `CharSequence`를 구현하고 있다.
주민등록번호는 Structured Key면서 Semantic Key이기 때문이다.

```java
void main() {
    ResidentKey key = ResidentKey.of("1111111111118");
    System.out.println("생일: " + key.subSequence(0, 6));
}
```
