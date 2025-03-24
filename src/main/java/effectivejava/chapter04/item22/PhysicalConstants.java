package effectivejava.chapter04.item22;

public final class PhysicalConstants {

    PhysicalConstants() {
        throw new AssertionError("Cannot instantiate " + getClass());
    }

    public static final double AVOGADRO_NUMBER = 6.022_140_76e23; // mol^-1
    public static final double BOLTZMANN_CONST = 1.380_649e-23; // J/K
    public static final double ELECTRON_MAS = 1.602_176_634e-19; // J
}
