package com.yidigun.base.examples;

import com.yidigun.base.PrimaryKey;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Calendar;
import java.util.regex.Pattern;

/// Structured & Semantic [PrimaryKey](주민등록번호)의 구현 예시.
///
/// 데이터 검증을 포함해서 올바른 경우만 생성하려면 [#of(String)] 메소드를 사용할 수 있다.
/// 유효성 검사 없이 생성하려면(이미 검증되었거나 무조건 인스턴스를 만들어야 할 경우)
/// [#ofUnchecked(String)]를 사용한다.
///
/// `1111111111118` 형식으로 저장된다.
/// [#of(String)]로 생성시에는 `111111-1111118` 형식을 지원한다.
///
public final class ResidentId implements PrimaryKey, CharSequence {

    /// `1111111111111` 형식 검사 패턴
    public static final Pattern PATTERN = Pattern.compile("^[0-9]{13}$");
    /// `111111-1111111` 형식 검사 패턴
    public static final Pattern PATTERN_WITH_DASH = Pattern.compile("^[0-9]{6}-[0-9]{7}$");

    /// 주민등록번호
    private final String residentId;

    /// 주민번호의 유효성을 검사하지 않고 생성한다.
    private ResidentId(String residentId) {
        this.residentId = residentId;
    }

    /// 주민번호의 유효성을 검사하여 올바른 경우만 생성한다.
    /// 유효성이 검증되지 않은 번호에 대해서는 예외를 던진다.
    /// @throws IllegalArgumentException 주민등록번호 형식이 잘못된 경우
    public static ResidentId of(String residentId) {
        if (residentId == null) {
            throw new IllegalArgumentException("주민등록번호는 null이 될 수 없습니다.");
        }
        if (residentId.length() == 14) {
            if (PATTERN_WITH_DASH.matcher(residentId).matches()) {
                residentId = residentId.replace("-", "");
            } else {
                throw new IllegalArgumentException("주민등록번호 형식이 잘못되었습니다.");
            }
        }
        else if (residentId.length() != 13 || !PATTERN.matcher(residentId).matches()) {
            throw new IllegalArgumentException("주민등록번호 형식이 잘못되었습니다.");
        }
        return new ResidentId(residentId);
    }

    /// 유효성 검사 없이 생성한다. 기존에 DB에 저장된 값을 로드하는 경우 등 검사가 필요없는 경우 사용한다.
    public static ResidentId ofUnchecked(String residentId) {
        return new ResidentId(residentId);
    }

    public boolean isValid() { return isValid(residentId); }
    public boolean isMale() { return valueAt(6) % 2 == 1; }
    public boolean isFemale() { return valueAt(6) % 2 == 0; }
    public Instant getBirthday() { return getBirthday(this); }

    @Override
    public int length() { return residentId.length(); }

    @Override
    public char charAt(int index) { return residentId.charAt(index); }

    @Override
    public @NotNull String subSequence(int start, int end) {
        return (String)residentId.subSequence(start, end);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ResidentId that = (ResidentId) obj;
        return residentId.equals(that.residentId);
    }

    @Override
    public int hashCode() {
        return residentId.hashCode();
    }

    @Override
    public @NotNull String toString() {
        return residentId;
    }

    public int valueAt(int index) {
        return Character.getNumericValue(residentId.charAt(index));
    }

    public static boolean isValid(ResidentId residentId) {
        return isValid(residentId.residentId);
    }

    public static boolean isValid(String residentId) {
        return residentId != null &&
                PATTERN.matcher(residentId).matches() &&
                hasValidChecksum(residentId);
    }

    private static boolean hasValidChecksum(String residentId) {
        int s = 0;
        int[] c = { 2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5 };
        for (int i = 0; i < 12; i++)
            s += valueAt(residentId, i) * c[i];
        return valueAt(residentId, 12) == ((11 - (s % 11))%10);
    }

    private static int valueAt(String residentId, int index) {
        return Character.getNumericValue(residentId.charAt(index));
    }

    private static int valueAt(ResidentId residentId, int index) {
        return valueAt(residentId.residentId, index);
    }

    public static Instant getBirthday(ResidentId residentId)  {
        Calendar cal = Calendar.getInstance();
        int year = Integer.parseInt(residentId.subSequence(0, 2));
        int month = Integer.parseInt(residentId.subSequence(2, 4));
        int day = Integer.parseInt(residentId.subSequence(4, 6));
        int sex = valueAt(residentId, 6);
        year += (sex == 3 || sex == 4 || sex == 7 || sex == 8)? 2000: 1900;
        cal.set(year, month - 1, day);
        return cal.toInstant();
    }
}
