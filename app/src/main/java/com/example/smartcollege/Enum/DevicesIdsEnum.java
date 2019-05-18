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

    public static DevicesIdsEnum findDeviceIdEnum(int deviceId){
        for(DevicesIdsEnum current : DevicesIdsEnum.values()){
            if(current.deviceId == deviceId){
                return current;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return Integer.toString(deviceId);
    }
}
