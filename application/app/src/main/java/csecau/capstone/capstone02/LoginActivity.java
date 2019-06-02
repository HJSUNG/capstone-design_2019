package csecau.capstone.capstone02;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static csecau.capstone.capstone02.MainActivity.user_id;

public class LoginActivity extends AppCompatActivity {
    private static boolean login_check = false;

    private boolean saveLoginData;
    private Button loginButton;
    private Button registrationButton;

    private EditText IDEdittext;
    private EditText PWEdittext;

    private String loginId, loginPw;
    private CheckBox checkbox;

    private SharedPreferences appData;

    private TextView textResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



//        final EditText idinput = (EditText)findViewById(R.id.idinput);
        appData = getSharedPreferences("appData",MODE_PRIVATE);
        load();

        loginButton = (Button) findViewById(R.id.loginButton);
        registrationButton = (Button) findViewById(R.id.signupButton);

        IDEdittext = (EditText) findViewById(R.id.idinput);
        PWEdittext = (EditText) findViewById(R.id.passwordinput);
        checkbox = (CheckBox) findViewById(R.id.checkbox);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        loginId = auto.getString("inputId",null);
        loginPw = auto.getString("inputPw",null);

        checkbox.setChecked(true);


        if(loginId != null && loginPw != null) {
            String ID = loginId;
            String PW = loginPw;
            IDEdittext.setText(ID);
            PWEdittext.setText(PW);

            Login task = new Login();
            task.execute("http://capstone02.cafe24.com/login.php", ID, PW);
        }

        textResult = (TextView) findViewById(R.id.TextResultLogin);

        registrationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ID = IDEdittext.getText().toString();
                String PW = PWEdittext.getText().toString();

                Login task = new Login();
                task.execute("http://capstone02.cafe24.com/login.php", ID, PW);
            }
        });
    }

    private void save() {
        SharedPreferences.Editor editor = appData.edit();

        editor.putBoolean("SAVE_LOGIN_DATA",checkbox.isChecked());
        editor.putString("ID", IDEdittext.getText().toString().trim());
        editor.putString("PW",PWEdittext.getText().toString().trim());

        editor.apply();
    }

    private void load() {
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        loginId = appData.getString("ID","");
        loginPw = appData.getString("PW","");
    }

    class Login extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = progressDialog.show(LoginActivity.this, "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            String input_string = result;
            boolean SameLogin = input_string.contains("Fail");
            Log.d("11",input_string);

            user_id = input_string.split("<br>")[1];


            if(SameLogin) {
                Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "로그인 성공 ! ", Toast.LENGTH_SHORT).show();
                login_check = true;

                if(login_check) {
                    //save();

                    SharedPreferences auto = getSharedPreferences("auto",Activity.MODE_PRIVATE);

                    SharedPreferences.Editor autoLogin = auto.edit();
                    autoLogin.putString("inputId", IDEdittext.getText().toString());
                    autoLogin.putString("inputPw", PWEdittext.getText().toString());

                    autoLogin.commit();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                    finish();
                }
            }
        }

        @Override
        protected String doInBackground(String...params) {
            String ID = (String)params[1];
            String PW = (String)params[2];

            String serverURL = (String)params[0];
            String postParameters = "ID=" + ID + "&Password=" + PW;

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
                else {
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
                Log.d("@@@", "Login Error ", e);
                return new String("ERROR: " + e.getMessage());
            }
        }
    }

}