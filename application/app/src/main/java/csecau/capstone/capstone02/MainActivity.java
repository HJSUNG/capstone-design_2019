package csecau.capstone.capstone02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button diaryButton, glucoseButton, medicationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        diaryButton = (Button) findViewById(R.id.DiaryButton);
        glucoseButton = (Button)findViewById(R.id.GlucoseButton);
        medicationButton = (Button)findViewById(R.id.MedicationButton);


        diaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DiaryActivity.class);
                startActivity(intent);
            }
        });

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

        LineChart lineChart = (LineChart) findViewById(R.id.chart);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(2f, 3));
        entries.add(new Entry(18f, 4));
        entries.add(new Entry(9f, 5));
        entries.add(new Entry(16f, 6));
        entries.add(new Entry(5f, 7));
        entries.add(new Entry(3f, 8));
        entries.add(new Entry(7f, 10));
        entries.add(new Entry(9f, 11));

        LineDataSet dataset = new LineDataSet(entries, "# of Calls");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("0 am");
        labels.add("2 am");
        labels.add("4 am");
        labels.add("6 am");
        labels.add("8 am");
        labels.add("10 am");
        labels.add("12 pm");
        labels.add("2 pm");
        labels.add("4 pm");
        labels.add("6 pm");
        labels.add("8 pm");
        labels.add("10 pm");

        LineData data = new LineData(labels, dataset);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        /*dataset.setDrawCubic(true); //선 둥글게 만들기
        dataset.setDrawFilled(true); //그래프 밑부분 색칠*/

        lineChart.setData(data);
        lineChart.animateY(5000);
    }
}
