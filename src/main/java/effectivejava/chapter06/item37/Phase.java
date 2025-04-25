package effectivejava.chapter06.item37;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

/**
 * 물질의 상태를 나타내는 열거형입니다.
 * 각 상은 물질의 물리적 상태를 설명합니다.
 */
public enum Phase {

    SOLID("고체"),
    LIQUID("액체"),
    GAS("기체"),
    PLASMA("플라스마");

    private final String koName;

    Phase(String koName) {
        this.koName = koName;
    }

    @Override
    public String toString() {
        return koName;
    }

    public enum Transition {

        MELT("융해", SOLID, LIQUID),
        FREEZE("응고", LIQUID, SOLID),
        BOIL("기화", LIQUID, GAS),
        CONDENSE("액화", GAS, LIQUID),
        SUBLIME("승화", SOLID, GAS),
        DEPOSIT("증착", GAS, SOLID),
        IONIZE("이온화", GAS, PLASMA),
        DEIONIZE("탈이온화", PLASMA, GAS);

        private final String koName;
        private final Phase from, to;

        Transition(String koName, Phase from, Phase to) {
            this.koName = koName;
            this.from = from;
            this.to = to;
        }

        public Phase from() { return from; }
        public Phase to() { return to; }

        @Override
        public String toString() {
            return koName;
        }

        private static final Map<Phase, Map<Phase, Transition>> TRANSITIONS =
                Stream.of(values())
                        .collect(groupingBy(Transition::from,
                                () -> new EnumMap<>(Phase.class),
                                toMap(Transition::to,
                                        t -> t,
                                        (x, y) -> y,
                                        () -> new EnumMap<>(Phase.class))));

        public static Transition from(Phase from, Phase to) {
            return TRANSITIONS.get(from).get(to);
        }
    }
}
