package effectivejava.chapter06.item36;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextTest {

    @Test
    public void testText1() {

        assertEquals(
                "<i><b>Hello, World!</b></i>",
                new Text1()
                    .append("Hello, World!")
                    .applyStyles(
                        Text1.STYLE_BOLD |
                        Text1.STYLE_ITALIC
                    )
                    .toString()
        );
    }

    @Test
    public void testText() {

        assertEquals(
                "<i><b>Hello, World!</b></i>",
                new Text()
                        .append("Hello, World!")
                        .applyStyles(EnumSet.of(
                            Text.Style.BOLD,
                            Text.Style.ITALIC
                        ))
                        .toString()
        );
    }

}
