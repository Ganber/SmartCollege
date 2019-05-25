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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.smartcollege.CloseCollege;
import com.example.smartcollege.DevicesStatus;
import com.example.smartcollege.DisplayDevices;
import com.example.smartcollege.Enum.DevicesIdsEnum;
import com.example.smartcollege.Request.GetImageSnapshots;
import com.example.smartcollege.R;
import com.example.smartcollege.Adapters.RecyclerViewAdapter;
import com.example.smartcollege.Request.GetRecordedVideos;
import com.example.smartcollege.Response.DeviceResponse;
import com.example.smartcollege.Request.StartImage;
import com.example.smartcollege.Request.StartVideoStreaming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DashboardActivity extends Activity implements Runnable {
    private final String CLOSE_COLLEGE = "Close College";
    private final String EVENTS = "Events";
    private final String CHANNEL_ID = "YOUR_CHANNEL_ID";
    private final String CHANNEL_NAME = "YOUR_CHANNEL_NAME";
    private final String CHANNEL_DESCRIPTION = "YOUR_NOTIFICATION_CHANNEL_DESCRIPTION";
    private boolean systemTriggered = false;
    private String TOKEN;
    private String encodingAuth;
    private Button mDemoButton;
    private Button mCloseCollege;
    private Button mEvents;
    private Intent intent;
    private DevicesStatus devicesStatus;
    private SharedPreferences prefs;
    private DisplayDevices displayDevices;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mDemoButton = findViewById(R.id.button_Demo);
        mCloseCollege = findViewById(R.id.button_close_college);
        mEvents = findViewById(R.id.button_events);
        mRecyclerView = findViewById(R.id.recyclerView);
        //set that main thread can do GET requests
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //get variables from MainActivity
        intent = getIntent();
        TOKEN = intent.getStringExtra("TOKEN");
        String auth = intent.getStringExtra("USER_NAME") + ":" + TOKEN;
        encodingAuth =new String(Base64.encode(auth.getBytes(),Base64.NO_WRAP));

        prefs = getSharedPreferences(CLOSE_COLLEGE,MODE_PRIVATE);
        //set button clicks listener
        mEvents.setOnClickListener(v -> showEvents());

        mCloseCollege.setOnClickListener(closeCollegeClick());

        mDemoButton.setOnClickListener(v -> {
            //TODO: here we need to take the data from phone
            burglaryAlarm(prefs);
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        //refresh the display devices data every 10 seconds
        displayDevices = new DisplayDevices(this::getDevicesStatus);
        new Thread(new DisplayDevices(displayDevices)).start();
    }

    private View.OnClickListener closeCollegeClick(){
        return v -> {
            closeCollege(prefs);
            mCloseCollege.setOnClickListener(openCollegeClick());
            mCloseCollege.setText(R.string.open_College);
        };
    }

    private View.OnClickListener openCollegeClick(){
        return v -> {
            systemTriggered = false;
            mCloseCollege.setOnClickListener(closeCollegeClick());
            mCloseCollege.setText(R.string.close_college);
            Toast.makeText(this, "System is not triggered", Toast.LENGTH_SHORT).show();
        };
    }

    private void showEvents() {
        Intent intent = new Intent(DashboardActivity.this, EventsActivity.class);
        displayDevices.Stop();
        startActivity(intent);
    }

    private void burglaryAlarm(SharedPreferences prefs) {
        burglaryNotification();
        //take a video snapshot
        takeVideoSnapshot();
        //take photos snapshots
        takePhotosSnapshots();

        //save data in phone for event mode
   //     saveEvents();
    }

    private void burglaryNotification() {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESCRIPTION);
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_ac_unit) // notification icon
                .setContentTitle("Burglary") // title for notification
                .setContentText("A burglary happened!")// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), EventsActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }

    private void closeCollege(SharedPreferences prefs){
        Map<DevicesIdsEnum,List<String>> devices = new HashMap();
        setDeviceMapParams(devices,DevicesIdsEnum.MotionSensor,DevicesIdsEnum.WindowContact);
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
        setDeviceMapParams(devices,DevicesIdsEnum.Camera);
        new StartVideoStreaming(devices,encodingAuth,prefs);
    }

    private void getRecordedVideos() {
        GetRecordedVideos getRecordedVideos = new GetRecordedVideos();
    }

    private void getDevicesStatus() {
        Map<DevicesIdsEnum,List<String>> devices = new HashMap();
        setDeviceMapParams(devices,DevicesIdsEnum.Camera,DevicesIdsEnum.MotionSensor,DevicesIdsEnum.WindowContact);
        devicesStatus = getDevicesStatusResponse(devices,encodingAuth,() ->{
            if(devicesStatus != null && devicesStatus.getDevicesResponseSize() == 3){
                showDeviceDetails();
            }
        });
    }

    private void setDeviceMapParams(Map<DevicesIdsEnum, List<String>> devices, DevicesIdsEnum... sensors) {
        for(DevicesIdsEnum current : sensors){
            if(!devices.containsKey(current)){
                devices.put(current,new ArrayList<>());
            }
            devices.get(current).add(Integer.toString(current.getDeviceId()));
        }


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
                setDeviceMapParams(devices,DevicesIdsEnum.findDeviceIdEnum(Integer.parseInt(entry.getKey())));
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
            if(!res.getStatus().equals(deviceSavedStatus.get(res.getProductId()))){
                burglaryAlarm(prefs);
            }
        }
    }

    private DevicesStatus getDevicesStatusResponse(Map<DevicesIdsEnum, List<String>> devices, String encodingAuth, Runnable runWhenFinished) {
        DevicesStatus devicesStatus = new DevicesStatus(devices,encodingAuth,runWhenFinished);
        return devicesStatus;
    }

    private void getDevicesInfo() {
        clearDevicesData();
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

    private void clearDevicesData(){
        mDevicesIDs.clear();
        mDevicesNames.clear();
        mDevicesIsActive.clear();
        mDevicesStatus.clear();
        mDevicesType.clear();
        mDevicesRoom.clear();
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
