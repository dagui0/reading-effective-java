package effectivejava.chapter06.item35;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnsembleTest {

    @Test
    public void testEnsemble1() {
        assertEquals(1, Ensemble1.SOLO.numberOfMusicians());
        assertEquals(2, Ensemble1.DUET.numberOfMusicians());
        assertEquals(3, Ensemble1.TRIO.numberOfMusicians());
        assertEquals(4, Ensemble1.QUARTET.numberOfMusicians());
        assertEquals(5, Ensemble1.QUINTET.numberOfMusicians());
        assertEquals(6, Ensemble1.SEXTET.numberOfMusicians());
        assertEquals(7, Ensemble1.SEPTET.numberOfMusicians());
        assertEquals(8, Ensemble1.OCTET.numberOfMusicians());
        assertEquals(9, Ensemble1.NONET.numberOfMusicians());
        assertEquals(10, Ensemble1.DECTET.numberOfMusicians());
        assertEquals(8, Ensemble1.DOUBLE_QUARTET.numberOfMusicians());
        assertEquals(12, Ensemble1.TRIPLE_QUARTET.numberOfMusicians());
    }

    @Test
    public void testEnsemble() {
        assertEquals(1, Ensemble.SOLO.numberOfMusicians());
        assertEquals(2, Ensemble.DUET.numberOfMusicians());
        assertEquals(3, Ensemble.TRIO.numberOfMusicians());
        assertEquals(4, Ensemble.QUARTET.numberOfMusicians());
        assertEquals(5, Ensemble.QUINTET.numberOfMusicians());
        assertEquals(6, Ensemble.SEXTET.numberOfMusicians());
        assertEquals(7, Ensemble.SEPTET.numberOfMusicians());
        assertEquals(8, Ensemble.OCTET.numberOfMusicians());
        assertEquals(8, Ensemble.DOUBLE_QUARTET.numberOfMusicians());
        assertEquals(9, Ensemble.NONET.numberOfMusicians());
        assertEquals(10, Ensemble.DECTET.numberOfMusicians());
        assertEquals(12, Ensemble.TRIPLE_QUARTET.numberOfMusicians());
    }
}
