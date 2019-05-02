package com.example.smartcollege.JSONObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class BodyRequest{

    private JSONObject mBody = new JSONObject();
    private JSONArray mParams = new JSONArray();

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
            boolean isParamsSet = false;
            for(Map.Entry<String, String> entry : params.getParams().entrySet()){
                if(entry.getKey().equals("params")){
                    mParams.put(Integer.valueOf(entry.getValue()));
                    isParamsSet = true;
                }
                else{
                    paramsObject.put(entry.getKey(),entry.getValue());
                }
            }
            if(!isParamsSet){
                mParams.put(paramsObject);
            }
         } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getJsonObject(){
        if(mParams.length() != 0){
            try{
                mBody.put("params",mParams);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mBody;
    }


}
