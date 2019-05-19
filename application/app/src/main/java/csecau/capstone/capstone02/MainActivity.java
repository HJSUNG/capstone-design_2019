package csecau.capstone.capstone02;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private Button diaryButton, glucoseButton, medicationButton, logoutButton, exerciseButton, mealButton;
    public static String user_id ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        glucoseButton = (Button)findViewById(R.id.GlucoseButton);
        medicationButton = (Button)findViewById(R.id.MedicationButton);
        exerciseButton = (Button)findViewById(R.id.ExerciseButton);
        mealButton = (Button)findViewById(R.id.MealButton);
        diaryButton = (Button) findViewById(R.id.DiaryButton);
        logoutButton = (Button) findViewById(R.id.logoutButton);



        glucoseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GlucoseActivity.class);
                startActivity(intent);
            }
        });

        medicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MedicationActivity.class);
                startActivity(intent);
            }
        });

        exerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExerciseActivity.class);
                startActivity(intent);
            }
        });

        mealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MealActivity.class);
                startActivity(intent);
            }
        });

        diaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DiaryActivity.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(MainActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        LineChart lineChart = (LineChart) findViewById(R.id.chart);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(100, 2));
        entries.add(new Entry(123, 3));
        entries.add(new Entry(100, 25));
        entries.add(new Entry(200, 26));
        entries.add(new Entry(180, 29));
        entries.add(new Entry(123, 30));
        entries.add(new Entry(100, 32));
        entries.add(new Entry(120, 33));
        entries.add(new Entry(120, 34));
        entries.add(new Entry(90, 36));
        entries.add(new Entry(120, 81 ));

        LineDataSet dataset = new LineDataSet(entries, "Blood Glucose");
        dataset.setColor(Color.parseColor("#FF0000"));
        dataset.setCircleColor(R.color.black);
        dataset.setCircleColorHole(R.color.black);


        ArrayList<String> labels = new ArrayList<String>();
        labels.add("04-07 6am");
        labels.add("04-07 12am");
        labels.add("04-07 6pm");
        labels.add("04-07 12pm");
        labels.add("04-08 6am");
        labels.add("04-08 12am");
        labels.add("04-08 6pm");
        labels.add("04-08 12pm");
        labels.add("04-09 6am");
        labels.add("04-09 12am");
        labels.add("04-09 6pm");
        labels.add("04-09 12pm");
        labels.add("04-10 6am");
        labels.add("04-10 12am");
        labels.add("04-10 6pm");
        labels.add("04-10 12pm");
        labels.add("04-11 6am");
        labels.add("04-11 12am");
        labels.add("04-11 6pm");
        labels.add("04-11 12pm");
        labels.add("04-12 6am");
        labels.add("04-12 12am");
        labels.add("04-12 6pm");
        labels.add("04-12 12pm");
        labels.add("04-13 6am");
        labels.add("04-13 12am");
        labels.add("04-13 6pm");
        labels.add("04-13 12pm");
        labels.add("04-14 6am");
        labels.add("04-14 12am");
        labels.add("04-14 6pm");
        labels.add("04-14 12pm");
        labels.add("04-15 6am");
        labels.add("04-15 12am");
        labels.add("04-15 6pm");
        labels.add("04-15 12pm");
        labels.add("04-16 6am");
        labels.add("04-16 12am");
        labels.add("04-16 6pm");
        labels.add("04-16 12pm");
        labels.add("04-17 6am");
        labels.add("04-17 12am");
        labels.add("04-17 6pm");
        labels.add("04-17 12pm");
        labels.add("04-18 6am");
        labels.add("04-18 12am");
        labels.add("04-18 6pm");
        labels.add("04-18 12pm");
        labels.add("04-19 6am");
        labels.add("04-19 12am");
        labels.add("04-19 6pm");
        labels.add("04-19 12pm");
        labels.add("04-20 6am");
        labels.add("04-20 12am");
        labels.add("04-20 6pm");
        labels.add("04-20 12pm");
        labels.add("04-21 6am");
        labels.add("04-21 12am");
        labels.add("04-21 6pm");
        labels.add("04-21 12pm");
        labels.add("04-22 6am");
        labels.add("04-22 12am");
        labels.add("04-22 6pm");
        labels.add("04-22 12pm");
        labels.add("04-23 6am");
        labels.add("04-23 12am");
        labels.add("04-23 6pm");
        labels.add("04-23 12pm");
        labels.add("04-24 6am");
        labels.add("04-24 12am");
        labels.add("04-24 6pm");
        labels.add("04-24 12pm");
        labels.add("04-25 6am");
        labels.add("04-25 12am");
        labels.add("04-25 6pm");
        labels.add("04-25 12pm");
        labels.add("04-26 6am");
        labels.add("04-26 12am");
        labels.add("04-26 6pm");
        labels.add("04-26 12pm");
        labels.add("04-27 6am");
        labels.add("04-27 12am");
        labels.add("04-27 6pm");
        labels.add("04-27 12pm");


        LineData data = new LineData(labels, dataset);
//        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        /*dataset.setDrawCubic(true); //선 둥글게 만들기
        dataset.setDrawFilled(true); //그래프 밑부분 색칠*/

        lineChart.setData(data);
        lineChart.animateY(5000);
    }
}
