package comp4521.group_s;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    ImageButton btnLogout,btnProfile,btnMealPlanning, btnNutrient, btnProgress, btnExpertadvice, btnAIadvice, btnSetting;
    TextView textViewHiUser;
    FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        auth = FirebaseAuth.getInstance();
        btnLogout = findViewById(R.id.btnLogout);
        btnProfile = findViewById(R.id.btnProfile);
        btnMealPlanning = findViewById(R.id.btnMealPlanning);
        btnNutrient = findViewById(R.id.BtnNutrient);
        btnProgress = findViewById(R.id.BtnProgress);
        btnExpertadvice = findViewById(R.id.BtnExpertadvice);
        btnAIadvice = findViewById(R.id.BtnAIadvice);
        btnSetting = findViewById(R.id.BtnSetting);
        textViewHiUser = findViewById(R.id.user_details);
        user = auth.getCurrentUser();
        SqlDataBaseHelper dbhelper = SqlDataBaseHelper.instanceOfDatabase(MainActivity.this);

        if (user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else {
            textViewHiUser.setText("Hi " + dbhelper.GetUserName(user.getUid()) + "!");
        }


        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileSetup.class);
                startActivity(intent);
                finish();
            }
        });

        btnMealPlanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealPlanning.class);
                startActivity(intent);
                finish();
            }
        });

        btnNutrient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class); //todo
                startActivity(intent);
                finish();
            }
        });

        btnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class); //todo
                startActivity(intent);
                finish();
            }
        });

        btnExpertadvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExpertAdvice.class);
                startActivity(intent);
                finish();
            }
        });

        btnAIadvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AIAdvice.class);
                startActivity(intent);
                finish();
            }
        });


        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class); //todo
                startActivity(intent);
                finish();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}