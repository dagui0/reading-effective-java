package effectivejava.chapter04.item24;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PublicInnerClassListTest {

    @Test
    public void testInstantiateInnerClass() {

        PublicInnerClassList<Integer> list = new PublicInnerClassList<>(10);
        list.add(1);
        list.add(2);
        list.add(3);

        Iterator<Integer> i = list.new MyIterator();
        int expected = 1;
        while (i.hasNext()) {
            assertEquals(expected++, i.next());
        }
    }
}
