package effectivejava.chapter05.item28;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArrayClassTest {

    @Test
    public void testArrayClass() {

        Example element = new Example("Hello");
        Example[] array = new Example[]{ element };
        Example[][] array2D = new Example[][] {
                { new Example("Hello"), new Example("World") },
                { new Example("Foo"), new Example("Bar") }
        };
        Example[][][] array3D = new Example[][][] {
                { { new Example("Hello"), new Example("World") }, { new Example("Foo"), new Example("Bar") } },
                { { new Example("안녕"), new Example("세계") }, { new Example("푸"), new Example("바") } }
        };

        assertEquals("Example", element.getClass().getSimpleName());
        assertEquals("Example[]", array.getClass().getSimpleName());
        assertEquals("Example[][]", array2D.getClass().getSimpleName());
        assertEquals("Example[][][]", array3D.getClass().getSimpleName());

        assertEquals(Example.class, array.getClass().getComponentType());
        assertEquals(Example[].class, array2D.getClass().getComponentType());
        assertEquals(Example[][].class, array3D.getClass().getComponentType());

        String compTypeClassLoaderName = Example.class.getClassLoader().getClass().getName();
        assertEquals(compTypeClassLoaderName, Example[].class.getClassLoader().getClass().getName()); // 1차원 배열
        assertEquals(compTypeClassLoaderName, Example[][].class.getClassLoader().getClass().getName()); // 2차원 배열
        assertEquals(compTypeClassLoaderName, Example[][][].class.getClassLoader().getClass().getName()); // 3차원 배열
        assertEquals(compTypeClassLoaderName, Example[][][][].class.getClassLoader().getClass().getName()); // 4차원 배열
        assertEquals(compTypeClassLoaderName, Example[][][][][].class.getClassLoader().getClass().getName()); // 5차원 배열
        assertEquals(compTypeClassLoaderName, Example[][][][][][].class.getClassLoader().getClass().getName()); // 6차원 배열
        assertEquals(compTypeClassLoaderName, Example[][][][][][][].class.getClassLoader().getClass().getName()); // 7차원 배열
        assertEquals(compTypeClassLoaderName, Example[][][][][][][][].class.getClassLoader().getClass().getName()); // 8차원 배열
        assertEquals(compTypeClassLoaderName, Example[][][][][][][][][].class.getClassLoader().getClass().getName()); // 9차원 배열
        assertEquals(compTypeClassLoaderName, Example[][][][][][][][][][].class.getClassLoader().getClass().getName()); // 10차원 배열
        assertEquals(compTypeClassLoaderName, Example[][][][][][][][][][][].class.getClassLoader().getClass().getName()); // 11차원 배열
        assertEquals(compTypeClassLoaderName, Example[][][][][][][][][][][][].class.getClassLoader().getClass().getName()); // 12차원 배열
        assertEquals(compTypeClassLoaderName, Example[][][][][][][][][][][][][].class.getClassLoader().getClass().getName()); // 13차원 배열
        assertEquals(compTypeClassLoaderName, Example[][][][][][][][][][][][][][].class.getClassLoader().getClass().getName()); // 14차원 배열
        assertEquals(compTypeClassLoaderName, Example[][][][][][][][][][][][][][][].class.getClassLoader().getClass().getName()); // 15차원 배열
        assertEquals(compTypeClassLoaderName, Example[][][][][][][][][][][][][][][][].class.getClassLoader().getClass().getName()); // 16차원 배열
        assertEquals(compTypeClassLoaderName, Example[][][][][][][][][][][][][][][][][].class.getClassLoader().getClass().getName()); // 17차원 배열
        assertEquals(compTypeClassLoaderName, Example[][][][][][][][][][][][][][][][][][].class.getClassLoader().getClass().getName()); // 18차원 배열
        assertEquals(compTypeClassLoaderName, Example[][][][][][][][][][][][][][][][][][][].class.getClassLoader().getClass().getName()); // 19차원 배열
        assertEquals(compTypeClassLoaderName, Example[][][][][][][][][][][][][][][][][][][][].class.getClassLoader().getClass().getName()); // 20차원 배열
    }
}

@Getter
@EqualsAndHashCode
class Example {
    private final String name;
    public Example(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "Example(" + name + ")";
    }
}
