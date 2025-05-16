package effectivejava.chapter06.item39;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link StaticExceptionTest} 애너테이션을 다중으로 사용할 수 있도록 해주는 애너테이션이다.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface StaticExceptionTestContainer {
    StaticExceptionTest[] value();
}
