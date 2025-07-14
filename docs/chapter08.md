# 이펙티브 자바 (3판) - 8장 메서드

## 목차

* [**아이템 49**: 매개변수가 유효한지 검사하라](#아이템-49-매개변수가-유효한지-검사하라)
* [**아이템 50**: 적시에 방어적 복사본을 만들라](#아이템-50-적시에-방어적-복사본을-만들라)
* [**아이템 51**: 메서드 시그니처를 신중히 설계하라](#아이템-51-메서드-시그니처를-신중히-설계하라)
* [**아이템 52**: 다중정의는 신중히 사용하라](#아이템-52-다중정의는-신중히-사용하라)
* [**아이템 53**: 가변인수는 신중히 사용하라](#아이템-53-가변인수는-신중히-사용하라)
* [**아이템 54**: `null`이 아닌 빈 배열이나 컬렉션을 반환하라](#아이템-54-null이-아닌-빈-배열이나-컬렉션을-반환하라)
* [**아이템 55**: 옵셔널 반환은 신중히 하라](#아이템-55-옵셔널-반환은-신중히-하라)
* [**아이템 56**: 공개된 API요소에는 항상 문서화 주석을 작성하라](#아이템-56-공개된-api요소에는-항상-문서화-주석을-작성하라)

## 아이템 49: 매개변수가 유효한지 검사하라

* 빠른 실패 전략(Fast failure)
  * 가능한 빨리 오류를 발생시키면, 오류의 원인을 찾을 대상 코드의 양이 줄어듬
* 실패의 원자성(Failure atomicity) [아이템 76](chapter10.md#아이템-76-가능한-한-실패-원자적으로-만들라)
  * 오류가 발생한 이후의 객체 상태는 정상적으로 사용 가능한 상태여야함(불변식이 깨진 상태여서는 안됨)

* `public`, `protected` 메서드는 반드시 입력값 오류에 대한 문서화를 해야 한다.
  * `IllegalArgumentException`
  * `IndexOutOfBoundsException`
  * `NullPointerException`

```java
/**
 * Returns the result of this BigInteger modulo m.
 * <p>
 * The result is in the range 0 <= result < m.
 *
 * @param m the modulus, must be positive
 * @return this BigInteger modulo m
 * @throws ArithmeticException if m is not positive
 */
public BigInteger mod(BigInteger m) {
    if (m.signum() <= 0)
        throw new ArithmeticException("계수(m)은 양수여야 합니다: " + m);
    return mod(m, m.bitLength());
}
```

* `java.util.Objects`
  * `null` 검사
    * `Objects.requireNonNull(obj [, messge])`: `if (obj == null) throw new NullPointerException(message);`
    * `Objects.isNull(obj)`: `obj == null;`
    * `Objects.nonNull(obj)`: `obj != null;`
    * `Objects.requireNonNullElse(obj, defaultObj)`: `(obj == null)? defaultObj: obj`
    * `Objects.requireNonNullElseGet(obj, supplier)`: `(obj == null)? supplier.get(): obj`
  * 배열 인덱스 검사 (inclusive ~ exclusive)
    * `Objects.checkFromIndexSize(from, size, length)`: `from >=0 && from + size <= length`
    * `Objects.checkFromToIndex(from, to, length)`: `from >= 0 && to >= from && to <= length`
    * `Objects.checkIndex(index, length)`: `index >= 0 && index < length`

### 단언문(`assert` 문)

```java
private static void sort(long[] a, int offset, int length) {
    assert a != null;
    assert offset >= 0 && offset <= a.length;
    assert length >= 0 && offset + length <= a.length;
    
    // 계산 수행
    // ...
}
```

* `assert` 문은 어떤 작업을 시작하기 전/후에 이런 상태여야 한다고 정의하고
  * 단언이 실패할 경우 `AssertionError`를 던진다.
* `assert`문은 디버깅용으로 사용한 `System.out.println`과 비슷하다.
  * 런타임에는 기본적으로 무시되며, JVM에 `-ea` 또는 `--enableassertions` 옵션을 주면 활성화된다.
  * Gradle 환경에서는 `test` 태스크에는 기본적으로 활성화되어 있지만, `run` 태스크에는 비활성화되어 있다.
  * 실행환경에서는 무시되고, 조건에 대한 문서화가 역할을 하므로 상당히 좋은 기능이다.

### 나중에 사용할 목적의 인수의 경우 반드시 검사

```java
static List<Integer> intArrayAsList(int[] a) {
    Objects.requireNonNull(a);
    
    return new AbstractList<Integer>() {
        @Override
        public Integer get(int index) {
            Objects.checkIndex(index, a.length);
            return a[index];
        }

        @Override
        public int size() {
            return a.length;
        }

        @Override
        public Integer set(int index, Integer element) {
            Objects.checkIndex(index, a.length);
            int oldValue = a[index];
            a[index] = element;
            return oldValue;
        }
    };
}
```

만약 `null`검사를 안했다면 나중에 리턴된 `List` 뷰를 사용할 때 `NullPointerException`이 발생하면 오류 지점을 찾기가 어렵다.

### 추가 고려 사항

* 입력값 검사를 생략하는 것이 나은 경우
  * 입력값 검사가 비용이 높고, 계산 과정에서 자연스럽게 검사가 되는 경우 (`sort` 등)
* 계산과정에서 유효성 검사가 이루어지지만 API가 명시한 예외와 다른 예외가 던져질 경우
  * API 명세에 따른 예외로 감싸는 것을 고려 (`NumberFormatException` => `IllegalArgumentException`)

## 아이템 50: 적시에 방어적 복사본을 만들라

```java
public class Period1 {

    private final Date start;
    private final Date end;

    public Period1(Date start, Date end) {
        if (start.compareTo(end) > 0) {
            throw new IllegalArgumentException("Start must not be greater than end");
        }
        this.start = start;
        this.end = end;
    }

    public Date start() {
        return start;
    }

    public Date end() {
        return end;
    }
}
```

* 필드를 `final`로 선언하고, 생성자에서 불변식 검사를 해서 안전한 클래스 처럼 보임
* 하지만 `Date`는 불변 클래스가 아니므로 생성자, 또는 접근자를 통한 참조값을 통해서 값이 변경될 수 있음
  * 이럴 때 '방어적 복사(defensive copy)'를 사용해서 불변성을 확보해야 원래 의도를 지킬 수 있음

```java
public class Period {

    private final Date start;
    private final Date end;

    public Period(Date start, Date end) {
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());

        if (start.compareTo(end) > 0) {
          throw new IllegalArgumentException("Start date must not be after end date");
        }
    }

    public Date start() {
        return new Date(start.getTime());
    }

    public Date end() {
        return new Date(end.getTime());
    }
}
```

* 복사본을 만든 이후에 복사본 객체로 불변식 검사를 하는 것이 '찰나의 위험'에서도 안전성을 보장한다.
  * 이렇게 시간차로 변조를 하는 공격 방법을 TOCTOU(Time-of-check to time-of-use) 공격이라고 한다.
* 복사본을 만들 때 `clone()`을 사용하지 않았는데, 이는 `Date`가 `final`이 아니기 때문
  * `clone`은 `Date`의 하위 클래스에 의해서 재정의될 수 있다
* 자료구조를 설계할 때는 객체 참조의 값이 변경될 수 있는지 여부를 항상 검토해야한다.
  * 객체가 내부적으로 `Set`이나 `Map`의 키로 사용된다면, 객체의 값이 변경될 경우 문제가 발생할 수 있음
* 값을 노출할때(접근자)에서도 리턴한 참조를 통해서 값이 변경될 수 있고, 그로 인해 객체의 불변식이 깨질 수 있다면
  * 접근자 메서드에서도 방어적 복사본을 만들어서 반환해야 한다.
* 되도록 불변 객체를 사용해야 방어적 복사본을 만드는 수고를 줄일 수 있다.
  * `Date` 대신 `Instant`, `LocalDate`, `LocalDateTime` 등 불변 클래스를 사용하자.

```java
public class Period {

    private final Instant start;
    private final Instant end;

    public Period(Instant start, Instant end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start must not be after end");
        }
        this.start = start;
        this.end = end;
    }

    public Instant start() {
        return start;
    }

    public Instant end() {
        return end;
    }
}
```

## 아이템 51: 메서드 시그니처를 신중히 설계하라

### 메서드 이름은 신중히

* 표준 명명 규칙 준수 ([아이템 68](chapter09.md#아이템-68-일반적으로-통용되는-명명-규칙을-따르라))
* 같은 패키지 내에서는 일관된 이름을 유지
* 자바 커뮤니티에서 널리 사용되는 이름
* 자바 표준 API에서 사용되는 이름

### 편의 메서드를 너무 많이 만들지 마라

* 너무 메서드가 많은 클래스는 배우기 힘들다. (만들고, 테스트하고, 유지보수하기도 힘들다)
* 너무 메서드가 많은 인터페이스는 구현하기 힘들다
* 약칭 메서드는 정말정말 자주 사용되는 경우가 아니면 만들지 마라

### 매개변수 목록은 짧게

* 매개변수는 4개 이하로 하라
* 매개변수가 너무 많으면 개발자는 문서를 항상 끼고 보면서 작업해야한다.
* 같은 타입의 변수가 연달아 나오는 경우 특히 해롭다
  * 잘못해서 순서를 바꿔 입력해도 컴파일이 됨

#### 매개변수 목록을 줄이는 방법

* 메서드를 여러 메서드로 쪼갠다. 직교성(orthogonality) 이 높아져서 메서드 수가 줄어들 수 있다
  * `subIndexOf(start, end, element)` => `subList(start, end).indexOf(element)`
  * `subSize(start, end)` => `subList(start, end).size()`
  * ...
* 여러 인자를 묶은 헬퍼 클래스(Parameter Class)를 만든다
  * `compareCard(rank1, suit1, rank2, suit2)` => `compareCard(new Card(rank1, suit1), new Card(rank2, suit2))`
  * 연관성이 높은 인자들을 클래스로 묶으면 내부 처리 로직도 깔끔하게 정리될 수 있다.
* 많은 수의 인자의 중요도가 서로 다른 경우(생략 가능한 인자가 많은 경우)
  * 빌더(Builder) 패턴 응용
    * setter로 값을 설정하고, 마지막에 `execute()`같은 메서드로 실행
  * 컨텍스트 객체(Context Class) 사용
    * 인자들을 하나의 객체에 때려 넣고, 호출자가 컨텍스트 객체를 만들어서 전달하게 함
    * 컨텍스트가 일련의 처리 과정중 유효하게 재사용 가능한 경우

### 매개변수의 타입은 클래스 보다 인터페이스가 낫다

* `HashMap` 보다는 `Map`을 사용
* `compare(String, String)` 보다는 `compare(Comparable, Comparable)`을 사용
* `setType(boolean)` 보다는 2개 짜리 열거 타입을 만들어서 사용 `setType(Type)`

```java
public enum TemperatureScape { FAHRENHEIT, CELSIUS }

void main() {
    Thermoometer.newInstance(true);     // 코드가 명확하지 않아진다
    Thermoometer.newInstance(TemperatureScape.FAHRENHEIT);
}
```

## 아이템 52: 다중정의는 신중히 사용하라

```java
public class CollectionClassifier {
    public static String classify(Set<?> s) { return "집합"; }
    public static String classify(List<?> l) { return "목록"; }
    public static String classify(Colleciton<?> c) { return "기타"; }
}
void main() {
    Collection<?>[] collections = {  
        new HashSet<String>(),
        new ArrayList<String>(),
        new HashMap<String, String>().values()
    };
    for (Collection<?> c: collections)
        System.out.println(CollectionClassifier.classify(c));
}
```

> 재정의한 메서드는 동적으로 선택되고, 다중정의한 메서드는 정적으로 선택된다.

* 다중 정의(오버로딩, Overloading) - 같은 이름의 메서드를 여러개 만드는 것
* 재정의(Overriding) - 상위 클래스의 메서드를 하위 클래스에서 재정의하는 것

### 지침

* 혼동을 줄이려면 매개변수의 수가 같은 다중정의 메서드는 만들지 마라
  * 타입에 따른 다중정의는 메서드 이름을 다르게 하는 것이 나을 수 있음
  * 생성자는 이름을 변경할 수 없지만 정적 팩토리 메서드를 활용할 수 있음 ([아이템1](chapter02.md#아이템-1-생성자-대신-정적-펙터리-메서드를-고려하라))
* 같은 수의 인자를 가진 다중정의의 경우 두개의 타입이 근본적으로 달라야 함
  * 서로 형 변환이 불가능한 경우 (`ArrayList(int)`와 `ArrayList(Collection)`)
  * '근본적으로 다르다' 또는 '관련이 없다'의 정의
    * `Object` 외의 클래스 타입과 배열 타입은 근본적으로 다르다. (`Object` vs `Object[]`)
    * 마찬가지로 인터페이스와 인터페이스의 배열 타입은 근본적으로 다르다. (`Serializable`, `Cloneable`은 제외)
    * 상속 트리의 연관이 없는 경우 (캐스팅이 불가능한 경우, `String` vs. `Throwable`) 관련이 없다고 함
    * 관련이 없는 클래스들은 근본적으로 다르다.

```java
@Test
public void testCollection() {

    Set<Integer> set = new TreeSet<>();
    List<Integer> list = new ArrayList<>();

    for (int i = -3; i < 3; i++) {
        set.add(i);
        list.add(i);
    }

    for (int i = 0; i < 3; i++) {
        set.remove(i);  // remove(Object o) 가 사용됨
        list.remove(i); // remove(int index) 가 사용됨
    }

    assertEquals(Set.of(-3, -2, -1), set);
    assertEquals(List.of(-2, 0, 2), list);
}
```

### 다중정의와 람다

```java
@Test
public void testRunnable() {

    new Thread(System.out::println).start();

    try (ExecutorService executor = Executors.newCachedThreadPool()) {
        // executor.submit(System.out::println);  // 컴파일 오류
    }
}
```

* `ExecutorService`에는 `submit(Runnable)`, `submit(Callable<T>)`  2개의 오버로드가 존재
  * `Runnable`: `() -> void`
  * `Callable<T>`: `() -> T`
* `System.out::println`: `println()`, `println(boolean)`, ... 중의 하나
* `submit`의 인자는 인자가 `void`인 람다이고, `println()`의 리턴 자료형은 `void`으므로 혼동이 없을 것 같이 보이나
  * 실제 컴파일러는 메서드를 결정하지 못하는 모호성(ambiguity)에 빠짐
    * `void println()`은 `Runnable`일 수도 있지만 `Callable<Void>`로 변환도 가능하여 선택을 못함
    * 이 상황에서 다른 후보가 있으면, 다른 후보도 검토하지만 인자가 다르므로 맞지 않음
    * 최종적으로 선택 가능한 후보는 2가지가 됨
  * 그런데 만약 `println()`이 다중 정의되지 않았고 한개만 있었다면, 오류는 발생하지 않았을 것임
    * 아래 예시에서 `MyClass.run()`은 다중정의가 없으므로, `run()`을 `Runnable`또는 `Callable<T>`로 변환 시도
    * `run()`은 `Callable<Void>`로 변환 가능하지만, `Runnable`과는 완전 동일하므로 `Runnable`이 선택됨

```java
@Test
public void testRunnable2() {
    
    class MyClass {
        public void run() {
            System.out.println("Hello, World!");
        }
    }
    
    MyClass o = new MyClass();
    try (ExecutorService executor = Executors.newCachedThreadPool()) {
        executor.submit(o::run);
    }
}
```

* 결론적으로 람다는 서로 다른 함수형 인터페이스라고 해도 근본적으로 다르지 않다는 점을 주의해야함
* **즉, 변환 가능한(유사한) 함수형 인터페이스를 가지는 다중정의를 하면 안된다.**
* 컴파일시에 `-Xlint:overloads` 옵션을 주면, 다중정의가 있는 경우 경고를 출력한다.

### Java API의 발전과 설계의 한계

* `contentEquals(StringBuffer)` (Java 1.4)
  * 나중에 `StringBuffer`, `StringBuilder`등을 포괄하는 `CharSequence`가 도입되고 (Java 5)
  * 이에 따라 `contentEquals(CharSequence)`가 추가됨

```java
// java.lang.String 소스코드
public boolean contentEquals(StringBuffer sb) {
    return contentEquals((CharSequence)sb);
}
public boolean contentEquals(CharSequence cs) {
    // ...
}
```

* 다중 정의가 전혀 다른 동작을 하는 경우도 있음

```java
@Test
public void testStringValueOf() {

    char[] chars = {'H', 'e', 'l', 'l', 'o'};

    assertEquals("Hello", String.valueOf(chars));
    assertEquals("[C@37fb0bed", String.valueOf((Object)chars));
}

// java.lang.String 소스코드
public static String valueOf(Object obj) {
    return (obj == null) ? "null" : obj.toString();
}
```

## 아이템 53: 가변인수는 신중히 사용하라

* 가변 인수(varargs)는 **`0`**개 이상의 인수를 배열로 받을 수 있
* 가변 인수 함수가 호출되면 인수의 개수 크기의 배열을 만들어서 함수에 제공함

### **`1`**개 이상의 인수를 받고 싶은 경우

```java
int sum(int... args) {
    if (args.length < 1) {
        // 컴파일시에 검사가 불가능하고, 정상적으로 컴파일된 코드가 예외를 던지게 됨
        throw new IllegalArgumentException("인수는 최소 1개 이상이어야 합니다.");
    }
    int result = 0;
    for (int arg : args) {
        result += arg;
    }
    return result;
}
int sum(int first, int... rest) {
    int result = first;
    for (int arg : rest) {
        result += arg;
    }
    return result;
}
```

### 배열 생성 비용을 절약하고 싶은 경우

* 95%는 5개 미만인 경우라면 개수별 오버로드를 제공하여 배열 생성 비용을 절약할 수 있음
* `EnumSet`의 사례

```java
// java.util.EnumSet 소스코드
public class EnumSet {
    public static <E extends Enum<E>> EnumSet<E> of(E e) {
        EnumSet<E> result = noneOf(e.getDeclaringClass());
        result.add(e);
        return result;
    }
    public static <E extends Enum<E>> EnumSet<E> of(E e1, E e2) {
        EnumSet<E> result = noneOf(e1.getDeclaringClass());
        result.add(e1);
        result.add(e2);
        return result;
    }

    // ... 5개 버전까지 정의되어 있음

    @SafeVarargs
    public static <E extends Enum<E>> EnumSet<E> of(E first, E... rest) {
        EnumSet<E> result = noneOf(first.getDeclaringClass());
        result.add(first);
        for (E e : rest)
          result.add(e);
        return result;
    }
}
```

## 아이템 54: `null`이 아닌 빈 배열이나 컬렉션을 반환하라

```java
public List<Cheese> getCheeses() {
    return cheesesInStock.isEmpty() ? null : new ArrayList<>(cheesesInStock);
}
```

* 호출측에서는 항상 `null`검사가 필요해짐
  * Kotlin의 경우 자료형을 `List<Cheese>?`로 선언해야함 (Java의 `Optional<List<Cheese>>`와 같음)

```java
public List<Cheese> getCheeses() {
    return new ArrayList<>(cheesesInStock);
}
```

* 빈 리스트를 리턴하는 것이 재고가 비었다는 의미를 올바르게 전달함
  * Kotlin의 경우 `List<Cheese>`로 사용 가능함.
  * 단 코틀린은 `@NotNull` 어노테이션이 없으면 `List<Cheese>!`로 인식(Platform Type)
  * `List<Cheese>!`은 `List<Cheese>` 또는 `List<Cheese>?`를 개발자가 선택 가능
* 반드시 `null`을 반환해야 하는 필요가 있는 경우가 아니라면 빈 리스트를 반환하는 것이 좋음

### 빈 리스트를 만드는 코딱지만한 비용을 절감하는 방법

```java
public List<Cheese> getCheeses() {
    return cheesesInStock.isEmpty() ? Collections.emptyList(): new ArrayList<>(cheesesInStock);
}
```

* 이 경우 `emptyList()`는 불변 리스트이고, `ArrayList`는 가변이므로 호출자 입장에서는 혼란을 가져올 수 있음
* 둘 다 불변으로 하거나, 둘 다 가변으로 해야함. 단, 대부분의 경우 불변이 더 좋다.

```java
public List<Cheese> getCheeses() {
    return cheesesInStock.isEmpty() ?
            Collections.emptyList(): Collecitons.unmodifiableList(new ArrayList<>(cheesesInStock));
}
```

### 배열을 반환하는 경우

```java
private final Cheese[] EMPTY_STOCK = new Cheese[0];
public Cheese[] getCheeses() {
    return cheesesInStock.isEmpty() ? EMPTY_STOCK : cheesesInStock.toArray(EMPTY_STOCK);
}
```

* `toArray(T[])`의 인자는 단지 배열의 타입을 결정하기 위한 것으로,
  `toArray(new Cheese[stock.size()])`와 같이 하면 배열을 두번 생성하는 것이 됨 

## 아이템 55: 옵셔널 반환은 신중히 하라

* 메서드가 값을 반환할 수 없는 경우
  * `throw new NoSuchElementException("No value present")` - 예외 상황이라고 하기 애매하고 비용이 높다
  * `return null` - 사용자에게 `null` 검사를 강요함
  * `Optional.empty()` (Java 8+)

```java
public static <E extends Comparable<? super E>> E max(Collection<E> c) {
    if (c.isEmpty())
        throw new IllegalArgumentException("비어있는 컬렉션에서 최대값을 구할 수 없습니다.");

    E max = null;
    // ...
    return max;
}

public static <E extends Comparable<? super E>> Optional<E> tryMax(Collection<E> c) {
    if (c.isEmpty())
        return Optional.empty();

    E max = null;
    // ...
    return Optional.of(max);
}
```

* `Optional`을 반환하는 메서드에서 `null`을 반환한다면 바보다.
* 효율성을 위해서 `OptionalInt`, `OptionalLong`, `OptionalDouble`이 제공됨

### `Optional`의 사용법은 생략

[람다 스페셜](lambda.md#one-more-thing)에서 충분히 다루었음

### `Optional` 관련 주의사항

* `Optional`을 컬렉션 같은 자료구조에 저장하지 마라
  * `Map`의 키로 사용하면 안됨. `Map<Optional<K>, V> schrodingersMap`
  * `List`, `Set`등에도 사용할 일이 거의 없음
* `Optional`을 필드에 저장할 필요가 있을까?
  * 역시 없다. 잘못된 설계일 가능성이 높음
  * 필수 필드를 가진 상위 클래스와, 추가 필드를 하위 클래스로 분리 가능할 수 있음
* `Optional`을 인수로 받는 메서드는 어떠한가?
  * 매개변수를 다르게 한 오버로드(다중정의)가 더 적절한 방법이다
* `Optional`은 반환값 전용으로 생각하자.

## 아이템 56: 공개된 API요소에는 항상 문서화 주석을 작성하라

* Javadoc으로 문서화를 해야 할 내용
  * 모든 `public`, `protected` 요소(메서드, 필드, 생성자 등)
  * 기본 생성자(생성자를 생략하는 경우)는 문서화를 할 수 없으므로
    `pulbic` 클래스라면 반드시 생성자를 만들어야함
  * 유지보수까지 고려한다면 `private`에도 문서화 주석을 추가하는 것이 좋음
* 무엇을 기술해야 하는가?
  * 상속을 전제로 하는 클래스 또는 인터페이스
    * 클라이언트와의 계약 내용을 명시 (How to implement)
    * 하위 클래스 작성자가 지켜야 할 내용을 기술
  * `final` 클래스 또는 메서드
    * 어떻게 동작하는 지를 기술 (What it does)
  * 클라이언트가 책임져야 할 내용 명시
    * 인자 값의 범위나 전제 조건(precondition) 등 (`@param`, `@throws`)
    * 성공적으로 수행된 후의 상태 (`@return`일 수도 있고, 객체 내부 상태일 수도 있음)
    * 발생 가능한 예외에 대한 설명(`@throws` 태그)
    * 부작용: 겉으로 드러나지 않는 내부적인 처리의 영향
      * 내부 스레드를 사용하는 경우 등
  * 클래스 사용시 반드시 알아야 할 내용
    * 불변성 여부(immutability)
    * 스레드 안전성 여부(thread-safety)
    * 직렬화 형식 관련 (`Serializable` 하다면)

### 태그별 작성 요령
  * `@return` - 설명의 내용이 메서드 설명과 동일하다면 생략을 고려할 수 있음
  * 보통 `@param`, `@return`은 명사형으로 작성
  * 보통 `@throws`는 if 절 형태로 작성

```java
/**
 * Returns the element at the specified position int this list.
 * 
 * <p>This method is <i>not</i> guaranteed to ruin in constant
 * time. In some implementations it may run in time proportional
 * to the element position.
 * 
 * @param index index of element to return;
 *              must be non-nagative and less than the size of this list
 * @return the element at the specified position in this list
 * @throws IndexOutOfBoundsException if the index is out of range
 *         ({@code index < 0 || index >= size()})
 */
E get(int index) {}
```

* `@implSpec`(Java 8+) - 하위 클래스 구현시 참고할 상세 정보
  * 일반적인 주석은 클래스-클라이언트간의 계약이라면, `@implSpec`는 클래스-하위 클래스간의 계약
  * `super` 키워드로 상위 클래스를 호출할때 어떻게 동작할지를 명확하게 설명
  * `javadoc` 옵션으로 `-tag "implSpec:a:Implementation Requirements:"` 를 주지 않으면 무시됨

```java
/**
 * Returns true if this collection is empty.
 * 
 * @implSpec
 * This implementation returns {@code this.size() == 0}.
 * 
 * @return true if this collection is empty
 */
public boolean isEmpty() { return size() == 0; }
```

* `@literal` - HTML 특수문자를 이스케이프할 때
  * `* {@literal |r| < 1}이면 기하 수열이 수렴한다.`
  * `@code`의 경우 글꼴을 다르게 표시해줌
* 첫번째 문장은 요약문으로 사용되어 인덱스 목록에서 사용됨
  * 요약 설명은 '메서드나 생성자의 동작을 설명하는 (주어가 없는) 동사구'
  * 첫 문장에서 마침표가 문장의 끝으로 인식되지 않게 하기 위해서는 `@literal`을 사용
  * `{@literal Erwin R. Schrödinger}는 고양이를 사랑함.`
  * 지금은 `@summary` 태그가 추가되어 더 깔금하게 할 수 있음 (Java 10+)
    * `{@summary Erwin R. Schrödinger 는 고양이를 사랑함.}`
* `@index` - 검색창에 등록할 키워드 (Java 9+)
  * `* This method complies with the {@index IEEE 754} standard.`

### [토론] 주석은 영문으로 해야한다?

### Markdown 문서화

```java
/// Returns the element at the specified position int this list.
/// 
/// This method is **not** guaranteed to ruin in constant
/// time. In some implementations it may run in time proportional
/// to the element position.
/// 
/// | size of this list | time complexity |
/// |-------------------|-----------------|
/// | 0                 | O(1)            |
/// | 1                 | O(1)            |
/// | 2                 | O(1)            |
/// 
/// @param index index of element to return;
///         must be non-nagative and less than the size of this list
/// @return the element at the specified position in this list
/// @throws IndexOutOfBoundsException if the index is out of range
///         `index < 0 || index >= size()`
E get(int index) {}
```

* Markdown 도입, 이제는 국가와 민족이 나서야 할 때

```kotlin
tasks.javadoc {
    group = "documentation"
    description = "Generates aggregated Javadoc documentation for all modules."

    // set javadoc toolchain
    javadocTool.set(javaToolchains.javadocToolFor {
        // libs.versions.javadoc: Javadoc용 JDK 버전 (23)
        languageVersion.set(JavaLanguageVersion.of(libs.versions.javadoc.get().toInt()))
    })

    options {
        if (this is StandardJavadocDocletOptions) {
            encoding = "UTF-8"
            charSet = "UTF-8"
            // libs.versions.java: 기존 JDK 버전 (17)
            source = libs.versions.java.get()
            title = "${project.name} ${project.version} API Documentation"
            version = project.version.toString()
            memberLevel = JavadocMemberLevel.PROTECTED
            tags("implSpec:a:Implementation Requirements:")
            links = listOf(
                "https://docs.oracle.com/en/java/javase/${libs.versions.java.get()}/docs/api/",
                "https://javadoc.io/doc/org.projectlombok/lombok/${libs.versions.lombok.get()}/",
                "https://javadoc.io/doc/org.jetbrains/annotations/${libs.versions.jetbrainsAnnotations.get()}/",
                "https://javadoc.io/doc/com.google.guava/guava/${libs.versions.guava.get()}/",
                "https://javadoc.io/doc/org.apache.commons/commons-lang3/${libs.versions.commonsLang3.get()}/",
                "https://javadoc.io/doc/org.slf4j/slf4j-api/${libs.versions.slf4j.get()}/",
            )
        }
    }
}
```
