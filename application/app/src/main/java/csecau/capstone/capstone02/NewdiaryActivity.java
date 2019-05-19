package csecau.capstone.capstone02;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static csecau.capstone.capstone02.MainActivity.user_id;

public class NewdiaryActivity extends AppCompatActivity {

    private float analysis_score = 0;
    private static String Contents = "";

    private Button doneButton;
    private Button recognizeButton;
    private EditText contentEdittext;
    private TextView resultText;

    private Intent i;
    private SpeechRecognizer mRecognizer;

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(), "Start", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {
            Toast.makeText(getApplicationContext(), String.valueOf(error), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            String recognition_result = "";
            recognition_result = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(recognition_result);
            String[] result_string = new String[mResult.size()];
            mResult.toArray(result_string);
            contentEdittext.setText("" + result_string[0]);
//            for (int i = 0; i < mResult.size(); i++) {
//                contentEdittext.append(" " + result_string[i]);
//            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newdiary);

        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(NewdiaryActivity.this);
        mRecognizer.setRecognitionListener(listener);

        doneButton = (Button) findViewById(R.id.DoneButton);
        recognizeButton = (Button) findViewById(R.id.RecognizeButton);
        contentEdittext = (EditText) findViewById(R.id.contentinput);
        resultText = (TextView) findViewById(R.id.resulttext);

        doneButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contents = contentEdittext.getText().toString();
                Contents.trim();

                if (Contents.length() == 0) {
                    Toast.makeText(getApplicationContext(), "내용을 입력하세요 !", Toast.LENGTH_SHORT).show();
                } else {
                    Translation_And_Sentiment_Analysis translation_and_sentiment_analysis = new Translation_And_Sentiment_Analysis();
                    translation_and_sentiment_analysis.execute(Contents);
                }
            }
        });

        recognizeButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecognizer.startListening(i);
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

            Toast.makeText(NewdiaryActivity.this, result, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {

            String ID = (String) params[1];
            String Contents = (String) params[2];
            String analysis_score = (String) params[3];

            String serverURL = (String) params[0];
            String postParameters = "ID=" + ID + "&Contents=" + Contents + "&analysis_score=" + analysis_score;

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

    class Translation_And_Sentiment_Analysis extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonElement target = parser.parse(result.toString()).getAsJsonObject().get("message").getAsJsonObject().get("result").getAsJsonObject().get("translatedText");

            result = target.toString();
            result = result.substring(1, result.length() - 1);

            contentEdittext.setText(result);

            try {
                int count = 0;

                ArrayList<String> stopwords = new ArrayList<String>();
                BufferedReader stop = new BufferedReader(new FileReader(getFilesDir() + "/stopwords.txt"));
                String line = "";
                while ((line = stop.readLine()) != null) {
                    stopwords.add(line);
                }

                Map<String, String> map = new HashMap<String, String>();
                BufferedReader in = new BufferedReader(new FileReader(getFilesDir() + "/afinn.txt"));

                line = "";
                while ((line = in.readLine()) != null) {
                    String parts[] = line.split("\t");
                    map.put(parts[0], parts[1]);
                    count++;
                }
                in.close();

                if ((contentEdittext.toString() != null)) {
                    float score = 0;
                    String[] word = contentEdittext.toString().split(" ");

                    for (int i = 0; i < word.length; i++) {
                        if (stopwords.contains(word[i].toLowerCase())) {

                        } else {
                            if (map.get(word[i]) != null) {
                                String wordscore = map.get(word[i].toLowerCase());
                                score = (float) score + Integer.parseInt(wordscore);
                            }
                        }
                    }

                    analysis_score = score;
                    resultText.setText("Analysis Result : " + score);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            InsertDiary insertdiary = new InsertDiary();
            insertdiary.execute("http://capstone02.cafe24.com/insert_diary.php", user_id, Contents, Float.toString(analysis_score));
        }

        @Override
        protected String doInBackground(String... params) {

            String contents = (String) params[0];

            String clientId = "bq9EraHFS9LV92ANe_Nc";//애플리케이션 클라이언트 아이디값";
            String clientSecret = "AGfnDuG5H2";//애플리케이션 클라이언트 시크릿값";

            try {
                String text = URLEncoder.encode(contents, "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                // post request
                String postParams = "source=ko&target=en&text=" + text;
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                return response.toString();
            } catch (Exception e) {
                return e.toString();
            }
        }
    }

}
