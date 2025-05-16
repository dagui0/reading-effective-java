package effectivejava.chapter06.item39;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 특정한 예외를 던져야 성공하는 테스트 메서드임을 선언하는 애너테이션이다.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface StaticThrowTest {
    Class<? extends Throwable>[] value();
}
