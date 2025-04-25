package effectivejava.chapter06.item34;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum ArithmeticOperation4 {

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

    private static final Map<String, ArithmeticOperation4> operators =
        Stream.of(ArithmeticOperation4.values())
            .collect(toMap(ArithmeticOperation4::toString, op -> op));

    private final String symbol;

    ArithmeticOperation4(String symbol) {
        this.symbol = symbol;
    }

    public abstract double apply(double x, double y);

    @Override
    public String toString() {
        return symbol;
    }

    public ArithmeticOperation4 fromString(String operator) {
        return Optional.ofNullable(ArithmeticOperation4.operators.get(operator))
                .orElseThrow(() -> new IllegalArgumentException("Invalid operator: " + operator));
    }
}
