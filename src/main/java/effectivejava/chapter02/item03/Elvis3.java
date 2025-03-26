package effectivejava.chapter02.item03;

import java.io.Serializable;

public class Elvis3 implements Serializable {
    private static final long serialVersionUID = -5889135599661573739L;
    private static final Elvis3 INSTANCE = new Elvis3();
    private Elvis3() { }
    public static Elvis3 getInstance() { return INSTANCE; }
    private transient String name = "Elvis Presley";
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public void leaveTheBuilding() { }

    private Object readResolve() {
        // 동일한 Elvis 객체가 반환되도록 하는 동시에 가짜 Elvis 객체는 가비지컬렉터가 처리하도록 한다.
        return INSTANCE;
    }
}
