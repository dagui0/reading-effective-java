package com.yidigun.base.examples;

import com.yidigun.base.PrimaryKey;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Calendar;
import java.util.Optional;
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
@Getter
public final class ResidentKey implements PrimaryKey, CharSequence, Comparable<ResidentKey> {

    /// `1111111111118` 형식 검사 패턴 `/^([0-9]{6})([0-9]{7})$/`
    public static final Pattern PATTERN = Pattern.compile("^([0-9]{6})([0-9]{7})$");
    /// `111111-1111118` 형식 검사 패턴 `/^([0-9]{6})-([0-9]{7})$/`
    public static final Pattern PATTERN_WITH_DASH = Pattern.compile("^([0-9]{6})-([0-9]{7})$");

    /// 주민등록번호
    private final String residentId;

    /// 주민등록번호의 유효성을 검사하지 않고 생성한다.
    /// @param residentId 주민등록번호 문자열
    private ResidentKey(String residentId) {
        this.residentId = residentId;
    }

    /// 주민등록번호의 유효성을 검사하여 올바른 경우만 생성한다.
    /// 유효성이 검증되지 않은 번호에 대해서는 예외를 던진다.
    /// @param residentId 주민등록번호 문자열
    /// @return ResidentId 주민등록번호 객체(성공적인 경우)
    /// @throws IllegalArgumentException 주민등록번호 형식이 잘못된 경우
    public static ResidentKey of(String residentId) {
        return ofNullable(residentId)
                .orElseThrow(() -> new IllegalArgumentException("주민등록번호 형식이 잘못되었습니다."));
    }

    /// 유효성 검사 없이 생성한다. 기존에 DB에 저장된 값을 로드하는 경우 등 검사가 필요없는 경우 사용한다.
    /// @param residentId 주민등록번호 문자열
    /// @return ResidentId 주민등록번호 객체(무조건 생성)
    public static ResidentKey ofUnchecked(String residentId) {
        return new ResidentKey(residentId);
    }

    /// 주민등록번호 유효성을 검사하여 성공적일 경우, Optional로 감싸서 반환한다.
    /// 실패할 경우 Optional.empty()를 반환한다.
    ///
    /// @param residentId 주민등록번호
    /// @return Optional<ResidentId> 주민등록번호 or null
    public static Optional<ResidentKey> ofNullable(String residentId) {
        if (residentId == null)
            return Optional.empty();
        else {
            String r = residentId.trim();
            if (r.length() == 14 && PATTERN_WITH_DASH.matcher(r).matches()) {
                r = r.replace("-", "");
                return (isValid(r))? Optional.of(new ResidentKey(r)): Optional.empty();
            }
            else if (r.length() == 13 && PATTERN.matcher(r).matches() && isValid(r))
                return Optional.of(new ResidentKey(r));
            else
                return Optional.empty();
        }
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
    public int compareTo(@NotNull ResidentKey o) {
        return residentId.compareTo(o.residentId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ResidentKey that = (ResidentKey) obj;
        return residentId.equals(that.residentId);
    }

    @Override
    public int hashCode() {
        return residentId.hashCode();
    }

    /// 기본형식(1111111111118) 주민등록번호 문자열을 리턴한다.
    /// @return 주민등록번호 문자열
    @Override
    public @NotNull String toString() {
        return residentId;
    }

    /// '-' 포함 여부를 선택 가능한 주민등록번호 문자열을 리턴하는 메소드
    /// @param withDash '-' 포함 여부
    /// @return 주민등록번호 문자열
    public String toString(boolean withDash) {
        return (withDash)? toString(PATTERN_WITH_DASH): residentId;
    }

    /// 내장 정규식 패턴에 맞춰 주민등록번호 문자열을 리턴하는 메소드.
    /// 내장 정규식 패턴인 [#PATTERN], [#PATTERN_WITH_DASH]만을 지원한다.
    /// 다른 패턴이 지정되면  기본 패턴([#PATTERN]) 패턴으로 리턴한다.
    ///
    /// @param pattern 정규식 패턴 [#PATTERN] 또는 [#PATTERN_WITH_DASH]
    /// @return 주민등록번호 문자열
    public String toString(Pattern pattern) {
        return (PATTERN_WITH_DASH.equals(pattern))?
                PATTERN.matcher(residentId).replaceFirst("$1-$2"):
                residentId;
    }

    public int valueAt(int index) {
        return Character.getNumericValue(residentId.charAt(index));
    }

    public static boolean isValid(ResidentKey residentKey) {
        return isValid(residentKey.residentId);
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
        return valueAt(residentId, 12) == ((11 - (s % 11)) % 10);
    }

    private static int valueAt(String residentId, int index) {
        return Character.getNumericValue(residentId.charAt(index));
    }

    private static int valueAt(ResidentKey residentKey, int index) {
        return valueAt(residentKey.residentId, index);
    }

    public static Instant getBirthday(ResidentKey residentKey)  {
        Calendar cal = Calendar.getInstance();
        int year = Integer.parseInt(residentKey.subSequence(0, 2));
        int month = Integer.parseInt(residentKey.subSequence(2, 4));
        int day = Integer.parseInt(residentKey.subSequence(4, 6));
        int sex = valueAt(residentKey, 6);
        year += (sex == 3 || sex == 4 || sex == 7 || sex == 8)? 2000: 1900;
        cal.set(year, month - 1, day);
        return cal.toInstant();
    }

    public interface Aware {
        default ResidentKey getResidentKey() {
            return ResidentKey.ofUnchecked(getResidentId());
        }
        default Optional<ResidentKey> tryGetResidentKey() {
            return ResidentKey.ofNullable(getResidentId());
        }

        String getResidentId();

        public interface Builder<B extends Builder<B>> {
            default B residentKey(ResidentKey key) {
                return residentId(key.getResidentId());
            }
            B residentId(String residentId);
        }
    }
}
