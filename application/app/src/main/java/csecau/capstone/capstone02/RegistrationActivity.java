package csecau.capstone.capstone02;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;

public class RegistrationActivity extends AppCompatActivity  {
    private static boolean IDcheck_done = false;

    private EditText IDEdittext;
    private EditText PWEdittext;
    private EditText confirmPWEdittext;
    private EditText NameEdittext;
    private EditText Email_First_Edittext;
    private EditText Email_Second_Edittext;
    private EditText phoneEdittext_first;
    private EditText phoneEdittext_second;
    private EditText phoneEdittext_third;

    private Button checkButton;
    private Button doneButton;


    private DatePicker mDate;
    private TextView dateText;
    String strDate;

    private TextView textResult;

    private Spinner spinner_email;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        IDEdittext = (EditText) findViewById(R.id.IDregister);
        PWEdittext = (EditText) findViewById(R.id.PWregister);
        confirmPWEdittext = (EditText)findViewById(R.id.ConfirmPW);
        NameEdittext = (EditText)findViewById(R.id.NameRegister);
        Email_First_Edittext = (EditText)findViewById(R.id.Email_First_Register);
//        Email_Second_Edittext = (EditText)findViewById(R.id.Email_Second_Register);
        mDate = (DatePicker)findViewById(R.id.datePicker);
        phoneEdittext_first = (EditText) findViewById(R.id.first_num);
        phoneEdittext_second = (EditText) findViewById(R.id.second_num);
        phoneEdittext_third = (EditText) findViewById(R.id.third_num);

        spinner_email = (Spinner)findViewById(R.id.Email_Second_Register);

        checkButton = (Button) findViewById(R.id.IDcheck);
        doneButton = (Button) findViewById(R.id.DoneRegister);

        textResult = (TextView) findViewById(R.id.TextResultRegister);
        dateText = (TextView) findViewById(R.id.dateText);


        mDate.init(mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int month,
                                              int day) {

                        strDate = Integer.toString(year) + "/" +Integer.toString(month+1)
                                +"/" + Integer.toString(day);
                        Log.d("date",strDate);
                    }
                });


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
                String Email = Email_First_Edittext.getText().toString() + spinner_email.getSelectedItem().toString();
                String DOB = strDate;
                String phone = phoneEdittext_first.getText().toString()+"-"+phoneEdittext_second.getText().toString()+"-"+phoneEdittext_third.getText().toString();

                boolean checkConfirmPW;
                checkConfirmPW = PW.equals(confirmPW);

                if(ID.contentEquals("") || PW.contentEquals("") || confirmPW.contentEquals("") ||Name.contentEquals("") || Email.contentEquals("") || phone.contentEquals("")) {
                    Toast.makeText(RegistrationActivity.this, "항목을 모두 채워주세요", Toast.LENGTH_SHORT).show();
                } else if (IDcheck_done == false) {
                    Toast.makeText(RegistrationActivity.this, "ID 중복확인을 해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkConfirmPW) {
                        InsertData task = new InsertData();
                        task.execute("http://capstone02.cafe24.com/registration.php", ID, PW, Name, Email, DOB, phone);

                        IDEdittext.setText("");
                        PWEdittext.setText("");
                        confirmPWEdittext.setText("");
                        NameEdittext.setText("");
                        Email_First_Edittext.setText("");
//                        Email_Second_Edittext.setText("");
//                        phoneEdittext_first.setText("");
//                        phoneEdittext_second.setText("");
//                        phoneEdittext_third.setText("");
                        finish();
                    } else {
                        Toast.makeText(RegistrationActivity.this, "비밀번호를 다시 확인하세요", Toast.LENGTH_SHORT).show();
                        confirmPWEdittext.setText("");
                    }
                }
            }
        });
    }

//    @Override
//    public void onDateChanged(DatePicker view, int year, int monthOfYear,
//                              int dayOfMonth) {
//        dateText.setText(String.valueOf(year) + String.valueOf(monthOfYear+1) + String.valueOf(dayOfMonth));
//        Log.d("date", String.valueOf(year) + String.valueOf(monthOfYear+1) + String.valueOf(dayOfMonth));
//    }

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
                Toast.makeText(RegistrationActivity.this, "사용 가능한 ID 입니다", Toast.LENGTH_SHORT).show();
                IDcheck_done = true;
                IDEdittext.setFocusable(false);
                IDEdittext.setClickable(false);
            } else {
                Toast.makeText(RegistrationActivity.this, "중복된 ID가 있습니다", Toast.LENGTH_SHORT).show();
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
            Log.d("###",result);

            if(result.contains("successful")) {
                Toast.makeText(RegistrationActivity.this, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegistrationActivity.this, "에러가 발생했습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
            }
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
