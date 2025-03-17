package effectivejava.chapter04.item17;

/**
 * Immutable 복소수 클래스 예제.
 * 예제를 위한 코드로 실무에 사용하지 말것.
 * NaN, Infinity, -Infinity 처리 등 제대로 구현하지 않은 부분이 많다.
 */
public class Complex {

    private static final Complex ZERO = new Complex(0, 0);
    private static final Complex ONE = new Complex(1, 0);
    private static final Complex I = new Complex(0, 1);

    private final double re;
    private final double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double realPart() {
        return re;
    }
    public double imaginaryPart() {
        return im;
    }

    public Complex plus(Complex c) {
        return new Complex(re + c.re, im + c.im);
    }
    public Complex minus(Complex c) {
        return new Complex(re - c.re, im - c.im);
    }
    public Complex times(Complex c) {
        return new Complex(re * c.re - im * c.im, re * c.im + im * c.re);
    }
    public Complex dividedBy(Complex  c) {
        double tmp = c.re * c.re + c.im * c.im;
        return new Complex((re * c.re + im * c.im) / tmp, (im * c.re - re * c.im) / tmp);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Complex)) return false;
        Complex c = (Complex) obj;
        return Double.compare(re, c.re) == 0 && Double.compare(im, c.im) == 0;
    }

    @Override
    public int hashCode() {
        return 31 * Double.hashCode(re) + Double.hashCode(im);
    }

    @Override
    public String toString() {
        return String.format("%s + %si", re, im);
    }
}
