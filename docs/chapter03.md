# 이펙티브 자바 (2판) - 3장 모든 객체의 공통 메서드

## 목차

* [**규칙 8**: `equals`를 재정의할 때는 일반 규약을 따르라](#규칙-8-equals를-재정의할-때는-일반-규약을-따르라)
* [**규칙 9**: `equals`를 재정의할 때는 반드시 `hashCode`도 재정의하라](#규칙-9-equals를-재정의할-때는-반드시-hashcode도-재정의하라)
* [**규칙 10**: `toString`은 항상 재정의하라](#규칙-10-tostring은-항상-재정의하라)
* [**규칙 11**: `clone`을 재정의할 때는 신중하라](#규칙-11-clone을-재정의할-때는-신중하라)
* [**규칙 12**: `Comparable` 구현을 고려하라](#규칙-12-comparable-구현을-고려하라)
* [**[추가]** Record Class](#record-class)

## 규칙 8: `equals`를 재정의할 때는 일반 규약을 따르라


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
