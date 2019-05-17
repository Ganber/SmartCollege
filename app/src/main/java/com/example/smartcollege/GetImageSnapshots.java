package com.example.smartcollege;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.smartcollege.Enum.AmdocsMethodsEnum;
import com.example.smartcollege.Enum.DevicesIdsEnum;
import com.example.smartcollege.REST.DevicesRequest;
import com.example.smartcollege.Response.DeviceResponse;
import com.example.smartcollege.Response.GetImageSnapshotsResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetImageSnapshots implements UpdateSubject{
    private DevicesIdsEnum cameraId;
    private String encodingAuth;
    private SharedPreferences prefs;
    private List<String> deviceParams = new ArrayList<>();

    public GetImageSnapshots(DevicesIdsEnum cameraId, String encodingAuth, SharedPreferences prefs) {
        this.cameraId = cameraId;
        this.encodingAuth = encodingAuth;
        this.prefs = prefs;
        exec();
    }

    private void exec() {
        deviceParams.add(cameraId.toString());
        DevicesRequest request = new DevicesRequest(AmdocsMethodsEnum.GET_IMAGE_SNAPSHOT, deviceParams, encodingAuth, this);
        request.execute();
    }

    @Override
    public void update(String res) {
        Gson json = new Gson();
        GetImageSnapshotsResponse response = json.fromJson(res, GetImageSnapshotsResponse.class);
        Log.d("getImageSnapshots",response.getUrl());
    }
}