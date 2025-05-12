package effectivejava.chapter06.item38;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Color {
    private final String name;

    // private 생성자로 외부에서 인스턴스 생성을 막음
    private Color(String name) {
        this.name = name;
    }

    // public static final 필드를 통해 열거형 상수 제공 (싱글톤)
    public static final Color RED = new Color("red");
    public static final Color GREEN = new Color("green");
    public static final Color BLUE = new Color("blue");

    // 모든 Color 인스턴스를 담는 private static final 리스트
    private static final List<Color> VALUES =
            Collections.unmodifiableList(Arrays.asList(RED, GREEN, BLUE));

    // 모든 Color 인스턴스를 반환하는 public static 메서드
    public static List<Color> values() {
        return VALUES;
    }

    // 문자열 표현 반환
    public String toString() {
        return name;
    }

    // 객체 식별자를 사용한 동등성 비교를 위해 equals() 재정의
    public boolean equals(Object o) {
        return o instanceof Color && ((Color) o).name == name; // 객체 레퍼런스 비교
    }

    // hashCode() 재정의 (equals()를 재정의했으므로)
    public int hashCode() {
        return name.hashCode();
    }

    // ... 추가적인 메서드 (예: name으로 Color 객체 찾기) ...
}
