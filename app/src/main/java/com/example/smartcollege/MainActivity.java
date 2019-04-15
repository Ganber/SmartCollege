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

import com.example.smartcollege.Class.AuthParams;
import com.example.smartcollege.Class.BodyRequest;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

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

    public String httpPostRequest(String URL, JSONObject obj) throws IOException {

        URL url = new URL(URL);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(20000);
        conn.setConnectTimeout(20000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8"));
        try {
            writer.write(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        writer.flush();
        writer.close();
        os.close();

        int responseCode=conn.getResponseCode(); // To Check for 200
        if (responseCode == HttpsURLConnection.HTTP_OK) {

            BufferedReader in=new BufferedReader( new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer("");
            String line="";
            while((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();
            return sb.toString();
        }
        return null;
    }

    public class RequestAsync extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {

                // POST Request
                BodyRequest jsonBody = new BodyRequest();
                AuthParams params = new AuthParams(mUsername, mPassword);
                jsonBody.setJsonrpc("2.0");
                jsonBody.setMethod("authentify");
                jsonBody.setParams(params);

                JSONObject obj = jsonBody.getJsonObject();

                return httpPostRequest(API_URL, obj);
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
