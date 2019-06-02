package csecau.capstone.capstone02;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static csecau.capstone.capstone02.MainActivity.user_id;

public class ExerciseActivity extends AppCompatActivity {

    private String[] exercise_list;

    private Button newExerciseButton;

    public static ExerciseActivity activity = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        activity = this;

        newExerciseButton = (Button) findViewById(R.id.newexercisebutton);

        newExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewExerciseActivity.class);
                startActivity(intent);
            }
        });

        Getexerciselist getexerciselist = new Getexerciselist();
        getexerciselist.execute("http://capstone02.cafe24.com/retrieve_exercise.php", user_id);

    }


    class Getexerciselist extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String result_string = result;
            exercise_list = result_string.split("<br>");

            ListView listview;
            exercise_listviewAdapter adapter;

            adapter = new exercise_listviewAdapter();

            listview = (ListView) findViewById(R.id.exerciselistview);
            listview.setAdapter(adapter);

            String test_sentence = exercise_list[0];
            boolean test_contains = result.contains("comma");

            if (result.contains("<comma>")) {
                for (String exercise : exercise_list) {
                    String exercise_split[] = exercise.split("<comma>");
                    adapter.addItem(exercise_split[1], exercise_split[2], exercise_split[3]);
                }
            }
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
