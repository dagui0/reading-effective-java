package lambdaspecial.java;

import org.jetbrains.annotations.NotNull;

public class Plant implements Comparable<Plant> {

    public enum LifeCycle {
        ANNUAL("한해살이"),
        PERENNIAL("여러해살이"),
        BIENNIAL("두해살이");

        private final String koName;

        LifeCycle(String koName) {
            this.koName = koName;
        }

        @Override
        public String toString() {
            return koName;
        }
    }

    private final String name;
    private final String koName;
    private final LifeCycle lifeCycle;

    Plant(String name, String koName, LifeCycle lifeCycle) {
        this.name = name;
        this.koName = koName;
        this.lifeCycle = lifeCycle;
    }

    public LifeCycle lifeCycle() {
        return lifeCycle;
    }

    public String name() {
        return name;
    }

    public String enName() {
        return koName;
    }

    @Override
    public int compareTo(@NotNull Plant o) {
        return this.koName.compareTo(o.koName);
    }

    @Override
    public String toString() {
        return koName;
    }
}
