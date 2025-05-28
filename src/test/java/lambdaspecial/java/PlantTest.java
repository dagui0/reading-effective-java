package lambdaspecial.java;

import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlantTest {

    /**
     * 함수의 커링 개념
     */
    @Test
    public void testLambdaCurring() {

        Plant plant = new Plant("Sunflower", "해바라기", "노랑", Plant.LifeCycle.ANNUAL);
        Plant.LifeCycle lifeCycleCriteria = Plant.LifeCycle.ANNUAL;

        /*
         * BiPredicate<T, U>: (T, U) -> boolean
         * - T: Type
         * - U: Secondary Type
         */

        // $ (λ(p, lc). p.lifeCycle() == lc) (plant, lifeCycleCriteria) $
        BiPredicate<Plant, Plant.LifeCycle> filterByLifeCycle = (p, lc) -> p.lifeCycle() == lc;

        assertTrue(filterByLifeCycle.test(plant, lifeCycleCriteria));

        BiPredicate<Plant, Plant.LifeCycle> filterByLifeCycle2 = new BiPredicate<Plant, Plant.LifeCycle>() {
            @Override
            public boolean test(Plant p, Plant.LifeCycle lifeCycle) {
                return p.lifeCycle() == lifeCycle;
            }
        };

        assertTrue(filterByLifeCycle2.test(plant, lifeCycleCriteria));

        /*
         * Predicate<T>: (T) -> boolean
         * - T: Type
         */

        // $ (λp. (λlc. p.lifeCycle() == lc) lifeCycleCriteria )  plant $
        Predicate<Plant> filterByLifeCycle3 =
                (p) ->
                        ((Predicate<Plant.LifeCycle>)(lc -> p.lifeCycle() == lc))
                                .test(lifeCycleCriteria);
        // Java의 람다 익스프레션은 타입 추론이 가능해야 리터럴로 제 기능을 할 수 있음
        // p -> (lc -> p.lifeCycle() == lc).test(lifeCycleCriteria);  // 컴파일 오류 발생

        assertTrue(filterByLifeCycle3.test(plant));

        Predicate<Plant> filterByLifeCycle4 = new Predicate<>() {
            @Override
            public boolean test(Plant p) {
                return new Predicate<Plant.LifeCycle>() {
                    @Override
                    public boolean test(Plant.LifeCycle lc) {
                        return p.lifeCycle() == lc;
                    }
                }.test(lifeCycleCriteria);
            }
        };

        assertTrue(filterByLifeCycle4.test(plant));
    }

    List<Plant> plants = List.of(
            new Plant("Rose", "장미", "빨강", Plant.LifeCycle.PERENNIAL),
            new Plant("Tulip", "튤립", "분홍", Plant.LifeCycle.PERENNIAL),
            new Plant("Daisy", "데이지", "흰색", Plant.LifeCycle.BIENNIAL),
            new Plant("Lily", "백합", "흰색", Plant.LifeCycle.PERENNIAL),
            new Plant("Sunflower", "해바라기", "노랑", Plant.LifeCycle.ANNUAL),
            new Plant("Daffodil", "수선화", "노랑", Plant.LifeCycle.PERENNIAL),
            new Plant("Orchid", "난초", "분홍", Plant.LifeCycle.PERENNIAL),
            new Plant("Marigold", "천수국", "주황", Plant.LifeCycle.ANNUAL),
            new Plant("Pansy", "팬지", "파랑", Plant.LifeCycle.BIENNIAL),
            new Plant("Chrysanthemum", "국화", "흰색", Plant.LifeCycle.BIENNIAL),
            new Plant("Petunia", "피튜니아", "보라", Plant.LifeCycle.ANNUAL),
            new Plant("Zinnia", "백일홍", "빨강", Plant.LifeCycle.ANNUAL)
    );

    @Test
    public void testProcedural() {
        Plant.LifeCycle annual = Plant.LifeCycle.ANNUAL;

        /*
         * SELECT name FROM plant WHERE lifeCycle = 'annual';
         */

        for (Plant p : plants) {
            // 1. lifeCycle이 annual인 Plant 필터링
            if (p.lifeCycle() == annual) {
                // 2. Plant 객체에서 name(영문 이름) 추출
                String name = p.name();
                // 3. 문자열 출력
                System.out.println(name);
            }
        }
    }

    @Test
    public void testLambda() {

        Plant.LifeCycle annual = Plant.LifeCycle.ANNUAL;

        /*
         * SELECT name FROM plant WHERE lifeCycle = 'annual';
         */

        plants.stream()
                .filter(p -> p.lifeCycle() == annual)
                .map(Plant::name)
                .forEach(System.out::println);
    }

    @Test
    public void testAnonymousClasses() {
        final Plant.LifeCycle annual = Plant.LifeCycle.ANNUAL; // 익명 클래스에서 참조되므로 final 또는 effectively final 이어야 함

        /*
         * Predicate<T>: (T) -> boolean
         * - T: Type
         * Function<T, R>: (T) -> R
         * - T: Type
         * - R: Return Type
         * Consumer<T>: (T) -> void
         * - T: Type
         */

        // 1. Predicate 익명 클래스: lifeCycle이 annual인 Plant 필터링
        Predicate<Plant> filterByAnnual = new Predicate<>() {
            @Override
            public boolean test(Plant p) {
                return p.lifeCycle() == annual;
            }
        };

        // annual = Plant.LifeCycle.PERENNIAL; // final 또는 effectively final이 아니므로 컴파일 에러 발생

        // 2. Function 익명 클래스: Plant 객체에서 name(영문 이름) 추출
        Function<Plant, String> getNameFromPlant = new Function<>() {
            @Override
            public String apply(Plant p) {
                return p.name();
            }
        };

        // 3. Consumer 익명 클래스: 문자열 출력
        Consumer<String> printName = new Consumer<>() {
            @Override
            public void accept(String name) {
                System.out.println(name);
            }
        };

        // 스트림 연산에 익명 클래스 인스턴스 적용
        plants.stream()
                .filter(filterByAnnual)
                .map(getNameFromPlant)
                .forEach(printName);
    }

    @Test
    public void testLambdaUpper() {

        Plant.LifeCycle annual = Plant.LifeCycle.ANNUAL;

        /*
         * SELECT UPPER(name) FROM plant WHERE lifeCycle = 'annual';
         */

        plants.stream()
                .filter(p -> p.lifeCycle() == annual)
                .map(Plant::name)
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }

    @Test
    public void testLambdaSimultaneity() {

        /*
         * SELECT koName || '(' || name || ', ' || lifeCycle || ')' AS plant
         *   FROM plant
         *  ORDER BY koName;
         * SELECT lifeCycle, count(*) FROM plant GROUP BY lifeCycle;
         */

        plants.stream()
                .sorted()
                .map(p -> p.koName() + "(" + p.name() + ", " + p.lifeCycle() + ")")
                .forEach(System.out::println);

        /*
         * Collectors.<T, K, D, A, M extends Map<T, D>>groupingBy(
         *         Function<T, K> classifier,
         *         Supplier<M> mapFactory,
         *         Collector<T, A, D> downstream
         * )
         * - T: Type
         * - K: Key
         * - D: Downstream
         * - A: Accumulator
         * - M: Map
         */
        plants.stream()
                .collect(
                        Collectors.groupingBy(
                            Plant::lifeCycle,
                            TreeMap::new,
                            Collectors.counting()
                        ))
                .forEach((k, v) -> System.out.println(k + ": " + v));
    }

    @Test
    public void testLambdaSimultaneity2() {

        /*
         * WITH P AS (SELECT koName, name, lifeCycle FROM plant)
         * SELECT 'formattedList' type,
         *        koName || '(' || name || ', ' || lifeCycle || ')' data,
         *        null cnt
         *   FROM P
         * UNION ALL
         * SELECT 'countsMap' type,
         *        lifeCycle data,
         *        count(*) cnt
         *   FROM P
         *  GROUP BY lifeCycle;
         */

        record Report(List<String> formattedList,
                      Map<Plant.LifeCycle, Long> countsMap) {
        }

        /*
         * Collectors.<T, R1, R2, R>teeing(
         *         Collector<T, ?, R1> downstream1,
         *         Collector<T, ?, R2> downstream2,
         *         BiFunction<R1, R2, R> merger
         * )
         * - T: Type
         * - R1: Downstream1
         * - R2: Downstream2
         * - R: Result
         * Collectors.mapping(
         *       Function<T, R> mapper,
         *       Collector<T, ?, R> downstream
         * )
         * - T: Type
         * - R: Result Type
         * BiFunction<R1, R2, R>: (R1, R2) -> R
         * - R1: Type
         * - R2: Secondary Type
         * - R: Result Type
         */
        Report r = plants.stream()
                .sorted()
                .collect(Collectors.teeing(
                        Collectors.mapping(
                                (p) -> p.koName() + "(" + p.name() + ", " + p.lifeCycle() + ")",
                            Collectors.toList()
                        ),
                        Collectors.groupingBy(
                                Plant::lifeCycle,
                                TreeMap::new,
                                Collectors.counting()
                        ),
                        Report::new));

        r.formattedList.forEach(System.out::println);
        r.countsMap.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    @Test
    public void testLambdaSimultaneity3() {

        /*
         * CREATE PROCEDURE p_formattedListAndCountMap AS
         * BEGIN
         *     DECLARE @result TABLE(type VARCHAR(20),
         *                           data VARCHAR(100),
         *                           cnt  INT);
         *     DECLARE @countsMap TABLE(lifeCycle VARCHAR(20),
         *                              cnt INT);
         *     DECLARE @koName      VARCHAR(20);
         *     DECLARE @name        VARCHAR(20);
         *     DECLARE @lifeCycle   VARCHAR(20);
         *     DECLARE @formatted   VARCHAR(100);
         *
         *     CURSOR cur FOR
         *          SELECT koName, name, lifeCycle FROM plant;
         *
         *     OPEN cur;
         *     FETCH NEXT FROM cur INTO @koName, @name, @lifeCycle;
         *     WHILE @@FETCH_STATUS = 0
         *     BEGIN
         *         SET @formatted = @koName + '(' + @name + ', ' + @lifeCycle + ')';
         *         INSERT INTO @result VALUES('formattedList', @formatted, NULL);
         *
         *         MERGE @countsMap AS target
         *         USING (SELECT @lifeCycle AS lifeCycle) AS source
         *         ON (target.lifeCycle = source.lifeCycle)
         *         WHEN MATCHED THEN
         *            UPDATE SET target.cnt = target.cnt + 1
         *         WHEN NOT MATCHED THEN
         *            INSERT (lifeCycle, cnt) VALUES (source.lifeCycle, 1);
         *
         *         FETCH NEXT FROM cur INTO @koName, @name, @lifeCycle;
         *     END
         *     CLOSE cur;
         *     DEALLOCATE cur;
         *
         *     INSERT INTO @result
         *     SELECT 'countsMap' AS type, lifeCycle, cnt FROM @countsMap;
         *
         *     SELECT * FROM @result ORDER BY type, data;
         * END
         */

        @Getter
        class Report {
            final List<String> formattedList = new ArrayList<>();
            final Map<Plant.LifeCycle, Integer> countsMap = new TreeMap<>();

            public void accumulate(Plant p) {
                Plant.LifeCycle lifeCycle = p.lifeCycle();
                formattedList.add(p.koName() + "(" + p.name() + ", " + lifeCycle + ")");
                countsMap.merge(lifeCycle, 1, Integer::sum);
            }

            public void combine(Report other) {
                formattedList.addAll(other.formattedList);
                other.countsMap.forEach((k, v) -> countsMap.merge(k, v, Integer::sum));
            }
        }

        Report r = plants.stream()
                .sorted()
                .collect(Report::new, Report::accumulate, Report::combine);

        r.formattedList.forEach(System.out::println);
        r.countsMap.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    @Test
    public void testConcurrency() {

        // 화분이 3개 뿐이라 순차적으로 배분하여 키워야 함
        int pots = 3;

        // 3개의 worker 스레드를 생성
        try (ExecutorService executorService = Executors.newFixedThreadPool(pots)) {

            // 작업 제출
            List<Future<Flower>> futures = plants.stream()
                    .map(plant -> executorService.submit(
                            () -> plant.grow(Thread.currentThread().getName())
                        ))
                    .toList();

            // Future 객체에서 결과를 가져와 출력
            futures.forEach(future -> {
                try {
                    Flower flower = future.get(); // 병렬 작업의 결과를 기다림
                    System.out.println(flower);
                }
                catch (Exception e) {
                    System.err.print("식물 키우기 실패: " + e.getMessage());
                    if (e instanceof InterruptedException ||
                            (e.getCause() != null && e.getCause() instanceof InterruptedException)) {
                        System.err.println(": 작업이 인터럽트되었습니다.");
                    }
                    else if (e instanceof ExecutionException ||
                            (e.getCause() != null && e.getCause() instanceof ExecutionException)) {
                        System.err.println(": 작업 실행 중 예외가 발생했습니다");
                    }
                    else {
                        System.err.println();
                    }
                }
            });
        }
    }
}
