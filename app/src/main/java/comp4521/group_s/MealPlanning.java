package comp4521.group_s;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MealPlanning extends AppCompatActivity {


    FirebaseAuth auth;
    FirebaseUser user;

    androidx.appcompat.widget.Toolbar toolbar;
    Button btnFoodDatabase,btnNearbyFood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_planning);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnFoodDatabase = findViewById(R.id.btnFoodDatabase);
        btnNearbyFood = findViewById(R.id.btnFindFood);

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnFoodDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FoodNutrientDatabaseActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnNearbyFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Nearbyfood.class);
                startActivity(intent);
                finish();
            }
        });
    }


}