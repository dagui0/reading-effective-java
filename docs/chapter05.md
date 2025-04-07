# 이펙티브 자바 (3판) - 5장 제네릭

## 목차

* [**아이템 26**: Raw 타입은 사용하지 말라](#아이템-26-raw-타입은-사용하지-말라)
  * [제네릭 관련 용어 정리](#제네릭-관련-용어-정리)
* [**아이템 27**: 비검사 경고를 제거하라](#아이템-27-비검사-경고를-제거하라)
* [**아이템 28**: 배열보다는 리스트를 사용하라](#아이템-28-배열보다는-리스트를-사용하라)
* [**아이템 29**: 이왕이면 제네릭 타입으로 만들라](#아이템-29-이왕이면-제네릭-타입으로-만들라)
* [**아이템 30**: 이왕이면 제네릭 메서드로 만들라](#아이템-30-이왕이면-제네릭-메서드로-만들라)
* [**아이템 31**: 한정적 와일드카드를 사용해 API 유연성을 높이라](#아이템-31-한정적-와일드카드를-사용해-api-유연성을-높이라)
* [**아이템 32**: 제네릭과 가변인수를 함께 쓸 때는 신중하라](#아이템-32-제네릭과-가변인수를-함께-쓸-때는-신중하라)
* [**아이템 33**: 타입 안전 이종 컨테이너를 고려하라](#아이템-33-타입-안전-이종-컨테이너를-고려하라)

## 아이템 26: Raw 타입은 사용하지 말라

* 제네릭 타입: `List<E>` 처럼 타입 매개변수(type parameter)를 가지고 선언된 타입
  * 역자 주: "E의 리스트"가 적절한듯 하다, 원어민들은 "List E", "List of E" 라고 읽는 듯  
* 매개변수화 타입(parameterized type): `List<String>` 처럼 타입 매개변수에 실제 타입을 대입해서 동적으로 만들어진 타입

### 로 타입(raw type)

* `List` 처럼 타입 매개변수가 없는 제네릭 타입
  * 제네릭이 도입되기 전 코드와 호환을 위해 추가된 기능임
    * 제네릭 구현에 타입 소거(erasure)를 사용하여 구현되었다 ([아이템 28](#아이템-28-배열보다는-리스트를-사용하라))
  * 로 타입을 사용하게 되면 제네릭의 안정성과 표현력을 모두 잃게 되므로 절대 사용하지 마라.
* 로 타입 `List`는 실제로 `List<Object>`와 같지만, `List<Object>`는 써도 된다.
  * `List`는 제네릭 메카니즘을 무시하지만, `List<Object>`는 명시적으로 모든 종류의 객체(`Object`)를 허용하겠다는 의사 표시인 것이다.
  * 로 타입은 매개변수화 타입의 상위 클래스 처럼 동작한다.
    * (개념적으로) `List<String> instanceof List` == true
    * (개념적으로) `List<String> instanceof List<Object>` == false
    * 따라서 `List<String>`을 받는 메소드에 `List`를 넘길 수는 있지만, `List<Object>`를 넘길 수는 없다. [RawTypeTest.java](../src/test/java/effectivejava/chapter05/item26/RawTypeTest.java)

### 비한정적 와일드카드 타입(unbounded wildcard type)

* `List<?>`처럼 타입 매개변수와 무관하게 사용할 수 있는 변수형
  * `List` 은 안되지만 `List<?>`는 써도 된다.
  * `List<?>`에는 어떤 타입의 원소도 넣을 수 없으므로 안전하다.

### 로 타입을 사용해야 하는 경우

* 제너릭 타입은 컴파일 시점의 검사로만 적용되는 것으로 실제 컴파일된 결과는 로 타입으로 저장된다.
  * 클래스 객체를 사용해야 할 경우. `List<String>.class` 같은거는 없다.
  * `instanceof` 와 사용할 경우 `instanceof List<String>` 같은 거는 안된다.

```java
private static List<String> checkTypeAndCast(Object o) {
    if (o instanceof List<?> list) {
        if (!list.isEmpty() && list.getFirst() instanceof String) {
            // 요소를 꺼내서 검사까지 했으므로 절대적으로 완전 킹갓 안전한 형 변환임
            @SuppressWarnings("unchecked")
            List<String> stringList = (List<String>) list;
            return stringList;
        }
    }
    throw new ClassCastException("Invalid type");
}
```

### 제네릭 관련 용어 정리

| 한글 용어          | 영어 용어                   | 예                                   | 아이템                                                                    |
|----------------|-------------------------|-------------------------------------|------------------------------------------------------------------------|
| 매개변수화 타입       | parameterized type      | `List<String>`                      | [아이템 26](#아이템-26-raw-타입은-사용하지-말라)                                      |
| 실제 타입 매개변수     | actual type parameter   | `String`                            | [아이템 26](#아이템-26-raw-타입은-사용하지-말라)                                      |
| 제네릭 타입         | generic type            | `List<E>`                           | [아이템 26](#아이템-26-raw-타입은-사용하지-말라), [아이템 29](#아이템-29-이왕이면-제네릭-타입으로-만들라) |
| 정규 타입 매개변수     | format type parameter   | `E`                                 | [아이템 26](#아이템-26-raw-타입은-사용하지-말라)                                      |
| 비한정적 와일드 카드 타입 | unbounded wildcard type | `List<?>`                           | [아이템 26](#아이템-26-raw-타입은-사용하지-말라)                                      |
| 로 타입           | raw type                | `List`                              | [아이템 26](#아이템-26-raw-타입은-사용하지-말라)                                      |
| 한정적 타입 매개변수    | bounded type parameter  | `<E extends Number>`                | [아이템 29](#아이템-29-이왕이면-제네릭-타입으로-만들라)                                    |
| 재귀적 타입 한정      | recursive type bound    | `<T extends Comparable<T>>`         | [아이템 30](#아이템-30-이왕이면-제네릭-메서드로-만들라)                                    |
| 한정적 와일드카드 타입   | bounded wildcard type   | `List<? super Integer>`             | [아이템 31](#아이템-31-한정적-와일드카드를-사용해-api-유연성을-높이라)                          |
| 제네릭 메서드        | generic method          | `static <E> List<E> asList(E... a)` | [아이템 30](#아이템-30-이왕이면-제네릭-메서드로-만들라)                                    |
| 타입 토큰          | type token              | `String.class`                      | [아이템 33](#아이템-33-타입-안전-이종-컨테이너를-고려하라)                                  |


## 아이템 27: 비검사 경고를 제거하라

* 제네릭 타입을 사용하기 시작하면 컴파일러 경고가 매우 많아진다. (결국 그냥 모두 무시하게 된다)
* 경고 내용을 잘 보고 가능한 모든 경고를 제거하도록 하자. \
  모두 제거하게되면 매우 좋은 소식을 들을 수 있다.
  > "당신의 코드에 타입 안정성이 보장되었습니다"

### `@SuppressWarnings` 어노테이션

```java
@Target(value={TYPE,FIELD,METHOD,CONSTRUCTOR,PARAMETER,LOCAL_VARIABLE})
@Retention(value=SOURCE)
public @interface SuppressWarnings {}
```

* 경고를 제거할 수 없다면 `@SuppressWarnings("unchecked")` 같이 해서 경고를 중지시키자.
  * `@SuppressWarnings`은 지역 변수 부터 클래스 전체까지 다양한 레벨에 설정할 수 있다.
  * 가능한 좁은 범위에 설정하자.
  * `@SuppressWarnings`를 지정할 수 없는 위치가 생기면,
    불필요하더라도 지역변수를 선언하고 어노테이션을 붙인다. 
  * 어노테이션을 붙일 때는 항상 주석으로 안전성을 확인한 거란 거를 남겨야 한다.

### [참고] `noinspection` 주석 

```java
public void testRawTypeConverting() {

    @SuppressWarnings("rawtypes")
    List rawList = new ArrayList();
    //noinspection unchecked
    rawList.add("Hello");
}
```

* Intelij IDEA에서는 `noinspection` 으로 시작하는 주석을 이용해서
  어노테이션을 붙일 수 없는 구문에서도 경고를 중지시킬 수 있다.
* 이 기능은 인텔리제이에서만 적용되는 것으로 사용하지 않는 것이 바람직하다고 생각되는데 토론에 붙여보자.

### 제네릭 관련 경고

* `rawtypes`: 매개변수화된 클래스 'List'의 원시 사용
  ```java
  @SuppressWarnings("rawtypes")
  List rawList = new ArrayList();
  ```
* `unchecked`
  * **비검사 메서드 호출 경고**: 원시 타입 'java.util.List'의 멤버로서 'add(E)'에 대한 확인되지 않은 호출
    ```java
    //noinspection unchecked
    rawList.add("Hello");
    ```
  * **비검사 매개변수화 가변인수 타입 경고**: 확인되지 않은 대입: 'java.util.List'을(를) 'java.util.List<java.lang.String>'에
    ```java
    //noinspection unchecked
    getSizeOfStringList(rawList);
    ```
  * **비검사 형 변환 경고**: 확인되지 않은 형 변환: 'java.util.List<capture<?>>'에서 'java.util.List<java.lang.String>'(으)로
    ```java
    @SuppressWarnings("unchecked")
    List<String> stringList = (List<String>) list;
    ```

## 아이템 28: 배열보다는 리스트를 사용하라

### 배열과 제네릭 타입의 차이

* 공변(covariant)
  * `class Sub extends Super`일 때
  * `Sub[]`는 `Super[]`의 하위 타입이다. (공변)
    ```java
    Super[] arr = new Sub[10]; // 가능함
    ```
  * `List<Sub>`는 `List<Super>`의 하위 타입이 아니다. (비공변)
    ```java
    List<Super> list = new ArrayList<Sub>(); // 컴파일 에러
    ```
* 실체화(reify) 여부
  * 배열은 실체화 된다. 배열은 런타임에 자신이 담기로 한 원소의 타입을 인지하고 확인한다.
  * 제네릭은 컴파일 시점에만 검사를 하고 런타임 시에는 소거(erasure)된다.
    * 즉 제네릭 타입(`List<String>`)은 런타임에는 로 타입(`List`)과,
      파라메터 타입(`String`)은 `Object`와 동일하다.

### 배열과 제네릭을 같이 쓸 수 없는 이유

```java
List<String>[] stringLists = new List<String>[10];  // (1) 컴파일 에러지만 된다 가정하면
List<Integer> intList = List.of(42);                // (2)
Object[] objects = stringLists;                     // (3)
objects[0] = intList;                               // (4)
String s = stringLists[0].get(0);                   // (5) ClassCastException
```

* 런타임시 발생하는 `ClassCastException`을 방지하고자 하는 제네릭의 근본 의도와 배치되는 결과 발생 가능하다.
* 실체화 불가 타입(non-reifiable type)
  * `List<?>`와 같이 비한정적 와일드카드 타입만 실체화가 가능하다.

```java
List<E> a                       // 됨 (제네릭 타입 안에서는)
    = new List<E>();            // 됨 (제네릭 타입 안에서는)
List<String>[] a                // 안됨
    = new ArrayList<String>[1]; // 안됨
E[] a                           // 됨 (제네릭 타입 안에서는)
    = new E[1];                 // 안됨
List<?> a                       // 됨
    = new ArrayList<?>();       // 안됨
    = new ArrayList<>();        // 됨
```

### 제네릭 타입 배열을 만들 수 없을때

* 제네릭 배열을 만들 수 없을때 해결 방법은 [아이템 33](#아이템-33-타입-안전-이종-컨테이너를-고려하라)에서 나온다고 하는데...

<details>
  <summary>스포일러 주의</summary>

```java
import java.lang.reflect.Array;

@SuppressWarnings("unchecked")
private static <T> T[] createGenericArray(Class<T> clazz, int size) {
    return (T[])Array.newInstance(clazz, size);
}
```
</details>

### 제네릭 타입과 가변 인수 메서드(varargs method)

* 제네릭 타입과 가변인수 메서드를 섞어서 사용할 경우 해석하기 어려운 경고 메시지를 받게 된다.
  * 가변 인수 메소드는 실제로는 배열을 전달하는 메서드이기 때문이다.
  * `@SafeVarargs` 어노테이션을 사용하면 경고를 없앨 수 있다.

> 매개변수화된 vararg 타입의 잠재적 힙 오염

```java
@SafeVarargs
private <T> int getVarargsCount(T... args) {
    return args.length;
}
```

### 결론

* 배열을 사용하면 여러모로 성가시므로, 코드가 복잡해지고 성능이 조금 낮아지더라도 `List<E>`를 사용하자.

## 아이템 29: 이왕이면 제네릭 타입으로 만들라

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

.

.

.

.

.

.

.

.

* 예제 참조
  * [Stack1.java](../src/main/java/effectivejava/chapter05/item29/Stack1.java)
  * [Stack2.java](../src/main/java/effectivejava/chapter05/item29/Stack2.java)
  * [Stack3.java](../src/main/java/effectivejava/chapter05/item29/Stack3.java)

<img src="https://i.namu.wiki/i/ZWG3Rg2RsWe5BTyxjNVVtVhBH7pjO5DPW2sk4KLJ-tXDNoNTPQkcHyn5N49x3jgymM3BeMtqrcFJpE8Ga8QWag.webp" style="width:200px" alt="자세한 설명은 생략한다"/>

## 아이템 30: 이왕이면 제네릭 메서드로 만들라

### 제네릭 메소드

* 예제 참조
  * [GenericUtils.java](../src/main/java/effectivejava/chapter05/item30/GenericUtils.java)

<img src="https://i.namu.wiki/i/ZWG3Rg2RsWe5BTyxjNVVtVhBH7pjO5DPW2sk4KLJ-tXDNoNTPQkcHyn5N49x3jgymM3BeMtqrcFJpE8Ga8QWag.webp" style="width:200px" alt="자세한 설명은 생략한다"/>

### 제네릭 싱글턴 패턴

* 타입별로 따로 작성할 필요가 없는 상수 객체에 대해서 제네릭 변환을 통해서 한개의 카피만 유지하도록 한다.

```java
private static final UnaryOperator<Object> IDENTITY = (t) -> t;

@SuppressWarnings("unchecked")
public static <T> UnaryOperator<T> identity() {
    return (UnaryOperator<T>) IDENTITY;
}
```

## 아이템 31: 한정적 와일드카드를 사용해 API 유연성을 높이라

* 매개변수화 타입은 불공변(invariant)이다.
  * `List<Type1>`과 `List<Type2>`는 `Type1`과 `Type2`간에 어떤 관계가 있다고 해도, 상관 없이 서로 하위 타입도 아니고, 상위타입도 아니다.
  * `List<String>`이 `List<Object>`의 하위타입이 아니라는 것

### 공변(variant) 스러운 관계가 필요할 때

```java
class Stack<E> {
    public void pushAll(Iterable<E> src) {
        for (E e: src)
          push(e);
    }
    public void popAll(Colleciton<E> dst) {
        while (!isEmpty())
          dst.add(pop());
    }
}

@Test
public void testStack3PushAll() {
    Stack3<Number> stack = new Stack3<>();
    List<Integer> intList = List.of(1, 2, 3);
    stack.pushAll(intList); // compile error
}
```

* 한정적 와일드카드 타입을 사용하면 해결 가능하다.
  * `Iterable<Integer>`는 `Iterable<Number>`과는 호환되지 않지만 `Iterable<? extends Number>`는 호환된다.
  * 타입 매개변수를 단순히 `<E>`에서 `<? extends E>`로 바꾸기만 하면 되며, 코드를 고칠 부분은 전혀 없다.

```java
public void pushAll(Iterable<? extends E> src) {
    for (E e: src)
        push(e);
}
public void popAll(Collection<? super E> dst) {
    while (!isEmpty())
        dst.add(pop());
}
```

* `popAll()`의 경우는 `Integer`는 상위 클래스인 `Number`에도 대입이 가능해야 하므로 `<? super E>`로 바꿔야 한다.

### PECS 공식

> 펙스(PECS): producer-extends, consumer-super

* 저자는 이렇게 암기하면 기억이 쉬울 것이라고 한다.
* 다행히도 번역자님이 어린백셩이 사맏디아니할새 친절하게 주석을 달아 주셨다.
  > pecs는 가슴근육(pectoral muscles)을 뜻하는 영어 약어이다.

```java
public static <T> Set<T> union2(Set<? extends T> s1, Set<? extends T> s2) {
    Set<T> result = new HashSet<>(s1);
    result.addAll(s2);
    return result;
}
```

* 매개변수에 대해서는 와일드 카드 타입을 사용하지만 리턴값은 `Set<T>`로 사용 하도록 한다.
  * 클라이언트 코드에서도 와일드카드로 사용해야 하는 불상사가 생긴다.
    ```java
    Set<? extends T> uset = GenericUtils2.union2(set1, set2);
    ```

### Java 7 이전

* 타입 추론 기능이 제대로 안되는 경우 아래와 같이 메소드 호출시에 타입을 명시해줘야 할 수 있다. 

```java
Set<Number> uset = GenericUtils2.<Number>union2(set1, set2);
```

### [옮긴이] 매개변수(parameter)와 인수(argument)의 차이

* 어린 백셩을 위해서 친절하게 설명해주셨음
  * 매개변수(parameter)는 메소드 선언/정의 하면서 생기는 (지역)변수를 의미하고
  * 인수(argument)는 클라이언트 코드에서 메소드를 호출할 때 넘겨주는 값을 의미한다.

```java
void add(int value) {}      // value: parameter
void main(String[] args) {
    add(10);                // 10: argument
}

class Set<T> {}             // T: 타입 매개변수(type parameter)
void main(String[] args) {
    Set<Integer> set1;      // Integer: 타입 인수(type argument)
}
```

### 종합본 결정판 예시

```java
public static <E extends Comparable<? super E>> E max(Collection<? extends E> c) {}
```

* PECS 공식에 따라:
  * `Collection`에서 값을 읽어들이므로 `extends`를 사용하고
  * `Comparable`에는 값을 집어넣으므로 `super`를 사용한다.


* 이렇게 복잡하게 해야만 제대로 동작하는 케이스는 `ScheduledFuture<V>`가 있다.
  ```java
  public interface Comparable<E> {}
  public interface Delayed extends Comparable<Delayed> {}
  public interface Future<V> {}
  public interface ScheduledFuture<V> extends Future<V>, Delayed {}
  
  public static <E extends Comparable<E>> E max(Collection<E> c) {}
  
  void main() {
      ScheduledFuture<?> maxVal = max(new ArrayList<ScheduledFuture<?>>()); // compile error
  }
  ```
  * `E`는 `ScheduledFuture<?>`인데 `ScheduledFuture<?>`는
    `Comparable<Delayed>`이지 `Comparable<ScheduledFuture<?>>`가 아니다.
    ```
    Comparable
       ^
       |
    Delayed               Future
       ^                    ^
       |                    |
       |--------------------+
    ScheduledFuture
    ```

### 와일드 카드 선언 두가지 방법

```java
public static <E> void swap(List<E> list, int i, int j) {}      // (1)
public static void swap(List<?> list, int i, int j) {}          // (2)
```

* 이 두 선언 방식은 클라이언트 입장에서는 완전 동일하게 사용 가능하다.
* 저자 왈: 선언된 타입 매개변수가 한번만 나온다면 `<?>`로 대체하라.
  * 그런데 `<?>`로 선언된 메서드의 경우 구현 코드를 작성할 수 없다는 것은 함정
  * 그래서 두가지를 다 써야 한다.

```java
public static void swap(List<?> list, int i, int j) {
    //Object temp = list.get(i);
    //list.set(i, list.get(j));
    //list.set(j, temp);    // List<?>에는 null 이외에는 넣을 수 없다.
    swapHelper(list, i, j);
}
private static <T> void swapHelper(List<T> list, int i, int j) {
    T temp = list.get(i);
    list.set(i, list.get(j));
    list.set(j, temp);
}
```

## 아이템 32: 제네릭과 가변인수를 함께 쓸 때는 신중하라

* 가변인수 메서드를 호출할 때 인수들을 담는 배열을 자동 생성하며 실제로 배열을 넘기는 메소드와 동일하다.
  ```java
  public void func(int... a) {}
  public void func(int[] a) {}      // compile error (func(int...)가 이미 정의되어 있습니다.)
  ```
* 가변인수(varargs)와 제네릭은 Java 5에 동시에 추가되었으나 잘 어울리지 못한다.
* 배열과 제네릭 타입이 문제의 소지가 있는 것 처럼, 
  가변인수와 제네릭 타입을 섞어쓰다 보면 타입 안정성이 깨질 수 있다.

### 가변인수와 제네릭 타입을 같이 쓰는 방법 

* 그냥 쓰면 된다. 제네릭 배열은 금지되어 있지만, 제네릭 타입의 가변인수는 허용되어있다.
  ```python
  print('그 이유는, 제네릭 타입의 가변인수가', '너무'*10, '유용하기 때문이다.')
  ```
* 제네릭 타입의 가변인수를 사용하면 컴파일 오류가 발생하지 않고 경고를 내는 선에서 그친다.
* 그 경고 조차도 개발자가 제네릭 타입의 가변인수가 안전하다는 것을 확인했다는 증표로
  `@SafeVarargs` 어노테이션을 적용하면 경고를 없앨 수 있다.

> 매개변수화된 vararg 타입의 잠재적 힙 오염

> [힙 오염(Heap Pollution)](https://inpa.tistory.com/entry/JAVA-%E2%98%95-%EC%A0%9C%EB%84%A4%EB%A6%AD-%ED%9E%99-%EC%98%A4%EC%97%BC-Heap-Pollution-%EC%9D%B4%EB%9E%80)이란
> 단어 그대로 JVM의 힙(Heap) 메모리 영역에 저장되어있는 특정 변수(객체)가 불량 데이터를 참조함으로써, 만일 힙에서 데이터를 가져오려고 할때 얘기치 못한 런타임 에러가 발생할 수 있는 오염 상태를 일컫는다.
> 
> - Inpa Dev

### `@SafeVarargs` 를 붙이기 위한 조건

* 가변 인수로 넘어온 배열의 값을 수정하지 않는다.
* 배열을 리턴해서 외부로 노출시키지 않는다.

[pickTwo() 예제](../src/test/java/effectivejava/chapter05/item32/GenericVarargsTest.java3#testPickTwo) 참조


## 아이템 33: 타입 안전 이종 컨테이너를 고려하라


## [추가] Switch expression

[`switch` 표현식](https://velog.io/@nunddu/Java-Switch-Expression-in-Java-14)은
Java 12에서 처음 소개 되었고, Java 13, 14 버전을 거치면서 사소한 변경이 몇 차례 있었다.

* `switch`문의 `case`는 절(sentence)이 아니고 레이블(label)이라서 `break`를 쓰지 않으면 다음 `case`가 실행된다.
  * `switch`식은 `case`절 처리가 끝나면 `switch`식 전체가 끝나므로 `break`가 필요없다.
  ```java
  void func(int choice) {
      String display;
      switch (choice) {          // 전통적인 C 스타일
          case 1:
              display = "1";
              System.out.println(display);
              break;
          case 2:
              display = "2";
              System.out.println(display);
              break;
          default:
              display = "default";
              System.out.println(display);
      }

      switch (choice) {          // Java 12 스타일
          case 1 -> {
              display = "1";
              System.out.println(display);
          }
          case 2 -> {
              display = "2";
              System.out.println(display);
          }
          default -> {
              display = "default";
              System.out.println(display);
          }
      }
  }
  ```
* `switch`문은 `break`없이 여러 `case`를 추가하여 다중 `case`를 처리할 수 있었다.
  * `switch`식은 여러 조건을 `case` 조건에 나열하여 가독성이 더 좋게 할 수 있다.
  ```java
  void func(int choice) {
      String display;
      switch (choice) {          // 전통적인 C 스타일
          case 1:
          case 2:
              display = "1or2";
              break;
          case 3:
              display = "3";
              break;
          default:
              display = "default";
      }
      System.out.println(display);

      switch (choice) {          // Java 12 스타일
          case 1, 2 -> {
              display = "1or2";
          }
          case 3 -> {
              display = "3";
          }
          default -> {
              display = "default";
          }
      }
      System.out.println(display);
  }
  ```
* `switch`식은 말 그대로 식(expression)이라서 리턴값이 있어서 inline으로 사용할 수 있다.
  * `case`절이 복잡한 블럭이 되는 경우는 `yield`를 사용하여 리턴값을 지정할 수 있다.
  ```java
  void func(int choice) {
      String display;
      switch (choice) {          // 전통적인 C 스타일
          case 1:
          case 2:
              display = "1or2";
              break;
          case 3:
              display = "3";
              break;
          default:
              display = "default";
      }
      System.out.println(display);

      System.out.println(switch (choice) {          // Java 12 스타일
          case 1, 2 -> "1or2";
          case 3 -> "3";
          default -> "default";
      });
  }
  ```
* `switch`문은 매치되는 `case`가 없고 `default`도 없으면 아무 일도 발생하지 않는다.
  * `switch`식은 `case`가 모든 경우를 커버하지 못하면 컴파일 오류가 발생한다.
    * `default`를 꼭 넣어주거나 `enum` 같은 것으로 범위를 제한해야 한다.
  ```java
  public static enum AppearanceType {
      PRETTY, CUTE, BEAUTIFUL
  }

  public static String getAppearanceTypeName(AppearanceType type) {
      return switch (type) {
          case PRETTY -> "예쁜";
          case CUTE -> "귀여운";
          case BEAUTIFUL -> "아름다운";
      };
  }
  ```
