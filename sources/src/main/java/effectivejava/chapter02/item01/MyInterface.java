package effectivejava.chapter02.item01;

public interface MyInterface {

    public static void staticMethod() {
        System.out.println("MyInterface.staticMethod()");
    }

    public default void defaultMethod() {
        System.out.println("MyInterface.defaultMethod()");
    }
}
