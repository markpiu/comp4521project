package comp4521.group_s;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProfileSetup extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;

    Button btnSave,btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_profile_setup);
        btnSave = findViewById(R.id.btn_save_profile);
        btnCancel = findViewById(R.id.btn_cancel_profile);
        EditText nameEditText = findViewById(R.id.username_profile);
        EditText emailEditText = findViewById(R.id.email_profile);
        DatePicker dobDatePicker = findViewById(R.id.date_of_birth_profile);
        RadioGroup genderRadioGroup = findViewById(R.id.gender_profile);
        EditText weightEditText = findViewById(R.id.weight_profile);
        EditText heightEditText = findViewById(R.id.height_profile);
        Spinner activityLevelSpinner = findViewById(R.id.activity_level_profile);
        Spinner fitnessGoalSpinner = findViewById(R.id.fitness_goal_profile);

        user = auth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), ProfileSetup.class);
            startActivity(intent);
            finish();
        }
        String user_id = user.getUid();
        SqlDataBaseHelper dbhelper = SqlDataBaseHelper.instanceOfDatabase(ProfileSetup.this);
        nameEditText.setText(dbhelper.GetUserName(user_id));
        String userEmail = user.getEmail();
        emailEditText.setText(userEmail);
        String dobStr = dbhelper.GetUserDOB(user_id);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date dob = sdf.parse(dobStr);
            cal.setTime(dob);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            dobDatePicker.init(year, month, day, null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String gender = dbhelper.GetUserGender(user_id);
        if (gender.equals("Male")) {
            genderRadioGroup.check(R.id.male_profile);
        } else {
            genderRadioGroup.check(R.id.female_profile);
        }

        weightEditText.setText(dbhelper.GetUserWeight(user_id));
        heightEditText.setText(dbhelper.GetUserHeight(user_id));
        String activityLevel = dbhelper.GetUserActivityLevel(user_id);
        int activityLevelIndex = Arrays.asList(getResources().getStringArray(R.array.activity_level_array)).indexOf(activityLevel);
        activityLevelSpinner.setSelection(activityLevelIndex);
        String fitnessGoal = dbhelper.GetUserFitnessGoal(user_id);
        int fitnessGoalIndex = Arrays.asList(getResources().getStringArray(R.array.fitness_goal_array)).indexOf(fitnessGoal);
        fitnessGoalSpinner.setSelection(fitnessGoalIndex);



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the values from the input fields
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                int day = dobDatePicker.getDayOfMonth();
                int month = dobDatePicker.getMonth() + 1; // DatePicker month is zero-based
                int year = dobDatePicker.getYear();
                String dob = String.format("%04d-%02d-%02d", year, month, day); // Format date as YYYY-MM-DD
                int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
                String gender = "";
                if (selectedGenderId == R.id.male_profile) {
                    gender = "Male";
                } else if (selectedGenderId == R.id.female_profile) {
                    gender = "Female";
                }
                String weight = weightEditText.getText().toString();
                if (weight.isEmpty()) {
                    weight = "50"; // Default weight value
                }
                String height = heightEditText.getText().toString();
                if (height.isEmpty()) {
                    height = "160"; // Default height value
                }
                String activityLevel = activityLevelSpinner.getSelectedItem().toString();
                String fitnessGoal = fitnessGoalSpinner.getSelectedItem().toString();


                // Update the user profile in the database
                dbhelper.UpdateUserprofile(user_id, name, email, dob, gender, weight, height, activityLevel, fitnessGoal);

                if(!email.equals(userEmail))
                {
                    user.updateEmail(email);
                    Toast.makeText(ProfileSetup.this, " Profile Saved with email Updated.",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ProfileSetup.this, " Profile Saved.",
                            Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}