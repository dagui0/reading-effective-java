package effectivejava.chapter06.item39;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class StaticTestRunner {

    public void runTest(Class<? extends StaticTestRunner> testClass) throws Exception {

        int tests = 0;
        int passed = 0;

        for (Method m: testClass.getDeclaredMethods()) {

            if (!Modifier.isStatic(m.getModifiers()) && m.getParameterCount() != 0) {
                continue;
            }

            if (m.isAnnotationPresent(StaticTest.class)) {

                tests++;
                try {
                    if (invokeStaticTestMethod(m))
                        passed++;
                    else
                        System.out.println(m + " failed");
                }
                catch (Throwable e) {
                    // unexpected exception
                    System.out.println(m + " invocation failed: " + e);
                    System.out.println(testClass + " test stopped");
                    throw e;
                }
            }
            else if (m.isAnnotationPresent(StaticThrowTest.class)) {

                tests++;
                try {
                    if (invokeStaticTestMethod(m))
                        passed++;
                    else
                        System.out.println(m + " failed");
                }
                catch (Throwable e) {
                    // unexpected exception
                    System.out.println(m + " invocation failed: " + e);
                    System.out.println(testClass + " test stopped");
                    throw e;
                }
            }
            else if (m.isAnnotationPresent(StaticExceptionTest.class)) {

                tests++;
                try {
                    if (invokeStaticExceptionTestMethod(m))
                        passed++;
                    else
                        System.out.println(m + " failed");
                }
                catch (Throwable e) {
                    // unexpected exception
                    System.out.println(m + " invocation failed: " + e);
                    System.out.println(testClass + " test stopped");
                    throw e;
                }
            }
        }

        System.out.printf("%nPassed: %d, Failed: %d%n", passed, tests - passed);
    }

    /**
     * {@link StaticTest} 애너테이션이 붙은 테스트 메서드를 실행한다.
     * @param m 실행할 테스트 메서드
     * @return 테스트 성공 여부
     * @throws Exception 실행 오류 발생시 테스트를 중단한다.
     */
    private boolean invokeStaticTestMethod(Method m) throws Exception {
        try {
            m.invoke(null);

            // test success
            return true;
        }
        catch (InvocationTargetException e) {
            // test failed
            System.out.println(m + " failed: " + e.getCause());
            return false;
        }
    }

    /**
     * {@link StaticThrowTest} 애너테이션이 붙은 테스트 메서드를 실행한다.
     * @param m 실행할 테스트 메서드
     * @return 테스트 성공 여부
     * @throws Exception 실행 오류 발생시 테스트를 중단한다.
     */
    private boolean invokeStaticThrowTestMethod(Method m) throws Exception {
        try {
            m.invoke(null);

            // test failed
            return false;
        }
        catch (InvocationTargetException e) {
            // test success
            for (Class<? extends Throwable> exType : m.getAnnotation(StaticThrowTest.class).value()) {
                if (exType.isInstance(e.getCause())) {
                    return true;
                }
            }
            // test failed
            return false;
        }
    }

    /**
     * {@link StaticExceptionTest} 애너테이션이 붙은 테스트 메서드를 실행한다.
     * @param m 실행할 테스트 메서드
     * @return 테스트 성공 여부
     * @throws Exception 실행 오류 발생시 테스트를 중단한다.
     */
    private boolean invokeStaticExceptionTestMethod(Method m) throws Exception {
        try {
            m.invoke(null);

            // test failed
            return false;
        }
        catch (InvocationTargetException e) {
            // test success
            for (StaticExceptionTest exType : m.getAnnotationsByType(StaticExceptionTest.class)) {
                if (exType.value().isInstance(e.getCause())) {
                    return true;
                }
            }
            // test failed
            return false;
        }
    }
}
