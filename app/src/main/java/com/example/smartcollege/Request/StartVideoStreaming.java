package com.example.smartcollege.Request;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.smartcollege.DevicesStatus;
import com.example.smartcollege.Enum.AmdocsMethodsEnum;
import com.example.smartcollege.Enum.DevicesIdsEnum;
import com.example.smartcollege.REST.DevicesRequest;
import com.example.smartcollege.Response.StartVideoStreamingResponse;
import com.example.smartcollege.UpdateSubject;
import com.example.smartcollege.VideoSessionDetails;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartVideoStreaming implements UpdateSubject, Runnable{
    private final String VIDEO_PROTOCOL="RTMP";
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
        StartVideoStreamingResponse response = json.fromJson(res, StartVideoStreamingResponse.class);
        Log.d("Res",res);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        deviceParams.get(DevicesIdsEnum.Camera).remove(1);
        VideoSessionDetails videoSessionDetails = new VideoSessionDetails(response.getStreamId(), response.getVideoServerIp(),
                response.getStreamUrl(), response.getStreamRtspUrl(),
                response.getStreamFlspUrl(), response.getStreamHlsUrl(),
                response.getVideoPort(), response.getAudioPort());
        new StartVideoRecording(videoSessionDetails, deviceParams, encodingAuth, prefs);
    }

    @Override
    public void run() {
        deviceParams.get(DevicesIdsEnum.Camera).add(VIDEO_PROTOCOL);
        DevicesRequest request = new DevicesRequest(AmdocsMethodsEnum.START_VIDEO_STREAMING, deviceParams.get(DevicesIdsEnum.Camera), encodingAuth, this);
        request.execute();
    }
}