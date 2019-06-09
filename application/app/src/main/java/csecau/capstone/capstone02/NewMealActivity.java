package csecau.capstone.capstone02;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static csecau.capstone.capstone02.MainActivity.user_id;

public class NewMealActivity extends AppCompatActivity {

    public static NewMealActivity activity = null;

    private String TAG = "Mealfunction";

    private Button MealSaveBtn;

    private EditText meal1, meal2, meal3, meal4, meal5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmeal);

        activity = this;

        MealSaveBtn = (Button) findViewById(R.id.MealSaveBtn);

        meal1 = (EditText) findViewById(R.id.mealtext1);
        meal2 = (EditText) findViewById(R.id.mealtext2);
        meal3 = (EditText) findViewById(R.id.mealtext3);
        meal4 = (EditText) findViewById(R.id.mealtext4);
        meal5 = (EditText) findViewById(R.id.mealtext5);

        MealSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Meal1 = meal1.getText().toString();
                String Meal2 = meal2.getText().toString();
                String Meal3 = meal3.getText().toString();
                String Meal4 = meal4.getText().toString();
                String Meal5 = meal5.getText().toString();
                String UserID = user_id;

                if (Meal1.contentEquals("")) {
                    Toast.makeText(NewMealActivity.this, "첫번째음식을 채워주세요", Toast.LENGTH_SHORT).show();
                } else if (Meal2.contentEquals("") && (!Meal3.contentEquals("") || !Meal4.contentEquals("") || !Meal5.contentEquals(""))) {
                    Toast.makeText(NewMealActivity.this, "두번째음식을 채워주세요.", Toast.LENGTH_SHORT).show();
                } else if(Meal3.contentEquals("") && (!Meal4.contentEquals("") || !Meal5.contentEquals(""))) {
                    Toast.makeText(NewMealActivity.this, "세번째음식을 채워주세요.", Toast.LENGTH_SHORT).show();
                } else if(Meal4.contentEquals("") && !Meal5.contentEquals("")) {
                    Toast.makeText(NewMealActivity.this, "네번째음식을 채워주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Meal task = new Meal();
                    task.execute("http://capstone02.cafe24.com/insert_meal.php", UserID, Meal1, Meal2, Meal3, Meal4, Meal5);
                }
            }
        });

    }

    class Meal extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(NewMealActivity.this, "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            String input_string = result;
            boolean Meal = input_string.contains("Error");
            Log.d(TAG, input_string);

            if (Meal) {
                Toast.makeText(NewMealActivity.this, "다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            } else {
                if (MealActivity.activity != null) {
                    MealActivity activity = (MealActivity) MealActivity.activity;
                    activity.finish();
                }

                NewMealActivity.activity.finish();

                Intent intent = new Intent(getApplicationContext(), MealActivity.class);
                startActivity(intent);

                Toast.makeText(NewMealActivity.this, "처리 완료", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = (String) params[0];

            String userID = (String) params[1];
            String meal1 = (String) params[2];
            String meal2 = (String) params[3];
            String meal3 = (String) params[4];
            String meal4 = (String) params[5];
            String meal5 = (String) params[6];

            String postParameters = "ID=" + userID + "&Contents1=" + meal1 + "&Contents2=" + meal2 + "&Contents3=" + meal3 +
                    "&Contents4=" + meal4 + "&Contents5=" + meal5;

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

                return sb.toString().trim();
            } catch (Exception e) {
                return new String("Error: " + e.getMessage());
            }
        }
    }
}
