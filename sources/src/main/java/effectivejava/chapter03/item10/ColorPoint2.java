package effectivejava.chapter03.item10;

import java.awt.*;

public class ColorPoint2 extends Point1 {
    private final Color color;

    public ColorPoint2(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point1))
            return false;

        if (!(o instanceof ColorPoint2))
            return o.equals(this);

        ColorPoint2 p = (ColorPoint2)o;
        return super.equals(o) && color.equals(p.color);
    }
}
