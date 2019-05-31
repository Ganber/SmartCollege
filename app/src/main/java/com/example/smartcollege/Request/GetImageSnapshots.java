package com.example.smartcollege.Request;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.smartcollege.BitmapDecoder;
import com.example.smartcollege.Enum.AmdocsMethodsEnum;
import com.example.smartcollege.Enum.DevicesIdsEnum;
import com.example.smartcollege.Enum.HTTPMethodsEnum;
import com.example.smartcollege.JSONObjects.BodyRequest;
import com.example.smartcollege.JSONObjects.DeviceParams;
import com.example.smartcollege.REST.DevicesRequest;
import com.example.smartcollege.REST.RestRequests;
import com.example.smartcollege.Response.GetImageSnapshotsResponse;
import com.example.smartcollege.UpdateSubject;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetImageSnapshots implements UpdateSubject {
    private DevicesIdsEnum cameraId;
    private String encodingAuth;
    private SharedPreferences prefs;
    private Context context;
    private List<String> deviceParams = new ArrayList<>();

    public GetImageSnapshots(Context context, DevicesIdsEnum cameraId, String encodingAuth, SharedPreferences prefs) {
        this.cameraId = cameraId;
        this.encodingAuth = encodingAuth;
        this.prefs = prefs;
        this.context = context;
        exec();
    }

    private void exec() {
        deviceParams.add(cameraId.toString());
        DevicesRequest request = new DevicesRequest(AmdocsMethodsEnum.GET_IMAGE_SNAPSHOT, deviceParams, encodingAuth, this);
        request.execute();
    }

    @Override
    public void update(String res) {
        Gson json = new Gson();
        GetImageSnapshotsResponse response = json.fromJson(res, GetImageSnapshotsResponse.class);
        try {
            String s = response.getUrl().replace("https://","http://");
            URL url = new URL(s);
            BitmapDecoder.decodeSnapshot(url,200,200,encodingAuth);
            //save photos in phone
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d("getImageSnapshots",response.getUrl());

        BodyRequest jsonBody = new BodyRequest();
        jsonBody.addParameter("jsonrpc","2.0");
        DeviceParams params = new DeviceParams(deviceParams);
        jsonBody.setParams(params);

        JSONObject obj = jsonBody.getJsonObject();
        try {
            String ress = new RestRequests().HttpRequest(response.getUrl(), obj,encodingAuth, HTTPMethodsEnum.GET);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("File1", Context.MODE_PRIVATE));
            outputStreamWriter.write(ress);
            outputStreamWriter.close();

            String filePath = context.getFilesDir().getPath();
            File f = new File(filePath);
            try{
                f.createNewFile();
                BufferedWriter buf = new BufferedWriter(new FileWriter(f, true));
                buf.append(ress);
                buf.newLine();
                buf.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}