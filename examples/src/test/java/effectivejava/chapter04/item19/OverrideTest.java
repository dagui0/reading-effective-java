package effectivejava.chapter04.item19;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OverrideTest {

    @Test
    public void testOverride() {

        Sub sub = new Sub("test");

        assertEquals("test", sub.getName());
        assertNull(sub.getLog().get(0));
    }
}

/**
 * 초기화시에 onInit 훅을 자식이 설정할 수 있도록 의도한 것이지만 잘못된 설계임.
 * 자식클래스가 초기화될 때 자식클래스에 정의된 필드가 사용될 경우
 * 초기화되지 않은 상태에서 사용됨.
 * 자식의 생성자에서는 super()가 먼저 호출되어야 하기때문.
 */
abstract class Super {

    protected final List<String> log;

    public Super() {
        log = new ArrayList<>();
        onInit();
    }

    protected abstract void onInit();

    public final List<String> getLog() {
        return log;
    }
}

class Sub extends Super {
    private final String name;

    Sub(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    protected void onInit() {
        log.add(name);
    }
}
