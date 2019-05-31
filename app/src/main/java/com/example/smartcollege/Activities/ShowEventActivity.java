package com.example.smartcollege.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.smartcollege.Adapters.EventsImageRecyclerViewAdapter;
import com.example.smartcollege.R;

import java.util.ArrayList;

public class ShowEventActivity extends AppCompatActivity {

    private String mEventID;
    private String mEventDate;

    private TextView mTextViewID;
    private TextView mTextViewDate;

    // Video player Variables
    private VideoView mVideoView;
    private Button mButtonPlay;

    // Recycler View variables
    private ArrayList<Integer> mEventsImages = new ArrayList<>();
    private RecyclerView mEventRecyclerView;
    private EventsImageRecyclerViewAdapter mEventsImageRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        Intent intent = getIntent();

        mEventID = intent.getStringExtra("ID");
        mEventDate = intent.getStringExtra("Date");

        mTextViewID = findViewById(R.id.textViewEventID);
        mTextViewDate = findViewById(R.id.textViewEventDate);
        mVideoView = findViewById(R.id.videoViewEvent);
        mButtonPlay = findViewById(R.id.buttonPlayVideo);

        mTextViewID.setText(mEventID);
        mTextViewDate.setText(mEventDate);

        injectTempImageEvents();

        //set burglary video from phone
        mButtonPlay.setOnClickListener(v -> startVideo("http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4"));
    }

    private void injectTempImageEvents() {
        //get set burglary images
        mEventsImages.add(R.drawable.burgler_image);
        mEventsImages.add(R.drawable.burgler_image_2);
        mEventsImages.add(R.drawable.burgler_image_3);
        mEventsImages.add(R.drawable.burgler_image_4);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mEventRecyclerView = findViewById(R.id.recyclerViewEventsImages);
        mEventsImageRecyclerViewAdapter = new EventsImageRecyclerViewAdapter(mEventsImages, this);
        mEventRecyclerView.setAdapter(mEventsImageRecyclerViewAdapter);
        mEventRecyclerView.setLayoutManager(layoutManager);
    }

    private void startVideo(String URL) {
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mediaController);

        String path = "android.resource://" + getPackageName() + "/" + R.raw.burgler_video;
        mVideoView.setVideoURI(Uri.parse(path));
        mVideoView.start();
    }
}
