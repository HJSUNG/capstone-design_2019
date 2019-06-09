package csecau.capstone.capstone02;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import static csecau.capstone.capstone02.MainActivity.user_id;

public class MedicationActivity extends AppCompatActivity {

    public static MedicationActivity activity = null;

    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    private int selected_hour, selected_minute;
    private String amVsPm = "";
    Context context;
    PendingIntent pendingIntent;

    private String[] alarm_list;

    private ListView medication_listview;
    private alarm_listviewAdapter medication_adapter = new alarm_listviewAdapter();
    private alarm_listviewAdapter temp_adapter = new alarm_listviewAdapter();

    private Button Addbtn, Deletebtn;

    private int clicked_item = -500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);

        activity = this;

        Addbtn = (Button) findViewById(R.id.btn_add);
        Deletebtn = (Button) findViewById(R.id.btn_delete);

        alarm_timepicker = (TimePicker) findViewById(R.id.time_picker);

        medication_listview = (ListView) findViewById(R.id.alarmlistview);
        medication_listview.setAdapter(medication_adapter);

        Addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_hour = alarm_timepicker.getHour();
                selected_minute = alarm_timepicker.getMinute();
                String selected_hour_original = Integer.toString(alarm_timepicker.getHour());
                String selected_minute_original = Integer.toString(alarm_timepicker.getMinute());
                String parsed_time = selected_hour_original + ":" + selected_minute_original;

                if (selected_hour == 0) {
                    selected_hour += 12;
                    amVsPm = "오전";
                } else if (0 < selected_hour && selected_hour < 12) {
                    amVsPm = "오전";
                } else if (selected_hour == 12) {
                    amVsPm = "오후";
                } else if (12 < selected_hour && selected_hour < 24) {
                    selected_hour -= 12;
                    amVsPm = "오후";
                }
                Addbtn.setClickable(false);
                InsertAlarm insertAlarm = new InsertAlarm();
                insertAlarm.execute("http://capstone02.cafe24.com/insert_alarm.php", user_id, parsed_time);
            }
        });

        Deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (medication_adapter.getCount() == 0) {
                    Toast.makeText(getApplicationContext(), "알람이 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    if (clicked_item == -500) {
                        Toast.makeText(getApplicationContext(), "삭제할 알람을 선택하세요", Toast.LENGTH_SHORT).show();
                    } else {
                        if (medication_adapter.getCount() == 1) {
                            String delete_time = ((alarm_listview) medication_adapter.getItem(0)).getTime();
                            String result_time_result = "";
                            if (delete_time.split(" ")[0].contentEquals("오전")) {
                                if (0 < Integer.parseInt(delete_time.split(" ")[1].split(":")[0]) && Integer.parseInt(delete_time.split(" ")[1].split(":")[0]) < 10) {
                                    result_time_result = "0" + delete_time.split(" ")[1].split(":")[0] + ":" + delete_time.split(" ")[1].split(":")[1] + ":00";
                                } else if (Integer.parseInt(delete_time.split(" ")[1].split(":")[0]) == 10 && Integer.parseInt(delete_time.split(" ")[1].split(":")[0]) == 11) {
                                    result_time_result = delete_time.split(" ")[1].split(":")[0] + ":" + delete_time.split(" ")[1].split(":")[1] + ":00";
                                } else {
                                    result_time_result = Integer.toString(Integer.parseInt(delete_time.split(" ")[1].split(":")[0]) - 12) + delete_time.split(" ")[1].split(":")[1] + ":00";
                                }
                            } else {
                                if (Integer.parseInt(delete_time.split(" ")[1].split(":")[0]) == 12) {
                                    result_time_result = delete_time.split(" ")[1].split(":")[0] + ":" + delete_time.split(" ")[1].split(":")[1] + ":00";
                                } else {
                                    result_time_result = Integer.toString(Integer.parseInt(delete_time.split(" ")[1].split(":")[0]) + 12) + ":" + delete_time.split(" ")[1].split(":")[1] + ":00";
                                }
                            }
                            DeleteAlarm deleteAlarm = new DeleteAlarm();
                            deleteAlarm.execute("http://capstone02.cafe24.com/delete_alarm.php", user_id, result_time_result);
                            medication_adapter.removeItem(0);
                        } else {
                            String delete_time = ((alarm_listview) medication_adapter.getItem(clicked_item)).getTime();
                            String result_time_result = "";
                            if (delete_time.split(" ")[0].contentEquals("오전")) {
                                if (0 < Integer.parseInt(delete_time.split(" ")[1].split(":")[0]) && Integer.parseInt(delete_time.split(" ")[1].split(":")[0]) < 10) {
                                    result_time_result = "0" + delete_time.split(" ")[1].split(":")[0] + ":" + delete_time.split(" ")[1].split(":")[1] + ":00";
                                } else if (Integer.parseInt(delete_time.split(" ")[1].split(":")[0]) == 10 && Integer.parseInt(delete_time.split(" ")[1].split(":")[0]) == 11) {
                                    result_time_result = delete_time.split(" ")[1].split(":")[0] + ":" + delete_time.split(" ")[1].split(":")[1] + ":00";
                                } else {
                                    result_time_result = Integer.toString(Integer.parseInt(delete_time.split(" ")[1].split(":")[0]) - 12) + delete_time.split(" ")[1].split(":")[1] + ":00";
                                }
                            } else {
                                if (Integer.parseInt(delete_time.split(" ")[1].split(":")[0]) == 12) {
                                    result_time_result = delete_time.split(" ")[1].split(":")[0] + ":" + delete_time.split(" ")[1].split(":")[1] + ":00";
                                } else {
                                    result_time_result = Integer.toString(Integer.parseInt(delete_time.split(" ")[1].split(":")[0]) + 12) + ":" + delete_time.split(" ")[1].split(":")[1] + ":00";
                                }
                            }
                            DeleteAlarm deleteAlarm = new DeleteAlarm();
                            deleteAlarm.execute("http://capstone02.cafe24.com/delete_alarm.php", user_id, result_time_result);
                            medication_adapter.removeItem(clicked_item);
                        }
                        if (medication_adapter.getCount() == 0) {
                            medication_listview.setAdapter(temp_adapter);
                        } else {
                            medication_listview.setAdapter(medication_adapter);
                        }
                    }
                    clicked_item = -500;
                }
            }
        });

        medication_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clicked_item = position;
            }
        });

        RetrieveAlarm retrieveAlarm = new RetrieveAlarm();
        retrieveAlarm.execute("http://capstone02.cafe24.com/retrieve_alarm.php", user_id);
    }

    @Override
    public void onBackPressed() {
        if (MainActivity.activity != null) {
            MainActivity activity = (MainActivity) MainActivity.activity;
            activity.finish();
        }

        MedicationActivity.activity.finish();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    //insert_alarm
    class InsertAlarm extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.contains("success")) {
                if (0 <= selected_minute && selected_minute < 10) {
                    medication_adapter.addItem(amVsPm + " " + selected_hour + ":" + "0" + selected_minute);
                } else {
                    medication_adapter.addItem(amVsPm + " " + selected_hour + ":" + selected_minute);
                }
                medication_listview.setAdapter(medication_adapter);
                Addbtn.setClickable(true);
            } else {
                Toast.makeText(MedicationActivity.this, "중복값이 있습니다", Toast.LENGTH_SHORT).show();
                Addbtn.setClickable(true);
            }
//            Toast.makeText(MedicationActivity.this, result, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {

            String ID = (String) params[1];
            String Time = (String) params[2];

            String serverURL = (String) params[0];
            String postParameters = "ID=" + ID + "&Time=" + Time;

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

    class DeleteAlarm extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

//            Toast.makeText(MedicationActivity.this, result, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {

            String ID = (String) params[1];
            String Time = (String) params[2];

            String serverURL = (String) params[0];
            String postParameters = "ID=" + ID + "&Time=" + Time;

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

    class RetrieveAlarm extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String result_string = result;

            if (!result_string.contains("No Result")) {
                alarm_list = result_string.split("<comma>");

                for (int i = 1; i < alarm_list.length; i++) {
                    String hour_from_server = alarm_list[i].split(":")[0];
                    String minute_from_server = alarm_list[i].split(":")[1];
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
                    medication_adapter.addItem(amVsPm_server + " " + hour_from_server_under12 + ":" + minute_from_server);
                }

                medication_listview.setAdapter(medication_adapter);
            } else {
                medication_listview.setAdapter(medication_adapter);
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
