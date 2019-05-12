package csecau.capstone.capstone02;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.IOException;
import java.io.InputStreamReader;

public class NewdiaryActivity extends AppCompatActivity {

    private Button doneButton;
    private EditText contentEdittext;
    private TextView resultText;

    private String ID = "2";
    private String Date = "2019-03-31 15:10:15";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newdiary);

        doneButton = (Button)findViewById(R.id.DoneButton);
        contentEdittext = (EditText)findViewById(R.id.contentinput);
                    resultText = (TextView)findViewById(R.id.resulttext);

                    doneButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String Contents = contentEdittext.getText().toString();
                            Contents.trim();
                            Translation translation = new Translation();
                            translation.execute(Contents);

                            try{
                                int count=0;

                                ArrayList<String> stopwords= new ArrayList<String>();
                                BufferedReader stop = new BufferedReader(new FileReader(getFilesDir() + "/stopwords.txt"));
                                String line = "";
                                while ((line = stop.readLine()) != null){
                                    stopwords.add(line);
                                }


                                Map<String, String> map = new HashMap<String, String>();
                                BufferedReader in = new BufferedReader(new FileReader(getFilesDir() + "/afinn.txt"));

                                line="";
                                while ((line = in.readLine()) != null) {
                                    String parts[] = line.split("\t");
                                    map.put(parts[0], parts[1]);
                                    count++;
                                }
                                in.close();
                                //   System.out.println(map.toString());

                                if((Contents != null)) {
                                    float score=0;
                                    String[] word= Contents.split(" ");

                                    for(int i=0; i<word.length;i++) {
                                        if(stopwords.contains(word[i].toLowerCase())) {

                                        }
                                        else{
                                            if(map.get(word[i])!=null) {
                                                String wordscore= map.get(word[i].toLowerCase());
                                                score=(float) score + Integer.parseInt(wordscore);
                                            }
                                        }
                                    }
//                                    Map<String, Float> sentiment= new HashMap<String, Float>();
//                                    sentiment.put("", score);

                                    resultText.setText("Analysis Result : " + score);

                                }

                            }
                            catch(FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

//                InsertDiary task = new InsertDiary();
//                task.execute("http://capstone02.cafe24.com/insert_diary.php", ID, Date , Contents);

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

    class Translation extends AsyncTask<String, Void, String> {
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
            result = result.substring(1,result.length()-1);

            contentEdittext.setText(result);

            try{
                int count=0;

                ArrayList<String> stopwords= new ArrayList<String>();
                BufferedReader stop = new BufferedReader(new FileReader(getFilesDir() + "/stopwords.txt"));
                String line = "";
                while ((line = stop.readLine()) != null){
                    stopwords.add(line);
                }


                Map<String, String> map = new HashMap<String, String>();
                BufferedReader in = new BufferedReader(new FileReader(getFilesDir() + "/afinn.txt"));

                line="";
                while ((line = in.readLine()) != null) {
                    String parts[] = line.split("\t");
                    map.put(parts[0], parts[1]);
                    count++;
                }
                in.close();
                //   System.out.println(map.toString());

                if((result != null)) {
                    float score=0;
                    String[] word= result.split(" ");

                    for(int i=0; i<word.length;i++) {
                        if(stopwords.contains(word[i].toLowerCase())) {

                        }
                        else{
                            if(map.get(word[i])!=null) {
                                String wordscore= map.get(word[i].toLowerCase());
                                score=(float) score + Integer.parseInt(wordscore);
                            }
                        }
                    }
//                                    Map<String, Float> sentiment= new HashMap<String, Float>();
//                                    sentiment.put("", score);

                    resultText.setText("Analysis Result : " + score);

                }

            }
            catch(FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... params) {

            String contents = (String)params[0];

            String clientId = "bq9EraHFS9LV92ANe_Nc";//애플리케이션 클라이언트 아이디값";
            String clientSecret = "AGfnDuG5H2";//애플리케이션 클라이언트 시크릿값";

            try {
                String text = URLEncoder.encode(contents, "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
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
                if(responseCode==200) { // 정상 호출
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
