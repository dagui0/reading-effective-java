# 이펙티브 자바 (2판) - 3장 모든 객체의 공통 메서드

## 목차

* [**규칙 8**: `equals`를 재정의할 때는 일반 규약을 따르라](#규칙-8-equals를-재정의할-때는-일반-규약을-따르라)
* [**규칙 9**: `equals`를 재정의할 때는 반드시 `hashCode`도 재정의하라](#규칙-9-equals를-재정의할-때는-반드시-hashcode도-재정의하라)
* [**규칙 10**: `toString`은 항상 재정의하라](#규칙-10-tostring은-항상-재정의하라)
* [**규칙 11**: `clone`을 재정의할 때는 신중하라](#규칙-11-clone을-재정의할-때는-신중하라)
* [**규칙 12**: `Comparable` 구현을 고려하라](#규칙-12-comparable-구현을-고려하라)
* [**[추가]** Pattern variable](#추가-pattern-variable)
* [**[추가]** Record Class](#추가-record-class)

## 규칙 8: `equals`를 재정의할 때는 일반 규약을 따르라

### `equals()` 를 재정의 하지 않아도 되는 경우

`java.lang.Object`의 `equals()` 메서드는 완전히 동일한 객체인지만을 비교한다.
다음 경우가 아니라면 반드시 재정의를 고려해야 한다.

* 각각의 객체가 고유한 인스턴스인 경우
  * 활성 개체를 나타내는 클래스 (eg: `Thread`)
* 클래스가 논리적 동일성(logical equality)을 확인할 필요가 없는 경우
  * `java.util.Random` 클래스 등 `equals()`를 호출할것 같지 않은 클래스
* 상위 클래스에서 재정의한 `equals()`가 하위 클래스에서 적절하게 동작하는 경우
* 클래스가 private 또는 package-private 이고 `equals()`를 호출할 일이 없는 경우

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
* **`null`에 대한 비동치성**: `null`이 아닌 참조 값 x에 대해, `x.equals(null)`은 false이다.

#### 반사성(reflexive) - `x.equals(x)`는 true이다.

이 규칙을 깨트리는 `equals()`를 만들기는 쉽지 않다.

#### 대칭성(symmetric) - `x.equals(y)`가 true이면 `y.equals(x)`도 true이다.

이 규칙은 주의하지 않으면 쉽게 깨질 수 있다.

다음 `CaseInsensitiveString` 예시는 `String`과 호환성을 가져가기 위해서 `equals()`에 `String`객체와 비교를 추가했지만
이로 인해 대칭성을 위반하게 된다. 
`CaseInsensitiveString.equals(String)`이 참일 경우에도 `String.equals(CaseInsensitiveString)`는 항상 거짓이 되기 때문이다. [CaseInsensitiveStringTest.java](../examples/src/test/java/effectivejava/chapter03/rule08/CaseInsensitiveStringTest.java)

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

* 예제: [Pointy1.java](../examples/src/main/java/effectivejava/chapter03/rule08/Point1.java)
* 예제: [ColorPoint1.java](../examples/src/main/java/effectivejava/chapter03/rule08/ColorPoint1.java)
* 예제: [ColorPointTest.java](../examples/src/test/java/effectivejava/chapter03/rule08/ColorPointTest.java)

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

* 예제: [ColorPoint2.java](../examples/src/main/java/effectivejava/chapter03/rule08/ColorPoint2.java)
* 예제: [ColorPointTest.java](../examples/src/test/java/effectivejava/chapter03/rule08/ColorPointTest.java)

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
또 다른 미묘한 문제를 발생시킬 수 있다.

이 경우 가장 바람직한 방법은 `Point` 상속을 포기하고 HAS-A 관계로 복합(composite) 객체를 만드는 것이다.
([규칙 16](chapter04.md#규칙-16-계승하는-대신-구성하라))

* 예제: [Point.java](../examples/src/main/java/effectivejava/chapter03/rule08/Point.java)
* 예제: [ColorPoint.java](../examples/src/main/java/effectivejava/chapter03/rule08/ColorPoint.java)

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
  ([규칙 15](chapter04.md#규칙-15-변경-가능성을-최소화하라))
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

* `equals`를 재정의할 때는 반드시 `hashCode`도 재정의하라. [규칙 9](#규칙-9-equals를-재정의할-때는-반드시-hashcode도-재정의하라)
* `@Override public boolean equals(Object o)` 시그니처를 변경하지 마라. 인자 자료형을 바꾸게 되면 재정의(override)가 안될 수 있다.
* 과도한 동치성에 집착하지 마라. [토론 필요](#equals관련-토론-주제)

### `equals()`관련 토론 주제: 어디까지 비교할 것인가?

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

* DK: 비교하면 안된다. 관리상 기록을 위한 것이지 객체의 속성이라고 볼 수 없다.
* JH:
* SK:
* YJ:

## 규칙 9: `equals`를 재정의할 때는 반드시 `hashCode`도 재정의하라

값 객체(Value Object)를 만들 때는 `Hashtable`, `HashMap`, `HashSet` 등의
해시 기반 컬렉션은 사용하게 되기 마련이므로 무조건 `hashCode()`를 재정의 해야 한다.

### `hashCode()` 규약

* `equals() == true` 면, 두 객체의 `hashCode()` 값은 같아야 한다.
* `equals() == false` 면, 두 객체의 `hashCode()` 값은 달라야 한다. 하지만 알고리즘상 중복될 수 있으므로 반드시는 아니다.
* 다만 프로그램이 종료되었다가 새로 시작되었을 때 까지 같을 필요는 없다.

`Object.hashCode()`는 동일한 객체인 경우(`Object.equals()`의 결과로)만 같은 값을 가지게 구현되어있다.
따라서 `equals()`를 재정의한 클래스는 `hashCode()`도 재정의 해야 한다.

`hashCode()`를 재정의 하지 않은 경우:

* 예제: [PhoneNumber1.java](../examples/src/main/java/effectivejava/chapter03/rule09/PhoneNumber1.java)
* 예제: [PhoneNumberTest.java](../examples/src/test/java/effectivejava/chapter03/rule09/PhoneNumberTest.java)

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

* `0`이 아닌 상수로 시작한다. (eg: 17)
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
  * 사전 계산 대신 지연 초기화(lazy initialization)을 사용할 수 있지만 신중하게 ([규칙 71](chapter10.md#규칙-71-초기화-지연은-신중하게-하라))

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

위 방법을 적용한 `HashCodeBuilder` 유틸리티:

* 예제: [HashCodeBuilder.java](../examples/src/main/java/com/yidigun/utils/HashCodeBuilder.java)
* 예제: [HashCodeBuilderTest.java](../examples/src/test/java/com/yidigun/utils/HashCodeBuilderTest.java)

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

## 규칙 10: `toString`은 항상 재정의하라


## 규칙 11: `clone`을 재정의할 때는 신중하라


## 규칙 12: `Comparable` 구현을 고려하라


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
