package com.example.priyanshu.health2u;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ViewBooking extends AppCompatActivity {
    private String user_name;
    private String name;
    private ArrayList<String> services = new ArrayList<String>();
    private String clinic_name0,clinic_name1,clinic_name2,clinic_name3;
    private String date0,date1,date2,date3;
    private String time0,time1,time2,time3;
    private TextView clinic0,clinic1,clinic2,clinic3;
    private TextView d0,d1,d2,d3;
    private TextView t0,t1,t2,t3;
    private LinearLayout l0,l1,l2,l3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking);
        Intent intent =getIntent();
        user_name = intent.getStringExtra("user_name");
        clinic0 = (TextView) findViewById(R.id.clinic_current);
        clinic1 = (TextView) findViewById(R.id.clinic_1);
        clinic2 = (TextView) findViewById(R.id.clinic2);
        clinic3 = (TextView) findViewById(R.id.clinic3);
        d0= (TextView) findViewById(R.id.tv_current_date);
        d1= (TextView) findViewById(R.id.tv_date_1);
        d2= (TextView) findViewById(R.id.tv_date_2);
        d3= (TextView) findViewById(R.id.tv_date_3);
        t0= (TextView) findViewById(R.id.tv_current_time);
        t1= (TextView) findViewById(R.id.tv_time_1);
        t2= (TextView) findViewById(R.id.tv_time_2);
        t3= (TextView) findViewById(R.id.tv_time_3);
        l0= (LinearLayout) findViewById(R.id.l0);
        l1= (LinearLayout) findViewById(R.id.l1);
        l2= (LinearLayout) findViewById(R.id.l2);
        l3= (LinearLayout) findViewById(R.id.l3);
//        String user_name = ParseUser.getCurrentUser().getString("user_name");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("booking");
        query.whereEqualTo("user_name", user_name);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> bookingList, ParseException e) {
                if (e == null) {
                    Log.d("bookings", "Retrieved " + bookingList.size() + " booking " + bookingList.get(0).getString("nric"));
                    if(bookingList!=null) {
                        clinic_name0 = bookingList.get(0).getString("clinic_name");
                        date0 = bookingList.get(0).getString("dateText");
                        time0 = bookingList.get(0).getString("timeText");
                        clinic0.setText(clinic_name0);
                        d0.setText(date0);
                        t0.setText(time0);
                        l0.setVisibility(View.VISIBLE);
                    }
                    if(bookingList.size()>1) {
                        clinic_name1 = bookingList.get(1).getString("clinic_name");
                        date1 = bookingList.get(1).getString("dateText");
                        time1 = bookingList.get(1).getString("timeText");
                        clinic1.setText(clinic_name1);
                        d1.setText(date1);
                        t1.setText(time1);
                        l1.setVisibility(View.VISIBLE);
                    }
                    if(bookingList.size()>2) {

                        clinic_name2 = bookingList.get(2).getString("clinic_name");
                        date2 = bookingList.get(2).getString("dateText");
                        time2 = bookingList.get(2).getString("timeText");
                        clinic2.setText(clinic_name2);
                        d2.setText(date2);
                        t2.setText(time2);
                        l2.setVisibility(View.VISIBLE);
                    }
                    if(bookingList.size()>3) {

                        clinic_name3 = bookingList.get(3).getString("clinic_name");
                        date3 = bookingList.get(3).getString("dateText");
                        time3 = bookingList.get(3).getString("timeText");
                        clinic3.setText(clinic_name3);
                        d3.setText(date3);
                        t3.setText(time3);
                        l3.setVisibility(View.VISIBLE);
                    }

                } else {
                    Log.d("booking", "Error: " + e.getMessage());
                }
            }
        });
    }
}
