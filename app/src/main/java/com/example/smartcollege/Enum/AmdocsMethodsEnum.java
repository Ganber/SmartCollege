package com.example.smartcollege.Enum;

public enum AmdocsMethodsEnum {
    GET_DEVICES("getDevices"),
    GET_DEVICE("getDevice"),
    GET_STREAMING_CAMERA("getStreamingCamera"),
    GET_IMAGE_SNAPSHOT("getImageSnapshot"),
    START_VIDEO_STREAMING("startVideoStreaming"),
    AUTHENTIFY("authentify");

    private String method;

    private AmdocsMethodsEnum(String method){
        this.method = method;
    }

    public String getMethod(){
        return  method;
    }
}
