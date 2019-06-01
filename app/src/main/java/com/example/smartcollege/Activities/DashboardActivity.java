package com.example.smartcollege.Activities;

import java.util.Calendar;
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
import android.widget.ImageView;
import android.widget.TextView;
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
import com.example.smartcollege.Request.StartVideoStreaming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DashboardActivity extends Activity implements Runnable {
    private final String CLOSE_COLLEGE = "Close College";
    private final String EVENTS = "Events";
    private final String EVENT_ID = "Event ID";
    private final String ID = "ID";
    private final String DATE = "DATE";
    private final String CHANNEL_ID = "YOUR_CHANNEL_ID";
    private final String CHANNEL_NAME = "YOUR_CHANNEL_NAME";
    private final String CHANNEL_DESCRIPTION = "YOUR_NOTIFICATION_CHANNEL_DESCRIPTION";
    private boolean mSystemTriggered = false;
    private String TOKEN;
    private String encodingAuth;
    private Button mDemoButton;
    private Button mCloseCollege;
    private Button mEvents;
    private Intent intent;
    private DevicesStatus devicesStatus;
    private DisplayDevices displayDevices;

    // System status variables
    private ImageView mImageViewSystemStatus;
    private TextView mTextViewSystemStatus;

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
        SharedPreferences prefs = getSharedPreferences(EVENT_ID,MODE_PRIVATE);
        //check if there any events on phone
        int amountOfHistoryEvents = prefs.getInt(ID,-1);
        if(amountOfHistoryEvents == -1){
            prefs.edit().putInt(ID,0).apply();
        }
        setContentView(R.layout.activity_dashboard);

        mDemoButton = findViewById(R.id.button_Demo);
        mCloseCollege = findViewById(R.id.button_close_college);
        mEvents = findViewById(R.id.button_events);
        mRecyclerView = findViewById(R.id.recyclerView);

        mImageViewSystemStatus = findViewById(R.id.imageViewSystemStatus);
        mTextViewSystemStatus = findViewById(R.id.textViewSystemStatus);

        //set that main thread can do GET requests
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //get variables from MainActivity
        intent = getIntent();
        TOKEN = intent.getStringExtra("TOKEN");
        String auth = intent.getStringExtra("USER_NAME") + ":" + TOKEN;
        encodingAuth =new String(Base64.encode(auth.getBytes(),Base64.NO_WRAP));
        //set button clicks listener
        mEvents.setOnClickListener(v -> showEvents());

        mCloseCollege.setOnClickListener(closeCollegeClick());

        mDemoButton.setOnClickListener(v -> burglaryAlarm());
    }

    @Override
    public void onResume(){
        super.onResume();
        //refresh the display devices data every 10 seconds
        displayDevices = new DisplayDevices(this::getDevicesStatus);
        new Thread(displayDevices).start();
    }

    private View.OnClickListener closeCollegeClick(){
        return v -> {
            SharedPreferences prefs = getSharedPreferences(CLOSE_COLLEGE,MODE_PRIVATE);
            closeCollege(prefs);
            mCloseCollege.setOnClickListener(openCollegeClick());
            mCloseCollege.setText(R.string.open_College);
        };
    }

    private View.OnClickListener openCollegeClick(){
        return v -> {
            mSystemTriggered = false;

            mImageViewSystemStatus.setImageResource(R.drawable.x_mark_symbopl);
            mTextViewSystemStatus.setText("NOT TRIGGERED");

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

    private void burglaryAlarm() {
        burglaryNotification();
        //take a video snapshot
        //takeVideoSnapshot();
        //take photos snapshots
        //takePhotosSnapshots();

        //save data in phone for event mode
        saveEvents();
    }

    private void saveEvents(){
        SharedPreferences prefs = getSharedPreferences(EVENT_ID,MODE_PRIVATE);
        Integer currentEventId = prefs.getInt(ID,-1);
        prefs.edit().remove(ID).apply();
        prefs.edit().putInt(ID,currentEventId+1).apply();
        prefs = getSharedPreferences(EVENTS+currentEventId,MODE_PRIVATE);
        prefs.edit().putString(DATE,Calendar.getInstance().getTime().toString()).apply();
        //saveImageURL
        //saveVideoURL
    }

    private void burglaryNotification() {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESCRIPTION);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_ac_unit) // notification icon
                .setContentTitle("Burglary") // title for notification
                .setContentText("A burglary happened!")// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), EventsActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        if (mNotificationManager != null) {
            mNotificationManager.notify(0, mBuilder.build());
        }
    }

    private void closeCollege(SharedPreferences prefs){
        Map<DevicesIdsEnum,List<String>> devices = new HashMap();
        setDeviceMapParams(devices,DevicesIdsEnum.MotionSensor,DevicesIdsEnum.WindowContact);
        //save current devices status
        new CloseCollege(devices,encodingAuth,prefs,() -> new Thread(this).start());
        mSystemTriggered = true;

        mImageViewSystemStatus.setImageResource(R.drawable.check_mark_symbol);
        mTextViewSystemStatus.setText("TRIGGERED");

        Toast.makeText(this, "System has been triggered", Toast.LENGTH_SHORT).show();
        //check every 10 seconds if the status was changed and alert it

    }

    private void showDeviceDetails(){
        getDevicesInfo();
        devicesStatus = null;
    }

    private void takePhotosSnapshots() {
        SharedPreferences prefs = getSharedPreferences(EVENTS,MODE_PRIVATE);
        new GetImageSnapshots(getApplicationContext(),DevicesIdsEnum.Camera,encodingAuth,prefs);
    }

    private void takeVideoSnapshot() {
        SharedPreferences prefs = getSharedPreferences(EVENTS,MODE_PRIVATE);
        Map<DevicesIdsEnum,List<String>> devices = new HashMap();
        setDeviceMapParams(devices,DevicesIdsEnum.Camera);
        new StartVideoStreaming(devices,encodingAuth,prefs);
    }

    private void getRecordedVideos() {
        GetRecordedVideos getRecordedVideos = new GetRecordedVideos();
        //save videos on phone
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
        //get current saved devices status
        SharedPreferences prefs = getSharedPreferences(CLOSE_COLLEGE,MODE_PRIVATE);
        Set<String> devicesToFollow = prefs.getStringSet("Devices",null);
        prefs = getSharedPreferences(CLOSE_COLLEGE,MODE_PRIVATE);
        prefs.edit().remove("Devices").apply();
        Map<Integer,String> deviceSavedStatus = new HashMap<>();

        for(String savedDevice : devicesToFollow) {
            String[] splitArray = savedDevice.split(":");
            deviceSavedStatus.put(Integer.parseInt(splitArray[0]), splitArray[1]);
        }
        //get current device status and check if the status was changed
        while (mSystemTriggered){
            for (Map.Entry<Integer, String> entry : deviceSavedStatus.entrySet()) {
                Map<DevicesIdsEnum,List<String>> devices = new HashMap();
                setDeviceMapParams(devices,DevicesIdsEnum.findDeviceIdEnum(entry.getKey()));
                devicesStatus = getDevicesStatusResponse(devices,encodingAuth,() ->{
                    if(devicesStatus != null && devicesStatus.getDevicesResponseSize() == 1){
                        checkBurglary(deviceSavedStatus);
                    }
                });
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void checkBurglary(Map<Integer,String> deviceSavedStatus) {
        //check every 10 seconds if the status changed
        for(DeviceResponse res : devicesStatus.getDevicesResponse()){
            if(deviceSavedStatus.containsKey((int)res.getDeviceId()) && !res.getStatus().equals(deviceSavedStatus.get((int)res.getDeviceId()))){
                burglaryAlarm();
            }
        }
    }

    private DevicesStatus getDevicesStatusResponse(Map<DevicesIdsEnum, List<String>> devices, String encodingAuth, Runnable runWhenFinished) {
        return new DevicesStatus(devices,encodingAuth,runWhenFinished);
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
        final String TYCO_CONTACT = "tyco_contact";
        final String MOVABLE_CAM_SERCOMM = "movable_cam_sercomm";
        final String TYCO_MOTION = "tyco_motion";
        switch (type) {
            case (TYCO_CONTACT):
                mDevicesImages.add(R.drawable.widows_contact_img);
                break;
            case (MOVABLE_CAM_SERCOMM):
                mDevicesImages.add(R.drawable.security_cam_img);
                break;
            case (TYCO_MOTION):
                mDevicesImages.add(R.drawable.motion_sensor_img);
                break;
            default: break;
        }
    }
}
