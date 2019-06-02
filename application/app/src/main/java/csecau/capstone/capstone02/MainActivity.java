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

//    private void setChart(List<Record> records){
//        LineChart lineChart = binding.lineChart;
//        lineChart.invalidate();
//        lineChart.clear();
//
//        ArrayList<Entry> values = new ArrayList<>();
//
//        for(Record record: records){
//            long dateTime = record.getDateTime();
//            float weight = (float) record.getValue();
//            values.add(new Entry(dateTime,value));
//        }
//    }


}
