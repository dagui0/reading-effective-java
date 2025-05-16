package effectivejava.chapter06.item39;

import java.lang.annotation.*;

/**
 * 특정한 예외를 던져야 성공하는 테스트 메서드임을 선언하는 애너테이션이다.
 * 다중 애너테이션 지원 버전
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(StaticExceptionTestContainer.class)
public @interface StaticExceptionTest {
    Class<? extends Throwable> value();
}
