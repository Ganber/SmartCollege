package com.example.smartcollege.Activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.smartcollege.CloseCollege;
import com.example.smartcollege.DevicesStatus;
import com.example.smartcollege.Enum.DevicesIdsEnum;
import com.example.smartcollege.GetImageSnapshots;
import com.example.smartcollege.R;
import com.example.smartcollege.Response.DeviceResponse;
import com.example.smartcollege.Response.StartVideoStreamingResponse;
import com.example.smartcollege.StartImage;
import com.example.smartcollege.StartVideoStreaming;
import com.example.smartcollege.UpdateSubject;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DashboardActivity extends Activity implements UpdateSubject, Runnable {
    private final String CLOSE_COLLEGE = "Close College";
    private final String EVENTS = "Events";
    private boolean systemTriggered = false;
    private String TOKEN;
    private String encodingAuth;
    private Button mDemoButton;
    private Button mCloseCollege;
    private Button mEvents;
    private Intent intent;
    private DevicesStatus devicesStatus;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final DashboardActivity activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        intent = getIntent();
        TOKEN = intent.getStringExtra("TOKEN");
        String auth = intent.getStringExtra("USER_NAME") + ":" + TOKEN;
        encodingAuth =new String(Base64.encode(auth.getBytes(),Base64.NO_WRAP));
        mDemoButton = findViewById(R.id.button_Demo);
        mCloseCollege = findViewById(R.id.button_close_college);
        mEvents = findViewById(R.id.button_events);

        prefs = getSharedPreferences(CLOSE_COLLEGE,MODE_PRIVATE);

        getDevicesStatus();

        mEvents.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showEvents();
            }
        });

        mCloseCollege.setOnClickListener(closeCollegeClick());

        mDemoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here we need to take the data from phone
                burglaryAlarm(prefs);
            }
        });
    }

    private View.OnClickListener closeCollegeClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeCollege(prefs);
                mCloseCollege.setOnClickListener(openCollegeClick());
                mCloseCollege.setText("Open College");
            }
        };
    }

    private View.OnClickListener openCollegeClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                systemTriggered = false;
                mCloseCollege.setOnClickListener(closeCollegeClick());
                mCloseCollege.setText("Close College");
            }
        };
    }

    private void showEvents() {
        //move to events page
        Intent intent = new Intent(DashboardActivity.this, EventsActivity.class);
        finish();
        startActivity(intent);
    }

    private void burglaryAlarm(SharedPreferences prefs) {
        //send notification
        //take a video snapshot
         takeVideoSnapshot();

        //take photos snapshots
         takePhotosSnapshots();

        //save data in phone for event mode
   //     saveEvents();
    }


    private void openCollege(){
        systemTriggered = true;
        Toast.makeText(this, "System is not triggered", Toast.LENGTH_SHORT).show();
    }

    private void closeCollege(SharedPreferences prefs){
        Map<DevicesIdsEnum,List<String>> devices = new HashMap();
        devices = setDeviceMapParams(devices,DevicesIdsEnum.MotionSensor);
        devices = setDeviceMapParams(devices,DevicesIdsEnum.WindowContact);
        new CloseCollege(devices,encodingAuth,prefs);
        systemTriggered = true;
        Toast.makeText(this, "System has been triggered", Toast.LENGTH_SHORT).show();
        new Thread(this).start();
    }

    private void showDeviceDetails(){
        devicesStatus.showDeviceDetails();
        devicesStatus = null;
    }

    private void takePhotosSnapshots() {
        new GetImageSnapshots(DevicesIdsEnum.Camera,encodingAuth,prefs);
        new StartImage(DevicesIdsEnum.Camera,encodingAuth,prefs);
    }

    private void takeVideoSnapshot() {
        Map<DevicesIdsEnum,List<String>> devices = new HashMap();
        devices = setDeviceMapParams(devices,DevicesIdsEnum.Camera);
        new StartVideoStreaming(devices,encodingAuth,prefs);
    }


    private void getDevicesStatus() {
        Map<DevicesIdsEnum,List<String>> devices = new HashMap();
        devices = setDeviceMapParams(devices,DevicesIdsEnum.Camera);
        devices = setDeviceMapParams(devices,DevicesIdsEnum.MotionSensor);
        devices = setDeviceMapParams(devices,DevicesIdsEnum.WindowContact);
        devicesStatus = getDevicesStatusResponse(devices,encodingAuth,() ->{
            if(devicesStatus != null && devicesStatus.getDevicesResponseSize() == 3){
                showDeviceDetails();
            }
        });
    }

    private Map<DevicesIdsEnum, List<String>> setDeviceMapParams(Map<DevicesIdsEnum, List<String>> devices, DevicesIdsEnum sensor) {
        devices.put(sensor,new ArrayList<String>());
        devices.get(sensor).add(Integer.toString(sensor.getDeviceId()));
        return devices;
    }

    @Override
    public void update(String res){
        Gson json = new Gson();
        StartVideoStreamingResponse r = json.fromJson(res,StartVideoStreamingResponse.class);
        Log.d("r",r.getStreamUrl());

    }

    @Override
    public void run() {
        Set<String> devicesToFollow = prefs.getStringSet("Devices",null);
        Map<String,String> deviceSavedStatus = new HashMap<>();
        for(String savedDevice : devicesToFollow){
            String[] splitArray = savedDevice.split(":");
            deviceSavedStatus.put(splitArray[0],splitArray[1]);
        }

        while (systemTriggered){
            for (Map.Entry<String, String> entry : deviceSavedStatus.entrySet()) {
                Map<DevicesIdsEnum,List<String>> devices = new HashMap();
                devices = setDeviceMapParams(devices,DevicesIdsEnum.findDeviceIdEnum(Integer.parseInt(entry.getKey())));
                devicesStatus = getDevicesStatusResponse(devices,encodingAuth,() ->{
                    if(devicesStatus != null && devicesStatus.getDevicesResponseSize() == 1){
                        checkBurglary(deviceSavedStatus);
                    }
                });
            }
        }
    }

    private void checkBurglary(Map<String,String> deviceSavedStatus) {
        for(DeviceResponse res : devicesStatus.getDevicesResponse()){
            if(res.getStatus() != deviceSavedStatus.get(res.getProductId())){
                //notify burglary
            }
        }
    }

    private DevicesStatus getDevicesStatusResponse(Map<DevicesIdsEnum, List<String>> devices, String encodingAuth, Runnable runWhenFinished) {
        DevicesStatus devicesStatus = new DevicesStatus(devices,encodingAuth,runWhenFinished);
        return devicesStatus;
    }
}
