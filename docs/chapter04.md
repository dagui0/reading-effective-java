# 이펙티브 자바 (3판) - 4장 클래스와 인터페이스

## 목차

* [**아이템 15**: 클래스와 멤버의 접근 권한을 최소화하라](#아이템-15-클래스와-멤버의-접근-권한을-최소화하라)
* [**아이템 16**: `public` 클래스에서는 `public` 필드가 아닌 접근자 메서드를 사용하라](#아이템-16-public-클래스에서는-public-필드가-아닌-접근자-메서드를-사용하라)
* [**아이템 17**: 변경 가능성을 최소화하라](#아이템-17-변경-가능성을-최소화하라)
  * [**[추가]** `List.of()`, `List.copyOf()`](#추가-listof-listcopyof)
  * [**[추가]**  Guava에는 변경 불가능한 기본(primitive) 자료형 배열 클래스가 있음](#추가-guava에는-변경-불가능한-기본primitive-자료형-배열-클래스가-있음)
* [**아이템 18**: 상속보다는 컴포지션을 사용하라](#아이템-18-상속보다는-컴포지션을-사용하라)
* [**아이템 19**: 상속을 고려해 설계하고 문서화하라, 그러지 않았다면 상속을 금지하라](#아이템-19-상속을-고려해-설계하고-문서화하라-그러지-않았다면-상속을-금지하라)
  * [**[추가]** 상속관련 토론 주제: 어노테이션 드리븐 개발과 관련 문제](#추가-상속관련-토론-주제-어노테이션-드리븐-개발과-관련-문제)
* [**아이템 20**: 추상 클래스보다는 인터페이스를 우선하라](#아이템-20-추상-클래스보다는-인터페이스를-우선하라)
* [**아이템 21**: 인터페이스는 구현하는 쪽을 생각해 설계하라](#아이템-21-인터페이스는-구현하는-쪽을-생각해-설계하라)
* [**아이템 22**: 인터페이스는 타입을 정의하는 용도로만 사용하라](#아이템-22-인터페이스는-타입을-정의하는-용도로만-사용하라)
* [**아이템 23**: 태그 달린 클래스보다는 클래스 계층구조를 활용하라](#아이템-22-인터페이스는-타입을-정의하는-용도로만-사용하라)
* [**아이템 24**: 멤버 클래스는 되도록 `static`으로 만들라](#아이템-23-태그-달린-클래스보다는-클래스-계층구조를-활용하라)
* [**아이템 25**: 톱레벨 클래스는 한 파일에 하나만 담으라](#아이템-24-멤버-클래스는-되도록-static으로-만들라)

## 아이템 15: 클래스와 멤버의 접근 권한을 최소화하라

> > 높은 응집도와 낮은 결합도 High Cohesion and Loose Coupling \
> > -- Larry Constantine
> 
> 구조적 프로그래밍 이론이 정립된 1970년대부터 높은 응집도와 낮는 결합도는 프로그램 설계의 가장 기본적인 원칙이다. \
> -- 조성조

* [Larry Constantine (1943 ~ )](https://en.wikipedia.org/wiki/Larry_Constantine) - 구조적 프로그래밍 이론을 만든 분들

### 캡슐화(Encapsulation), 정보 은닉(Information Hiding)

* 빠르다 - 병렬 개발이 가능함
* 쉽다 - 모듈별로 독립적이기 때문에 디버깅, 수정, 최적화, 교체등이 쉽다
* 재사용성이 높아진다.
* 큰 시스템을 설계하는데 난이도를 낮춰준다.

정보 은닉을 위한 방법은 접근 제한을 가능한 높게 만들어 접근 가능한 코드를 최소한으로 줄여야 한다는 것

* 클래스
  * `public` - API의 일부가 됨. 영원히 하위 호환을 신경써야 한다.
  * package-private - 모듈 내부용으로만 사용할 클래스들로 언제든 변경하거나 교체해도 된다.
  * `private` 내부 클래스 - 부모 클래스 내부에서만 사용할 클래스.
* 멤버, 메소드, 중첩 클래스, 중첩 인터페이스
 * `private` - 클래스 내부에서만 사용
   * 테스트 코드를 위해 package-private으로 설정하기도 한다.
 * package-private - 같은 패키지에서만 사용
 * `protected` - 같은 패키지와 하위 클래스에서 사용 (package-private을 포함한다.)
   * package-private에서 `protected` 단계로 가면 접근 허용 범위가 엄청나게 넓어지는 것이다.
   * `protected`는 공개 API의 일부분이므로 영원히 호환성을 지원해야한다.
 * `public` - 어디서든 사용 가능
   * `public` 클래스의 `public` 멤버 변수는 사용해서는 안된다.
     * 단 `final`로된 상수는 봐줄 수 있음. 상수는 관례적으로 대문자로 이름을 작성하고 값이거나 불변 객체여야 한다.
     * `public` 멤버 변수가 있으면 대개 Thread safe하지 않게 된다.
     * 제약조건 설정이나, 리펙터링을 불가능하게 한다.

클래스를 상속하는 과정에서 멤버들의 접근제한을 낮게 변경할 수 있으나 높게 변경할 수는 없다.
[하위 클래스는 상위클래스와 동일하게 동작해야 한다는 원칙](https://inpa.tistory.com/entry/OOP-%F0%9F%92%A0-%EC%95%84%EC%A3%BC-%EC%89%BD%EA%B2%8C-%EC%9D%B4%ED%95%B4%ED%95%98%EB%8A%94-LSP-%EB%A6%AC%EC%8A%A4%EC%BD%94%ED%94%84-%EC%B9%98%ED%99%98-%EC%9B%90%EC%B9%99) 때문에 어쩔 수 없는 것.

### `private` 배열

배열은 불변객체가 아니므로 `private`하고자 한 의도가 무너질 수 있다.

```java
class ArrayMemberAntiPattern {
    private static final int[] DEFAULT_VALUES = { 1, 4, 7, 8, 9, 6, 3, 2, 5 };

    private int[] values = DEFAULT_VALUES;
    public int[] getValues() {
        return values;
    }
    public void setValues(int[] values) {
        this.values = values;
    }
}
class Bar {
    public static void main(String[] args) {
        ArrayMemberAntiPattern antiPattern = new ArrayMemberAntiPattern();
        int[] values = antiPattern.getValues();
        values[0] = 100; // 원래 의도와는 다르게 값을 변경할 수 있다.
        System.out.println(antiPattern.getValues()[0]); // 100
    }
}
``` 

이를 해결하기 위해서는 `Collections.unmodifiableList()`를 사용하거나, `clone()`, `Arrays.copyOf()`를 사용하여 복사본을 반환해야 한다.

### 모듈 (Java 9)

패키지의 상위개념인 모듈이 추가되었으며 최상위 레벨 접근제어 장치로 동작한다.
`module-info.java`에서 모듈 밖으로 공개할 패키지를 선언할 수 있다.

```java
// module-info.java
module com.example.module {
    exports com.example.module.api; // API로 공개할 패키지
    exports com.example.module.internal to com.example.module.test; // 테스트 모듈에만 공개
}
```
```java
module com.example.module.test {
    requires com.example.module;
    requires com.example.module.internal;
}
```

하지만 모듈로 설계된 라이브러리 jar를 그냥 클래스패스에 추가하면 모듈과 무관하게 접근 가능해진다.
클래스패스의 모든 클래스들은 "unnamed"라는 모듈에 포함된 것으로 간주됨.

자세한 설명은 [Java의 module](https://velog.io/@kkywalk2/Java%EC%9D%98-module-1) 참조.

## 아이템 16: `public` 클래스에서는 `public` 필드가 아닌 접근자 메서드를 사용하라

이 섹션의 내용은 다들 너무 잘 알고 있는 내용이므로 정리를 하기가 싫다.

### `java.awt.Dimension` 예시

별 중요하지 않은데 성능 저하의 원인이 되고 있는 클래스로 유명한듯 하다.
[아이템 67](chapter09.md#아이템-67-최적화는-신중히-하라)

```java
package java.awt;
public class Dimension extends Dimension2D {
    public int width;
    public int height;
    public Dimension(int width, int height) {
        this.width = width;
        this.height = height;
    }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
}
```

## 아이템 17: 변경 가능성을 최소화하라

Immutable 패턴이라고 알려진 불변 객체는 대표적으로 `String`, `Integer`등 Boxing 객체들, `BigInteger`, `BigDecimal`등이 포함된다.

* 객체의 상태를 변경하는 메소드(setter)를 제공하지 않는다.
* 클래스를 `final`로 해서 상속할 수 없도록 한다.
* 모든 필드를 `final`로 선언한다.
* 모든 필드를 `private`로 한다.
* 자신 외에는 내부의 가변 컴포넌트에 접근할 수 없도록 한다. 객체의 레퍼런스를 리턴하는 메소드를 만들어서는 안된다.
  생성자, getter, `readObject()`에서 모두 방어적 복사를 수행하라.
  ([아이템 88](chapter12.md#아이템-88-readobject-메서드는-방어적으로-작성하라))
* 같은 객체를 만들 필요가 없으므로 복사 생성자나 `clone`메소드를 제공하지 않는다.

### 복소수 예제

[`Complex.java`](../src/main/java/com/effectivejava/chapter02/Complex.java) 참조

* 사칙 연산 메서드(`plus`, `minus`, `times`, `dividedBy`)는 새로운 객체를 반환한다.
* `add`, `subtract`, `multiply`, `divide` 대신 `plus`, `minus`, `times`, `dividedBy` 이름을 사용하여 새로운 객체를 리턴한다는 느낌적인 느낌을 주었다.
  * `BigInteger`, `BigDecimal`의 경우 불변 객체이지만 메소드명 때문에 혼란을 가져왔다.

### 불변 객체의 장점

* 코드를 단순하고 안전하다.
* Thread-safe 하다.
* 자주 쓰이는 값을 상수로 제공해도 안전하고, 그렇게 해서 객체 생성을 절약할 수 있다.
  ```java
  private static final Complex ZERO = new Complex(0, 0);
  private static final Complex ONE = new Complex(1, 0);
  private static final Complex I = new Complex(0, 1);
  ```
* 불변 객체 끼리는 내부 데이터를 공유해도 안전하다. `BigInteger.negate()`는 내부 배열을 재사용해서 새로운 객체를 만든다.
* `Map`의 key값이나, `Set`의 원소로 사용되기 좋다.
* 실패 원자성을 제공한다. (메소드 호출이 실패해도 정합성을 유지해야한다는 말) ([아이템 76](chapter10.md#아이템-76-실패-원자성을-제공하라))

### 불변 객체의 단점

* 값이 조금만 변경될 때도 새로 객체를 만들어야 한다.
  * 값 변경이 가능한 [가변 동반 객체](https://github.com/Java-Bom/ReadingRecord/issues/47)를 제공한다. `String`과 `StringBuilder`, `BigIntger`와 `BitSet`.

### 불변 클래스를 만드는 요령

* 생성자 대신 정적 팩터리 메서드를 사용하도록 한다. ([아이템 1](chapter02.md#아이템-1-생성자보다-정적-팩터리-메서드를-사용하라))
  * 내부적으로 캐싱 등을 적용해서 성능을 끌어올릴 여지가 생긴다.
* 반드시 `final` 클래스로 해서 상속을 못하게 해야한다.
  * `BigInteger`나 `BigDecimal`은 `final`이 아닌데, 호환성 때문에 바로잡지 못하고 있다.
  * 만약 불변이어야 하는게 중요하다면 `BigInteger`나 `BigDecimal`은 가변 객체로 간주하는 것이 좋다.
* 불변 클래스가 너무 무겁다고 생각된다면 가변동반 클래스를 같이 제공한다.
* 불변이 아닌 클래스를 만들때도 클래스는 필요한 경우가 아니면 가능한 불변인 편이 좋다.
  * getter가 있다고 해서 무조건 setter를 만들지 말자.
  * 특별한 이유가 없다면 필드는 가급적 `final`로 한다.
  * 생성자와 팩터리 메서드 외에는 초기화 메서드를 제공하지 말것

### **[추가]** `List.of()`, `List.copyOf()`

Java 9 부터 `Collections.unmodifiableList()`를 대체할 수 있는 `List.of()`, `List.copyOf()` 추가됨.
당연한 이야기지만 `Set.of()` 등도 있음.

* `<E> List<E> of(E e1, E e2)` - 최소 0 ~ 최대 10개까지 메소드가 준비되어 있음
* `<E> List<E> of(E... elements)`
* `<E> List<E> copyOf(Collection<? extends E> coll)`
* `<K,V> Map<K,V> of(K k1, V v1, K k2, V v2)` - 최소 0 ~ 최대 10쌍까지 메소드가 준비되어 있음
* `<K,V> Map<K,V> ofEntries(Map.Entry<? extends K,? extends V>... entries)`
* `<K,V> Map<K,V> copyOf(Map<? extends K,? extends V> map)`

### **[추가]**  Guava에는 변경 불가능한 기본(primitive) 자료형 배열 클래스가 있음

* [Guava: Google Core Libraries for Java](https://github.com/google/guava)
* [Guava API docs](https://guava.dev/releases/22.0/api/docs/)
* [Guava를 써야하는 5가지 이유](https://blog.outsider.ne.kr/710)

```java
import com.google.common.primitives.ImmutableIntArray;

@Test
public void testImmutableIntArray() {
    ImmutableIntArray arr = ImmutableIntArray.of(1, 2, 3);

    assertEquals(1, arr.get(0));
}
```

## 아이템 18: 상속보다는 컴포지션을 사용하라

___**상속은 코드를 재활용하는 강력한 수단이 될 수도 있지만, 기본적으로 캡슐화 원칙을 깨뜨린다.**___

* 프로그래머 혼자서 부모와 자식 클래스를 만든다면 안전하지만 그렇지 않은 경우 의도하지 않은 결과가 나올 수 있다.

```java
public class InstrumentedHashSet<E> extends HashSet<E> {
    private int addCount = 0;
    public InstrumentedHashSet(int capacity) {
        super(capacity);
    }
    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }
    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);     // 알고 보면 내부적으로 add()를 사용하므로 문제가 된다.
    }
    public int getAddCount() { return addCount; }
}
```

* 버전이 올라가면서 상위 클래스 동작이 변경되면 하위클래스가 제대로 동작하지 않을 수 있다.
  * 이전부터 존재하던 `Hashtable`과 `Vector`를 컬렉션 API에 추가하면서 보안 문제가 발생했었다.  (어떤 문제지? 아우 궁금해)
  * 최악의 경우에는 내가 확장해서 사용하던 메소드와 똑같은 이름의 메소드가 상위 클래스에 추가될 수 있다.

### 컴포지션을 이용한 해결책

```java
public class InstrumentedSet<E> extends ForwardingSet<E> {
    private int addCount = 0;
    public InstrumentedSet(Set<E> s) {
        super(s);
    }
    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }
    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }
    public int getAddCount() { return addCount; }
}
```

* HAS-A 관계로 확장하면 모든 문제가 해결된다.
* 이런 형태의 또 다른 장점은 기존에 사용중이던 `Set`을 임시로 감싸서 기능을 추가할 수 있다는 점이다.
* `ForwardingSet` 같은 클래스를 준비해두면 여러 형태로 추가 기능을 만들어 사용할 수 있다. ([데코레이터 패턴](https://inpa.tistory.com/entry/GOF-%F0%9F%92%A0-%EB%8D%B0%EC%BD%94%EB%A0%88%EC%9D%B4%ED%84%B0Decorator-%ED%8C%A8%ED%84%B4-%EC%A0%9C%EB%8C%80%EB%A1%9C-%EB%B0%B0%EC%9B%8C%EB%B3%B4%EC%9E%90))
  * `FilterInputStream`, `FilterOutputStream`, `FilterReader`, `FilterWriter` 등
  * `HttpServletRequestWrapper`, `HttpServletResponseWrapper` 등
* 이러한 래퍼(wrapper)클래스의 단점은 callback 유형의 인터페이스와 어울리지 않는다. ([참고](https://stackoverflow.com/questions/28254116/wrapper-classes-are-not-suited-for-callback-frameworks))

### 상속을 할 것인가 말 것인가를 선택하는 기준

* IS-A 관계가 성립하는가라고 여러번 생각해 본다. 그래도 IS-A라면 상속하도록한다.
* 그렇지 않다면 `private` 필드에 저장하고 필요한 다른 API를 제공하도록 설계하라.
  * `Stack`은 `Vector`가 아니므로 상속해서는 안됬었다. 단지 `Stack`을 `Vector`의 기능을 이용해 구현했을 뿐
  * `Properties`도 `Hashtable`가 아니므로 마찬가지
    * `Properties`는 키를 반드시 문자열로 하도록 하고 싶어서 `getProperty()`와 `setProperty()`를 추가했지만,
      `Hashtable`로 부터 상속받은 `get()`와 `put()`은 여전히 사용할 수 있다.
* 마지막으로 만들고자하는 API에 상위 클래스의 API가 필요한가를 물어보라. \
  상위 클래스의 API가 지금 만들 클래스에 적합하지 않다면 상속하지 마라.

### **[추가]** `Iterator`와 Fail-Fast 원칙

* `Map.keySet().iterator()`, `Collection.iterator()`는 `Iterator`를
  리턴하는데 이를 이용한 순회 과정에서 원본 컬렉션에 변경이 발생하면 `ConcurrentModificationException`을 발생시킨다.
  * 이를 문제의 소지가 있으면 바로 중단시키기 위한 Fail-Fast 원칙이라고 하는데,
  * Fail-Fast를 검색해보면 `ConcurrentModificationException`말고는 딱히 언급되는 사례가 없다.
* 더 오래된 `Hashtable`과 `Vector`의 `Enumeration`은 `Iterator`와 달리 `ConcurrentModificationException`을 리턴하지 않는다.
  * 이는 아마도 `Enumeration`이 사본을 대상으로 순회를 하도록 구현되어있기 때문으로 추측된다.
* `ConcurrentMap`인터페이스와 `ConcurrentHashMap`이란 것이 있는데 이에 대해서는 
  [자세한 설명](https://velog.io/@alsgus92/ConcurrentHashMap%EC%9D%98-Thread-safe-%EC%9B%90%EB%A6%AC)은 생략한다.

## 아이템 19: 상속을 고려해 설계하고 문서화하라, 그러지 않았다면 상속을 금지하라

> 상속은 기본적으로 객체지향의 정보은닉(information hide) 원칙을 위반하는 행위이다. 

### 상속을 염두에 둔 클래스 작성 요령

* 재정의할 메소드들이 어떤식으로 동작해야 하는지, 내부적으로 어떻게 동작하는지 문서화해야 한다.
  * 재정의할 수 있는 메소드를 내부적으로 호출하는 경우에 대한 내용을 명시적으로 설명해야한다.
  * API 문서에 보통 "Implementation Requirements"라는 내용이 있는 경우가 있는데 이런 내용을 설명하는 것이다. \
    javadoc 주석에서 `@implSpec` 태그를 사용하면 된다.
  * "좋은 문서는 '어떻게'가 아니라 '무엇'을 설명해야 한다"는 격언에 대치되지만 어쩔 수 없다. \
    상속이 기본적으로 캡슐화를 위반하는 것이기 때문에 어쩔 수 없다.
* API를 공개하기 전에 직접 하위클래스를 설계해보라.
* 생성자는  직접적이건 간접적이건 재정의 가능한 메소드를 호출해서는 안된다.
  * 하위 클래스의 코드가 상위 클래스의 코드보다 먼저 실행될 수 있다.
  * 하위클래스의 코드가 하위클래스에서 추가한 속성에 의존적이라면 의도하지 않은 결과를 만들어 낸다. ([OverrideTest.java](../src/test/java/com/effectivejava/chapter02/OverrideTest.java))
* `Cloneable`, `Serializable`을 구현하지 마라. 확장하는 개발자에게 너무 무거운 짐을 지우는 것이다.
  ([아이템 13](chapter03.md#아이템-13-clone-재정의는-주의해서-진행하라), [아이템 86](chapter12.md#아이템-86-serializable을-구현할지는-신중히-결정하라))
  * `clone()`, `readObject()`는 재정의 가능한 메소드를 호출해서는 안된다.
  * `readResolve()`, `writeReplace()` 메소드는 `protected`로 선언해서 재정의 되도록 해야한다.

### 상속용이 아닌 클래스 작성 요령

* 상속을 의도하고 만든 클래스가 아니라면 `final`로 설정한다.
* 다른 사람이 상속하는 꼴은 못보지만, 나는 상속을 사용하고 싶으면 생성자를 `private`나 package-private로 한다.
  ([아이템 17](#아이템-17-변경-가능성을-최소화하라))
* 상속할 수 없도록 하는 대신에 주요 기능을 모두 담은 인터페이스(`List` 등)를 제공하고 사용하도록 한다.
* 그래도 상속 가능성을 조금은 남겨두고 싶으면, 클래스의 기능에서 재정의 가능한 메소드에 대한 의존성을 제거한다.
  * 재정의 가능하도록 허용할 메소드의 기능을 `private` 메소드로 옮기고, 다른 코드는 `private`메소드에 의존하도록 한다.

### **[추가]** 상속관련 토론 주제: 어노테이션 드리븐 개발과 관련 문제

[lombok](https://projectlombok.org/) 같이 어노테이션을 이용해서 코드를 자동 확장하는 경우.
설사 어노테이션 프로세서가 내부적으로 상속 메커니즘을 이용하지 않는다 해도, 상속과 같은 것이라고 할 수 있다.

이런 경우에는 상속을 피하라는 규칙에 위배된다고 볼 수 있을까?

멤버 의견:
* Alejandro: 
* Leeturn:
* Scully:
* Lucie:

## 아이템 20: 추상 클래스보다는 인터페이스를 우선하라


## 아이템 21: 인터페이스는 구현하는 쪽을 생각해 설계하라


## 아이템 22: 인터페이스는 타입을 정의하는 용도로만 사용하라


## 아이템 23: 태그 달린 클래스보다는 클래스 계층구조를 활용하라


## 아이템 24: 멤버 클래스는 되도록 `static`으로 만들라


## 아이템 25: 톱레벨 클래스는 한 파일에 하나만 담으라

