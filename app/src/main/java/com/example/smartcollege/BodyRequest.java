package com.example.smartcollege;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BodyRequest {

    JSONObject mBody = new JSONObject();
    JSONArray mParams = new JSONArray();

    public void setJsonrpc(String jsonrpc) {
        try {
            mBody.put("jsonrpc",jsonrpc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setMethod(String method) {
        try {
            mBody.put("method",method);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setParams(AuthParams params){
        JSONObject user = new JSONObject();
        try {
            user.put("password",params.getPassword());
            user.put("username",params.getUsername());
            mParams.put(user);

            mBody.put("params",mParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getJsonObject(){
        return mBody;
    }

}
