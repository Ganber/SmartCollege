package com.example.smartcollege;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
        TOKEN = intent.getStringExtra("TOKEN");
        BodyRequest jsonBody = new BodyRequest();
        jsonBody.addParameter("jsonrpc","2.0");
        jsonBody.addParameter("method","getDeviceAttributes");
        jsonBody.addParameter("Authorization",TOKEN);
        DeviceParams params = new DeviceParams(156517);
        jsonBody.setParams(params);

        JSONObject obj = jsonBody.getJsonObject();
        try{
            String response = new RestRequests().postRequest(HOME_SERVICE_URL, obj);
            Log.d("response",response);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
