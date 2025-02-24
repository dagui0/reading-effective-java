package effectivejava.chapter03.item13;

public class PhoneNumber implements Cloneable {
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

    /**
     * Returns a string representation of the phone number in the format "XXX-XXXX-XXXX",
     * where each component (area code, prefix, and line number) is zero-padded to match its
     * respective width.
     *
     * @return a string representation of the phone number in the format "XXX-XXXX-XXXX".
     */
    @Override
    public String toString() {
        return String.format("%03d-%04d-%04d", areaCode, prefix, lineNumber);
    }

    @Override
    public PhoneNumber clone() {
        try {
            return (PhoneNumber) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Can't happen
        }
    }
}
