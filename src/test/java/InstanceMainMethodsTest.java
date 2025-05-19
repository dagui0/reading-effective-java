import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class InstanceMainMethodsTest {

    /// JEP 463 의 규약상 이런 코드는 컴파일 안되는 것이 정상임
    /// > 묵시적으로 선언된 클래스는 다른 코드에서 참조할 수 없다.
    ///
    /// ```java
    /// var helloJava = new HelloJava(); // 오류: 묵시적으로 선언된 클래스 'HelloJava'을(를) 참조할 수 없습니다
    /// helloJava.main();
    /// ```
    ///
    /// 하지만 25.05.19 현재 --enable-preview 설정을 한 gradle에서는
    /// 성공적으로 컴파일 가능하고 테스트도 통과됨, 반면에 인텔리제이에서는 오류 검출함
    @Disabled
    @Test
    public void testMainMethod() {
//        var helloJava = new HelloJava();
//        helloJava.main();
    }
}
