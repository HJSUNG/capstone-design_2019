package csecau.capstone.capstone02;

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

public class NewExerciseActivity extends AppCompatActivity {

    private String TAG = "Exercisefunction";

    private Button ExerciseSaveBtn;

    private EditText ExerciseEdittext;
    private EditText TimeEdittext;

    public static NewExerciseActivity activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newexercise);

        activity = this;

        ExerciseSaveBtn = (Button)findViewById(R.id.ExerciseSaveBtn);

        ExerciseEdittext = (EditText)findViewById(R.id.exercise);
        TimeEdittext = (EditText)findViewById(R.id.exe_time);

        ExerciseSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String exercise = ExerciseEdittext.getText().toString();
                String exe_time = TimeEdittext.getText().toString();
                String UserID = user_id;

                if(exercise.equals("") || exe_time.equals("")){
                    Toast.makeText(NewExerciseActivity.this,"모든 값을 넣어주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Exercsie task = new Exercsie();
                    task.execute("http://capstone02.cafe24.com/insert_exercise.php", UserID, exercise, exe_time);
                }
            }
        });
    }

    class Exercsie extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(NewExerciseActivity.this,"Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            String input_string = result;
            boolean Exercise = input_string.contains("Error");
            Log.d(TAG, input_string);

            if(Exercise) {
                Toast.makeText(NewExerciseActivity.this, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
            } else {
                if (ExerciseActivity.activity != null) {
                    ExerciseActivity activity = (ExerciseActivity) ExerciseActivity.activity;
                    activity.finish();
                }

                NewExerciseActivity.activity.finish();

                Intent intent = new Intent(getApplicationContext(), ExerciseActivity.class);
                startActivity(intent);

                Toast.makeText(NewExerciseActivity.this, "처리 완료", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = (String)params[0];
            String userID = (String)params[1];
            String exercise = (String)params[2];
            String exe_time = (String)params[3];
            
            String postParameters = "ID=" + userID + "&ActivityName=" + exercise + "&Value=" + exe_time;

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
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
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
