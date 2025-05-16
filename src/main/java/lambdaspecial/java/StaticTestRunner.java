package lambdaspecial.java;

import effectivejava.chapter06.item39.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class StaticTestRunner {

    enum TestType {
        NORMAL_TEST(StaticTest.class),
        THROW_TEST(StaticThrowTest.class),
        EXCEPTION_TEST(StaticExceptionTest.class);

        private final Class<? extends Annotation> annotationClass;

        TestType(Class<? extends Annotation> annotationClass) {
            this.annotationClass = annotationClass;
        }

        public Class<? extends Annotation> annotationClass() {
            return annotationClass;
        }

        public static TestType getTestType(Method m) {
            if (m.isAnnotationPresent(StaticTest.class)) {
                return NORMAL_TEST;
            }
            else if (m.isAnnotationPresent(StaticThrowTest.class)) {
                return THROW_TEST;
            }
            else if (m.isAnnotationPresent(StaticExceptionTest.class)) {
                return EXCEPTION_TEST;
            }
            else {
                return null;
            }
        }
    }

    static record TestResult(String testCase,
                             TestType testType,
                             boolean passed,
                             Throwable exception) {

        public static TestResult success(String testCase, TestType testType) {
            return new TestResult(testCase, testType, true, null);
        }

        public static TestResult success(String testCase, TestType testType, Throwable exception) {
            return new TestResult(testCase, testType, true, exception);
        }

        public static TestResult failure(String testCase, TestType testType, Throwable exception) {
            return new TestResult(testCase, testType, false, exception);
        }
    }

    public void runTest(Class<? extends effectivejava.chapter06.item39.StaticTestRunner> testClass) throws Exception {

        try {
            List<TestResult> results = Arrays.stream(testClass.getDeclaredMethods())
                    .filter(m -> Modifier.isStatic(m.getModifiers()) && m.getParameterCount() == 0)
                    .map(m -> {
                        TestType testType = TestType.getTestType(m);
                        if (testType == null)
                            return null;

                        return switch (testType) {
                            case NORMAL_TEST -> invokeStaticTestMethod(m);
                            case THROW_TEST -> invokeStaticThrowTestMethod(m);
                            case EXCEPTION_TEST -> invokeStaticExceptionTestMethod(m);
                        };
                    })
                    .filter(Objects::nonNull)
                    .peek(result -> {
                        System.out.printf("%s: %s%s%n", result.testCase, (result.passed)? "passed" : "failed",
                                (result.exception != null)?
                                        ": " + result.exception + " throwed" + ((result.passed)? "(expected)": "")
                                        : "");
                    })
                    .toList();

            int passed = (int) results.stream().filter(r -> r.passed).count();
            int failed = results.size() - passed;
            System.out.printf("%nPassed: %d, Failed: %d%n", passed, failed);
        }
        catch (Throwable e) {
            // unexpected exception
            System.out.println(testClass + " test stopped: " + e);
            throw e;
        }
    }

    /**
     * {@link StaticTest} 애너테이션이 붙은 테스트 메서드를 실행한다.
     * @param m 실행할 테스트 메서드
     * @return 테스트 결과
     * @throws RuntimeException 실행 오류 발생시 테스트를 중단한다.
     */
    private TestResult invokeStaticTestMethod(Method m) {
        try {
            m.invoke(null);

            // test success
            return TestResult.success(m.getName(), TestType.NORMAL_TEST);
        }
        catch (InvocationTargetException e) {
            // test failed
            System.out.println(m + " failed: " + e.getCause());
            return TestResult.failure(m.getName(), TestType.NORMAL_TEST, e.getCause());
        }
        catch (Throwable e) {
            throw new RuntimeException("unexpected exception: " + e, e);
        }
    }

    /**
     * {@link StaticThrowTest} 애너테이션이 붙은 테스트 메서드를 실행한다.
     * @param m 실행할 테스트 메서드
     * @return 테스트 결과
     * @throws RuntimeException 실행 오류 발생시 테스트를 중단한다.
     */
    private TestResult invokeStaticThrowTestMethod(Method m) {
        try {
            m.invoke(null);

            // test failed
            return TestResult.failure(m.getName(), TestType.THROW_TEST, null);
        }
        catch (InvocationTargetException e) {
            // test success
            if (Arrays.stream(m.getAnnotation(StaticThrowTest.class).value())
                    .anyMatch(exType -> exType.isInstance(e.getCause()))) {
                return TestResult.success(m.getName(), TestType.THROW_TEST, e.getCause());
            }
            // test failed
            else {
                return TestResult.failure(m.getName(), TestType.THROW_TEST, e.getCause());
            }
        }
        catch (Throwable e) {
            throw new RuntimeException("unexpected exception: " + e, e);
        }
    }

    /**
     * {@link StaticExceptionTest} 애너테이션이 붙은 테스트 메서드를 실행한다.
     * @param m 실행할 테스트 메서드
     * @return 테스트 결과
     * @throws RuntimeException 실행 오류 발생시 테스트를 중단한다.
     */
    private TestResult invokeStaticExceptionTestMethod(Method m) {
        try {
            m.invoke(null);

            // test failed
            return TestResult.failure(m.getName(), TestType.EXCEPTION_TEST, null);
        }
        catch (InvocationTargetException e) {
            // test success
            if (Arrays.stream(m.getAnnotationsByType(StaticExceptionTestContainer.class))
                    .flatMap(container -> Arrays.stream(container.value()))
                    .anyMatch(exType -> exType.value().isInstance(e.getCause()))) {
                return TestResult.success(m.getName(), TestType.EXCEPTION_TEST, e.getCause());
            }
            // test failed
            else {
                return TestResult.failure(m.getName(), TestType.EXCEPTION_TEST, e.getCause());
            }
        }
        catch (Throwable e) {
            throw new RuntimeException("unexpected exception: " + e, e);
        }
    }
}
