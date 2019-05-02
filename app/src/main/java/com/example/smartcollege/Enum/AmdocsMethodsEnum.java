package com.example.smartcollege.Enum;

public enum AmdocsMethodsEnum {
    GET_DEVICES("getDevices"),
    AUTHENTIFY("authentify");

    private String method;

    private AmdocsMethodsEnum(String method){
        this.method = method;
    }

    public String getMethod(){
        return  method;
    }
}
