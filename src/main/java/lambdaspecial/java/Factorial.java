package lambdaspecial.java;

public class Factorial {

    public static long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers.");
        }
        if (n == 0 || n == 1) {
            return 1;
        }
        // 이 구현은 꼬리 재귀가 아닙니다.
        return n * factorial(n - 1);
    }

    public static long factorialDebug(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers.");
        }
        if (n == 0 || n == 1) {
            return 1;
        }

        // 스택 트레이스를 확인하기 위해 예외를 던짐
        if (n == 2)
            throw new RuntimeException("for debug");

        return n * factorialDebug(n - 1);
    }
}
