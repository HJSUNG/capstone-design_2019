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

    public static NewdiaryActivity activity = null;

    private float analysis_score = 0;
    private static String Contents = "";

    private Button doneButton;
    private Button recognizeButton;
    private EditText contentEdittext;
//    private TextView resultText;

    private Intent i;
    private SpeechRecognizer mRecognizer;

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(), "음성인식 시작", Toast.LENGTH_SHORT).show();
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

            String error_string = "";

            switch (error) {
                case 1:
                case 2:
                    error_string = "연결상태를 확인하세요";
                    break;

                case 3:
                case 4:
                case 5:
                case 7:
                case 8:
                    error_string = "다시 한번 말씀해 주세요";
                    break;

                case 6:
                    error_string = "조금 더 크게 말씀해 주세요";
                    break;

                case 9:
                    error_string = "마이크 사용권한을 설정하세요";
                    break;
            }

            Toast.makeText(getApplicationContext(), error_string, Toast.LENGTH_SHORT).show();

//                1 ERROR_NETWORK_TIMEOUT : 네트워크 타임아웃
//
//                2 ERROR_NETWORK :  그 외 네트워크 에러
//
//                3 ERROR_AUDIO :  녹음 에러
//
//                4 ERROR_SERVER :  서버에서 에러를 보냄
//
//                5 ERROR_CLIENT :  클라이언트 에러
//
//                6 ERROR_SPEECH_TIMEOUT :  아무 음성도 듣지 못했을 때
//
//                7 ERROR_NO_MATCH :  적당한 결과를 찾지 못했을 때
//
//                8 ERROR_RECOGNIZER_BUSY :  RecognitionService가 바쁠 때
//
//                9 ERROR_INSUFFICIENT_PERMISSIONS: uses-permission(즉 RECORD_AUDIO) 이 없을 때
        }

        @Override
        public void onResults(Bundle results) {
            String existing_sentence = contentEdittext.getText().toString();
            String recognition_result = "";
            recognition_result = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(recognition_result);
            String[] result_string = new String[mResult.size()];
            mResult.toArray(result_string);

            if (existing_sentence.contentEquals("")) {
                contentEdittext.setText(result_string[0]);
            } else {
                contentEdittext.setText(existing_sentence + " " + result_string[0]);
            }
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

        activity = this;

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
//        resultText = (TextView) findViewById(R.id.resulttext);

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

            boolean diary_error = result.contains("Error");
            if (diary_error) {
                Toast.makeText(NewdiaryActivity.this, result, Toast.LENGTH_SHORT).show();
            } else {
                if (DiaryActivity.activity != null) {
                    DiaryActivity activity = (DiaryActivity) DiaryActivity.activity;
                    activity.finish();
                }

                NewdiaryActivity.activity.finish();

                Intent intent = new Intent(getApplicationContext(), DiaryActivity.class);
                startActivity(intent);

                Toast.makeText(NewdiaryActivity.this, "처리 완료", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String ID = (String) params[1];
            String Contents = (String) params[2];
            String analysis_score = (String) params[3];
            String english_contents = (String) params[4];

            String serverURL = (String) params[0];
            String postParameters = "ID=" + ID + "&Contents=" + Contents + "&Value=" + analysis_score + "&english_contents=" + english_contents;

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

            if (!result.contains("errorMessage")) {
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonElement target = parser.parse(result.toString()).getAsJsonObject().get("message").getAsJsonObject().get("result").getAsJsonObject().get("translatedText");

                result = target.toString();
                result = result.substring(1, result.length() - 1);

                contentEdittext.setText(result);

                InsertDiary insertdiary = new InsertDiary();
                insertdiary.execute("http://capstone02.cafe24.com/insert_diary.php", user_id, Contents, Float.toString(analysis_score), result);
            } else {
                Toast.makeText(NewdiaryActivity.this, "내용을 입력하세요 ", Toast.LENGTH_SHORT).show();
            }
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
