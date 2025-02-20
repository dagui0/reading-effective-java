# 이펙티브 자바 (2판) - 2장 객체의 생성과 삭제

## 목차

* [**규칙 1**: 생성자 대신 정적 펙터리 메서드를 사용할 수 없는지 생각해 보라](#규칙-1-생성자-대신-정적-펙터리-메서드를-사용할-수-없는지-생각해-보라)
* [**규칙 2**: 생성자 인자가 많을 때는 Builder 패턴 적용을 고려하라](#규칙-2-생성자-인자가-많을-때는-builder-패턴-적용을-고려하라)
* [**규칙 3**: `private` 생성자나 `enum` 자료형은 싱글턴 패턴을 따르도록 설계하라](#규칙-3-private-생성자나-enum-자료형은-싱글턴-패턴을-따르도록-설계하라)
* [**규칙 4**: 객체 생성을 막을 때는 `private` 생성자를 사용하라](#규칙-4-객체-생성을-막을-때는-private-생성자를-사용하라)
* [**규칙 5**: 불필요한 객체는 만들지 말라](#규칙-5-불필요한-객체는-만들지-말라)
* [**규칙 6**: 유효기간이 지난 객체 참조는 폐기하라](#규칙-6-유효기간이-지난-객체-참조는-폐기하라)
* [**규칙 7**: 종료자(finalizer) 사용을 피하라](#규칙-7-종료자finalizer-사용을-피하라)
  * [**[추가]** Java 7 `try-with-resources` 문](#추가-java-7-try-with-resources-문)

## 규칙 1: 생성자 대신 정적 펙터리 메서드를 사용할 수 없는지 생각해 보라

`java.lang.Boolean`의 정적 팩터리 메서드를 만드는 예시

```java
public static Boolean valueOf(boolean b) {
    return b? Boolean.TRUE: Boolean.FALSE;
}
```

이 방법은 GoF의 Factory Method Pattern과는 다르며 GoF의 다른 어떤 패턴과도 다르다.

### 정적 팩터리 메서드의 장점

#### 1. 생성자와 달리 정적 팩터리 메서드에는 이름이 있다.

생성자는 이름이 없어서 기능을 설명할 수 없지만 이름이 있으면 설명이 가능함
```java
bi = new BigInteger(int, int, Random);  // 생성자의 기능을 파악하기 어려움
bi = BigInteger.probablePrime(int, int, Random); // 생성자의 기능을 유추 가능
```

생성자는 같은 시그니처를 가지는 생성자를 여러개 만들 수 없다. 그렇게 하려면 인자의 순서를 바꿔야 하는데 아마도 이해할 수 없는 코드가 될것이다.
```java
bi = new BigInteger(int, int, Random);     // 쌍
bi = new BigInteger(int, Random, int);     // 욕
bi = new BigInteger(Random, int, int);     // 나옴
```

#### 2. 생성자와 달리 새로운 객체를 만들 필요가 없다.

새로운 객체를 생성하지 않고 만들어진 객체 캐시을 유지할 수도 있다. 이렇게 하면 [Flyweight 패턴](https://inpa.tistory.com/entry/GOF-%F0%9F%92%A0-Flyweight-%ED%8C%A8%ED%84%B4-%EC%A0%9C%EB%8C%80%EB%A1%9C-%EB%B0%B0%EC%9B%8C%EB%B3%B4%EC%9E%90)과 유사하다.

개체 통제 클래스instance-controlled class:

* 특정 시점에 인스턴스가 얼마나 만들어질 지 통제할 수 있다
* 생성이 불가능한 클래스(eg: 싱글턴)을 만들면 `equals()`가 `==` 연산자로 비교 가능하여 성능이 향상된다.

#### 3. 생성자와 달리 하위 클래스를 리턴할 수 있다.

`public`으로 선언되지 않은 하위 클래스를 리턴 가능하다.

인터페이스 기반 프레임워크 interface-based framework ([규칙 18](chapter04.md#규칙-18-추상-클래스-대신-인터페이스를-사용하라)):

* 인터페이스는 메서드를 가질 수 없으므로 전용 클래스를 만든다. (`SomeType SomeTypes.getSomeType()`)
  > 이거 자바 8부터 [인터페이스에 메서드 추가가 가능](https://blog.naver.com/amas1004/222287203050)한데 `static`도 가능한가?
* 인터페이스 기반의 장점은 API의 규모가 줄어드는 것이 아니라 개념상의 무게감 conceptual weight이 줄어든 것이다.
    * Java Collection API 에 32개의 비 `public` 구현 클래스들이 있지만 `java.util.Collection`을 구현하므로 따로 문서화할 필요가 없다.
* 버전에 따라서 다른 구현체를 리턴할 수도 있으므로 유지보수성이 높아진다.
* `java.util.EnumSet`([규칙 32](chapter06.md#규칙-32-비트필드bit-field-대신-enumset을-사용하라))은 `public` 생성자가 없고 정적 팩토리 메서드 뿐이다.
    * `enum` 상수의 개수가 64개 이하인 경우 내부적으로 `long` 변수의 비트를 활용하는 버전(`RegularEnumSet`)을 리턴
    * 64개 초과인 경우 `long` 형 배열을 사용하는 `JumboEnumSet` 을 리턴한다.
    * 다음 릴리즈에서 `JumboEnumSet`이 성능이 만족하지 않는다면 `MegaEnumSet`이라던가 `GigaEnumSet`으로 변경될 수 있다.

서비스 제공자 프레임워크service provider framework (eg: JDBC):

* 심지어 정적 팩터리 메서드가 반환하는 인스턴스는 구현체가 아직 존재하지 않을 수도 있다.
* 팩터리 메서드는 프레임워크가 제공하지만, 구현체 클래스는 공급자(driver)가 제공한다.
* 서비스 인터페이스, 서비스 제공자 등록 API(for provider), 서비스 접근 API(for client), 서비스 제공자 인터페이스
* 서비스 접근 API는 "유연한 정적 팩토리 메서드"이다.

```java
Connection conn;   // 서비스 인터페이스
Driver driver = YidigunDBDriver.getDriver(); // 서비스 제공자 인터페이스
DriverManager.registerDriver(driver); // 서비스 제공자 등록 API
conn = DriverManager.getConnection("yidigun:saefolder/jickbackguri"); // 서비스 접근 API
conn.getClass();     // 실제 클래스는 YidigunDBConnection
```

#### 4. 형인자 자료형(parameterized type) 객체를 만들 때 편한다.

이 내용은 1.7 부터 생성자에서 자료형 유추를 지원하면서 의미가 없어졌다.

```java
Map<String, List<String>> m = new HashMap<String,List<String>>();  // 아우 불편해

// 이런 정적 메서드가 있다면
public static <K, V> HashMap<K, V> newInstance() {
    return new HashMap<K, V>();
}

Map<String, List<String>> m = HashMap.newInstance(); // 아우 편해

Map<String, List<String>> m = new HashMap<>(); // java 1.7 이것도 충분히 편해
```

### 정적 팩터리메서드의 단점

* `public`, `protected` 생성자가 없으므로 하위 클래스를 만들 수 없다.
    * 상속inheritance 대신 복합compsition 기법을 권장하는 의미에서는 더 좋을 수도 있다.
* 팩터리 메서드가 문법적이나 javadoc 문서 등에서 일반 메서드와 구분이 되지 않는다
    * 생성자를 쓰지 말고 팩터리 메서드를 쓰도록 설계된 객체의 사용법을 파악하기 쉽지 않다.
    * 많이 사용되는 이름
        * `valueOf()`
        * `of()`
        * `getInstance()`
        * `newInstance()`
        * `SomeType getSomeType()`
        * `SomeType newSomeType()`

### 결론

* 정적 팩터리 메서드와 `public`생성자는 용도가 서로 다름
* 의외로 팩터리 메서드가 효과적일 때가 많음음

## 규칙 2: 생성자 인자가 많을 때는 Builder 패턴 적용을 고려하라

생성에 필요한 인자 중 선택적 인자가 많은 경우 생성자나 팩토리 메서드의 모두 문제가 있다.

### 점층적 생성자 패턴

```java
public class NutritionFacts1 {
    private final int servingSize;      // 필수
    private final int servings;         // 필수
    private final int calories;         // 선택
    private final int fat;              // 선택
    private final int sodium;           // 선택
    private final int carbohydrate;     // 선택

    public NutritionFacts1(int servingSize, int servings) {
        this(servingSize, servings, 0);
    }
    public NutritionFacts1(int servingSize, int servings, int calories) {
        this(servingSize, servings, calories, 0);
    }
    public NutritionFacts1(int servingSize, int servings, int calories, int fat) {
        this(servingSize, servings, calories, fat, 0);
    }
    public NutritionFacts1(int servingSize, int servings, int calories, int fat, int sodium) {
        this(servingSize, servings, calories, fat, sodium, 0);
    }
    public NutritionFacts1(int servingSize, int servings, int calories, int fat, int sodium, int carbohydrate) {
        this.servingSize = servingSize;
        this.servings = servings;
        this.calories = calories;
        this.fat = fat;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
    }
}

NutritionFacts1 nf1 = new NutritionFacts1(240, 8, 100, 3, 25, 27);
```

* 인자 수가 늘어남에 따라 코드 작성이 번거롭고 읽기 어려운 코드가 된다.

### JavaBeans 패턴

```java
public class NutritionFacts2 {
    private int servingSize = -1;   // 필수
    private int servings = -1;      // 필수
    private int calories = 0;       // 선택
    private int fat = 0;            // 선택
    private int sodium = 0;         // 선택
    private int carbohydrate = 0;   // 선택

    public NutritionFacts2() {}

    public void setServingSize(int servingSize) { this.servingSize = servingSize; }
    public void setServings(int servings) { this.servings = servings; }
    public void setCalories(int calories) { this.calories = calories; }
    public void setFat(int fat) { this.fat = fat; }
    public void setSodium(int sodium) { this.sodium = sodium; }
    public void setCarbohydrate(int carbohydrate) { this.carbohydrate = carbohydrate; }
}

NutritionFacts2 nf2 = new NutritionFacts2();
nf2.setServingSize(240);
nf2.setServings(8);
nf2.setCalories(100);
nf2.setFat(3);
nf2.setSodium(35);
nf2.setCarbohydrate(27);
```

* 1회의 함수 호출로 생성할 수 없으므로 객체의 일관성consistency가 일시적으로 깨지는 시점이 있다.
* 변경 불가능한 객체를 만들 수 없다.

### Builder 패턴

GoF의 [Builder 패턴](https://inpa.tistory.com/entry/GOF-%F0%9F%92%A0-%EB%B9%8C%EB%8D%94Builder-%ED%8C%A8%ED%84%B4-%EB%81%9D%ED%8C%90%EC%99%95-%EC%A0%95%EB%A6%AC)은 생성자 패턴의 안전성과 자바빈 패턴의 가독성을 모두 제공하는 대안이다.

```java
public class NutritionFacts3 {
    private final int servingSize;      // 필수
    private final int servings;         // 필수
    private final int calories;         // 선택
    private final int fat;              // 선택
    private final int sodium;           // 선택
    private final int carbohydrate;     // 선택

    public static class Builder {
        private final int servingSize;  // 필수
        private final int servings;     // 필수
        private int calories = 0;       // 선택
        private int fat = 0;            // 선택
        private int sodium = 0;         // 선택
        private int carbohydrate = 0;   // 선택

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }
        public Builder calories(int calories) { this.calories = calories; return this; }
        public Builder fat(int fat) { this.fat = fat; return this; }
        public Builder sodium(int sodium) { this.sodium = sodium; return this; }
        public Builder carbohydrate(int carbohydrate) { this.carbohydrate = carbohydrate; return this; }

        public NutritionFacts3 build() {
            return new NutritionFacts3(this);
        }
    }

    private NutritionFacts3(Builder builder) {
        this.servingSize = builder.servingSize;
        this.servings = builder.servings;
        this.calories = builder.calories;
        this.fat = builder.fat;
        this.sodium = builder.sodium;
        this.carbohydrate = builder.carbohydrate;
    }
}

NutritionFacts3 nf3 = new NutritionFacts3.Builder(240, 8)
        .calories(100).fat(3).sodium(35).carbohydrate(27)
        .build();
```

* [Ada](https://www.adaic.org/resources/add_content/docs/95style/html/sec_5/5-2-2.html),
  [Python](https://int-i.github.io/python/2020-06-04/python-keyword-args/#google_vignette)의
  경우 선택적 인자에 이름을 붙일 수 있든데 비슷한 코드를 만들 수 있다.
* 생성자와 마찬가지로 [불변식(invariant)](https://banaba.tistory.com/34)을 적용할 수 있다.
    * `build()` 또는 `private` 생성자에서 에서 불변식 검사를 할 경우 실제 객체의 값을 두고 검사할 수 있다.([규칙 39](chapter07.md#규칙-39-필요하다면-방어적-복사본을-만들라))
        * 이 경우 오류 발생시 `IllegalStateException`을 던져야 한다.([규칙 60](chapter09.md#규칙-60-표준-예외를-사용하라))
            * 이렇게 던져진 예외에는 어떤 불변식 위반이 문제인지 알 수 있도록 상세 정보가 포함되어야 한다.([규칙 63](chapter09.md#규칙-63-어떤-오류인지를-드러내는-정보를-상세한-메시지에-담으라))
    * 여러 인자에 걸쳐서 불변식을 검사하려면 여러 인자를 받는 setter 메소드에를 만들면 된다.
        * 이 경우 오류 발생시 `IllegalArgumentException`을 던져야 한다.
* setter 메소드마다 따로따로 호출되므로, 여러개의 가변수 인자(varargs)를 받을수 있다.
* 유연하게 확장 가능하다.
    * 자동으로 증가하는 일련번호 등 추가적인 기능을 처리할 수 있다.
    * 설정 변수를 사용해서 생성될 객체를 바꿀 수도 있다. 이 경우 [GoF의 추상적 팩토리 패턴](https://junhkang.com/posts/61/)이 될 수 있다.
    * 제네릭을 사용하면 여러 자료형에 적용할 수도 있다.
      ```java
      public interface Builder<T> {
        public T build();
      }

      Tree buildTree(Builder<? extends Node> nodeBuilder) { ... }
      ```

### Builder 패턴의 단점

* 객체를 생성하기 위해 Builder객체를 생성해야 하므로 객체 생성 오버헤드가 증가한다.
* 생성자 보다 코드 량이 많아지므로 인자 수가 많은 경우 또는 많아질 가능성이 있는 경우등 잘 판단해야 한다.

### 결론

* 인자 수가 많은 생성자를 대체할 수 있다.
* 대부분의 인자가 선택적인 경우 적용할 수 있다.

## 규칙 3: `private` 생성자나 `enum` 자료형은 싱글턴 패턴을 따르도록 설계하라

### 싱글턴 패턴과 관련된 문제들

[싱글턴 패턴](https://inpa.tistory.com/entry/GOF-%F0%9F%92%A0-%EC%8B%B1%EA%B8%80%ED%86%A4Singleton-%ED%8C%A8%ED%84%B4-%EA%BC%BC%EA%BC%BC%ED%95%98%EA%B2%8C-%EC%95%8C%EC%95%84%EB%B3%B4%EC%9E%90)의 대표적 단점은 테스트 하기 어려워 진다는 것.

싱글턴 구현 방법1:

```java
public class Elvis1 {
    public static final Elvis1 INSTANCE = new Elvis1();
    private Elvis1() { }
    
    private String name = "Elvis Presley";
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public void leaveTheBuilding() { }
}
```

[참고] `private` 생성자를 강제로 실행시켜 객체를 만드는 방법:

```java
import java.lang.reflect.Constructor;

public class PrivateInvoker {
    public static void main(String[] args) throws Exception {
        // 리플렉션의 setAccessiable() 메소드를 통해 private로 선언된 생성자의 호출 권한을 획득한다.
        Constructor<?> con = Private.class.getDeclaredConstructors()[0];
        con.setAccessible(true);
        Private p = (Private)con.newInstance();
    }
}

public class Private {
    private Private() {
        System.out.println("Oh, no!");
    }
}
```

싱글턴 구현 방법2:
* 싱글턴 패턴의 팩토리 메소드를 통해서 성능이 향상되기는 힘든데 JVM에 의해서 거의 대부분 inline 함수로 변환된다.

```java
public class Elvis2 {
    private static final Elvis2 INSTANCE = new Elvis2();
    private Elvis2() { }
    public static Elvis2 getInstance() { return INSTANCE; }
    
    private String name = "Elvis Presley";
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public void leaveTheBuilding() { }
}

```

싱글턴 팩토리 메소드를 만들 때 장점:
* 인터페이스를 변경하지 않고 싱글턴 로직을 변경할 수 있다.(eg: 스레드별 객체 사용 등)
* 제네릭 타입을 수용하기 쉽다. ([규칙 27](chapter05.md#규칙-27-가능하면-제네릭-메서드로-만들-것))

### 싱글턴 패턴을 직렬화serialize 하려면 `Serializable`을 구현하는 것만으로는 불가능 하다.

역직렬화deserialize될 때 여러개의 객체가 생길수 있기 때문

* 모든 필드를 [`transient`](https://nesoy.github.io/blog/Java-transient) 로 선언하고
* [`readResolve()`](https://madplay.github.io/post/what-is-readresolve-method-and-writereplace-method)를 추가해야 한다.
    * 만일 역직렬화 과정에서 자동으로 호출되는 `readObject()`가 있더라도 `readResolve()`에서 반환한 인스턴스로 대체된다. 그리고 `readObject()`를 통해 자동으로 만들어진 인스턴스는 가비지 컬렉션 대상이 된다.

```java
public class Elvis3 implements Serializable {
    private static final Elvis3 INSTANCE = new Elvis3();
    private Elvis3() { }
    public static Elvis3 getInstance() { return INSTANCE; }
    
    private transient String name = "Elvis Presley";    // transient
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public void leaveTheBuilding() { }

    private Object readResolve() {
        // 동일한 Elvis 객체가 반환되도록 하는 동시에 가짜 Elvis 객체는 가비지컬렉터가 처리하도록 한다.
        return INSTANCE;
    }
}

```

### 결론: 원소가 하나뿐인 `enum` 자료형을 이용한 싱글턴 구현

```java
public enum Elvis4 {
    INSTANCE;

    private String name = "Elvis Presley";
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public void leaveTheBuilding() { }
}
```

기능적으로는 `public` 필드를 사용한 1번 방법과 동일하지만
* 더 간결하고
* 직렬화에 안전하고
* 리플렉션 공격에도 안전하다.
* 킹갓짱굳임

## 규칙 4: 객체 생성을 막을 때는 `private` 생성자를 사용하라

* `java.util.Arrays`, `java.util.Collections` 같은 정적 메소드 모음집 클래스를 만들 때는 `private` 생성자를 만들자.
    * 생성자를 만들지 않으면 기본default 생성자가 자동으로 만들어지기 때문.

```java
public class MyFunctions {
    private MyFunctions() {
        throw new AssertionError();
    }

    public static void Foo() {}
    public static void Bar() {}
}
```

## 규칙 5: 불필요한 객체는 만들지 말라

```java
String s = new Strign("stringette");    // 곤란하다
String s = "stringette";                // 바람직하다
Boolean b = new Boolean("true");        // 곤란하다 (deprecated from 9)
Boolean b = Boolean.valueOf("true");    // 바람직하다
```

### 비용이 많이 드는 객체를 재활용하는 방법

문제 코드: 비용이 많이 드는 `Calendar` 객체를 매 메소드 호출시 마다 생성한다.

```java
public class Person {
    private final Date birthDate;

    public boolean isBabyBoomer() {
        Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        gmtCal.set(1946, Calendar.JANUARY, 1, 0, 0, 0);
        Date boomStart = gmtCal.getTime();
        getCal.set(1965, Calendar.JANUARY, 1, 0, 0, 0);
        Date boomEnd = gmtCal.getTime();

        return birthDate.compareTo(boomStart) >= 0 && birthDate.compareTo(boomEnd) < 0;
    }
}
```

정적 초기화 블럭static initializer으로 개선:

```java
public class Person {
    private final Date birthDate;
    private static final BOOM_START;
    private static final BOOM_END;

    static {
        Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        gmtCal.set(1946, Calendar.JANUARY, 1, 0, 0, 0);
        BOOM_START = gmtCal.getTime();
        getCal.set(1965, Calendar.JANUARY, 1, 0, 0, 0);
        BOOM_END = gmtCal.getTime();
    }

    public boolean isBabyBoomer() {
        return birthDate.compareTo(BOOM_START) >= 0 && birthDate.compareTo(BOOM_END) < 0;
    }
}
```

`isBabyBoomer()`가 한번도 실행되지 않는 경우를 위해서
초기화 지연lazy initialization 방법([규칙 71](chapter10.md#규칙-71-초기화-지연은-신중하게-하라))을 쓸 수 있으나 **추천하지 않는다**. \
초기화를 지연시키면 구현이 복잡해지고 추가적인 성능 개선이 어렵기 때문이다.([규칙 55](chapter08.md#규칙-55-신중하게-최적화하라))

### 어댑터 패턴과 객체 생성

* 뷰View라고도 불리는 어댑터 패턴의 경우 내부 객체backing object를 다루는 것 이외의 다른 정보를 가지지 않으므로 하나 이상의 객체를 만들 필요가 없다.
    * 예를 들어 `Map` 인터페이스의 `keySet()` 메소드로 얻는 `Map`에 대한 `Set` 뷰는 여러번 호출해도 같은 객체를 리턴한다.

### 자동 객체화autoboxing과 객체 생성

Java 5 부터 기본 자료형(eg: `int`)과 객체형(eg: `Integer`)을 자동으로 변환해주는 boxing, unboxing이 지원되는데 주의해야 한다.

```java
public static void main(String[] args) {
    Long sum = 0L;
    for (long i = 0L; i < Integer.MAX_VALUE; i++ ) {
        sum += i;       // Long 객체가 2억개 생긴다.
    }
    System.out.println(sum);
}
```

* 테스트 결과: `Long`인 경우 6초 걸림, `long`인 경우 1초 걸림

### 적절한 객체의 사용은 문제가 되지 않는다.

* 객체 생성자에서 하는 일이 작고 명확하다면 객체의 생성과 반환은 신속하게 처리된다.
* 객체 풀object pool의 경우도 극단적으로 객체 생성 비용이 높지 않다면 사용하지 않는 것이 좋다.
* 객체 풀은 코드가 어려워지고, 메모리를 많이 쓰고, 성능도 떨어진다.
* 최신 JVM은 고도로 최적화된 가비지 컬렉터를 사용하므로 가벼운 객체는 풀을 사용하는 것 보다 훨씬 성능이 좋다.
* 방어적 카피([규칙 39](chapter07.md#규칙-39-필요하다면-방어적-복사본을-만들라))의 방어적 복사가 요구되는 상황에서는
  재사용을 하려고 하는 것이 더 비용이 높다는 점 주의하라.

## 규칙 6: 유효기간이 지난 객체 참조는 폐기하라

가비지컬렉터를 가지고 있는 Java 에서 메모리 누수가 발생한다고?

```java
public class Stack {
    private Object[] elements;
    private int size;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        return elements[--size];
    }

    /**
     * 적어도 하나 이상의 원소를 담을 공간을 보장한다.
     * 배열의 길이를 늘려야 할 때마다 대략 두배가 늘어난다.
     */
    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }
}
```

* `pop()`의 `elements[--size]`를 하더라도 객체 참조가 끊어지지 않기 때문에 메모리가 해제되지 않음
* 원소를 `pop()`한 후 객체 참조를 끊어줘야 한다.

```java
public Object pop() {
    if (size == 0)
        throw new EmptyStackException();
    Object e = elements[--size];
    elements[size] = null;
    return e;
}
```

* 다만 `null`처리에 너무 강박관념을 가지지는 마라, `null`처리는 규범norm이라기 보다는 예외적인 조치가 되어야 한다.
* `Stack` 클래스의 예는 `Stack` 클래스가 메모리를 자체적으로 관리하기 때문에 발생하는 문제이고 예외조치가 필요한 상황이었던 것이었다.

### 자체적으로 메모리 관리하는 클래스 주의 사항

* 자체적으로 관리하는 메모리가 있는 클래스를 만들 때는 메모리 누수가 발생하지 않도록 주의해야한다.
* 캐시cache도 메모리 누수가 발생할 수 있다.
    * [`WeakHashMap`](https://bepoz-study-diary.tistory.com/340)을 이용해서 캐시를 구현하면, 키에 대한 참조가 만료되는 순간 값도 참조가 해제된다.
    * 캐시 정리를 위한 백그라운드 쓰레드를 이용하는 경우([`Timer`](https://blog.naver.com/highkrs/220283709171) 
      또는 [`ScheduledThreadPoolExecutor`](https://junuuu.tistory.com/1017))
      [`LinkedHashMap`](https://hbase.tistory.com/136) 을 사용하면
      [`removeEldestEntry()`](https://codingdog.tistory.com/entry/java-linkedhashmap-removeeldestentry-%EB%A9%94%EC%86%8C%EB%93%9C%EC%97%90-%EB%8C%80%ED%95%B4-%EC%95%8C%EC%95%84%EB%B4%85%EC%8B%9C%EB%8B%A4)를 사용할 수 있어서 좋다.
* 리스너listener와 callback을 등록하는 패턴의 경우도 메모리 누수가 쉽게 발생할 수 있다.
    * 가비지 컬렉터가 즉시 처리하도록 하기위한 가장 좋은 방법은 리스너 참조를 `WeakHashMap`의 키로 저장하는 방법이다.

### 결론

* 메모리 누수는 보통 별 증상이 없기 때문에 테스트 과정에서 쉽게 발견되지 않고 수년간 시스템에 남아 있을 수 있다.
* 이러한 문제가 발생할 수 있다는 것을 인지하고 미리 방지 대책을 세워야 한다.

## 규칙 7: 종료자(finalizer) 사용을 피하라

[추가] 수많은 문제를 가지고 있던 `finalize()`는 결국 Java 9부터 deprecated 되었다고 한다.
[Java finalize() 은퇴식](https://jaeyeong951.medium.com/finalize-%EC%9D%80%ED%87%B4%EC%8B%9D-4a52fb855910)

### 선 결론: 종료자(finalizer)는 99.99% 쓸데 없다

* 예측 불가능하며 (언제 실행될지 알 수 없어)
* 대체로 위험하고 (심지어 제대로 실행되지 않을 수도 있어)
* 일반적으로 필요 없다. (가비지 컬렉터가 있으므로)

### 종료자(finalizer)의 쓰레기 같은 특징

* Java는 C++과는 달리 가비지컬렉터가 메모리 해제를 해주므로 보통 필요 없다.
* 그러나 언제 해제된다는 보장이 없으므로 빨리 해제되어야 할 자원은 명시적으로 해제시키야 한다. (DB Connection, Lock 등등)
* finalizer가 있는 경우 오히려 해제가 지연되는 경우가 있다. finalizer를 실행하는 쓰레드가 우선순위가 낮은 경우 finalize가 계속 밀린다.
* Java Spec에는 finalizer가 언제 실행되어야 한다는 이야기도 없을 뿐만 아니라, 심지어 반드시 실행되어야 한다는 말도 없다.
* `System.gc()`, `System.runFinalization()` 도 가능성을 높여줄 뿐 실행을 보장하지는 않는다.
    * `System.runFinalizersOnExit()`, `Runtime.runFinalizersOnExit()`은 보장하긴 했었다. 지금은 deprecated.
* 실행이 되더라도 끝까지 실행된다는 보장이 없다.
    * 중간에 예외가 발생하면 아무런 경고나 오류메시지 없이 실행이 중단된다.
* finalizer를 사용하면 프로그램 성능이 심각하게 떨어질 수 있다.

### 명시적인 종료 메소드를 만들자

`close()`, `release()`, `dispose()`, `flush()` 따위를 만들고  `try`-`finally`와 함께 사용하도록 한다.

```java
public void bar() {
    Handle h = new Handle();

    try {
        h.doSomething();
    }
    finally {
        h.close();
    }
}
```

* 명시적인 종료메소드를 만들고 안전장치로 finalizer에서 호출하도록 할 수는 있다.
    * 이 경우 명시적올 호출되지 않은 종료메소드를 finalizer가 로그를 남기도록 해서 버그를 잡는 것이 좋다.

### 네이티브 피어native peer를 사용하는 경우는 유의미함

네이티브 피어란 네이티브 메소드를 통해 기능을 수행하는 네이티브 객체를 의미한다.

네이티브 피어는 일반적인 객체가 아니므로 JVM이 상황을 알 수 없으므로 중요한 자원을 사용하지 않는다면 finalizer를 사용하는 것도 좋다.

### `finalize()`와 상속

finalizer를 가진 클래스의 하위클래스에서 finalizer를 재정의하면 상위 클래스의 해제 동작을 수행하지 않는다.
반드시 `super.finalize()`를 명시적으로 호출해 줘야 한다.

```java
@Override
protected void finalize() throws Throwable {
    try {
        // do something to finalize
    }
    finally {
        super.finalize();
    }
}
```

더 좋은 방법은 모든 클래스에 부모와 상관없이 자기의 뒷처리를 하는 익명 클래스 객체를 만들면 좋다.

```java
public class Foo {
    private final Object finalizeGuardian = new Object() {
        @Override
        protected void finalize() thorws Throwable {
            // finalize Foo
        }
    }
}

public class Bar extends Foo {
    private final Object finalizeGuardian = new Object() {
        @Override
        protected void finalize() thorws Throwable {
            // finalize Bar
        }
    }
}
```

### 최종 결론

* 자원 해제 등 후처리를 위한 명시적인 종료 메소드를 반드시 만들고
* 종료 메소드 없이 종료되는 경우에 대한 로그를 찍도록 하라
* 굳이 `finalize()`를 사용해야 하는 경우라면 `super.finalize()`를 잊지 말자.

### [추가] Java 7 `try-with-resources` 문

`AutoClosable` 인터페이스를 구현한 객체에 대해서 [`try-with-resources`](https://mangkyu.tistory.com/217) 문을 지원하게 되었다.

```java
public class Handle implements AutoCloseable {
    @Override
    public void close() {
        // close Foo
    }
}

public void bar() {
    try (Handle h = new Handle()) {
        h.doSomething();
    }
}
```

```java
public static void main(String args[]) throws IOException {
    try (FileInputStream is = new FileInputStream("file.txt");
         BufferedInputStream bis = new BufferedInputStream(is)) {
        int data;
        while ((data = bis.read()) != -1) {
            System.out.print((char) data);
        }
    }
}
```
