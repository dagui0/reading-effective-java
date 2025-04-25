package effectivejava.chapter06.item34;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum ArithmeticOperation3 {

    PLUS("+") {
        public double apply(double x, double y) {
            return x + y;
        }
    },
    MINUS("-") {
        public double apply(double x, double y) {
            return x - y;
        }
    },
    TIMES("*") {
        public double apply(double x, double y) {
            return x * y;
        }
    },
    DIVIDE("/") {
        public double apply(double x, double y) {
            return x / y;
        }
    };

    private static final Map<String, ArithmeticOperation3> operators =
        new HashMap<>();

    static {
        for (ArithmeticOperation3 op : ArithmeticOperation3.values()) {
            operators.put(op.toString(), op);
        }
    }

    private final String symbol;

    ArithmeticOperation3(String symbol) {
        this.symbol = symbol;
    }

    public abstract double apply(double x, double y);

    @Override
    public String toString() {
        return symbol;
    }

    public ArithmeticOperation3 fromString(String operator) {
        return Optional.ofNullable(ArithmeticOperation3.operators.get(operator))
                .orElseThrow(() -> new IllegalArgumentException("Invalid operator: " + operator));
    }
}
