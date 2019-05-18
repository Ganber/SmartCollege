package com.example.smartcollege.JSONObjects;

import java.util.List;

public class DeviceParams extends BodyParams {

    public DeviceParams(List<String> deviceParams){
        for(String param : deviceParams)
            super.addParam("params",param);
    }
}
