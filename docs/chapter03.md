# 이펙티브 자바 (2판) - 3장 모든 객체의 공통 메서드

## 목차

* [**규칙 8**: `equals`를 재정의할 때는 일반 규약을 따르라](#규칙-8-equals를-재정의할-때는-일반-규약을-따르라)
* [**규칙 9**: `equals`를 재정의할 때는 반드시 `hashCode`도 재정의하라](#규칙-9-equals를-재정의할-때는-반드시-hashcode도-재정의하라)
* [**규칙 10**: `toString`은 항상 재정의하라](#규칙-10-tostring은-항상-재정의하라)
* [**규칙 11**: `clone`을 재정의할 때는 신중하라](#규칙-11-clone을-재정의할-때는-신중하라)
* [**규칙 12**: `Comparable` 구현을 고려하라](#규칙-12-comparable-구현을-고려하라)
* [**[추가]** Record Class](#record-class)

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
if (!(o instanceof ColorPoint))
    return false;
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
    private final int id;
    private final String name;
    private final Date regDate;
    private final Date modDate;

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


## 규칙 9: `equals`를 재정의할 때는 반드시 `hashCode`도 재정의하라


## 규칙 10: `toString`은 항상 재정의하라


## 규칙 11: `clone`을 재정의할 때는 신중하라


## 규칙 12: `Comparable` 구현을 고려하라



## Record Class

[Record Class 소개](https://mr-popo.tistory.com/243)
* 불변(immutable) 객체를 쉽게 생성할 수 있도록 하는 유형의 클래스입니다.
* JDK14에서 preview로 등장하여 JDK16에서 정식 스펙으로 포함되었습니다.

### 조건

* 모든 필드 `private final`
* 필드 값을 모두 포함한 생성자 존재
* 접근자 메서드(getter)

### 혜택

컴파일 타임에 컴파일러가 코드를 추가해주기 때문입니다.

```java
public record Student(String name, int age) {
}
```
* getters 메서드 자동 생성
* `equals()` 자동 생성
* `hashCode()` 자동 생성
* `toString()` 자동 생성
