package effectivejava.chapter03.item10;

import java.awt.*;

public class ColorPoint1 extends Point1 {
    private final Color color;

    public ColorPoint1(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ColorPoint1))
            return false;
        ColorPoint1 p = (ColorPoint1)o;
        return super.equals(o) && color.equals(p.color);
    }
}
