package com.example.smartcollege.Request;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.smartcollege.DevicesStatus;
import com.example.smartcollege.Enum.AmdocsMethodsEnum;
import com.example.smartcollege.Enum.DevicesIdsEnum;
import com.example.smartcollege.REST.DevicesRequest;
import com.example.smartcollege.Response.StartVideoStreamingResponse;
import com.example.smartcollege.UpdateSubject;
import com.example.smartcollege.VideoSession;
import com.example.smartcollege.VideoSessionDetails;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StopVideoStreaming implements UpdateSubject, Runnable{
    private final String VIDEO_PROTOCOL="RTMP";
    private String encodingAuth;
    private SharedPreferences prefs;
    private Map<DevicesIdsEnum,List<String>> deviceParams = new HashMap<>();
    private DevicesStatus devicesStatus;
    private VideoSessionDetails videoSession;

    public StopVideoStreaming(VideoSessionDetails videoSessionDetails, Map<DevicesIdsEnum,List<String>> deviceParams, String encodingAuth, SharedPreferences prefs) {
        this.deviceParams = deviceParams;
        this.videoSession = videoSessionDetails;
        this.encodingAuth = encodingAuth;
        this.prefs = prefs;
        exec();
    }

    private void exec() {
        Objects.requireNonNull(deviceParams.get(DevicesIdsEnum.Camera)).clear();
        deviceParams.get(DevicesIdsEnum.Camera).add(Integer.toString(DevicesIdsEnum.Camera.getDeviceId()));
        devicesStatus = new DevicesStatus(deviceParams,encodingAuth,this);
    }

    @Override
    public void update(String res) {
        Gson json = new Gson();
        //StartVideoStreamingResponse response = json.fromJson(res, StartVideoStreamingResponse.class);
        Log.d("Res",res);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new GetRecordedVideos(encodingAuth);
    }

    @Override
    public void run() {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.serializeNulls().create();
        String jsonInString = gson.toJson(videoSession);
        deviceParams.get(DevicesIdsEnum.Camera).add(jsonInString);
        DevicesRequest request = new DevicesRequest(AmdocsMethodsEnum.STOP_VIDEO_STREAMING, deviceParams.get(DevicesIdsEnum.Camera), encodingAuth, this);
        request.execute();
    }
}