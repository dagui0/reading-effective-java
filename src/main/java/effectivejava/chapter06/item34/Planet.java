package effectivejava.chapter06.item34;

public enum Planet {

    MERCURY(    "수성",   3.302e+23, 2.439e6)
    ,VENUS(     "금성",   4.869e+24, 6.052e6)
    ,EARTH(     "지구",   5.975e+24, 6.378e6)
    ,MARS(      "화성",   6.419e+23, 3.393e6)
    ,JUPITER(   "목성",   1.899e+27, 7.149e7)
    ,SATURN(    "토성",   5.685e+26, 6.027e7)
    ,URANUS(    "천왕성", 8.683e+25, 2.556e7)
    ,NEPTUNE(   "혜왕성", 1.024e+26, 2.477e7)
    ,@Deprecated PLUTO("명왕성", 1.303e+22, 	1.186e6)
    ;

    private final String name;
    private final double mass;              // kg
    private final double radius;            // m
    private final double surfaceGravity;    // m/s^2
    private static final double G = 6.673E-11;

    Planet(String name, double mass, double radius) {
        this.name = name;
        this.mass = mass;
        this.radius = radius;
        this.surfaceGravity = Planet.G * mass / (radius * radius);
    }

    public double mass() { return mass; }
    public double radius() { return radius; }
    public double surfaceGravity() { return surfaceGravity; }
    public double surfaceWeight(double mass) {
        return mass * surfaceGravity();
    }

    @Override
    public String toString() {
        return name;
    }
}
