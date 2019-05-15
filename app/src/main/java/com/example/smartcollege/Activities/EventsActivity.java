package com.example.smartcollege.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.smartcollege.R;

public class EventsActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        prefs = getSharedPreferences("Events",MODE_PRIVATE);

        //get events from phone

        //display events on screen

    }
}
