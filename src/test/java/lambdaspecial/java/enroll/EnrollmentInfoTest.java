package lambdaspecial.java.enroll;

import lambdaspecial.java.enroll.domain.ClassInfo;
import lambdaspecial.java.enroll.domain.Student;
import lambdaspecial.java.enroll.domain.StudentClass;
import lambdaspecial.java.enroll.dto.EnrollmentInfo;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class EnrollmentInfoTest {

    List<Student> students = Arrays.asList(
            new Student(20231001, "홍길동", "강남구"),
            new Student(20231002, "이순신", "종로구"),
            new Student(20231003, "강감찬", "서초구"),
            new Student(20231004, "유관순", "마포구")
    );

    List<ClassInfo> classes = Arrays.asList(
            new ClassInfo(20231001, "수학", "김철수"),
            new ClassInfo(20231002, "과학", "이영희"),
            new ClassInfo(20231003, "영어", "박영수")
    );

    List<StudentClass> studentClasses = Arrays.asList(
            new StudentClass(20231001, 20231001),
            new StudentClass(20231002, 20231002),
            new StudentClass(20231003, 20231001),
            new StudentClass(20231004, 20231003)
    );

    @Test
    public void testJoin() {
        // 목표: SQL 쿼리와 동일한 결과 얻기
        // SELECT S.S_NO, S.NAME, S.ADDRESS, C.CLASS_NAME, C.TEACHER
        // FROM STUDENT S, STUDENT_CLASS SC, CLASS C
        // WHERE S.S_NO = SC.S_NO AND SC.C_NO = C.C_NO AND S.NAME = '홍길동';

        students.stream()
                // 1. S.NAME = '홍길동' 조건으로 먼저 필터링 (성능상 이점)
                .filter(student -> student.getName().equals("홍길동"))
                // 2. STUDENT 와 STUDENT_CLASS 조인 (S.S_NO = SC.S_NO)
                // flatMap을 사용하여 각 학생에 대해 매칭되는 수강 정보를 스트림으로 만듦
                .flatMap(student -> studentClasses.stream()
                        .filter(sc -> sc.getsNo() == student.getsNo())
                        // 조인된 결과를 임시 객체나 새로운 스트림 요소로 만듦
                        // 여기서는 Student와 StudentClass의 cNo를 함께 가지는 Pair 같은 것을 만들거나,
                        // 바로 다음 조인을 위해 cNo를 가지고 ClassInfo와 매칭할 수 있도록 함
                        .map(sc -> new Object[]{student, sc.getcNo()}) // 임시 배열로 학생과 수업번호 전달
                )
                // 3. 이전 조인 결과와 CLASS 조인 (SC.C_NO = C.C_NO)
                .flatMap(studentAndClassNo -> { // studentAndClassNo[0]는 Student, studentAndClassNo[1]는 cNo
                    Student student = (Student) studentAndClassNo[0];
                    int cNo = (Integer) studentAndClassNo[1];
                    return classes.stream()
                            .filter(cls -> cls.getcNo() == cNo)
                            .map(cls -> new EnrollmentInfo(
                                    student.getsNo(),
                                    student.getName(),
                                    student.getAddress(),
                                    cls.getClassName(),
                                    cls.getTeacher()
                            ));
                })
                .forEach(System.out::println);
    }
}
