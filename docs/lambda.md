# 람다 스페셜

## 목차

* [람다 그거 먹는건가요?](#람다-그거-먹는건가요)
* [함수형 프로그래밍](#함수형-프로그래밍)
* [Java 람다 표현식](#Java-람다-표현식)
* [스트림 API와 컬렉션 조작](#스트림-API와-컬렉션-조작)
* [그럼 람다가 짱인가요?](#그럼-람다가-짱인가요)
* [결론: 3줄 요약](#결론-3줄-요약)

## 람다 그거 먹는건가요?

![람다 카라기난](img/lambda-carrageenan.png)

### 람다 대수(Lambda Calculus)

* 람다(Lamda, λ)는 그리스어 알파벳의 11번째 글자로, 로마 알파벳의 L에 해당함.
* 람다 대수는 알론조 처치(Alonzo Church)가 1930년대에 제안한 수학적 모델로, 함수와 함수의 적용을 다루는 이론적 체계임.
* 함수를 나타낼때 `λ` 기호를 사용하였기 때문에 람다라는 용어를 사용하게됨
* 처치-튜링 명제(Church-Turing thesis)
  * 람다 대수는 모든 계산 가능한 함수를 표현할 수 있음
  * 튜링 완전성(Turing completeness): 튜링 기계(Turing machine)와 동등한 계산 능력을 가짐

#### 람다 대수(Lambda Calculus) 기본 표기법

* 변수(Variable): 값을 나타내는 식별자 `$x$`
* 추상화(Abstraction): 함수를 말함
  * 함수 정의를 나타내는 식.
  * `$λx.M$` 형태로 표현됨. `λ`는 함수의 시작을, `x`는 인자,
    `M`은 함수의 본체(body)를 나타내는 또다른 람다 항이다.
  * 람다 추상화(함수)는 이름이 없다.
    * `$λx.x + 1$`은 Javascript로 `function(x) { return x + 1; }`와 같음
  * 수학시간에 배운 함수 표기와 차이
    * `f(x) = x + 1, g(x) = f(x) + 2`
      * `f`, `g`라는 함수 이름이 있음
      * `f(x)`를 호출하기 전에 정의되어 있어야 함
    * `$(λx.x + 2) (λx.x + 1)$`
      * 함수 이름이 없음
      * 함수 정의와 호출이 동시에 이루어짐
* 적용(Application): 함수 호출을 말함
  * 함수에 인자를 적용하는 것을 나타내는 식.
  * `$MN$` 형태로 표현됨. 여기서 `$M$`은 함수, `$N$`은 인자
  * 연산 순서는 왼쪽결합으로 `$MNP$`는 `$(MN)P$`와 같음.
  * `$λx.x + 1$`에 `2`를 적용하는 것은 `$(λx.x + 1) (2)$`로 표현됨.
    * 이는 Javascript로 `(function(x) { return x + 1; })(2)`와 같다.

#### 자유변수(free variable) vs. 속박변수(bound variable)

* 속박변수(bound variable): 람다 추상화에 정의된 변수(인자로 선언된 변수)
* 자유변수(free variable): 람다항 내에 나타나지만 해당변수를 속박되지 않은 변수(인자가 아닌 변수)
* `$λx.x + y$`에서 `x`는 속박변수이고, `y`는 자유변수임
* 스포일러: 이 구분은 이후 계산 과정에서 중요해짐

#### 커링(Curring)

* 람다 함수는 원칙상 인자가 1개뿐이다.
* `$λ(x,y).x + y$`는 `$λx.λy.x + y$`로 표현되어야 한다.
* 이렇게 다중 인자를 가진 함수를 하나의 인자를 가진 함수들의 연쇄로 변환하는 것을 커링이라고 한다.
  ```javascript
  (function(a, b) {
      return a + b;
  })(3, 4); // 3 + 4
     
  (function(a) {
      return function(b) {
          return a + b;
      };
  })(3)(4); // 3 + 4
  ```

### 람다 대수(Lambda Calculus) 계산 규칙

* 알파 변환(α-conversion)
  * 람다식의 속박 변수를 다른 변수로 치환해도 함수는 동등하다. 이때 자유변수는 건드리지 않는다.
    * `$λx.x + y$`와 `$λz.z + y$`는 같은 함수임
  * 알파변환의 목적은 다음의 베타 축약을 할때 변수명 충돌을 방지하기 위해 필요한 것임 
* 베타 축약(β-reduction)
  * 함수 적용(application)이 동작하는 원리
  * `$λx.M N$`은 함수 `$λx.M$`에 `$N$`을 인자로 적용하는 것인데
    * 이 과정은 `λ`(꼭지)를 떼고 `$M$`에서 속박변수 `$x$`를 `$N$`으로 치환한 결과와 같다
    ```
    $(λx.x + 3) 2$
    => $2 + 3$
    
    $(λf.λx.f(f x)) (λx.x*2) 3$
    => $(λf.λx.f(f x)) (λy.y*2) 3$     # 알파 치환 (x: = y)
       => $(λx.(λy.y*2)(λy.y*2 x)) 3$
          => $(λy.y*2)(λy.y*2 3)$
             => $(λy.y*2) 6$ 
                => $6 * 2$
                   => 12
      ```
* 에타 변환(η-conversion)
  * 두 함수가 모든 가능한 입력에 대해서 동일한 출력을 낸다면 두 함수는 동등하다.
  * 람다식의 본체가 속박변수에 대한 함수 호출인 경우, 이를 제거할 수 있음
    * `$λx.(M x)$`는 `$M$`과 동등하다.
    * `$λx.(λx.(x + 1) x)$`는 `$λx.x + 1$`과 같다.
      ```javascript
      let f1 = function(y) {
          return (function(x) { return x + 1 })(y)
      }
      
      let f2 = function(x) { return x + 1 }  // 두 함수는 같다.
      ```
  * 에타 변환은 람다식을 간결하게 하고 때로 추가적인 베타축약을 가능하게 한다.
  * Java에서는 메소드 참조(Method Reference)라는 개념의 이론적 배경이 된다.
    ```java
    @Test
    public void testLambda() {
        // (int, int) -> int
        BiFunction<Integer, Integer, Integer> f = (a, b) -> Integer.sum(a, b);
        BiFunction<Integer, Integer, Integer> g = Integer::sum;
        // 이론적으로는 동등하지만 실제로는 다르다 ㅋㅋ
        // assertEquals(f, g); 
        assertNotEquals(f, g);
    }
    ```

### 클로저(closure)

```javascript
let plus_a = function(a) {  // $λa.λb.a + b$
    return function(b) {    // 클로저
        return a + b        // a: 자유변수
    }
}

let plus_3 = plus_a(3)  // $(λa.λb.a + b) 3$ => $λb.3 + b$
plus_3(5)               // 8

let plus_4 = plus_a(4)  // $(λa.λb.a + b) 4$ => $λb.4 + b$
plus_4(5)               // 9
```

* 클로저(closure)란 함수와 함수가 정의된 환경이 캡슐화된 객체임
  * `function(b) { return a + b; }`는 `a`라는 자유변수를 가진 클로저
  * 클로저는 함수의 커링을 지원하기 위해서 고안된 개념이고 Lisp언어에서 유래함
* 클로저 인스턴스(closure instance)
  * 클로저에 자유변수에 대한 정의(`a := 3`)가 적용된 `plus_3`같은 함수
  * 딱히 규정된 이름은 없는 듯 하고, 다양하게 부른다.
    * 특화된 함수 (Specialized Function)
    * 부분 적용된 함수 (Partially Applied Function)
    * 커링된 함수 (Curried Function)
  * `plus_a` 함수의 지역변수인 `a`가 클로저 내에 자유변수로 포획(captured)되었다고 말한다.

> Q: 람다는 그냥 메소드를 인라인으로 쓸 수 있는 것 아닌가요?
> 
> A: 네 아닙니다. 람다식은 클로저이며 객체로 생각해야 합니다. \
>    포획된 변수를 필드로 하고, 함수의 본체를 메소드로 하는 (실행 가능한) 객체라고 생각하세요.

#### Javascript의 클로저

* Javascript에서는 클로저를 이용해서 private 멤버를 가진 객체를 생성하는 패턴이 흔히 사용됨

```javascript
let Person = function(name, age) {

    let _name = name || "John Doe"; // 클로저를 통해 private 상태 유지
    let _age = age;

    const personInstance = {
        home: "Seoul",      // public 속성
                            // delete person['home'] 같이 삭제도 가능
    };
    Object.defineProperties(personInstance, {
        isAdult: {
            // alice.isAdult() 형태로 접근 가능
            value: () => _age >= 18,
            enumerable: false,   // for (let prop in person) {} 같이
                                 // 순회할때 포함되지 않음
            configurable: false  // 프로퍼티를 read-only로 만듬
        },
        name:   {
            // alice.name = "Alice" 형태로 접근 가능
            get: () => _name,
            set: (newName) => { _name = newName;},
            enumerable: true,
            configurable: false
        },
        age:    {
            // alice.age = 30 형태로 접근 가능
            get: () => _age,
            set: (newAge) => { _age = newAge; },
            enumerable: true,
            configurable: false
        },
    });
    return personInstance;
}
module.exports = Person
```

* Javascript에서는 지역변수가 클로저에 포획되면, 함수가 종료되더라도 클로저가 살아있는 동안까지 생명이 연장됨
  * `_name`과 `_age`는 `Person()` 함수가 종료되더라도 getter와 setter, `isAdult()` 등의 메소드에서 포획되었으므로 계속 사용가능함
  * ES6 부터 `class` 문법이 추가되었고, ES2022 부터는 `#`접두사를 사용하여 진정한 `private`멤버를 만들 수 있게 되었다. 
  * 하지만 클로저를 이용해서 클래스를 흉내내는 코드도 아직 많이 사용되고 있다.

#### 변수 포획(Capturing Variables)

* C언어 부터 함수 포인터를 사용할 수 있었고, 함수 포인터를 다른 함수의 인자로 넘길 수도 있지만,
  함수 포인터를 람다식이라고 하지 않음. (C에서는 변수 포획 메커니즘이 구현 불가능함)
* C++에서도 C와 같이 함수 포인터는 가능하지만 그것을 람다라고 하지 않음.
  * C++에서는 별도의 람다식이 지원되며 템플릿 클래스와 같이 취급됨
* Java 에서는 람다식에 변수 포획이 지원되지만 `final` 이나 effectively final 만 가능하다는 제약이 있음
  ```java
  void main() {
      int a = 3;
      Runnable r = () -> {
          System.out.println(a); // a는 자유변수로 포획됨
      };
      a = 4; // 컴파일 에러: 람다식에서 포획된 변수는 final 이나 effectively final 이어야 함
  }
  ```
* `final` 객체 참조를 통한 변경 가능한 변수를 만드는 것은 가능함``
  ```java
  class Holder {
      int value;
  }
  void main() {
      final Holder holder = new Holder();
      holder.value = 3; // 변경 가능한 변수
      Runnable r = () -> {
          System.out.println(holder.value); // holder 객체 참조는 자유변수로 포획됨
      };
      holder.value = 4; // holder의 필드는 변경 가능함
  }
  ```

## 함수형 프로그래밍

![진화](img/evolution.png)

### 람다 대수(Lambda Calculus)에서 함수형 프로그래밍(Functional Programming) 까지

#### 알론조 처치(Alonzo Church, 1903 ~ 1995)

![Alonzo Church](img/alonzo_church.jpg)

* 컴퓨터 과학은 아니고 논리 수학자
* 1930년대 람다 대수(Lambda Calculus)를 제안
* 제자들에 의해서 튜링 완전성(Turing completeness)이 증명됨

> 스티븐 클레이니(Stephen Kleene, 1909 ~ 1994)
>
> ![Stephen Kleene](img/stephen_kleene.jpg)
> * 알론조 처치의 제자이자 논리 수학자
> * 재귀 함수론과 람다 대수 사이의 관계를 연구
> * 정규 표현식(regular expression)이라는 용어와 기초가 되는 논리 연산자(`|`, `+`, `*` 등)를 제안

#### 하스켈 커리(Haskell Brooks Curry, 1900 ~ 1982)

![Haskell Curry](img/haskell_curry.jpg)

* 역시 논리 수학자로 함수형 프로그래밍의 이론적 토대를 만듬.
* 커링(Currying)이라는 용어는 이분의 이름에서 따온 것임
* 이름의 모든 부분이 프로그래밍언어의 이름으로 사용되었다.
  * Haskell: 함수형의 대표격으로 언급되는 언어
  * Brooks: 실험적 다중 패러다임 프로그래밍 언어(2003년)
  * Curry: 역시 함수형 언어

> 모시스 쇤핀켈 (Moses Schönfinkel, 1882 ~ 1942)
>
> ![Moses_Schönfinkel_1922_(cropped).jpg](img/Moses_Sch%C3%B6nfinkel_1922_%28cropped%29.jpg)
> * 러시아 출신 논리학자로 커링 개념의 원작자
> * 1920년대 다중 인자를 받는 함수를 하나의 인자만 받는 함수들의 연속으로 변환하는 아이디어를 제시

#### 1급 시민(First-Class Citizen)

프로그래밍 언어에서 다음 조건을 만족하는 것를 1급 시민이라고 함.

* 변수에 할당될 수 있다.
* 데이터 구조에 저장될 수 있다.
* 함수의 인자로 전달될 수 있다.
* 함수의 반환 값으로 사용될 수 있다.
* 실행 중에 동적으로 생성될 수 있다.

최초로 함수를 1급 시민으로 취급한 언어는 Lisp(LISt Processing) 언어이다.

```lisp
(defun factorial (n)
  (if (<= n 1)
      1
      (* n (factorial (- n 1)))))
```

> 크리스토퍼 스트레이치(Christopher Strachey, 1916 ~ 1975)
>
> ![Christopher Strachey](img/christopher_strachey.jpg)
> * 60년대에 프로그래밍 언어를 연구하며 1급 시민 개념을 최초로 제시
> * Lisp개발에 참여한건 아니고, 이후 C언어의 조상인 CPL(Combined Programming Language)
    >   언어 개발에 참여하심

#### ISWIM (If You See What I Mean)

피터 랜딘이 실험적으로 만든 언어로 모든 함수형 언어의 조상에 해당.

```iswim
let fact n =
  where rec f x = if x = 0 then 1 else x * f (x - 1)
  in f n
```

> 피터 랜딘(Peter Landin, 1930 ~ 2009)
>
> ![Peter Landin](img/peter_landin.jpg)
> * SECD 기계: 람다 대수 표현식을 평가하기 위한 추상 기계. 이는 많은 함수형 언어 인터프리터의 기초가 됨
> * "Syntactic sugar" (문법적 설탕)라는 표현을 대중화 시킴

#### Hindley-Milner 타입 추론 알고리즘

* 프로그래머가 명시적으로 타입을 선언하지 않아도 컴파일러가 타입을 추론할 수 있는 알고리즘.
* J. Roger Hindley에 의해서 처음 발견하였고, 로빈 밀러가 (독립적으로?) 다시 발견함

```java
var v = 1; // v는 int 타입으로 추론됨
var f = (x) -> x + 1; // f는 (int) -> int 타입(Function<Integer>)으로 추론됨
```

> 로빈 밀너(Robin Milner, 1934 ~ 2010)
>
> ![Robin Milner](img/robin_milner.jpg)
> * ML(Meta Language) 함수형 언어 개발 (Standard ML, OCaml, F# 등의 조상)

#### 함수형 프로그래밍 언어(Functional Programming Language)

* 최근의 범용 프로그래밍 언어들은 대부분 람다식과 함수형 개념을 지원하는데,
  그런 것 말고 함수형 전용으로 만들어진 언어들을 소개한다.
* Lisp (LISt Processing)
  * 최초로 함수형 프로그래밍을 지원한 언어
  * 괄호의 압박으로 Lots of Irritating Superfluous Parentheses(짜증나는 불필요한 괄호가 너무 많음)의 약자라는 밈이 있음
  * 주요 방언: Common Lisp, Scheme, Clojure 등
  * Clojure는 JVM 상에서 동작하며 Java 클래스 라이브러리를 사용 가능함 (하지만 네이밍 컨벤션이 이질적이라...)
    ```clojure
    (ns cfj.core
      (:import [cfj Event]))

    (defrecord EventImpl [name ts]
      Event
      (getTimestamp [_] ts)
      (getName [_] name))
    ```
* ML (Meta Language)
  * Hindley-Milner 타입 추론 알고리즘을 사용함
  * 패턴 매칭(Pattern Matching) 기능으로 복잡한 데이터 구조를 간결하게 분해하고 처리 가능
  * 모듈 시스템을 통해 코드의 구조화와 재사용성을 높임
  * Standard ML, OCaml, F# 등 많은 함수형 언어들의 직계 조상
  * F#은 .NET에서 구동되는 함수형 언어
    ```fsharp
    let describeNumber number =
        match number with
        | 0 -> "Zero"
        | 1 -> "One"
        | 2 -> "Two"
        | n when n > 0 && n < 10 -> "A small positive number between 3 and 9"
        | _ -> "Something else"
    
    let square x = x * x
    let isOdd x = x % 2 <> 0
    let numbers = [ 1; 2; 3; 4; 5 ]
    let squareOddValuesAndAddOne values =
        values
        |> List.filter isOdd
        |> List.map square
        |> List.map (fun x -> x + 1)

    let result = squareOddValuesAndAddOne numbers
    printfn "Result: %A" result // Result: [2; 10; 26]
    ```
* Erlang
  * 동시성(Concurrency)과 분산 시스템(distributed system)에 중점을 둔 언어
  * 에릭슨(Ericsson)의 통신 장비용으로 개발되었으며 엄청난 수의 동시 처리를 안정적으로 지원
  * 엑터 모델(Actor Model): 가볍고 독립적인 프로세스들이 메시지를 주고 받으며 비동기적으로 동작함
  * 오류 감내(Fault Tolerance): 한 프로세스의 오류가 다른 프로세스에 영향을 주지 않도록 하고, 오류 감지와 복구를 슈퍼바이저가 알아서 처리
  * 핫 스와핑(Hot Swapping): 실행 중인 시스템에 코드를 동적으로 업데이트할 수 있는 기능
  * 패턴 매칭, 불변 데이터, 고차함수 등 함수형 특징을 지원
  * 고가용성 분산 시스템 구축에 많이 사용됨
  ```erlang
  -spec create_actor() -> pid().
  create_actor() ->
      Pid = spawn(?MODULE, receive_message, []),
      io:format("~n spawned new process under Pid --> ~p ~n", [Pid]),
      Pid.

  -spec send_message(ActorAddress :: pid(), Message :: any()) -> ok.
  send_message(ActorAddress, Message) ->
      ActorAddress ! {self(), Message}.

  -spec receive_message() -> any().
  receive_message() ->
      receive
          {From, Int} when is_integer(Int) ->
              From ! ok,
              io:format("~n I've got an Integer from ~p actor, lets multiply it by 2 and will get ->> ~p ~n", [From, Int * 2]),
              receive_message();
          {From, List} when is_list(List) ->
              From ! ok,
              io:format("~n I've got a List from ~p actor, lets concat it with another one ->> ~p ~n", [From, List ++ [1, 2, 3]]),
              receive_message();
          {From, _} ->
              io:format("~n I've got unexpected message from ~p actor, process will be terinated ~n", [From])
      end.
  ```
* Haskell
  * 순수 함수형 프로그래밍 언어로 부작용(side effects)을 철저히 관리
  * 지연 평가(Lazy Evaluation): 성능 최적화와 무한 데이터 구조를 지원함
  * 타입 클래스(Type Classes): 다형성을 제공하는 강력한 타입 시스템. 연산자 오버로딩과 비슷함
  * 모나드(Monads): 부작용(IO, 상태 변경, 예외처리 등)을 안전하게 처리하기 위한 추상화
  * 강력한 타입 시스템과 타입 추론 지원
  * 함수형 프로그래밍의 대표격으로 자주 언급됨
  ```haskell
  factorial :: Int -> Int
  factorial 0 = 1
  factorial n = n * factorial (n-1)
  ```
* Brooks
  * "페트라 호프스테트가 2003년에 제안한 실험적 다중 패러다임 프로그래밍 언어"라고 함
  * "함수형 논리" 언어로서 "함수형 세계의 결정론적 계산과 논리형 세계의 비결정론적 검색 연산을 결합"한다고 함
* Curry
  * 함수형과 논리 프로그래밍을 결합한 언어
  * 비결정성(Non-determinism): 하나의 입력에 대해 여러가지 가능한 결과가 있을 수 있으며 시스템은 이 해를 탐색함
  * 지연 평가(Lazy Evaluation): 필요할 때만 계산을 수행하여 성능 최적화
  * 고차함수, 패턴 매칭, 타입시스템 등을 지원
  * 제약조건 해결(Constraint Solving): 제약조건을 만족하는 해를 찾는 문제 해결에 강력함
  * 학계에서 연구되지만 범용 언어로 사용되는 경우는 적은듯함
  ```curry
  split _ [] = ([],[])
  split e (x:xs) | e>=x  = (x:l,r)
                 | e<x   = (l,x:r)
                 where (l,r) = split e xs

  qsort []     = []
  qsort (x:xs) = let (l,r) = split x xs
                 in qsort l ++ (x:qsort r)

  goal = qsort [8,6,7,5,4,2,3,1]
  ```

### 무엇에 쓰는 물건인고?

![무엇에 쓰는 물건인고?](img/what-might-this-thing-be-for.webp)

#### 절차적 vs. 선언적

* 거창하게 함수형 프로그래밍이라고 말하면 이해하기 어렵지만,
  하고자 하는 일을 **선언적**으로 기술하는 언어라고 생각하면 쉬움
  * 우리는 SQL을 배울 때 절차적 사고를 버려야 한다고 배웠음
  * 그 외에도 HTML, CSS 등 선언적 언어를 우리는 많이 다루고 있음

#### Collection과 Stream API

```sql
SELECT name FROM plant WHERE lifeCycle = @annual
```

* 절차적 구현
  ```java
  @Test
  public void testProcedural() {
      Plant.LifeCycle annual = Plant.LifeCycle.ANNUAL;
  
      for (Plant p : plants) {                // FROM plant
          if (p.lifeCycle() == annual) {      // WHERE lifeCycle = 'annual'
              String name = p.name();         // SELECT name
              System.out.println(name);
          }
      }
  }
  ```

* 함수형 구현
  ```java
  @Test
  public void testLambda() {
  
      Plant.LifeCycle annual = Plant.LifeCycle.ANNUAL;
  
      plants.stream()                                     // FROM plant
              .filter(p -> p.lifeCycle() == annual)       // WHERE lifeCycle = 'annual'
              .map(Plant::name)                           // SELECT name
              .forEach(System.out::println);
  }
  ```

#### 콜백 함수를 간단히

* 비동기/동시성 처리(Asynchronous/Concurrent Processing)
  ```java
  void main() {
      // 여러 쓰레드에서 동시에 재배
      try (ExecutorService executor = Executors.newFixedThreadPool(plants.size())) {

          // 식물별 쓰레드를 만들고 작업을 제출
          List<Future<Flower>> futures = plants.stream()
                  .map(p -> executor.submit(() -> p.grow()))   // Stream<Future<Flower>>
                  .collect(Collectors.toList());
  
          // 모든 작업 제출 완료되었고 각자 실행중
          futures.stream()
                  .forEach(future -> {
                      try {
                          Flower flower = future.get();  // 완료 안된 작업이 있으면 여기서 블로킹
                          System.out.println(flower.color());
                      } catch (InterruptedException | ExecutionException e) {
                          e.printStackTrace();
                      }
                  });
      }
  }
  ```
* 이벤트 리스너
  ```java
  void main() {
      JButton button = new JButton("클릭하세요");
      button.addActionListener(e -> System.out.println("버튼 클릭됨!"));
  }
  ```
* `null`검사
  ```java
  void main() {
      service.getPlantByName("장미").ifPresentOrElse(
          plant -> System.out.println("장미가 있습니다: " + plant.name()),
          () -> System.out.println("장미가 없습니다.")
      );
  }
  ```

## Java 람다 표현식

### 람다 표현식 문법

* 기본 문법: `(parameters ...) -> { statements }`
* 함수 바디가 리턴문만 있는 경우
  * `(parameters ...) -> { return value; }` => `$(parameters ...) -> value`
* 인자가 한개 뿐인 경우
  * `(parameter) -> value` => `parameter -> value`
* 자료형 지정이 가능하나 보통은 타입 추론에 맏긴다.
  * `(String s) -> s.length()` => `s -> s.length()`
* 메소드 참조: 함수 바디가 메소드 호출 하나뿐인 경우
  * `s -> s.length()` => `String::length`
  * `s -> { System.out.println(s); }` => `System.out::println`

### 람다는 객체라고 했는데 어떤 클래스?

* 람다식은 함수형 인터페이스(Functional Interface)라고 불리는 인터페이스를 구현한 익명 클래스의 인스턴스임
* 함수형 인터페이스
  * 추상 메소드(abstract method)가 한개뿐인 인터페이스
  * `@FunctionalInterface` 어노테이션을 사용하여 함수형 인터페이스임을 명시할 수 있음(없어도 됨)
  * 메소드의 이름은 람다에서는 상관없고, 직접 코드로 호출하는 경우에 적절한 이름으로 선택하면 된다.

```java
@FunctionalInterface
interface Operation<T> {
    T apply(T a, T b);
}

enum Operator {
    ADD("+", (a, b) -> a + b),
    SUBTRACT("-", (a, b) -> a - b),
    MULTIPLY("*", (a, b) -> a * b),
    DIVIDE("/", (a, b) -> a / b);

    private final String symbol;
    private final Operation<Integer> operation;

    Operator(String symbol, Operation<Integer> operation) {
        this.symbol = symbol;
        this.operation = operation;
    }
    public String symbol() { return symbol; }
    public Operation<Integer> operation() { return operation; }
}
```

* 함수형 인터페이스는 C언어의 함수 포인터 선언과 비슷하다고 생각하면 됨

```c
#include <stdio.h>

typedef int (*Operator)(int, int);

int add(int, int);

void main() {
    Operator plus = add;
    printf("%d + %d = %d\n", 3, 4, plus(3, 4));
}

int add(int a, int b) {
    return a + b;
}
```

### 내장 함수 인터페이스

* 그런데 함수의 기능과 이름은 다양할 수 있지만 많이 사용하는 함수의 시그니처는 거기서 거기인 경우가 많음
* 그래서 많이 사용되는 함수 시그니처가 `java.util.function` 패키지에 미리 정의되어있음.

| 시그니처             | 이름                   | 메소드                   | 예                     |
|------------------|----------------------|-----------------------|-----------------------|
| `(T) -> T`       | `UnaryOperator<T>`   | `T apply(T t)`        | `String::toLowerCase` |
| `(T, T) -> T`    | `BinaryOperator<T>`  | `T apply(T t1, T t2)` | `BigInteger::add`     |
| `(T) -> boolean` | `Predicate<T>`       | `boolean test(T t)`   | `Collection::isEmpty` |
| `(T) -> R`       | `Function<T, R>`     | `R apply(T t)`        | `Arrays::asList`      |
| `() -> T`        | `Supplier<T>`        | `T get()`             | `Instant::now`        |
| `(T) -> void`    | `Consumer<T>`        | `void accept(T t)`    | `System.out::println` |
| `() -> void`     | `java.lang.Runnable` | `void run()`          | `Thread::run`         |

* 위 예제의 `Operation<T>` 인터페이스는 `BinaryOperator<T>`와 같은 시그니처이며
  새로 인터페이스를 만들 필요 없이 그냥 `BinaryOperator<T>`를 사용하면 됨.
  ([아이템 44: 표준 함수형 인터페이스를 사용하라](chapter07.md#아이템-44-표준-함수형-인터페이스를-사용하라))

```java
enum Operator {
    ADD("+", (a, b) -> a + b),
    SUBTRACT("-", (a, b) -> a - b),
    MULTIPLY("*", (a, b) -> a * b),
    DIVIDE("/", (a, b) -> a / b);

    private final String symbol;
    private final BinaryOperator<Integer> operation;

    Operator(String symbol, BinaryOperator<Integer> operation) {
        this.symbol = symbol;
        this.operation = operation;
    }
    public String symbol() { return symbol; }
    public BinaryOperator<Integer> operation() { return operation; }
}
```

* 원시 자료형(primitive types)별 변형

| 기본형                 | 시그니처                  | 이름                     | 메소드                               |
|---------------------|-----------------------|------------------------|-----------------------------------|
| `UnaryOperator<T>`  | `(long) -> long`      | `LongUnaryOperator`    | `long apply(long v)`              |
|                     | `(int) -> int`        | `IntUnaryOperator`     | `int apply(int v)`                |
|                     | `(double) -> double`  | `DoubleUnaryOperator`  | `double apply(double v)`          |
| `BinaryOperator<T>` | `(long) -> long`      | `LongBinaryOperator`   | `long apply(long v1, long v2)`    |
|                     | `(int) -> int`        | `IntBinaryOperator`    | `int apply(int v1, int v2)`       |
|                     | `(double) -> double`  | `DoubleBinaryOperator` | `double apply(double v1, int v2)` |
| `Predicate<T>`      | `(long) -> boolean`   | `LongPredicate`        | `boolean test(long v)`            |
|                     | `(int) -> boolean`    | `IntPredicate`         | `boolean test(int v)`             |
|                     | `(double) -> boolean` | `DoublePredicate`      | `boolean test(double v)`          |
| `Function<T, R>`    | `(long) -> int`       | `LongToIntFunction`    | `int apply(long v)`               |
|                     | `(long) -> double`    | `LongToDoubleFunction` | `double apply(long v)`            |
|                     | `(int) -> long`       | `IntToLongFunction`    | `long apply(int v)`               |
|                     | `(int) -> double`     | `IntToDoubleFunction`  | `double apply(int v)`             |
|                     | `(double) -> long`    | `DoubleToLongFunction` | `long apply(double v)`            |
|                     | `(double) -> int`     | `DoubleToIntFunction`  | `int apply(double v)`             |
|                     | `(T) -> long`         | `ToLongFunction<T>`    | `long apply(T t)`                 |
|                     | `(T) -> int`          | `ToIntFunction<T>`     | `int apply(T t)`                  |
|                     | `(T) -> double`       | `ToDoubleFunction<T>`  | `double apply(T t)`               |
| `Supplier<T>`       | `() -> long`          | `LongSupplier`         | `long get()`                      |
|                     | `() -> int`           | `IntSupplier`          | `int get()`                       |
|                     | `() -> double`        | `DoubleSupplier`       | `double get()`                    |
|                     | `() -> boolean`       | `BooleanSupplier`      | `boolean get()`                   |
| `Consumer<T>`       | `(long) -> void`      | `LongConsumer`         | `void accept(long v)`             |
|                     | `(int) -> void`       | `IntConsumer`          | `void accept(int v)`              |
|                     | `(double) -> void`    | `DoubleConsumer`       | `void accept(double v)`           |

* 인수 개수별 변형

| 기본형              | 시그니처                  | 이름                         | 메소드                          |
|------------------|-----------------------|----------------------------|------------------------------|
| `Predicate<T>`   | `(T, U) -> boolean`   | `BiPredicate<T, U>`        | `boolean test(T t, U u)`     |
| `Function<T, R>` | `(T, U) -> R`         | `BiFunction<T, U, R>`      | `R apply(T t, U u)`          |
|                  | `(T, U) -> long`      | `ToLongBiFunction<T, U>`   | `long apply(T t, U u)`       |
|                  | `(T, U) -> int`       | `ToIntBiFunction<T, U>`    | `int apply(T t, U u)`        |
|                  | `(T, U) -> double`    | `ToDoubleBiFunction<T, U>` | `double apply(T t, U u)`     |
| `Consumer<T>`    | `(T, U) -> void`      | `BiConsumer<T, U>`         | `void accept(T t, U u)`      |
|                  | `(T, long) -> void`   | `ObjLongConsumer<T>`       | `void accept(T t, long v)`   |
|                  | `(T, int) -> void`    | `ObjIntConsumer<T>`        | `void accept(T t, int v)`    |
|                  | `(T, double) -> void` | `ObjDoubleConsumer<T>`     | `void accept(T t, double v)` |

```java
@Test
public void testHaskellBrooksCurrying() {

    BinaryOperator<Integer> add = (a, b) -> a + b;
    assertEquals(7, add.apply(3, 4));
  
    Function<Integer, UnaryOperator<Integer>> addCurrying = (a) -> (b) -> a + b;
    assertEquals(7, addCurrying.apply(3).apply(4));
}
```

### Java에서의 함수(람다식)란 어떤 존재인가?

* Javascript에서 함수는 리터럴로 취급된다.
  ```javascript
  (function(a, b) { return a + b; })(1, 2); // 3

  ((a, b) => a + b)(1, 2); // 3
  ```
* 반면 자바에서의 함수(람다식)은 리터럴이 아니다.
  ```java
  @Test
  public void testFunctionClassName() {
  
      // 람다식은 리터럴이 아니다.
      // int val = ((a, b) -> a + b)(1, 2);  // 컴파일 안됨

      BinaryOperator<Integer> addLambda = (a, b) -> a + b;
      BinaryOperator<Integer> addAnonClz = new BinaryOperator<Integer>() {
          @Override
          public Integer apply(Integer a, Integer b) {
              return a + b;
          }
      };

      // 람다식은 익명 클래스이지만 .class 파일이 만들어지지는 않는다!!
      // 게다가 클래스명은 매번 달라짐!!!
      // assertEquals("lambdaspecial.java.LambdaTest$$Lambda/0x00000288811608c0", addLambda.getClass().getName());
      assertEquals("lambdaspecial.java.LambdaTest$1", addAnonClz.getClass().getName());
  }
  ```
* Java의 람다식이 동작하는 방식
  * 컴파일 타임
    * 컴파일러는 람다식이 나오면 클래스에 함수 본체를 `private static` 메소드로 컴파일해놓음
    * 람다식이 출현한 위치에는 `invokedynamic`이라는 JVM 명령을 넣음
  * 런타임
    * JVM은 `invokedynamic`을 발견하면 `java.lang.invoke.LambdaMetafactory`를 이용해
      동적으로 클래스를 생성하고 앞서 만들었던 `private static` 메소드를 연결함
    * 클래스명은 JVM 내부 규칙에 따라 자동으로 만들어지므로 매번 달라질 수 있음. \
      람다식을 위해서 `Class.forName()`을 사용할 일은 없겠지만 근본적으로 불가능함.
* Java의 타입 시스템은 클래스와 인터페이스를 근간으로 하여 설계되어 있음
  * 굴러온 돌인 람다(함수)라는 개념이 시스템의 근간을 흔들게 할 수는 없었던 모양
  * `(a, b) -> a + b`는 `BinaryOperator<T>`일 수도 있고, `BiFunction<T,U,R>`일 수도 있고, `MyArithmeticOperator<T,R>`일 수도 있음.
    * 따라서 `int r = ((a, b) -> a + b)(3, 4)`이라는 코드는 런타임에 어떤 인터페이스를 구현할 지 결정할 수 없음
  * 람다식의 실제 클래스 타입은 위치한 곳(변수 선언이나 메소드 인자 선언)에서 요구되는 인터페이스에 따라 결정되는데
    이를 목표 타이핑(target typing)이라고 함.
* 로딩 시점의 비교
  * 익명클래스: 클래스는 보통 클래스가 로드될때 참조하는 다른 클래스가 연쇄적으로 같이 로딩됨 \
    -> 익명 클래스들도 마찬가지로 애플리케이션 시작시 동시 로딩됨
  * 람다: 해당 람다식이이 있는 코드가 처음 실행될 때 클래스가 생성됨 \
    -> 지연 로딩(lazy loading)으로 로딩 속도가 빨라질 수 있음

## 스트림 API와 컬렉션 조작

![세대 차이](img/generation-gap.jpg)

### 스트림이란 무엇인가?

```
Forged in the fires of Windows, this file emerges!
What's kickin', Windows?

Bill Gates for Emperor!!
Microsoft shall inherit the Earth!!
```

* `ed`는 Unix에서 사용된 초기의 텍스트 편집기임
  * `ed` 스크립트의 예시
  ```
  $ cat <<EOF | ed -s file.txt      # 문자열 치환을 수행하고 끝으로 `wq` 저장
  g/Windows/s//Windows(TM)/g
  g/Microsoft/s//Microsoft(TM)/g
  /^Bill Gates/s//The Mighty Bill G/
  wq
  EOF
  $ cat file.txt
  Forged in the fires of Windows(TM), this file emerges!
  What's kickin', Windows(TM)?
  
  The Mighty Bill G for Emperor!!
  Microsoft(TM) shall inherit the Earth!!
  ```

* 쉘 스크립트에서 많이 사용하는 `sed`는 `stream ed`를 의미하는데, `ed`와 비슷하지만 약간 다름
  * `sed`는 메모리에 올려놓기에는 너무 큰 파일을 편집하기 위해 개발되었음
  ```
  $ cat <<EOF | sed -f - file.txt >newfile.txt  # 명령이 약간 다르고 `wq`가 없음
  s/Windows/Windows(TM)/g
  s/Microsoft/Microsoft(TM)/g
  s/^Bill Gates/The Mighty Bill G/
  EOF
  $ cat newfile.txt                      # newfile.txt에 저장됨
  Forged in the fires of Windows(TM), this file emerges!
  What's kickin', Windows(TM)?
  
  The Mighty Bill G for Emperor!!
  Microsoft(TM) shall inherit the Earth!!
  $ cat file.txt                         # 원본은 건드리지 않았음
  Forged in the fires of Windows, this file emerges!
  What's kickin', Windows?
  
  Bill Gates for Emperor!!
  Microsoft shall inherit the Earth!!
  ```

* `ed`와 `sed`의 차이점 - 스트림이란 무엇인가?
  * `ed`는 파일을 메모리 버퍼에 올려놓고, `ed` 명령어를 순차적으로 실행한다.
    * 스크립트의 명령 1행을 읽은 후 버퍼에서 편집할 라인을 찾아가서 수행됨
      * `g/Windows/` -> `Windows`가 있는 모든 행
      * `/^Bill Gates/` -> `Bill Gates`로 시작하는 첫번째 행
      ```ed
      g/Windows/s//Windows(TM)/g
      g/Microsoft/s//Microsoft(TM)/g
      /^Bill Gates/s//The Mighty Bill G/
      wq
      ```
    * 스크립트의 모든 행을 처리하면 끝남
  * `sed`는 메모리 버퍼에 파일을 올려두지 않고 파일에서 한행씩 읽어서 처리한다.
    * 입력 1행을 읽은 후 스크립트를 순차적으로 실행하고, 다음 행을 읽은 후 또 스크립트를 실행하는 식으로 파일의 끝까지 반복
      * 명령에 행지정을 따로 안하면 "모든 행에 대한"(처음의 `g`)이란 설정이 암묵적으로 적용되는 효과가 있음
        * 예시에서 `Bill Gates`로 시작하는 행이 여러개 있었다면 모두 적용됨 (`g/^Bill Gates/s//The Mighty Bill G/` 와 같음)
      ```sed
      s/Windows/Windows(TM)/g
      s/Microsoft/Microsoft(TM)/g
      s/^Bill Gates/The Mighty Bill G/
      ```
      * 입력의 모든 행을 다 읽으면 끝남 (입력:명령이 N:M으로 모두 적용됨)
    * 그리고 기본적으로 편집된 내용을 원본에 덮어쓰지 않고 다른 파일(표준출력)로 저장함 (옵션으로 덮어쓰게 할 수도 있기는 함)

* 스트림 API를 새로 배우는 상황은 `ed`만을 사용하다가 처음으로 `sed`를 사용해보는 것과 같으며 약간의 사고의 전환이 필요함
  * 참고로 현재 Unix/Linux에서는 `ed`는 `vi`등으로 대체되었지만, `sed`는 여전히 많이 사용되고 있다.
  * Java는 스트림과 람다의 등장(java 8+)을 기준으로 고전 자바(Classic Java)와 모던 자바(Modern Java)라고 할 만큼 개발 방식이 바뀌고 있다.

* Stream 처리 방식을 사용하는 또다른 사례
  * XML Parser를 사용할때 보통 DOM 기반 인터페이스를 사용하지만, 스트림 기반 인터페이스를 이용하는 SAX(Simple API for XML)도 있다.
    * SAX는 문서를 전체 로드하지 않고 처리할 수 있으므로 메모리를 적게 쓰고, 경우에 따라 더 빠르고 효율적으로 처리할 수도 있다.
  * 대용량 ETL이나 빅데이터 처리 파이프라인, 동영상 스트리밍, 이벤트 스트림 처리 특화 시스템(Kafka, Akka Streams, Spark Streaming 등)
  * 스트림 처리의 특징
    * 작은 단위로 나누어 단계별로 처리하여 메모리 사용량을 최적화
    * 작은 단위는 독립적으로 실행되므로 병렬 처리가 가능
    * 무한 데이터 스트림을 지원할 수 있음 (이벤트 로그 수집 등)

#### 절차적 vs. 함수형

```java
void main() {
    List<Integer> numbers = List.of(1, 2, 3, 4, 5);
    
    // 외부 반복
    int sumOfSquares1 = 0;
    for (int number : numbers) {
        if (number % 2 == 0) {
            sumOfSquares += number * number;
        }
    }

    // 내부 반복
    int sumOfSquares2 = numbers.stream()
            .filter(n -> n % 2 == 0)
            .mapToInt(n -> n * n)
            .sum();
}
```

* 절차적 - 외부 반복(꼰대, 조조형)
  * 개발자가 반복을 제어하고 모든 과정을 명시적으로 제시함
  * 병렬 처리가 어려움
  * 일반적으로 코드가 길고 장황해짐
* 함수형 - 내부 반복(쿨함, 유비형)
  * 전체 흐름은 라이브러리가 제어함(IoC, Inversion of Control)
  * 선언적 프로그램
  * 최적화 용이함 (지연 평가, 단락, 병렬 처리)
  * 병렬처리 용이성 `numbers.stream()` -> `numbers.parallelStream()`

```java
@SuppressWarnings("DataFlowIssue")
@Test
public void testDailyHomework() {

    List<String> dailyHomeworks = List.of("장미", "튤립", "해바라기", "백합", "국화");
    Stream<String> homeworkStream = dailyHomeworks.stream();

    // 첫째날
    String firstHomework = homeworkStream
            .findFirst()
            .map((s) -> s + " 그림")
            .orElse("숙제 끝!");
  
    assertEquals("장미 그림", firstHomework);

    // 둘째날
    assertThrows(IllegalStateException.class, () -> {
        String secondHomework = homeworkStream
                .findFirst()
                .map((s) -> s + " 그림")
                .orElse("숙제 끝!");
        assertEquals("튤립 그림", secondHomework);
    });

    // 아 안되네 다시 둘째날
    String secondHomework = dailyHomeworks.stream()
            .findFirst()
            .map((s) -> s + " 그림")
            .orElse("숙제 끝!");

    assertNotEquals("튤립 그림", secondHomework);
    assertEquals("장미 그림", secondHomework);

    // 아씨 이것도 안되네 다시 둘째날
    String againSecondHomework = dailyHomeworks.stream()
            .skip(1)
            .findFirst()
            .map((s) -> s + " 그림")
            .orElse("숙제 끝!");

    assertEquals("튤립 그림", againSecondHomework);
}
```

* 스트림은 1회용이다. 원소 1개만 사용했다고 해도 스트림은 종료된 상태이기 때문에 다시 쓸 수 없다.
* 스트림에서 조작을 하더라도 원본 컬렉션에는 영향이 없다.

### 스트림 연산

#### 스트림 파이프라인

* 스트림 연산은 스트림 메소드의 체인으로 나타나고 이를 파이프라인(pipeline)이라고도 표현함
  * 스트림이나 Builder 패턴 같이 메소드 체인으로 연결 가능한 API를 플루언트 API(Fluent API)라고도 부름

```java
void main() {
    
    List<Plant> plants = List.of(
            new Plant("장미", Plant.LifeCycle.ANNUAL),
            new Plant("튤립", Plant.LifeCycle.BIENNIAL),
            new Plant("해바라기", Plant.LifeCycle.ANNUAL),
            new Plant("백합", Plant.LifeCycle.PERENNIAL),
            new Plant("국화", Plant.LifeCycle.ANNUAL)
    );

    // SELECT name FROM plant WHERE lifeCycle = 'annual'
    Plant.LifeCycle annual = Plant.LifeCycle.ANNUAL;
    plants.stream()                                     // 스트림 생성
            .filter(p -> p.lifeCycle() == annual)       // 중간 연산(Intermediate Operation)
            .map(Plant::name)                           // 중간 연산(Intermediate Operation)
            .forEach(System.out::println);              // 최종 연산(Terminal Operation)
}
```

* 중간연산은 스트림을 리턴하는 메소드를 말한다.
* 최종연산은 스트림을 리턴하지 않아서 더이상 파이프라인이 이어지지 않음
* 지연 평가(Lazy Evaluation)
  * 미리 중간연산을 수행해서 데이터를 처리해 놓는 것이 아니라 최종 연산이 호출될때 실행이 시작된다.
  * 한건이 스트림에서 추출되고 파이프라인이 순서대로 실행어 최종 연산까지 실행되고,
    다음 원소가 다시 추출되어서 파이프라인이 다시 순서대로 실행되는 식으로 반복된다. (병렬 스트림이 아닌 경우)
    1. `Plant("장미").lifeCycle() == annual` -> `true`
    2. `Plant("장미").name()` -> `"장미"`
    3. `System.out.println("장미")`
    4. `Plant("튤립").lifeCycle() == annual` -> `false`
    5. `Plant("해바라기").lifeCycle() == annual` -> `true`
    6. `Plant("해바라기").name()` -> `"해바라기"`
    7. ...
  * 실제 실행되는 코드는 절차적으로 작성한 것과 크게 차이 없다.

### 스트림 생성

* 주로 `Collection.stream()` 또는 `Stream<T>`의 `static` 메소드로 파이프라인이 시작됨

| SQL           | 스트림 연산                                | 인수 타입                            | 결과 타입       | 설명                                         |
|---------------|---------------------------------------|----------------------------------|-------------|--------------------------------------------|
| `FROM`        | `Collection.stream()`                 |                                  | `Stream<T>` | 컬렉션에서 스트림 생성                               |
|               | `Collection.parallelStream()`         |                                  | `Stream<T>` | 병렬 스트림 생성 (`ForkJoinPool.commonPool()` 사용) |
| `UNION ALL`   | `Stream.concat(a, b)`                 | `Steream<T>`, `Stream<T>`        | `Stream<T>` | 두 스트림을 이어붙임                                |
| `FROM dual`   | `Stream.of(value)`                    | `T`                              | `Stream<T>` | 단일 값 스트림 생성                                |
|               | `Stream.ofNullable(value)`            | `T`                              | `Stream<T>` | 단일 값 스트림 생성 (null이면 빈 스트림, java 9+)        |
| `FROM copy_t` | `Stream.of(values ...)`               | `T...`                           | `Stream<T>` | 가변 인자, 배열로 부터 스트림 생성                       |
| -             | `Stream.empty()`                      |                                  | `Stream<T>` | 빈 스트림 생성                                   |
| -             | `Stream.generate(supplier)`           | `() -> T`                        | `Stream<T>` | 무한 스트림 생성 (지연 평가)                          |
| -             | `Stream.iterate(seed, next)`          | `T, (T, T) -> T`                 | `Stream<T>` | 무한 스트림 생성 (지연 평가)                          |
| -             | `Stream.iterate(seed, hasNext, next)` | `T, (T) -> boolean, (T, T) -> T` | `Stream<T>` | 무한 스트림 생성 (지연 평가, java 9+)                 |

* 모든 스트림 객체는 인터페이스 `BaseStream<T, S extends BaseStream<T, S>>`를 구현한다.
  * 리턴 타입이 `S`로 제네릭화 되어있는 이유는 메소드 체인을 사용하기 위해서 서브 클래스 타입을 리턴해야 하기 때문임

| 메소드                | 인수 타입        | 반환 타입            | 설명                             |
|--------------------|--------------|------------------|--------------------------------|
| `close()`          |              |                  | 스트림을 닫음 (리소스 해제, 직접 쓸 일은 없을 듯) |
| `onClose(handler)` | `() -> void` | `S`              | 스트림이 닫힐 때 실행할 핸들러 등록           |
| `isParallel()`     |              | `boolean`        | 병렬인지 여부                        |
| `sequential()`     |              | `S`              | 순차적으로 변경                       |
| `parallel()`       |              | `S`              | 병렬적으로 변경                       |
| `iterator()`       |              | `Iterator<T>`    | 반복자로 변환                        |
| `spliterator()`    |              | `Spliterator<T>` | 분할 가능한 반복자로 변환 (주로 병렬 처리용)     |

### 중간 연산(Intermediate Operation)

* 새로운 스트림을 반환 (스트림 연산 체인을 이어갈 수 있음)
* MapReduce 패턴의 Map 단계에 해당 - 가장 중요한 연산은 `filter()`와 `map()`임
* 지연 평가(Lazy Evaluation): 최종 연산이 실행되기 전에는 연산이 수행되지 않음
* 상태 있음(stateful) 또는 없음(stateless): 스트림 연산이 중간 상태를 저장하는지 여부
  * 상태를 관리하는 연산은 병렬처리가 어렵거나 불가능하다. (`sorted()`, `distinct()`, `limit()`, `skip()` 등)
  * 병렬 스트림 파이프라인에서 상태 있는 연산이 나타나면 모든 스레드의 결과를 기다렸다가 다시 시작하게 될 수 있다. 
* 단락(short-circit): 스트림을 다 처리하지 않고 끝날 수 있음
* 부작용(Side Effect) 전용 연산: `peek()` 같은 경우 스트림을 건드리지 않고 다른 일을 하는데, 
  이를 함수형 언어에서는 함수 외부에 영향을 주는 부작용이라고 봄.

#### `filter()`류: 원본 스트림과 동일한 타입의 스트림을 리턴함

| SQL            | 스트림 연산                 | 인수 타입            | 결과 타입       | 설명                                                 |
|----------------|------------------------|------------------|-------------|----------------------------------------------------|
| `WHERE`        | `filter(predicate)`    | `(T) -> boolean` | `Stream<T>` | 필요한 것만 골라냄                                         |
|                | `dropWhile(predicate)` | `(T) -> boolean` | `Stream<T>` | 참일 때까지 버림 (short-circit, java 9+)                  |
|                | `takeWhile(predicate)` | `(T) -> boolean` | `Stream<T>` | 거짓이 나오면 나머지는 버림 (short-circit, java 9+)            |
| `DISTINCT`     | `distinct()`           |                  | `Stream<T>` | 중복된 요소 제거 (stateful)                               |
| `SELECT TOP n` | `limit(n)`             | `long`           | `Stream<T>` | 지정된 개수만큼 반환 (stateful, short-circit)               |
| -              | `skip(n)`              | `long`           | `Stream<T>` | 지정된 개수만큼 건너뜀 (stateful)                            |
| `ORDER BY`     | `sorted()`             |                  | `Stream<T>` | 정렬 (stateful)                                      |
|                | `sorted(comparator)`   | `Comparator<T>`  | `Stream<T>` | 커스텀 버전의 정렬 (stateful)                              |
| -              | `peak(action)`         | `(T) -> void`    | `Stream<T>` | 순회하지만 변경하지 않음(side effect, `forEach`의 중간 연산 버전)    |

* `dropWhile()`과 `takeWhile()`은 단락(short-circiting)을 지원하는 버전의 `filter()`인데,
  보통 스트림의 원소가 정렬되어 있을때, 불필요한 처리를 피하기 위해 사용됨
* `peek()`는 보통 디버깅을 위해서 많이 사용됨
  ```java
    void main() {
        plants.stream()
                .filter(p -> p.length() > 3)
                .peek(System.out::println)    // 필터링이 잘되었나?
                .map(p -> p.name() + " (" + p.lifeCycle() + ")")
                .forEach(System.out::println);
    }
  ```

#### `map()`류: 원본 스트림과 다른 타입의 스트림을 리턴함

* 데이터의 스트림에 조작을 가해서 원하는 결과로 점차 만들어가는 중간 과정의 대표격
  * `gather()`는 아직 preview 단계인데 중간 연산계의 스위스 아미 나이프라고 할 수 있는 기능(자세한 설명은 생략한다)

| SQL             | 스트림 연산                 | 인수 타입                      | 결과 타입       | 설명                                                      |
|-----------------|------------------------|----------------------------|-------------|---------------------------------------------------------|
| `SELECT func()` | `<R>map(mapper)`       | `(T) -> R`                 | `Stream<R>` | 다른 형태의 스트림으로 변환                                         |
| `JOIN`          | `<R>flatMap(mapper)`   | `(T) -> Stream<R>`         | `Stream<R>` | 다른 스트림의 스트림으로 변환한 후 평탄화(`concat`) (see below)           |
|                 | `<R>mapMulti(mapper)`  | `(T, Consumer<R>) -> void` | `Stream<R>` | flatter `flatMap()`. 많은 경우 더 효율적임 (see below, java 16+) |
|                 | `<R>gather(gatherer)`  | `Gatherer<T, ?, R>`        | `Stream<R>` | 커스텀 버전 중간 연산 (short-circuit, java 22 preview)           |

* 원시 자료형별 변형

| 기본형          | 스트림 연산                          | 인수 타입                         | 결과 타입            | 설명                                  |
|--------------|---------------------------------|-------------------------------|------------------|-------------------------------------|
| `map()`      | `mapToLong(mapper)`             | `(T) -> long`                 | `LongStream`     | `long` 버전 `map()`                   |
|              | `mapToInt(mapper)`              | `(T) -> int`                  | `IntStream`      | `int` 버전 `map()`                    |
|              | `mapToDouble(mapper)`           | `(T) -> double`               | `DoubleStream`   | `double` 버전 `map()`                 |
|              | `LongStream.mapToObj(mapper)`   | `(long) -> R`                 | `Stream<R>`      | `long` 역변환 버전 `map()`               |
|              | `IntStream.mapToObj(mapper)`    | `(int) -> R`                  | `Stream<R>`      | `int` 역변환 버전 `map()`                |
|              | `DoubleStream.mapToObj(mapper)` | `(double) -> R`               | `Stream<R>`      | `double` 역변환 버전 `map()`             |
|              | `LongStream.boxed()`            |                               | `Stream<Long>`   | `long` 스트림을 객체 스트림으로 변환             |
|              | `IntStream.boxed()`             |                               | `Stream<Int>`    | `int` 스트림을 객체 스트림으로 변환              |
|              | `DoubleStream.boxed()`          |                               | `Stream<Double>` | `double` 스트림을 객체 스트림으로 변환           |
| `flatMap()`  | `flatMapToLong(mapper)`         | `(T) -> LongStream`           | `LongStream`     | `long` 버전 `flatMap()`               |
|              | `flatMapToInt(mapper)`          | `(T) -> IntStream`            | `IntStream`      | `int` 버전 `flatMap()`                |
|              | `flatMapToDouble(mapper)`       | `(T) -> DoubleStream`         | `DoubleStream`   | `double` 버전 `flatMap()`             |
| `mapMulti()` | `mapMultiToLong(mapper)`        | `(T, LongConsumer) -> void`   | `LongStream`     | `long` 버전 `mapMulti()` (java 16+)   |
|              | `mapMultiToInt(mapper)`         | `(T, IntConsumer) -> void`    | `IntStream`      | `int` 버전 `mapMulti()` (java 16+)    |
|              | `mapMultiToDouble(mapper)`      | `(T, DoubleConsumer) -> void` | `DoubleStream`   | `double` 버전 `mapMulti()` (java 16+) |

#### `flatMap()`을 이용한 `JOIN` 연산

* `flatMap()`은 원소를 스트림으로 매핑하는데, 이는 SQL의 `JOIN` 연산으로 결과셋이 뻥튀기되는 것과 유사하다.

```java
class DepartmentDto {
    String id;
    String name;
}
class EmployeeDto {
    String id;
    String name;
    String departmentId;
    Department department;
}
void main() {

    // SELECT E.name || '(' || D.name || ')' employeeName
    // FROM employee E,
    //      department D
    // WHERE E.departmentId = D.id
    // ORDER BY 1

    List<DepartmentDto> departments = employeeService.getAllDepartments();
    List<String> employees = departments.stream()
            .flatMap((depart) ->
                    employeeService.getEmployeesByDepartment(depart).stream())
            .map((empl) ->
                    empl.getName() + " (" + empl.getDepartment().getName() + ")")
            .sorted()
            .toList();
}
```

* `flatMap()`과 동일한 일을 `mapMulti()`로 할 수 있다.

```java
void main() {
    List<DepartmentDto> departments = employeeService.getAllDepartments();
    List<String> employees = departments.stream()
            .<EmployeeDto>mapMulti((depart, consumer) -> {
                    employeeService.getEmployeesByDepartment(depart)
                            .forEach(consumer);
            })
            .map(empl -> empl.getName() + " (" + empl.getDepartment().getName() + ")")
            .sorted()
            .toList();
}
```

* `flatMap()`은 내부적으로 `Stream.concat()`을 사용하여 스트림을 이어붙이기 때문에 다소 비효율적임
* `mapMulti()`의 인자는 `BiConsumer<T, Consumer<R>>`, 즉 `(T, Consumer<T>) -> void`인데 리턴 값이 없어서 타입 추론이 끊어지는 문제가 있다.
  * 따라서 `<EmployeeDto>mapMulti()` 같이 제네릭 타입을 명시적으로 지정을 해야 한다.

### 최종 연산(Terminal Operation)

* 스트림이 아닌 다른 값을 반환 (또는 `void`)
* MapReduce 패턴의 Reduce 단계에 해당 - 가장 중요한 연산은 `reduce()`와 `collect()`임
* 최종연산이 호출 되어야 파이프라인이 실행됨
* 부작용(Side Effect) 전용 연산: `forEach()`류

| SQL                | 스트림 연산                     | 인수 타입                | 결과 타입         | 설명                                    |
|--------------------|----------------------------|----------------------|---------------|---------------------------------------|
| `SELECT TOP 1`     | `findFirst()`              |                      | `Optional<T>` | 첫번째 요소 반환 (short-circit)              |
|                    | `findAny()`                |                      | `Optional<T>` | 병렬 연산용 `findFirst()` (short-circit)   |
| `ALL`              | `allMatch(predicate)`      | `(T) -> boolean`     | `boolean`     | 모든 요소가 조건에 맞는지 (short-circit)         |
|                    | `noneMatch(predicate)`     | `(T) -> boolean`     | `boolean`     | 하나도 조건에 안 맞는지 (short-circit)          |
| `ANY`              | `anyMatch(predicate)`      | `(T) -> boolean`     | `boolean`     | 하나라도 조건에 맞는지 (short-circit)           |
| `COUNT(*)`         | `count()`                  |                      | `long`        | 개수 반환                                 |
| `MAX()`            | `max(comparator)`          | `Comparator<T>`      | `Optional<T>` | 최대값 반환                                |
| `MIN()`            | `min(comparator)`          | `Comparator<T>`      | `Optional<T>` | 최소값 반환                                |
| `SUM(), AVG() ...` | `reduce(accumulator)`      | `(T r, T e) -> T`    | `Optional<T>` | 누산 (다른 버전 有)                          |
| `GROUP BY`         | `<R, A>collect(collector)` | `Collector<T, A, R>` | `R`           | `reduce()`의 커스텀 버전 (다른 버전 有)          |
| -                  | `toList()`                 |                      | `List<T>`     | 리스트로 변환 (java 16+)                    |
| -                  | `toArray()`                |                      | `Object[]`    | 배열로 변환                                |
| -                  | `<R>toArray()`             | `(int) -> R`         | `R[]`         | 배열로 변환 (배열 생성자 지정 `String[]::new`)    |
| -                  | `forEach()`                | `(T) -> void`        |               | 스트림을 순회하며 처리 (side effect)            |
| -                  | `forEachOrdered()`         | `(T) -> void`        |               | 스트림을 순회하며 처리 (side effect, 컬렉션 순서 보장) |

* 원시 자료형별 최종 연산
  * `LongStream`, `IntStream`, `DoubleStream`에는 추가로 통계 연산이 최종 연산으로 제공됨

| SQL     | 스트림 연산                           | 인수 타입 | 결과 타입                   | 설명                                    |
|---------|----------------------------------|-------|-------------------------|---------------------------------------|
| `MIN()` | `LongStream.min()`               |       | `OptionalLong`          | 최소값                                   |
| `MAX()` | `LongStream.max()`               |       | `OptionalLong`          | 최대값                                   |
| `SUM()` | `LongStream.sum()`               |       | `long`                  | 합계                                    |
| `AVG()` | `LongStream.average()`           |       | `OptionalLong`          | 평균                                    |
| -       | `LongStream.summaryStatistics()` |       | `LongSummaryStatistics` | 통계 요약 (min, max, sum, count, average) |

### `reduce()`를 활용한 컬렉션 집계

* `reduce()`는 스트림의 요소를 누적하여 단일 결과를 생성하는데 사용됨
  * `count()`, `min()`, `max()`, `sum()` 등은 `reduce()`를 사용하여 구현된 응용 메소드라고 생각할 수 있음

| 기본형        | 스트림 연산                          | 인수 타입                | 결과 타입         | 설명           |
|------------|---------------------------------|----------------------|---------------|--------------|
| `reduce()` | `reduce(accumulator)`           | `   (T r, T e) -> T` | `Optional<T>` | 단순 누산        |
|            | `reduce(identity, accumulator)` | `T, (T r, T e) -> T` | `T`           | 초기값을 지정하는 버전 |

```java
void main() {
    List<Integer> numbers = List.of(1, 2, 3, 4, 5);

    int sum   = s.stream().reduce((r, e) -> r + e)          .orElse(0);
    int count = s.stream().reduce((r, e) -> ++r)            .orElse(0);
    int min   = s.stream().reduce((r, e) -> (r < e) ? r : e).orElse(0);
    int max   = s.stream().reduce((r, e) -> (r > e) ? r : e).orElse(0);

    System.out.printf("Sum: %d, count: %d, min: %d, max: %d\n", sum, count, min, max);
    // Sum: 15, count: 5, min: 1, max: 5
}
```

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

![넌 방금 내 함정 카드를 발동시켰다](img/trap-card.jpg)

```java
void main() {
    List<Integer> numbers = List.of(5, 4, 3, 2, 1);
  
    int count = s.stream().reduce((r, e) -> ++r).orElse(0);
    int count2 = s.stream().reduce(0, (r, e) -> ++r);
  
    System.out.printf("Count: %d, count2: %d\n", count, count2); // Count: 9, count2: 5
}
```

* `reduce(accumulator)`의 동작 방식
  * 스트림의 첫번째 요소를 결과값의 초기값(identity)으로 사용하고 누산기(accumulator)를 호출하지 않음
    * 스트림에 원소가 하나 뿐이면 그 원소를 `Optional`로 감싸 리턴함
    * 스트림이 비어있으면 `Optional.empty()`를 리턴함
  * 스트림의 두번째 요소부터 `accumulator(첫번째, 두번째)`를 호출하고, 반환값을 다음 누산값으로 삼음.
  * 최종 `accumulator(누산값, 마지막)`의 반환값을 `Optional`로 감싸 리턴함
  * `sum`, `min`, `max`와는 호환되지만, `count`와는 호환되지 않음
* `reduce(identity, accumulator)`의 동작 방식
  * `identity`를 결과값의 초기값으로 사용함
    * 스트림이 비어있으면 `identity`가 리턴됨
  * 스트림의 첫번째 요소부터 `accumulator(identity, 첫번째)`를 호출하고, 반환값을 다음 누산값으로 삼음.
  * 이어서 `accumulator(누산값, 두번째)`를 호출하고, 반환값을 다음 누산값으로 삼음.
  * 최종 `accumulator(누산값, 마지막)`의 반환값을 리턴함
  * `sum`, `count`와는 호환되지만 `min`, `max`와는 호환되지 않음
    * `min`, `max`는 스트림에 존재하는 값이어야 하는데, 상황에 따라서 초기값이 리턴될 수 있음
    * 초기값(0) 보다 모두 크면 `min`은 `0`으로 나옴, 또는 스트림이 비어있어도 초기값이 리턴됨

### 확장된 `reduce()`: `collect()`

| 기본형         | 스트림 연산                                        | 인수 타입                                         | 결과 타입     | 설명                |
|-------------|-----------------------------------------------|-----------------------------------------------|-----------|-------------------|
| `collect()` | `<R>collect(supplier, accumulator, combiner)` | `() -> R, (R r, T e) -> void, (R, R) -> void` | `R`       | 컬렉터 생성을 풀어 쓴 버전   |
|             | `<R, A>collect(collector)`                    | `Collector<T, A, R>`                          | `R`       | 커스텀 컬렉터 객체 지정 버전  |

* `reduce()`가 스트림을 하나의 값(scalar value)로 요약하는 것이라면, `collect()`는 하나의 객체(object)로 요약하는 것.
  * 즉 `reduce(identity, accumulator)`에서 `identity`로 객체를 제공하는 것으로 생각할 수 있음.
* `LongStream` 등의 `summaryStatistics()`와 같은 통계 연산은 `collect()`의 응용 메소드라고 생각할 수 있음

#### `collect()`를 사용한 통계 연산

* 평균(average)은 `reduce()`로 구현할 수 없고 `collect()`를 사용해야 함

```java
class Summary {
    double sum = 0;
    double count = 0;

    public double average() {
        return count == 0 ? 0 : sum / count;
    }
}

void main() {
    List<Double> numbers = List.of(5.0, 4.0, 3.0, 2.0, 1.0);
  
    Summary s = numbers.stream()
            .collect(
                    () -> new Summary(), // supplier
                    (r, e) -> { r.sum += e; r.count++; }, // accumulator
                    (r1, r2) -> { r1.sum += r2.sum; r1.count += r2.count; } // combiner
            );

    System.out.printf("Sum: %.2f, count: %.2f, average: %.2f\n", s.sum, s.count, s.average());
    // Sum: 15.00, count: 5.00, average: 3.00
}
```

* `supplier`: 결과 객체를 생성하는 함수
* `accumulator`: 스트림의 각 요소를 결과 객체에 추가하는 함수
* `combiner`: 병렬 스트림에서 두 결과 객체를 합치는 함수
  * 병렬 스트림에서는 여러 스레드가 각자 `Summary`객체를 가지고 `accumulator`를 호출하게 되고,
    최종적으로 `combiner`를 이용해서 하나로 합쳐짐.

| 기본형         | 스트림 연산                                        | 인수 타입                                         | 결과 타입 | 설명                |
|-------------|-----------------------------------------------|-----------------------------------------------|-------|-------------------|
| `reduce()`  | `<R>reduce(identity, accumulator, combiner)`  | `R,       (R r, T e) -> R,    (R, R) -> R`    | `R`   | 병렬 처리용 `reduce()` |
| `collect()` | `<R>collect(supplier, accumulator, combiner)` | `() -> R, (R r, T e) -> void, (R, R) -> void` | `R`   | 컬렉터 생성을 풀어 쓴 버전   |

* 평균은 `reduce`로는 구할 수 없다고 했지만 사실 가능한 `reduce`버전이 있다.
* 하지만 `<R>reduce(identity, accumulator, combiner)`는 이름은 `reduce`이지만,
  개념상 `collect(supplier, accumulator, combiner)`의 변형이라고 볼 수도 있다.

#### `Collector` 인터페이스

* `Collector` 인터페이스는 `collect(supplier, accumulator, combiner)`의 인수들을 미리 정의해 놓을 때 사용함
  * `collect(supplier, accumulator, combiner)`의 결과 객체(`Summary`)를 임시 계산용 객체(`A`)와 최종 결과 객체(`R`)로 분리
* `Collector<T, A, R>`
  * `T`: 스트림의 요소 타입
  * `A`: 누산을 위한 임시 객체 타입 (보통 내부 로직으로 감춰짐)
  * `R`: 최종 결과 타입

```java
static class Summary {

    private int sum = 0;
    private int count = 0;

    // accumulator용
    // (Summary, Integer) -> void
    public void accept(int value) {
        sum += value;
        count++;
    }

    // combiner용
    // (Summary, Summary) -> Summary
    public Summary combine(Summary other) {
        this.sum += other.sum;
        this.count += other.count;
        return this;
    }

    // finisher용
    // Summary -> Double
    public double average() {
        return (count == 0)? 0.0 : (double)sum / (double)count;
    }
}

static class AverageCollector implements Collector<Integer, Summary, Double> {

    // () -> Summary
    @Override
    public Supplier<Summary> supplier() { return Summary::new; }

    // (Summary, Integer) -> void
    @Override
    public BiConsumer<Summary, Integer> accumulator() { return Summary::accept; }

    // (Summary, Summary) -> Summary
    @Override
    public BinaryOperator<Summary> combiner() { return Summary::combine; }

    // Summary -> Double
    @Override
    public Function<Summary, Double> finisher() { return Summary::average; }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(
            // Collector.Characteristics.IDENTITY_FINISH,  // A, R이 같은 타입이라 finisher가 필요 없을 때 사용
            Collector.Characteristics.UNORDERED,        // 순서가 중요하지 않을 때 사용
            Collector.Characteristics.CONCURRENT        // 병렬 처리 가능할 때 사용
        );
    }
}
void main() {
    List<Integer> numbers = List.of(5, 4, 3, 2, 1);

    // AverageCollector implements Collector<Integer, Summary, Double>
    double average = numbers.stream()
            .collect(new AverageCollector());

    System.out.printf("Average: %.2f\n", average); // Average: 3.00
}
```

* `Collector` 메소드
  * `supplier()`: 임시 객체를 생성하는 함수. `collect(supplier, accumulator, combiner)` 형태의 첫번째 인자를 리턴.
  * `accumulator()`: 스트림의 각 요소를 임시 객체에 추가하는 함수. 두번째 인자를 리턴.
  * `combiner()`: 병렬 스트림에서 두 임시 객체를 합치는 함수. 세번째 인자를 리턴.
  * `finisher()`: 임시 객체를 최종 결과로 변환하는 함수
    * `A`와 `R`이 같은 타입인 경우 `Collector.Characteristics.IDENTITY_FINISH`를 사용하여 생략할 수 있음
  * `characteristics()`: 컬렉터의 특성을 정의하는 집합
    * `Collector.Characteristics.UNORDERED`: 순서가 중요하지 않음을 나타냄
    * `Collector.Characteristics.CONCURRENT`: 병렬 처리 가능함을 나타냄
    * `Collector.Characteristics.IDENTITY_FINISH`: `finisher()`가 필요 없음을 나타냄

#### `Collectors`에 사전 정의된 `Collector` 구현

* `Collectors` 클래스의 `static` 메소드로 많이 사용될법한 컬렉터가
  엄청 많이 정의되어있어서 `Collector`를 새로 만들 필요는 크지 않다.
* `import static java.util.stream.Collectors.*` 로 `Collectors.` 을 생략 가능

##### 집계(aggregation)

| SQL         | Collector                             | 인수 타입                              | 컬렉터 타입                                     |
|-------------|---------------------------------------|------------------------------------|--------------------------------------------|
| `LISTAGG()` | `joining()`                           |                                    | `Collector<CharSequence, ?, String>`       |
|             | `joining(delimiter)`                  | `String`                           | `Collector<CharSequence, ?, String>`       |
|             | `joining(delimiter, prefix, suffix)`  | `String, String, String`           | `Collector<CharSequence, ?, String>`       |
| `COUNT(*)`  | `counting()`                          |                                    | `Collector<T, ?, Long>`                    |
| `SUM()`     | `summingLong(mapper)`                 | `(T) -> long`                      | `Collector<T, ?, Long>`                    |
|             | `summingInt(mapper)`                  | `(T) -> int`                       | `Collector<T, ?, Integer>`                 |
|             | `summingDouble(mapper)`               | `(T) -> double`                    | `Collector<T, ?, Double>`                  |
| `AVG()`     | `averagingLong(mapper)`               | `(T) -> long`                      | `Collector<T, ?, Double>`                  |
|             | `averagingInt(mapper)`                | `(T) -> int`                       | `Collector<T, ?, Double>`                  |
|             | `averagingDouble(mapper)`             | `(T) -> double`                    | `Collector<T, ?, Double>`                  |
| `MIN()`     | `minBy(comparator)`                   | `Comparator<T>`                    | `Collector<T, ?, Optional<T>>`             |
| `MAX()`     | `maxBy(comparator)`                   | `Comparator<T>`                    | `Collector<T, ?, Optional<T>>`             |
| -           | `summarizingLong(mapper)`             | `(T) -> long`                      | `Collector<T, ?, LongSummaryStatistics>`   |
| -           | `summarizingInt(mapper)`              | `(T) -> int`                       | `Collector<T, ?, IntSummaryStatistics>`    |
| -           | `summarizingDouble(mapper)`           | `(T) -> double`                    | `Collector<T, ?, DoubleSummaryStatistics>` |

* 자세한 설명은 생략한다.

##### 컬렉션으로 변환(conversion)

* 파이프라인으로 변경된 스트림을 컬렉션으로 저장하는 용도.
  * `Stream.toList()`는 java 16부터 지원되는 메소드로, 16 이전 버전에서는 `Stream.collect(Collectors.toList())`로 해야 함.

| Collector                                                            | 인수 타입                                      | 컬렉터 타입                                 |
|----------------------------------------------------------------------|--------------------------------------------|----------------------------------------|
| `toCollection(collectionFactory)`                                    | `() -> C`                                  | `Collector<T, ?, C>`                   |
| `toList()`                                                           |                                            | `Collector<T, ?, List<T>>`             |
| `toUnmodifiableList()` (java 10+)                                    |                                            | `Collector<T, ?, List<T>>`             |
| `toSet()`                                                            |                                            | `Collector<T, ?, Set<T>>`              |
| `toUnmodifiableSet()`  (java 10+)                                    |                                            | `Collector<T, ?, Set<T>>`              |
| `toMap(keyMapper, valueMapper)`                                      | `(T) -> K, (T) -> V`                       | `Collector<T, ?, Map<K, V>>`           |
| `toMap(keyMapper, valueMapper, mergeFunction)`                       | `(T) -> K, (T) -> V, (V, V) -> V`          | `Collector<T, ?, Map<K, V>>`           |
| `toMap(keyMapper, valueMapper, mergeFunction, mapFactory)`           | `(T) -> K, (T) -> V, (V, V) -> V, () -> M` | `Collector<T, ?, M>`                   |
| `toUnmodifiableMap(keyMapper, valueMapper)`                          | `(T) -> K, (T) -> V`                       | `Collector<T, ?, Map<K, V>>`           |
| `toUnmodifiableMap(keyMapper, valueMapper, mergeFunction)`           | `(T) -> K, (T) -> V, (V, V) -> V`          | `Collector<T, ?, Map<K, V>>`           |
| `toConcurrentMap(keyMapper, valueMapper)` (java 10+)                 | `(T) -> K, (T) -> V`                       | `Collector<T, ?, ConcurrentMap<K, V>>` |
| `toConcurrentMap(keyMapper, valueMapper, mergeFunction)`             | `(T) -> K, (T) -> V, (V, V) -> V`          | `Collector<T, ?, ConcurrentMap<K, V>>` |
| `toConcurrentMap(keyMapper, valueMapper, mergeFunction, mapFactory)` | `(T) -> K, (T) -> V, (V, V) -> V, () -> M` | `Collector<T, ?, M>`                   |

* `toMap()` 시리즈
  * `toMap(keyMapper, valueMapper)`
    * `keyMapper`로 키를, `valueMapper`로 값을 추출하여 `Map`으로 변환함
    * 만약 하나의 키에 중복된 값이 나오게 되면 `IllegalStateException`이 던져짐
    * 1:1 매핑이 보장되는 경우에만 사용 가능
      ```java
      void main() {
          Map<String, Employee> employeeMap = employees.stream()
                .collect(
                        Collectors.toMap(
                                Employee::getId,    // keyMapper
                                Function.identity() // valueMapper (x) -> x
                        ));
      }
      ```
      * `Function.identity()`는 `x -> x`인데 이를 항등 함수라고 하고, 함수 합성 연산에서 항등원(identity element)의 역할을 한다.
  * `toMap(keyMapper, valueMapper, mergeFunction)`
    * `mergeFunction`을 사용하여 중복된 키의 값을 병합함
      ```java
      void main() {
          Map<String, String> employeesByLastName = employees.stream()
                .collect(
                        Collectors.toMap(
                                Employee::getLastName,  // keyMapper
                                Employee::getFirstName, // valueMapper
                                // mergeFunction
                                (firstName1, firstName2) -> firstName1 + ", " + firstName2
                        ));
      }
      ```
    * `Map`의 중복된 값을 리스트로 병합하고 싶다면 `toMap()` 대신 `groupingBy()`를 사용해야 함

##### 그룹화(grouping)

* 스트림을 `classifier`, `predicate`의 각 리턴값을 키로 하는 `Map`으로 재편한다.
* `downstream`으로 다른 `Collector`를 지정하여 그룹화 후의 각 그룹에 대한 추가 처리를 할 수도 있다.
* `mapFactory`를 지정하면 `Map`의 구현체를 지정할 수 있다. (eg: `TreeMap`)

| SQL        | Collector                                                  | 인수 타입                                   | 컬렉터 타입                                       |
|------------|------------------------------------------------------------|-----------------------------------------|----------------------------------------------|
| `GROUP BY` | `groupingBy(classifier)`                                   | `(T) -> K`                              | `Collector<T, ?, Map<K, List<T>>>`           |
|            | `groupingBy(classifier, downstream)`                       | `(T) -> K, Collector<T, A, D>`          | `Collector<T, ?, Map<K, D>>`                 |
|            | `groupingBy(classifier, mapFactory, downstream)`           | `(T) -> K, () -> M, Collector<T, A, D>` | `Collector<T, ?, Map<K, D>>`                 |
|            | `groupingByConcurrent(classifier)`                         | `(T) -> K`                              | `Collector<T, ?, ConcurrentMap<K, List<T>>>` |
|            | `groupingByConcurrent(classifier, downstream)`             | `(T) -> K, Collector<T, A, D>`          | `Collector<T, ?, ConcurrentMap<K, D>>`       |
|            | `groupingByConcurrent(classifier, mapFactory, downstream)` | `(T) -> K, () -> M, Collector<T, A, D>` | `Collector<T, ?, ConcurrentMap<K, D>>`       |
|            | `partitioningBy(predicate)`                                | `(T) -> boolean`                        | `Collector<T, ?, Map<Boolean, List<T>>>`     |
|            | `partitioningBy(predicate, downstream)`                    | `(T) -> boolean, Collector<T, A, D>`    | `Collector<T, ?, Map<Boolean, D>>`           |

```java
import static java.util.stream.Collectors.*;

void main() {
    List<Employee> employees = getEmployees();

    Map<Department, List<Employee>> employeesByDepartment = employees.stream()
            .collect(
                    groupingBy(
                            Employee::getDepartment,   // classifier
                            TreeMap::new,              // mapFactory
                            toCollection(   // downstream
                                    LinkedList::new))  // collectionFactory
                    );
}
```

##### 스트림 연산(stream operations)

* 스트림 연산과 비슷한 기능을 하는 `Collector`가 제공됨
  * `collect(Collectors.mapping(mapper, ...))`은 `map(mapper).collect(...)`와 유사

| Stream      | Collector                                    | 인수 타입                                    | 컬렉터 타입                         |
|-------------|----------------------------------------------|------------------------------------------|--------------------------------|
| `filter()`  | `filtering(predicate, downstream)` (java 9+) | `(T) -> boolean, Collector<T, A, R>`     | `Collector<T, ?, R>`           |
| `map()`     | `mapping(mapper, downstream)`                | `(T) -> T2, Collector<T2, A, R>`         | `Collector<T, ?, R>`           |
| `flatMap()` | `flatMapping(mapper, downstream)` (java 9+)  | `(T) -> Stream<T2>, Collector<T2, A, R>` | `Collector<T, ?, R>`           |
| `reduce()`  | `reducing(accumulator)`                      | `(T, T) -> T`                            | `Collector<T, ?, Optional<T>>` |
|             | `reducing(identity, accumulator)`            | `T, (T, T) -> T`                         | `Collector<T, ?, T>`           |
|             | `reducing(identity, mapper, accumulator)`    | `T2, (T) -> T2, (T2, T2) -> T2`          | `Collector<T, ?, T2>`          |

* `downstream` 인자를 가지는 다른 `Collector`와 조합하여 체인을 만드는 용도
   ```java
    import static java.util.stream.Collectors.*;
    void main() {
  
        // SELECT D.name, SUM(E.salary) AS totalSalary
        // FROM employee E,
        //      department D
        // WHERE E.departmentId = D.id
        // GROUP BY D.name
  
        List<Employee> employees = getEmployees();
        Map<Department, Long> byDepartment = employees.stream()
                .collect(
                        groupingBy(
                                Employee::getDepartment,     // classifier
                                reducing(
                                        0L,                  // identity
                                        Employee::getSalary, // mapper
                                        Long::sum            // accumulator
                                )
                ));
    }
   ```

##### 기타

| 스트림 연산                                                | 인수 타입                                                     | 컬렉터 타입                |
|-------------------------------------------------------|-----------------------------------------------------------|-----------------------|
| `teeing(downstream1, downstream2, merger)` (java 12+) | `Collector<T, ?, R1>, Collector<T, ?, R2>, (R1, R2) -> R` | `Collector<T, ?, R>`  |
| `collectingAndThen(downstream, finisher)`             | `Collector<T, ?, R>, (R) -> R2`                           | `Collector<T, ?, R2>` |

* `teeing()`은 스트림을 두개로 쪼갰다가 다시 합치는 용도 (see below)
* `collectingAndThen()`은 컬렉터를 래핑하는 용도(Adapter 패턴)
  ```java
  void main() {
      long average = numbers.stream()
              .collect(
                  Collectors.collectingAndThen(
                      new AverageCollector(),       // Collector<Integer, Summary, Double>
                      avg -> (long)Math.round(avg)  // finisher
                  ));
  }
  ```

#### `teeing()`을 이용한 두마리 토끼 잡기

* 최대값과 최소값을 동시에 찾기

```java
import static java.util.stream.Collectors.*;

record MinMax(int min, int max) {}

void main() {
    List<Integer> numbers = List.of(5, 4, 3, 2, 1);

    MinMax result = numbers.stream()
            .collect(
                    teeing(
                            minBy(Comparator.naturalOrder()), // downstream1
                            maxBy(Comparator.naturalOrder()), // downstream2
                            (min, max) -> new MinMax(min.orElse(0), max.orElse(0)) // merger
                    )
            );

    System.out.printf("Min: %d, Max: %d\n", result.min(), result.max());
    // Min: 1, Max: 5
}
```

* 태그별 통계와 텍스트를 동시에 추출하기 

```java
import static java.util.stream.Collectors.*;

record DOMData(Map<String, Long> countMap, Map<String, List<String>> textMap) {}

void main() {
    
    DOMTree dom = getDOMTree();
    DOMData result = dom.allNodes().stream()
        .collect(
            teeing(
                groupingBy(                         // downstream1
                        Node::getTagName,
                        counting()),
                groupingBy(                         // downstream2
                        Node::getTagName,
                        mapping(
                                Node::getTextContent,
                                toList())),
                (countMap, textMap) -> {            // merger
                    return new DOMData(countMap, textMap);
                }
            )
        );
}
```

#### 병렬 처리

```java
public interface Flower {
    String color();
    String name();
}
public class Plant {

    public class FlowerImpl implements Flower {
        private final String pot;
        FlowerImpl(String pot) { this.pot = pot; }

        @Override
        public String color() { return color; }
        @Override
        public String name() { return name + " 꽃"; }
        public String pot() { return pot; }
        @Override
        public String toString() {
            return String.format("%s 화분에서 키운 %s: %s", pot(), name(), color());
        }
    }

    private final String name;
    private final String color;

    Plant(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String name() { return name; }

    public Flower grow(String pot) {
        try {
            // 시뮬레이션을 위해 1-2초간 대기
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 2000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 인터럽트 상태를 다시 설정
            throw new RuntimeException("인터럽트됨: " + e.getMessage(), e);
        }
        return new FlowerImpl(pot);
    }
}

@Test
public void testConcurrency() {

    // 화분이 3개 뿐이라 순차적으로 배분하여 키워야 함
    int pots = 3;

    // 3개의 worker 스레드를 생성
    try (ExecutorService executorService = Executors.newFixedThreadPool(pots)) {

        // 작업 제출
        List<Future<Flower>> futures = plants.stream()
                .map(plant -> executorService.submit(
                        () -> plant.grow(Thread.currentThread().getName())
                    ))
                .toList();

        // Future 객체에서 결과를 가져와 출력
        futures.forEach(future -> {
            try {
                Flower flower = future.get(); // 병렬 작업의 결과를 기다림
                System.out.println(flower);
            }
            catch (Exception e) {
                System.err.print("식물 키우기 실패: " + e.getMessage());
                if (e instanceof InterruptedException ||
                        (e.getCause() != null && e.getCause() instanceof InterruptedException)) {
                    System.err.println(": 작업이 인터럽트되었습니다.");
                }
                else if (e instanceof ExecutionException ||
                        (e.getCause() != null && e.getCause() instanceof ExecutionException)) {
                    System.err.println(": 작업 실행 중 예외가 발생했습니다");
                }
                else {
                    System.err.println();
                }
            }
        });
    }
}
```

* `grow()`의 예외 처리블럭에서 `Thread.currentThread().interrupt()`을 다시 호출하는 이유는
  `InterruptedException`을 `catch`하게 되면 `Thread`의 인터럽트 상태가 해제되기 때문에,
  `ExecutorService` 같은 병렬 처리 라이브러리나 프레임워크가 인터럽트가 발생했는지 알 수 없음.
* 위 예시에서 `RuntimeException`으로 감싸서 던졌기 때문에 `ExecutorService`는
  인터럽트로 인한 예외인지 아니면 다른 예외인지 구분하지 못할 수 있음
* 이런 스레드 관리기능에서 인터럽트에 대한 추가적인 처리가 필요할 수도 있으므로
  `InterruptedException`을 `catch`했다가 다시 던지려면,
  반드시 `interrupt()`를 호출하여 인터럽트 상태를 설정한 후 예외를 다시 던져야 한다.  
* 예외를 감쌀 때도 `new RuntimeExcpetion(e)`처럼 `cause`를 반드시 설정해줘야 한다. 


### One More Thing...

![One More Thing...](img/one-more-thing.jpg)

* `java.util.Optional<T>`는 `null`일 수도 있는 값을 담고 있는 컨테이너 객체인데,
  원소가 최대 1개인 스트림으로 생각할 수 있다.

### `Optional` 생성

* `static` 팩토리 메소드를 이용하여 생성한다.

| 팩토리 메소드             | 인수 타입 | 결과 타입         | 설명                       |
|---------------------|-------|---------------|--------------------------|
| `empty()`           |       | `Optional<T>` | 빈 `Optional`             |
| `of(value)`         | `T`   | `Optional<T>` | 값이 있는 `Optional`         |
| `ofNullable(value)` | `T`   | `Optional<T>` | `null`일 수도 있는 `Optional` |

#### 중간 연산

| 연산                  | 인수 타입               | 결과 타입        |
|---------------------|---------------------|--------------|
| `stream()`          |                     | `Stream<T>`  |
| `filter(predicate)` | `(T) -> boolean`    | `Stream<T>`  |
| `map(mapper)`       | `(T) -> T2`         | `Stream<T2>` |
| `flatMap(mapper)`   | `(T) -> Stream<T2>` | `Stream<T2>` |

* 하스켈의 `Maybe` 모나드
  * `Maybe`는 Java의 `Optional`의 원형이 되는 하스켈의 내장 객체인데 아래와 같이 정의되어있다.
    ```haskell
    data Maybe a = Nothing | Just a
    ```
  * 호출 체인(모나딕 연산)의 중간에 하나라도 `Nothing`이 나오면 `Nothing`이 반환됨

```haskell
getCityName :: Int -> Maybe String
getCityName userId = do
    userName <- findUser userId
    address  <- findAddress userName
    city     <- findCity address
    return city
```

* Java에서는 `Optional.flatMap()`을 사용하여 유사한 방식을 구현할 수 있음
  * 호출 체인의 중간에 하나라도 `Optional.empty()`가 나오면 최종 결과도 `Optional.empty()`가 됨

```java
Optional<String> getCityName(int userId) {
    return findUser(userId)                     // Optional<User>
        .flatMap(user -> findAddress(user))     // Optional<Address>
        .flatMap(address -> findCity(address)); // Optional<String>
}
```

#### 최종 연산(`null` 값 검사 및 처리)

| 연산                                     | 인수 타입                       | 결과 타입                                 |
|----------------------------------------|-----------------------------|---------------------------------------|
| `isEmpty()`                            |                             | `boolean`                             |
| `isPresent()`                          |                             | `boolean`                             |
| `get()`                                |                             | `T` or throw `NoSuchElementException` |
| `orElseThrow()`                        |                             | `T` or throw `NoSuchElementException` |
| `orElseThrow(supplier)`                | `() -> X extends Throwable` | `T` or throw `X`                      |
| `orElse(other)`                        | `T`                         | `T`                                   |
| `orElseGet(supplier)`                  | `() -> T`                   | `T`                                   |
| `ifPresent(action)`                    | `(T) -> void`               |                                       |
| `ifPresentOrElse(action, emptyAction)` | `(T) -> void, () -> void`   |                                       |

* 그밖의 자세한 설명은 생략한다

## 그럼 람다가 짱인가요?

![럭키 짱](img/lucky-zzang.jpg)

### QuickSort 성능 비교

테스트 비교군 정의

* Java
  * `array`: `int[]` 사용 절차적 프로그램
  * `list`: `List<Integer>` 사용 절차적 프로그램 
  * `lambda`: `List<Integer>.stream()` 사용 함수형 프로그램
* Kotlin
  * `array`: `IntArray` 사용 절차적 프로그램
  * `list`: `List<Int>` 사용 절차적 프로그램
  * `lambda`: `List<Int>` 사용 함수형 프로그램

> 단위: `java array` 대비 비율

| lang   | method | 3      | 10   | 100  | 1k   | 10k  | 100k | 1M   |
|--------|--------|--------|------|------|------|------|------|------|
| java   | array  | 1.0    | 1.0  | 1.0  | 1.0  | 1.0  | 1.0  | 1.0  |
| java   | list   | 2.1    | 3.5  | 4.1  | 2.7  | 3.3  | 2.7  | 4.3  |
| java   | lambda | 22.4   | 55.6 | 31.3 | 22.2 | 33.0 | 20.0 | 13.0 |
| kotlin | array  | 2579.4 | 0.8  | 0.8  | 1.2  | 1.2  | 1.0  | 1.0  |
| kotlin | list   | 998.7  | 2.2  | 2.2  | 2.6  | 3.2  | 2.6  | 4.3  |
| kotlin | lambda | 1642.3 | 24.1 | 14.0 | 14.3 | 14.9 | 13.5 | 10.9 |

<details>
<summary>ms 단위 보기</summary>

| lang   | method | 3      | 10    | 100   | 1k    | 10k    | 100k    | 1M      |
|--------|--------|--------|-------|-------|-------|--------|---------|---------|
| java   | array  | 0.005  | 0.005 | 0.050 | 0.323 | 0.993  | 5.998   | 72.889  |
| java   | list   | 0.010  | 0.017 | 0.206 | 0.858 | 3.272  | 16.096  | 315.650 |
| java   | lambda | 0.110  | 0.278 | 1.568 | 7.166 | 32.749 | 119.956 | 946.153 |
| kotlin | array  | 12.639 | 0.004 | 0.039 | 0.374 | 1.231  | 5.893   | 71.227  |
| kotlin | list   | 4.894  | 0.011 | 0.109 | 0.847 | 3.209  | 15.866  | 316.333 |
| kotlin | lambda | 8.047  | 0.121 | 0.702 | 4.631 | 14.806 | 81.056  | 792.388 |

</details>

* 일반적인 성능에 대한 커뮤니티 의견(Kotlin >= Java)이 맞는 것으로 보임
  * 특히 서버 API에서는 주로 100건 이하일텐데 이 경우는 분명하게 Kotlin이 빠름
  * 람다의 경우 Kotlin이 Java보다 확실히 빠름
* Kotlin 3건 대상의 경우 매우 느린데, Java에서 Kotlin 코드 실행시 초기화 작업(jit 컴파일 같은)이 필요한 것으로 보임
  * 반대로 Kotlin에서 Java 코드 실행시에는 초기 오버헤드가 훨씬 덜한 것으로 보임
    <details>
      <summary>숫자 보기</summary>

    >   테스트 실행시마다 랜덤수로 배열을 생성하므로 java에서 실행한 결과와 1:1 비교는 불가능함

      | lang   | method | 3     | 10   | 100  | 1k   | 10k  | 100k  | 1M   |
      |--------|--------|-------|------|------|------|------|-------|------|
      | java   | array  | 1.0   | 1.0  | 1.0  | 1.0  | 1.0  | 1.0   | 1.0  |
      | java   | list   | 2.9   | 3.3  | 3.3  | 2.1  | 2.8  | 2.6   | 4.2  |
      | java   | lambda | 227.6 | 55.2 | 34.1 | 21.7 | 24.7 | 19.1  | 13.7 |
      | kotlin | array  | 0.7   | 0.9  | 0.7  | 0.9  | 1.0  | 1.0   | 1.0  |
      | kotlin | list   | 1.7   | 3.1  | 1.8  | 2.1  | 2.7  | 3.1   | 4.2  |
      | kotlin | lambda | 7.2   | 23.5 | 10.6 | 11.4 | 12.0 | 10.6  | 11.0 |
    
      | lang   | method | 3     | 10    | 100   | 1k    | 10k    | 100k    | 1M      |
      |--------|--------|-------|-------|-------|-------|--------|---------|---------|
      | java   | array  | 0.006 | 0.003 | 0.060 | 0.325 | 1.183  | 5.947   | 69.891  |
      | java   | list   | 0.018 | 0.011 | 0.199 | 0.670 | 3.288  | 15.225  | 294.623 |
      | java   | lambda | 1.389 | 0.177 | 2.030 | 7.040 | 29.266 | 113.410 | 958.802 |
      | kotlin | array  | 0.004 | 0.003 | 0.043 | 0.306 | 1.156  | 6.237   | 69.246  |
      | kotlin | list   | 0.010 | 0.010 | 0.106 | 0.672 | 3.147  | 18.153  | 295.639 |
      | kotlin | lambda | 0.044 | 0.075 | 0.630 | 3.710 | 14.202 | 63.236  | 770.101 |

    </details>

### 꼬리 호출 최적화(Tail Call Optimization)

* 꼬리 호출(Tail Call)
  * 함수의 마지막 문장이 다른 함수를 호출하는 경우를 말함.
  * 꼬리 호출인 경우
    ```javascript
    function foo(n) {
      // ... 어떤 연산 ...
      return bar(n - 1); // bar의 결과가 foo의 최종 결과가 됨
    }
    ```
  * 꼬리 호출이 아닌 경우
    ```javascript
    function baz(n) {
        // ... 어떤 연산 ...
        return 1 + qux(n - 1); // qux 호출 후 덧셈 연산이 추가로 있음
    }
    ```
* 꼬리 호출 최적화(TCO, Tail Call Optimization)
  * 꼬리 호출인 경우 새로운 콜 스택 프레임을 얹는 것이 아니라 현재 스택 프레임을 덮어쓰도록 함
  * 재귀호출 등으로 콜 스택이 많이 쌓여도 스택 오버플로우(Stack Overflow) 위험이 줄어든다.
  * 함수형 프로그래밍 언어(Lisp, Scheme, Haskell, Scala, Erlang)들은 언어 차원에서 지원
  * JavaScript, Scala, Kotlin 등에서도 부분적으로 지원한다.
* Java의 경우 TCO를 지원하지 않음
  * 이는 Java의 콜 스택이 언어의 다른 메커니즘들과 깊은 관련이 있기 때문이다.
    (예외 처리 Stack trace, `SecurityManager.checkMemberAccess()` 등 보안 관련 기능)
  * Java는 TCO는 지원하지 않지만 가상 스레드(Virtual Threads)를 통해서 스택 오버플로우 위험을 쪼금은 줄일 수 있음(Java 21)
    <details>
    <summary>가상 스레드 예시</summary>

    ```java
    public class Factorial {
        public static long factorialDebug(int n) {
            if (n < 0)
                throw new IllegalArgumentException("Factorial is not defined for negative numbers.");
            if (n == 0 || n == 1)
                return 1;
            // 스택 트레이스를 확인하기 위해 예외를 던짐
            if (n == 2)
                throw new RuntimeException("for debug");
            return n * factorialDebug(n - 1);
        }
    }
    @Test
    public void testCheckStackTrace() {
        try {
            Factorial.factorialDebug(5);
        }
        catch (RuntimeException e) {
            System.out.println("Stack trace for testCheckStackTrace:");
            e.printStackTrace();
        }
    }
    ```

    결과:
    ```
    Stack trace for testCheckStackTrace:
    java.lang.RuntimeException: for debug
        at lambdaspecial.java.Factorial.factorialDebug(Factorial.java:26)
        at lambdaspecial.java.Factorial.factorialDebug(Factorial.java:28)
        at lambdaspecial.java.Factorial.factorialDebug(Factorial.java:28)
        at lambdaspecial.java.Factorial.factorialDebug(Factorial.java:28)
        at lambdaspecial.java.FactorialTest.testCheckStackTrace(FactorialTest.java:20)
        at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
        at java.base/java.lang.reflect.Method.invoke(Method.java:580)
        at org.junit.platform.commons.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:766)
        at org.junit.jupiter.engine.execution.MethodInvocation.proceed(MethodInvocation.java:60)
        at org.junit.jupiter.engine.execution.InvocationInterceptorChain$ValidatingInvocation.proceed(InvocationInterceptorChain.java:131)
        at org.junit.jupiter.engine.extension.TimeoutExtension.intercept(TimeoutExtension.java:156)
        at org.junit.jupiter.engine.extension.TimeoutExtension.interceptTestableMethod(TimeoutExtension.java:147)
        at org.junit.jupiter.engine.extension.TimeoutExtension.interceptTestMethod(TimeoutExtension.java:86)
        at org.junit.jupiter.engine.execution.InterceptingExecutableInvoker$ReflectiveInterceptorCall.lambda$ofVoidMethod$0(InterceptingExecutableInvoker.java:103)
        at org.junit.jupiter.engine.execution.InterceptingExecutableInvoker.lambda$invoke$0(InterceptingExecutableInvoker.java:93)
        at org.junit.jupiter.engine.execution.InvocationInterceptorChain$InterceptedInvocation.proceed(InvocationInterceptorChain.java:106)
        at org.junit.jupiter.engine.execution.InvocationInterceptorChain.proceed(InvocationInterceptorChain.java:64)
        at org.junit.jupiter.engine.execution.InvocationInterceptorChain.chainAndInvoke(InvocationInterceptorChain.java:45)
        at org.junit.jupiter.engine.execution.InvocationInterceptorChain.invoke(InvocationInterceptorChain.java:37)
        at org.junit.jupiter.engine.execution.InterceptingExecutableInvoker.invoke(InterceptingExecutableInvoker.java:92)
        at org.junit.jupiter.engine.execution.InterceptingExecutableInvoker.invoke(InterceptingExecutableInvoker.java:86)
        at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.lambda$invokeTestMethod$8(TestMethodTestDescriptor.java:217)
        at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
        at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.invokeTestMethod(TestMethodTestDescriptor.java:213)
        at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.execute(TestMethodTestDescriptor.java:138)
        at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.execute(TestMethodTestDescriptor.java:68)
        at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$6(NodeTestTask.java:156)
        at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
        at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:146)
        at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137)
        at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$9(NodeTestTask.java:144)
        at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
        at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:143)
        at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:100)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1597)
        at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:41)
        at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$6(NodeTestTask.java:160)
        at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
        at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:146)
        at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137)
        at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$9(NodeTestTask.java:144)
        at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
        at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:143)
        at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:100)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1597)
        at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:41)
        at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$6(NodeTestTask.java:160)
        at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
        at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:146)
        at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137)
        at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$9(NodeTestTask.java:144)
        at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
        at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:143)
        at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:100)
        at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.submit(SameThreadHierarchicalTestExecutorService.java:35)
        at org.junit.platform.engine.support.hierarchical.HierarchicalTestExecutor.execute(HierarchicalTestExecutor.java:57)
        at org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine.execute(HierarchicalTestEngine.java:54)
        at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:198)
        at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:169)
        at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:93)
        at org.junit.platform.launcher.core.EngineExecutionOrchestrator.lambda$execute$0(EngineExecutionOrchestrator.java:58)
        at org.junit.platform.launcher.core.EngineExecutionOrchestrator.withInterceptedStreams(EngineExecutionOrchestrator.java:141)
        at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:57)
        at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:103)
        at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:85)
        at org.junit.platform.launcher.core.DelegatingLauncher.execute(DelegatingLauncher.java:47)
        at org.gradle.api.internal.tasks.testing.junitplatform.JUnitPlatformTestClassProcessor$CollectAllTestClassesExecutor.processAllTestClasses(JUnitPlatformTestClassProcessor.java:124)
        at org.gradle.api.internal.tasks.testing.junitplatform.JUnitPlatformTestClassProcessor$CollectAllTestClassesExecutor.access$000(JUnitPlatformTestClassProcessor.java:99)
        at org.gradle.api.internal.tasks.testing.junitplatform.JUnitPlatformTestClassProcessor.stop(JUnitPlatformTestClassProcessor.java:94)
        at org.gradle.api.internal.tasks.testing.SuiteTestClassProcessor.stop(SuiteTestClassProcessor.java:63)
        at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
        at java.base/java.lang.reflect.Method.invoke(Method.java:580)
        at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:36)
        at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)
        at org.gradle.internal.dispatch.ContextClassLoaderDispatch.dispatch(ContextClassLoaderDispatch.java:33)
        at org.gradle.internal.dispatch.ProxyDispatchAdapter$DispatchingInvocationHandler.invoke(ProxyDispatchAdapter.java:92)
        at jdk.proxy1/jdk.proxy1.$Proxy4.stop(Unknown Source)
        at org.gradle.api.internal.tasks.testing.worker.TestWorker$3.run(TestWorker.java:200)
        at org.gradle.api.internal.tasks.testing.worker.TestWorker.executeAndMaintainThreadName(TestWorker.java:132)
        at org.gradle.api.internal.tasks.testing.worker.TestWorker.execute(TestWorker.java:103)
        at org.gradle.api.internal.tasks.testing.worker.TestWorker.execute(TestWorker.java:63)
        at org.gradle.process.internal.worker.child.ActionExecutionWorker.execute(ActionExecutionWorker.java:56)
        at org.gradle.process.internal.worker.child.SystemApplicationClassLoaderWorker.call(SystemApplicationClassLoaderWorker.java:121)
        at org.gradle.process.internal.worker.child.SystemApplicationClassLoaderWorker.call(SystemApplicationClassLoaderWorker.java:71)
        at worker.org.gradle.process.internal.worker.GradleWorkerMain.run(GradleWorkerMain.java:69)
        at worker.org.gradle.process.internal.worker.GradleWorkerMain.main(GradleWorkerMain.java:74)
    ```

    가상 스레드를 사용하여 콜 스택을 분리하면
    ```java
    @Test
    public void testCheckVirtualThreadStackTrace() {
        ValueHolder holder = new ValueHolder();
        Thread virtualThread = Thread.ofVirtual().start(() -> {
            try {
                holder.value = Factorial.factorialDebug(5);
            }
            catch (RuntimeException e) {
                System.out.println("Stack trace for testCheckVirtualThreadStackTrace:");
                e.printStackTrace();
            }
        });
        try {
            virtualThread.join();
        }
        catch (InterruptedException e) {
            fail("Virtual thread was interrupted");
        }
    }
    ```

    결과:
    ```
    Stack trace for testCheckVirtualThreadStackTrace:
    java.lang.RuntimeException: for debug
        at lambdaspecial.java.Factorial.factorialDebug(Factorial.java:26)
        at lambdaspecial.java.Factorial.factorialDebug(Factorial.java:28)
        at lambdaspecial.java.Factorial.factorialDebug(Factorial.java:28)
        at lambdaspecial.java.Factorial.factorialDebug(Factorial.java:28)
        at lambdaspecial.java.FactorialTest.lambda$testCheckVirtualThreadStackTrace$1(FactorialTest.java:56)
        at java.base/java.lang.VirtualThread.run(VirtualThread.java:329)
    ```
    </details>
  * Scala, Kotlin은 Java와 같은 JVM에서 실행되지만 TCO를 지원하는데,
    컴파일러 수준에서 지원하는 것이고 함수형 언어들과 같은 본격적인 지원은 아니다.
    * 예를들어 Kotlin의 경우 기본적으로 TCO를 지원하지 않지만, 메소드 선언에 `tailrec` 키워드를 주면
      컴파일러가 꼬리 호출을 반복문처럼 바꾸어 컴파일 한다.

## 결론: 3줄 요약

* Stream API와 람다식은 자바를 고전 자바와 모던 자바로 나누고 있다.
* 하지만 함수형 프로그래밍에 심취하여 알고리즘을 함수형으로 구현하는 개발자가 있다면 혼쭐 내주자.
* 그래도 람다식이 눈에 안들어온다면 성능과 짬밥을 무기로 찍어 누르자.
