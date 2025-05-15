package lambdaspecial.java.enroll.dto;

// 최종 결과를 담을 클래스 (DTO: Data Transfer Object)
public class EnrollmentInfo {
    private int sNo;
    private String studentName;
    private String studentAddress;
    private String className;
    private String teacherName;

    public EnrollmentInfo(int sNo, String studentName, String studentAddress, String className, String teacherName) {
        this.sNo = sNo;
        this.studentName = studentName;
        this.studentAddress = studentAddress;
        this.className = className;
        this.teacherName = teacherName;
    }

    // Getter 생략 (필요시 추가)

    @Override
    public String toString() {
        return "EnrollmentInfo{" +
                "sNo=" + sNo +
                ", studentName='" + studentName + '\'' +
                ", studentAddress='" + studentAddress + '\'' +
                ", className='" + className + '\'' +
                ", teacherName='" + teacherName + '\'' +
                '}';
    }
}

