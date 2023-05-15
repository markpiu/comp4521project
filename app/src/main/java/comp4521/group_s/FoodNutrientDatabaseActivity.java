package comp4521.group_s;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FoodNutrientDatabaseActivity extends AppCompatActivity {
    ListView lv;
    ArrayAdapter adapter;
    ArrayList<String> list1;
    SearchView sv;
    androidx.appcompat.widget.Toolbar toolbar2;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_food_database);
        toolbar2 = findViewById(R.id.toolbar2);

        toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealPlanning.class);
                startActivity(intent);
                finish();
            }
        });

        db = FirebaseFirestore.getInstance();
        lv = findViewById(R.id.list);
        sv = findViewById(R.id.search);
        list1 = new ArrayList<>();

        db.collection("FoodNutrientData").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Recipes recipe = document.toObject(Recipes.class);
                        String item = recipe.name + "\n" +
                                "kcal: " + recipe.kcal + ", " +
                                "fat: " + recipe.fat + "g, " +
                                "saturates: " + recipe.saturates + "g, " +
                                "carbs: " + recipe.carbs + "g, " +
                                "sugars: " + recipe.sugars + "g, " +
                                "fibre: " + recipe.fibre + "g, " +
                                "protein: " + recipe.protein + "g, " +
                                "salt: " + recipe.salt + "g";
                        list1.add(item);
                    }
                    adapter = new ArrayAdapter<>(FoodNutrientDatabaseActivity.this, android.R.layout.simple_list_item_1, list1);
                    lv.setAdapter(adapter);
                } else {

                }
            }
        });

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

}