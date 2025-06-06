package effectivejava.chapter06.item34;

public enum PayrollDay1 {

    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    private static final int MINS_PER_SHIFT = 8 * 60;

    int pay(int minutesWorked, int payRate) {
        int basePay = minutesWorked * payRate;

        int overtimePay;
        switch (this) {
            case SATURDAY:
            case SUNDAY:
                overtimePay = basePay / 2;
                break;
            default:
                overtimePay = minutesWorked > MINS_PER_SHIFT ?
                        (minutesWorked - MINS_PER_SHIFT) * payRate / 2 : 0;
        };

        return basePay + overtimePay;
    }
}
