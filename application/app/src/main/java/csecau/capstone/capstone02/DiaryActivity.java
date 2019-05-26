package csecau.capstone.capstone02;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static csecau.capstone.capstone02.MainActivity.user_id;

public class DiaryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String[] diary_list;


    private Button newdiaryButton;
    private EditText searchEdittext;
    private Spinner spinner;

    public ListView diary_listview;
    public diary_listviewAdapter adapter = new diary_listviewAdapter();

    public static DiaryActivity activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        activity = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairy);

        newdiaryButton = (Button) findViewById(R.id.newdiarybutton);
        searchEdittext = (EditText) findViewById(R.id.search_edittext);

        diary_listview = (ListView) findViewById(R.id.dairylistview);
        diary_listview.setAdapter(adapter);

        spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);

        final String[] item = new String[]{"내용", "날짜", "점수"};

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        searchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                diary_listviewAdapter newadapter = new diary_listviewAdapter();
                String input_type = spinner.getSelectedItem().toString();
                String input_text = searchEdittext.getText().toString();

                if (searchEdittext.getText().toString().contentEquals("")) {
                    for (String diary : diary_list) {
                        newadapter.addItem(diary.split("<comma>")[2], diary.split("<comma>")[0], diary.split("<comma>")[1]);
                    }
                }

                for (String diary : diary_list) {
                    if (input_type.contentEquals("내용")) {
                        if (diary.split("<comma>")[0].contains(searchEdittext.getText().toString())) {
                            newadapter.addItem(diary.split("<comma>")[2], diary.split("<comma>")[0], diary.split("<comma>")[1]);
                        }
                    } else if (input_type.contentEquals("날짜")) {
                        if (diary.split("<comma>")[1].contains(searchEdittext.getText().toString())) {
                            newadapter.addItem(diary.split("<comma>")[2], diary.split("<comma>")[0], diary.split("<comma>")[1]);
                        }
                    } else if (input_type.contentEquals("점수")) {
                        try {
                            if (searchEdittext.getText().toString().contentEquals("")) {

                            } else if (Integer.parseInt(searchEdittext.getText().toString()) >= 0) {
                                if (Integer.parseInt(diary.split("<comma>")[2]) >= 0) {
                                    newadapter.addItem(diary.split("<comma>")[2], diary.split("<comma>")[0], diary.split("<comma>")[1]);
                                }
                            } else if (Integer.parseInt(searchEdittext.getText().toString()) < 0) {
                                if (Integer.parseInt(diary.split("<comma>")[2]) < 0) {
                                    newadapter.addItem(diary.split("<comma>")[2], diary.split("<comma>")[0], diary.split("<comma>")[1]);
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                }
                diary_listview.setAdapter(newadapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        newdiaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewdiaryActivity.class);
                startActivity(intent);
            }
        });

        diary_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clicked_content = ((diary_listview) adapter.getItem(position)).getContent();
                String clicked_score = ((diary_listview) adapter.getItem(position)).getAnalysis_score();
                ;
                String clicked_time = ((diary_listview) adapter.getItem(position)).getTime();
                ;

                Intent intent = new Intent(getApplicationContext(), DiaryshowActivity.class);
                intent.putExtra("content", clicked_content);
                intent.putExtra("score", clicked_score);
                intent.putExtra("time", clicked_time);

                startActivity(intent);

            }
        });

        Getdairylist getdairylist = new Getdairylist();
        getdairylist.execute("http://capstone02.cafe24.com/retrieve_diary.php", user_id);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    class Getdairylist extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String result_string = result;
            diary_list = result_string.split("<br>");

//            ListView listview;
//            diary_listviewAdapter adapter;
//
//            adapter = new diary_listviewAdapter();

            diary_listview = (ListView) findViewById(R.id.dairylistview);
            diary_listview.setAdapter(adapter);

            if (result.contains("<comma>")) {
                for (String diary : diary_list) {
                    String diary_split[] = diary.split("<comma>");
                    adapter.addItem(diary_split[2], diary_split[0], diary_split[1]);
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
