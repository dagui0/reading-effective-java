package effectivejava.chapter08.item50;

import java.util.Date;

public class Period2 {

    private final Date start;
    private final Date end;

    public Period2(Date start, Date end) {
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());
        if (this.start.compareTo(this.end) > 0) {
            throw new IllegalArgumentException("Start date must not be after end date");
        }
    }

    public Date start() {
        return new Date(start.getTime());
    }

    public Date end() {
        return new Date(end.getTime());
    }
}
