# 이펙티브 자바 (3판) - 2장 객체의 생성과 삭제

## 목차

* [**아이템 1**: 생성자 대신 정적 펙터리 메서드를 고려하라](#아이템-1-생성자-대신-정적-펙터리-메서드를-고려하라)
* [**아이템 2**: 생성자에 매개변수가 많다면 빌더를 고려하라](#아이템-2-생성자에-매개변수가-많다면-빌더를-고려하라)
* [**아이템 3**: `private` 생성자나 열거 타입으로 싱글턴임을 보증하라](#아이템-3-private-생성자나-열거-타입으로-싱글턴임을-보증하라)
* [**아이템 4**: 인스턴스화를 막으려거든 `private` 생성자를 사용하라](#아이템-4-인스턴스화를-막으려거든-private-생성자를-사용하라)
* [**아이템 5**: 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라](#아이템-5-자원을-직접-명시하지-말고-의존-객체-주입을-사용하라)
* [**아이템 6**: 불필요한 객체 생성을 피하라](#아이템-6-불필요한-객체-생성을-피하라)
* [**아이템 7**: 다 쓴 객체 참조를 해제하라](#아이템-7-다-쓴-객체-참조를-해제하라)
* [**아이템 8**: finalizer와 cleaner 사용을 피하라](#아이템-8-finalizer와-cleaner-사용을-피하라)
* [**아이템 9**: try-finally보다는 try-with-resources를 사용하라](#아이템-9-try-finally-보다는-try-with-resources를-사용하라)

## 아이템 1: 생성자 대신 정적 펙터리 메서드를 고려하라

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

#### 3. 생성자와 달리 하위 클래스를 리턴할 수 있다.`

`public`으로 선언되지 않은 하위 클래스를 리턴 가능하다.

인터페이스 기반 프레임워크 interface-based framework ([아이템 20](chapter04.md#아이템-20-추상-클래스보다는-인터페이스를-우선하라)):

* 2판의 내용 "인터페이스는 메서드를 가질 수 없으므로 전용 클래스를 만든다.(`SomeType SomeTypes.getSomeType()`)"는
  자바 8부터 [인터페이스에 메서드 추가가 가능](https://blog.naver.com/amas1004/222287203050)하기 때문에 의미가 퇴색되었다.
  * Java 9부터는 `private` 메소드도 만들 수 있게 되었으나, `static` 메소드는 `public`이어야만 한다.
  * 그렇지만 초기화 등 위해 `private` 또는 package-private 클래스가 필요할 수 있다.
* 인터페이스 기반의 장점은 API의 규모가 줄어드는 것이 아니라 개념상의 무게감 conceptual weight이 줄어든 것이다.
    * Java Collection API 에 32개의 비 `public` 구현 클래스들이 있지만 `java.util.Collection`을 구현하므로 따로 문서화할 필요가 없다.

#### 4. 입력 매개변수에 따라 다른 클래스를 리턴할 수 있다.

* 라이브러리 버전이 올라감에 따라서 다른 구현체를 리턴할 수도 있으므로 유지보수성이 높아진다.
* `java.util.EnumSet`([아이템 36](chapter06.md#아이템-36-비트-필드-대신-enumset을-사용하라))은 `public` 생성자가 없고 정적 팩토리 메서드 뿐이다.
    * `enum` 상수의 개수가 64개 이하인 경우 내부적으로 `long` 변수의 비트를 활용하는 버전(`RegularEnumSet`)을 리턴
    * 64개 초과인 경우 `long` 형 배열을 사용하는 `JumboEnumSet` 을 리턴한다.
    * 다음 릴리즈에서 `JumboEnumSet`이 성능이 만족하지 않는다면 `MegaEnumSet`이라던가 `GigaEnumSet`으로 변경될 수 있다.

#### 5. 정적 팩터리 메서드를 작성하는 시점에 반환할 객체가 존재하지 않을 수도 있다. 

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

#### 2판 내용: 형인자 자료형(parameterized type) 객체를 만들 때 편한다.

이 내용은 Java 7 부터 생성자에서 자료형 유추를 지원하면서 의미가 없어졌다.

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
*  찾기가 어렵다 - 팩터리 메서드가 문법적이나 javadoc 문서 등에서 일반 메서드와 구분이 되지 않는다
    * 생성자를 쓰지 말고 팩터리 메서드를 쓰도록 설계된 객체의 사용법을 파악하기 쉽지 않다.
    * 많이 사용되는 이름
        * `Date d = Date.from(instant);`
        * `Set<Rank> faceCards = EnumSet.of(JACK, QUEEN, KING);`
        * `BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE);`
        * `StackWalker luke = StackWalker.getInstance(options);`
        * `Object newArray = Array.newInstance(classObject, arrayLen);`
        * `FileStore fs = Files.getFileStore(path);`
        * `BufferedReader br = Files.newBufferedReader(path);`
        * `List<Complaint> litany = Collections.list(legacyLitany);`

### 결론

* 정적 팩터리 메서드와 `public`생성자는 용도가 서로 다름
* 의외로 팩터리 메서드가 효과적일 때가 많음음

## 아이템 2: 생성자에 매개변수가 많다면 빌더를 고려하라

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
    * `build()` 또는 `private` 생성자에서 에서 불변식 검사를 할 경우 실제 객체의 값을 두고 검사할 수 있다.([아이템 50](chapter08.md#아이템-50-적시에-방어적-복사본을-만들라))
        * 이 경우 오류 발생시 `IllegalStateException`을 던져야 한다.([아이템 72](chapter10.md#아이템-72-표준-예외를-사용하라))
            * 이렇게 던져진 예외에는 어떤 불변식 위반이 문제인지 알 수 있도록 상세 정보가 포함되어야 한다.([아이템 75](chapter10.md#아이템-75-예외의-상세-메시지에-실패-관련-정보를-담으라))
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
* 계층적으로 설계된 클래스 상속 트리와 잘 어울린다.
  * 추상 클래스는 추상 빌더를, 구체 클러스는 구체 빌더를 갖게 한다.

### Builder 패턴의 단점

* 객체를 생성하기 위해 Builder객체를 생성해야 하므로 객체 생성 오버헤드가 증가한다.
* 생성자 보다 코드 량이 많아지므로 인자 수가 최소 4개 이상인 경우 효율적임
* 또는 많아질 가능성이 있는 경우등 잘 판단해야 한다. 보통 API는 확장되어 인자 수가 늘어나기 마련

### 결론

* 인자 수가 많은 생성자를 대체할 수 있다.
* 대부분의 인자가 선택적인 경우 적용할 수 있다.

## 아이템 3: `private` 생성자나 열거 타입으로 싱글턴임을 보증하라

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
* 제네릭 타입을 수용하기 쉽다. ([아이템 30](chapter05.md#아이템-30-이왕이면-제네릭-메서드로-만들라))

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

### 결론: 원소가 하나뿐인 열거타입 이용한 싱글턴 구현

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

## 아이템 4: 인스턴스화를 막으려거든 `private` 생성자를 사용하라

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

## 아이템 5: 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라

특정 자원에 의존하는 기능을 구현하는 경우 정적 유틸리티 클래스나 싱글턴은 확장성이 떨어지고, 테스트하기 어렵기 때문에 적절하지 않다. 

인스턴스를 생성할 때 필요한 자원을 전달하는 것이 바람직하다.

```java
import java.util.Objects;

public class SpellChecker {
    private final Lexicon dictionary;

    public SpellChecker(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }
    public booelan isValid(String word) { }
    public List<String> suggestions(String typo) { }
}
```

### 장점: 의존객체 주입 패턴은 활용도가 매우 좋다.

* 불변 객체를 사용하여 여러 객체가 공유해서 사용할 수 있다 ([아이템 17](chapter04.md#아이템-17-변경-가능성을-최소화하라))
* 생성자, 정적 팩터리([아이템 1](chapter02.md#아이템-1-생성자-대신-정적-펙터리-메서드를-고려하라)), 빌더([아이템 2](chapter02.md#아이템-2-생성자에-매개변수가-많다면-빌더를-고려하라)) 모두 사용 가능

의존 객체를 직접 넘겨주는 것이 아니라 팩터리 객체를 넘겨줄 수 있다.
* GoF의 [팩터리 메서드 패턴](https://inpa.tistory.com/entry/GOF-%F0%9F%92%A0-%ED%8C%A9%ED%86%A0%EB%A6%AC-%EB%A9%94%EC%84%9C%EB%93%9CFactory-Method-%ED%8C%A8%ED%84%B4-%EC%A0%9C%EB%8C%80%EB%A1%9C-%EB%B0%B0%EC%9B%8C%EB%B3%B4%EC%9E%90)
* [`Supplier<T>`](https://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html)가 완벽한 예시
  * 한정적 와일드카드 타입(bounded wildcard type, [아이템 31](chapter05.md#아이템-31-한정적-와일드카드를-사용해-api-유연성을-높이라))을 사용해야 한다.

### 단점: 너무 많은 클래스를 만들 수 있다.

* [Dagger](https://seosh817.tistory.com/74), [Guice](https://tobee.tistory.com/entry/Guice-Google), [Spring](https://namu.wiki/w/%EB%8D%94%20%EC%9D%B4%EC%83%81%EC%9D%98%20%EC%9E%90%EC%84%B8%ED%95%9C%20%EC%84%A4%EB%AA%85%EC%9D%80%20%EC%83%9D%EB%9E%B5%ED%95%9C%EB%8B%A4.) 등 프레임워크와 같이 쓰자

## 아이템 6: 불필요한 객체 생성을 피하라

```java
String s = new Strign("stringette");    // 곤란하다
String s = "stringette";                // 바람직하다
Boolean b = new Boolean("true");        // 곤란하다 (deprecated from 9)
Boolean b = Boolean.valueOf("true");    // 바람직하다
```

[잡담] 2판의 예제에 [stringette](https://www.youtube.com/watch?v=2xDuBONyNP0) 란 단어가 나오는데 무슨 뜻인지 모르겠다. 구글번역도 번역을 못하는 단어이다. 

### 비용이 많이 드는 객체를 재활용하는 방법 (3판)

문제 코드 `Pattern`객체가 매번 만들어졌다가 버려진다.

```java
static boolean isRomanNumeral(String s) {
    return s.matches("^(?=.)M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
}
```

다음과 같이하면 성능도 좋아질 뿐만 아니라 코드의 명확성도 좋아짐

```java
import java.util.regex.Pattern;

class RomanNumerials {
    private static final Pattern ROMAN =
        Pattern.compile("^(?=.)M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
    
    static boolean isRomanNumeral(String s) {
        return ROMAN.matcher(s).matches();
    }
}
```

초기화 지연lazy initialization 방법([아이템 83](chapter11.md#아이템-83-지연-초기화는-신중히-사용하라))을 쓸 수 있으나 **추천하지 않는다**. \
초기화를 지연시키면 구현이 복잡해지고 추가적인 성능 개선이 어렵기 때문이다.([아이템 67](chapter09.md#아이템-67-최적화는-신중히-하라))

### 비용이 많이 드는 객체를 재활용하는 방법 (2판)

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

### 어댑터 패턴과 객체 생성

* 뷰View라고도 불리는 [어댑터 패턴](https://inpa.tistory.com/entry/GOF-%F0%9F%92%A0-%EC%96%B4%EB%8C%91%ED%84%B0Adaptor-%ED%8C%A8%ED%84%B4-%EC%A0%9C%EB%8C%80%EB%A1%9C-%EB%B0%B0%EC%9B%8C%EB%B3%B4%EC%9E%90)의 경우 내부 객체backing object를 다루는 것 이외의 다른 정보를 가지지 않으므로 하나 이상의 객체를 만들 필요가 없다.
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
* 방어적 카피([아이템 50](chapter08.md#아이템-50-적시에-방어적-복사본을-만들라))의 방어적 복사가 요구되는 상황에서는
  재사용을 하려고 하는 것이 더 비용이 높다는 점 주의하라.

## 아이템 7: 다 쓴 객체 참조를 해제하라

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

## 아이템 8: finalizer와 cleaner 사용을 피하라

수많은 문제를 가지고 있던 `finalize()`는 결국 Java 9에서 deprecated 되고,
[cleaner](https://velog.io/@hope0206/Java-Cleaner-PhantomReference-%ED%81%B4%EB%9E%98%EC%8A%A4-%EC%99%9C-%ED%83%9C%EC%96%B4%EB%82%AC%EB%8A%94%EC%A7%80-%EB%AC%B4%EC%97%87%EC%9D%B8%EC%A7%80-%EC%84%A4%EB%AA%85%ED%95%B4%EC%A3%BC%EC%84%B8%EC%9A%94)가 새로 나왔다.

### 선 결론: finalizer와 cleaner는 99.99% 쓸데 없다

* 예측 불가능하며 (언제 실행될지 알 수 없어)
* 대체로 위험하고 (심지어 제대로 실행되지 않을 수도 있어)
* 일반적으로 필요 없다. (가비지 컬렉터가 있으므로)
* cleaner는 finalizer보나는 덜 위험하지만 여전히 예측할 수 없고, 느리고, 불필요하다.

### finalizer/cleaner의 쓰레기 같은 특징

* Java는 C++과는 달리 가비지컬렉터가 메모리 해제를 해주므로 보통 필요 없다.
* 그러나 언제 해제된다는 보장이 없으므로 빨리 해제되어야 할 자원은 명시적으로 해제시키야 한다. (DB Connection, Lock 등등)
* finalizer/cleaner가 있는 경우 오히려 해제가 지연되는 경우가 있다. finalizer/cleaner를 실행하는 쓰레드가 우선순위가 낮은 경우 청소 작업이 계속 밀린다.
* Spec에는 finalizer/cleaner가 언제 실행되어야 한다는 이야기도 없을 뿐만 아니라, 심지어 반드시 실행되어야 한다는 말도 없다.
* `System.gc()`, `System.runFinalization()` 도 가능성을 높여줄 뿐 실행을 보장하지는 않는다.
    * `System.runFinalizersOnExit()`, `Runtime.runFinalizersOnExit()`은 보장하긴 했었다. 지금은 deprecated.
* finalizer는 실행이 되더라도 끝까지 실행된다는 보장이 없다.
    * 중간에 예외가 발생하면 아무런 경고나 오류메시지 없이 실행이 중단된다.
    * 단, cleaner의 경우는 적어도 예외에 의해서 중단되지는 않는다.
* finalizer/cleaner를 사용하면 프로그램 성능이 심각하게 떨어질 수 있다.

### finalizer 공격

생성자나 역직렬화(`readObject()`, `readResolve()`) 과정에서 예외가 발생하면 생성되다만 객체에서 악의적인 하위클래스의 finalizer가 실행될 수 있다고?

[[item 8] finalizer 공격의 예시](https://github.com/ehBeak/Effective-Java-Study/issues/16)

> 어떤 악의적인 A클래스가 B클래스를 상속하면, A클래스는 B(부모)클래스의 finalizer메소드를 오버라이딩 할 수 있다.
그리고 이 A클래스가 생성될 때 예외가 발생하면, finalizer에 의해서 예외가 터지지 않고 결국 A객체는 좀비 클래스가 된다.
이렇게 되면 A객체는 오버라이딩한 finalizer메소드를 통해 B클래스에 마음대로 접근이 가능하게 된다.
이를 막기 위해서는 B클래스를 상속받지 못하도록 final로 선언을 하거나 B클래스에서 finalizer메소드를 final선언하여 오버라이딩 못하게 해야한다.

### 명시적인 종료 메소드를 만들자

`AutoClosable` 인터페이스에는 `close()`이 있고 이를 구현하면 된다.

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

### cleaner가 유의미한 경우

* 명시적인 종료메소드를 만들고 안전장치로 finalizer/cleaner에서 호출하도록 할 수는 있다.
  * 이 경우 명시적올 호출되지 않은 종료메소드를 finalizer가 로그를 남기도록 해서 버그를 잡는 것이 좋다.
* 네이티브 피어native peer를 사용하는 경우는 유의미함 \
  (네이티브 피어란 네이티브 메소드를 통해 기능을 수행하는 네이티브 객체를 의미한다.)
  * 네이티브 피어는 일반적인 객체가 아니므로 JVM이 상황을 알 수 없으므로 중요한 자원을 사용하지 않는다면 finalizer를 사용하는 것도 좋다.

#### `AutoCloseable`과 cleaner 조합 예제

```java
import java.lang.ref.Cleaner;

public class Room implements AutoCloseable {
    private static final Cleaner cleaner = Cleaner.create();
    
    private static class State implements Runnable {
        int numJunkPiles; // 방 안의 쓰레기 수
        
        State(int numJunkPiles) {
            this.numJunkPiles = numJunkPiles;
        }
        
        @Override
        public void run() {
            System.out.println("방 청소");
            numJunkPiles = 0;
        }
    }
    
    private final State state;
    
    private final Cleaner.Cleanable cleanable;
    
    public Room(int numJunkPiles) {
        state = new State(numJunkPiles);
        cleanable = cleaner.register(this, state);
    }
    
    @Override
    public void close() {
        cleanable.clean();
    }
}
```

* `State` 는 정리해야할 자원을 담고 있는데, 절대 `Room`의 참조를 가지고 있으면 안된다. \
  그러면 가비지 컬렉팅이 동작하지 않고 cleaner는 호출되지 않는다.
* cleaner가 호출된다는 보장은 없다. try-with-resources 문으로 `close()`가 명시적으로 실행되도록 하는 것이 최선이다.
  > The behavior of cleaners during System.exit is implementation specific. No guarantees are made relating to whether cleaning actions are invoked or not. \
  > `System.exit`을 호출할 때 cleaner의 동작은 구현에 따라 다를 수 있다. 청소 동작의 실행이 보장되지 않는다.
  > https://docs.oracle.com/javase/9/docs/api/java/lang/ref/Cleaner.html

### `finalize()`와 상속 (2판)

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
* 종료 메소드 없이 종료되는 경우에 대한 안전장치로만 사용하라, 그리고 로그를 찍도록 하라 (그래야 찾아서 고치지)

## 아이템 9: try-finally 보다는 try-with-resources를 사용하라

나쁜 코드:

```java
import java.io.FileInputStream;
import java.io.FileOutputStream;

static void copy(String src, String dest) throws IOException {
    InputStream is = new FileInputStream(src);
    try {
        OutputStream os = new FileOutputStream(dest);
        try {
            byte[] buf = new byte[BUFFERSIZE];
            int n;
            while ((n = is.read(buf)) >= 0) {
                os.write(buf, 0, n);
            }
        }
        finally {
            os.close();
        }
    }
    finally {
        is.close();
    }
}
```

* 중첩된 `try` 블럭에 의해서 예외가 다른 예외를 씹어버릴 수 있다.

좋은 코드:

```java
import java.io.FileInputStream;

static void copy(String src, String dest) throws IOException {
    try (InputStream is = new FileInputStream(src);
            OutputStream os = new FileOutputStrem(dest)) {
        byte[] buf = new byte[BUFFERSIZE];
        int n;
        while ((n = is.read(buf)) >= 0) {
            os.write(buf, 0, n);
        }
    }
}
```

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
