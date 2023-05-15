package comp4521.group_s;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;

public class NutrientTrackingActivity extends AppCompatActivity {

    Button btnNext, btnNutrientInput;
    int counter;
    int current_ID;
    LineChart lineChart;
    ArrayList<Entry> data = new ArrayList<>();
    DatabaseManager dbManager;
    Cursor cursor;
    String nutrientChosen;
    Spinner spinner;
    float averageConsumption = 0;

    FirebaseAuth auth;
    FirebaseUser user;

    androidx.appcompat.widget.Toolbar toolbar;

    @SuppressLint("Range")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrient_tracking);
        lineChart = findViewById(R.id.nutrientLineChart);
        btnNutrientInput = findViewById(R.id.btnNutrientInput);
        btnNext = findViewById(R.id.btnNext);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(NutrientTrackingActivity.this,
                    android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.display_nutrients_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        dbManager = new DatabaseManager(this);
        try {
            dbManager.open();
            Log.i("database open: ", "Database open successfully");
        }
        catch (Exception e){
            Log.i("database open: ", "Database open exception");
            e.printStackTrace();
        }

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                counter = 1;
                data.removeAll(data);
                nutrientChosen = spinner.getSelectedItem().toString();
                cursor = dbManager.query(nutrientChosen);
                if (cursor.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String amount = cursor.getString(cursor.getColumnIndex(DatabaseHelper.AMOUNT));
                        counter++;
                        data.add(new Entry(counter, Float.parseFloat(amount)));

                    } while (cursor.moveToNext());
                }

                refreshLineChart();

                btnNext.setOnClickListener(view -> {
                    int currentPosition = spinner.getSelectedItemPosition();
                    //Calculate the position of the next item
                    int nextPosition = currentPosition + 1;

                    if (nextPosition + 1 >= spinner.getCount()) {
                        btnNext.setText("Back to Menu");
                    }
                    //If the next position exceeds the total number of items, loop back to the first item (0 position)
                    if (nextPosition >= spinner.getCount()) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {

                        //Set the next item as selected in the Spinner
                        spinner.setSelection(nextPosition);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    void refreshLineChart() {
        data.removeAll(data);
        nutrientChosen = spinner.getSelectedItem().toString();
        cursor = dbManager.query(nutrientChosen);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String amount = cursor.getString(cursor.getColumnIndex(DatabaseHelper.AMOUNT));
                counter++;
                data.add(new Entry(counter, Float.parseFloat(amount)));

            } while (cursor.moveToNext());
        }
        Log.i("DATA", data.toString());
        LineDataSet lineDataSet;
        averageConsumption = 0;
        if (data.size() < 7) {
            lineDataSet = new LineDataSet(data, nutrientChosen + " Consumed");
        } else {
            ArrayList<Entry> lastSevenEntries = new ArrayList<>(data.subList(data.size() - 7, data.size()));
            for (Entry b : lastSevenEntries) {
                b.setX(b.getX() - (data.size() - 6));
                averageConsumption += b.getY();
            }
            averageConsumption /= 7;
            lineDataSet = new LineDataSet(lastSevenEntries, nutrientChosen + " Consumed");
            LimitLine ll1 = new LimitLine(averageConsumption, "Average " + nutrientChosen + " Consumed");
            ll1.setLineWidth(4f);
            ll1.enableDashedLine(10f, 10f, 0f);
            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll1.setTextSize(10f);

            YAxis leftAxis = lineChart.getAxisLeft();
            leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
            leftAxis.addLimitLine(ll1);
        }
        lineDataSet.setLineWidth(3f);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setValueTextSize(16f);

        LineData lineData = new LineData(lineDataSet);

        lineChart.setData(lineData);
        lineChart.getDescription().setText(nutrientChosen + " Consumed This Week");
        lineChart.animateX(1200, Easing.EaseInSine);
    }

    public void showMealInput(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter your meals eaten today:");
        LinearLayout inputPrompt = new LinearLayout(this);
        inputPrompt.setOrientation(LinearLayout.VERTICAL);

        final Spinner breakfastSpinner = new Spinner(this);
        Food[] breakfasts = { new Food("None", 0f, 0f, 0f, 0f, 0f),
                              new Food("Toast", 64f, 12f, 2f,0f, 14.2f),
                              new Food("Cereal", 100f, 25f, 3.6f, 10f, 20f),
                              new Food("Pancake", 250f, 37f, 8f, 28f, 25f)};
        ArrayList<String> breakfast_names = new ArrayList<>();
        for (Food f : breakfasts)
            breakfast_names.add(f.getName());
        final ArrayAdapter<String> breakfastAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, breakfast_names);
        breakfastSpinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        breakfastSpinner.setAdapter(breakfastAdapter);

        final Spinner lunchSpinner = new Spinner(this);
        Food[] lunches = { new Food("None", 0f, 0f, 0f, 0f, 0f),
                           new Food("Salad", 83f, 10f, 2f,0f, 14.2f),
                           new Food("Sandwich", 361f, 32.5f, 19.3f, 15f, 25f),
                           new Food("Instant Noodles", 385f, 55.7f, 7.9f, 32f, 30f)};
        ArrayList<String> lunch_names = new ArrayList<>();
        for (Food f : lunches)
            lunch_names.add(f.getName());
        final ArrayAdapter<String> lunchAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, lunch_names);
        lunchSpinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        lunchSpinner.setAdapter(lunchAdapter);

        final Spinner dinnerSpinner = new Spinner(this);
        Food[] dinners = { new Food("None", 0f, 0f, 0f, 0f, 0f),
                           new Food("Burger and Fries", 900f, 88f, 34f,0f, 14.2f),
                           new Food("Steak", 537f, 0f, 78f, 30f, 60f),
                           new Food("Spaghetti Carbonara", 1018f, 133f, 44f, 28f, 50f)};
        ArrayList<String> dinner_names = new ArrayList<>();
        for (Food f : dinners)
            dinner_names.add(f.getName());
        final ArrayAdapter<String> dinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, dinner_names);
        dinnerSpinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        dinnerSpinner.setAdapter(dinnerAdapter);

        alert.setPositiveButton("Submit Meals Eaten", (dialog, whichButton) -> {
            Food breakfastChosen = getFood(breakfastSpinner.getSelectedItem().toString(), breakfasts);
            Food lunchChosen = getFood(lunchSpinner.getSelectedItem().toString(), lunches);
            Food dinnerChosen = getFood(dinnerSpinner.getSelectedItem().toString(), dinners);

            String[] inputTypes = new String[]{"Calories (kcal)", "Carbohydrates (g)", "Protein (g)", "Vitamins (mg)", "Minerals (mg)"};

            for (int i = 0; i < 5; i++) {
                current_ID = Long.valueOf(dbManager.getSize()).intValue() + 1;
                Long date = new Date().getTime();
                float totalAmount = breakfastChosen.getValue(i) + lunchChosen.getValue(i) + dinnerChosen.getValue(i);
                dbManager.insert(String.valueOf(current_ID), date, inputTypes[i], totalAmount);
            }
            refreshLineChart();
            Toast.makeText(getApplicationContext(), "Added!", Toast.LENGTH_SHORT).show();
        });

        inputPrompt.addView(breakfastSpinner);
        inputPrompt.addView(lunchSpinner);
        inputPrompt.addView(dinnerSpinner);

        alert.setView(inputPrompt);
        alert.create().show();
    }

    private Food getFood(String name, Food[] foods) {
        for (Food f : foods)
            if (f.getName().equals(name)) {
                return f;
            }
        return null;
    }
}
