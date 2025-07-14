package effectivejava.chapter08.item50;

import java.time.Instant;

public class Period {

    private final Instant start;
    private final Instant end;

    public Period(Instant start, Instant end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start must not be after end");
        }
        this.start = start;
        this.end = end;
    }

    public Instant start() {
        return start;
    }

    public Instant end() {
        return end;
    }

    @Override
    public String toString() {
        return "Period{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Period period)) return false;
        return start.equals(period.start) && end.equals(period.end);
    }

    @Override
    public int hashCode() {
        int result = start.hashCode();
        result = 31 * result + end.hashCode();
        return result;
    }
}
