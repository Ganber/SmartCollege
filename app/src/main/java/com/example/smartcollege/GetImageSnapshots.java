package com.example.smartcollege;

import android.content.SharedPreferences;

import com.example.smartcollege.Enum.AmdocsMethodsEnum;
import com.example.smartcollege.Enum.DevicesIdsEnum;
import com.example.smartcollege.REST.DevicesRequest;
import com.example.smartcollege.Response.DeviceResponse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetImageSnapshots{
    private DevicesIdsEnum cameraId;
    private String encodingAuth;
    private SharedPreferences prefs;
    private List<String> deviceParams = new ArrayList<>();

    public GetImageSnapshots(DevicesIdsEnum cameraId, String encodingAuth, SharedPreferences prefs){
        this.cameraId = cameraId;
        this.encodingAuth = encodingAuth;
        this.prefs = prefs;
        exec();
    }

    private void exec() {
        /*
        for (String param : entry.getValue()) {
                    deviceParams.add(param);
                }
                DevicesRequest request = new DevicesRequest(AmdocsMethodsEnum.GET_DEVICE, deviceParams, encodingAuth, this);
                request.execute();
            }
        }
        */
    }


/*

    @Override
    public void run(){
        if(devicesStatus != null && devicesStatus.getDevicesResponseSize() == devices.size()){
            Set<String> devicesToSave = new HashSet<>();
            for(DeviceResponse device : devicesStatus.getDevicesResponse()){
                if(device.isActive() && device.getSensorTriggerModeWhenSystemArmed().equals("ENABLED")){
                    devicesToSave.add(Long.toString(device.getDeviceId())  + ":" + device.getStatus());
                }
            }

            prefs.edit().putStringSet("Devices",devicesToSave).apply();
        }
    }
*/
}
