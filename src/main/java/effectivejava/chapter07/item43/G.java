package effectivejava.chapter07.item43;

import java.io.IOException;

/// > The function type of a functional interface is defined nondeterministically: while the signatures in M are "the same", they may be syntactically different (HashMap.Entry and Map.Entry, for example); the return type may be a subtype of every other return type, but there may be other return types that are also subtypes (List<?> and List<? extends Object>, for example); and the order of thrown types is unspecified. These distinctions are subtle, but they can sometimes be important. However, function types are not used in the Java programming language in such a way that the nondeterminism matters. Note that the return type and throws clause of a "most specific method" are also defined nondeterministically when there are multiple abstract methods (§15.12.2.5).
///
/// > When a generic functional interface is parameterized by wildcards, there are many different instantiations that could satisfy the wildcard and produce different function types. For example, each of Predicate<Integer> (function type Integer -> boolean), Predicate<Number> (function type Number -> boolean), and Predicate<Object> (function type Object -> boolean) is a Predicate<? super Integer>. Sometimes, it is possible to known from the context, such as the parameter types of a lambda expression, which function type is intended (§15.27.3). Other times, it is necessary to pick one; in these circumstances, the bounds are used. (This simple strategy cannot guarantee that the resulting type will satisfy certain complex bounds, so not all complex cases are supported.)
/// >
/// > 함수형 인터페이스의 함수 유형은 비결정론적으로 정의됩니다. M의 시그니처는 "동일"하지만 구문적으로 다를 수 있습니다(예: HashMap.Entry와 Map.Entry). 반환 유형은 다른 모든 반환 유형의 하위 유형일 수 있지만, 하위 유형인 다른 반환 유형이 있을 수 있습니다(예: List<?>와 List<? extends Object>). throw되는 유형의 순서는 지정되지 않습니다. 이러한 구분은 미묘하지만 때로는 중요할 수 있습니다. 그러나 Java 프로그래밍 언어에서는 함수 유형이 비결정론적 특성을 갖는 방식으로 사용되지 않습니다. "가장 구체적인 메서드"의 반환 유형과 throws 절도 여러 추상 메서드가 있는 경우 비결정론적으로 정의됩니다(§15.12.2.5).
/// >
/// > 제네릭 함수형 인터페이스가 와일드카드로 매개변수화되는 경우, 와일드카드를 충족하고 다양한 함수 유형을 생성할 수 있는 다양한 인스턴스화가 존재합니다. 예를 들어, Predicate<Integer> (함수 유형 Integer -> boolean), Predicate<Number> (함수 유형 Number -> boolean), 그리고 Predicate<Object> (함수 유형 Object -> boolean)는 모두 Predicate<?super Integer>입니다. 경우에 따라 람다 표현식의 매개변수 유형과 같이 문맥을 통해 어떤 함수 유형이 의도되었는지 알 수 있습니다(§15.27.3). 다른 경우에는 하나를 선택해야 하며, 이러한 상황에서는 경계가 사용됩니다. (이 간단한 전략은 결과 유형이 특정 복소 경계를 충족한다고 보장할 수 없으므로 모든 복소 경계가 지원되는 것은 아닙니다.)

interface G1 {
    <E extends Exception> Object m() throws E;
}

interface G2 {
    <F extends Exception> String m() throws Exception;
}

/// <F extends Exception> () -> String throws F
@FunctionalInterface
interface G extends G1, G2  {}

class MyStringUtils {
    @SuppressWarnings({"unchecked", "SingleStatementInBlock", "ConstantValue"})
    public static <F extends Exception> String helloWorld() throws F {
        if (false) {
            throw (F) new RuntimeException("Generic exception example");
        }
        return "Hello, World!";
    }
}

class UsingG<F extends Exception> {

    private final Class<F> exType;
    public UsingG(Class<F> exType) {
        this.exType = exType;
    }

    @SuppressWarnings("unchecked")
    public void usingGFuncMethod(G func) throws F {
        try {
            func.m();
        }
        catch (Exception e) {
            if (exType.isInstance(e)) { // e instanceof F
                throw (F) e; // 예외를 다시 던짐
            } else {
                throw new RuntimeException("Unknown exception", e);
            }
        }
    }

    public void doSomething() {
        try {
            //usingGFuncMethod(() -> "Hello, World!"); // 컴파일 에러: 타깃 메서드는 제네릭입니다
            usingGFuncMethod(MyStringUtils::helloWorld);
        }
        catch (Exception e) {
            if (exType.isInstance(e)) { // e instanceof F
                // F 예외 처리
            } else {
                System.err.println("unknown exception: " + e);
            }
        }
    }
}
