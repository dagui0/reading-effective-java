package lambdaspecial.java;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class PlantTest {

    List<Plant> plants = List.of(
            new Plant("Rose", "장미", Plant.LifeCycle.PERENNIAL),
            new Plant("Tulip", "튤립", Plant.LifeCycle.PERENNIAL),
            new Plant("Daisy", "데이지", Plant.LifeCycle.BIENNIAL),
            new Plant("Lily", "백합", Plant.LifeCycle.PERENNIAL),
            new Plant("Sunflower", "해바라기", Plant.LifeCycle.ANNUAL),
            new Plant("Daffodil", "수선화", Plant.LifeCycle.PERENNIAL),
            new Plant("Orchid", "난초", Plant.LifeCycle.PERENNIAL),
            new Plant("Marigold", "천수국", Plant.LifeCycle.ANNUAL),
            new Plant("Pansy", "팬지", Plant.LifeCycle.BIENNIAL),
            new Plant("Chrysanthemum", "국화", Plant.LifeCycle.BIENNIAL),
            new Plant("Petunia", "피튜니아", Plant.LifeCycle.ANNUAL),
            new Plant("Zinnia", "백일홍", Plant.LifeCycle.ANNUAL)
    );

    @Test
    public void testProcedural() {
        Plant.LifeCycle annual = Plant.LifeCycle.ANNUAL;

        // SELECT name FROM plant WHERE lifeCycle = 'annual';

        // 1. lifeCycle이 annual인 Plant 필터링
        for (Plant p : plants) {
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

        // SELECT name FROM plant WHERE lifeCycle = 'annual';

        plants.stream()
                .filter(p -> p.lifeCycle() == annual)
                .map(Plant::name)
                .forEach(System.out::println);
    }

    @Test
    public void testAnonymousClasses() {
        final Plant.LifeCycle annual = Plant.LifeCycle.ANNUAL; // 익명 클래스에서 참조되므로 final 또는 effectively final 이어야 함

        // 1. Predicate 익명 클래스: lifeCycle이 annual인 Plant 필터링
        Predicate<Plant> filterByAnnual = new Predicate<Plant>() {
            @Override
            public boolean test(Plant p) {
                return p.lifeCycle() == annual;
            }
        };

        // annual = Plant.LifeCycle.PERENNIAL; // final 또는 effectively final이 아니므로 컴파일 에러 발생

        // 2. Function 익명 클래스: Plant 객체에서 name(영문 이름) 추출
        Function<Plant, String> getNameFromPlant = new Function<Plant, String>() {
            @Override
            public String apply(Plant p) {
                return p.name();
            }
        };

        // 3. Consumer 익명 클래스: 문자열 출력
        Consumer<String> printName = new Consumer<String>() {
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

        // SELECT UPPER(name) FROM plant WHERE lifeCycle = 'annual';

        plants.stream()
                .filter(p -> p.lifeCycle() == annual)
                .map(p -> p.name().toUpperCase())
                .forEach(System.out::println);
    }
}
