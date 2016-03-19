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
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

public class PatientDetails extends AppCompatActivity {

    private String title;
    private String dateText;
    private String timeText;
    String[] arr = new String[20];
    ArrayList<String> services = new ArrayList<>();
    private EditText name_et;
    private EditText nric_et;
    String name,nric;
    private TextView tv_time1,tv_date1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Patient Details");
        Intent intent = getIntent();
        title= intent.getStringExtra("clinic_name");
        dateText = intent.getStringExtra("date");
        timeText = intent.getStringExtra("time");
        services = intent.getStringArrayListExtra("services");
        name_et = (EditText)findViewById(R.id.name_et);
        nric_et = (EditText)findViewById(R.id.nric_et);
        tv_time1 = (TextView) findViewById(R.id.tv_time);
        tv_date1 = (TextView) findViewById(R.id.tv_date);
        tv_time1.setText(timeText);
        tv_date1.setText(dateText);
        TextView tv1 = (TextView) findViewById(R.id.tv1);
        tv1.setText(title);
    }

    public void SubmitDetails(View view) {
        name = name_et.getText().toString();
        nric = nric_et.getText().toString();
        Log.v("patient", name);
        Log.v("patient", nric);
        ParseObject booking = new ParseObject("booking");
        booking.put("patient_name",name);
        booking.put("clinic_name",title);
        booking.put("services",services);
        booking.put("dateText", dateText);
        booking.put("timeText", timeText);
        String user_name = ParseUser.getCurrentUser().getString("name");
        Log.v("PatintDetails",user_name);
        booking.put("user_name", user_name);
        Log.v("arr", arr.toString());
        booking.put("nric", nric);
        booking.put("attended",false);
        booking.saveInBackground();
        Intent intent = new Intent(this, DrawerActivity.class);
        //intent.putExtra("user_name", user_name);
        intent.putExtra("origin",true);
        startActivity(intent);
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
}
