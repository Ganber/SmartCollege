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

public class StopVideoStreaming implements UpdateSubject, Runnable{
    private final String VIDEO_PROTOCOL="RTMP";
    private String encodingAuth;
    private SharedPreferences prefs;
    private Map<DevicesIdsEnum,List<String>> deviceParams = new HashMap<>();
    private DevicesStatus devicesStatus;
    private StartVideoStreamingResponse res;

    public StopVideoStreaming(StartVideoStreamingResponse res,Map<DevicesIdsEnum,List<String>> deviceParams, String encodingAuth, SharedPreferences prefs) {
        this.deviceParams = deviceParams;
        this.res = res;
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
        StartVideoStreamingResponse response = json.fromJson(res, StartVideoStreamingResponse.class);
        Log.d("Res",res);
    }

    @Override
    public void run() {
        /*deviceParams.get(DevicesIdsEnum.Camera).add(res.getStreamId());
        deviceParams.get(DevicesIdsEnum.Camera).add(res.getVideoServerIp());
        deviceParams.get(DevicesIdsEnum.Camera).add(res.getStreamUrl());
        deviceParams.get(DevicesIdsEnum.Camera).add(res.getStreamRtspUrl());
        deviceParams.get(DevicesIdsEnum.Camera).add(res.getStreamHlsUrl());
        deviceParams.get(DevicesIdsEnum.Camera).add(Integer.toString(res.getVideoPort()));
        deviceParams.get(DevicesIdsEnum.Camera).add(Integer.toString(res.getAudioPort()));*/
        VideoSessionDetails videoSessionDetails = new VideoSessionDetails(res.getStreamId(), res.getVideoServerIp(), res.getStreamUrl(), res.getStreamRtspUrl(),
                res.getStreamFlspUrl(), res.getStreamHlsUrl(), res.getVideoPort(), res.getAudioPort());
        VideoSession videoSession = new VideoSession(videoSessionDetails);
        GsonBuilder builder = new GsonBuilder();
        //Gson gson = new Gson();
        Gson gson = builder.serializeNulls().create();
        String jsonInString = gson.toJson(videoSessionDetails);
        deviceParams.get(DevicesIdsEnum.Camera).add(jsonInString);
        DevicesRequest request = new DevicesRequest(AmdocsMethodsEnum.STOP_VIDEO_STREAMING, deviceParams.get(DevicesIdsEnum.Camera), encodingAuth, this);
        request.execute();
    }
}