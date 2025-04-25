package effectivejava.chapter06.item36;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.stream.IntStream;

public class Text1 implements CharSequence, Appendable, Comparable<Text1> {

    public static final int STYLE_BOLD          = 1; // 1
    public static final int STYLE_ITALIC        = 1 << 1; // 2
    public static final int STYLE_UNDERLINE     = 1 << 2; // 4
    public static final int STYLE_STRIKETHROUGH = 1 << 3; // 8
    public static final int STYLE_SUPERSCRIPT   = 1 << 4; // 16
    public static final int STYLE_SUBSCRIPT     = 1 << 5; // 32

    private StringBuilder s;

    public Text1(String s) {
        this.s = new StringBuilder(s);
    }

    public Text1() {
        this.s = new StringBuilder();
    }

    public Text1 applyStyles(int styles)  {
        if ((styles & STYLE_BOLD) != 0) {
            s.insert(0, "<b>").append("</b>");
        }
        if ((styles & STYLE_ITALIC) != 0) {
            s.insert(0, "<i>").append("</i>");
        }
        if ((styles & STYLE_UNDERLINE) != 0) {
            s.insert(0, "<u>").append("</u>");
        }
        if ((styles & STYLE_STRIKETHROUGH) != 0) {
            s.insert(0, "<s>").append("</s>");
        }
        if ((styles & STYLE_SUPERSCRIPT) != 0) {
            s.insert(0, "<sup>").append("</sup>");
        }
        if ((styles & STYLE_SUBSCRIPT) != 0) {
            s.insert(0, "<sub>").append("</sub>");
        }

        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return s.toString();
    }

    @Override
    public int length() {
        return s.length();
    }

    @Override
    public char charAt(int index) {
        return s.charAt(index);
    }

    @Override
    public @NotNull Text1 subSequence(int start, int end) {
        return new Text1(s.subSequence(start, end).toString());
    }

    @Override
    public boolean isEmpty() {
        return s.isEmpty();
    }

    @Override
    public @NotNull IntStream chars() {
        return s.chars();
    }

    @Override
    public @NotNull IntStream codePoints() {
        return s.codePoints();
    }

    @Override
    public int compareTo(@NotNull Text1 o) {
        return s.compareTo(o.s);
    }

    @Override
    public Text1 append(CharSequence csq) throws IOException {
        s.append(csq);
        return this;
    }

    public Text1 append(StringBuffer sb) {
        s.append(sb);
        return this;
    }

    public Text1 append(String str) {
        s.append(str);
        return this;
    }

    public Text1 append(Object obj) {
        s.append(obj);
        return this;
    }

    public Text1 append(CharSequence csq, int start, int end) throws IOException {
        s.append(csq, start, end);
        return this;
    }

    @Override
    public Text1 append(char c) throws IOException {
        s.append(c);
        return this;
    }

    public Text1 append(boolean b) {
        s.append(b);
        return this;
    }

    public Text1 append(char[] str, int offset, int len) {
        s.append(str, offset, len);
        return this;
    }

    public Text1 append(char[] str) {
        s.append(str);
        return this;
    }

    public Text1 append(double d) {
        s.append(d);
        return this;
    }

    public Text1 append(float f) {
        s.append(f);
        return this;
    }

    public Text1 append(long lng) {
        s.append(lng);
        return this;
    }

    public Text1 append(int i) {
        s.append(i);
        return this;
    }
}
