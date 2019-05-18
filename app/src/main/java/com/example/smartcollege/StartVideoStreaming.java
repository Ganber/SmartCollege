package com.example.smartcollege;

import android.content.SharedPreferences;

import com.example.smartcollege.Enum.AmdocsMethodsEnum;
import com.example.smartcollege.Enum.DevicesIdsEnum;
import com.example.smartcollege.REST.DevicesRequest;
import com.example.smartcollege.Response.DeviceResponse;
import com.example.smartcollege.Response.GetImageSnapshotsResponse;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartVideoStreaming implements UpdateSubject, Runnable{
    private String encodingAuth;
    private SharedPreferences prefs;
    private Map<DevicesIdsEnum,List<String>> deviceParams = new HashMap<>();
    private DevicesStatus devicesStatus;
    public StartVideoStreaming(Map<DevicesIdsEnum,List<String>> deviceParams, String encodingAuth, SharedPreferences prefs) {
        this.deviceParams = deviceParams;
        this.encodingAuth = encodingAuth;
        this.prefs = prefs;
        exec();
    }

    private void exec() {
        devicesStatus = new DevicesStatus(deviceParams,encodingAuth,this);
    }

    @Override
    public void update(String res) {
        Gson json = new Gson();
        StartVideoStreaming response = json.fromJson(res, StartVideoStreaming.class);
    }

    @Override
    public void run() {
        for(DeviceResponse res : devicesStatus.getDevicesResponse()){
            deviceParams.get(DevicesIdsEnum.Camera).add(res.getProtocol());
        }
        devicesStatus.getDevicesResponse().stream()
                .filter(value -> (int)value.getDeviceId() == DevicesIdsEnum.Camera.getDeviceId())
                .map(value -> deviceParams.get(DevicesIdsEnum.Camera).add(value.getProtocol()));
        DevicesRequest request = new DevicesRequest(AmdocsMethodsEnum.START_VIDEO_STREAMING, deviceParams.get(DevicesIdsEnum.Camera), encodingAuth, this);
        request.execute();
    }
}