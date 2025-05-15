package lambdaspecial.java.enroll.domain;

public class ClassInfo {
    private int cNo;
    private String className;
    private String teacher;

    public ClassInfo(int cNo, String className, String teacher) {
        this.cNo = cNo;
        this.className = className;
        this.teacher = teacher;
    }

    public int getcNo() { return cNo; }
    public String getClassName() { return className; }
    public String getTeacher() { return teacher; }

    @Override
    public String toString() {
        return "ClassInfo{" + "cNo=" + cNo + ", className='" + className + '\'' + ", teacher='" + teacher + '\'' + '}';
    }
}
