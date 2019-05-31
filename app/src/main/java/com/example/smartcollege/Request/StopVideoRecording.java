package com.example.smartcollege.Request;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.smartcollege.Enum.AmdocsMethodsEnum;
import com.example.smartcollege.Enum.DevicesIdsEnum;
import com.example.smartcollege.REST.DevicesRequest;
import com.example.smartcollege.UpdateSubject;
import com.example.smartcollege.VideoSessionDetails;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

class StopVideoRecording implements UpdateSubject {
    private String encodingAuth, recordId;
    private SharedPreferences prefs;
    private Map<DevicesIdsEnum, List<String>> deviceParams;
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
        Log.d("Res", res);
        new StopVideoStreaming(videoSession, deviceParams, encodingAuth, prefs);
    }
}
