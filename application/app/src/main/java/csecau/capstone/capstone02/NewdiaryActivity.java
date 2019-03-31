package csecau.capstone.capstone02;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class NewdiaryActivity extends AppCompatActivity {

    private Button doneButton;
    private EditText contentEdittext;

    private String ID = "2";
    private String Date = "2019-03-31 15:10:15";
    private String Contents = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newdiary);

        doneButton = (Button)findViewById(R.id.DoneButton);
        contentEdittext = (EditText)findViewById(R.id.contentinput);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Contents = contentEdittext.getText().toString();

                InsertDiary task = new InsertDiary();
                task.execute("http://capstone02.cafe24.com/insert_diary.php", ID, Date , Contents);

            }
        });
    }

    class InsertDiary extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Toast.makeText(NewdiaryActivity.this,result,Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {

            String ID = (String)params[1];
            String Date = (String)params[2];
            String Contents = (String)params[3];

            String serverURL = (String)params[0];
            String postParameters = "ID=" + ID + "&Date=" + Date + "&Contents=" + Contents;

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

                return sb.toString();
            } catch (Exception e) {
                Log.e("@@@", "exception", e);
                return new String("Same ID exists !");
            }
        }
    }
}
