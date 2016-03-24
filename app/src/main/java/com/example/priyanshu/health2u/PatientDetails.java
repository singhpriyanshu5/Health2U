package com.example.priyanshu.health2u;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class PatientDetails extends AppCompatActivity {

    private String title;
    private String dateText;
    private String timeText;
    String[] arr = new String[20];
    ArrayList<String> services = new ArrayList<>();
    private EditText name_et,last_name_et, contact_et, email_et, dob_day_et, dob_month_et, dob_year_et;
    private EditText nric_et, extra_et;
    String dob,fullname,name,nric,last_name,contact,email,dob_day,dob_month,dob_year,extra;
    private TextView tv_time1,tv_date1;
    private FrameLayout initial_frame, load_details, enter_details;
    private TextView name_tv, nric_tv, contact_tv, email_tv, dob_tv, extra_tv;
    private EditText full_name_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Patient Details");
        initial_frame = (FrameLayout)findViewById(R.id.initial_frame);
        load_details = (FrameLayout)findViewById(R.id.load_details);
        enter_details = (FrameLayout)findViewById(R.id.enter_details);

        initial_frame.setVisibility(View.VISIBLE);

        full_name_et = (EditText)findViewById(R.id.full_name_et);
        Intent intent = getIntent();
        title= intent.getStringExtra("clinic_name");
        dateText = intent.getStringExtra("date");
        timeText = intent.getStringExtra("time");
        services = intent.getStringArrayListExtra("services");
        name_et = (EditText)findViewById(R.id.name_et);
        nric_et = (EditText)findViewById(R.id.nric_et);
        last_name_et = (EditText) findViewById(R.id.last_name_et);
        contact_et = (EditText) findViewById(R.id.contact_et);
        email_et = (EditText) findViewById(R.id.email_et);
        dob_day_et = (EditText) findViewById(R.id.dob_day_et);
        dob_month_et = (EditText) findViewById(R.id.dob_month_et);
        dob_year_et = (EditText) findViewById(R.id.dob_year_et);
        tv_time1 = (TextView) findViewById(R.id.tv_time);
        tv_date1 = (TextView) findViewById(R.id.tv_date);
        tv_time1.setText(timeText);
        tv_date1.setText(dateText);
        TextView tv1 = (TextView) findViewById(R.id.tv1);
        tv1.setText(title);

        name_tv = (TextView)findViewById(R.id.name_tv);
        nric_tv = (TextView)findViewById(R.id.nric_tv);
        contact_tv = (TextView)findViewById(R.id.contact_tv);
        email_tv = (TextView)findViewById(R.id.email_tv);
        dob_tv = (TextView)findViewById(R.id.dob_tv);
        extra_tv = (TextView)findViewById(R.id.extra_tv);
    }

    public void SubmitDetails(View view) {
        if(enter_details.getVisibility() == View.VISIBLE) {
            name = name_et.getText().toString();
            nric = nric_et.getText().toString();
            last_name = last_name_et.getText().toString();
            fullname = name + " " + last_name;
            contact = contact_et.getText().toString();
            email = email_et.getText().toString();
            dob_day = dob_day_et.getText().toString();
            dob_month = dob_month_et.getText().toString();
            dob_year = dob_year_et.getText().toString();
            dob = dob_day + "/" + dob_month + "/" + dob_year;
        }else if(load_details.getVisibility() == View.VISIBLE){
            fullname = name;
        }
        extra = extra_et.getText().toString();
        ParseObject booking = new ParseObject("booking");
        booking.put("patient_name", fullname);
        booking.put("clinic_name", title);
        booking.put("services", services);
        booking.put("dateText", dateText);
        booking.put("timeText", timeText);
        String user_name = ParseUser.getCurrentUser().getString("user_name");
        booking.put("user_name", user_name);
        booking.put("attended", false);
        booking.put("extra_comments", extra);
        booking.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("patient");
                    query.whereEqualTo("name", fullname);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                if (objects.size() == 0) {
                                    ParseObject patient = new ParseObject("patient");
                                    patient.put("name", fullname);
                                    patient.put("contact", contact);
                                    patient.put("email", email);
                                    patient.put("dob", dob);
                                    patient.put("nric", nric);
                                    patient.saveInBackground();
                                } else {
                                    Log.d("PatientDetails", "xxxxxxxxxxxx patient data alredy there");
                                }
                            }
                        }
                    });

                    Intent intent = new Intent(PatientDetails.this, DrawerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("origin", true);
                    startActivity(intent);
                    finish();

                } else {
                    Log.d("PatientDetails", "some error in booking savecallback");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if(id==android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void enterDetails(View view) {
        extra_et = (EditText) findViewById(R.id.extra_et_enter);
        initial_frame.setVisibility(View.GONE);
        enter_details.setVisibility(View.VISIBLE);

    }

    public void loadDetails(View view) {
        extra_et = (EditText) findViewById(R.id.extra_et_load);

        name= full_name_et.getText().toString();
        name_tv.setText(name);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("patient");
        query.whereEqualTo("name", name);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        nric = objects.get(0).getString("nric");
                        nric_tv.setText(nric);
                        contact = objects.get(0).getString("contact");
                        contact_tv.setText(contact);
                        email = objects.get(0).getString("email");
                        email_tv.setText(email);
                        dob = objects.get(0).getString("dob");
                        dob_tv.setText(dob);
                        initial_frame.setVisibility(View.GONE);
                        load_details.setVisibility(View.VISIBLE);
                    } else {
                        Log.d("BookingDetails", "xxxxxxxxxx no patient data found");
                    }
                }
            }
        });

    }
}
