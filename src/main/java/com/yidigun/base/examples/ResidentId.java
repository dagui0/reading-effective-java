package com.yidigun.base.examples;

import com.yidigun.base.PrimaryKey;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/// Structured & Semantic [PrimaryKey](주민등록번호)의 구현 예시.
///
/// `1111111111118` 형식으로 저장된다.
/// [#of(String)]로 생성시에는 `111111-1111118` 형식을 지원한다.
///
/// @param residentId 주민등록번호
public record ResidentId(String residentId) implements PrimaryKey, CharSequence {

    public static final Pattern PATTERN = Pattern.compile("^[0-9]{13}$");
    public static final Pattern PATTERN_WITH_DASH = Pattern.compile("^[0-9]{6}-[0-9]{7}$");

    public static ResidentId of(String residentId) {
        if (residentId == null) {
            throw new IllegalArgumentException("residentId must not be null");
        }
        if (PATTERN_WITH_DASH.matcher(residentId).matches()) {
            residentId = residentId.replace("-", "");
        }
        if (!PATTERN.matcher(residentId).matches()) {
            throw new IllegalArgumentException("invalid residentId format");
        }
        return new ResidentId(residentId);
    }

    public boolean isValid() { return isValid(residentId); }
    public boolean isMale() { return valueAt(6) % 2 == 1; }
    public boolean isFemale() { return valueAt(6) % 2 == 0; }

    @Override
    public int length() { return residentId.length(); }

    @Override
    public char charAt(int index) { return residentId.charAt(index); }

    @Override
    public @NotNull String subSequence(int start, int end) {
        return (String)residentId.subSequence(start, end);
    }

    @Override
    public String toString() {
        return residentId;
    }

    public int valueAt(int index) {
        return Character.getNumericValue(residentId.charAt(index));
    }

    public static boolean isValid(String residentId) {
        return residentId != null &&
                PATTERN.matcher(residentId).matches() &&
                hasValidChecksum(residentId);
    }

    public static boolean isValid(ResidentId residentId) { return isValid(residentId.residentId); }

    private static int valueAt(String residentId, int index) {
        return Character.getNumericValue(residentId.charAt(index));
    }

    private static boolean hasValidChecksum(String residentId) {
        int s = 0;
        int[] c = { 2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5 };
        for (int i = 0; i < 12; i++)
            s += valueAt(residentId, i) * c[i];
        return valueAt(residentId, 12) == ((11 - (s % 11))%10);
    }
}
