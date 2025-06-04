# 이펙티브 자바 (3판) - 7장 람다와 스트림

## 목차

* [**아이템 42**: 익명 클래스 보다는 람다를 사용하라](#아이템-42-익명-클래스-보다는-람다를-사용하라)
* [**아이템 43**: 람다보다는 메서드 참조를 사용하라](#아이템-43-람다보다는-메서드-참조를-사용하라)
* [**아이템 44**: 표준 함수형 인터페이스를 사용하라](#아이템-44-표준-함수형-인터페이스를-사용하라)
* [**아이템 45**: 스트림은 주의해서 사용하라](#아이템-45-스트림은-주의해서-사용하라)
* [**아이템 46**: 스트림에서는 부작용 없는 함수를 사용하라](#아이템-46-스트림에서는-부작용-없는-함수를-사용하라)
* [**아이템 47**: 반환 타입으로는 스트림보다 컬렉션이 낫다](#아이템-47-반환-타입으로는-스트림보다-컬렉션이-낫다)
* [**아이템 48**: 스트림 병렬화는 주의해서 적용하라](#아이템-48-스트림-병렬화는-주의해서-적용하라)

## 아이템 42: 익명 클래스 보다는 람다를 사용하라

* 람다가 나오기 까지
  * 함수객체(function object): 콜백 함수에 사용되는 한개의 메소드를 가진 객체
  * 익명 객체(anonymous object): 본격 함수 객체를 지원하기 위한 기능(JDK 1.1)
    * 놀랍게도 람다식 이전부터 익명 클래스도 클로저(closure)로서 지역변수 포획 기능을 지원했음
  * 람다식(lambda expression): 익명 객체의 문법적 설탕(syntactic sugar, Java 8)
* 함수형 인터페이스(functional interface)
  * 단 하나의 추상 메서드만을 가진 인터페이스
  * `default`나 `static` 메소드는 추가로 더 가질 수 있다
  * 인터페이스 정의시 `@FunctionalInterface`를 붙이면 추가적인 검사가 가능하지만 필수는 아님
  * `Comparator<T>.compareTo()`의 시그니처는 `(T, T) -> int`인데 `BiFunction<T, T, Integer>`로 볼 수 있음

| 인터페이스                               | 추상 메소드                  | 시그니처                            | `java.util.function`            |
|-------------------------------------|-------------------------|---------------------------------|---------------------------------|
| `java.lang.Runnable`                | `run()`                 | `() -> void`                    |                                 |
| `java.util.Comparator<T>`           | `compare(a, b)`         | `(T, T) -> int`                 | `BiFunction<T, T, Integer>`     |
| `java.util.concurrent.Callable<V>`  | `call()`                | `() -> V`                       | `Supplier<V>`                   |
| `java.io.FileFilter`                | `accept(file)`          | `(File) -> boolean`             | `Predicate<File>`               |
| `java.beans.PropertyChangeListener` | `propertyChange(event)` | `(PropertyChangeEvent) -> void` | `Consumer<PropertyChangeEvent>` |
| `java.util.EventListener`           | `handleEvent(event)`    | `(Event) -> void`               | `Consumer<Event>`               |

```java
void main() {
    Collections.sort(words, (String s1, String s2) -> Integer.compare(s1.length(), s2.length()));
    Collections.sort(words, (s1, s2) -> Integer.compare(s1.length(), s2.length()));
}
```

* 람다식을 이용하는 이유가 간결함인 만큼 자료형 선언은 타입 추론에 맏기고 생략하고, 타입 추론이 안되는 경우만 명시하도록 한다.
  * 타입 추론은 제네릭과 밀접하게 연계되어있는데, 람다식을 자유롭게 쓰기 위해서라도 제네릭을 사용해야 한다.
    ([아이템 26](chapter05.md#아이템-26-raw-타입은-사용하지-말라), [아이템 29](chapter05.md#아이템-29-이왕이면-제네릭-타입으로-만들라),
    [아이템 30](chapter05.md#아이템-30-이왕이면-제네릭-메서드로-만들라))

```java
import static java.util.Comparator.*;
void main() {
    Collections.sort(words, comparingInt(String::length));
    words.sort(comparingInt(String::length));
}
```

* `Comparator`의 정적 팩토리 메서드를 사용하면 더 간결하게 할 수 있다.

```java
public enum Operation {
    PLUS("+", (x, y) -> x + y),
    MINUS("-", (x, y) -> x - y),
    TIMES("*", (x, y) -> x * y),
    DIVIDE("/", (x, y) -> x / y);

    private final String symbol;
    private final DoubleBinaryOperator operator; // (double, double) -> double

    Operation(String symbol, DoubleBinaryOperator operator) {
        this.symbol = symbol;
        this.operator = operator;
    }

    public int apply(double x, double y) { return operator.applyAsDouble(x, y); }

    @Override
    public String toString() { return symbol; }
}
```

* 람다식이 아니라 익명 객체를 사용해야 하는 경우
  * 람다 함수의 본체가 너무 긴 경우
    * 람다식은 1줄이 기본이고, 아무리 길어도 3줄을 넘지 않도록 한다.
    * 차라리 익명 클래스를 쓰거나, 핵심 로직을 별도 메소드로 분리하도록 한다.
  * 함수형 인터페이스가 아닌 인터페이스의 경우
    * 추상 메소드가 1개 이상인 경우
    * 인터페이스가 아니라 추상 클래스를 상속하는 경우
    * `this`가 필요한 경우
      * 람다식 내의 `this`는 함수 객체가 아니라 함수를 생성한 객체의 `this`임
      * 람다 함수는 순수 함수를 지향하고 포획된 변수(captured variable) 외에 별도의 속성을 가지지 않아야 함
    * 자기 재귀(self-recursion)가 필요한 경우
      * 람다식은 이름이 없어 재귀 알고리즘을 구현하기 힘들다. (불가능하지는 않지만 복잡해짐)
* 익명 클래스 객체도 마찬가지이지만 람다를 직렬화(serialization)하지 마라.
  ([Java에서의 함수(람다식)란 어떤 존재인가?](lambda.md#java에서의-함수람다식란-어떤-존재인가))

### 람다 에서의 `this`와 `super`

* 람다식 내에서 `this`나 `super`는 함수가 정의되는 클래스의 인스턴스를 가리킨다.
  * 따라서 람다 객체를 의미하는 `this`는 사용할 수 없다.
  * 또한 인스턴스가 없는 `static` 컨텍스트에서는 사용할 수 없다.

```java
public class ChildTest {

    public static String invokeGetName(Supplier<String> supplier) {
        return supplier.get();
    }

    @Test
    public void testInInstanceContext() {
        Child child = new Child();
        child.inInstanceContext();
    }
}

class Parent {
public String getName() { return "Parent"; }
}

class Child extends Parent {
    @Override
    public String getName() { return "Child"; }

    @SuppressWarnings({"Convert2MethodRef"})
    void inInstanceContext() {
        String s = null;

        // "Child"
        assertEquals("Child", ChildTest.invokeGetName(() -> this.getName()));
        assertEquals("Child", ChildTest.invokeGetName(this::getName));

        // "Parent"
        assertEquals("Parent", ChildTest.invokeGetName(() -> super.getName()));
        assertEquals("Parent", ChildTest.invokeGetName(super::getName));
    }
}
```

## 아이템 43: 람다보다는 메서드 참조를 사용하라

* 익명 클래스가 있었지만, 간결함을 위해 람다가 Java에 추가된 것이지만,
  람다보다 더 간결한 것이 있으니 그것은 메서드 참조(method reference)임

```java
void main() {
    List<String> tokens = getTokens();
    Map<String, Integer> tokenCounts = new HashMap<>();

    tokens.forEach(token -> {
        tokenCounts.merge(token, 1, (count, incr) -> count + incr);
    });
    tokens.forEach(token -> {
        tokenCounts.merge(token, 1, Integer::sum); // 메서드 참조
    });
}
```

* 컴파일러(또는 IDE)는 많은 경우 람다식을 메서드 참조로 줄일 수 있다고 권고할 것이다.
  하지만 람다식을 쓰는 것이 더 좋은 경우도 있다.
  * 클래스명이 매우 긴 경우
    ```java
    void main() {
        service.execute(GoshThisClassNameIsHumongous::action);
        service.execute(() -> action()); // 이게 더 간결함
    }
    ```
  * 람다식의 인자 이름이 로직을 분명하게 해주는 경우
    ```java
    void main() {
        int sum1 = numbers.stream().reduce(Integer::sum).orElse(0);
        int sum2 = numbers.stream()
                .reduce((prior, next) -> prior + next).orElse(0);
        // Integer::sum이 더 간결하지만 람다식 쪽이
        // reduce(accumulator)의 동작 방식을 더 잘 드러냄
    }
    ```
  * 아주 드물지만 메소드 참조로는 타입 추론이 안되는 경우가 있을 수 있다.

* 메서드 참조의 유형

| 유형         | 람다                                                       | 예시                       |
|------------|----------------------------------------------------------|--------------------------|
| 정적         | `str -> Integer.parseInt(str)`                           | `Integer::parseInt`      |
| 한정적(인스턴스)  | `Instant then = Instant.now();`<br>`t -> then.isAfter()` | `Instant.now()::isAfter` |
| 비한정적(인스턴스) | `str -> str.toLowerCase()`                               | `String::toLowerCase`    |
| 생성자        | `() -> new ArrayList<>()`                                | `ArrayList::new`         |
| 배열 생성자     | `(len) -> new int[len]`                                  | `int[]::new`             |

* Receiver란 메소드를 호출할 대상 인스턴스를 말함
  * 한정적(인스턴스): `Instant.now()`를 Bound receiver라고 함
  * 비한정적(인스턴스): `str`를 Unbound receiver라고 함

### 람다로는 불가능하고 정적 메소드로만 가능한 경우

* [옮긴이 주석] 제네릭 함수 타입(generic function type) 구현
  ([Java 명세 - Example 9.9-2. Generic Function Types](https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.9))

```java
interface G1 {
    <E extends Exception> Object m() throws E;
}
interface G2 {
    <F extends Exception> String m() throws Exception;
}

// 두 인터페이스를 다중 상속하면 다음과 같은 시그니처로 간주됨
// <F extends Exception> () -> String throws F
// 하지만 람다에는 이런 제네릭 문법은 없다.
@FunctionalInterface
interface G extends G1, G2  {
    // @Override
    // <F extends Exception> String m() throws F;
}

class UsingG<F extends Exception> {
    private final Class<F> exType;
    public UsingG(Class<F> exType) {
        this.exType = exType;
    }

    public void usingGFuncMethod(G func) throws F { func.m(); }

    public void doSomething() {
        try {
            //usingGFuncMethod(() -> "Hello, World!"); // 컴파일 에러: 타깃 메서드는 제네릭입니다
            usingGFuncMethod(MyStringUtils::helloWorld);
        }
        catch (Exception e) {
            if (exType.isInstance(e)) { // e instanceof F
                // F 예외 처리
            } else {
                System.err.println("unknown exception: " + e);
            }
        }
    }
}
  
class MyStringUtils {
    public static <F extends Exception> String helloWorld() throws F {
        if (false)
            throw (F) new RuntimeException("Generic exception example");
        return "Hello, World!";
    }
}
```

## 아이템 44: 표준 함수형 인터페이스를 사용하라

* `LinkedHashMap`은 `removeEldestEntry()`를 재정의하여 캐시처럼 활용할 수 있다.

```java
class MySimpleCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;
    public MySimpleCache(int capacity) {
        super(capacity);
        this.capacity = capacity;
    }

    /// LinkedHashMap의 put() 구현
    /// ```java
    /// public V put(K key, V value) {
    ///     if (removeEldestEntry(new Entry(key, value))) {
    ///         super.remove(eldestKey);
    ///     }
    ///     ...
    /// }
    /// ```
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity; // capacity를 넘으면 가장 오래된 항목을 제거
    }
}
```

* 만약 람다가 나온 이후에 `LinkedHashMap`을 만들었다면, 아마도 다음과 같이 했을 것이다.

```java
@FunctionalInterface
interface EldestEntryRemovalFunction<K, V> {
    // (Map, Map.Entry) -> boolean
    boolean removeEldestEntry(Map<K, V> map, Map.Entry<K, V> eldest);
}
class LinkedHashMapLambda<K, V> implements Map<K, V> {
    private final EldestEntryRemovalFunction<K, V> removal;
    public LinkedHashMapLambda(EldestEntryRemovalFunction<K, V> removal) {
        this.removal = removal;
    }

    public V put(K key, V value) {
        if (removal.removeEldestEntry(this, new Entry<>(key, value)))
            return remove(null);
        // ...
    }
    // ...
}
class MySimpleCacheLambda<K, V> {
    private final LinkedHashMapLambda<K, V> cache;
    public MySimpleCacheLambda(int capacity) {
        cache = new LinkedHashMapLambda<>((map, eldest) -> map.size() > capacity);
    }
    // ...
}
```

* `EldestEntryRemovalFunction` 인터페이스의 함수 시그니처는
  `(Map<K, V>, Map.Entry<K, V>) -> boolean`이며, 이것은
  `java.util.function.BiPredicate<Map<K, V>, Map.Entry<K, V>>`와 동일하다.
* 필요한 함수 인터페이스가 표준 라이브러리에 미리 정의되어있다면 표준 라이브러리를 사용하는 것이 좋다.
* `java.util.function` 패키지에는 총 43개의 표준 함수형 인터페이스가 미리 정의되어 있다.
  ([내장 함수 인터페이스](lambda.md#내장-함수-인터페이스))
  * `java.lang.Runnable`(`() -> void`)는 `java.util.function`는 아니지만 표준 함수 인터페이스로 생각할 수 있다.
* 표준 함수 인터페이스의 장점
  * 코드가 간결해지고, 가독성이 높아진다.
  * 표준 함수 인터페이스는 Java API와 호환되므로, 다른 라이브러리와의 호환성도 높아진다.
  * IDE나 컴파일러가 제공하는 자동완성 기능을 활용할 수 있다.
  * 표준 함수 인터페이스에 있는 유용한 디폴트 메서드를 활용할 수도 있다.
    * 예를 들어 `Predicate<T>`에는 `and()`, `or()`, `negate()` 같은 메서드가 있다.
    ```java
    void main() {
        Predicate<String> condition = s -> s.length() > 5;
        Predicate<String> reverseCondition = condition.negate();
        Predicate<String> combinedCondition = condition.and(s -> s.startsWith("A"));
    }
    ```

### 자체 함수 인터페이스를 정의해야하는 경우

* 표준 함수 인터페이스가 지원하지 않는 원시 자료형(primitive type)을 사용해야 하는 경우 ([아이템 61](chapter09.md#아이템-61-박싱된-기본-타입보다는-기본-타입을-사용하라))
  * 표준 함수 인터페이스와 Stream API는 `int`, `long`, `double`을 지원하지만 모든 경우를 커버하는 것은 아님
  * 객체 타입을 사용해도 동작은 하지만 계산량이 많을때는 처참하게 느려질 수 있다.([QuickSort 성능 비교](lambda.md#quicksort-성능-비교))
* `Comparator<T>` 같이 의미를 명확하게 구분하는 것이 좋은 경우
  * `BiFunction<T, T, Integer>`는 `Comparator<T>`와 동일하지만, `Comparator`가 더 명확하다.
  * `Comparator.naturalOrder()`같은 관련 `static`, `default` 메서드를 제공할 수 있다.
* 결론: 자체 함수 인터페이스를 정의하는 기준
  * 자주 쓰이며, 이름 자체가 용도를 명확히 한다.
  * 반드시 따라야 하는 규약이 있다.
  * 유용한 디폴트 메서드를 제공할 수 있다.
* 함수 인터페이스를 직접 정의하기로 했다면 반드시 `@FunctionalInterface`를 붙여라.

### 함수를 인수로 받는 메서드를 만들때 주의점

* 호환되는 함수 시그니처를 가지는 여러 인터페이스가 있다면, \
  **메서드를 오버로드하여 여러 버전을 만들지 마라!!**
* `ExecutorService`에는 `submit(Callable<T>)`와 `submit(Runnable)`이 있음
  * `Callable<T>`는 `() -> T`이고, `Runnable`은 `() -> void`라서
    `submit()`을 사용할때는 동일하게 사용이 가능하긴 하다.
  * 이렇게 2가지가 있으면 편할것 같지만, 때로는 자료형을 명확히 정의해줘야 할 경우가 생길 수 있음
    ([아이템 52](chapter08.md#아이템-51-메서드-시그니처를-신중히-설계하라))

## 아이템 45: 스트림은 주의해서 사용하라

* 스트림에 대한 자세한 설명은 생략한다. ([스트림이란 무엇인가?](lambda.md#스트림이란-무엇인가))

### [사람에게는 얼마만큼의 땅이 필요한가?](https://namu.wiki/w/%EC%82%AC%EB%9E%8C%EC%97%90%EA%B2%8C%EB%8A%94%20%EC%96%BC%EB%A7%88%EB%A7%8C%ED%81%BC%EC%9D%98%20%EB%95%85%EC%9D%B4%20%ED%95%84%EC%9A%94%ED%95%9C%EA%B0%80)

* 1번 사례
  ```java
  public class Anagrams1 {
      public static void main(String[] args) throws Exception {
          File dictionary = new File(args[0]);
          int minGroupSize = Integer.parseInt(args[1]);
  
          Map<String, Set<String>> groups = new HashMap<>();
          try (Scanner s = new Scanner(dictionary, StandardCharsets.UTF_8)) {
              while (s.hasNext()) {
                  String word = s.next();
                  // V computeIfAbsent(K key, Function<K, V> mapper) map에 키가 있으면 리턴, 없으면 람다 실행
                  groups.computeIfAbsent(alphabetize(word), (unused) -> new TreeSet<>()) // TreeSet
                      .add(word);
              }
          }
          for (Set<String> group : groups.values()) {
              if (group.size() >= minGroupSize) {
                  System.out.println(group.size() + ": " + group);
              }
          }
      }
      private static String alphabetize(String s) {
          char[] chars = s.toCharArray();
          Arrays.sort(chars);
          return new String(chars);
      }
  }
  ```
* 2번 사례
  ```java
  public class Anagrams2 {
      public static void main(String[] args) throws Exception {
          Path dictionary = Paths.get(args[0]);
          int minGroupSize = Integer.parseInt(args[1]);
  
          try (Stream<String> words = Files.lines(dictionary, StandardCharsets.UTF_8)) {
              words.collect(
                  groupingBy(word -> word.chars()
                      .sorted()
                      .collect(
                          StringBuilder::new,
                          (sb, c) -> sb.append((char) c),
                          StringBuilder::append).toString(),
                          toCollection(TreeSet::new)))
                  .values().stream()
                  .filter(group -> group.size() >= minGroupSize)
                  .map(group -> group.size() + ": " + group)
                  .forEach(System.out::println);
          }
      }
  }
  ```
* 3번 사례
  ```java
  public class Anagrams {
  
      public static void main(String[] args) throws Exception {
  
          Path dictionary = Paths.get(args[0]);
          int minGroupSize = Integer.parseInt(args[1]);
  
          try (Stream<String> words = Files.lines(dictionary, StandardCharsets.UTF_8)) {
              words.collect(
                  groupingBy(
                      Anagrams::alphabetize,
                      toCollection(TreeSet::new)))
                  .values().stream()
                      .filter(group -> group.size() >= minGroupSize)
                      .map(group -> group.size() + ": " + group)
                      .forEach(System.out::println);
          }
      }
  
      private static String alphabetize(String s) {
          char[] chars = s.toCharArray();
          Arrays.sort(chars);
          return new String(chars);
      }
  }
  ```

> 농부가 차지할 수 있었던 땅은 그가 묻힌 3아르신(2미터) 만큼이었다. \
> -- 톨스토이
> 
> 스트림을 처음 쓰기 시작하면 반복문을 스트림으로 바꾸고 싶은 유혹이 일겠지만 서두르지 않는 편이 좋다. \
> -- 조슈아 블로크

### 스트림을 언제 써야 하는가?

* `String.char()`는 `IntStream`이다.
  ```java
  void main() {
      "Hello world!".chars().forEach(System.out::println); // 72101108...
      "Hello world!".chars().forEach(c -> System.out.println((char)c));
      System.out.println("Hello world!");
  }
  ```
* 스트림이 아니라 반복문의 써야 하는경우
  * 지역변수를 읽고 수정해야 하는 경우 (포획된 변수는 `final`)
  * `break`, `continue`가 필요한 경우
  * Checked 예외를 던저야 하는 경우 (스트림의 람다에서는 주로 `RuntimeException`을 던지게 됨)
* 스트림이 적절한 경우
  * 원소의 시퀀스를 일관되게 변환한다
  * 원소의 시퀀스를 필터링한다
  * 원소의 시퀀스를 하나의 연산을 사용해서 결합한다
  * 원소의 시퀀스를 컬렉션에 모은다
  * 원소의 시퀀스에서 특정 조건을 만족하는 원소를 찾는다

### 스트림으로 할 수 있지만 복잡해지는 경우

* 원본 원소와 변환된 객체가 같이 필요한 경우
  * 가능은 하지만 나이스하지 않다
  ```java
  record Pair<F, S>(F first, S second) {
      public static <F, S> Pair<F, S> of(F first, S second) {
          return new Pair<>(first, second);
      }
  }
  
  @Test
  public void testDtoRequired() {
  
      plants.stream()
              .filter(p -> p.lifeCycle() == Plant.LifeCycle.ANNUAL)
              .map(p -> Pair.of(p.name() + " (" + p.koName() + ")", p))
              .map(pair -> pair.first() + ": " + pair.second().lifeCycle())
              .forEach(System.out::println);
  }
  ```
* "부먹과 찍먹", "마블과 DC"에 이어 가장 뜨거운 논쟁 
  ```java
  public enum Rank {
      ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN,
      EIGHT, NINE, TEN, JACK, QUEEN, KING
  }
  public enum Suit { SPADES, HEARTS, DIAMONDS, CLUBS }
  
  public record Card(Suit suit, Rank rank) {
  
      public static List<Card> newDeck1() {
          List<Card> deck = new ArrayList<>();
          for (Suit suit : Suit.values()) {
              for (Rank rank : Rank.values()) {
                  deck.add(new Card(suit, rank));
              }
          }
          return deck;
      }
  
      public static List<Card> newDeck2() {
          return Stream.of(Suit.values())
              .flatMap(suit ->
                  Stream.of(Rank.values())
                      .map(rank -> new Card(suit, rank)))
              .toList();
      }
  }
  ```

## 아이템 46: 스트림에서는 부작용 없는 함수를 사용하라

* 스트림인듯 스트림 아닌 스트림 같은 너
  ```java
  @Test
  public void testSideEffects() {
      Map<String, Long> freq = new HashMap<>();
      try (Stream<String> words = new Scanner(file).tokens()) {
          // 반복문을 함수로 바꾸었을 뿐 함수형 방식이 아님
          words.forEach(token ->
                  freq.merge(token.toLowerCase(), 1L, Long::sum));
      }
  }
  @Test
  public void testPureFunctional() {
      Map<String, Long> freq;
      try (Stream<String> words = new Scanner(file).tokens()) {
          freq = words.collect(
              groupingBy(String::toLowerCase, counting()));
      }
  }
  ```

* 함수에서 함수 외부에 영향을 주는 것을 **부작용(side effect)** 이라고 하며,
  함수형 프로그래밍에서는 지양하는 방식임
  * 객체지향 프로그래밍에서 전역변수를 쓰지말라는 것과 비슷함
    * 예측 불가능한 결과: 특히 병렬 스트림의 경우 경쟁 상태(race condition)가 발생 가능
    * 가독성 및 유지보수성: 스트림 파이프라인 만으로 파악이 안됨 
    * 병렬 처리 어려움: 외부 상태에 대한 동기화가 필요 -> 병렬 처리의 이점 감소
  * `Stream.forEach()`, `Stream.peak()`은 부작용(side effect)이 불가피한 기능을 위한 메서드들임
    * 연산용으로 쓰지 말고 최종 출력용(`forEach()`) 또는 디버깅(`peak()`)용으로만 쓰자

#### `Collector` 활용

* 아이템 46은 부작용에 대한 설명 보다 `Collector`에 대한 설명이 주를 이루는데,
  자세한 설명은 생략한다.([확장된 `reduce()`: `collect()`](lambda.md#확장된-reduce-collect))

## 아이템 47: 반환 타입으로는 스트림보다 컬렉션이 낫다

* 시퀀스를 반환해야 하는 경우, `for`문과 호환(`Iterable`)되는 다음 중 하나를 사용했다.
  * Collection 인터페이스(`List`, `Set`)
  * `Iterable` - 반복은 가능하지만 Collection 인터페이스를 모두 구현할 수 없는 경우
  * 배열 - 원시 자료형이거나 성능이 중요한 경우 
* 이제 Java에도 스트림이 있으니 어떤 시퀀스를 반환하기에 가장 좋은 형태라고 생각할 수도 있음
  * 하지만 스트림은 반복해서 사용할 수 없다.

### 스트림과 `Iterable`

* 만약 `Iterable`이나 `Stream`을 리턴하는 메서드가 이미 만들어져 있다면?
* 스트림은 기능적으로는 `Iterable`처럼 동작할 수 있지만 `Iterable`을 구현하지 않았다.
  * 반면 `Iterator`는 `stream()` 메서드를 제공하여 스트림으로 변환 가능함
  * 꼭 `Iterable`이 필요하다면 스트림은 `iterator()`를 제공하기 때문에 간단한 어댑터로 변환할 수 있음
    * 하지만 이렇게 만들어진 `Iterable` 역시 재사용은 불가능하다는 점 주의

```java
public static <T> Iterable<T> iterableOf(Stream<T> stream) {
    // Iterable은 @FunctionalInterface는 안붙어 있지만
    // 1개의 추상 메소드만 있는 함수형 인터페이스라서 람다로 표현 가능
    // Iterator<T> iterator()
    //     = () -> Iterator<T>
    //     = Supplier<Iterator<T>>
    return () -> stream.iterator();
}
```

* [`Anagrams1` 예제](#사람에게는-얼마만큼의-땅이-필요한가)에서 `Scanner`를 사용한 이유는,
  반복문에 사용하기 위해서였음
* 하지만 `Files.lines()`는 `Stream`을 리턴하지만, 예외 처리를 다 알아서 해주기 때문에 더 좋다.
  ```java
  void main() {
      try (Stream<String> stream = Files.lines(dictionary, StandardCharsets.UTF_8)) {
          for (String word: iterableOf(stream)) {
              groups.computeIfAbsent(alphabetize(word),
                              (unused) -> new TreeSet<>()) // TreeSet
                  .add(word);
          }
      }
  }
  ```

* 역으로 `Iterable`을 스트림으로 변환할 수도 있다.

```java
private static <T> Stream<T> streamOf(Iterable<T> iterable) {
    return streamOf(iterable, false);
}
private static <T> Stream<T> streamOf(Iterable<T> iterable, boolean parallel) {
    return StreamSupport.stream(iterable.spliterator(), parallel);
}
```

### 시퀀스를 반환하는 메서드는 어떤 타입을 반환해야 하는가?

* `Iterable`을 반환하면 반복문에 사용할 수 있다. `for (T t : getIterable()) { ... }`
* `Stream`을 반환하면 스트림 파이프라인을 사용할 수 있다. `getStream().filter(...)`
* 하지만 `Collection`이 출동하면 어떨까?
  * `for (T t : getCollection())`
  * `getCollection().stream().filter(...)`
* 그렇지만 시퀀스의 크기가 매우 클때 생각없이 컬렉션을 반환하도록 코드를 작성하면 
  * 메모리를 매우 많이 사용할 것임
  * 지연 처리가 불가능해짐
* 이런 문제를 방지하기 위해서 알고리즘을 간단하게 컬렉션 인터페이스로 감싸서 반환할 수 있다.
  ```java
  public class PowerSet<E> extends AbstractList<Set<E>> {
  
      private final List<E> src;
  
      public PowerSet(List<E> src) {
          if (src.size() > 30) {
              throw new IllegalArgumentException("Set too large: " + src.size());
          }
          this.src = new ArrayList<>(src);
      }
  
      @Override
      public int size() {
          return 1 << src.size();
      }
  
      @SuppressWarnings({"SlowListContainsAll", "unchecked"})
      @Override
      public boolean contains(Object o) {
          return o instanceof Set &&
                  src.containsAll((Set<E>) o);
      }
  
      @Override
      public Set<E> get(int index) {
          Set<E> result = new HashSet<>();
          for (int i = 0; index != 0; i++, index >>= 1) {
              if ((index & (1 << i)) != 0) {
                  result.add(src.get(i));
              }
          }
          return result;
      }
  }
  ```
  * [참 쉽죠?](https://en.wikipedia.org/wiki/Bob_Ross)
    * 좀 더 어려운 예제
      ```java
      class NumberRange extends AbstractList<Integer> {
          private final int startInclusive;
          private final int endInclusive;
          private final int size;
  
          public NumberRange(int startInclusive, int endInclusive) {
              if (endInclusive < startInclusive) {
                  throw new IllegalArgumentException("끝 값은 시작 값보다 작을 수 없습니다.");
              }
              this.startInclusive = startInclusive;
              this.endInclusive = endInclusive;
              this.size = (endInclusive - startInclusive) + 1;
          }
  
          @Override
          public Integer get(int index) {
              if (index < 0 || index >= size) {
                  throw new IndexOutOfBoundsException("인덱스: " + index + ", 크기: " + size);
              }
              return startInclusive + index;
          }
  
          @Override
          public int size() {
              return this.size;
          }
  
          @Override
          public boolean contains(Object o) {
              if (!(o instanceof Integer value)) {
                  return false;
              }
              return value >= startInclusive && value <= endInclusive;
          }
      }
      ```

## 아이템 48: 스트림 병렬화는 주의해서 적용하라

* 메르센 소수를 구하는 예제
  * 메르센 소수(Mersenne prime)는 2^p - 1 형태의 소수로, p가 소수일 때만 메르센 소수가 됨
    * p가 소수일지라도 2^p - 1이 소수가 아닐 수 있음
  * `BigInteger`는 `isProbablePrime(int certainty)` 메서드를 제공하여 확률적으로 소수인지 판별할 수 있다.

```java
static Stream<BigInteger> primes() {
    return Stream.iterate(TWO, BigInteger::nextProbablePrime);
}
void main() {
    primes()
        .map(p -> TWO.pow(p.intValueExcact()).substract(ONE))
        .filter(mersenne -> meresenne.isProbablePrime(50))
        .limit(20)
        .forEach(System.out::println);
}
```

* 이 작업은 매우 오래걸리는 작업으로 성능 향상을 위해서 `primes().parallel()` 적용하고자 할 수 있다.
  * 이 코드의 경우 병렬스트림을 적용하면 CPU 100% 상태로 끝나지 않는다. (언젠가 끝날지도 모르지만 그 전에 컴퓨터가 터짐)
  * 스트림 라이브러리가 이 스트림을 병렬화 하는 방법을 제대로 찾아내지 못했기 때문
    * `Stream.iterate()`을 사용한 경우
    * `limit()`은 원하는 대로 동작하지 않을 수 있음
  * 위 예제는 두가지를 모두 갖춘 완벽한 예시임
    * 제한보다 더 처리를 한 후 최종 결과 병합시에 버려도 된다고 판단한 것으로 보임
    * 하지만 메르센 소수 예제에서는 p가 1증가할때마다 계산 소요시간이 엄청나게 늘어남

### 병렬 스트림을 고려할 때 검토할 것

* 병렬 스트림에 가장 잘 어울리는 스트림 소스
  * 배열, `ArrayList`, `HashMap`, `HashSet`, `ConcurrentHashMap`
  * 크기를 명확하게 알 수 있고(sized)
  * 원하는 크기로 정확하고 쉽게 나누어 분배할 수 있음(splittable)
    * 분할은 `Spliterator`인터페이스가 처리하며, 스트림의 `spliterator()`로 얻을 수 있음
    * 분할된 청크는 각자 독립적으로 처리 가능
  * 참조 지역성(locality of reference)이 좋은 경우 또는 순서가 있는 경우(ordered)
* 병렬 처리와 잘 어울리는 스트림 연산
  * 단락(short-circuiting) 특성의 연산
    * `findFirst()`, `findAny()`, `anyMatch()`, `allMatch()`, `noneMatch()`
  * `reduce(identity, accumulator, combiner)`같이 병렬연산을 고려한 오버로드를 사용
  * 최종 연산(terminal operation)이나 순차 처리가 필요한 단계가 있다면
    그 이전의 중간 연산으로 처리량이 잘 분배가 되어야 병렬 처리의 잇점을 살릴 수 있을것임
  * 스트림 소스가 직접 구현한 객체라면 `spliterator()`를 효과적으로 분배되도록 제대로 구현해야 한다.
* 병렬 처리 후에는 보통 원본의 순서가 보장되지 않음
  * 최종 처리 직전에 `sorted()`를 하거나 `forEachOrdered()`를 사용해야 할 수 있음

### 병렬 스트림이 효과 없거나 문제가 될 경우

* 함수가 제대로 구현되지 않은 경우 안전 실패(safety failure)로 이어질 수 있음
  * `mapper`, `filter`, `accumulator` 같은 함수가 Stream API 규악을 위배한 경우 발생할 수 있다.
  * `accumulator`, `combiner` 등의 스트림 메서드의 인자로 제공되는 함수는 규칙을 지켜야함
    * 결합 법칙(associative): `(a + b) + c == a + (b + c)` 일 경우 `+`는 결합법칙을 만족
    * 불간섭(non-interfering): 파이프라인이 수행중에 스트림 소스가 변경되면 안됨
    * 무상태(stateless): 함수가 외부 상태에 의존하지 않아야 함
* 병렬화에 들어가는 비용이 병렬화로 얻는 이점보다 더 클 수 있음
  * 병렬화는 대량 처리의 경우에 적용할 수 있는 최적화 수단이며 대부분은 효과를 보기 어렵다
    * `if (스트림의 원소수 * 파이프라인의 코드 라인수 >= 수십만) useParallelStream = true`
    * 병렬화를 적용한 후 반드시 성능을 비교해보고 효과가 있는지 확인해야함
  * 병렬화는 공용 스레드풀 `ForkJoinPool.commonPool()`을 사용하므로,
    잘못된 선택이 다른 스레드 작업에도 영향을 줄 수 있음을 명심할것

* 병렬 연산이 효과를 보는 예제
  * `n`보다 작은 소수의 갯수를 구하는 함수
  ```java
  static long pi(long n) {
      return LongStream.rangeClosed(2, n)
          .filter(i -> i == 2 || i % 2 != 0) // 2와 홀수만 필터링
          .parallel()                        // 병렬 스트림
          .mapToObj(BigInteger::valueOf)
          .filter(i -> i.isProbablePrime(50))
          .count();
  }
  ```
  * `LongStream.rangeClosed(2, n)`을 사용하여 스트림의 크기를 명확하게 제한하고 시작한 것이 핵심
    * 작업 분할 용이성 (Splittability)
    * 예측 가능성 및 스케줄링: 스레드 수 등 최적의 병렬화 실행 계획을 수립할 수 있어야 함
    * 분배 오버헤드: 크기를 알 수 없는 경우 하나씩 분배하므로 추가 오버헤드가 발생

* 무작위 수로 이루어진 병렬 스트림이 필요하면 `SplitableRandom`을 사용하자. 
  * `ThreadLocalRandom`은 병렬 스트림에 적합하지 않음
  ```java
  SplittableRandom random = new SplittableRandom();
  Stream<Integer> randomIntegers = Stream.generate(random::nextInt);
  IntStream randomInts = random.ints();
  ```
