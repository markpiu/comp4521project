package comp4521.group_s;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;

public class ProgressTrackingActivity extends AppCompatActivity {

    Button btnNext;
    EditText progressInput;
    int current_ID;
    int counter;
    LineChart lineChart;
    ArrayList<Entry> data = new ArrayList<>();
    DatabaseManager dbManager;
    Cursor cursor;
    String progressChosen;
    Spinner spinner;

    FirebaseAuth auth;
    FirebaseUser user;

    androidx.appcompat.widget.Toolbar toolbar;

    @SuppressLint("Range")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_tracking);
        btnNext = findViewById(R.id.btnNext);
        lineChart = findViewById(R.id.progressLineChart);
        progressInput = findViewById(R.id.progressInput);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ProgressTrackingActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.display_progress_array));
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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                counter = 1;
                data.removeAll(data);
                progressChosen = spinner.getSelectedItem().toString();
                cursor = dbManager.query(progressChosen);
                progressInput.setText("");
                progressInput.setHint("Enter your " + progressChosen + " measurements...");
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

                progressInput.setOnEditorActionListener((v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        String val = progressInput.getText().toString();
                        if (!val.isEmpty()) {
                            counter++;
                            Long date = new Date().getTime();
                            current_ID = Long.valueOf(dbManager.getSize()).intValue() + 1;
                            dbManager.insert(String.valueOf(current_ID), date, progressChosen, Float.parseFloat(val));
                            Log.i("Log", "NEW ENTRY ADDED");
                            data.add(new Entry(counter, Float.parseFloat(val)));
                            progressInput.setText("");
                            refreshLineChart();
                        }
                        return true;
                    }
                    return false;
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    void refreshLineChart() {
        LineDataSet lineDataSet;
        if (data.size() < 7) {
            lineDataSet = new LineDataSet(data, progressChosen);
        } else {
            ArrayList<Entry> lastSevenEntries = new ArrayList<>(data.subList(data.size() - 7, data.size()));
            lineDataSet = new LineDataSet(lastSevenEntries, progressChosen);
        }
        lineDataSet.setLineWidth(3f);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setValueTextSize(16f);

        LineData lineData = new LineData(lineDataSet);

        lineChart.setData(lineData);
        lineChart.getDescription().setText(progressChosen + " Measurements This Week.");
        lineChart.animateX(1200, Easing.EaseInSine);
    }
}
