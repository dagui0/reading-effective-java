package lambdaspecial.java;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class StreamTest {

    @Test
    public void testReduce() {

        List<Integer> s = List.of(1, 2, 3, 4, 5);

        int sum = s.stream()
                .reduce((r, e) -> r + e)
                .orElse(0);
        int count = s.stream()
                .reduce((r, e) -> ++r)
                .orElse(0);
        int min = s.stream()
                .reduce((r, e) -> (r < e) ? r : e)
                .orElse(0);
        int max = s.stream()
                .reduce((r, e) -> (r > e) ? r : e)
                .orElse(0);

        assertEquals(15, sum);
        assertEquals(5, count);
        assertEquals(1, min);
        assertEquals(5, max);
    }

    @Test
    public void testReduce2() {

        List<Integer> s = List.of(5, 4, 3, 2, 1);

        int count = s.stream()
                .reduce((r, e) -> ++r)
                .orElse(0);
        int count2 = s.stream()
                .reduce(0, (r, e) -> ++r);

        assertEquals(9, count);
        assertEquals(5, count2);
    }

    @Test
    public void testCollect1() {

        List<Double> numbers = List.of(5.0, 4.0, 3.0, 2.0, 1.0);

        Summary s = numbers.stream()
                .collect(
                        Summary::new,    // suplier     () -> new Summary()
                        Summary::accept, // accumulator (s, e) -> s.accept(e)
                        Summary::combine // combiner    (s1, s2) -> s1.combine(s2)
                );

        System.out.printf("Sum: %.2f, count: %.2f, average: %.2f\n", s.sum, s.count, s.average());
        // Sum: 15.00, count: 5.00, average: 3.00
    }

    static class Summary {
        private double sum = 0;
        private double count = 0;

        // Summary -> Double
        public double average() {
            return count == 0 ? 0 : sum / count;
        }

        // (Summary, Double) -> void
        public void accept(double value) {
            sum += value;
            count++;
        }

        // (Summary, Summary) -> Summary
        public Summary combine(Summary other) {
            this.sum += other.sum;
            this.count += other.count;
            return this;
        }
    }

    static class AverageCollector implements Collector<Double, Summary, Double> {

        @Override
        public Supplier<Summary> supplier() {
            // () -> Summary
            return Summary::new;
        }

        @Override
        public BiConsumer<Summary, Double> accumulator() {
            // (Summary, Double) -> void
            return Summary::accept;
        }

        @Override
        public BinaryOperator<Summary> combiner() {
            // (Summary, Summary) -> Summary
            return Summary::combine;
        }

        @Override
        public Function<Summary, Double> finisher() {
            // Summary -> Double
            return Summary::average;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.of(
                    // Collector.Characteristics.IDENTITY_FINISH,  // A, R이 같은 타입이라 finisher가 필요 없을 때 사용
                    Collector.Characteristics.UNORDERED,        // 순서가 중요하지 않을 때 사용
                    Collector.Characteristics.CONCURRENT        // 병렬 처리 가능할 때 사용
            );
        }
    }

    @Test
    public void testCollect2() {

        List<Double> numbers = List.of(5.0, 4.0, 3.0, 2.0, 1.0);

        double average = numbers.stream().collect(new AverageCollector());

        System.out.printf("Average: %.2f\n", average);
        // average: 3.00
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    public void testDailyHomework() {

        List<String> dailyHomeworks = List.of("장미", "튤립", "해바라기", "백합", "국화");
        Stream<String> homeworkStream = dailyHomeworks.stream();

        // 첫째날
        String firstHomework = homeworkStream
                .map((s) -> s + " 그림")
                .findFirst()
                .orElse("숙제 끝!");
        assertEquals("장미 그림", firstHomework);

        // 둘째날
        assertThrows(IllegalStateException.class, () -> {
            String secondHomework = homeworkStream
                    .map((s) -> s + " 그림")
                    .findFirst()
                    .orElse("숙제 다함");
            assertEquals("튤립 끝!", secondHomework);
        });

        // 아 안되네 다시 둘째날
        String secondHomework = dailyHomeworks.stream()
                .map((s) -> s + " 그림")
                .findFirst()
                .orElse("숙제 끝!");

        assertNotEquals("튤립 그림", secondHomework);
        assertEquals("장미 그림", secondHomework);

        // 아씨 이것도 안되네 다시 둘째날
        String againSecondHomework = dailyHomeworks.stream()
                .skip(1)
                .map((s) -> s + " 그림")
                .findFirst()
                .orElse("숙제 끝!");

        assertEquals("튤립 그림", againSecondHomework);
    }
}
