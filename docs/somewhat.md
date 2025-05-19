# Somewhat Effective Java

## 목차

* [**아이템 0.01**: 닥치고 Java 23으로 업그레이드 하라](#아이템-001-닥치고-java-23으로-업그레이드-하라)
* [**아이템 0.02**: Primary Key는 별도 타입을 만들라](#아이템-002-primary-key는-별도-타입을-만들라)

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
///     public static record Key(int no) impelements PrimaryKey, Comparable<Key> {
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
2. 의미론적 동등성(Content-based equality): 같은 의미을 나타낸다.
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
이런 경우 의미론적 동등성과 상태 동등성은 ORM 내부 처리 메커니즘과정에서만 필요하다.

하지만, SQL mapper나 JDBC를 사용하여 개발자가 직접 도메인 객체의 `equals()`를 코딩할 필요가 있는 경우,
그 대부분은 의미론적 동등성일 가능성이 높다.  

```java
/// Primary Key를 담은 DTO용 마커 인터페이스
interface PrimaryKey {}

/// DB테이블에 대응하는 도메인 객체용 인터페이스
interface DomainObject<K extends PrimaryKey> {
    K getPrimaryKey();
    boolean equalsKeyOnly(Object o);    // 식별자 동등성
    boolean equals(Object o);           // 의미론적 동등성
    boolean equalsAllFields(Object o);  // 상태 동등성
}
```

`equalsKeyOnly()`와 `equalsAllFields()`는 미리 `default` 메소드로 구현해서 개발자가 다시 구현할 필요가 없게 할 수 있다.

```java
interface DomainObject<K extends PrimaryKey> {
    K getPrimaryKey();          // 직접 구현 필요
    boolean equals(Object o);   // 직접 구현 필요
    default boolean equalsKeyOnly(Object o) {
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

    public static record Key(int no) implements PrimaryKey {}

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
public static record Key(int no) implements PrimaryKey, Comparable<Key> {
    @Override
    public int compareTo(@NotNull Key o) {
        return Integer.compare(this.no, o.no());
    }
}

void main() {
    Map<SimpleKeyExample.Key, SimpleKeyExample> map = new TreeMap<>();
    map.put(new SimpleKeyExample.Key(1), new SimpleKeyExample());
}
```

### 복합 키 예시

테이블의 PK가 한개 이상의 컬럼으로 구성된 경우도 흔하다. 이런 경우 `PrimaryKey`가 단일 값으로 취급될 수 있도록
`getPrimaryKey()`에 대응하는 `setPrimaryKey()` 메소드를 반드시 구현해 주어야 할 것이다.

불변 객체라면 `@Builder`가 생성하는 `클래스명+Builder` 클래스에 빌드 메소드를 미리 추가해주어면 된다.

```java
@Getter
@Builder(toBuilder = true) // @Setter 대신 @Builder 사용
public final class ComplexKeyExample implements DomainObject<ComplexKeyExample.Key> {
    private final int keyPart1;      // primary key
    private final String keyPart2;   // primary key
    // ...
    public static record Key(int keyPart1, String keyPart2) {}
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
            .primaryKey(otherExampleObj.getPrimaryKey())  // PK 객체로 객체 생성
            .otherField1("field1")
            .otherField2("field2").build();
}
```
