package com.example.smartcollege.Request;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.smartcollege.DevicesStatus;
import com.example.smartcollege.Enum.AmdocsMethodsEnum;
import com.example.smartcollege.Enum.DevicesIdsEnum;
import com.example.smartcollege.REST.DevicesRequest;
import com.example.smartcollege.Response.GetRecordedVideosResponse;
import com.example.smartcollege.Response.StartVideoStreamingResponse;
import com.example.smartcollege.UpdateSubject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class GetRecordedVideos implements UpdateSubject, Runnable {
    private final String VIDEO_PROTOCOL="RTMP";
    private String encodingAuth;
    private SharedPreferences prefs;
    private List<String> deviceParams = new ArrayList<>();
    private DevicesStatus devicesStatus;
    private StartVideoStreamingResponse res;

    public GetRecordedVideos(String encodingAuth, SharedPreferences prefs) {
        this.encodingAuth = encodingAuth;
        this.prefs = prefs;
        exec();
    }

    public GetRecordedVideos() {
        exec();
    }

    public GetRecordedVideos(String encodingAuth) {
        this.encodingAuth = encodingAuth;
        exec();
    }

    private void exec() {
        VideoRecordsRequest videoRecordsRequest = new VideoRecordsRequest
                (Long.valueOf(DevicesIdsEnum.GateWay.toString()));
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.serializeNulls().create();
        String jsonInString = gson.toJson(videoRecordsRequest);
        deviceParams.add(jsonInString);
        DevicesRequest request = new DevicesRequest(AmdocsMethodsEnum.GET_RECORDED_VIDEOS, deviceParams, encodingAuth, this);
        request.execute();
    }

    @Override
    public void update(String res) {
        Gson json = new Gson();
        GetRecordedVideosResponse response = json.fromJson(res, GetRecordedVideosResponse.class);
        Log.d("Res",res);
    }

    @Override
    public void run() {

    }
}
