package com.example.smartcollege.Request;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.smartcollege.Enum.AmdocsMethodsEnum;
import com.example.smartcollege.Enum.DevicesIdsEnum;
import com.example.smartcollege.REST.DevicesRequest;
import com.example.smartcollege.Response.StartVideoRecordingResponse;
import com.example.smartcollege.UpdateSubject;
import com.example.smartcollege.VideoSessionDetails;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

class StartVideoRecording implements UpdateSubject {
    private String encodingAuth;
    private SharedPreferences prefs;
    private Map<DevicesIdsEnum, List<String>> deviceParams;
    private VideoSessionDetails videoSession;

    public StartVideoRecording(VideoSessionDetails videoSessionDetails, Map<DevicesIdsEnum, List<String>> deviceParams, String encodingAuth, SharedPreferences prefs) {
        this.deviceParams = deviceParams;
        this.videoSession = videoSessionDetails;
        this.encodingAuth = encodingAuth;
        this.prefs = prefs;
        exec();
    }

    private void exec() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.serializeNulls().create();
        String jsonInString = gson.toJson(videoSession);
        deviceParams.get(DevicesIdsEnum.Camera).add(jsonInString);
        DevicesRequest request = new DevicesRequest(AmdocsMethodsEnum.START_VIDEO_RECORDING, deviceParams.get(DevicesIdsEnum.Camera), encodingAuth, this);
        request.execute();
    }

    @Override
    public void update(String res) {
        Gson json = new Gson();
        StartVideoRecordingResponse response = json.fromJson(res, StartVideoRecordingResponse.class);
        Log.d("Res", res);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new StopVideoRecording(videoSession, response.getRecordId(), deviceParams, encodingAuth, prefs);
    }
}
