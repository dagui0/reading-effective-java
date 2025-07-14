package effectivejava.chapter08.item50;

import java.util.Date;

public class Period1 {

    private final Date start;
    private final Date end;

    public Period1(Date start, Date end) {
        if (start.compareTo(end) > 0) {
            throw new IllegalArgumentException("Start must not be greater than end");
        }
        this.start = start;
        this.end = end;
    }

    public Date start() {
        return start;
    }

    public Date end() {
        return end;
    }
}
