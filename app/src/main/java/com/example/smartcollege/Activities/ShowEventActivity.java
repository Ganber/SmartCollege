package com.example.smartcollege.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.smartcollege.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShowEventActivity extends AppCompatActivity {

    private String mEventID;
    private String mEventDate;

    private TextView mTextViewID;
    private TextView mTextViewDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        Intent intent = getIntent();
        mEventID = intent.getStringExtra("ID");
        mEventDate = intent.getStringExtra("Date");

        mTextViewID = findViewById(R.id.textViewEventID);
        mTextViewDate = findViewById(R.id.textViewEventDate);

        mTextViewID.setText(mEventID);
        mTextViewDate.setText(mEventDate);
    }
}
