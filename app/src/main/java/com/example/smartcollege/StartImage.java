package com.example.smartcollege;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.smartcollege.Enum.AmdocsMethodsEnum;
import com.example.smartcollege.Enum.DevicesIdsEnum;
import com.example.smartcollege.REST.DevicesRequest;
import com.example.smartcollege.Response.StartImageResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class StartImage implements UpdateSubject{
    private DevicesIdsEnum cameraId;
    private String encodingAuth;
    private SharedPreferences prefs;
    private List<String> deviceParams = new ArrayList<>();

    public StartImage(DevicesIdsEnum cameraId, String encodingAuth, SharedPreferences prefs) {
        this.cameraId = cameraId;
        this.encodingAuth = encodingAuth;
        this.prefs = prefs;
        exec();
    }

    private void exec() {
        deviceParams.add(cameraId.toString());
        DevicesRequest request = new DevicesRequest(AmdocsMethodsEnum.START_IMAGE, deviceParams, encodingAuth, this);
        request.execute();
    }

    @Override
    public void update(String res) {
        Gson json = new Gson();
        StartImageResponse response = json.fromJson(res, StartImageResponse.class);
        Log.d("getImageSnapshots",response.getImageUrl());
    }
}