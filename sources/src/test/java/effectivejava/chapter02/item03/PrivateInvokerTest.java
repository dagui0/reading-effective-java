package effectivejava.chapter02.item03;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PrivateInvokerTest {

    @Test
    void testCreatePrivateConstructor() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        // 리플렉션의 setAccessiable() 메소드를 통해 private로 선언된 생성자의 호출 권한을 획득한다.
        Constructor<?> con = Private.class.getDeclaredConstructors()[0];
        con.setAccessible(true);
        Private p = (Private)con.newInstance();

        assertNotNull(p);
    }
}

class Private {
    private Private() {
        System.out.println("oh, no!");
    }
}
