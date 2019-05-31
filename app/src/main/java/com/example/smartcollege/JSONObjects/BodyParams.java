package com.example.smartcollege.JSONObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BodyParams {

    private Map<String, List<String>> params = new HashMap<>();

    protected void addParam(String key, String value){
        if(params.containsKey(key)){
            params.get(key).add(value);
        }
        else{
            params.put(key,new ArrayList<>());
            params.get(key).add(value);
        }
    }
    public Map<String,List<String>> getParams(){
        return params;
    }
}
