package lambdaspecial.java.enroll.domain;

// Student.java
public class Student {
    private int sNo;
    private String name;
    private String address;

    public Student(int sNo, String name, String address) {
        this.sNo = sNo;
        this.name = name;
        this.address = address;
    }

    public int getsNo() { return sNo; }
    public String getName() { return name; }
    public String getAddress() { return address; }

    @Override
    public String toString() {
        return "Student{" + "sNo=" + sNo + ", name='" + name + '\'' + ", address='" + address + '\'' + '}';
    }
}

