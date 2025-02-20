package effectivejava.chapter03.rule09;

public class PhoneNumber {
    private final short areaCode, prefix, lineNumber;

    public PhoneNumber(int areaCode, int prefix, int lineNumber) {
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
        if (!(o instanceof PhoneNumber))
            return false;
        PhoneNumber pn = (PhoneNumber)o;
        return pn.areaCode == areaCode && pn.prefix == prefix && pn.lineNumber == lineNumber;
    }

    @Override
    public int hashCode() {
        int result = 27;
        result = 31 * result + (int)areaCode;
        result = 31 * result + (int)prefix;
        result = 31 * result + (int)lineNumber;
        return result;
    }
}
