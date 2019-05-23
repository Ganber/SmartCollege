package com.example.smartcollege.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.smartcollege.Adapters.EventsRecyclerViewAdapter;
import com.example.smartcollege.R;

import java.util.ArrayList;

public class EventsActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    // RecyclerView Variables
    private ArrayList<String> mEventsID = new ArrayList<>();
    private ArrayList<String> mEventsDate = new ArrayList<>();
    private RecyclerView mEventRecyclerView;
    private EventsRecyclerViewAdapter mEventsRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        prefs = getSharedPreferences("Events",MODE_PRIVATE);

        // TODO: get events from HTTP request
        injectTempEvents();
    }

    private void injectTempEvents() {

        int eventImage = R.drawable.events_icon;

        mEventsID.add("Event 1");
        mEventsID.add("Event 2");

        mEventsDate.add("22/5/2019 - 14:21");
        mEventsDate.add("23/5/2019 - 18:34");

        mEventRecyclerView = findViewById(R.id.eventsRecyclerView);
        mEventsRecyclerViewAdapter = new EventsRecyclerViewAdapter(mEventsID, mEventsDate, eventImage, this);
        mEventRecyclerView.setAdapter(mEventsRecyclerViewAdapter);
        mEventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
