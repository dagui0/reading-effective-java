package effectivejava.chapter03.rule08;

import java.awt.*;

public class ColorPoint {
    private final Point point;
    private final Color color;

    public ColorPoint(Point point, Color color) {
        this.point = point;
        this.color = color;
    }
    public ColorPoint(int x, int y, Color color) {
        this(new Point(x, y), color);
    }

    public Point getPoint() {
        return point;
    }

    public Color getColor() {
        return color;
    }

    public int getX() {
        return point.getX();
    }

    public int getY() {
        return point.getY();
    }

    public Point asPoint() {
        return point;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ColorPoint))
            return false;
        ColorPoint p = (ColorPoint)o;
        return point.equals(p.point) && color.equals(p.color);
    }

    @Override
    public int hashCode() {
        return point.hashCode() * 1000000007 + color.hashCode();
    }

    @Override
    public String toString() {
        return "(" + point.getX() + ", " + point.getY() + ", " + color + ")";
    }
}
