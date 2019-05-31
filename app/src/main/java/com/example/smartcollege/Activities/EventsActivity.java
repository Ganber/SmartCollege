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

    private final String EVENTS = "Events";
    private final String EVENT_ID = "Event ID";
    private final String ID = "ID";
    private final String DATE = "DATE";
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
        injectTempEvents();
    }

    private void injectTempEvents() {
        //get all burglaries events from phone
        prefs = getSharedPreferences(EVENT_ID,MODE_PRIVATE);
        int eventImage = R.drawable.events_icon;
        Integer numberOfEvents = prefs.getInt(ID,-1);
        for(int i = 0; i<numberOfEvents;++i){
            prefs = getSharedPreferences(EVENTS + i,MODE_PRIVATE);
            mEventsID.add("Event: " + (i+1));
            String date = prefs.getString(DATE,null);
            mEventsDate.add(date);
            // get imageURL from prefs
            // get videoURL from prefs
        }

        //display burglaries events
        mEventRecyclerView = findViewById(R.id.eventsRecyclerView);
        mEventsRecyclerViewAdapter = new EventsRecyclerViewAdapter(mEventsID, mEventsDate, eventImage, this);
        mEventRecyclerView.setAdapter(mEventsRecyclerViewAdapter);
        mEventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
