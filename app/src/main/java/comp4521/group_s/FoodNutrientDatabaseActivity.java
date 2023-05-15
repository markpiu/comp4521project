package comp4521.group_s;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class FoodNutrientDatabaseActivity extends AppCompatActivity {
    ListView lv;
    ArrayAdapter adapter;
    ArrayList<String> list1;
    SearchView sv;
    androidx.appcompat.widget.Toolbar toolbar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_food_database);
        toolbar2 = findViewById(R.id.toolbar2);

        toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

        });

        lv = (ListView) findViewById(R.id.list);
        sv = (SearchView) findViewById(R.id.search);
        list1 = new ArrayList<String>();
        list1.add("Poached eggs with broccoli, tomatoes & wholemeal flatbread\nkcal: 383, fat: 17g, saturates: 4.4g, carbs: 31g, sugars: 4g, fibre: 9g, protein: 22g, salt: 0.4g");
        list1.add("Easy protein pancakes\nkcal: 437, fat: 16g, saturates: 4g, carbs: 39g, sugars: 9g, fibre: 4g, protein: 31g, salt: 1.4g");
        list1.add("Porridge with blueberry compote\nkcal: 214, fat: 4g, saturates: 1g, carbs: 35g, sugars: 9g, fibre: 7g, protein: 13g, salt: 0.05g");
        list1.add("Green shakshuka\nkcal: 337, fat: 20g, saturates: 5g, carbs: 13g, sugars: 8g, fibre: 7g, protein: 22g, salt: 0.6g");
        list1.add("Chicken satay salad\nkcal: 353, fat: 10g, saturates: 2g, carbs: 24g, sugars: 21g, fibre: 7g, protein: 38g, salt: 1.6g");
        list1.add("Salmon salad with sesame dressing\nkcal: 478, fat: 22g, saturates: 4g, carbs: 35g, sugars: 17g, fibre: 9g, protein: 30g, salt: 0.99g");
        list1.add("Chicken with Spanish-style butter beans\nkcal: 429, fat: 22g, saturates: 5g, carbs: 21g, sugars: 4g, fibre: 10g, protein: 33g, salt: 0.5g");
        list1.add("Healthy Turkish meatloaf\nkcal: 291, fat: 10g, saturates: 3g, carbs: 27g, sugars: 14g, fibre: 9g, protein: 19g, salt: 0.4g");
        list1.add("Steak burrito bowl\nkcal: 549, fat: 24g, saturates: 7g, carbs: 49g, sugars: 10g, fibre: 3g, protein: 32g, salt: 0.8g");
        list1.add("Oven-baked pork chops\nkcal: 805, fat: 38g, saturates: 10g, carbs: 67g, sugars: 31g, fibre: 7g, protein: 46g, salt: 1.69g");
        list1.add("Slow cooker meatballs\nkcal: 260, fat: 5g, saturates: 1g, carbs: 21g, sugars: 10g, fibre: 5g, protein: 29g, salt: 0.21g");

        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list1);

        lv.setAdapter(adapter);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText){
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
    public class Recipes {
        private String name;
        private double Kcal;
        private double fat;
        private double saturates;
        private double carbs;
        private double sugars;
        private double fibre;
        private double protein;
        private double salt;
        private String Ingredients;
        public Recipes(String name, double Kcal, double fat, double saturates, double carbs, double sugars, double fibre, double protein, double salt, String Ingredients) {
            this.name = name;
            this.Kcal = Kcal;
            this.fat = fat;
            this.saturates = saturates;
            this.carbs = carbs;
            this.sugars = sugars;
            this.fibre = fibre;
            this.protein = protein;
            this.salt = salt;
            this.Ingredients = Ingredients;
        }
    }
}