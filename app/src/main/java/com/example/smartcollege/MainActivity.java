package com.example.smartcollege;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smartcollege.JSONObjects.AuthParams;
import com.example.smartcollege.JSONObjects.BodyRequest;
import com.example.smartcollege.REST.RestRequests;

import org.json.JSONObject;

public class MainActivity extends Activity {
    public final String API_URL = "https://sb.ch.amdocs.com/mobile-gateway/jsonrpc/AuthenticationService";
    private String TOKEN = "";

    private Button mLoginButton;
    private EditText usernameEditText;
    private EditText passwordEditText;

    private String mPassword;
    private String mUsername;

    private String mJSONstring = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginButton = findViewById(R.id.button_login);
        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPassword = passwordEditText.getText().toString();
                mUsername = usernameEditText.getText().toString();

                new RequestAsync().execute();

                // Make delay before entering next screen (the POST request is in different thread)
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(mJSONstring);

                            if (json.getJSONObject("result").getString("success").equals("true")) {

                                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);

                                TOKEN = json.getJSONObject("result").getJSONObject("authenticationDetails").getString("securityToken");
                                intent.putExtra("TOKEN", TOKEN);

                                startActivity(intent);
                                finish();
                            }
                        } catch(Throwable t) {

                        }
                    }
                }, 2000);
            }
        });
    }

    public class RequestAsync extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {

                // POST Request
                BodyRequest jsonBody = new BodyRequest();
                AuthParams params = new AuthParams(mUsername, mPassword);
                jsonBody.addParameter("jsonrpc","2.0");
                jsonBody.addParameter("method","authentify");
                jsonBody.setParams(params);

                JSONObject obj = jsonBody.getJsonObject();

                return new RestRequests().postRequest(API_URL, obj);
            } catch (Exception e) {
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                mJSONstring = s;
            }
        }
    }
}
