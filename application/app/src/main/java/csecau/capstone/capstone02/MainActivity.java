package csecau.capstone.capstone02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String[] glucose_list;

    private Button diaryButton, glucoseButton, medicationButton, logoutButton, exerciseButton, mealButton;
    public static String user_id ="";
    private ListView main_alarm;

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

        main_alarm = (ListView)findViewById(R.id.main_medication_alarm);

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


//        alarm_listviewAdapter adapter;
//
//        adapter = new alarm_listviewAdapter();
//        main_alarm.setAdapter(adapter);



//        LineChart lineChart = (LineChart) findViewById(R.id.chart);
//        ArrayList<Entry> entries = new ArrayList<>();



//        entries.add(new Entry(100, 2));

//        LineDataSet dataset = new LineDataSet(entries, "Blood Glucose");
//        dataset.setColor(Color.parseColor("#FF0000"));
//        dataset.setCircleColor(R.color.black);
//        dataset.setCircleColorHole(R.color.black);


//        ArrayList<String> labels = new ArrayList<String>();
//        labels.add("04-07 6am");

//        LineData data = new LineData(labels, dataset);
//        dataset.setColors(ColorTemplate.COLORFUL_COLORS);

//        ArrayList<Entry> entries = new ArrayList<>();
//        for(int i=0;i<glucose_list.length;i++){
//
//        }


//        entries.add(new Entry(100, 2));
//        entries.add(new Entry(123, 3));
//        entries.add(new Entry(100, 25));
//        entries.add(new Entry(200, 26));
//        entries.add(new Entry(180, 29));
//        entries.add(new Entry(123, 30));
//        entries.add(new Entry(100, 32));
//        entries.add(new Entry(120, 33));
//        entries.add(new Entry(120, 34));
//        entries.add(new Entry(90, 36));
//        entries.add(new Entry(120, 81 ));

//        LineDataSet dataset = new LineDataSet(entries, "Blood Glucose");
//        dataset.setColor(Color.parseColor("#FF0000"));
//        dataset.setCircleColor(R.color.black);
//        dataset.setCircleColorHole(R.color.black);


        ArrayList<String> labels = new ArrayList<String>();
//        labels.add("04-07 6am");
//        labels.add("04-07 12am");
//        labels.add("04-07 6pm");
//        labels.add("04-07 12pm");
//        labels.add("04-08 6am");
//        labels.add("04-08 12am");
//        labels.add("04-08 6pm");
//        labels.add("04-08 12pm");
//        labels.add("04-09 6am");
//        labels.add("04-09 12am");
//        labels.add("04-09 6pm");
//        labels.add("04-09 12pm");
//        labels.add("04-10 6am");
//        labels.add("04-10 12am");
//        labels.add("04-10 6pm");
//        labels.add("04-10 12pm");
//        labels.add("04-11 6am");
//        labels.add("04-11 12am");
//        labels.add("04-11 6pm");
//        labels.add("04-11 12pm");
//        labels.add("04-12 6am");
//        labels.add("04-12 12am");
//        labels.add("04-12 6pm");
//        labels.add("04-12 12pm");
//        labels.add("04-13 6am");
//        labels.add("04-13 12am");
//        labels.add("04-13 6pm");
//        labels.add("04-13 12pm");
//        labels.add("04-14 6am");
//        labels.add("04-14 12am");
//        labels.add("04-14 6pm");
//        labels.add("04-14 12pm");
//        labels.add("04-15 6am");
//        labels.add("04-15 12am");
//        labels.add("04-15 6pm");
//        labels.add("04-15 12pm");
//        labels.add("04-16 6am");
//        labels.add("04-16 12am");
//        labels.add("04-16 6pm");
//        labels.add("04-16 12pm");
//        labels.add("04-17 6am");
//        labels.add("04-17 12am");
//        labels.add("04-17 6pm");
//        labels.add("04-17 12pm");
//        labels.add("04-18 6am");
//        labels.add("04-18 12am");
//        labels.add("04-18 6pm");
//        labels.add("04-18 12pm");
//        labels.add("04-19 6am");
//        labels.add("04-19 12am");
//        labels.add("04-19 6pm");
//        labels.add("04-19 12pm");
//        labels.add("04-20 6am");
//        labels.add("04-20 12am");
//        labels.add("04-20 6pm");
//        labels.add("04-20 12pm");
//        labels.add("04-21 6am");
//        labels.add("04-21 12am");
//        labels.add("04-21 6pm");
//        labels.add("04-21 12pm");
//        labels.add("04-22 6am");
//        labels.add("04-22 12am");
//        labels.add("04-22 6pm");
//        labels.add("04-22 12pm");
//        labels.add("04-23 6am");
//        labels.add("04-23 12am");
//        labels.add("04-23 6pm");
//        labels.add("04-23 12pm");
//        labels.add("04-24 6am");
//        labels.add("04-24 12am");
//        labels.add("04-24 6pm");
//        labels.add("04-24 12pm");
//        labels.add("04-25 6am");
//        labels.add("04-25 12am");
//        labels.add("04-25 6pm");
//        labels.add("04-25 12pm");
//        labels.add("04-26 6am");
//        labels.add("04-26 12am");
//        labels.add("04-26 6pm");
//        labels.add("04-26 12pm");
//        labels.add("04-27 6am");
//        labels.add("04-27 12am");
//        labels.add("04-27 6pm");
//        labels.add("04-27 12pm");


//        LineData data = new LineData(labels, dataset);
//        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //

        /*dataset.setDrawCubic(true); //선 둥글게 만들기
        dataset.setDrawFilled(true); //그래프 밑부분 색칠*/

//        lineChart.setData(data);
//        lineChart.animateY(5000);

        Getglucoselist getglucoselist = new Getglucoselist();
        getglucoselist.execute("http://capstone02.cafe24.com/retrieve_glucose_graph.php", user_id);
    }

    class Getglucoselist extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String result_string = result;

            glucose_list = result_string.split("<br>");

            ListView listview;
            glucose_listviewAdapter adapter;

            adapter = new glucose_listviewAdapter();

//            listview = (ListView) findViewById(R.id.glucoselistview);
//            listview.setAdapter(adapter);
            LineChart lineChart = (LineChart) findViewById(R.id.chart);
            ArrayList<Entry> entries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<String>();
//            ArrayList date_hour= new ArrayList();

            String test_sentence = glucose_list[0];
            boolean test_contains = result.contains("comma");
            int i=0;
            if (result.contains("<comma>")) {
                for (String glucose : glucose_list) {
                    String glucose_split[] = glucose.split("<comma>");
//                    adapter.addItem(glucose_split[1], glucose_split[2]);
                    String date = glucose_split[1].substring(5,13);
                    entries.add(new Entry(Integer.parseInt(glucose_split[2]), i));
                    labels.add(date);
//                    date_hour.add(date);
                    i++;
                }
            }
            LineDataSet dataset = new LineDataSet(entries, "Blood Glucose");
            dataset.setColor(Color.parseColor("#FF0000"));
            dataset.setCircleColor(R.color.black);
            dataset.setCircleColorHole(R.color.black);

            LineData data = new LineData(labels, dataset);

            lineChart.setData(data);
            lineChart.animateY(5000);

//            glucose_list = result_string.split("<br>");
//
//            String test_sentence = glucose_list[0];
//            boolean test_contains = result.contains("comma");
//
//            if (result.contains("<comma>")) {
//                for (String glucose : glucose_list) {
//                    String glucose_split[] = glucose.split("<comma>");
////                    entries.add(glucose_split[1], glucose_split[0]);
//                }
//            }

        }

        @Override
        protected String doInBackground(String... params) {

            String user_id = (String) params[1];

            String serverURL = (String) params[0];
            String postParameters = "ID=" + user_id;

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("@@@", "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();
            } catch (Exception e) {
                Log.e("@@@", "exception", e);
                return new String("Same ID exists !");
            }
        }
    }

}
