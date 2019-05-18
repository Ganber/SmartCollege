package com.example.smartcollege;

import android.util.Log;

import com.example.smartcollege.Enum.AmdocsMethodsEnum;
import com.example.smartcollege.Enum.DevicesIdsEnum;
import com.example.smartcollege.REST.DevicesRequest;
import com.example.smartcollege.Response.DeviceResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DevicesStatus implements UpdateSubject {
    private Object object = new Object();
    private final Map<DevicesIdsEnum,List<String>> devices;
    private Map<Long,DeviceResponse> devicesResponse = new ConcurrentHashMap<>();
    private String encodingAuth;
    private Runnable notifier;

    public DevicesStatus(Map<DevicesIdsEnum,List<String>> devices, String encodingAuth, Runnable runWhenFinished){
        this.devices = devices;
        this.encodingAuth = encodingAuth;
        notifier = runWhenFinished;
        exec();
    }

    private void exec() {
        if (devices != null && !devices.isEmpty()) {
            for (Map.Entry<DevicesIdsEnum, List<String>> entry : devices.entrySet()) {
                List<String> deviceParams = new ArrayList<>();
                for (String param : entry.getValue()) {
                    deviceParams.add(param);
                }
                DevicesRequest request = new DevicesRequest(AmdocsMethodsEnum.GET_DEVICE, deviceParams, encodingAuth, this);
                request.execute();
            }
        }
    }
    @Override
    public void update(String res) {
            Gson json = new Gson();
            DeviceResponse response = json.fromJson(res, DeviceResponse.class);
            devicesResponse.put(response.getDeviceId(), response);
            notifier.run();
    }

    public int getDevicesResponseSize(){
         return devicesResponse.size();
    }

    public Collection<DeviceResponse> getDevicesResponse(){
        return devicesResponse.values();
    }

    public void showDeviceDetails() {
        for(DeviceResponse device : devicesResponse.values()){
            Log.d("-","-----------------");
            Log.d(device.getName(),"Is Active: "+device.isActive());
            Log.d(device.getName(),"Status: " +device.getStatus());
            Log.d(device.getName(),"Type:" + device.getType());
            Log.d(device.getName(),"Room: "+device.getRoom());
            Log.d(device.getName(),"BatteryStatus: "+ device.getBatteryStatus());

        }
    }
}
