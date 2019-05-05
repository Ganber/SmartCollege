package com.example.smartcollege;

import android.util.Log;

import com.example.smartcollege.Enum.AmdocsMethodsEnum;
import com.example.smartcollege.Enum.DevicesIdsEnum;
import com.example.smartcollege.REST.DevicesRequest;
import com.example.smartcollege.Response.DeviceResponse;
import com.example.smartcollege.Response.StartVideoStreamingResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CloseCollege implements Subject {
    private Map<DevicesIdsEnum,List<String>> devices = new HashMap();
    private Map<Long,DeviceResponse> devicesResponse = new HashMap<>();
    private String encodingAuth;
    public CloseCollege(Map<DevicesIdsEnum,List<String>> devices, String encodingAuth){
        this.devices = devices;
        this.encodingAuth = encodingAuth;
        exec();
    }

    private void exec() {
        if (devices != null && !devices.isEmpty()) {
            for (Map.Entry<DevicesIdsEnum, List<String>> entry : devices.entrySet()) {
                List<String> deviceParams = new ArrayList<>();
                for(String param : entry.getValue()){
                    deviceParams.add(param);
                }
                DevicesRequest request = new DevicesRequest(AmdocsMethodsEnum.GET_DEVICE,deviceParams,encodingAuth,this);
                request.execute();
            }
        }
    }

    @Override
    public void update(String res) {
            Gson json = new Gson();
            DeviceResponse r = json.fromJson(res,DeviceResponse.class);
            devicesResponse.put(r.getDeviceId(),r);
    }
}
