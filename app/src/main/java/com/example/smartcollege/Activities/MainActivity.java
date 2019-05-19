package com.example.smartcollege.Activities;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smartcollege.Enum.AmdocsMethodsEnum;
import com.example.smartcollege.Enum.HTTPMethodsEnum;
import com.example.smartcollege.JSONObjects.AuthParams;
import com.example.smartcollege.JSONObjects.BodyRequest;
import com.example.smartcollege.R;
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
                mUsername = "orangeDemo";
                mPassword = "Password1";

                new RequestLoginAsync().execute();

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
                                intent.putExtra("USER_NAME", mUsername);

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

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notify_001", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public class RequestLoginAsync extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {

                // POST Request
                BodyRequest jsonBody = new BodyRequest();
                AuthParams params = new AuthParams(mUsername, mPassword);
                jsonBody.addParameter("jsonrpc","2.0");
                jsonBody.addParameter("method", AmdocsMethodsEnum.AUTHENTIFY.getMethod());
                jsonBody.setParams(params);

                JSONObject obj = jsonBody.getJsonObject();

                return new RestRequests().HttpRequest(API_URL, obj,null, HTTPMethodsEnum.POST);
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
