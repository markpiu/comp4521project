package comp4521.group_s;

public class Food {
    private String name;
    private Float[] values;

    public Food(String name, Float calories, Float carbohydrates, Float protein, Float vitamins, Float minerals) {
        this.name = name;
        this.values = new Float[]{ calories, carbohydrates, protein, vitamins, minerals };
    }

    public String getName() {
        return name;
    }

    public Float getValue(int index) {
        return values[index];
    }
}