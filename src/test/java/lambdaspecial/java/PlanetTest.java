package lambdaspecial.java;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;

public class PlanetTest {

    @Test
    public void testMaxLambda() {

        /*
         * Stream<T>.max(
         *      Comparator<? super T> comparator
         * ): Optional<T>
         * - T: Type
         */
        Arrays.stream(Planet.values())
                .max((a, b) -> Double.compare(a.mass(), b.mass()))
                .ifPresent(p -> System.out.println("가장 큰 행성: " + p));
    }

    @Test
    public void testReduce() {
        /*
         * Stream<T>.reduce(
         *      U identity,
         *      BinaryOperator<T> accumulator
         * ) -> U
         * Stream<T>.<U>reduce(
         *      U identity,
         *      BiFunction<U, ? super T, U> accumulator,
         *      BinaryOperator<U> combiner
         * ) -> U
         * BiFunction<U, ? super T, U>: (U, T) -> U
         * BinaryOperator<T>: (T, T) -> T
         * - T: Type
         * - U: Result type (identity value)
         */
        Double sum = Arrays.stream(Planet.values())
                .reduce(0.0,
                        (acc, p) -> acc + p.mass(),
                        Double::sum);
        System.out.println("모든 행성의 질량 합: " + sum);

        class AverageCalculator {
            double sum = 0;
            int count = 0;

            public double average() {
                return sum / count;
            }

            public void accept(Planet planet) {
                sum += planet.mass();
                count++;
            }

            public void combine(AverageCalculator other) {
                sum += other.sum;
                count += other.count;
            }
        }

        AverageCalculator average = Arrays.stream(Planet.values()).reduce(
                new AverageCalculator(),
                (acc, p) -> { acc.accept(p); return acc; },
                (acc1, acc2) -> { acc1.combine(acc2); return acc1; }
        );
        System.out.println("모든 행성의 평균 질량: " + average.average());
    }

    @Test
    public void testStatistics() {

        DoubleSummaryStatistics stats = Arrays.stream(Planet.values())
                .collect(Collectors.summarizingDouble(Planet::mass));

        System.out.println("행성 질량 통계:");
        System.out.println("합계: " + stats.getSum());
        System.out.println("개수: " + stats.getCount());
        System.out.println("최대: " + stats.getMax());
        System.out.println("최소: " + stats.getMin());
        System.out.println("평균: " + stats.getAverage());
    }
}
