package comp4521.group_s;

public class Recipes {
    public String name;
    public double kcal;
    public double fat;
    public double saturates;
    public double carbs;
    public double sugars;
    public double fibre;
    public double protein;
    public double salt;
    public String ingredients;

    public Recipes() {}  // Needed for Firestore

    public Recipes(String name, double kcal, double fat, double saturates, double carbs, double sugars, double fibre, double protein, double salt, String ingredients) {
        this.name = name;
        this.kcal = kcal;
        this.fat = fat;
        this.saturates = saturates;
        this.carbs = carbs;
        this.sugars = sugars;
        this.fibre = fibre;
        this.protein = protein;
        this.salt = salt;
        this.ingredients = ingredients;
    }

    // Getters (if necessary)
    public String getName() { return name; }
    public double getkcal() { return kcal; }
    public double getFat() { return fat; }
    public double getSaturates() { return saturates; }
    public double getCarbs() { return carbs; }
    public double getSugars() { return sugars; }
    public double getFibre() { return fibre; }
    public double getProtein() { return protein; }
    public double getSalt() { return salt; }
    public String getIngredients() { return ingredients; }
}
