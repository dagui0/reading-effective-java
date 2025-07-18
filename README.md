# 이펙티브 자바 (3판)

## [저자 소개](docs/author.md)

## 1장 들어가기

> 자바는 아주 단순한 노동자용 언어
> - James Gosling

> 갓 탄생한 언어 대부분이 그렇듯, 자바가 상대적으로 간결해 보이는 이유는 어느 정도는 허상이고, 어느 정도는 그 기능의 미숙함 때문이다.
> 자바도 시간이 흐르면서 급격히 커지고 복잡해질 것이다. 결국 두세 배는 커지고 종속적인 확장과 라이브러리가 느러날 것이다.
> - Bjarne Stroustrup

* 이 책은 순서대로 읽는 것을 기준으로 만들어지지 않았으며 각 90개의 절은 독립적으로 읽을 수 있다.
* 성능 문제는 다루지 않는다. 명료하고 정확하고 사용이 편리하고 안정적이고 유연하고 유지보수가 쉬운 프로그램을 만드는 방법을 알면 필요한 수준의 성능을 내는 것은 상대적으로 쉽다.
* 접근 권한을 명시하지 않았을때 의미가 분명한 package-private라는 용어를 쓰지만 정확한 용어는 pacakge access (default access 2판) 이다.

* 버전별 추가된 기능

| 기능                 | 아이템 번호   | 도입된 자바 버전 |
|--------------------|----------|-----------|
| 람다                 | 42 ~ 44  | 자바 8      |
| 스트림                | 45 ~ 48  | 자바 8      |
| 옵셔널                | 55       | 자바 8      |
| 인터페이스의 디폴트 메소드     | 21       | 자바 8      |
| try-with-resources | 9        | 자바 7      |
| @SafeVarargs       | 32       | 자바 7      |
| 모듈                 | 15       | 자바 9      |


* "[추가]"라고 표시된 내용은 책에 없는 내용이다.

### 참고 링크

* [이펙티브 자바 3판 번역 용어 해설](https://docs.google.com/document/d/1Nw-_FJKre9x7Uy6DZ0NuAFyYUCjBPCpINxqrP0JFuXk/edit?tab=t.0)
* [이펙티브 자바 3판 한국어판 예제 소스](https://github.com/WegraLee/effective-java-3e-source-code)
* [백기선 - 이펙티브 자바 강의](https://www.youtube.com/watch?v=X7RXP6EI-5E&list=PLfI752FpVCS8e5ACdi5dpwLdlVkn0QgJJ)

## 목차

* [2장 객체의 생성과 삭제](docs/chapter02.md)
  * [**아이템 1**: 생성자 대신 정적 펙터리 메서드를 고려하라](docs/chapter02.md#아이템-1-생성자-대신-정적-펙터리-메서드를-고려하라)
  * [**아이템 2**: 생성자에 매개변수가 많다면 빌더를 고려하라](docs/chapter02.md#아이템-2-생성자에-매개변수가-많다면-빌더를-고려하라)
  * [**아이템 3**: `private` 생성자나 열거 타입으로 싱글턴임을 보증하라](docs/chapter02.md#아이템-3-private-생성자나-열거-타입으로-싱글턴임을-보증하라)
  * [**아이템 4**: 인스턴스화를 막으려거든 `private` 생성자를 사용하라](docs/chapter02.md#아이템-4-인스턴스화를-막으려거든-private-생성자를-사용하라)
  * [**아이템 5**: 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라](docs/chapter02.md#아이템-5-자원을-직접-명시하지-말고-의존-객체-주입을-사용하라)
  * [**아이템 6**: 불필요한 객체 생성을 피하라](docs/chapter02.md#아이템-6-불필요한-객체-생성을-피하라)
  * [**아이템 7**: 다 쓴 객체 참조를 해제하라](docs/chapter02.md#아이템-7-다-쓴-객체-참조를-해제하라)
  * [**아이템 8**: finalizer와 cleaner 사용을 피하라](docs/chapter02.md#아이템-8-finalizer와-cleaner-사용을-피하라)
  * [**아이템 9**: try-finally보다는 try-with-resources를 사용하라](docs/chapter02.md#아이템-9-try-finally-보다는-try-with-resources를-사용하라)
* [3장 모든 객체의 공통 메서드](docs/chapter03.md)
  * [**아이템 10**: `equals`는 일반 규약을 지켜 재정의하라](docs/chapter03.md#아이템-10-equals는-일반-규약을-지켜-재정의하라)
    * [**[추가]** `equals()`관련 토론 주제: 어디까지 비교할 것인가?](docs/chapter03.md#추가-equals관련-토론-주제-어디까지-비교할-것인가)
  * [**아이템 11**: `equals`를 재정의하려거든 `hashCode`도 재정의하라](docs/chapter03.md#아이템-11-equals를-재정의하려거든-hashcode도-재정의하라)
    * [**[추가]** `HashCodeBuilder` 유틸리티](docs/chapter03.md#추가-hashcodebuilder-유틸리티)
  * [**아이템 12**: `toString`을 항상 재정의하라](docs/chapter03.md#아이템-12-tostring을-항상-재정의하라)
  * [**아이템 13**: `clone` 재정의는 주의해서 진행하라](docs/chapter03.md#아이템-13-clone-재정의는-주의해서-진행하라)
    * [**[추가]** 깊은 복사(deep copy)가 문제되는 경우](docs/chapter03.md#추가-깊은-복사deep-copy가-문제되는-경우)
    * [**[추가]** `clone()` 관련 토론 주제: Prototype 패턴이 의미가 있는가?](docs/chapter03.md#추가-clone-관련-토론-주제-prototype-패턴이-의미가-있는가)
  * [**아이템 14**: `Comparable`을 구현할지 고려하라](docs/chapter03.md#아이템-14-comparable을-구현할지-고려하라)
    * [**[추가]** `CompareToBuilder` 유틸리티](docs/chapter03.md#추가-comparetobuilder-유틸리티)
  * [**[추가]** Pattern variable](docs/chapter03.md#추가-pattern-variable)
  * [**[추가]** Record Class](docs/chapter03.md#추가-record-class)
* [4장 클래스와 인터페이스](docs/chapter04.md)
  * [**아이템 15**: 클래스와 멤버의 접근 권한을 최소화하라](docs/chapter04.md#아이템-15-클래스와-멤버의-접근-권한을-최소화하라)
  * [**아이템 16**: `public` 클래스에서는 `public` 필드가 아닌 접근자 메서드를 사용하라](docs/chapter04.md#아이템-16-public-클래스에서는-public-필드가-아닌-접근자-메서드를-사용하라)
  * [**아이템 17**: 변경 가능성을 최소화하라](docs/chapter04.md#아이템-17-변경-가능성을-최소화하라)
    * [**[추가]** `List.of()`, `List.copyOf()`](docs/chapter04.md#추가-listof-listcopyof)
    * [**[추가]**  Guava에는 변경 불가능한 기본(primitive) 자료형 배열 클래스가 있음](docs/chapter04.md#추가-guava에는-변경-불가능한-기본primitive-자료형-배열-클래스가-있음)
  * [**아이템 18**: 상속보다는 컴포지션을 사용하라](docs/chapter04.md#아이템-18-상속보다는-컴포지션을-사용하라)
  * [**아이템 19**: 상속을 고려해 설계하고 문서화하라, 그러지 않았다면 상속을 금지하라](docs/chapter04.md#아이템-19-상속을-고려해-설계하고-문서화하라-그러지-않았다면-상속을-금지하라)
    * [**[추가]** 상속관련 토론 주제: 어노테이션 드리븐 개발과 관련 문제](docs/chapter04.md#추가-상속관련-토론-주제-어노테이션-드리븐-개발과-관련-문제)
  * [**아이템 20**: 추상 클래스보다는 인터페이스를 우선하라](docs/chapter04.md#아이템-20-추상-클래스보다는-인터페이스를-우선하라)
  * [**아이템 21**: 인터페이스는 구현하는 쪽을 생각해 설계하라](docs/chapter04.md#아이템-21-인터페이스는-구현하는-쪽을-생각해-설계하라)
    * [**[추가]** 인터페이스 설계와 TDD](docs/chapter04.md#추가-인터페이스-설계와-tdd)
  * [**아이템 22**: 인터페이스는 타입을 정의하는 용도로만 사용하라](docs/chapter04.md#아이템-22-인터페이스는-타입을-정의하는-용도로만-사용하라)
  * [**아이템 23**: 태그 달린 클래스보다는 클래스 계층구조를 활용하라](docs/chapter04.md#아이템-22-인터페이스는-타입을-정의하는-용도로만-사용하라)
  * [**아이템 24**: 멤버 클래스는 되도록 `static`으로 만들라](docs/chapter04.md#아이템-23-태그-달린-클래스보다는-클래스-계층구조를-활용하라)
  * [** 아이템 25**: 톱레벨 클래스는 한 파일에 하나만 담으라](docs/chapter04.md#아이템-24-멤버-클래스는-되도록-static으로-만들라)
  * [**[추가]** `@NonNull`, `@Nonnull`, `@NotNull`, `@Nullable`, `@NotBlank`, `@NotEmpty` 등](docs/chapter04.md#추가-nonnull-nonnull-notnull-nullable-notblank-notempty-등)
* [5장 제네릭](docs/chapter05.md)
  * [**아이템 26**: Raw 타입은 사용하지 말라](docs/chapter05.md#아이템-26-raw-타입은-사용하지-말라)
    * [제네릭 관련 용어 정리](docs/chapter05.md#제네릭-관련-용어-정리)
  * [**아이템 27**: 비검사 경고를 제거하라](docs/chapter05.md#아이템-27-비검사-경고를-제거하라)
  * [**아이템 28**: 배열보다는 리스트를 사용하라](docs/chapter05.md#아이템-28-배열보다는-리스트를-사용하라)
  * [**아이템 29**: 이왕이면 제네릭 타입으로 만들라](docs/chapter05.md#아이템-29-이왕이면-제네릭-타입으로-만들라)
  * [**아이템 30**: 이왕이면 제네릭 메서드로 만들라](docs/chapter05.md#아이템-30-이왕이면-제네릭-메서드로-만들라)
  * [**아이템 31**: 한정적 와일드카드를 사용해 API 유연성을 높이라](docs/chapter05.md#아이템-31-한정적-와일드카드를-사용해-api-유연성을-높이라)
  * [**아이템 32**: 제네릭과 가변인수를 함께 쓸 때는 신중하라](docs/chapter05.md#아이템-32-제네릭과-가변인수를-함께-쓸-때는-신중하라)
  * [**아이템 33**: 타입 안전 이종 컨테이너를 고려하라](docs/chapter05.md#아이템-33-타입-안전-이종-컨테이너를-고려하라)
  * [**[추가]** Switch expression](docs/chapter05.md#추가-switch-expression)
* [6장 열거 타입과 애너테이션](docs/chapter06.md)
  * [**아이템 34**: `int` 상수 대신 열거 타입을 사용하라](docs/chapter06.md#아이템-34-int-상수-대신-열거-타입을-사용하라)
  * [**아이템 35**: `ordinal` 메서드 대신 인스턴스 필드를 사용하라](docs/chapter06.md#아이템-35-ordinal-메서드-대신-인스턴스-필드를-사용하라)
  * [**아이템 36**: 비트 필드 대신 `EnumSet`을 사용하라](docs/chapter06.md#아이템-36-비트-필드-대신-enumset을-사용하라)
  * [**아이템 37**: `ordinal` 인덱스 대신 `EnumMap`을 사용하라](docs/chapter06.md#아이템-37-ordinal-인덱스-대신-enummap을-사용하라)
  * [**아이템 38**: 확장할 수 있는 열거 타입이 필요하면 인터페이스를 사용하라](docs/chapter06.md#아이템-38-확장할-수-있는-열거-타입이-필요하면-인터페이스를-사용하라)
  * [**아이템 39**: 명명 패턴보다 애너테이션을 사용하라](docs/chapter06.md#아이템-39-명명-패턴보다-애너테이션을-사용하라)
  * [**아이템 40**: `@Override` 애너테이션을 일관되게 사용하라](docs/chapter06.md#아이템-40-override-애너테이션을-일관되게-사용하라)
  * [**아이템 41**: 정의하려는 것이 타입이라면 마커 인터페이스를 사용하라](docs/chapter06.md#아이템-41-정의하려는-것이-타입이라면-마커-인터페이스를-사용하라)
  * [**[추가]**: Annotation Proccessor 개발](docs/chapter06.md#추가-annotation-proccessor-개발)
* [람다 스페셜](docs/lambda.md)
  * [람다 그거 먹는건가요?](docs/lambda.md#람다-그거-먹는건가요)
  * [함수형 프로그래밍](docs/lambda.md#함수형-프로그래밍)
  * [Java 람다 표현식](docs/lambda.md#Java-람다-표현식)
  * [스트림 API와 컬렉션 조작](docs/lambda.md#스트림-API와-컬렉션-조작)
  * [그럼 람다가 짱인가요?](docs/lambda.md#그럼-람다가-짱인가요)
  * [결론: 3줄 요약](docs/lambda.md#결론-3줄-요약)
* [7장 람다와 스트림](docs/chapter07.md)
  * [**아이템 42**: 익명 클래스 보다는 람다를 사용하라](docs/chapter07.md#아이템-42-익명-클래스-보다는-람다를-사용하라)
  * [**아이템 43**: 람다보다는 메서드 참조를 사용하라](docs/chapter07.md#아이템-43-람다보다는-메서드-참조를-사용하라)
  * [**아이템 44**: 표준 함수형 인터페이스를 사용하라](docs/chapter07.md#아이템-44-표준-함수형-인터페이스를-사용하라)
  * [**아이템 45**: 스트림은 주의해서 사용하라](docs/chapter07.md#아이템-45-스트림은-주의해서-사용하라)
  * [**아이템 46**: 스트림에서는 부작용 없는 함수를 사용하라](docs/chapter07.md#아이템-46-스트림에서는-부작용-없는-함수를-사용하라)
  * [**아이템 47**: 반환 타입으로는 스트림보다 컬렉션이 낫다](docs/chapter07.md#아이템-47-반환-타입으로는-스트림보다-컬렉션이-낫다)
  * [**아이템 48**: 스트림 병렬화는 주의해서 적용하라](docs/chapter07.md#아이템-48-스트림-병렬화는-주의해서-적용하라)
* [8장 메서드](docs/chapter08.md)
  * [**아이템 49**: 매개변수가 유효한지 검사하라](docs/chapter08.md#아이템-49-매개변수가-유효한지-검사하라)
  * [**아이템 50**: 적시에 방어적 복사본을 만들라](docs/chapter08.md#아이템-50-적시에-방어적-복사본을-만들라)
  * [**아이템 51**: 메서드 시그니처를 신중히 설계하라](docs/chapter08.md#아이템-51-메서드-시그니처를-신중히-설계하라)
  * [**아이템 52**: 다중정의는 신중히 사용하라](docs/chapter08.md#아이템-52-다중정의는-신중히-사용하라)
  * [**아이템 53**: 가변인수는 신중히 사용하라](docs/chapter08.md#아이템-53-가변인수는-신중히-사용하라)
  * [**아이템 54**: `null`이 아닌 빈 배열이나 컬렉션을 반환하라](docs/chapter08.md#아이템-54-null이-아닌-빈-배열이나-컬렉션을-반환하라)
  * [**아이템 55**: 옵셔널 반환은 신중히 하라](docs/chapter08.md#아이템-55-옵셔널-반환은-신중히-하라)
  * [**아이템 56**: 공개된 API요소에는 항상 문서화 주석을 작성하라](docs/chapter08.md#아이템-56-공개된-api요소에는-항상-문서화-주석을-작성하라)
* [9장 일반적인 프로그래밍 원칙](docs/chapter09.md)
  * [**아이템 57**: 지역변수의 범위를 최소화하라](docs/chapter09.md#아이템-57-지역변수의-범위를-최소화하라)
  * [**아이템 58**: 전통적인 for문보다는 for-each 문을 사용하라](docs/chapter09.md#아이템-58-전통적인-for문보다는-for-each-문을-사용하라)
  * [**아이템 59**: 라이브러리를 익히고 사용하라](docs/chapter09.md#아이템-59-라이브러리를-익히고-사용하라)
  * [**아이템 60**: 정확한 답이 필요하다면 `float`과 `double`은 피하라](#아이템-60-정확한-답이-필요하다면-float과-double은-피하라)
  * [**아이템 61**: 박싱된 기본 타입보다는 기본 타입을 사용하라](docs/chapter09.md#아이템-61-박싱된-기본-타입보다는-기본-타입을-사용하라)
  * [**아이템 62**: 다른 타입이 적절하다면 문자열 사용을 피하라](docs/chapter09.md#아이템-62-다른-타입이-적절하다면-문자열-사용을-피하라)
  * [**아이템 63**: 문자열 연결은 느리니 주의하라](docs/chapter09.md#아이템-63-문자열-연결은-느리니-주의하라)
  * [**아이템 64**: 객체는 인터페이스를 사용해 참조하라](docs/chapter09.md#아이템-64-객체는-인터페이스를-사용해-참조하라)
  * [**아이템 65**: 리플렉션보다는 인터페이스를 사용하라](docs/chapter09.md#아이템-65-리플렉션보다는-인터페이스를-사용하라)
  * [**아이템 66**: 네이티브 메서드는 신중히 사용하라](docs/chapter09.md#아이템-66-네이티브-메서드는-신중히-사용하라)
  * [**아이템 67**: 최적화는 신중히 하라](docs/chapter09.md#아이템-67-최적화는-신중히-하라)
  * [**아이템 68**: 일반적으로 통용되는 명명 규칙을 따르라](docs/chapter09.md#아이템-68-일반적으로-통용되는-명명-규칙을-따르라)
* [10장 예외](docs/chapter10.md)
  * [**아이템 69**: 예외는 진짜 예외 상황에만 사용하라](docs/chapter10.md#아이템-69-예외는-진짜-예외-상황에만-사용하라)
  * [**아이템 70**: 복구할 수 있는 상황에서는 검사 예외를, 프로그래밍 오류에는 런타임 예외를 사용하라](docs/chapter10.md#아이템-70-복구할-수-있는-상황에서는-검사-예외를-프로그래밍-오류에는-런타임-예외를-사용하라)
  * [**아이템 71**: 필요 없는 검사 예외 사용은 피하라](docs/chapter10.md#아이템-71-필요-없는-검사-예외-사용은-피하라)
  * [**아이템 72**: 표준 예외를 사용하라](docs/chapter10.md#아이템-72-표준-예외를-사용하라)
  * [**아이템 73**: 추상화 수준에 맞는 예외를 던지라](docs/chapter10.md#아이템-73-추상화-수준에-맞는-예외를-던지라)
  * [**아이템 74**: 메서드가 던지는 모든 예외를 문서화하라](docs/chapter10.md#아이템-74-메서드가-던지는-모든-예외를-문서화하라)
  * [**아이템 75**: 예외의 상세 메시지에 실패 관련 정보를 담으라](docs/chapter10.md#아이템-75-예외의-상세-메시지에-실패-관련-정보를-담으라)
  * [**아이템 76**: 가능한 한 실패 원자적으로 만들라](docs/chapter10.md#아이템-76-가능한-한-실패-원자적으로-만들라)
  * [**아이템 77**: 예외를 무시하지 말라](docs/chapter10.md#아이템-77-예외를-무시하지-말라)
* [11장 동시성](docs/chapter11.md)
  * [**아이템 78**: 공유 중인 가변 데이터는 동기화해 사용하라](docs/chapter11.md#아이템-78-공유-중인-가변-데이터는-동기화해-사용하라)
  * [**아이템 79**: 과도한 동기화는 피하라](docs/chapter11.md#아이템-79-과도한-동기화는-피하라)
  * [**아이템 80**: 스레드보다는 실행자, 태스크, 스트림을 애용하라](docs/chapter11.md#아이템-80-스레드보다는-실행자-태스크-스트림을-애용하라)
  * [**아이템 81**: `wait`와 `notify`보다는 동시성 유틸리티를 애용하라](docs/chapter11.md#아이템-81-wait와-notify보다는-동시성-유틸리티를-애용하라)
  * [**아이템 82**: 스레드 안전성 수준을 문서화하라](docs/chapter11.md#아이템-82-스레드-안전성-수준을-문서화하라)
  * [**아이템 83**: 지연 초기화는 신중히 사용하라](docs/chapter11.md#아이템-83-지연-초기화는-신중히-사용하라)
  * [**아이템 84**: 프로그램 동작을 스레드 스케줄러에 기대지 말라](docs/chapter11.md#아이템-84-프로그램-동작을-스레드-스케줄러에-기대지-말라)
* [12장 직렬화](docs/chapter12.md)
  * [**아이템 85**: 자바 직렬화의 대안을 찾으라](docs/chapter12.md#아이템-85-자바-직렬화의-대안을-찾으라)
  * [**아이템 86**: `Serializable`을 구현할지는 신중히 결정하라](docs/chapter12.md#아이템-86-serializable을-구현할지는-신중히-결정하라)
  * [**아이템 87**: 커스텀 직렬화 형태를 고려해보라](docs/chapter12.md#아이템-87-커스텀-직렬화-형태를-고려해보라)
  * [**아이템 88**: `readObject` 메서드는 방어적으로 작성하라](docs/chapter12.md#아이템-88-readobject-메서드는-방어적으로-작성하라)
  * [**아이템 89**: 인스턴스 수를 통제해야 한다면 `readResolve`보다는 열거 타입을 사용하라](docs/chapter12.md#아이템-89-인스턴스-수를-통제해야-한다면-readresolve보다는-열거-타입을-사용하라)
  * [**아이템 90**: 직렬화된 인스턴스 대신 직렬화 프록시 사용을 검토하라](docs/chapter12.md#아이템-90-직렬화된-인스턴스-대신-직렬화-프록시-사용을-검토하라)
* 번개 스터디
  * [2025/3/5](docs/lightning_250305.md)
  * [Vibe Coding 간이 스페셜](docs/vibe_coding.md)
* [Somewhat Effective Java](docs/somewhat.md)
  * [**아이템 0.01**: 닥치고 Java 23으로 업그레이드 하라](docs/somewhat.md#아이템-001-닥치고-java-23으로-업그레이드-하라)
  * [**아이템 0.02**: Primary Key는 별도 타입을 만들라](docs/somewhat.md#아이템-002-primary-key는-별도-타입을-만들라)
  * [**아이템 0.03**: 마스터 테이블의 PK는 독립 클래스로 만들라](docs/somewhat.md#아이템-003-마스터-테이블의-pk는-독립-클래스로-만들라)
  * [**아이템 0.04**: 원시 타입을 사용하는 경우 `null`처리를 위해 `Optional`을 고려하라](docs/somewhat.md#아이템-004-원시-타입을-사용하는-경우-null처리를-위해-optional을-고려하라)
