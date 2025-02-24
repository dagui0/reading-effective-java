package effectivejava.chapter03.item10;

public class CaseInsensitiveString1 {
    private final String s;

    public CaseInsensitiveString1(String s) {
        this.s = s;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CaseInsensitiveString1) {
            return s.equalsIgnoreCase(((CaseInsensitiveString1)o).s);
        }
        if (o instanceof String) {
            return s.equalsIgnoreCase((String)o);
        }
        return false;
    }
}
