package com.example.smartcollege;

import android.content.SharedPreferences;

import com.example.smartcollege.Enum.DevicesIdsEnum;
import com.example.smartcollege.Response.DeviceResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CloseCollege implements Runnable{
    private Map<DevicesIdsEnum, List<String>> devices;
    private String encodingAuth;
    private SharedPreferences prefs;
    private DevicesStatus devicesStatus;

    public CloseCollege(Map<DevicesIdsEnum,List<String>> devices, String encodingAuth, SharedPreferences prefs){
        this.devices = devices;
        this.encodingAuth = encodingAuth;
        this.prefs = prefs;
        exec();
    }

    private void exec() {
        devicesStatus = new DevicesStatus(devices,encodingAuth,this);
    }

    @Override
    public void run(){
        if(devicesStatus != null && devicesStatus.getDevicesResponseSize() == devices.size()){
            Set<String> devicesToSave = new HashSet<>();
            for(DeviceResponse device : devicesStatus.getDevicesResponse()){
                if(device.isActive() && device.getSensorTriggerModeWhenSystemArmed().equals("ENABLED")){
                    devicesToSave.add(device.getDeviceId()  + ":" + device.getStatus());
                }
            }
            //save current devices status in phone
            prefs.edit().putStringSet("Devices",devicesToSave).apply();
        }
    }
}
