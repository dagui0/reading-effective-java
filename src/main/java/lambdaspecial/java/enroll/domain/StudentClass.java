package lambdaspecial.java.enroll.domain;

// StudentClass.java (중간 테이블)
public class StudentClass {
    private int sNo;
    private int cNo;

    public StudentClass(int sNo, int cNo) {
        this.sNo = sNo;
        this.cNo = cNo;
    }

    public int getsNo() { return sNo; }
    public int getcNo() { return cNo; }

    @Override
    public String toString() {
        return "StudentClass{" + "sNo=" + sNo + ", cNo=" + cNo + '}';
    }
}
