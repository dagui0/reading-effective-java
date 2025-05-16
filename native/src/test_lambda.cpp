#include <iostream>

struct Foo {
    int value;
};

// 어떤 함수 안에서:
Foo& makeRef() {
    static Foo foo{42};  // static이라서 함수 종료 후에도 생존
    return foo;
}

auto someLambda() {
    Foo& ref = makeRef(); // ref는 'static Foo'를 참조
    // 람다에서 ref를 '값 캡처'한다고 가정
    return [ref]() {
        // 이 내부에서는 'ref'라는 참조를 복사한 상태
        // 결국 '원본 Foo'에 접근 가능
        std::cout << ref.value << std::endl;
    };
}

auto someLambda2() {
    Foo foo{100};
    Foo& ref = foo;
    // foo는 함수 끝나면 소멸
    // 람다에서 ref를 값 캡처
    return [ref]() {
        // ref는 이미 소멸된 foo를 참조하는 '잘못된' 참조
        std::cout << ref.value << std::endl; // UB!
    };
}

int main() {
    auto f = someLambda();
    // 함수 someLambda()가 끝났지만 'static Foo'는 살아 있음
    f(); // OK. 42 출력

    auto f2 = someLambda2();
    f2(); // UB! ref는 소멸된 foo를 참조

    return 0;
}
