# 이펙티브 자바 (3판) - 6장 열거 타입과 애너테이션

## 목차

* [**아이템 34**: `int` 상수 대신 열거 타입을 사용하라](#아이템-34-int-상수-대신-열거-타입을-사용하라)
* [**아이템 35**: `ordinal` 메서드 대신 인스턴스 필드를 사용하라](#아이템-35-ordinal-메서드-대신-인스턴스-필드를-사용하라)
* [**아이템 36**: 비트 필드 대신 `EnumSet`을 사용하라](#아이템-36-비트-필드-대신-enumset을-사용하라)
* [**아이템 37**: `ordinal` 인덱스 대신 `EnumMap`을 사용하라](#아이템-37-ordinal-인덱스-대신-enummap을-사용하라)
* [**아이템 38**: 확장할 수 있는 열거 타입이 필요하면 인터페이스를 사용하라](#아이템-38-확장할-수-있는-열거-타입이-필요하면-인터페이스를-사용하라)
* [**아이템 39**: 명명 패턴보다 애너테이션을 사용하라](#아이템-39-명명-패턴보다-애너테이션을-사용하라)
* [**아이템 40**: `@Override` 애너테이션을 일관되게 사용하라](#아이템-40-override-애너테이션을-일관되게-사용하라)
* [**아이템 41**: 정의하려는 것이 타입이라면 마커 인터페이스를 사용하라](#아이템-41-정의하려는-것이-타입이라면-마커-인터페이스를-사용하라)
* [**[추가]**: Annotation Proccessor 개발](#추가-annotation-proccessor-개발)

## 아이템 34: `int` 상수 대신 열거 타입을 사용하라

```java
public class Constants {
    public static final int APPLE_FUJI = 0;
    public static final int APPLE_PIPPIN = 1;
    public static final int APPLE_GRANNY_SMITH = 2;
    
    public static final int ORANGE_NAVEL = 0;
    public static final int ORANGE_TEMPLE = 1;
    public static final int ORANGE_BLOOD = 2;
}
```

* [아이템 22](#아이템-22-상수-인터페이스를-사용하지-말라)에서 유형을 정의하는 상수는 열거형을 사용하라고 언급했었다.
* 정수형 상수의 문제점(Integer enum pattern)
  * 다른 유형의 상수를 지정해도 컴파일러는 알려줄 수 없다.
  * 이름 공간의 충돌을 시스템적으로 막을 수 없다. (prefix를 사용한다)
  * 바이너리 호환성에 문제가 발생할 수 있다.
    * 컴파일되면 단지 정수 상수가 되기 때문에, 값이 변경될 경우 재컴파일을 하지 않으면 예전 값을 계속 사용할 수 있다.
  * 정수 상수는 문자열로 변환하기 어렵다. (디버거 등으로 확인할 때 정수 값만 보인다)
  * 같은 범주의 상수가 몇개가 있는 지를 알 수 없다. 그룹의 모든 상수를 순회할 수 없다.
* 정수 대신 문자열을 사용하는 경우의 문제점(String enum pattern)
  * 상수 대신 문자열을 직접 하드코딩하게 만들 수 있다.
  * 오타가 발생하도 사전 검증할 수 없다. 

### Java의 열거 타입

```java
public enum Apple { FUJI, PIPPIN, GRANNY_SMITH }
public enum Orange { NAVEL, TEMPLE, BLOOD }
```

* 모든 열거형은 하나의 완전한 클래스로 동작한다.
  * 모든 열거 상수는 인스턴스이며 사전에 생성되어 `public static final`로 공개된다.
  * 새로운 인스턴스를 만들 수 없으므로, 인스턴스가 확실하게 통제된다.
  * 한개의 상수가 있는 열거 타입은 싱글턴이고, 반대로 열거타입은 싱글턴을 일반화한 형태로 볼 수 있다.
  * 열거타입은 정수 열거 상수 패턴의 모든 단점을 해결한다.
    * 타입 안정성, 이름 충돌 문제, 문자열 변환, 순회 문제(`values()`)
    * 바이너리 호환성: 컴파일될 때 정수가 아니라 이름으로 저장되므로 값이 변경되어도 문제가 없다.
* 완전한 클래스이기 때문에 다른 언어의 열거타입이 가지지 못하는 장점이 있다.
  * 메소드를 만들 수 있음
  * 필드를 만들 수 있음(단, final이어야 함)
  * 인터페이스를 구현할 수 있음
  * `Object`의 기본 메소드, `toString()`, `equals()`, `hashCode()` 메서드가 열거 상수에 맞게 오버라이드 되어 있다.
  * 기본적으로 `Comparable`, `Serializable`을 구현하고 있다.

```java
public enum Planet {

    MERCURY(    "수성",   3.302e+23, 2.439e6)
    ,VENUS(     "금성",   4.869e+24, 6.052e6)
    ,EARTH(     "지구",   5.975e+24, 6.378e6)
    ,MARS(      "화성",   6.419e+23, 3.393e6)
    ,JUPITER(   "목성",   1.899e+27, 7.149e7)
    ,SATURN(    "토성",   5.685e+26, 6.027e7)
    ,URANUS(    "천왕성", 8.683e+25, 2.556e7)
    ,NEPTUNE(   "혜왕성", 1.024e+26, 2.477e7)
    ,@Deprecated PLUTO("명왕성", 1.303e+22, 	1.186e6)
    ;

    private final String name;
    private final double mass;              // kg
    private final double radius;            // m
    private final double surfaceGravity;    // m/s^2
    private static final double G = 6.673E-11;

    Planet(String name, double mass, double radius) {
        this.name = name;
        this.mass = mass;
        this.radius = radius;
        this.surfaceGravity = Planet.G * mass / (radius * radius);
    }

    public double mass() { return mass; }
    public double radius() { return radius; }
    public double surfaceGravity() { return surfaceGravity; }
    public double surfaceWeight(double mass) {
        return mass * surfaceGravity();
    }

    @Override
    public String toString() {
        return name;
    }
}
```

* `@Getter`를 사용할 수도 있다.
  * 단, 메소드 네이밍은 `getName()`처럼 할 수도 있지만, `name()` 같이 하는 경우가 더 많다고 한다.

### 상수의 추가/삭제와 관련된 안전성

* 2006년 8월 24일 체코 프라하에서 개최된 국제천문연맹(IAU, International Astronomical Union) 총회에서 명왕성은 행성의 지위를 박탈당했다.
  * 이에 따라서 `Planet.PLUTO`를 삭제하게 되더라도, 직접적으로 삭제된 상수를 사용하는 코드가 아니라면 아무런 문제가 없다.
  * `Planet.PLUTO`를 직접 참조하는 코드는 `java.lang.NoSuchFieldError` 예외를 발생시킴
  * 새로운 상수가 추가되는 경우 기존 코드에 아무런 영향이 없을 것이라고 책에는 되어있음

```java
public enum ArithmeticOperation {

    PLUS, MINUS, TIMES, DIVIDE;

    public double apply(double x, double y) {
        return switch (this) {
            case PLUS -> x + y;
            case MINUS -> x - y;
            case TIMES -> x * y;
            case DIVIDE -> x / y;
        };
    }
}
```

* 위 코드에 `MODULO`라는 연산을 추가하게 되면 다음과 같은 컴파일 오류가 발생한다.
  * `'switch' 식이 모든 가능한 입력 값을 포함하지 않습니다`

### `enum`에서의 `abstract` 메서드

* `enum` 클래스는 상속이 불가능하다. 기본적으로 `final`로 취급되며 모든 `enum` 클래스의 부모는 `java.lang.Enum`으로 고정되어 있다.
  * 그럼에도 불구하고 `enum` 클래스는 `abstract` 메서드를 가질 수 있는데, 하위 클래스가 아니라 상수별로 재정의를 할 수 있다.
  * [ArithmeticOperation2.java](../src/main/java/effectivejava/chapter06/item34/ArithmeticOperation2.java)

```java
public enum ArithmeticOperation2 {

    PLUS("+") {
        @Override
        public double apply(double x, double y) {
            return x + y;
        }
    },
    MINUS("-") {
        @Override
        public double apply(double x, double y) {
            return x - y;
        }
    };

    public abstract double apply(double x, double y);
}
```

### 열거 클래스에서 정적 필드를 초기화하는 방법

```java
public enum ArithmeticOperation3 {
    PLUS("+"), MINUS("-"), TIMES("*"), DIVIDE("/");

    private final String operator;
    private static final Map<String, ArithmeticOperation3> stringToEnum = new HashMap<>();

    ArithmeticOperation3(String operator) {
        this.operator = operator;
        stringToEnum.put(operator, this);
    }
    
    @Override
    public String toString() {
        return operator;
    }
    
    public static ArithmeticOperation3 fromString(String symbol) {
        return stringToEnum.get(symbol);
    }
}
```

* `enum` 생성자가 실행되는 시점에는 `static` 필드가 초기화되지 않은 상태이다.
  1. `enum` 클래스가 초기화된다.
  2. 각각의 상수가 초기화된다.
  3. `static` 필드가 초기화된다.
  4. `static` 블럭이 실행된다.

```java
public enum ArithmeticOperation3 {
    PLUS("+"), MINUS("-"), TIMES("*"), DIVIDE("/");

    private final String operator;
    private static final Map<String, ArithmeticOperation3> stringToEnum = new HashMap<>();
    
    static {
        for (ArithmeticOperation3 op : ArithmeticOperation3.values())
            stringToEnum.put(op.operator, op);
    }

    ArithmeticOperation3(String operator) {
        this.operator = operator;
    }
}
```

### 열거 타입과 `switch` 문

* 저자는 열거타입을 `switch` 문과 사용하는 것을 위험하다고 생각한다.
  * 열거 상수가 추가되었을 때 `switch`문에 `case` 절을 추가하지 않고 넘어갈 위험이 있기 때문이다. ([PayrollDay1.java](../src/main/java/effectivejava/chapter06/item34/PayrollDay1.java))
  * 그래서 전략 상수 열거타입을 추가하는 방안을 제시한다. (([PayrollDay2.java](../src/main/java/effectivejava/chapter06/item34/PayrollDay2.java)))
  * 이 문제는 새로 추가된 `switch` 표현식 문법을 잘 사용하면 보완할 수 있다. ([PayrollDay.java](../src/main/java/effectivejava/chapter06/item34/PayrollDay.java))


## 아이템 35: `ordinal` 메서드 대신 인스턴스 필드를 사용하라

* `enum` 클래스에는 `ordinal()` 메서드가 있는데 쓰지마라. 끗.
  * `Enum`의 문서에 따르면 `ordinal()` 메서드는 `EnumSet`과 `EnumMap` 같은 열거타입 기반의 범용 자료구조를 위해 만들어졌다.

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


## 아이템 36: 비트 필드 대신 `EnumSet`을 사용하라

* 비트 필드를 사용하는 경우 `EnumSet`을 사용하라. 끗.

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

```java
class Text {
    public static final int STYLE_BOLD          = 1; // 1
    public static final int STYLE_ITALIC        = 1 << 1; // 2
    public static final int STYLE_UNDERLINE     = 1 << 2; // 4
    public static final int STYLE_STRIKETHROUGH = 1 << 3; // 8
    public static final int STYLE_SUPERSCRIPT   = 1 << 4; // 16
    public static final int STYLE_SUBSCRIPT     = 1 << 5; // 32
    
    public void applyStyles(int styles) {}
    
    public static void main(String[] args) {
        Text text = new Text();
        text.applyStyles(STYLE_BOLD | STYLE_ITALIC | STYLE_UNDERLINE);
    }
}
```

* `EnumSet`은 비트 필드의 단점을 커버하면서 장점은 유지한다.
  * `EnumSet`은 비트 필드 알고리즘을 범용화하여 구현해놓은 것이다.  
  * 비트 필드의 장점
    * 비트 필드는 메모리 사용량이 적다.
    * 비트 필드는 성능이 좋다.
  * 비트 필드의 단점
    * 비트 필드는 가독성이 떨어진다.
    * 비트 필드는 타입 안전하지 않다.

```java
class Text {
    public enum Style {
        BOLD,
        ITALIC,
        UNDERLINE,
        STRIKETHROUGH,
        SUPERSCRIPT,
        SUBSCRIPT
    }

    public void applyStyles(Set<Style> styles) {}
    
    public static void main(String[] args) {
        Text text = new Text();
        text.applyStyles(EnumSet.of(Style.BOLD, Style.ITALIC, Style.UNDERLINE));
    }
}
```

## 아이템 37: `ordinal` 인덱스 대신 `EnumMap`을 사용하라

* 앞서 분명 `ordinal()` 메서드를 쓰지 말라고 했다. ([아이템 35](#아이템-35-ordinal-메서드-대신-인스턴스-필드를-사용하라))
    * `ordinal()` 메서드로 얻은 값을 배열의 인덱스로 사용하는 경우 `EnumMap`을 사용하면된다.
    * `EnumSet`과 마찬가지로 `EnumMap`은 배열을 이용한 알고리즘을 범용화 하여 구현해 놓은 것이다.

```java
public void testPlant1() {

    @SuppressWarnings("unchecked")
    Set<Plant>[] plantsByLifeCycle = (Set<Plant>[]) new Set[Plant.LifeCycle.values().length];

    for (int i = 0; i < plantsByLifeCycle.length; i++) {
        plantsByLifeCycle[i] = new TreeSet<>();
    }

    for (Plant plant : plants) {
        plantsByLifeCycle[plant.lifeCycle().ordinal()].add(plant);
    }
}
```

* `EnumMap`을 사용하면 다음과 같이 쓸 수 있다.

```java
@Test
public void testPlant2() {
    Map<Plant.LifeCycle, Set<Plant>> plantsByLifeCycle =
            new EnumMap<>(Plant.LifeCycle.class);

    for (Plant.LifeCycle lifeCycle : Plant.LifeCycle.values()) {
        plantsByLifeCycle.put(lifeCycle, new TreeSet<>());
    }

    for (Plant plant : plants) {
        plantsByLifeCycle.get(plant.lifeCycle()).add(plant);
    }
}
```

* `EnumMap`을 생성을 람다를 이용하여 한줄로 바꾸는 방법

```java
@Test
public void testPlant3() {
    Map<Plant.LifeCycle, Set<Plant>> plantsByLifeCycle =
            plants.stream().collect(
                    groupingBy(Plant::lifeCycle,
                            () -> new EnumMap<>(Plant.LifeCycle.class),
                            Collectors.toCollection(TreeSet::new)));
}
```


## 아이템 38: 확장할 수 있는 열거 타입이 필요하면 인터페이스를 사용하라

* 초판에서 소개한 타입 안전 열거형 패턴(Type-safe enum pattern)
  * 모든 상황에서 `enum` 타입이 좋지만, 상수를 추가할 수 없다는 점은 열거 패턴이 낫다.
  * 초판의 타입 안전 열거 패턴이란 `enum`이 Java 1.5에서 추가되기 전에 사용하던 방법으로, `enum` 클래스를 수동으로 만드는 것과 같다.
    ([Color.java](../src/main/java/effectivejava/chapter06/item38/Color.java))
* 어떻게 상수를 추가할 수 있는가?
  * `enum` 클래스는 상속도 할 수 없으므로 이미 정의된 열거 타입을 확장할 방법이 없음
  * `enum` 클래스를 인터페이스를 상속하도록 해서 같은 인터페이스를 구현하는 추가 `enum`을 만들 수 있다.

```java
public interface Operation {
    double apply(double x, double y);
}
public enum BasicOperation implements Operation {
    PLUS("+") { @Override public double apply(double x, double y) { return x + y; } },
    MINUS("-") { @Override public double apply(double x, double y) { return x - y; } },
    TIMES("*") { @Override public double apply(double x, double y) { return x * y; } },
    DIVIDE("/") { @Override public double apply(double x, double y) { return x / y; } };
}
public enum ExtendedOperation implements Operation {
    EXP("^") { @Override public double apply(double x, double y) { return Math.pow(x, y); } },
    REMAINDER("%") { @Override public double apply(double x, double y) { return x % y; } };
}
```

* Java API에도 이 패턴을 사용하는 사례가 있다.

```java
package java.nio.file;
public enum LinkOption implements OpenOption, CopyOption {
    NOFOLLOW_LINKS;
}
```

## 아이템 39: 명명 패턴보다 애너테이션을 사용하라

* 과거 Java에서는 명명 패턴을 많이 사용됨
  * 식별자 이름에 역할을 알 수 있는 규칙을 적용하는 것. `MemberDto`, `MemberService` 등
    * JavaBeans는 `get*()`, `set*()`, `is*()` 같은 식별자 규칙을 정해놓았음
    * 초기 Spring AOP는 `*Service.set*()` 같은 식으로 권한이나 로깅 규칙을 정하여 Aspect를 주입했었음
    * 초기 JUnit은 `test*()`, `setUp()`, `tearDown()` 같은 식으로 메소드 네이밍 규칙이 있었음
  * 장점
    * Reflection 기반 구현이 간단함
    * 식별자 이름으로 역할을 명확히 알 수 있음
    * 별도의 설정이 필요없고 규칙대로만 하면 동작함
  * 단점
    * 코드가 장황해짐
    * 오타가 나면 안됨
    * 반드시 정해진 규칙대로 만들어야 함 (이름이 의미를 전달하는 것 이상의 규칙에 제약됨)
    * 이름 외에 추가적인 정보(매개변수)를 지정할 방법이 없음

### 애너테이션(Annotation)

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {}
```

* 애너테이션은 별도의 처리기에의해 해석되기 위한 것으로, 애너테이션 인터페이스는 단지 선언적 역할만을 함
* 메타 애너테이션(meta-annotation) - 애너테이션 인터페이스 선언(`ElementType.ANNOTATION_TYPE`)에 적용되는 애너테이션
  * `@Retention(RetentionPolicy)` - 애너테이션이 유지되는 범위
    * `RetentionPolicy.SOURCE`: 컴파일러가 애너테이션을 제거 (컴파일 시점까지만 존재)
      * 컴파일러에 처리기(Annotation Processor)를 등록하여 처리(컴파일 오류, 경고 등 생성)
      * Lombok, JPA, Spring Data JPA 등
    * `RetentionPolicy.CLASS`: 클래스 파일에 애너테이션을 저장하지만, 런타임에는 제거 (바이트코드에만 존재)
      * 바이트코드 분석 및 조작을 지원하는 도구(바이트코드 수준의 AOP 구현 가능, 정적 분석기 등 개발/빌드 환경용 도구)
    * `RetentionPolicy.RUNTIME`: 런타임에도 애너테이션이 유지됨
      * Replection API로 접근 가능
      * JUnit, Spring, Hibernate 등
  * `@Target(ElementType)` - 애너테이션이 적용될 수 있는 대상(요소)을 지정
    * `ElementType.TYPE`: 클래스, 인터페이스, 열거형, 어노테이션 타입
    * `ElementType.FIELD`: 필드
    * `ElementType.METHOD`: 메서드
    * `ElementType.PARAMETER`: 매개변수
    * `ElementType.CONSTRUCTOR`: 생성자
    * `ElementType.LOCAL_VARIABLE`: 지역 변수
    * `ElementType.ANNOTATION_TYPE`: 애너테이션 타입
    * `ElementType.PACKAGE`: 패키지
    * `ElementType.TYPE_PARAMETER`: 제네릭 타입 매개변수 선언 (java 8+)
    * `ElementType.TYPE_USE`: 타입 사용 위치(인자, 반환값, 변수 선언, `instanceof` 등) (java 8+)
  * `@Inherited` - 애너테이션이 상속될 수 있음 (부모 클래스에만 지정하면 자식 클래스에도 적용)
    * `ElementType.TYPE`, `ElementType.PACKAGE` 등과 함께 사용
  * `@Repeatable` - 같은 애너테이션을 여러개 지정 가능
  * `@Documented` - 애너테이션이 문서화될 때 포함
    * [Spring MVC - @Documented 애노테이션](https://goodgid.github.io/Spring-MVC-Documented-Annotation/)

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExcetpionTest {
    Class<? extends Throwable> value();
}

class MyTest {
    @ExcetpionTest(NullPointerException.class)
    public void testNullPointer() {
        // ...
    }
}
```

* 애너테이션 인터페이스의 메소드 선언(인자 없는)은 에너테이션의 속성(또는 매개변수)으로 사용됨
  * 기본 속성명은 `value()`로 고정되어 있음. (속성명 없이 지정 가능)
  * `value()` 외에 추가적인 속성을 정의하는 것도 가능 `@MyAnnotation("name", age = 30)`
  * 속성을 배열로 설정할 수도 있음 `@MyAnnotation("Alejandro", friends={"Scully", "Leeturn", "Lucy"})`

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExcetpionTest {
    Class<? extends Throwable>[] value();
    /// null이면 method 명을 테스트케이스명으로 사용함
    String testCaseName() default null;
}

class MyTest {
    @ExcetpionTest({NullPointerException.class, IllegalArgumentException.class})
    public void testNullPointer() {
        // ...
    }
}
```

### `@Repeatable` 예시 

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(ExcetpionTestContainer.class)
public @interface ExcetpionTest {
    Class<? extends Throwable> value();
    /// `false`이면 하위 클래스는 허용되지 않고 지정한 예외여야만 통과됨 
    boolean allowSubclasses() default true;
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExcetpionTestContainer {
    ExcetpionTest[] value();
}

class MyTest {
    @ExcetpionTest(IOException.class, allowSubclasses = false)
    @ExcetpionTest(IllegalArgumentException.class)
    public void testExactIOExceptionAndOther() {
        // ...
    }
}
```

* `T[] value()`: 처리기에게 복수의 스칼라값 전달하는 경우 적합
* `@Repeatable`: 처리기에게 복수의 복합 객체(애너테이션의 속성이 여러개 있는 경우) 전달 가능
  * 처리 프로그램은 `ExcetpionTestContainer`를 이용하여 `ExcetpionTest` 목록을 조회  가능

## 아이템 40: `@Override` 애너테이션을 일관되게 사용하라

```java
class Bigram {
    // ...
    @Override                         // 오버라이드 되지 않았다고 컴파일 오류 발생
    public boolean equals(Bigram o) { // 인자 타입이 잘못됨
        // ...
    }
}
```

* `@Override` 애너테이션은 메소드가 오버라이드된 것임을 명시적으로 표시
  * 컴파일러가 오버라이드된 메소드인지 확인해줌
  * 오타로 인해 오버라이드가 되지 않는 경우를 방지할 수 있음
  * 상위 타입의 메소드 시그니처가 변경되거나 삭제되었을 때 컴파일 오류로 변경 사항을 확인시켜줌
  * 코드가 복잡해 지는 듯하지만 인터페이스 구현이나 상속 관계를 명확히 드러내서 이해를 돕는 면도 있음
* 결론 `@Override`는 무조건 붙이자. 끝.

## 아이템 41: 정의하려는 것이 타입이라면 마커 인터페이스를 사용하라

> "정의하는 것이 타입이라면"이 정확하게 무슨 뜻일까?

* 마커란?
  * 객체가 특정 목적을 가지고 있다거나, 특정 유형이라는 것을 표시하는 것
* 마커 인터페이스(Marker Interface)란 아무 메소드도 정의하지 않는 인터페이스
  * `Serializable`, `Cloneable`, `Remote` 등
  * 애너테이션이 나오면서 의미가 많이 퇴색했고, 마커 용도로 애너테이션이 많이 사용됨
* 마커 인터페이스의 장점
  * 마커 인터페이스는 타입이므로 컴파일타임 검사가 가능
    * 인자 목록에 "특정 애너테이션이 붙은 객체만 허용" 같은 규칙은 설정 불가함 
    * 애너테이션은 Annotation Processor에 의해 처리되는 경우 제한적인 컴파일 타임 규칙 설정 가능
    * Annotation Processor의 개발과 설정은 간단하지 않음
  * 적용 대상을 정밀하게 지정 가능
    * `ElementType.TYPE`으로 설정된 애너테이션은 클래스, 인터페이스, 열거 타입, 애너테이션에 모두 적용 가능
    * 마커 인터페이스는 특정 인터페이스, 클래스 트리에만 적용되게 할 수 있음
      * `interface SerializableData extends Data, Serializable {}`
      * `interface EntitySet<T extends Entity> extends Set<T> {}`
* 마커 애너테이션의 장점
  * 애너테이션 시스템은 유연하고 확장 가능한 처리 방식을 다양하게 지원(컴파일타임, 런타임 등)
  * 일관성: 이미 애너테이션을 지향 컨벤션을 채택한 라이브러리들이 매우 많이 사용되고 있음

### 애너테이션 vs. 마커 인터페이스 

* 클래스와 인터페이스 외에 다양한 요소를 마킹해야 하는 경우 -> 애너테이션
* 마킹된 객체를 인자로 받는 메서드가 필요한가? -> 마커 인터페이스
  * "마킹하는 대상이 하나의 타입을 의미하는 것인가?"와 같은 질문
* 프레임워크나 라이브러리에서 제공하는 애너테이션이 있음 -> 그것을 사용
* 잘 모르겠는데? -> 애너테이션 권장

## [추가] Annotation Proccessor 개발

```java
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Inherited
public @interface CheckEqualsAndHashCode {}

/// 프로젝트 공통 추상 객체
@CheckEqualsAndHashCode
public abstract class BaseDomainObject<K extends PrimaryKey> implements DomainObject<K> {}

/// 구체 클래스
public class User extends BaseDomainObject<User.Key> {

    private long userNo;
    private String name;
    // ...
    public record Key(long userNo) implements PrimaryKey {}
    // ...
}
```

* `@CheckEqualsAndHashCode`
  * `equals()`와 `hashCode()` 메서드를 명시적으로 재정의 했는지 검사하는 목적
  * `@Retention(RetentionPolicy.SOURCE)`: 컴파일 타임에만 적용됨
  * `@Inherited`: `User`도 `@CheckEqualsAndHashCode`가 적용됨

```java
@SupportedAnnotationTypes("com.yidigun.base.CheckEqualsAndHashCode")
@SupportedSourceVersion(SourceVersion.RELEASE_23)
public class CheckEqualsAndHashCodeProcessor extends AbstractProcessor {
    
    // ...

    /// 오버라이드 여부 확인 결과
    class EqualsAndHashCode {
        private final TypeElement element;          // Class<?> - 컴파일 타임에는 Reflection API와는 다른 API를 사용함
        private final ExecutableElement equals;     // Method
        private final boolean equalsOverridden;
        private final ExecutableElement hashCode;
        private final boolean hashCodeOverridden;
        // ...
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        // ...

        // 처리 시작
        roundEnv.getElementsAnnotatedWith(CheckEqualsAndHashCode.class)
                .stream()
                .filter(e ->
                        e.getKind() == ElementKind.CLASS && !e.getModifiers().contains(Modifier.ABSTRACT)
                )
                .filter(e -> !isWarningSuppressed(e)) // SuppressWarnings 확인
                .map(clazz -> getEqualsAndHashCode((TypeElement)clazz))
                .filter(result -> !result.equalsOverridden() || !result.hashCodeOverridden())
                .forEach((result) -> {
                    // 컴파일러 경고 출력
                    processingEnv.getMessager()
                            .printMessage(Diagnostic.Kind.WARNING, result.message(), result.element);
                });
        return true;
    }

    /// 클래스에서 [Object#equals(Object)]와 [Object#hashCode()] 메소드를 찾아서 반환한다.
    private EqualsAndHashCode getEqualsAndHashCode(TypeElement type) {
        // ...
    }
}
```

* `java.lang.annotation.AbstractProcessor`를 상속하여 애너테이션 프로세서를 구현
  * `@SupportedAnnotationTypes` - 처리할 애너테이션 타입을 지정
* 컴파일타임 프로세서의 테스트는 쉽지 않은데 Google의 `compile-testing` 라이브러리를 사용하면 편리함
  ```kotlin
  dependencies {
      // compile testing
      testImplementation("com.google.guava:guava:${project.ext["guava.version"]}")
      testImplementation("com.google.testing.compile:compile-testing:${project.ext["compile.testing.version"]}")
  }
  ```
  ```java
  @Test
  public void testEnclosedStaticClass() {
  
      String source = """
  ...
  public class OuterClass {
  
      @Getter
      @RequiredArgsConstructor
      @CheckEqualsAndHashCode
      public static class StaticEnclosedNotOverrideBoth {
          private final String name;
      }
  }
  """;
  
      Compilation compilation = getTestCompiler()
              .compile(JavaFileObjects.forSourceString(
                      "com.yidigun.base.processors.OuterClass", source));
  
      String expectedClass = "com.yidigun.base.processors.OuterClass.StaticEnclosedNotOverrideBoth".replace(".", "\\.");
      String expectedMethods = "equals\\(\\), hashCode\\(\\)";
      Pattern warningPattern = Pattern.compile(
              String.format("^%s 클래스는 %s 메소드를 재정의해야 합니다\\.$", expectedClass, expectedMethods));
  
      assertThat(compilation).succeeded();
      assertThat(compilation).hadWarningContainingMatch(warningPattern);
  }
  ```
* 프로세서를 개발하고나면 정해진 규약에 맞게 패키징 필요
  * `META-INF/services/javax.annotation.processing.Processor` 파일에 프로세서 클래스 이름을 지정
    ```
    com.yidigun.base.processors.CheckEqualsAndHashCodeProcessor
    ```
* 컴파일러 실행시 프로세서를 등록해줘야함
  * Javac
    ```bash
    javac -cp project-base.jar:... \
          -processor com.yidigun.base.processors.CheckEqualsAndHashCodeProcessor \
          -processorpath project-base.jar \
          ...
    ```
  * Gradle
    ```groovy
    dependencies {
        implementation 'com.yidigun:project-base:0.0.1'
        annotationProcessor 'com.yidigun:project-base:0.0.1'
    }
    ```
