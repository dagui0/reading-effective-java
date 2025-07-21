# 이펙티브 자바 (3판) - 9장 일반적인 프로그래밍 원칙

## 목차

* [**아이템 57**: 지역변수의 범위를 최소화하라](#아이템-57-지역변수의-범위를-최소화하라)
* [**아이템 58**: 전통적인 for문보다는 for-each 문을 사용하라](#아이템-58-전통적인-for문보다는-for-each-문을-사용하라)
* [**아이템 59**: 라이브러리를 익히고 사용하라](#아이템-59-라이브러리를-익히고-사용하라)
* [**아이템 60**: 정확한 답이 필요하다면 `float`과 `double`은 피하라](#아이템-60-정확한-답이-필요하다면-float과-double은-피하라)
* [**아이템 61**: 박싱된 기본 타입보다는 기본 타입을 사용하라](#아이템-61-박싱된-기본-타입보다는-기본-타입을-사용하라)
* [**아이템 62**: 다른 타입이 적절하다면 문자열 사용을 피하라](#아이템-62-다른-타입이-적절하다면-문자열-사용을-피하라)
* [**아이템 63**: 문자열 연결은 느리니 주의하라](#아이템-63-문자열-연결은-느리니-주의하라)
* [**아이템 64**: 객체는 인터페이스를 사용해 참조하라](#아이템-64-객체는-인터페이스를-사용해-참조하라)
* [**아이템 65**: 리플렉션보다는 인터페이스를 사용하라](#아이템-65-리플렉션보다는-인터페이스를-사용하라)
* [**아이템 66**: 네이티브 메서드는 신중히 사용하라](#아이템-66-네이티브-메서드는-신중히-사용하라)
* [**아이템 67**: 최적화는 신중히 하라](#아이템-67-최적화는-신중히-하라)
* [**아이템 68**: 일반적으로 통용되는 명명 규칙을 따르라](#아이템-68-일반적으로-통용되는-명명-규칙을-따르라)

## 아이템 57: 지역변수의 범위를 최소화하라

* 지역변수 범위를 줄이라는 말은 '클래스와 멤버의 접근 권한을 최소화하라'([아이템 15](chapter04.md#아이템-15-클래스와-멤버의-접근-권한을-최소화하라))와 비슷
  * 가독성과 유지보수성이 높아지고 오류 가능성은 낮아짐
* 지역변수를 사용하기 직전에 선언하기
  * 지역변수를 함수 서두에 선언하는 것은 C언어의 오랜 관습
  * 지역변수를 미리 선언하면 지역변수의 생명력이 길어지게됨 -> 사용할 블럭에서 선언하도록
* 지역변수는 선언하는 동시에 초기화해야함. 초기화할 수 없는 지역변수는 아직 선언할 준비가 안되었다는 뜻
  * try-catch 블럭만 제외임

### `while`보다는 `for`가 좋다

* 반복 변수가 블럭 내로 제한되기 때문

```java
void main() {
    for (Element e: coll) {
        // ...
    }

    for (Iterator<Element> it = coll.iterator(); i.hasNext(); ) {
        Elemement e = it.next();
        // ...
    }
    
    for (int i = 0, n = coll.size(); i < n; i++) {
        Element e = coll.get(i);
        // ...
    }

    Iterator<Element> i = c1.iterator();
    while (i.hasNext()) {
        Element e = i.next();
        // ...
    }

    Iterator<Element> i2 = c2.iterator();
    while (i.hasNext()) {
        Element e = i2.next();
        // ...
    }
}
```

### 메서드를 작고 한가지 일만 하도록 만들라

* 메서드가 여러가지 일을 하게 되면, 하나의 작업이 다른 작업용 지역변수에도 접근할 수 있게 함
* 배열과 컬렉션의 코드가 다르다

## 아이템 58: 전통적인 for문보다는 for-each 문을 사용하라

* `while`문 보다 `for`문이 좋지만, 역시 인덱스 변수는 코드를 복잡하게 하고 오류의 가능성을 높인다.

### 향상된 `for` 문(Enhanced `for` statement, for-each 문)

```java
for (Element e: elements) {
    // ...
}
```

`:`(colon)은 in 으로 읽는다. 'elements 안의 모든 e에 대해'

```kotlin
// Kotlin
for (e: Element in elements) {
    // ...
}
```
```javascript
// JavaScript
for (let e of elements) {
    // ...
}
for (let key in obj) {
  // ...
}
```
```python
# Python
for e in elements:
    # ...
for key, value in dict.items():
    # ...
```

### for-each 문의 힘

* 버그를 찾아보자

```java
enum Suit { CLUB, DIAMOND, HEART, SPADE }
enum Rank { ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING }
record Card(Suit suit, Rank rank) {}
static Collection<Suit> suits = EnumSet.allOf(Suit.class);
static Collection<Rank> ranks = EnumSet.allOf(Rank.class);

List<Card> deck = new ArrayList<>();
for (Iterator<Suit> i = suits.iterator(); i.hasNext(); )
    for (Iterator<Rank> j = ranks.iterator(); j.hasNext(); )
        deck.add(new Card(i.next(), j.next()));
```

* for-each 문을 사용하면 버그를 만들고 싶어도 만들기가 힘듬

```java
for (Suit suit: suits)
    for (Rank rank: ranks)
        deck.add(new Card(suit, rank));
```

### for-each 문을 사용할 수 없는 경우

* 파괴적인 필터링(destructive filtering) - 컬렉션을 순회하면서 필요 없는 것을 제거하는 경우
* 변형(transformation) - 컬렉션을 순회하면서 값을 변경
* 병렬 반복(parallel iteration) - 여러 컬렉션을 병렬로 순회

(하지만 `Stream`이 출동하면 어떨까?)

### `Iterable`

* 컬렉션을 구현하지 않더라도, `Iterator`를 만들고 `Iterable` 인터페이스를 구현하게 하면 for-each문을 사용 가능

```java
class GangOfFour implements Iterable<String> {
    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            private int i = 0;
            @Override
            public boolean hasNext() {
                return i < 4;
            }
            @Override
            public String next() {
                return switch (i++) {
                    case 0 -> "Erich Gamma";
                    case 1 -> "Richard Helm";
                    case 2 -> "Ralph Johnson";
                    case 3 -> "John Vlissides";
                    default -> throw new NoSuchElementException();
                };
            }
        };
    }
}

for (String member : new GangOfFour()) {
        System.out.println(member);
}
```

## 아이템 59: 라이브러리를 익히고 사용하라

> 바퀴를 재발명하지 마세요(Don't reinvent the wheel)

```java
static Random rnd = new Random();
static int random(int n) {
    return Math.abs(rnd.nextInt()) % n;
}
```

* 언듯 그럴듯해 보이는 코드지만 문제가 있음
  * `n`이 2의 제곱수이고 크지 않을 경우, 같은 수열이 반복됨
  * `n`이 2의 제곱수가 아니면 몇면 숫자가 더 자주 나옴, `n`이 크면 이 현상이 더 심해짐
  * 만약 `rnd.nextInt()`가 `Integer.MIN_VALUE`가 나온 경우, 음수가 나올 수 있음

```java
@Test
public void testRandom2() {

    int max = 2 * (Integer.MAX_VALUE / 3);
    int count = 1000000;

    int low = 0;
    for (int i = 0; i < count; i++) {
        if (random(max) < max / 2)
            low++;
    }

    System.out.println("Under middle value: " + low); // 667262
}
```

* `random(int)` 메서드는 사실 새로 만들 필요 없고 `Random.nextInt(int)` 메서드를 사용하면 됨
* `Random` 대신에 `ThreadLocalRandom`을 사용해야 함(Java 7+) - 더 랜덤하고 더 빠름
  * Fork-Join Pool이나 병렬 스트림에서는 `SplittableRandom`을 사용(Java 8+)

### 표준 라이브러리의 장점

* 단순하게 필요한 기능도 의외로 복잡한 코드가 필요할 수 있음(알고리즘, 예외 등)
* 기능과 직접 관련 없는 코드를 만들지 않아도 됨
* 표준 라이브러리는 내가 노력하지 않아도 성능이 개선됨
* 기능이 점점 더 풍부해지고 편리해짐
* 내가 작성한 코드를 다른 사람이 이해하기 쉬워짐

> 이상의 이점들에 비춰볼 때 표준 라이브러리의 기능을 사용하는 것이 좋아보이지만,
> 실상은 많은 프로그래머가 직접 구현해서 쓰고 있다. 왜 그럴까?
> **아마도 라이브러리에 그런 기능이 있는 지를 모르기 때문일 것이다.**

```java
// 초간단 curl 명령
void curl(String url) throws IOException {
    try (InputStream in = new URL(url).openStream()) {
        in.transferTo(System.out); // Java 9+
    }
}
```

* 표준 라이브러리가 방대하여 다 알 수 없다 해도,
  `java.lang`, `java.util`, `java.io`는 모두 외워라 (THIS IS SPARTA!)
* 다 외우고 나면 컬렉션 API, 스트림 API를 외워라 (THIS IS SPARTA!)
* `java.util.concurrent`는 동시성 처리를 위한 저수준, 고수준 기능을 제공함([아이템 80](#아이템-80-스레드보다는-실행자-태스크-스트림을-애용하라), [아이템 81](#아이템-81-wait와-notify보다는-동시성-유틸리티를-애용하라))
* 원하는 기능이 표준 라이브러리에 없다면, 서드파티 라이브러리(Guava 등)를 고려하고 그래도 없으면 직접 구현하라

## 아이템 60: 정확한 답이 필요하다면 `float`과 `double`은 피하라

```java
void main() {
    
    final BigDecimal TEN_CENTS = new BigDecimal("0.10");
    
    int itemBought = 0;
    BigDecimal funds = new BigDecimal("1.00");
    
    for (BigDecimal price = TEN_CENTS;
        funds.compareTo(price) >= 0;
        price = price.add(TEN_CENTS)
    ) {
        funds = funds.subtract(price);
        itemBought++;
    }
    System.out.println("구입한 물건의 개수: " + itemBought);
    System.out.println("남은 돈: " + funds);
}
```
자세한 설명은 생략한다.

## 아이템 61: 박싱된 기본 타입보다는 기본 타입을 사용하라

* 오토박싱과 오토 언박싱으로 서로 호환되어 사용될 수 있으나([아이템 6](#아이템-6-불필요한-객체-생성을-피하라))
  기본 타입(primitive type)과 박싱된 객체 타입(boxed type)은 다름
  * 박싱된 타입은 값에 더해서 식별성을 가짐 `new Integer(1) != new Integer(1)`
  * 기본 타입은 항상 유효한 값을 가지지만, 박싱된 타입은 `null`을 가질 수 있음
  * 박싱된 타입 보다 기본 타입이 압도적으로 성능이 좋음(시간, 메모리 사용)
* **오토 박싱/언박싱은 번거로움을 줄여주지만, 위험성을 줄여주지는 않는다.** 

### 오토 언박싱이 안되는 경우

```java
@Test
public void testComparator() {

    Comparator<Integer> comparator =
            (i, j) -> (i < j)? -1: (i == j)? 0: 1;
  
    assertEquals(1, comparator.compare(new Integer(42), new Integer(42))); // 같지 않다고?
    assertEquals(0, comparator.compare(Integer.valueOf(42), Integer.valueOf(42)));
}
```

* `i < j`는 오토언박싱이 적용되어 정상적으로 계산됨
* `i == j` 부분은 `new Integer(42) == new Integer(42)`로 계산되어 `false`로 평가됨

```java
Comparator<Integer> c1 = (iBoxed, jBoxed) -> {
  int i = iBoxed, j = jBoxed;
  return (i < j) ? -1 : (i > j) ? 1 : 0;
};
Comparator<Integer> c2 = Comparator.nutralOrder();
Comparator<Integer> c3 = Integer::compareTo;
```

### 오토 언박싱이 되는 경우

```java
public class Unbelievable {
    static Integer i;
    
    public static void main(String[] args) {
        if (i == 42)
            System.out.println("믿을 수 없군!");
    }
}
```

* `Integer`와 `int`가 섞여서 연산이 될 경우,  `Integer`를 `int`로 언박싱함
* `null`을 `int`로 언박싱하다가 `NullPointerException`이 발생함

### 오토 박싱이 되는 경우

```java
public static void main(String[] args) {
    Long sum = 0L;
    for (long i = 0; i <= Integer.MAX_VALUE; i++) {
        sum += i;
    }
}
```

### 박싱된 타입은 언제 사용해야 하는가?

* 컬렉션에 저장할 때 `List<Integer>`, `Map<String, Integer>`, `ThreadLocal<Integer>` 등
* 타입 매개변수로 사용할 때 `Comparator<Integer>`, `Function<Integer, String>` 등
* 리플렉션 API를 사용할 때. `setter.invoke(obj, Integer.valueOf(42))` 등

### [토론] Database 에서 조회한 값을 저장할 때 박싱된 타입(`Integer`)을 사용하는 것이 좋을까?

## 아이템 62: 다른 타입이 적절하다면 문자열 사용을 피하라

* 문자열은 문자열에만 사용하라
  * I/O 채널을 통해서 입력받은 값은 문자열 형태로 전달될 수밖에 없다
  * 데이터 타입에 맞춰서 적절한 타입으로 변환하여 저장하라

### 문자열로 저장하기 적절하지 않은 데이터

* 열거 타입
  * 컴파일러가 오타를 검사해 줄 수 없음([아이템34](chapter06.md#아이템-34-int-상수-대신-열거-타입을-사용하라))
* 혼합 타입
  * 여러 값의 복합 데이터(복합 키, Compound Key): `memberNo + ":" + addressSeq`
    * 개별 요소를 확인하기 위해서는 파싱을 해야함
    * 구분자의 이스케이프 문제도 고려해야 함
    * `private` 정적 멤버 클래스를 사용하는 것이 나음([아이템24](chapter04.md#아이템-23-태그-달린-클래스보다는-클래스-계층구조를-활용하라))
      * `private record AddressKey(long memberNo, long addressSeq) {}`

### 권한(Capacity, Capability)의 표현

* `java.lang.ThreadLocal`(Java 1.2+)이 도입되기 이전.
  * 많은 개발자들이 `ThreadLocal`의 기능을 위해 고민했고, 아래 설계에 도달함

```java
public class ThreadLocal1 {
    private ThreadLocal1() {}
    public static void set(String key, Object value) {}
    public static Object get(String key) {}
}
```

* 이 설계의 문제는 Thread 간 동일 키를 사용할 수 있다는 점
  * Key를 독자적인 타입으로 바꿔서 다른 스레드가 위조할 수 없도록 함(capacity)

```java
public class ThreadLocal2 {
    private ThreadLocal2();
    private static class Key { Key() {} }
    private static Key getKey() { return new Key(); }
    public static void set(String key, Object value) {}
    public static Object get(String key) {}
}
```

* 생각해보니
  * 독자적인 Key가 그냥 `ThreadLocal`이 되면 됨

```java
public final class ThreadLocal3 {
    public ThreadLocal3();
    public void set(Object value);
    public Object get();
} 
```

* 최종 버전

```java
public class ThreadLocal<T> {

    private static final Map<Thread, Map<ThreadLocal<?>, Object>> values
            = new ConcurrentHashMap<>();

    public ThreadLocal() {}

    public void set(T value) {
        Thread currentThread = Thread.currentThread();
        values.computeIfAbsent(currentThread, k -> new ConcurrentHashMap<>())
             .put(this, value);
    }

    @SuppressWarnings("unchecked")
    public T get() {
        Thread currentThread = Thread.currentThread();
        Map<ThreadLocal<?>, Object> threadValues = values.get(currentThread);
        if (threadValues != null) {
            return (T) threadValues.get(this);
        }
        return null;
    }
}
```

* `java.lang.ThreadLocal`과의 차이점
  * 표준 라이브러리의 `ThreadLocal`은 `Thread`마다 가지고 있는 `ThreadLocalMap`을 사용
    * 값을 쓰거나 할 때 쓰레드간 경합이 없음
    * 스레드가 종료될 때 `ThreadLocalMap`이 제거되도록 함
  * `ThreadLocal`과 `ThreadLocalMap`의 연결은 `WeakReference`를 사용


## 아이템 63: 문자열 연결은 느리니 주의하라

* 자세한 설명은 생략한다
  * JVM이 문자열 최적화에 힘을 많이 쓰고 있으나, 그래도 `StringBuilder`가 빠름

## 아이템 64: 객체는 인터페이스를 사용해 참조하라

* 매개변수 타입을 클래스가 아닌 인터페이스로 하라고 했음([아이템51](chapter08.md#아이템-51-메서드-시그니처를-신중히-설계하라)) **지역변수도 그렇게 하라!**
* 단, 구현 클래스가 특별히 추가된 메서드를 제공하는 경우는 어쩔 수 없음(`BufferedReader.readLine()` 등)
* 결론: 지역변수 선언시에는 가능한한 덜 구체적인 상위 타입을 선택하라.

## 아이템 65: 리플렉션보다는 인터페이스를 사용하라

> With great power, comes great responsibility
> 
> - Uncle Ben

* 리플렉션(Reflection API)의 장단점
  * 장점
    * 아무 클래스에나 원하는 만큼의 접근이 가능
    * 컴파일 시에 없던 클래스(외부 코드)도 실행이 가능함
    * 제한적이긴 하나 접근 권한 오버라이드 가능(`method.setAccessible(true)`)
  * 단점
    * 컴파일타임 검사의 잇점을 쓸 수 없음
    * 코드가 장황해지고 이해하기 힘들어짐
    * 성능이 떨어짐. 많이 떨어짐
* 올바른 리플렉션 사용법
  * 인스턴스 생성시에만 쓰고 상위 인스턴스나 클래스 참조로 받아서 사용
    * 총 6종의 예외 처리가 필요 - `ReflectiveOperationException`(Java 7+)의 하위 예외
    * 생성자 호출로 한 줄로 될 기능에 수많은 코드가 필요함

```java
@SuppressWarnings("unchecked")
<T> Set<T> createSet(String className) {
  
    Class<?> c = null;
    try {
        c = Class.forName(className);
    }
    catch (ClassNotFoundException e) { // 클래스가 없음
        throw new RuntimeException("Class not found: " + className, e);
    }
  
    // ClassCastException 을 피하기 위해서 미리 검사할 경우
    // if (!Set.class.isAssignableFrom(c)) {
    //     throw new RuntimeException("Not a Set: " + className);
    // }
  
    Constructor<? extends Set<T>> cons = null;
    try {
        cons = (Constructor<? extends Set<T>>)c.getDeclaredConstructor();
    }
    catch (NoSuchMethodException e) { // 맞는 생성자가 없음
        throw new RuntimeException("No default constructor: " + className, e);
    }
  
    // IllegalAccessException 을 미리 우회하고자 할 경우
    // try {
    //     cons.setAccessible(true);
    // }
    // catch (SecurityException e) {
    //     throw new RuntimeException("Can't access constructor: " + className, e);
    // }
  
    try {
        return cons.newInstance(); // 생성자 호출
    }
    catch (IllegalAccessException           // 생성자에 접근할 수 없음
           | InstantiationException         // 객체 생성 실패
           | InvocationTargetException      // 생성자에서 예외 발생
           | ClassCastException e) {        // 타입이 호환 안됨
        throw new RuntimeException("Can't instantiate: " + className, e);
    }
}
```

### 리플렉션의 동적 호출 성능 개선

* Method Handle API (Java 7+)
  * 리플렉션 API의 성능 문제를 해결하기 위해 도입됨
    * 리플렉션은 `invoke()` 실행시 마다 권한 검사를 수행하고, 이것이 속도에 큰 영향을 줌
  * `java.lang.invoke` 패키지에 포함되어 있음
  * 메서드 핸들(Method Handle)을 사용하여 메서드를 동적으로 호출할 수 있음
    * 메서드 핸들은 생성시에 권한 검사를 하고, 이후에는 수행하지 않음
* LambdaMetaFactory (Java 8+)
  * 메서드 핸들을 사용하여 람다 표현식을 생성할 수 있음
  * `java.lang.invoke.LambdaMetafactory` 클래스를 사용하여 람다 표현식을 생성함
  * 성능이 매우 뛰어나며, 컴파일이 완료된 이후에는 거의 네이티브 수준 성능이 나옴

```java
// Reflection을 이용한 객체 프로퍼티 접근자
record ReflectionProperty(String name,
                          Method getter,
                          Method setter) implements PropertyHandle {

    public ReflectionProperty(String name, Method getter, Method setter) {
        this.name = name;
        this.getter = getter;
        this.setter = setter;

        try {
            if (getter != null)
                getter.setAccessible(true);
            if (setter != null)
                setter.setAccessible(true);
        } catch (SecurityException e) {
            throw new PropertyMapException("Cannot access property: " + name, e);
        }
    }
}

// Method Handle을 이용한 객체 프로퍼티 접근자
record MethodHandleProperty(String name,
                            MethodHandle getter,
                            MethodHandle setter) implements PropertyHandle {

    public static MethodHandleProperty of(ReflectionProperty reflectionProperty, MethodHandles.Lookup lookup) {
        try {
            return new MethodHandleProperty(reflectionProperty.name(),
                    reflectionProperty.getter() == null ? null :
                            lookup.unreflect(reflectionProperty.getter()),
                    reflectionProperty.setter() == null ? null :
                            lookup.unreflect(reflectionProperty.setter()));
        } catch (IllegalAccessException e) {
            throw new PropertyMapException(e);
        }
    }
}

// Lambda 를 이용한 객체 프로퍼티 접근자
record LambdaProperty(String name,
                      Function<Object, Object> getter,
                      BiConsumer<Object, Object> setter) implements PropertyHandle {

    public static LambdaProperty of(MethodHandleProperty mhProperty, MethodHandles.Lookup lookup) {
        try {
            return new LambdaProperty(mhProperty.name(),
                    createGetter(mhProperty, lookup),
                    createSetter(mhProperty, lookup));
        } catch (Throwable e) {
            throw new PropertyMapException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static Function<Object, Object> createGetter(MethodHandleProperty mhProperty, MethodHandles.Lookup lookup) throws Throwable {
        if (mhProperty.getter() == null) {
            return null;
        }
  
        MethodHandle getterHandle = mhProperty.getter();
        MethodType targetType = MethodType.methodType(Object.class, Object.class);
        CallSite site = LambdaMetafactory.metafactory(
                lookup,
                "apply",
                MethodType.methodType(Function.class),
                targetType,
                getterHandle,
                getterHandle.type().changeReturnType(Object.class)
        );
        return (Function<Object, Object>) site.getTarget().invoke();
    }
}
```

## 아이템 66: 네이티브 메서드는 신중히 사용하라

* 자바 네이티브 인터페이스(Java Native Interface, JNI)를 사용하는 이유
  * 자바에서 네이티브 코드를 호출하는 방법
  * 레지스트리 같은 시스템 특화 기능 사용
  * 기존에 작성된 네이티브 라이브러리 사용(게임 엔진 등)
  * 성능 개선을 위해 많이 사용되는 부분 최적화
* JNI를 사용할 필요가 없는 이유
  * Java의 버전이 올라가면서 시스템 지원기능이 점차 많아지고 있음. Process API(java 9+)
  * 성능 개선을 위해 네이티브 코드를 사용하는 경우(Java 1.3 이전)는 더 이상 권장되지 않음
    * 하지만 정말 고성능을 제공하는 라이브러리가 있다면 고려할만함 (Gnu MP 등)
* 네이티브 코드를 사용할 경우 단점
  * 메모리 오류로 부터 자유로울 수 없다.
  * 이식성이 떨어짐 (네이티브 코드의 이식성에 제한)
  * 디버깅이 어려움
  * 자바와 네이티브 컨텍스트의 전환 비용도 존재함

## 아이템 67: 최적화는 신중히 하라

> More computing sins are committed in the name of efficiency (without necessarily achieving it) than for any other single reason - including blind stupidity.
> 
> - William Wulf
> 
> (맹목적인 어리석음을 포함해) 그 어떤 핑계보다 효율성이라는 이름 아래 행해진 컴퓨팅 죄악이 더 많다(심지어 효율을 높이지도 못하면서)
> 
> - 윌리엄 울프

> We should forget about small efficiencies, say about 97% of the time: premature optimization is the root of all evil. Yet we should not pass up our opportunities in that critical 3%.
>
> - Donald Knuth
>
> (전체의 97% 정도인) 자그마한 효율성은 모두 잊자. 섣부른 최적화가 만악의 근원이다.
> 
> - 도널드 크누스

> The First Rule of Program Optimization: Don't do it.
> 
> The Second Rule of Program Optimization (for experts only!): Don't do it yet.
>
> - Michael A. Jackson
>
> 최적화를 할 때는 다음 두 규칙을 따르라. \
> 첫 번째, 하지마라. \
> 두 번째, (전문가 한정) 아직 하지 마라. 다시 말해, 완전히 명백하고 최적화되지 않은 해법을 찾을 때 까지는 하지 마라.
> 
> - 마이클 A. 잭슨

* 윌리엄 울프(William Wulf, 1939~2023)
  * 컴파일러 최적화와 프로그래밍 언어 분야의 선구자, 미국 국립 공학 학회(National Academy of Engineering)의 회장 역임
  * 초창기 컴퓨터 과학 박사 학위 수여자 중 한 명으로, 여러 운영체제, 프로그래밍 언어(BLISS), 그리고 최적화 컴파일러 개발에 크게 기여
* 도널드 크누스(Donald Knuth, 1938~)
  * "현대 컴퓨터 과학의 아버지", 알고리즘의 바이블 "The Art of Computer Programming"
* 마이클 A. 잭슨(Michael A. Jackson, 1936~)
  * 소프트웨어 개발 방법론의 대가. 잭슨 구조적 프로그래밍(JSP)과 잭슨 시스템 개발(JSD) 방법론 창시 

### 빠른 프로그램 보다는 좋은 프로그램을 작성하라

* 바람직한 프로그램을 작성하면, 설사 성능이 안나온다고 해도 개선할 수 있는 길이 보임
* 정보 은닉 원칙을 철저하게 지켰다면 부분적으로 개선이 가능함
* 성능 개선을 제한하는 구조를 만들지 말아야 함
  * 아키텍처의 결함이 성능을 제한하는 경우라면 시스템 전체를 다시 작성할 수 밖에 없음
  * 변경하기 가장 어려운 부분은 소통과 관련된 부분임: 컴포넌트간의 통신 방식, 저장 형식, 네트워크 프로토콜 등
  * `public` 가변 객체를 남발하면, 방어적 복사를 위해 성능을 깎아먹게 됨
  * 컴포지션(compsition)으로 할 수 있는 것을 상속을 하게되면, 상위 클래스의 성능 제약을 물려받게됨
* 잘 된 설계는 성능도 잘 나오는게 보통임. 위 격언들은 대부분의 경우 잘 된 설계만으로 충분하다는 의미
* 잘 설계된 API를 성능을 위해 예외를 추가하거나 왜곡하는 것은 가장 안좋음

#### 안좋은 설계가 성능을 깎아먹는 사례

* `java.awt.Component`
  * `Dimension getSize()` 의 경우 `Dimension`이 불변이 아니기 때문에 방어적 복사가 필수적임
  * `getWidth()`와 `getHeight()`가 추가되었으나 `getSize()`를 물릴 방법은 없음

### 최적화를 할 경우 주의할 점

1. 하지 마라
2. 아직 하지마라
3. 최적화 전후 성능 측정을 통해서 성과를 비교하라
   * 최적화 결과가 별로 차이가 없거나, 오히려 나빠질 경우가 더 많음

> 자바는 프로그래머가 작성하는 코드와 CPU가 수행하는 명령 사이의 '추상화 격차'가 커서
> 최적화로 인한 성능 변화를 일정하게 예측하기 어렵다. 그래서인지 **최적화와 관련해
> 일부만 맞거나 터무니없는 미신들이 떠돌아다닌다.**

* 자바의 성능 모델은 시스템, 프로세서, 릴리즈 마다 달라짐
* 일반적으로 통용되는 최적화 기법은 구조를 잘 설계하는 것이라고 할 수 있음

## 아이템 68: 일반적으로 통용되는 명명 규칙을 따르라

* 자바 언어 명세에는 명명 규칙이 상세하게 정의되어 있음
* 이들 규칙을 잘 따라야 가독성이 좋아지고, 개발자들간 소통이 원활해짐

### 철자 규칙

* 패키지, 클래스, 인터페이스, 메서드, 필드, 타입변수의 이름을 짓는 방법
* 패키지
  * `.`으로 구분된 소문자로 계층적으로 구성
  * 대외적으로 배포될 패키지라면 도메인명 역순을 붙여서 식별 가능하게 해야함
  * 짧게 하는 것이 좋음(8자 이하, 약어 사용도 고려. `utilities` -> `util`)
  * 패키지의 계층 구조는 의미상 포함관계를 가지지만, 기능상 관련은 없음
* 클래스, 인터페이스, 열거타입 등
  * 대문자로 시작
  * 정말 널리 사용되는 약어(`min`, `max` 등)가 아니면 줄이지 않음
  * 이니셜로 만들어진 약어를 모두 대문자로 표기할 것인가? 일반 단어처럼 취급하는 것이 대세 (`URLDataSet` -> `UrlDataSet`)
* 메서드, 필드
  * 소문자로 시작한다는 것을 빼면 클래스와 동일
  * 단, 상수 필드는 대문자와 `_`로 지음
    * `static final` 이면서 기본 타입 또는 불변 객체인 경우
    * `static final Pattern HTTP_ERROR_PATTERN = Pattern.compile("^[45]")`
* 지역변수
  * 메서드, 필드와 동일한데 약어 사용 가능
* 타입 매개변수
  * 대문자 한 글자로 표현 (`T`ype, `E`lement, `K`ey, `V`alue, E`X`ception, `R`esult 등)
  * 임의의 타입이 여러개인 경우: `T`, `U`, `V` or `T1`, `T2`, `T3`

### 문법 규칙

* 클래스
  * 기본적으로 단수 명사 (`Thread`, `PriorityQueue`, `StringBuilder` 등)
  * 인스턴스화 불가능한 클래스는 복수 명사 (`Collections`, `Arrays` 등)
* 인터페이스
  * 클래스에 준해서 정하거나(`Collection`, `Comparator`), 형용사형으로 함 (`Runnable`, `Iterable` 등)
* 애너테이션
  * 개판임 (`@BindingAnnotation`, `@Inject`, `@Singleton` 등)
* 메서드
  * 동사 또는 동사+목적어 (`append()`, `appendTo()`, `appendAll()` 등)
  * `boolean` 반환 타입의 메서드는 `is`, `has`로 시작 가능 (`isEnabled()`, `hasChildren()` 등)
  * JavaBeans 명세
    * `get`, `set`, `is`로 시작하는 메서드의 집합을 프로퍼티(속성)으로 인식
  * 타입 변환(casting)
    * `toString()`, `toArray()`, `toList()` 등
    * 다른 형식의 뷰를 반환하는 경우 `asList()` 등
    * 기본 타입으로 변환하는 경우 `intValue()`, `longValue()` 등
  * 정적 팩터리 메서드
    * `of()`, `from()`, `valueOf()`, `getInstance()` 등
* 필드
  * 필드에 대한 사항은 별로 특별한게 없음 - 필드를 노출할 일이 없다고 생각해서인 듯
  * `boolean` 필드는 `is`, `has` 없이 그냥 형용사나 분사형을 사용
    * `boolean isEnabled` -> `boolean isIsEnabled()` (좋지 안타)

### 모던 자바가 상속 트리를 API를 제공하는 방법

* Java 초기에는 `AbstractList`와 `ArrayList` 같이 추상 클래스와 구현체를 같이 제공함
  * 이 때는 상속을 통한 기능 확장을 권장하는 분위기였음
* 최근의 API는 `Collectors.groupingBy()` 같이 인터페이스만 노출하고 구현체는 팩터리 메서드로 제공됨
  * 인터페이스 하나만 잘 익히면 되도록 함
  * 다양한 구현체는 `List.of()` 같이 인자에 따라서 알아서 선택되게 하여 편의성을 높임
  * 구체적인 구현 클래스를 사용하지 못하도록 함([아이템64](#아이템-64-객체는-인터페이스를-사용해-참조하라))
