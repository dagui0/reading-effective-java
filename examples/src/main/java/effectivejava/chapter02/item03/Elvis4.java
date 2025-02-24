package effectivejava.chapter02.item03;

public enum Elvis4 {
    INSTANCE;

    private transient String name = "Elvis Presley";
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public void leaveTheBuilding() { }
}
