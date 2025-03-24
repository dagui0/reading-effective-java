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

## 아이템 20: 추상 클래스보다는 인터페이스를 우선하라

### 추상 클래스 vs 인터페이스

* 추상 클래스 (Abstract Class)
  * 클래스를 만들기 쉽도록 반쯤 만들어 놓은 클래스
  * 필드를 가지므로 상태를 가질 수 있다. 인스턴스화(new 하여 메모리 공간을 할당)할 수 있다.
  * 메소드를 작성하여 동작을 구현 할 수 있다.
  * 메소드를 정의만 할 수 있다. (`abstract` 메소드)
* 인터페이스 (Interface)
  * 라이브리가 클라이언트(개발자)과 한 약속(조약, 규약, API)
  * 필드를 가질 수 없으므로 상태를 가질 수 없다. 인스턴스화 할 수 없다.
  * 메소드를 정의만 할 수 있을 뿐 구현할 수 없(었)다. (Java 8부터 `default` 메소드 구현 가능)

### 추상 클래스와 인터페이스의 차이점

* 추상 클래스를 규약으로 사용할 수도 있지만 규약을 구현하려면 상속해서 하위클래스가 되어야만 한다.
* Java는 다중 상속을 허용하지 않으므로 추상클래스로 제공된 규약은 유연성이 떨어진다.
  * Servlet API는 [`HttpServlet`을 상속받아야만](https://docs.oracle.com/javaee/7/api/javax/servlet/http/HttpServlet.html) 하므로 불편한 점이 많았다.
  * Struts는 [`Action`도 클래스](https://svn.apache.org/repos/asf/struts/archive/trunk/struts-doc-1.1/api/org/apache/struts/action/Action.html)이므로 유연성이 떨어졌다.
  * String MVC는 [`Controller`는 인터페이스](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/mvc/Controller.html)이므로 유연성이 올라갔다.
  * 요즘 Spring은 `@Controller`를 사용하므로 `Controller` 인터페이스를 구현할 필요조차 없다.

### 인터페이스의 장점

* 인터페이스는 믹스인(mixin)타입 정의에 안성맞춤이다.
  * `Comparable` 처럼 주된 기능 외에 지원하는 부가 기능을 정의
* 인터페이스를 이용하면 계층 구조가 없는 타입 프레임워크를 만들 수 있다.
  * 상속관계는 엄격한 트리구조가 강제되지만 인터페이스를 이용하면 다양한 형태의 타입간 관계를 설정할 수 있다. 
* 래퍼클래스([아이템 18](#아이템-18-상속보다는-컴포지션을-사용하라))와 함께라면 안전하고 강력한 수단이 된다.

### `default` 메소드

* `default` 메소드를 이용해서 개발자들의 일을 덜어줄 수 있다.
  * 단 `equals()`, `hashCode()`, `toString()` 등을 구현해서는 안된다.
* 인터페이스와 추상 골격 구현(skeletal implemetaion)을 이용하면 [템플릿 메소드 패턴(Template Method Pattern)](https://inpa.tistory.com/entry/GOF-%F0%9F%92%A0-%ED%85%9C%ED%94%8C%EB%A6%BF-%EB%A9%94%EC%86%8C%EB%93%9CTemplate-Method-%ED%8C%A8%ED%84%B4-%EC%A0%9C%EB%8C%80%EB%A1%9C-%EB%B0%B0%EC%9B%8C%EB%B3%B4%EC%9E%90)을 구현할 수 있다.
  * 인터페이스에서 `default`로 주요 메소드를 구현한다.
  * 추상 골격 구현에서 나머지 메소드들에 대한 기본 구현을 제공한다.
  * 개발자는 추상 골격을 재정의 하는 것으로 기능 구현을 쉽게 할 수 있다.

## 아이템 21: 인터페이스는 구현하는 쪽을 생각해 설계하라

* `default` 메소드는 컴파일에는 문제가 없었다고해도 기존 상속 트리에 적용되면 문제가 될 수 있고
  런타임 오류를 발생시킬 수 있다.
  * Java 8에서 `default` 메소드가 도입되면서 기존 인터페이스에 많은 디폴트 메소드가 추가되었고 이로 인해 혼란이 있었다고 한다.
* `default` 메소드를 기존 인터페이스에 추가하는 것은 매우 신중해야 한다. 반면 새로운 인터페이스를 만드는 경우라면 적극 활용하자 ([아이템 20](#아이템-20-추상-클래스보다는-인터페이스를-우선하라))
* 새로운 인터페이스를 릴리즈 하기 전에 반드시 테스트로 구현체들을 3개는 만들어 봐야 한다.

### `removeIf()` 사례

* `SynchronizedCollection`: commons-collection 라이브러리에 포함되어있다.
  * `List.removeIf()`는 java 8(2014년 3월 18일)에서 추가되었는데, 이 책이 나오기 전까지 아직 `removeIf()`를 구현하지 않고 있다.(2017년 12월 27일)
  * 클라이언트가 `SynchronizedCollection.removeIf()`를 호출하면 `List`의 디폴트 구현이 사용되게 되고 lock이 제대로 처리되지 않을 것이다.
    * `SynchronizedCollection.removeIf()`는 Java 8 릴리즈 4년 후 4.4(2018년 6월 27일) 부터 추가되었다.

### **[추가]** 인터페이스 설계와 TDD

* [TDD(Test Driven Development)](https://etst.tistory.com/388)는 테스트 코드를 먼저 작성하는 것을 원칙으로 하는 개발 방법이다.
* 이는 내가 작성할 코드를 사용하는 사람의 입장이 먼저 되도록 해서 인터페이스를 잘 설계할 수 있도록 도와준다고 생각한다.
* 인터페이스를 만들어야 한다면 TDD를 고려하면 좋을듯 하다.

## 아이템 22: 인터페이스는 타입을 정의하는 용도로만 사용하라

```java
public interface Constants {
    static final String URL_PREFIX = "https://www.interpark.com";
    static final String IMG_PREFIX = "https://img.interpark.com";
}
```

* 인터페이스를 상수 정의 모음집 용도로 사용하지 마라
  * 상수 정의는 구현의 내용이고 내부 구현 상세사항을 외부로 노출하는 것은 바람직하지 않다.
  * `final`이 아닌 클래스가 상수를 정의한다면, 하위 클래스들이 상수를 사용하게 되고 돌이킬 수 없게 된다.
* 공개가 필요한 API로서의 상수는 관련성이 높은 객체에 정의하는 것이 낫다. (`Integer.MAX_VALUE` 처럼)
* 유형을 정의하는 상수라면 열거타입(enum)을 사용하라.
* 이도 저도 아니라면 상수 정의용 클래스를 만들어라.

```java
public final class PhysicalConstants {

  PhysicalConstants() {
    throw new AssertionError("Cannot instantiate " + getClass());
  }

  public static final double AVOGADRO_NUMBER = 6.022_140_76e23; // mol^-1
  public static final double BOLTZMANN_CONST = 1.380_649e-23; // J/K
  public static final double ELECTRON_MAS = 1.602_176_634e-19; // J
}
```

* 정적 임포트 기능을 이용하면 상수만 사용할 수도 있다.

```java
import static effectivejava.chapter04.item22.PhysicalConstants.*;
```

## 아이템 23: 태그 달린 클래스보다는 클래스 계층구조를 활용하라

```java
class Figure {
    enum Shape { CIRCLE, RECTANGLE };
    
    final Shape shape;
    double length, width;   // RACTANGLE
    double radius;          // CIRCLE
    Figure(double radius) {
        this.shape = Shape.CIRCLE;
        this.radius = radius;
    }
    Figure(double length, double width) {
        this.shape = Shape.RECTANGLE;
        this.length = length;
        this.width = width;
    }
    
    double area() {
        switch (shape) {
            case CIRCLE:
                return Math.PI * radius * radius;
            case RECTANGLE:
                return length * width;
            default:
                throw new AssertionError("Unknown shape: " + shape);
        }
    }
}
```

위와 같은 클래스를 만들 사람은 없을 것이라고 생각된다. Java 개발자라면 아래 처럼 만들겠죠.

```java
abstract class Shape {
    public abstract area();
}

class Circle extends Shape {
    private double radius;
    public Circle(double radius) {
        this.radius = radius;
    }
    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

class Rectangle extends Shape {
    private double length;
    private double width;

    public Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }
    @Override
    public double area() {
        return length * width;
    }
}

class Square extends Rectangle {
    public Square(double length) {
        super(length, length);
    }
}
```

## 아이템 24: 멤버 클래스는 되도록 `static`으로 만들라

* Inner 클래스의 종류
  * `static` 멤버 클래스
  * 그냥 멤버 클래스(인스턴스 멤버 클래스)
  * 익명 클래스
  * 지역 클래스

.

.

.

.

.

.

.

.

.

.

.

.

.

### `static` 멤버 클래스

* 정적 멤버 클래스는 외부 클래스의 `private` 접근 제한을 통과할 수 있다는 점을 제외하면 별도 클래스와 같다.
  * 독립적으로 사용 가능한 부분 구성요소나, 도우미 클래스 등에 사용된다.

```java
public class Map<K, V> {
    public static class Entry<K, V> {
        private K key;
        private V value;
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
        public K getKey() { return key; }
        public V getValue() { return value; }
    }
}
```

### 인스턴스 멤버 클래스

* 그냥 멤버 클래스는 외부 클래스의 참조를 내부적으로 가지고 있으며, 외부 클래스의 인스턴스 변수에 접근 가능하다.
  * 내부 구조를 다른 방식으로 사용하도록 해주는 View 객체로 주로 사용된다.
* 바깥 클래스에 대한 참조를 가지고 있으므로,
  바깥 인스턴스가 내부 클래스에 대한 참조를 가지면 상호참조가 되어 가비지컬렉션에 문제가 생길수 있다.

```java
public class List<E> {
    public int size() { return 0;}
    public E get(int index) { return null;}

    private class MyIterator implements java.util.Iterator<E> {
        private int index = 0;
        public boolean hasNext() { return index < size(); }
        public E next() {
            return get(index++);
        }
    }

    public Iterator<E> iterator() {
        return new MyIterator();
    }
}
```

### 익명 클래스

* 인라인 클래스 정의를 위한 문법인데 람다 구문이 나온 이후에 밀렸다.
* 익명 클래스의 제약 사항
  * 다중 상속 즉, 추가적인 인터페이스 구현이 불가능하다. 인터페이스 하나만을 구현하는 것은 가능하다.
  * `static` 코드에서 생성되면 `static` 멤버 클래스이고, 인스턴스 코드에서 생성되면 인스턴스 멤버 클래스이다.
  * `final`이 아닌 `static` 필드를 가질 수 없다고 책에 나오는데... (되더라)
  * 이름이 없으므로 `instanceof`에 사용될 수 없다.

```java
public static <K,V> Entry<K,V> readonlyEntry(Entry<K,V> entry) {
    return new Entry<>(entry) {
        @Override
        public void setKey(K key) {
            throw new UnsupportedOperationException("setKey");
        }
        @Override
        public void setValue(V value) {
            throw new UnsupportedOperationException("setValue");
        }
    };
}
```

### 지역 클래스

* 메소드 내에서 정의하는 클래스이며, 메소드 내의 지역 변수와 같은 스코프를 가진다.
* 지역 클래스의 제약 사항
  * `static` 코드에서 생성되면 `static` 멤버 클래스이고, 인스턴스 코드에서 생성되면 인스턴스 멤버 클래스이다.

## 아이템 25: 톱레벨 클래스는 한 파일에 하나만 담으라

역시 이렇게 작성할 Java 개발자는 없을 것으로 생각된다.

## **[추가]** `@NonNull`, `@Nonnull`, `@NotNull`, `@Nullable`, `@NotBlank`, `@NotEmpty` 등

편집기에서 @NonNull로 지정되면 뭔가륵 막 검사해주는 듯.
[@Nonnull vs @NonNull](https://velog.io/@gongmeda/Nonnull-vs-NonNull)

* **[OK]** `@javax.annotation.Nonnull` - JSR 305
  * [javax.annotation:javax.annotation-api:1.3.2](https://javadoc.io/doc/javax.annotation/javax.annotation-api/latest/index.html) - 이 아티펙트와는 관련이 없다.
  * [com.google.code.findbugs:jsr305:3.0.2](https://javadoc.io/doc/com.google.code.findbugs/jsr305/latest/index.html) - 이 패키지에 들어있으며 guava에 포함되어있음
* **[OK]** `@lombok.NonNull`
  * [org.projectlombok:lombok:1.18.36](https://projectlombok.org/api/lombok/package-summary)
* **[OK]** `@org.jetbrains.annotations.NotNull` - JetBrains
  * [org.jetbrains:annotations:26.0.2](https://javadoc.io/doc/org.jetbrains/annotations/latest/index.html) 에 포함되어있음
* **[OK]** `@org.eclipse.jdt.annotation.NonNull` - Eclipse JDT
  * [org.eclipse.jdt:org.eclipse.jdt.annotation:2.0.0](https://javadoc.io/doc/org.eclipse.jdt/org.eclipse.jdt.annotation/2.0.0/index.html)
* **[OK]** `@org.springframework.lang.NonNull` - Spring Framework
  * [org.springframework:spring-core:6.2.5](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/lang/package-summary.html)
  * 자매품 `@org.springframework.lang.Nullable`, `@org.springframework.lang.NonNullApi`, `@org.springframework.lang.NonNullFields`
* **[OK]**`@org.checkerframework.checker.nullness.qual.NonNull` - Checker Framework
  * [org.checkerframework:checker-qual:3.49.1](https://javadoc.io/doc/org.checkerframework/checker/latest/org/checkerframework/checker/nullness/qual/NonNull.html)


* **[다른 거임]** `@javax.validation.constraints.NotNull`
  * [javax.validation:validation-api:2.0.0](https://javaee.github.io/javaee-spec/javadocs/javax/validation/constraints/package-summary.html)
  * 자매품 `@NotEmpty`, `@NotBlank`
  * [[Spring Boot] @NotNull, @NotEmpty, @NotBlank 의 차이점 및 사용법](https://sanghye.tistory.com/36)
* **[N/A]** `org.apache.commons.lang3` - Apache Commons Lang 어노테이션은 아니고 메소드
  * [org.apache.commons:commons-lang3:3.0](https://commons.apache.org/proper/commons-lang/apidocs/index.html)
