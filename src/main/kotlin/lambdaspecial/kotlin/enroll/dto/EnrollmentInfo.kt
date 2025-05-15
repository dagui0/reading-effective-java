package lambdaspecial.kotlin.enroll.dto

// 최종 결과를 담을 데이터 클래스
public data class EnrollmentInfo(
    val sNo: Int,
    val studentName: String,
    val studentAddress: String,
    val className: String,
    val teacherName: String
)