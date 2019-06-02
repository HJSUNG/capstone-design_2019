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

public class NewGlucoseActivity extends AppCompatActivity {

    public static NewGlucoseActivity activity = null;

    private String TAG = "Glucosefunction";

    private Button GlucoseSaveBtn;

    private EditText GlucoseEdittext;
    private EditText ComentEdittext;

    /////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newglucose);

        activity = this;

        GlucoseSaveBtn = (Button)findViewById(R.id.GlucoseSaveBtn);

        GlucoseEdittext = (EditText)findViewById(R.id.GlucoseValue);
        ComentEdittext = (EditText)findViewById(R.id.coment_explain);

        GlucoseSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String glucose = GlucoseEdittext.getText().toString();
                String comment = ComentEdittext.getText().toString();
                String UserID =  user_id;

                if(glucose.equals("") || comment.equals("")){
                    Toast.makeText(NewGlucoseActivity.this,"모든 값을 넣어주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Glucose task = new Glucose();
                    task.execute("http://capstone02.cafe24.com/insert_glucose.php", UserID, glucose, comment);
                }
            }
        });

    }

    class Glucose extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(NewGlucoseActivity.this,"Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            String input_string = result;
            boolean Glucose = input_string.contains("Error");
            Log.d(TAG, input_string);

            if(Glucose) {
                Toast.makeText(NewGlucoseActivity.this, "다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            } else {
                if (GlucoseActivity.activity != null) {
                    GlucoseActivity activity = (GlucoseActivity) GlucoseActivity.activity;
                    activity.finish();
                }

                NewGlucoseActivity.activity.finish();

                Intent intent = new Intent(getApplicationContext(), GlucoseActivity.class);
                startActivity(intent);

                Toast.makeText(NewGlucoseActivity.this, "처리 완료", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String userID = (String)params[1];
            String glucose = (String)params[2];
            String comment = (String)params[3];

            String serverURL = (String)params[0];
            String postParameters = "ID=" + userID + "&Value=" + glucose + "&Comment=" + comment;

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
