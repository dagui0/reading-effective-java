package effectivejava.chapter06.item37;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PhaseTransitionTest {

    @Test
    public void testTransition1() {

        assertEquals("융해", Phase1.Transition.from(Phase1.SOLID, Phase1.LIQUID).toString());
        assertEquals("기화", Phase1.Transition.from(Phase1.LIQUID, Phase1.GAS).toString());
        assertEquals("액화", Phase1.Transition.from(Phase1.GAS, Phase1.LIQUID).toString());
        assertEquals("응고", Phase1.Transition.from(Phase1.LIQUID, Phase1.SOLID).toString());
        assertEquals("승화", Phase1.Transition.from(Phase1.SOLID, Phase1.GAS).toString());
        assertEquals("증착", Phase1.Transition.from(Phase1.GAS, Phase1.SOLID).toString());
    }

    @Test
    public void testTransition() {

        assertEquals("융해", Phase.Transition.from(Phase.SOLID, Phase.LIQUID).toString());
        assertEquals("기화", Phase.Transition.from(Phase.LIQUID, Phase.GAS).toString());
        assertEquals("액화", Phase.Transition.from(Phase.GAS, Phase.LIQUID).toString());
        assertEquals("응고", Phase.Transition.from(Phase.LIQUID, Phase.SOLID).toString());
        assertEquals("승화", Phase.Transition.from(Phase.SOLID, Phase.GAS).toString());
        assertEquals("증착", Phase.Transition.from(Phase.GAS, Phase.SOLID).toString());

        assertEquals("이온화", Phase.Transition.from(Phase.GAS, Phase.PLASMA).toString());
        assertEquals("탈이온화", Phase.Transition.from(Phase.PLASMA, Phase.GAS).toString());
    }
}
