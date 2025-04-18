package effectivejava.chapter03.item10;

public class Point1 {
    private final int x, y;

    public Point1(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point1))
            return false;
        Point1 p = (Point1)o;
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y; // 31은 소수
    }
}
