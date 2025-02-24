# 이펙티브 자바 (2판) - 4장 클래스와 인터페이스

## 목차

* [**규칙 13**: 클래스와 멤버의 접근 권한은 최소화 하라](#규칙-13-클래스와-멤버의-접근-권한은-최소화-하라)
* [**규칙 14**: `public`클래스 안에는 `public` 필드를 두지 말고 접근자 메서드를 사용하라](#규칙-14-public클래스-안에는-public-필드를-두지-말고-접근자-메서드를-사용하라)
* [**규칙 15**: 변경 가능성을 최소화하라](#규칙-15-변경-가능성을-최소화하라)
  * [**[추가]** `List.of()`, `List.copyOf()`](#추가-listof-listcopyof)
  * [**[추가]**  Guava에는 변경 불가능한 기본(primitive) 자료형 배열 클래스가 있음](#추가-guava에는-변경-불가능한-기본primitive-자료형-배열-클래스가-있음)
* [**규칙 16**: 계승하는 대신 구성하라](#규칙-16-계승하는-대신-구성하라)
* [**규칙 17**: 계승을 위한 설계와 문서를 갖추거나, 그럴 수 없다면 계승을 금지하라](#규칙-17-계승을-위한-설계와-문서를-갖추거나-그럴-수-없다면-계승을-금지하라)
  * [**[추가]** 상속관련 토론 주제: 어노테이션 드리븐 개발과 관련 문제](#추가-상속관련-토론-주제-어노테이션-드리븐-개발과-관련-문제)
* [**규칙 18**: 추상 클래스 대신 인터페이스를 사용하라](#규칙-18-추상-클래스-대신-인터페이스를-사용하라)
* [**규칙 19**: 인터페이스는 자료형을 정의할 때만 사용하라](#규칙-19-인터페이스는-자료형을-정의할-때만-사용하라)
* [**규칙 20**: 태그 달린 클래스 대신 클래스 계층을 활용하라](#규칙-20-태그-달린-클래스-대신-클래스-계층을-활용하라)
* [**규칙 21**: 전략을 표현하고 싶을 때는 함수 객체를 사용하라](#규칙-21-전략을-표현하고-싶을-때는-함수-객체를-사용하라)
* [**규칙 22**: 멤버 클래스는 가능하면 `static`으로 선언하라](#규칙-22-멤버-클래스는-가능하면-static으로-선언하라)

## 규칙 13: 클래스와 멤버의 접근 권한은 최소화 하라


## 규칙 14: `public`클래스 안에는 `public` 필드를 두지 말고 접근자 메서드를 사용하라


## 규칙 15: 변경 가능성을 최소화하라



### **[추가]** `List.of()`, `List.copyOf()`

Java 9 부터 `Collections.unmodifiableList()`를 대체할 수 있는 `List.of()`, `List.copyOf()` 추가됨.
당연한 이야기지만 `Set.of()` 등도 있음.

* `<E> List<E> of(E e1, E e2)` - 최소 0 ~ 최대 10개까지 메소드가 준비되어 있음
* `<E> List<E> of(E.. elements)`
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

## 규칙 16: 계승하는 대신 구성하라


## 규칙 17: 계승을 위한 설계와 문서를 갖추거나, 그럴 수 없다면 계승을 금지하라


### **[추가]** 상속관련 토론 주제: 어노테이션 드리븐 개발과 관련 문제

[lombok](https://projectlombok.org/) 같이 어노테이션을 이용해서 코드를 자동 확장하는 경우.
설사 어노테이션 프로세서가 내부적으로 상속 메커니즘을 이용하지 않는다 해도, 상속과 같은 것이라고 할 수 있다.

이런 경우에는 상속을 피하라는 규칙에 위배된다고 볼 수 있을까? (계엄령의 근거가 될 수 있을까?)

멤버 의견:
* Alejandro: 
* Leeturn:
* Scully:
* Lucie:

## 규칙 18: 추상 클래스 대신 인터페이스를 사용하라


## 규칙 19: 인터페이스는 자료형을 정의할 때만 사용하라


## 규칙 20: 태그 달린 클래스 대신 클래스 계층을 활용하라


## 규칙 21: 전략을 표현하고 싶을 때는 함수 객체를 사용하라


## 규칙 22: 멤버 클래스는 가능하면 `static`으로 선언하라


