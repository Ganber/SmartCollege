package com.example.smartcollege.JSONObjects;

public class DeviceParams extends BodyParams {
    public DeviceParams(long deviceID){
        super.addParam("deviceId",Long.toString(deviceID));
    }
}