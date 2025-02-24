package effectivejava.chapter03.item10;

public class CaseInsensitiveString
        implements CharSequence, Comparable<CaseInsensitiveString> {

    private final String s;

    public CaseInsensitiveString(String s) {
        this.s = s;
    }

    @Override
    public boolean equals(Object o) {
         return (o instanceof CaseInsensitiveString) &&
                 ((CaseInsensitiveString)o).s.equalsIgnoreCase(s);
    }

    @Override
    public int hashCode() {
        return s.toLowerCase().hashCode();
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
    public CharSequence subSequence(int start, int end) {
        return s.subSequence(start, end);
    }

    @Override
    public String toString() {
        return s;
    }

    @Override
    public int compareTo(CaseInsensitiveString o) {
        return String.CASE_INSENSITIVE_ORDER.compare(s, o.s);
    }
}
