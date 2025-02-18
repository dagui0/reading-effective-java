package effectivejava.chapter02.rule02;

public class NutritionFacts3 {
    private final int servingSize;      // 필수
    private final int servings;         // 필수
    private final int calories;         // 선택
    private final int fat;              // 선택
    private final int sodium;           // 선택
    private final int carbohydrate;     // 선택

    public static class Builder {
        private final int servingSize;  // 필수
        private final int servings;     // 필수
        private int calories = 0;       // 선택
        private int fat = 0;            // 선택
        private int sodium = 0;         // 선택
        private int carbohydrate = 0;   // 선택

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }
        public Builder calories(int calories) { this.calories = calories; return this; }
        public Builder fat(int fat) { this.fat = fat; return this; }
        public Builder sodium(int sodium) { this.sodium = sodium; return this; }
        public Builder carbohydrate(int carbohydrate) { this.carbohydrate = carbohydrate; return this; }

        public NutritionFacts3 build() {
            return new NutritionFacts3(this);
        }
    }

    private NutritionFacts3(Builder builder) {
        this.servingSize = builder.servingSize;
        this.servings = builder.servings;
        this.calories = builder.calories;
        this.fat = builder.fat;
        this.sodium = builder.sodium;
        this.carbohydrate = builder.carbohydrate;
    }

    public int getServingSize() {
        return servingSize;
    }
    public int getServings() {
        return servings;
    }
    public int getCalories() {
        return calories;
    }
    public int getFat() {
        return fat;
    }
    public int getSodium() {
        return sodium;
    }
    public int getCarbohydrate() {
        return carbohydrate;
    }
}
