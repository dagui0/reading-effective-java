# 이펙티브 자바 (2판) - 2장 객체의 생성과 삭제

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
new BigInteger(int, int, Random);  // 생성자의 기능을 파악하기 어려움
BigInteger BigInteger.probablePrime(int, int, Random); // 생성자의 기능을 유추 가능
```

생성자는 같은 시그니처를 가지는 생성자를 여러개 만들 수 없다. 그렇게 하려면 인자의 순서를 바꿔야 하는데 아마도 이해할 수 없는 코드가 될것이다.
```java
new BigInteger(int, int, Random);     // 쌍
new BigInteger(int, Random, int);     // 욕
new BigInteger(Random, int, int);     // 나옴
```

#### 2. 생성자와 달리 새로운 객체를 만들 필요가 없다.

새로운 객체를 생성하지 않고 만들어진 객체 캐시을 유지할 수도 있다. 이렇게 하면 [Flyweight 패턴](https://inpa.tistory.com/entry/GOF-%F0%9F%92%A0-Flyweight-%ED%8C%A8%ED%84%B4-%EC%A0%9C%EB%8C%80%EB%A1%9C-%EB%B0%B0%EC%9B%8C%EB%B3%B4%EC%9E%90)과 유사하다.

개체 통제 클래스instance-controlled class:

* 특정 시점에 인스턴스가 얼마나 만들어질 지 통제할 수 있다
* 생성이 불가능한 클래스(eg: 싱글턴)을 만들면 `equals()`가 `==` 연산자로 비교 가능하여 성능이 향상된다.

#### 3. 생성자와 달리 하위 클래스를 리턴할 수 있다.

`public`으로 선언되지 않은 하위 클래스를 리턴 가능하다.

인터페이스 기반 프레임워크 interface-based framework ([규칙 18](chapter04.md)):

* 인터페이스는 메서드를 가질 수 없으므로 전용 클래스를 만든다. (`SomeType SomeTypes.getSomeType()`)
  > 이거 자바 8부터 [인터페이스에 메서드 추가가 가능](https://blog.naver.com/amas1004/222287203050)한데 `static`도 가능한가?
* 인터페이스 기반의 장점은 API의 규모가 줄어드는 것이 아니라 개념상의 무게감 conceptual weight이 줄어든 것이다.
    * Java Collection API 에 32개의 비 `public` 구현 클래스들이 있지만 `java.util.Collection`을 구현하므로 따로 문서화할 필요가 없다.
* 버전에 따라서 다른 구현체를 리턴할 수도 있으므로 유지보수성이 높아진다.
* `java.util.EnumSet`([규칙 32](chapter06.md))은 `public` 생성자가 없고 정적 팩토리 메서드 뿐이다.
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
private class NutritionFacts {
    private final int servingSize;      // 필수
    private final int servings;         // 필수
    private final int calories;         // 선택
    private final int fat;              // 선택
    private final int soduim;           // 선택
    private final int carbohydrate;     // 선택

    public NutritionFacts(int servingSize, int servings) {
        this(servingSize, servings, 0);
    }
    public NutritionFacts(int servingSize, int servings, int calories) {
        this(servingSize, servings, calories, 0);
    }
    public NutritionFacts(int servingSize, int servings, int calories, int fat) {
        this(servingSize, servings, calories, fat, 0);
    }
    public NutritionFacts(int servingSize, int servings, int calories, int fat, int soduim) {
        this(servingSize, servings, calories, fat, soduim, 0);
    }
    public NutritionFacts(int servingSize, int servings, int calories, int fat, int soduim, int carbohydrate) {
        this.servingSize = servingSize;
        this.servings = servings;
        this.calories = calories;
        this.fat = fat;
        this.soduim = soduim;
        this.carbohydrate = carbohydrate;
    }
}

NutritionFacts nf = new NutritionFacts(240, 8, 100, 3, 25, 27);
```

* 인자 수가 늘어남에 따라 코드 작성이 번거롭고 읽기 어려운 코드가 된다.

### JavaBeans 패턴

```java
private class NutritionFacts {
    private int servingSize = -1;   // 필수
    private int servings = -1;      // 필수
    private int calories = 0;       // 선택
    private int fat = 0;            // 선택
    private int soduim = 0;         // 선택
    private int carbohydrate = 0;   // 선택

    public NutritionFacts() {}

    public setServingSzie(int servingSize) { this.servingSize = servingSize; }
    public setServings(int servings) { this.servings = servings; }
    public setCalories(int calories) { this.calories = calories; }
    public setFat(int fat) { this.fat = fat; }
    public setSodium(int sodium) { this.sodium = sodium; }
    public setCarbohydrate(int carbohydrate) { this.carbohydrate = carbohydrate; }
}

NutritionFacts nf = new NutritionFacts();
nf.setServingSzie(240);
nf.setServings(8);
nf.setCalories(100);
nf.setFat(3);
nf.setSodium(35);
nf.setCarbohydrate(27);
```

* 1회의 함수 호출로 생성할 수 없으므로 객체의 일관성consistency가 일시적으로 깨지는 시점이 있다.
* 변경 불가능한 객체를 만들 수 없다.

### Builder 패턴

GoF의 [Builder 패턴](https://inpa.tistory.com/entry/GOF-%F0%9F%92%A0-%EB%B9%8C%EB%8D%94Builder-%ED%8C%A8%ED%84%B4-%EB%81%9D%ED%8C%90%EC%99%95-%EC%A0%95%EB%A6%AC)은 생성자 패턴의 안전성과 자바빈 패턴의 가독성을 모두 제공하는 대안이다.

```java
private class NutritionFacts {
    private final int servingSize;      // 필수
    private final int servings;         // 필수
    private final int calories;         // 선택
    private final int fat;              // 선택
    private final int soduim;           // 선택
    private final int carbohydrate;     // 선택

    public static class Builder {
        private final int servingSize;  // 필수
        private final int servings;     // 필수
        private int calories = 0;       // 선택
        private int fat = 0;            // 선택
        private int soduim = 0;         // 선택
        private int carbohydrate = 0;   // 선택

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }
        public Builder calories(int calories) { this.calories = calories; return this; }
        public Builder fat(int fat) { this.fat = fat; return this; }
        public Builder soduim(int soduim) { this.soduim = soduim; return this; }
        public Builder carbohydrate(int carbohydrate) { this.carbohydrate = carbohydrate; return this; }

        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }

    private NutritionFacts(Builder builder) {
        this.servingSize = builder.servingSize;
        this.servings = builder.servings;
        this.calories = builder.calories;
        this.fat = builder.fat;
        this.soduim = builder.soduim;
        this.carbohydrate = builder.carbohydrate;
    }
}

NutritionFacts nf = new NutritionFacts.Builder(240, 8)
                        .calories(100).fat(3).sodium(35).carbohydrate(27)
                        .build();
```

* [Ada](https://www.adaic.org/resources/add_content/docs/95style/html/sec_5/5-2-2.html),
  [Python](https://int-i.github.io/python/2020-06-04/python-keyword-args/#google_vignette)의
  경우 선택적 인자에 이름을 붙일 수 있든데 비슷한 코드를 만들 수 있다.
* 생성자와 마찬가지로 [불변식(invariant)](https://banaba.tistory.com/34)을 적용할 수 있다.
    * `build()` 또는 `private` 생성자에서 에서 불변식 검사를 할 경우 실제 객체의 값을 두고 검사할 수 있다.([규칙 39](chapter07.md))
        * 이 경우 오류 발생시 `IllegalStateException`을 던져야 한다.([규칙 60](chapter09.md))
            * 이렇게 던져진 예외에는 어떤 불변식 위반이 문제인지 알 수 있도록 상세 정보가 포함되어야 한다.([규칙 63](chapter09.md))
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


## 규칙 4: 객체 생성을 막을 때는 `private` 생성자를 사용하라


## 규칙 5: 불필요한 객체는 만들지 말라


## 규칙 6: 유효기간이 지난 객체 참조는 폐기하라


## 규칙 7: 종료자 사용을 피하라


