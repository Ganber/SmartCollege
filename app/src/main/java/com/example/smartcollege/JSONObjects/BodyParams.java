package com.example.smartcollege.JSONObjects;

import java.util.HashMap;
import java.util.Map;

public abstract class BodyParams {

    private Map<String,String> params = new HashMap<>();

    protected void addParam(String key, String value){
        params.put(key,value);
    }
    public Map<String,String> getParams(){
        return params;
    }
}
