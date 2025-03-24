package effectivejava.chapter04.item22;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PhysicalConstantsTest {

    @Test
    public void testPhysicalConstants() {

        assertThrows(AssertionError.class, () -> {
            PhysicalConstants constants = new PhysicalConstants();
        });
        assertEquals(Double.valueOf(6.02214076e23), PhysicalConstants.AVOGADRO_NUMBER);
        assertEquals(Double.valueOf(1.380649e-23), PhysicalConstants.BOLTZMANN_CONST);
        assertEquals(Double.valueOf(1.602176634e-19), PhysicalConstants.ELECTRON_MAS);
    }
}
