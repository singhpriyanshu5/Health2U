package com.example.priyanshu.health2u;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import java.util.Random;

public class QueueActivity extends AppCompatActivity {

    private String clinic_name;
    private int latest_queue,your_queue,current_queue,estimated_time;
    private TextView clinic_name_tv,your_queue_tv,current_queue_tv,estimated_time_tv;
    private EditText name_et,nric_et;
    private Button get_queue_no;
    private String patient_name,user_name,nric;
    private FrameLayout queue_frame,frame_initial;
    RelativeLayout loadingPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Queue Status");
        setContentView(R.layout.activity_queue);
        loadingPanel = (RelativeLayout)findViewById(R.id.loadingPanel);

        final int[] random_estimated_time ={10,20,30,40,50};

        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        final int randomNum = rand.nextInt(6) + 0;

        nric_et = (EditText)findViewById(R.id.nric_et);
        your_queue_tv = (TextView)findViewById(R.id.your_queue_tv);
        current_queue_tv = (TextView)findViewById(R.id.current_queue_tv);
        estimated_time_tv = (TextView)findViewById(R.id.estimated_time_tv);
        name_et = (EditText)findViewById(R.id.name_et);
        get_queue_no = (Button)findViewById(R.id.get_queue_no);
        queue_frame = (FrameLayout)findViewById(R.id.queue_frame);
        frame_initial = (FrameLayout)findViewById(R.id.frame_initial);
        clinic_name = getIntent().getStringExtra("clinic_name");
        Log.d("QueueActivity", "clinic name: " + clinic_name);
        get_queue_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPanel.setVisibility(View.VISIBLE);
                Log.d("QueueActivity", "vvvvvvvvvvvvvv inside stuff");
                patient_name = name_et.getText().toString();
                nric = nric_et.getText().toString();
                ParseQuery<ParseObject> query = ParseQuery.getQuery("usernameToClinic");
                query.whereEqualTo("clinic_name", clinic_name);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            if (objects.size() > 0) {
                                loadingPanel.setVisibility(View.GONE);
                                user_name = getIntent().getStringExtra("user_name");
                                current_queue = objects.get(0).getInt("current_queue");
                                latest_queue = objects.get(0).getInt("latest_queue");
                                if(current_queue>0){
                                    your_queue = latest_queue + 1;
                                    latest_queue = your_queue;
                                }else{
                                    current_queue = 1;
                                    your_queue = 1;
                                    latest_queue = 1;
                                }
                                objects.get(0).put("current_queue",current_queue);
                                objects.get(0).put("latest_queue",latest_queue);
                                objects.get(0).saveInBackground();
                                int est_time = (your_queue-current_queue)*15;

                                //your_queue = latest_queue + 50;
                                your_queue_tv.setText(String.valueOf(your_queue));
                                //current_queue = latest_queue + 20;
                                current_queue_tv.setText(String.valueOf(current_queue));
                                estimated_time_tv.setText(String.valueOf(est_time));
                                clinic_name_tv = (TextView) findViewById(R.id.clinic_name_tv);
                                clinic_name_tv.setText(clinic_name);
                                frame_initial.setVisibility(View.GONE);
                                queue_frame.setVisibility(View.VISIBLE);
                                Log.d("QueueActivity", "hhhhhhhhh" + String.valueOf(queue_frame.getVisibility()));
                                ParseObject po = new ParseObject("queue");
                                po.put("user_name", user_name);
                                po.put("patient_name", patient_name);
                                po.put("clinic_name", clinic_name);
                                po.put("queue_number", your_queue);
                                po.put("nric", nric);
                                po.saveInBackground();

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
            Intent i = new Intent(QueueActivity.this, UserNavigation.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
