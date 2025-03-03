package effectivejava.chapter03.item13;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CloneableStackTest {

    @Test
    public void testDeepCopy() {

        CloneableStack<CloneableObj> stack = new CloneableStack<>();
        CloneableObj a = new CloneableObj(1);
        CloneableObj b = new CloneableObj(2);
        CloneableObj c = new CloneableObj(3);
        stack.push(a).push(b).push(c);

        CloneableStack<CloneableObj> clone = stack.clone();

        a.setValue(10);
        b.setValue(20);
        c.setValue(30);

        assertEquals(3, clone.pop().getValue());
        assertEquals(2, clone.pop().getValue());
        assertEquals(1, clone.pop().getValue());

        assertEquals(30, stack.pop().getValue());
        assertEquals(20, stack.pop().getValue());
        assertEquals(10, stack.pop().getValue());

    }
}

@Getter
@Setter
class CloneableObj implements RealyCloneable {
    private int value;

    public CloneableObj(int value) {
        this.value = value;
    }

    @Override
    public CloneableObj clone() {
        return new CloneableObj(value);
    }
}
