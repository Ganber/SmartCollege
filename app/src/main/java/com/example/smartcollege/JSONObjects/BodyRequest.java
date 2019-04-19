package com.example.smartcollege.JSONObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class BodyRequest{

    JSONObject mBody = new JSONObject();
    JSONArray mParams = new JSONArray();
    Boolean mParamsAlreadySet = false;

    public void addParameter(String jsonKey, String jsonValue) {
        try {
            mBody.put(jsonKey,jsonValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setParams(BodyParams params){
        JSONObject paramsObject = new JSONObject();
        try {
            for(Map.Entry<String, String> entry : params.getParams().entrySet()){
                paramsObject.put(entry.getKey(),entry.getValue());
            }
            mParams.put(paramsObject);
         } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getJsonObject(){
        if(mParams.length() != 0 && !mParamsAlreadySet){
            try{
                mBody.put("params",mParams);
                mParamsAlreadySet = true;
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mBody;
    }


}
