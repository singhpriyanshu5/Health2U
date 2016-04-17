package com.example.priyanshu.health2u.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.priyanshu.health2u.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class BookingDetails extends AppCompatActivity {

    private TextView clinic_name_tv, time_text_tv, date_text_tv, name_tv, nric_tv, contact_tv, email_tv, dob_tv, extra_tv, doctor_tv;
    private Button attended_btn;
    private  String objectId,clinic_name, time_text, date_text, name, nric, contact, email, dob, extra, doctor;
    private boolean isAdmin=false;
    private FrameLayout frame_booking;
    RelativeLayout loadingPanel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        loadingPanel = (RelativeLayout)findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.VISIBLE);
        frame_booking = (FrameLayout)findViewById(R.id.frame_booking);
        clinic_name_tv = (TextView)findViewById(R.id.clinic_name_tv);
        time_text_tv = (TextView)findViewById(R.id.time_tv);
        date_text_tv = (TextView)findViewById(R.id.date_tv);
        name_tv = (TextView)findViewById(R.id.name_tv);
        nric_tv = (TextView)findViewById(R.id.nric_tv);
        contact_tv = (TextView)findViewById(R.id.contact_tv);
        email_tv = (TextView)findViewById(R.id.email_tv);
        dob_tv = (TextView)findViewById(R.id.dob_tv);
        extra_tv = (TextView)findViewById(R.id.extra_tv);
        doctor_tv = (TextView)findViewById(R.id.doctor_tv);
        attended_btn = (Button)findViewById(R.id.attended_btn);

        Intent i = getIntent();
//        clinic_name = i.getStringExtra("clinic_name");
//        time_text = i.getStringExtra("time_text");
//        date_text = i.getStringExtra("date_text");
//        name = i.getStringExtra("patient_name");
        objectId = i.getStringExtra("objectId");
        Log.d("BookingDetails", "xxxxxxx" + objectId);
        ParseQuery<ParseObject> q = ParseQuery.getQuery("booking");
        q.whereEqualTo("objectId", objectId);
        q.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {

                        clinic_name = objects.get(0).getString("clinic_name");
                        time_text = objects.get(0).getString("timeText");
                        date_text = objects.get(0).getString("dateText");
                        name = objects.get(0).getString("patient_name");
                        extra = objects.get(0).getString("extra_comments");
                        doctor = objects.get(0).getString("selected_doctor");
                        clinic_name_tv.setText(clinic_name);
                        time_text_tv.setText(time_text);
                        date_text_tv.setText(date_text);
                        name_tv.setText(name);
                        extra_tv.setText(extra);
                        doctor_tv.setText(doctor);
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("patient");
                        query.whereEqualTo("name", name);
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    if (objects.size() > 0) {
                                        loadingPanel.setVisibility(View.GONE);
                                        nric = objects.get(0).getString("nric");
                                        nric_tv.setText(nric);
                                        contact = objects.get(0).getString("contact");
                                        contact_tv.setText(contact);
                                        email = objects.get(0).getString("email");
                                        email_tv.setText(email);
                                        dob = objects.get(0).getString("dob");
                                        dob_tv.setText(dob);
                                        frame_booking.setVisibility(View.VISIBLE);
                                    } else {
                                        Log.d("BookingDetails", "xxxxxxxxxx no patient data found");
                                    }
                                }
                            }
                        });

                        Log.d("BookingDetails", "xxxxxxxxxxxx" + clinic_name);
                    } else {
                        Log.d("BookingDetails", "xxxxxxxxxx no booking for this patient data found");
                    }
                }
            }
        });
        isAdmin = i.getBooleanExtra("isAdmin", false);
        if(isAdmin){
            attended_btn.setVisibility(View.VISIBLE);
        }





        attended_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query3 = ParseQuery.getQuery("booking");
                query3.whereEqualTo("objectId",objectId);
                query3.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(final List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            if (objects.size() > 0) {
                                objects.get(0).put("attended", true);
                                objects.get(0).saveInBackground();
                                attended_btn.setText("Attended");
                                attended_btn.setEnabled(false);

                            } else {
                                Log.d("BookingDetails", "xxxxxxxxxx no booking for this patient data found");
                            }
                        }
                    }
                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
