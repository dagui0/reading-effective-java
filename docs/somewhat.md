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

### 동등성(Equality)의 의미

두 객체가 같다(`a.equals(b)`)고 할 때 무엇이 같은 것인가?

* 식별자 기반 동등성(Identifier-based equality): 같은 대상을 나타낸다.
* 의미론적 동등성(Content-based equality): 같은 의미을 나타낸다.
* 완전한 상태 동등성(Full state equality): 상태를 포함한 모든 필드가 같다.

