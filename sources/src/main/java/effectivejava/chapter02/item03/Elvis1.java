package effectivejava.chapter02.item03;

public class Elvis1 {
    public static final Elvis1 INSTANCE = new Elvis1();
    private Elvis1() { }
    private String name = "Elvis Presley";
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public void leaveTheBuilding() { }
}
