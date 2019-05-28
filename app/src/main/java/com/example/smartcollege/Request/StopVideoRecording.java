package com.example.smartcollege.Request;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.smartcollege.DevicesStatus;
import com.example.smartcollege.Enum.AmdocsMethodsEnum;
import com.example.smartcollege.Enum.DevicesIdsEnum;
import com.example.smartcollege.REST.DevicesRequest;
import com.example.smartcollege.Response.StartVideoRecordingResponse;
import com.example.smartcollege.UpdateSubject;
import com.example.smartcollege.VideoSessionDetails;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class StopVideoRecording implements UpdateSubject {
    private final String VIDEO_PROTOCOL = "RTMP";
    private String encodingAuth, recordId;
    private SharedPreferences prefs;
    private Map<DevicesIdsEnum, List<String>> deviceParams = new HashMap<>();
    private DevicesStatus devicesStatus;
    private VideoSessionDetails videoSession;

    public StopVideoRecording(VideoSessionDetails videoSession, String recordId, Map<DevicesIdsEnum, List<String>> deviceParams, String encodingAuth, SharedPreferences prefs) {
        this.deviceParams = deviceParams;
        this.videoSession = videoSession;
        this.encodingAuth = encodingAuth;
        this.prefs = prefs;
        this.recordId = recordId;
        exec();
    }

    private void exec() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.serializeNulls().create();
        String recordIdString = gson.toJson(recordId);
        deviceParams.get(DevicesIdsEnum.Camera).add(recordIdString);
        DevicesRequest request = new DevicesRequest(AmdocsMethodsEnum.STOP_VIDEO_RECORDING, deviceParams.get(DevicesIdsEnum.Camera), encodingAuth, this);
        request.execute();
    }

    @Override
    public void update(String res) {
        Gson json = new Gson();
        //StartVideoRecordingResponse response = json.fromJson(res, StartVideoRecordingResponse.class);
        Log.d("Res", res);
        new StopVideoStreaming(videoSession, deviceParams, encodingAuth, prefs);
    }

    /*@Override
    public void run() {

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.serializeNulls().create();
        String jsonInString = gson.toJson(videoSession);
        deviceParams.get(DevicesIdsEnum.Camera).add(jsonInString);
        DevicesRequest request = new DevicesRequest(AmdocsMethodsEnum.START_VIDEO_RECORDING, deviceParams.get(DevicesIdsEnum.Camera), encodingAuth, this);
        request.execute();
    }*/
}
