package com.example.smartcollege;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.smartcollege.JSONObjects.AuthParams;
import com.example.smartcollege.JSONObjects.BodyRequest;
import com.example.smartcollege.JSONObjects.DeviceParams;
import com.example.smartcollege.REST.RestRequests;

import org.json.JSONObject;

import java.io.IOException;

public class DashboardActivity extends Activity {
    private final String HOME_SERVICE_URL="https://sb.ch.amdocs.com/mobile-gateway/jsonrpc/HomeService";
    private String TOKEN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Intent intent = getIntent();
        TOKEN = Base64.encodeToString(intent.getStringExtra("TOKEN").getBytes(),Base64.NO_WRAP);
        new RequestGetDeviceAsync().execute();

    }

    public class RequestGetDeviceAsync extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {

                // POST Request
                BodyRequest jsonBody = new BodyRequest();
                jsonBody.addParameter("jsonrpc","2.0");
                jsonBody.addParameter("method","getDeviceAttributes");
                DeviceParams params = new DeviceParams(156517);
                jsonBody.setParams(params);

                JSONObject obj = jsonBody.getJsonObject();
                String response = new RestRequests().postRequest(HOME_SERVICE_URL, obj,TOKEN);
                Log.d("response",response);
                return response;
            } catch (Exception e) {
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        }
    }

}
