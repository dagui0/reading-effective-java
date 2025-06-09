package effectivejava.chapter07.item43;

import java.io.EOFException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLTransientException;
import java.util.List;

/// The function type of an intersection type
///    that induces a notional functional interface
/// is the function type of the notional functional interface.
/// 명목상의 함수형 인터페이스를 유도하는 교차 타입의 함수형 타입은
/// 명목상의 함수형 인터페이스의 함수형 타입이다.

interface X { void m() throws IOException; }
interface Y { void m() throws EOFException; }
interface Z { void m() throws ClassNotFoundException; }

/// () -> void throws EOFException
@FunctionalInterface
interface XY extends X, Y {
}

/// () -> void (throws nothing)
@FunctionalInterface
interface XYZ extends X, Y, Z {
}

interface A {
    List<String> foo(List<String> arg) throws IOException, SQLTransientException;
}
interface B {
    List<String> foo(List<String> arg) throws EOFException, SQLException, TimoutException;
}
@SuppressWarnings("rawtypes")
interface C {
    List foo(List arg) throws Exception;
}

/// (List<String>) -> List<String> throws EOFException, SQLTransientException
@FunctionalInterface
interface D extends A, B {
}

/// (List)->List throws EOFException, SQLTransientException
// @FunctionalInterface // 컴파일 에러: 재정의하지 않는 추상 메서드 여러 개가 E에서 발견되었습니다
interface E extends A, B, C {}
