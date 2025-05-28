package lambdaspecial.java;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class Plant implements Comparable<Plant> {

    public enum LifeCycle {
        ANNUAL("한해살이"),
        BIENNIAL("두해살이"),
        PERENNIAL("여러해살이");

        private final String koName;

        LifeCycle(String koName) {
            this.koName = koName;
        }

        @Override
        public String toString() {
            return koName;
        }
    }

    public class FlowerImpl implements Flower {
        private final String pot;
        FlowerImpl(String pot) {
            this.pot = pot;
        }
        @Override
        public String color() { return color; }

        @Override
        public String name() { return koName + " 꽃"; }

        public String pot() { return pot; }

        @Override
        public String toString() {
            return String.format("%s (%s) - %s", name(), pot(), color());
        }
    }

    private final String name;
    private final String koName;
    private final String color;
    private final LifeCycle lifeCycle;

    Plant(String name, String koName, String color, LifeCycle lifeCycle) {
        this.name = name;
        this.koName = koName;
        this.color = color;
        this.lifeCycle = lifeCycle;
    }

    public LifeCycle lifeCycle() {
        return lifeCycle;
    }

    public String name() {
        return name;
    }

    public String koName() {
        return koName;
    }

    public Flower grow(String pot) {
        try {
            // 시뮬레이션을 위해 1-2초간 대기
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 2000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 인터럽트 상태를 다시 설정
            throw new RuntimeException("인터럽트됨: " + e.getMessage(), e);
        }
        return new FlowerImpl(pot);
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
