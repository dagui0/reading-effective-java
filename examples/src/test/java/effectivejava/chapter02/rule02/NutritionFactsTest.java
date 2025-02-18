package effectivejava.chapter02.rule02;

import org.junit.jupiter.api.Test;

public class NutritionFactsTest {

    @Test
    public void testNutritionFacts1() {
        NutritionFacts1 nf1 = new NutritionFacts1(240, 8, 100, 3, 25, 27);
        nf1.toString();
    }

    @Test
    public void testNutritionFacts2() {
        NutritionFacts2 nf2 = new NutritionFacts2();
        nf2.setServingSize(240);
        nf2.setServings(8);
        nf2.setCalories(100);
        nf2.setFat(3);
        nf2.setSodium(35);
        nf2.setCarbohydrate(27);
        nf2.toString();
    }

    @Test
    public void testNutritionFacts3() {
        NutritionFacts3 nf3 = new NutritionFacts3.Builder(240, 8)
                .calories(100).fat(3).sodium(35).carbohydrate(27)
                .build();
        nf3.toString();
    }
}
