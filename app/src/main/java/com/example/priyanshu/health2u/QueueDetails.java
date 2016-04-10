package com.example.priyanshu.health2u;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import java.util.Random;

public class QueueDetails extends AppCompatActivity {
    private String objectId;
    private TextView clinic_name_tv,patient_name_tv,your_queue_tv,current_queue_tv,estimated_time_tv;
    private FrameLayout frame_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_details);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        frame_main = (FrameLayout)findViewById(R.id.frame_main);
        patient_name_tv = (TextView)findViewById(R.id.patient_name_tv);
        your_queue_tv = (TextView)findViewById(R.id.your_queue_tv);
        current_queue_tv = (TextView)findViewById(R.id.current_queue_tv);
        estimated_time_tv = (TextView)findViewById(R.id.estimated_time_tv);
        clinic_name_tv = (TextView)findViewById(R.id.clinic_name_tv);
        Intent i = getIntent();
        objectId = i.getStringExtra("objectId");
        Log.d("QueueDetails","xxxxxxx"+objectId);
        final int[] random_estimated_time ={10,20,30,40,50};

        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        final int randomNum = rand.nextInt(6) + 0;

        ParseQuery<ParseObject> q = ParseQuery.getQuery("queue");
        q.whereEqualTo("objectId",objectId);
        q.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){
                        final String clinic_name = objects.get(0).getString("clinic_name");
                        final int your_queue = objects.get(0).getInt("queue_number");
                        final String patient_name = objects.get(0).getString("patient_name");

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("usernameToClinic");
                        query.whereEqualTo("clinic_name", clinic_name);
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    if (objects.size() > 0) {
                                        int current_queue = objects.get(0).getInt("current_queue");
                                        int estimated_wait = (your_queue-current_queue)*15;
                                        clinic_name_tv.setText(clinic_name);
                                        your_queue_tv.setText(String.valueOf(your_queue));
                                        current_queue_tv.setText(String.valueOf(current_queue));
                                        estimated_time_tv.setText(String.valueOf(estimated_wait) + "mins");
                                        patient_name_tv.setText(patient_name);
                                        frame_main.setVisibility(View.VISIBLE);

                                    } else {
                                        Log.d("QueueDetails", "no such clinic");
                                    }

                                }
                            }
                        });



                    }else{
                        Log.d("QueueDetails", "no queue data found for this objectId");
                    }
                }
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
