package com.example.mile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.mile.Route.RouteItem;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class Tracking_Page extends AppCompatActivity {
    public TextView timeTxt;
    public TextView dateTxt;
    public TextView startTxt;
    public TextView destinationTxt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_page);
        Intent intent = getIntent();


        String datetime = intent.getStringExtra("time");
        String start = intent.getStringExtra("start");
        String destination = intent.getStringExtra("destination");


        timeTxt=findViewById(R.id.startTime);
        dateTxt=findViewById(R.id.date);
        startTxt=findViewById(R.id.startPoint);
        destinationTxt=findViewById(R.id.destination);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm");


        try {
            Date date = dateFormat.parse(datetime);
            SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("EEE MMM dd yyyy");
            SimpleDateFormat timeOnlyFormat = new SimpleDateFormat("HH:mm");
            timeTxt.setText(timeOnlyFormat.format(date));
            dateTxt.setText(dateOnlyFormat.format(date));
        } catch (ParseException e) {
            timeTxt.setText(datetime);
            dateTxt.setText(datetime);
            throw new RuntimeException(e);
        }

        // Format the Date object to get date and time components

        startTxt.setText(start);
        destinationTxt.setText(destination);

    }
}