package effectivejava.chapter06.item37;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    public void testPlant1() {

        @SuppressWarnings("unchecked")
        Set<Plant>[] plantsByLifeCycle = (Set<Plant>[]) new Set[Plant.LifeCycle.values().length];

        for (int i = 0; i < plantsByLifeCycle.length; i++) {
            plantsByLifeCycle[i] = new TreeSet<>();
        }

        for (Plant plant : plants) {
            plantsByLifeCycle[plant.lifeCycle().ordinal()].add(plant);
        }

        assertEquals("[백일홍, 천수국, 피튜니아, 해바라기]", plantsByLifeCycle[0].toString());
        assertEquals("[난초, 백합, 수선화, 장미, 튤립]", plantsByLifeCycle[1].toString());
        assertEquals("[국화, 데이지, 팬지]", plantsByLifeCycle[2].toString());
    }

    @Test
    public void testPlant2() {
        Map<Plant.LifeCycle, Set<Plant>> plantsByLifeCycle =
                new EnumMap<>(Plant.LifeCycle.class);

        for (Plant.LifeCycle lifeCycle : Plant.LifeCycle.values()) {
            plantsByLifeCycle.put(lifeCycle, new TreeSet<>());
        }

        for (Plant plant : plants) {
            plantsByLifeCycle.get(plant.lifeCycle()).add(plant);
        }

        assertEquals("[백일홍, 천수국, 피튜니아, 해바라기]",
                plantsByLifeCycle.get(Plant.LifeCycle.ANNUAL).toString());
        assertEquals("[난초, 백합, 수선화, 장미, 튤립]",
                plantsByLifeCycle.get(Plant.LifeCycle.PERENNIAL).toString());
        assertEquals("[국화, 데이지, 팬지]",
                plantsByLifeCycle.get(Plant.LifeCycle.BIENNIAL).toString());
    }


    @Test
    public void testPlant3() {
        Map<Plant.LifeCycle, Set<Plant>> plantsByLifeCycle =
                plants.stream().collect(
                        groupingBy(Plant::lifeCycle,
                                () -> new EnumMap<>(Plant.LifeCycle.class),
                                Collectors.toCollection(TreeSet::new)));

        assertEquals("[백일홍, 천수국, 피튜니아, 해바라기]",
                plantsByLifeCycle.get(Plant.LifeCycle.ANNUAL).toString());
        assertEquals("[난초, 백합, 수선화, 장미, 튤립]",
                plantsByLifeCycle.get(Plant.LifeCycle.PERENNIAL).toString());
        assertEquals("[국화, 데이지, 팬지]",
                plantsByLifeCycle.get(Plant.LifeCycle.BIENNIAL).toString());
    }
}
