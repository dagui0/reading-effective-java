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
    * (개념적으로) `List instanceof List<String>` == true
    * (개념적으로) `List<Object> instanceof List<String>` == false
    * 따라서 `List<String>`을 받는 메소드에 `List`를 넘길 수는 있지만, `List<Object>`를 넘길 수는 없다. [RawTypeTest.java](../src/test/java/effectivejava/chapter05/item26/RawTypeTest.java)

### 비한정적 와일드카드 타입(unbounded wildcard type)

* `List<?>`처럼 타입 매개변수와 무관하게 사용할 수 있는 변수형
  * `List` 은 안되지만 `List<?>`는 써도 된다.
  * `List<?>`에는 어떤 타입의 원소도 넣을 수 없으므로 안전하다.

### 로 타입을 사용해야 하는 경우

* 제너릭 타입은 컴파일 시점의 검사로만 적용되는 것으로 실제 컴파일된 결과는 로 타입으로 저장된다.
  * 클래스 객체를 사용해야 할 경우. `List<String>.class` 같은거는 없다.
  * `instanceof` 와 사용할 경우 `instanceof String<List>` 같은 거는 안된다.

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
@Target(value={TYPE,FIELD,METHOD,PARAMETER,CONSTRUCTOR,LOCAL_VARIABLE})
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

* [Intelij IDEA]()에서는 `noinspection` 으로 시작하는 주석을 이용해서
  어노테이션을 붙일 수 없는 구분에서도 경고를 중지시킬 수 있다.
* 이 기능은 인텔리제이에서만 적용되는 것으로 사용하지 않는 것이 바람직하다고 생각되는데 토론에 붙여보자.

* 제네릭 관련 경고
  * 비검사 형 변환 경고
  * 비검사 메서드 호출 경고
  * 비검사 매개변수화 가변인수 타입 경고
  * 비검사 변환 경고




## 아이템 28: 배열보다는 리스트를 사용하라



## 아이템 29: 이왕이면 제네릭 타입으로 만들라


## 아이템 30: 이왕이면 제네릭 메서드로 만들라


## 아이템 31: 한정적 와일드카드를 사용해 API 유연성을 높이라


## 아이템 32: 제네릭과 가변인수를 함께 쓸 때는 신중하라


## 아이템 33: 타입 안전 이종 컨테이너를 고려하라


