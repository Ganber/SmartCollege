package com.example.smartcollege;

public enum HTTPMethodsEnum {
    GET("GET"),
    POST("POST");

    private String methodName;

    private HTTPMethodsEnum(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName(){
        return methodName;
    }
}
