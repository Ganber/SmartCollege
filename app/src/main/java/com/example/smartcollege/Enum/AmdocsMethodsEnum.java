package com.example.smartcollege.Enum;

public enum AmdocsMethodsEnum {
    GET_DEVICES("getDevices"),
    GET_DEVICE("getDevice"),
    GET_STREAMING_CAMERA("getStreamingCamera"),
    GET_IMAGE_SNAPSHOT("getImageSnapshot"),
    START_VIDEO_STREAMING("startVideoStreaming"),
    STOP_VIDEO_STREAMING("stopVideoStreaming"),
    GET_RECORDED_VIDEOS("getRecordedVideos"),
    START_IMAGE("startImage"),
    START_VIDEO_RECORDING("startVideoRecording"),
    STOP_VIDEO_RECORDING("stopVideoRecording"),
    AUTHENTIFY("authentify");

    private String method;

    AmdocsMethodsEnum(String method){
        this.method = method;
    }

    public String getMethod(){
        return  method;
    }
}
