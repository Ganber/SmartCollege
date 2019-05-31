package com.example.smartcollege.Enum;

public enum HTTPMethodsEnum {
    GET("GET"),
    POST("POST");

    private String methodName;

    HTTPMethodsEnum(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName(){
        return methodName;
    }
}
