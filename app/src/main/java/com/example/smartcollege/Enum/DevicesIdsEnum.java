package com.example.smartcollege.Enum;

public enum DevicesIdsEnum {
    GateWay(81004),
    Camera(156517),
    WindowContact(132561),
    MotionSensor(132558);

    private int deviceId;

    private DevicesIdsEnum(int deviceId){
        this.deviceId=deviceId;
    }

    public int getDeviceId(){
        return deviceId;
    }
}
