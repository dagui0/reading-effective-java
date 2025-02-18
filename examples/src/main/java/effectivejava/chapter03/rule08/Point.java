package effectivejava.chapter03.rule08;

public class Point {
    private final int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point))
            return false;
        Point p = (Point)o;
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return x * 1000000007 + y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
