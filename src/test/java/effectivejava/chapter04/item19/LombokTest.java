package effectivejava.chapter04.item19;

import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LombokTest {

    @Test
    public void testDataTransferObject() {
        DataTrasnferObject dto = new DataTrasnferObject("Alejandro", 30);
        assertEquals("Alejandro", dto.getName());
        assertEquals("effectivejava.chapter04.item19.DataTrasnferObject", dto.getClass().getName());
    }
}

@Data
final class DataTrasnferObject {
    private final String name;
    private final int age;
}
