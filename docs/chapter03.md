# 이펙티브 자바 (3판) - 3장 모든 객체의 공통 메서드

## 목차

* [**아이템 10**: `equals`는 일반 규약을 지켜 재정의하라](#아이템-10-equals는-일반-규약을-지켜-재정의하라)
  * [**[추가]** `equals()`관련 토론 주제: 어디까지 비교할 것인가?](#추가-equals관련-토론-주제-어디까지-비교할-것인가)
* [**아이템 11**: `equals`를 재정의하려거든 `hashCode`도 재정의하라](#아이템-11-equals를-재정의하려거든-hashcode도-재정의하라)
  * [**[추가]** `HashCodeBuilder` 유틸리티](#추가-hashcodebuilder-유틸리티)
* [**아이템 12**: `toString`을 항상 재정의하라](#아이템-12-tostring을-항상-재정의하라)
* [**아이템 13**: `clone` 재정의는 주의해서 진행하라](#아이템-13-clone-재정의는-주의해서-진행하라)
  * [**[추가]** 깊은 복사(deep copy)가 문제되는 경우](#추가-깊은-복사deep-copy가-문제되는-경우)
  * [**[추가]** `clone()` 관련 토론 주제: Prototype 패턴이 의미가 있는가?](#추가-clone-관련-토론-주제-prototype-패턴이-의미가-있는가)
* [**아이템 14**: `Comparable`을 구현할지 고려하라](#아이템-14-comparable을-구현할지-고려하라)
  * [**[추가]** `CompareToBuilder` 유틸리티](#추가-comparetobuilder-유틸리티)
* [**[추가]** Pattern variable](#추가-pattern-variable)
* [**[추가]** Record Class](#추가-record-class)

## 아이템 10: `equals`는 일반 규약을 지켜 재정의하라

### `equals()` 를 재정의 하지 않아도 되는 경우

`java.lang.Object`의 `equals()` 메서드는 완전히 동일한 객체인지만을 비교한다.
다음 경우가 아니라면 반드시 재정의를 고려해야 한다.

* 각각의 객체가 고유한 인스턴스인 경우
  * 활성 개체를 나타내는 클래스 (eg: `Thread`)
* 클래스가 논리적 동일성(logical equality)을 확인할 필요가 없는 경우
  * `java.util.Random`, `java.util.regex.Pattern` 클래스 등 `equals()`를 호출할것 같지 않은 클래스
* 상위 클래스에서 재정의한 `equals()`가 하위 클래스에서 적절하게 동작하는 경우
* 클래스가 private 또는 package-private 이고 `equals()`를 호출할 일이 없는 경우. \
  하지만 나는 이러한 경우라도 `equals()`를 재정의한다.
  ```java
  @Override
  public boolean equals(Object o) {
      throw new AssertionError(); // 호출 금지
  }
  ```

### 남자의, 아니 `equals()`의 자격

`equals()`는 동치관계(equivalence)를 구현하는데, 다음 조건을 만족한다는 의미이다.

* **반사성(reflexive)**: null이 아닌 모든 참조 값 x에 대해, `x.equals(x)`는 true이다.
* **대칭성(symmetric)**: null이 아닌 모든 참조 값 x, y에 대해, `x.equals(y)`가 true이면 `y.equals(x)`도 true이다.
* **추이성(transitive)**: null이 아닌 모든 참조 값 x, y, z에 대해, `x.equals(y)`가 true이고 `y.equals(z)`가 true이면 `x.equals(z)`도 true이다.
* **일관성(consistency)**: 객체의 필드값이 변경되지 않았다면 값 x, y에 대해, `x.equals(y)`를 반복해서 호출해도 항상 같은 값을 반환한다.
* **`null` 아님**: `null`이 아닌 참조 값 x에 대해, `x.equals(null)`은 false이다.

#### 반사성(reflexive) - `x.equals(x)`는 true이다.

이 규칙을 깨트리는 `equals()`를 만들기는 쉽지 않다.

#### 대칭성(symmetric) - `x.equals(y)`가 true이면 `y.equals(x)`도 true이다.

이 규칙은 주의하지 않으면 쉽게 깨질 수 있다.

다음 `CaseInsensitiveString` 예시는 `String`과 호환성을 가져가기 위해서 `equals()`에 `String`객체와 비교를 추가했지만
이로 인해 대칭성을 위반하게 된다. 
`CaseInsensitiveString.equals(String)`이 참일 경우에도 `String.equals(CaseInsensitiveString)`는 항상 거짓이 되기 때문이다. [CaseInsensitiveStringTest.java](../src/test/java/effectivejava/chapter03/item10/CaseInsensitiveStringTest.java)

```java
public class CaseInsensitiveString1 {
    private final String s;

    public CaseInsensitiveString1(String s) {
        this.s = s;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CaseInsensitiveString1) {
            return s.equalsIgnoreCase(((CaseInsensitiveString1)o).s);
        }
        if (o instanceof String) {
            return s.equalsIgnoreCase((String)o);
        }
        return false;
    }
}
```

#### 추이성(transitive) - `x.equals(y)`가 true이고 `y.equals(z)`가 true이면 `x.equals(z)`도 true이다.

클래스 상속의 정의상 근본적으로 `equals()` 원칙과 충돌되는 문제가 있다.

* 하위클래스는 상위 클래스와 동등하게 취급될 수 있어야 한다.
* 상위클래스는 하위클래스를 대체할 수 없다.

`x`, `y` 필드를 가지는  `Point` 클래스와 이를 상속하여 필드를 추가하는 `ColorPoint`의 예시

* 예제: [Pointy1.java](../src/main/java/effectivejava/chapter03/item10/Point1.java)
* 예제: [ColorPoint1.java](../src/main/java/effectivejava/chapter03/item10/ColorPoint1.java)
* 예제: [ColorPointTest.java](../src/test/java/effectivejava/chapter03/item10/ColorPointTest.java)

```java
@Override
public boolean equals(Object o) {
    if (!(o instanceof ColorPoint1))
        return false;
    ColorPoint1 p = (ColorPoint1)o;
    return super.equals(o) && color.equals(p.color);
}

@Test
public void testColorPoint1EqualsWithPointSymmetry() {
    int x = 10, y = 11;
    Point1 point = new Point1(x, y);
    ColorPoint1 redPoint = new ColorPoint1(x, y, Color.RED);
  
    assertEquals(point, redPoint);      // Point1.equals(ColorPoint1) == true
    assertNotEquals(redPoint, point);   // ColorPoint1.equals(Point1) != true
}
```

억지로 상위 클래스의 대칭성을 만족 시킬 경우 추이성을 만족시킬 수 없다.

* 예제: [ColorPoint2.java](../src/main/java/effectivejava/chapter03/item10/ColorPoint2.java)
* 예제: [ColorPointTest.java](../src/test/java/effectivejava/chapter03/item10/ColorPointTest.java)

```java
@Override
public boolean equals(Object o) {
    if (!(o instanceof Point1))
        return false;

    if (!(o instanceof ColorPoint2))
        return o.equals(this);

    ColorPoint2 p = (ColorPoint2)o;
    return super.equals(o) && color.equals(p.color);
}

@Test
public void testColorPoint2EqualsWithPointTransitivity() {
  int x = 10, y = 11;
  Point1 point = new Point1(x, y);
  ColorPoint2 redPoint = new ColorPoint2(x, y, Color.RED);
  ColorPoint2 bluePoint = new ColorPoint2(x, y, Color.BLUE);

  assertEquals(redPoint, point);
  assertEquals(bluePoint, point);
  assertNotEquals(redPoint, bluePoint);
}
```

상속과 관련된 문제를 해결하기 위해 `instanceof` 대신에 `getClass() == o.getClass()`를 사용하는 것도 생각해 볼 수 있으나,
또 다른 미묘한 문제를 발생시킬 수 있다. ([리스코프 치환 원칙](https://inpa.tistory.com/entry/OOP-%F0%9F%92%A0-%EC%95%84%EC%A3%BC-%EC%89%BD%EA%B2%8C-%EC%9D%B4%ED%95%B4%ED%95%98%EB%8A%94-LSP-%EB%A6%AC%EC%8A%A4%EC%BD%94%ED%94%84-%EC%B9%98%ED%99%98-%EC%9B%90%EC%B9%99) 위반)

이 경우 가장 바람직한 방법은 `Point` 상속을 포기하고 HAS-A 관계로 복합(composite) 객체를 만드는 것이다.
([아이템 18](chapter04.md#아이템-18-상속보다는-컴포지션을-사용하라))

* 예제: [Point.java](../src/main/java/effectivejava/chapter03/item10/Point.java)
* 예제: [ColorPoint.java](../src/main/java/effectivejava/chapter03/item10/ColorPoint.java)

```java
public class ColorPoint {
    private final Point point;
    private final Color color;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ColorPoint))
            return false;
        ColorPoint p = (ColorPoint) o;
        return point.equals(p.point) && color.equals(p.color);
    }
}
```

값 객체를 상속을 하면서 필드가 추가 되는 경우 문제의 대표적인 사례는 `java.util.Date`를 상속하는 `java.sql.Timestamp`이다.
`Timestamp.equals()`는 대칭성을 위반한다. `Timestamp`의 문서는 `Date` 섞어 쓰지 말 것을 권고하고 있다.
무신경하게 섞어 사용할 경우 미묘한 버그가 발생할 수 있을 것이다.


#### 일관성(consistency) - 객체의 필드값이 변경되지 않았다면 값 x, y에 대해, `x.equals(y)`를 반복해서 호출해도 항상 같은 값을 반환한다.

* 변경 가능한 객체는 시간에 따라서 동치관계가 달라질 수 있다. 반면 불변객체(immutable)는 걱정할 필요가 없다.
  ([아이템 17](chapter04.md#아이템-17-변경-가능성을-최소화하라))
* 신뢰성이 보장되지 않는 자원에 대한 비교를 피해야 한다. (eg: `java.net.URL`은 IP 주소를 확인하기 위해 시스템 호출을 사용하므로 일관성에 문제가 생길 수 있다.)

#### `null`에 대한 비동치성 - `null`이 아닌 참조 값 x에 대해, `x.equals(null)`은 false이다.

`instanceof`에서 첫번째 연산자가 `null`이면 무조건 `false`를 리턴하므로 타입 체크만 하면 쉽게 달성 가능하다. 

```java
@Override
public boolean equals(Object o) {
    // ...
    if (!(o instanceof ColorPoint))
      return false;
    // ...
}
```

### 결론: 바람직한 `equals`

```java
@Override
boolean equals(Object o) {
    // 1. 자기 자신과 비교
    if (o == this)
        return true;
    // 2. 타입 체크
    if (!(o instanceof MyType))
        return false;
    // 3. 타입 변환
    MyType mt = (MyType) o;
    // 4. 각 필드 비교
    return field1 == mt.field1 &&  // 더 자주 다를 것 같은 필드를 먼저 검사
        (field2 == mt.field2 || (field2 != null && field2.equals(mt.field2))) && // null이 될 수 있는 필드는 null 체크 포함
        Double.compare(field3, mt.field3) == 0; // float, double은 정적 메서드를 사용하여 비교
}
```

`equals()`관련 추가적인 지침들

* `float`, `double`을 비교할 때는 == 대신에 `Float.compare()`, `Double.compare()`를 사용하라
* `null` 일 수 있는 필드를 비교할 때는 [`Objects.equals()`](https://docs.oracle.com/javase/8/docs/api/java/util/Objects.html)를 사용하라. (Java 7 부터 추가)
* `equals`를 재정의할 때는 반드시 `hashCode`도 재정의하라. [아이템 11](#아이템-11-equals를-재정의하려거든-hashcode도-재정의하라)
* `@Override public boolean equals(Object o)` 시그니처를 변경하지 마라. 인자 자료형을 바꾸게 되면 재정의(override)가 안될 수 있다.
* 과도한 동치성에 집착하지 마라. [토론 필요](#equals관련-토론-주제)

### `equals()` 구현을 위한 보조적인 도구 (`hashCode()`도 같이 해결됨)

* [구글 AutoValue](https://www.baeldung.com/introduction-to-autovalue)
* [Lombok](https://projectlombok.org/features/)
* [레코드 record class](#추가-record-class)

### [추가] `equals()`관련 토론 주제: 어디까지 비교할 것인가?

다음과 같은 테이블에 대한 DTO(Data Transfer Object)가 있다고 할 때,
`equals()`에서는 `regDate`, `modDate`를 비교할 필요가 있을까?

```sql
CREATE TABLE MEMBER (
    ID      INTEGER PRIMARY KEY,
    NAME    VARCHAR(20),
    REG_DT  DATETIME,
    MOD_DT  DATETIME
);
```

```java
public class Member {
    private int id;
    private String name;
    private Date regDate;
    private Date modDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return id == member.id &&
                Objects.equals(name, member.name) &&
                Objects.equals(regDate, member.regDate) &&
                Objects.equals(modDate, member.modDate);
    }
}
```

스터디 멤버 의견:

* Alejandro: 비교하면 안된다. 관리상 기록을 위한 것이지 객체의 속성이라고 볼 수 없음
* Leeturn: 모두 비교해야 pure java지만 관리용 날짜는 빼는것이 적절함
* Scully: 생성 수정 날짜는 비교에서 빼야함
* Lucie: 오딧 날짜는 빼고 비교해야함

## 아이템 11: `equals`를 재정의하려거든 `hashCode`도 재정의하라

값 객체(Value Object)를 만들 때는 `Hashtable`, `HashMap`, `HashSet` 등의
해시 기반 컬렉션은 사용하게 되기 마련이므로 무조건 `hashCode()`를 재정의 해야 한다.

### `hashCode()` 규약

* `equals() == true` 면, 두 객체의 `hashCode()` 값은 같아야 한다.
* `equals() == false` 면, 두 객체의 `hashCode()` 값은 달라야 한다. 하지만 알고리즘상 중복될 수 있으므로 반드시는 아니다.
* 다만 프로그램이 종료되었다가 새로 시작되었을 때 까지 같을 필요는 없다.

`Object.hashCode()`는 동일한 객체인 경우(`Object.equals()`의 결과로)만 같은 값을 가지게 구현되어있다.
따라서 `equals()`를 재정의한 클래스는 `hashCode()`도 재정의 해야 한다.

`hashCode()`를 재정의 하지 않은 경우:

* 예제: [PhoneNumber1.java](../src/main/java/effectivejava/chapter03/item11/PhoneNumber1.java)
* 예제: [PhoneNumberTest.java](../src/test/java/effectivejava/chapter03/item11/PhoneNumberTest.java)

```java
@Test
public void testPhoneNumber1HashMapInsert() {
    Map<PhoneNumber1, String> map = new HashMap<>();
    PhoneNumber1 n1 = new PhoneNumber1(10, 123, 4567);
    PhoneNumber1 n2 = new PhoneNumber1(10, 123, 4567);
  
    map.put(n1, "Jenny");
  
    assertNull(map.get(n2));
    assertFalse(map.containsKey(n2));
}
```

### `hashCode()` 작성 요령

* `0`이 아닌 상수로 시작한다. (eg: 17) (이 항목은 2판에는 있었는데 3판에서 없어짐)
* 상수값은 소수(prime number)인 것이 좋다.
* `equals()`에 사용된 각 필드의 `hashCode`를 더한다.
  * 더할 때는 현재 해시값에 일정한 상수를 곱한 후 더한다. (eg: `result = 31 * result + field.hashCode();`)
  * 곱셈을 하고 더하는 이유는 필드의 해시값이 0인 경우 해시값에 아무런 변화가 없기 때문(필드 값이 해시에 기여를 못함)
  * 곱셈을 할 상수는 홀수여야 좋고, 보통은 소수를 선택한다. (`31 * result`는 `result << 5 - result`와 같다.)
* 자료형 별 해시값 계산 방법
  * **boolean**: `(field ? 1 : 0)`
  * **byte, char, short, int**: `(int) field`
  * **long**: `(int) (field ^ (field >>> 32))`
  * **float**: `Float.floatToIntBits(field)`
  * **double**: `Double.doubleToLongBits(field)` 후 `long` 해시값 계산
  * **Object**: `field == null? 0: field.hashCode()`
* 해시코드를 계산하는 비용이 높은 경우, 사전 계산을 하거나 결과를 캐싱하거나 할 수는 있지만 필드를 생략해서는 안된다.
  * 사전 계산 대신 지연 초기화(lazy initialization)을 사용할 수 있지만 신중하게 ([아이템 83](chapter11.md#아이템-83-지연-초기화는-신중히-사용하라))

```java
public class PhoneNumber {
    private volatile int hashCode;  // volatile (규칙 71)
    
    @Override
    public int hashCode() {
        int r = hashCode;
        if (r == 0) {
            r = 17;
            r = 31 * r + areaCode;
            r = 31 * r + prefix;
            r = 31 * r + lineNumber;
            hashCode = r;
        }
        return r;
    }
}
```

### 주의 사항

* `hashCode()`의 성능을 높인다고 핵심 필드 계산을 생략해서는 안된다.
* `hashCode()`의 계산공식을 문서에 설명하지마라. 클라이언트가 의존하게 되면 변경할 수 없다.

### `hashCode()` 구현을 위한 보조적인 도구

* [`Objects.hash()`](https://docs.oracle.com/javase/8/docs/api/java/util/Objects.html#hash-java.lang.Object...-) \
  - `Object[]`로 전달해야 해서 박싱을 사용하게 되므로 느릴 수 있다.
* 더 복잡한 해싱이 필요한 경우 [구아바(Guava)](https://github.com/google/guava)의 `com.google.common.hash.Hashing` 참조

### [추가] `HashCodeBuilder` 유틸리티

위 방법을 적용한 `HashCodeBuilder` 유틸리티

* 예제: [HashCodeBuilder.java](../src/main/java/com/yidigun/utils/HashCodeBuilder.java)
* 예제: [HashCodeBuilderTest.java](../src/test/java/com/yidigun/utils/HashCodeBuilderTest.java)

```java
class Test1 {
    int a = 0, b = 0;

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(a)
                .append(b)
                .calculateHashCode();
    }
}
```

## 아이템 12: `toString`을 항상 재정의하라

* `toString()`을 잘 만들어 놓으면 클래스를 좀 더 쾌적하게 사용할 수 있다.
* 모든 주요 정보를 포함시키는 것이 좋다. 그래야 디버깅하기 편하다.
  ```
  Assertion failure: expected {abc, 123}, but was {abc, 123}.
  ```
* getter 메소드를 이용해서 얻을 수 없는 정보는 `toString()`에 포함 시켜서는 안된다.
  * 그렇지 않다면 개발자들은 `toString()`의 결과를 파싱하려고 할 것이고, 문자혈 형식을 바꾸기 어려워질 것이다.
* 어떤 방식으로 구현하건 간에 `toString()`의 출력에 대한 내용을 명확하게 문서화 해야 한다.
  1. 이러저러한 형식으로 문자열로 변환된다고 명시
     * 해당 형식 문자열을 파싱하는 기능을 통해 완전히 문자열과 호환되게 하는 방법도 있을 수 있다.
  2. 문자열 형식은 큰 의미 없고 향후 변경될 수 있음을 명시적으로 경고

## 아이템 13: `clone` 재정의는 주의해서 진행하라

### 선 결론: `Cloneable` 인터페이스는 문제가 많으므로 지양하라

* 그럼에도 불구하고 널리 사용되고 있으므로 문제점과 사용법을 알아놓을 필요가 있다.
* `Cloneable`과 `clone()`의 동작 방식을 잘 알게 되면, 웬만해선 사용하지 않을거라 믿는다.

#### `Cloneable` 인터페이스와 `Object.clone()`의 문제점

* `Cloneable`은 복제해도 되는 클래스임을 명시하는 믹스인 인터페이스(mixin interface, [아이템 20](chapter04.md#아이템-20-추상-클래스보다는-인터페이스를-우선하라))이지만 망했다.
  * `Comparable.compareTo()` 같은 다른 인터페이스와 동작 방식이 매우 다르다.
  * `Cloneable` 인터페이스에는 `clone()` 메소드가 없다.
    * 그래서 `Cloneable`을 구현하고나면 `Object`의 `protected Object clone()`을 `public`으로 재정의할 필요가 있다.
* `Cloneable` 인터페이스는 `Object.clone()` 메서드가 호출되었을 때 복제를 할지 말지 결정하게 하는 마커(marker)임
  * `Object.clone()`(많은 경우 하위 클래스에 의해서 `super.clone()`)이 호출되면, \
    현재 클래스(`Object`가 아니고 호출된 하위 클래스)가 `Cloneable` 인터페이스를 구현한 경우 복제본을 리턴한다.
  * 현재 클래스가 `Cloneable`이 아니면 `CloneNotSupportedException`을 던진다.
* `CloneNotSupportedException` 은 checked 예외이므로 `clone()`을 호출할때 반드시 `try-catch`가 필요하다.
  * 하지만 `Cloneable`을 구현한 이상 실제로 던져지지 않기 때문에 불필요한 예외처리이다.
* `Object.clone()`은 얕은 복사(shallow copy)를 수행한다.
  * 객체의 필드가 참조 타입인 경우, 참조된 객체는 복제되지 않고 참조만 복사된다.
  * 따라서 참조된 객체가 변경되면 복제본도 영향을 받는다.
  * 복합(composite) 객체의 경우 반드시 깊은 복사(deep copy)를 수행하도록 `clone()`을 재정의해야 한다.
* `Object.clone()`은 생성자를 호출하지 않는다.
  * 생성자가 필요한 초기화 작업이 있는 경우, 복제본을 생성한 후에 초기화 작업을 수행해야 한다.
  * 이것은 상속트리가 깊어서 복잡한 단계의 `super()`를 통해 초기화되는 경우 문제가 발생할 가능성이 높다.
  * 복제되더라도 다른 값을 가져야 하는 필드인데 `final`인 경우 등의 경우 변경할 방법이 없다. \
    `final`한 필드라도 `final`로 선언하면 안된다. (eg: 객체 생성 일련번호, 객체 생성 타임스탬프)
* 만약 `clone()`을 재정의하여 생성자를 사용하도록 한다면, 하위클래스를 만들었을때 문제가 발생하게 된다.
  * `Object.clone()`은 호출한 자식 클래스 인스턴스를 리턴하지만 생성자를 사용하면 사용된 생성자의 클래스가 리턴되기때문. 
  * 결국에는 모든 하위 클래스도 자신의 생성자를 사용하여 재정의할 수 밖에 없다.
* `clone()`을 재정의 하는 경우에 상속 트리상의 모든 클래스들이
  각자 자신에게 맞는 제대로된 `clone()`을 재정의 해야한다. 

### 그나마 올바른 `Cloneable` 사용법

* 상속해서 사용하기 위한 클래스[(아이템 19)](chapter04.md#아이템-19-상속을-고려해-설계하고-문서화하라-그러지-않았다면-상속을-금지하라)의 경우 `Cloneable`을 구현하지 말아야 한다.
  * 제대로된 `private T clone()`을 구현해 놓고 자식 클래스가 `Cloneable`을 구현할지 말지 선택할 수 있게 해야한다.
* 반드시 `clone()`을 재정의한다. (아래 내용은 모두 상위 클래스의 메소드를 재정의할 때 변경 가능한 것들이다.)
  * 접근 제한자를 `public`으로 변경한다.
  * 리턴형을 클래스 타입으로 변경한다.
  * `CloneNotSupportedException`을 던지지 않도록 한다.
* 그나마 올바른 `clone()`의 구현 방법
  * `super.clone()` 호출
  * 객체 필드에 대해서 깊은 복사(deep copy) 수행
    * 불변 객체에 대해서는 복사하지 않을 수 있다. (eg: String)
    * 멤버 객체: `clone()`을 하여 재설정해줘야 한다. (`Cloneable`한 객체의 멤버들도 `Cloneable`해야 함)
    * 멤버 객체가 또 복합 객체인 경우 재귀적으로 깊은 복사가 필요하다.
      * 배열: 배열 객체는 기본적으로 `Cloneable`하지만 `clone()`과 `Arrays.copyOf()`은 얕은 복사(shallow copy)이므로 루프를 돌면서 각 원소에 `clone()`을 사용해야 한다.
        * 그러려면 배열의 원소도 `Cloneable`을 구현해야 한다.
      * Collection API: 모두 `Cloneable`을 구현하고 있으므로 `ArrayList.clone()`, `HashMap.clone()` 등 사용 가능 하지만 역시 얕은 복사이므로 루프를 사용해야 한다.

### `clone()`같은 것이 필요할때 가장 좋은 방법

* 복사 생성자를 제공 또는 내부적으로는 생성자를 사용하는 팩토리 메소드를 제공
* 생성자를 사용하므로 `final` 필드와 이슈가 없음
* 생성자나 팩토리 메소드는 인터페이스를 인자로 받을 수 있음
  * 변환 생성자(conversion constructor), 변환 팩토리(conversion factory)라고 한다.
  * `treeSet = new TreeSet(hashSet)` 같이 호환되는 다른 객체로 부터 변환 생성 가능

```java
public class PhoneNumber {
    private final short areaCode, prefix, lineNumber;
    public PhoneNumber(int areaCode, int prefix, int lineNumber) {
        this.areaCode = areaCode;
        this.prefix = prefix;
        this.lineNumber = lineNumber;
    }
    public PhoneNumber(PhoneNumber pn) {
        this.areaCode = pn.areaCode;
        this.prefix = pn.prefix;
        this.lineNumber = pn.lineNumber;
    }
    public static PhoneNumber newInstance(PhoneNumber pn) {
        return new PhoneNumber(pn.areaCode, pn.prefix, pn.lineNumber);
    }
}
```

### 결론

* `Cloneable`과 `clone()`은 문제가 많으므로 쓰지 마라. 절대 쓰지 마라.

### [추가] 깊은 복사(deep copy)가 문제되는 경우

만약 같은 객체가 2번 추가되어있는 경우, 깊은 복사를 하는 과정에서 2번 복제되어서 원본과 다른 동작을 하게 될 수 있다.

예제: [DeepCopyTest.java](../src/test/java/effectivejava/chapter03/item13/DeepCopyTest.java)

```java
@Test
public void testDeepCopyArrayListHavingDuplicatedElements() {

    // 원본 리스트는 같은 객체가 2번 추가 되어 있음
    ArrayList<Member> members = new ArrayList<>();
    members.add(new Member("Alejandro"));
    members.add(members.get(0));

    // 복제된 리스트는 복제되면서 다른 객체가 2번 추가됨
    ArrayList<Member> newList = new ArrayList<>();
    for (Member member : members) {
        newList.add(member.clone());
    }

    // 원본: 같은 객체
    assertSame(members.get(0).getName(), members.get(1).getName());

    // 카피본: 다른 객체
    assertNotSame(newList.get(0).getName(), newList.get(1).getName());
}
```

이 문제가 만약 중요한 상황이라면 해결이 쉽지 않은데 비용이 높을것이 예상되지만 직렬화(serialize)를 통해 가능하긴 하다.

```java
private <T extends Serializable> T deepCopyUsingSerialize(T o) {
    try {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(o);
        out.close();

        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));

        @SuppressWarnings("unchecked")
        T copy = (T) in.readObject();
        in.close();

        return copy;
    } catch (IOException | ClassNotFoundException e) {
        throw new IllegalStateException(e);
    }
}
```

### [추가] `clone()` 관련 토론 주제: Prototype 패턴이 의미가 있는가?

GoF의 [Prototype 패턴](https://tmd8633.tistory.com/26)은 복제(clone)를 통해 객체를 생성하는 방법을 제시한다.

* Java의 `clone()`이 문제가 많아서 사용이 권장되지 않는다.
* `Object.clone()`, `Arrays.copyOf()`(내부적으로는 `System.arraycopy()`)은 native 코드이므로 빠르고 효율적이라고 가정하더라도 언어가 커버해주는 범위가 너무 적은듯 하다
* 실제로 Prototype 패턴이 유의미한 경우가 있을까?
  * 블로그 등에서는 DB에서 조회한 객체는 초기화 비용이 높으므로 유의미하다고 주장하지만, DB 처리 프로그램에서 데이터 객체의 복제가 필요한 경우는 보지 못했다.
  * GUI 프로그램 같은 경우 의미가 있는 경우가 있을까?

스터디 멤버 의견:

* Alejandro: 
* Leeturn: 
* Scully: 
* Lucie: 

## 아이템 14: `Comparable`을 구현할지 고려하라

* `compareTo()` 메소드는 `equals()`와 비슷한 역할을 하지만, `Object`에 기본으로 존재하지 않는다.
* 순서를 정할 수 있는 값 객체(value object)의 경우 `Comparable`과 `compareTo()`를 구현하는 것이 좋다.
* 정렬에 사용할 수 있을 뿐만 아니라 중복 제거에도 사용될 수 있다. (`Set.sort()`)

### `compareTo()` 의 요건

`compareTo()` 구현 방법은 `equals()`와 비슷한 부분이 있다.

* 두 값이 같을 경우: `0`
* 내가 작을 경우(this < o): 양수(`-1`)
* 내가 클 경우(this > o): 음수(`1`)
* 많은 경우 `-1`, `0`, `1` 이겠지만, 규약상 "음수", `0`, "양수"라는 것을 명심해야 한다.
  * `a.compareTo(b) == -1`는 문제가 될 수 있다. `a.compareTo(b) < 0`으로 사용할 것

`sign()`은 정수값의 부호를 리턴함 -1, 0, 1:

* **대칭성(symmetric)**: `sign(x.compareTo(y)) == sign(y.compareTo(x))`
* **추이성(transitive)**: `x.compareTo(y) > 0 && y.compareTo(z) > 0`면 `x.compareTo(z) > 0`
* **일관성(consistency)**: `x.compareTo(y) == 0`이면 `sign(x.compareTo(z)) == sign(y.compareTo(z))`
* `this.compareTo(o) == 0`라면 논리상 `this.equals(o) == true` 라야 하지만, 그리고 강력히 권장하지만 필수는 아님
  * `compareTo()`와 `equals()`의 결과가 동일하게 나오도록 구현하는 것이 좋다.(라고 쓰고 반드시라고 읽는다)
  * Collection API의 일부 클래스는 동일성 비교를 위해 `equals()` 대신 `compareTo() == 0`을 사용한다. (`TreeSet` 등)
  * 만약 다르게 구현되어야 한다면 반드시 문서화할것 (eg: `BigDecimal`) \
    ([ComparableTest.java](../src/test/java/effectivejava/chapter03/item14/ComparableTest.java) testBigDecimalEquality())

### `compareTo()` 구현 방법

* `public <T> T int compareTo(T o)` 이므로 타입 캐스팅이 필요 없고, `instanceof`도 필요 없다.
  * 클래스가 다르면 무조건 `ClassCastException` 됨
* `hashCode()` 가 제대로 구현되지 않으면 컬렉션 사용시 문제가 되는 것 처럼, `compareTo()` 마찬가지 문제가 될 수 있다.
* `equals()` 사례 처럼 `Comparable` 구현한 클래스의 서브클래싱도 마찬가지로 문제가 된다.
  * 복합 객체(composite, HAS-A 관계)를 사용하여 확장할 것
* 자료형 별 비교 방법(3판)
  * 기본 자료형 클래스의 `compare()`를 사용하라. (`Integer.compare()`, `Short.compare()`, `Double.compare()`등)
* 자료형 별 비교 방법(2판)
  * 정수형 필드의 경우 `this.value - that.value` 연산을 통해서 효율적으로 계산할 수 있지만, overflow를 주의해야 한다.
    * 두 값의 차이가 `Integer.MAX_VALUE` 보다 작은 경우에만 사용 가능하다. \
      ([ComparableTest.java](../src/test/java/effectivejava/chapter03/item14/ComparableTest.java) testIntegerCompareToOverflow())
  * `float`, `double`은 `Float.compare()`, `Double.compare()`를 사용한다.
  * 객체형 필드는 `compareTo()`를 호출한다.
* `compareTo()`는 `equals()`와 다르게 `null`을 받을 수 없다. `null`이 전달되면 `NullPointerException`을 던져야 한다. 
  ([`Comparable` 문서상 명시된 규정](https://docs.oracle.com/javase/8/docs/api/java/lang/Comparable.html))
  * 반면 `Comparator.compare()`는 **OPTIONALLY** `null`을 받도록 구현될 수 있다. 모든 `Comparator`가 그렇다는 것은 아님
    ([`Comparator` 문서상 명시된 규정](https://docs.oracle.com/javase/8/docs/api/java/lang/Comparable.html))

### Java 8 `Comparator` 체인 방식

깔끔하고 매력적인 방법이지만 약 10% 느리다ㅠ

```java
private static final Comparator<PhoneNumber> COMPARATOR =
        comparingInt((PhoneNumber pn) -> pn.areaCode)
                .thenComparingInt(pn -> pn.prefix)
                .thenComparingInt(pn -> pn.lineNUm);
```

### [추가] `CompareToBuilder` 유틸리티

위 방법을 적용한 `CompareToBuilder` 유틸리티

* 예제: [CompareToBuilder.java](../src/main/java/com/yidigun/utils/CompareToBuilder.java)
* 예제: [CompareToBuilderTest.java](../src/test/java/com/yidigun/utils/CompareToBuilderTest.java)

```java
class Member implements Comparable<Member> {
    private String name;
    private int age;
    private String job;
  
    @Override
    public int compareTo(Member o) {
        return new CompareToBuilder()
              .compare(name, o.name)
              .compare(age, o.age)
              .compare(job, o.job)
              .toComparison();
    }
}
```

## [추가] Pattern variable

[Pattern Matching for instanceof](https://velog.io/@gkskaks1004/Java-16%EC%97%90%EC%84%9C-instanceof-%EC%97%B0%EC%82%B0%EC%9E%90%EC%97%90-%EB%8C%80%ED%95%9C-pattern-matching) Java 16부터 추가

`instanceof` 검사하고 나면 캐스팅(Type casting)이 따라오는게 인지상정!

```java
@Override
public boolean equals(Object o) {
    // ...
    if (!(o instanceof Point point))
        return false;
    return x == point.x && y == point.y; 
}
```

## [추가] Record Class

불변(immutable) 객체를 쉽게 생성할 수 있도록 하는 [Record Class](https://mr-popo.tistory.com/243) Java 16부터 정식 추가.

* 모든 필드 `private final` 선언
* 필드 값을 모두 포함한 생성자 자동 생성
* getters 메서드 자동 생성
* `equals()` 자동 생성
* `hashCode()` 자동 생성
* `toString()` 자동 생성

```java
public record Student(String name, int age) {
    // 내용은 알아서 생성해줌
}
```
