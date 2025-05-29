package lambdaspecial.java.enroll.domain;

import java.util.Date;

// Student.java
public class Student {

    private int sNo; // Primary Key
    private String name;
    private String address;
    private Date createDate;
    private Date updateDate;

    public Student(int sNo, String name, String address) {
        this.sNo = sNo;
        this.name = name;
        this.address = address;
    }

    public int getsNo() { return sNo; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public Date getCreateDate() { return createDate; }
    public Date getUpdateDate() { return updateDate; }

    @Override
    public String toString() {
        return "Student{" +
                "sNo=" + sNo +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return sNo == student.sNo && name.equals(student.name) && address.equals(student.address);
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(sNo);
        result = 31 * result + name.hashCode();
        result = 31 * result + address.hashCode();
        return result;
    }
}
