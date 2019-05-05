package com.example.smartcollege.REST;

import android.os.AsyncTask;
import android.util.Log;

import com.example.smartcollege.Enum.AmdocsMethodsEnum;
import com.example.smartcollege.Enum.HTTPMethodsEnum;
import com.example.smartcollege.JSONObjects.BodyRequest;
import com.example.smartcollege.JSONObjects.DeviceParams;
import com.example.smartcollege.Subject;

import org.json.JSONObject;

import java.util.List;

public class DevicesRequest extends AsyncTask<String,String,String> {
    private AmdocsMethodsEnum deviceEnum;
    private List<String> deviceParams;
    private String encodingAuth;
    private Subject responseNotifier;
    private final String HOME_SERVICE_URL="https://sb.ch.amdocs.com/mobile-gateway/jsonrpc/HomeService";

    public DevicesRequest(AmdocsMethodsEnum deviceEnum, List<String> deviceId, String encodingAuth, Subject responseNotifier){
        this.deviceEnum = deviceEnum;
        this.deviceParams = deviceId;
        this.encodingAuth=encodingAuth;
        this.responseNotifier = responseNotifier;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {

            // POST Request
            BodyRequest jsonBody = new BodyRequest();
            jsonBody.addParameter("jsonrpc","2.0");
            jsonBody.addParameter("method", deviceEnum.getMethod());
            DeviceParams params = new DeviceParams(deviceParams);
            jsonBody.setParams(params);

            JSONObject obj = jsonBody.getJsonObject();
            String response = new RestRequests().HttpRequest(HOME_SERVICE_URL, obj,encodingAuth, HTTPMethodsEnum.POST);
            Log.d("response",response);
            return response;
        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String response) {
        if (response != null) {
//            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            Log.d("res",response);
            responseNotifier.update(response);
        }
    }

}
