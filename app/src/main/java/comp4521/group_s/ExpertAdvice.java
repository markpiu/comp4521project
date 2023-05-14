package comp4521.group_s;

import static comp4521.group_s.SqlDataBaseHelper.dbhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ExpertAdvice extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbar3;
    TextView PersonalData;
    TextView BMI;
    ImageButton ArticleBtn, ProteinBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_expert_advice);
        toolbar3 = findViewById(R.id.toolbar3);

        toolbar3.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

        });
        ArticleBtn = findViewById(R.id.ArticleBtn);
        ArticleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ArticleWebsite.class);
                startActivity(intent);
                finish();
            }

        });
        PersonalData = findViewById(R.id.personalData);
        BMI = findViewById(R.id.BMI);
        PersonalData.setText("Name: " + dbhelper.GetUserName(FirebaseAuth.getInstance().getCurrentUser().getUid())+"\n"
        + "Height: "+ dbhelper.GetUserHeight(FirebaseAuth.getInstance().getCurrentUser().getUid())+"\n"+
                "Weight: " + dbhelper.GetUserWeight(FirebaseAuth.getInstance().getCurrentUser().getUid())+"\nFitness Goal: "+
                dbhelper.GetUserFitnessGoal(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        double height = Double.valueOf(dbhelper.GetUserHeight(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        double weight = Double.valueOf(dbhelper.GetUserWeight(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        double ProteinRate = 1.8;
        String Goal = dbhelper.GetUserFitnessGoal(FirebaseAuth.getInstance().getCurrentUser().getUid());
        if (Goal == "Weight Loss"){
            ProteinRate = 1.4;
        }else if(Goal =="None"||Goal =="Improve General Health")
            ProteinRate = 0.8;
        BMI.setText("Body Mass Index (kg/m^2): "+String.format("%.2f",(weight/height/height*10000))+"\n"+
                "Protein per day to achieve fitness goal: " + weight*ProteinRate);

    }
}