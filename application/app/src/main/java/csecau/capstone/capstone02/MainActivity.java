package csecau.capstone.capstone02;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static MainActivity activity = null;

    private String[] glucose_list;

    private Button diaryButton, glucoseButton, medicationButton, logoutButton, exerciseButton, mealButton;
    public static String user_id = "";

    private String[] main_list;

    int n[] = new int[9];
    int sum[] = new int[9];
    int[] average = new int[9];

    String strDate0, strDate1, strDate2;

    private ListView main_listview;
    private alarm_listviewAdapter main_adapter = new alarm_listviewAdapter();
    private alarm_listviewAdapter temp_adapter = new alarm_listviewAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        glucoseButton = (Button) findViewById(R.id.GlucoseButton);
        medicationButton = (Button) findViewById(R.id.MedicationButton);
        exerciseButton = (Button) findViewById(R.id.ExerciseButton);
        mealButton = (Button) findViewById(R.id.MealButton);
        diaryButton = (Button) findViewById(R.id.DiaryButton);
        logoutButton = (Button) findViewById(R.id.logoutButton);

        main_listview = (ListView) findViewById(R.id.main_medication_alarm);

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

        RetrieveClosetAlarm retrieveClosetAlarm = new RetrieveClosetAlarm();
        retrieveClosetAlarm.execute("http://capstone02.cafe24.com/retrieve_alarm.php", user_id);


        Calendar cal = new GregorianCalendar(Locale.KOREA);
        cal.setTime(new Date());

        SimpleDateFormat fm1 = new SimpleDateFormat("MM-dd");
        String date = fm1.format(new Date());
        System.out.println("현재시간 월일 = " + date);

        strDate0 = fm1.format(cal.getTime());//현재날짜
        cal.add(Calendar.DAY_OF_YEAR, -1);
        strDate1 = fm1.format(cal.getTime());//하루전 날짜
        cal.add(Calendar.DAY_OF_YEAR, -1);
        strDate2 = fm1.format(cal.getTime());//이틀전 날짜


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

            LineChart lineChart = (LineChart) findViewById(R.id.chart);
            ArrayList<Entry> entries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<String>();
//            ArrayList date_hour= new ArrayList();

            String test_sentence = glucose_list[0];
            boolean test_contains = result.contains("comma");

            int i = 0;
            String date_hour[][] = new String[30][2];
            int glucose_value[] = new int[30];

            if (result.contains("<comma>")) {
                for (String glucose : glucose_list) {
                    String glucose_split[] = glucose.split("<comma>");
                    String date = glucose_split[1].substring(5, 13);//'MM-dd hh'
                    date_hour[i] = date.split(" ");
                    glucose_value[i] = Integer.parseInt(glucose_split[2]);
//                    entries.add(new Entry(Integer.parseInt(glucose_split[2]), i));
//                    labels.add(date);
                    Log.d("날짜", date_hour[i][0]);
                    Log.d("시간", date_hour[i][1]);

                    i++;
                }
                for (int j = 0; j < i; j++) {
                    if (date_hour[j][0].equals(strDate2)) {
                        if (Integer.parseInt(date_hour[j][1]) <= 8) {
                            //이틀전 아침
                            sum[0] += glucose_value[j];
                            n[0]++;
                        } else if (Integer.parseInt(date_hour[j][1]) <= 16) {
                            //이틀전 점심
                            sum[1] += glucose_value[j];
                            n[1]++;
                        } else {
                            //이틀전 저녁
                            sum[2] += glucose_value[j];
                            n[2]++;
                        }
                    } else if (date_hour[j][0].equals(strDate1)) {
                        if (Integer.parseInt(date_hour[j][1]) <= 8) {
                            //하루전 아침
                            sum[3] += glucose_value[j];
                            n[3]++;
                        } else if (Integer.parseInt(date_hour[j][1]) <= 16) {
                            //하루전 점심
                            sum[4] += glucose_value[j];
                            n[4]++;
                        } else {
                            //하루전 저녁
                            sum[5] += glucose_value[j];
                            n[5]++;
                        }
                    } else if (date_hour[j][0].equals(strDate0)) {
                        if (Integer.parseInt(date_hour[j][1]) <= 8) {
                            //오늘 아침
                            sum[6] += glucose_value[j];
                            n[6]++;
                        } else if (Integer.parseInt(date_hour[j][1]) <= 16) {
                            //오늘 점심
                            sum[7] += glucose_value[j];
                            n[7]++;
                        } else {
                            //오늘 저녁
                            sum[8] += glucose_value[j];
                            n[8]++;
                        }
                    }
                }

                for (int k = 0; k < 9; k++) {
                    if (n[k] != 0) {
                        average[k] = sum[k] / n[k];
                    } else average[k] = 0;
                    entries.add(new Entry(average[k], k));
                }
                labels.add(strDate2 + "아침");
                labels.add(strDate2 + "점심");
                labels.add(strDate2 + "저녁");
                labels.add(strDate1 + "아침");
                labels.add(strDate1 + "점심");
                labels.add(strDate1 + "저녁");
                labels.add(strDate0 + "아침");
                labels.add(strDate0 + "점심");
                labels.add(strDate0 + "저녁");
            }
            LineDataSet dataset = new LineDataSet(entries, "혈당");
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

    class RetrieveClosetAlarm extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String result_string = result;

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String total_string = date.toString().split(" ")[3];

            String getTime = sdf.format(date).split(" ")[1];

            if (Integer.parseInt(total_string.split(":")[0]) > 12) {
                getTime = Integer.toString(Integer.parseInt(getTime.split(":")[0]) + 12) + ":" + getTime.split(":")[1] + ":" + getTime.split(":")[2];
            }

            String showTime = "no";

            if (!result_string.contains("No Result")) {
                main_list = result_string.split("<comma>");

                for (int i = 1; i < main_list.length; i++) {
                    String hour_from_server = main_list[i].split(":")[0];
                    String minute_from_server = main_list[i].split(":")[1];
                    int hour_from_server_under12 = Integer.parseInt(hour_from_server);
                    String amVsPm_server = "";

//                    if (showTime.contains("no")) {
//                        if (Integer.parseInt(hour_from_server) - Integer.parseInt(getTime.split(":")[0]) == 0) {
//                            if (Integer.parseInt(minute_from_server) - Integer.parseInt(getTime.split(":")[1]) >= 0) {
//                                showTime = amVsPm_server + " " + hour_from_server_under12 + ":" + minute_from_server;
//                            }
//                        } else if (Integer.parseInt(hour_from_server) - Integer.parseInt(getTime.split(":")[0]) > 0) {
//                            showTime = amVsPm_server + " " + hour_from_server_under12 + ":" + minute_from_server;
//                        }
//                    }

                    if (hour_from_server_under12 == 0) {
                        hour_from_server_under12 += 12;
                        amVsPm_server = "오전";
                    } else if (0 < hour_from_server_under12 && hour_from_server_under12 < 12) {
                        amVsPm_server = "오전";
                    } else if (hour_from_server_under12 == 12) {
                        amVsPm_server = "오후";
                    } else if (12 < hour_from_server_under12 && hour_from_server_under12 < 24) {
                        hour_from_server_under12 -= 12;
                        amVsPm_server = "오후";
                    }

                    if (showTime.contains("no")) {
                        if (Integer.parseInt(hour_from_server) - Integer.parseInt(getTime.split(":")[0]) == 0) {
                            if (Integer.parseInt(minute_from_server) - Integer.parseInt(getTime.split(":")[1]) >= 0) {
                                main_adapter.addItem(amVsPm_server + " " + hour_from_server_under12 + ":" + minute_from_server);
                                showTime = "Yes";
                                break;
                            }
                        } else if (Integer.parseInt(hour_from_server) - Integer.parseInt(getTime.split(":")[0]) > 0) {
                            main_adapter.addItem(amVsPm_server + " " + hour_from_server_under12 + ":" + minute_from_server);
                            showTime = "Yes";
                            break;
                        }
                    }
                }

                if (showTime.contains("no")) {
                    String hour_from_server = main_list[1].split(":")[0];
                    String minute_from_server = main_list[1].split(":")[1];
                    int hour_from_server_under12 = Integer.parseInt(hour_from_server);
                    String amVsPm_server = "";

                    if (hour_from_server_under12 == 0) {
                        hour_from_server_under12 += 12;
                        amVsPm_server = "오전";
                    } else if (0 < hour_from_server_under12 && hour_from_server_under12 < 12) {
                        amVsPm_server = "오전";
                    } else if (hour_from_server_under12 == 12) {
                        amVsPm_server = "오후";
                    } else if (12 < hour_from_server_under12 && hour_from_server_under12 < 24) {
                        hour_from_server_under12 -= 12;
                        amVsPm_server = "오후";
                    }
                    main_adapter.addItem(amVsPm_server + " " + hour_from_server_under12 + ":" + minute_from_server);
                }

                main_listview.setAdapter(main_adapter);
            } else {
                main_adapter.addItem("알람이 없습니다");
                main_listview.setAdapter(main_adapter);
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String ID = (String) params[1];

            String serverURL = (String) params[0];
            String postParameters = "ID=" + ID;

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
