package effectivejava.chapter02.item02;

public class NutritionFacts2 {
    private int servingSize = -1;   // 필수
    private int servings = -1;      // 필수
    private int calories = 0;       // 선택
    private int fat = 0;            // 선택
    private int sodium = 0;         // 선택
    private int carbohydrate = 0;   // 선택

    public NutritionFacts2() {}

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

    public void setServingSize(int servingSize) { this.servingSize = servingSize; }
    public void setServings(int servings) { this.servings = servings; }
    public void setCalories(int calories) { this.calories = calories; }
    public void setFat(int fat) { this.fat = fat; }
    public void setSodium(int sodium) { this.sodium = sodium; }
    public void setCarbohydrate(int carbohydrate) { this.carbohydrate = carbohydrate; }
}
