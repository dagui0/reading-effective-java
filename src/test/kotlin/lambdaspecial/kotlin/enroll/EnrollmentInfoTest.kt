package lambdaspecial.kotlin.enroll

import lambdaspecial.kotlin.enroll.domain.ClassInfo
import lambdaspecial.kotlin.enroll.domain.Student
import lambdaspecial.kotlin.enroll.domain.StudentClass
import lambdaspecial.kotlin.enroll.dto.EnrollmentInfo
import kotlin.test.Test

public class EnrollmentInfoTest {

    val students = listOf(
        Student(20231001, "홍길동", "강남구"),
        Student(20231002, "이순신", "종로구"),
        Student(20231003, "강감찬", "서초구"),
        Student(20231004, "유관순", "마포구")
    )

    val classes = listOf(
        ClassInfo(20231001, "수학", "김철수"),
        ClassInfo(20231002, "과학", "이영희"),
        ClassInfo(20231003, "영어", "박영수")
    )

    val studentClasses = listOf(
        StudentClass(20231001, 20231001),
        StudentClass(20231002, 20231002),
        StudentClass(20231003, 20231001),
        StudentClass(20231004, 20231003)
    )

    @Test
    fun testJoin() {
        // 목표 SQL:
        // SELECT S.S_NO, S.NAME, S.ADDRESS, C.CLASS_NAME, C.TEACHER
        // FROM STUDENT S,
        //      STUDENT_CLASS SC,
        //      CLASS C
        // WHERE S.S_NO = SC.S_NO
        //   AND SC.C_NO = C.C_NO
        //   AND S.NAME = '홍길동';

        val targetStudentName = "홍길동"

        students
            .filter { it.name == targetStudentName } // WHERE S.NAME = '홍길동'
            .flatMap { student -> // FROM STUDENT S
                studentClasses
                    .filter { sc -> sc.sNo == student.sNo } // JOIN STUDENT_CLASS SC ON S.S_NO = SC.S_NO
                    .flatMap { sc -> // 이제 student와 sc (수강정보)를 알고 있음
                        classes
                            .filter { cls -> cls.cNo == sc.cNo } // JOIN CLASS C ON SC.C_NO = C.C_NO
                            .map { cls -> // SELECT S.S_NO, S.NAME, S.ADDRESS, C.CLASS_NAME, C.TEACHER
                                EnrollmentInfo(
                                    sNo = student.sNo,
                                    studentName = student.name,
                                    studentAddress = student.address,
                                    className = cls.className,
                                    teacherName = cls.teacher
                                )
                            }
                    }
            }
            .forEach { println(it) }
    }

    @Test
    fun testAssociatedMap() {

        val targetStudentName = "홍길동"

        // 수업 정보를 cNo를 키로 하는 Map으로 변환 (조회 성능 향상)
        val classMap = classes.associateBy { it.cNo }

        // 학생-수업 관계 정보를 sNo를 키로 하고, cNo 리스트를 값으로 하는 Map으로 변환 (한 학생이 여러 수업을 들을 수 있으므로)
        // 여기서는 studentClasses가 sNo당 cNo가 하나씩만 있다고 가정하고 단순화 (만약 여러 개면 groupBy 사용)
        val studentClassMap = studentClasses.associate { it.sNo to it.cNo } // 여기서는 1:1 매핑 가정
        // 만약 한 학생이 여러 수업을 듣는다면:
        // val studentToClassNosMap = studentClasses.groupBy({ it.sNo }, { it.cNo })

        students
            .filter { it.name == targetStudentName }
            .flatMap { student ->
                // student.sNo로 studentClassMap에서 cNo를 찾음
                val cNo = studentClassMap[student.sNo]
                if (cNo != null) {
                    // cNo로 classMap에서 ClassInfo를 찾음
                    val classInfo = classMap[cNo]
                    if (classInfo != null) {
                        listOf( // flatMap은 각 요소에 대해 컬렉션(여기서는 List)을 반환해야 함
                            EnrollmentInfo(
                                sNo = student.sNo,
                                studentName = student.name,
                                studentAddress = student.address,
                                className = classInfo.className,
                                teacherName = classInfo.teacher
                            )
                        )
                    } else {
                        emptyList() // 매칭되는 ClassInfo가 없으면 빈 리스트 반환
                    }
                } else {
                    emptyList() // 매칭되는 StudentClass 정보가 없으면 빈 리스트 반환
                }
            }
            .forEach { println(it) }
    }
}