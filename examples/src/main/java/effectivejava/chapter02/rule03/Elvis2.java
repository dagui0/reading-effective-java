package effectivejava.chapter02.rule03;

public class Elvis2 {
    private static final Elvis2 INSTANCE = new Elvis2();
    private Elvis2() { }
    public static Elvis2 getInstance() { return INSTANCE; }
    private String name = "Elvis Presley";
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public void leaveTheBuilding() { }
}
