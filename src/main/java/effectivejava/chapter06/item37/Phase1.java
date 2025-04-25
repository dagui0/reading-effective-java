package effectivejava.chapter06.item37;

/**
 * 물질의 상태를 나타내는 열거형입니다.
 * 각 상은 물질의 물리적 상태를 설명합니다.
 */
public enum Phase1 {

    SOLID("고체"),
    LIQUID("액체"),
    GAS("기체");

    private final String koName;

    Phase1(String koName) {
        this.koName = koName;
    }

    @Override
    public String toString() {
        return koName;
    }

    public enum Transition {

        MELT("융해"), FREEZE("응고"), BOIL("기화"),
        CONDENSE("액화"), SUBLIME("승화"), DEPOSIT("증착");

        private final String koName;

        Transition(String koName) {
            this.koName = koName;
        }

        @Override
        public String toString() {
            return koName;
        }

        private static final Transition[][] TRANSITIONS = {
                {null, MELT, SUBLIME},
                {FREEZE, null, BOIL},
                {DEPOSIT, CONDENSE, null}
        };

        public static Transition from(Phase1 from, Phase1 to) {
            return TRANSITIONS[from.ordinal()][to.ordinal()];
        }
    }
}
