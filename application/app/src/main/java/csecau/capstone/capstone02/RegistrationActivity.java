package csecau.capstone.capstone02;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;

public class RegistrationActivity extends AppCompatActivity {
    private static boolean IDcheck_done = false;

    private EditText IDEdittext;
    private EditText PWEdittext;
    private EditText confirmPWEdittext;
    private EditText NameEdittext;
    private EditText EmailEdittext;
    private EditText DOBEdittext;
    private EditText phoneEdittext_first;
    private EditText phoneEdittext_second;
    private EditText phoneEdittext_third;

    private Button checkButton;
    private Button doneButton;

    private TextView textResult;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        IDEdittext = (EditText) findViewById(R.id.IDregister);
        PWEdittext = (EditText) findViewById(R.id.PWregister);
        confirmPWEdittext = (EditText)findViewById(R.id.ConfirmPW);
        NameEdittext = (EditText)findViewById(R.id.NameRegister);
        EmailEdittext = (EditText)findViewById(R.id.EmailRegister);
        DOBEdittext = (EditText)findViewById(R.id.DOBRegister);
        phoneEdittext_first = (EditText) findViewById(R.id.first_num);
        phoneEdittext_second = (EditText) findViewById(R.id.second_num);
        phoneEdittext_third = (EditText) findViewById(R.id.third_num);
        checkButton = (Button) findViewById(R.id.IDcheck);
        doneButton = (Button) findViewById(R.id.DoneRegister);

        textResult = (TextView) findViewById(R.id.TextResultRegister);


        checkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String IDcompare = IDEdittext.getText().toString();

                if (IDcompare.contentEquals("")) {
                    Toast.makeText(RegistrationActivity.this, "Insert ID", Toast.LENGTH_SHORT).show();
                } else {
                    CheckID task = new CheckID();
                    task.execute("http://capstone02.cafe24.com/IDcheck.php", IDcompare);

                }
            }
        });

        //다 하고 완료 누르면 DB 에 넣는 부분
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID = IDEdittext.getText().toString();
                String PW = PWEdittext.getText().toString();
                String confirmPW = confirmPWEdittext.getText().toString();
                String Name = NameEdittext.getText().toString();
                String Email = EmailEdittext.getText().toString();
                String DOB = DOBEdittext.getText().toString();
                String phone = phoneEdittext_first.getText().toString()+"-"+phoneEdittext_second.getText().toString()+"-"+phoneEdittext_third.getText().toString();

                boolean checkConfirmPW;
                checkConfirmPW = PW.equals(confirmPW);

                if(ID.contentEquals("") || PW.contentEquals("") || confirmPW.contentEquals("") ||Name.contentEquals("") || Email.contentEquals("") || DOB.contentEquals("") ||phone.contentEquals("")) {
                    Toast.makeText(RegistrationActivity.this, "Fill out the form", Toast.LENGTH_SHORT).show();
                } else if (IDcheck_done == false) {
                    Toast.makeText(RegistrationActivity.this, "Check your ID first", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkConfirmPW) {
                        InsertData task = new InsertData();
                        task.execute("http://capstone02.cafe24.com/registration.php", ID, PW, Name, Email, DOB, phone);

                        IDEdittext.setText("");
                        PWEdittext.setText("");
                        confirmPWEdittext.setText("");
                        NameEdittext.setText("");
                        EmailEdittext.setText("");
                        DOBEdittext.setText("");
                        phoneEdittext_first.setText("");
                        phoneEdittext_second.setText("");
                        phoneEdittext_third.setText("");
                        finish();
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Check PW again", Toast.LENGTH_SHORT).show();
                        confirmPWEdittext.setText("");
                    }
                }
            }
        });
    }


    class CheckID extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(RegistrationActivity.this,"Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String input_string = result;

            boolean sameID = input_string.contains("exist");
            Log.d("AAAA",input_string);

            progressDialog.dismiss();

            if (!sameID) {
                Toast.makeText(RegistrationActivity.this, "You can use this ID", Toast.LENGTH_SHORT).show();
                IDcheck_done = true;
                IDEdittext.setFocusable(false);
                IDEdittext.setClickable(false);
            } else {
                Toast.makeText(RegistrationActivity.this, "Same ID exists", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String ID = (String)params[1];

            String serverURL = (String)params[0];
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

    class InsertData extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("a",result);

            Toast.makeText(RegistrationActivity.this,result,Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {

            String ID = (String)params[1];
            String PW = (String)params[2];
            String Name = (String)params[3];
            String Email = (String)params[4];
            String DOB = (String)params[5];
            String phone = (String)params[6];

            String serverURL = (String)params[0];
            String postParameters = "ID=" + ID + "&Password=" + PW + "&Name=" + Name + "&Email=" + Email + "&DOB=" + DOB + "&PhoneNumber=" + phone;

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
                return new String("Same ID exists !");
            }
        }
    }
}
