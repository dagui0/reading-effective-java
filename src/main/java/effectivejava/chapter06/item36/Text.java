package effectivejava.chapter06.item36;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Set;
import java.util.stream.IntStream;

public class Text implements CharSequence, Appendable, Comparable<Text> {

    public enum Style {
        BOLD,
        ITALIC,
        UNDERLINE,
        STRIKETHROUGH,
        SUPERSCRIPT,
        SUBSCRIPT
    }

    private StringBuilder s;

    public Text(String s) {
        this.s = new StringBuilder(s);
    }

    public Text() {
        this.s = new StringBuilder();
    }

    public Text applyStyles(Set<Style> styles)  {
        if (styles.contains(Style.BOLD)) {
            s.insert(0, "<b>").append("</b>");
        }
        if (styles.contains(Style.ITALIC)) {
            s.insert(0, "<i>").append("</i>");
        }
        if (styles.contains(Style.UNDERLINE)) {
            s.insert(0, "<u>").append("</u>");
        }
        if (styles.contains(Style.STRIKETHROUGH)) {
            s.insert(0, "<s>").append("</s>");
        }
        if (styles.contains(Style.SUPERSCRIPT)) {
            s.insert(0, "<sup>").append("</sup>");
        }
        if (styles.contains(Style.SUBSCRIPT)) {
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
    public @NotNull Text subSequence(int start, int end) {
        return new Text(s.subSequence(start, end).toString());
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
    public int compareTo(@NotNull Text o) {
        return s.compareTo(o.s);
    }

    @Override
    public Text append(CharSequence csq) throws IOException {
        s.append(csq);
        return this;
    }

    public Text append(StringBuffer sb) {
        s.append(sb);
        return this;
    }

    public Text append(String str) {
        s.append(str);
        return this;
    }

    public Text append(Object obj) {
        s.append(obj);
        return this;
    }

    public Text append(CharSequence csq, int start, int end) throws IOException {
        s.append(csq, start, end);
        return this;
    }

    @Override
    public Text append(char c) throws IOException {
        s.append(c);
        return this;
    }

    public Text append(boolean b) {
        s.append(b);
        return this;
    }

    public Text append(char[] str, int offset, int len) {
        s.append(str, offset, len);
        return this;
    }

    public Text append(char[] str) {
        s.append(str);
        return this;
    }

    public Text append(double d) {
        s.append(d);
        return this;
    }

    public Text append(float f) {
        s.append(f);
        return this;
    }

    public Text append(long lng) {
        s.append(lng);
        return this;
    }

    public Text append(int i) {
        s.append(i);
        return this;
    }
}
