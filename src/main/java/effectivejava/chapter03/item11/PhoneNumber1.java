package effectivejava.chapter03.item11;

@SuppressWarnings("overrides")
public class PhoneNumber1 {
    private final short areaCode, prefix, lineNumber;

    public PhoneNumber1(int areaCode, int prefix, int lineNumber) {
        checkRange(areaCode, 999, "area code");
        checkRange(prefix, 9999, "prefix");
        checkRange(lineNumber, 9999, "line number");
        this.areaCode = (short)areaCode;
        this.prefix = (short)prefix;
        this.lineNumber = (short)lineNumber;
    }

    private static void checkRange(int arg, int max, String name) {
        if (arg < 0 || arg > max)
            throw new IllegalArgumentException(name + ": " + arg);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PhoneNumber1))
            return false;
        PhoneNumber1 pn = (PhoneNumber1)o;
        return pn.areaCode == areaCode && pn.prefix == prefix && pn.lineNumber == lineNumber;
    }
}
