package com.example.smartcollege.Activities;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.smartcollege.CloseCollege;
import com.example.smartcollege.DevicesStatus;
import com.example.smartcollege.Enum.DevicesIdsEnum;
import com.example.smartcollege.Request.GetImageSnapshots;
import com.example.smartcollege.R;
import com.example.smartcollege.RecyclerViewAdapter;
import com.example.smartcollege.Response.DeviceResponse;
import com.example.smartcollege.Response.StartVideoStreamingResponse;
import com.example.smartcollege.Request.StartImage;
import com.example.smartcollege.Request.StartVideoStreaming;
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

    // RecyclerView
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private ArrayList<String> mDevicesNames = new ArrayList<>();
    private ArrayList<Integer> mDevicesImages = new ArrayList<>();
    private ArrayList<Long> mDevicesIDs = new ArrayList<>();
    private ArrayList<String> mDevicesStatus = new ArrayList<>();
    private ArrayList<String> mDevicesRoom = new ArrayList<>();
    private ArrayList<String> mDevicesType = new ArrayList<>();
    private ArrayList<String> mDevicesIsActive = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final DashboardActivity activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        intent = getIntent();
        TOKEN = intent.getStringExtra("TOKEN");
        String auth = intent.getStringExtra("USER_NAME") + ":" + TOKEN;
        encodingAuth =new String(Base64.encode(auth.getBytes(),Base64.NO_WRAP));
        mDemoButton = findViewById(R.id.button_Demo);
        mCloseCollege = findViewById(R.id.button_close_college);
        mEvents = findViewById(R.id.button_events);

        prefs = getSharedPreferences(CLOSE_COLLEGE,MODE_PRIVATE);

        getDevicesStatus();

        mRecyclerView = findViewById(R.id.recyclerView);

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
        //showNotification();

        takeVideoSnapshot();

        //take photos snapshots
        takePhotosSnapshots();

        //save data in phone for event mode
   //     saveEvents();
    }

    void showNotification() {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_stat_ac_unit) // notification icon
                .setContentTitle("My notification") // title for notification
                .setContentText("Hello World!")// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), EventsActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
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
        getDevicesInfo();
        devicesStatus = null;
    }

    private void takePhotosSnapshots() {
        new GetImageSnapshots(getApplicationContext(),DevicesIdsEnum.Camera,encodingAuth,prefs);
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

    private void getDevicesInfo() {

        for(DeviceResponse device : devicesStatus.getDevicesResponse()) {

            addDeviceImage(device.getType());

            mDevicesIDs.add(device.getDeviceId());
            mDevicesNames.add(device.getName());
            mDevicesIsActive.add(device.isActive() ? "ACTIVE" : "NOT ACTIVE");
            mDevicesStatus.add(device.getStatus());
            mDevicesType.add(device.getType());
            mDevicesRoom.add(device.getRoom());
        }

        mRecyclerViewAdapter = new RecyclerViewAdapter(mDevicesNames, mDevicesImages, mDevicesIDs, mDevicesStatus, mDevicesRoom, mDevicesType, mDevicesIsActive, this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void addDeviceImage(String type) {

        switch (type) {
            case ("tyco_contact"):
                mDevicesImages.add(R.drawable.widows_contact_img);
                break;
            case ("movable_cam_sercomm"):
                mDevicesImages.add(R.drawable.security_cam_img);
                break;
            case ("tyco_motion"):
                mDevicesImages.add(R.drawable.motion_sensor_img);
                break;
            default: break;
        }
    }
}
