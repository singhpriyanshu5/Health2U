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
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.example.priyanshu.health2u.R;
import com.example.priyanshu.health2u.controller.PatientDetails;

import java.util.ArrayList;

public class SelectServices extends AppCompatActivity {

    public LinearLayout linearList ;
    CheckBox checkBox;
    ArrayList<String> finalArray = new ArrayList<>();
    String[] arr = new String[20];
    private  String title;
    private String dateText;
    private String timeText;

    String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Select Medical Services");
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        title = intent.getStringExtra("clinic_name");
        dateText = intent.getStringExtra("date");
        timeText = intent.getStringExtra("time");
        user_name = intent.getStringExtra("user_name");
        setContentView(R.layout.activity_select_services);
        linearList = (LinearLayout) findViewById(R.id.linear_list);
        ArrayList<String> arr = new ArrayList<>();
        arr.add("Aesthetics");

        arr.add("Anesthesiology");

        arr.add("Bariatric Surgery");

        arr.add("Bone Marrow Transplant");

        arr.add("Breast Surgery");

        arr.add("Cardiology");

        arr.add("Cardiothoracic Surgery");

        arr.add("Chinese Medicine");


        for(int i=0; i<arr.size(); i++)
        {
            checkBox=new CheckBox(this);
            checkBox.setId(i);
            checkBox.setText(arr.get(i));
            checkBox.setTextSize(20);
            checkBox.setPadding(0,0,0,10);
            checkBox.setOnClickListener(getOnClickDo(checkBox));
            linearList.addView(checkBox);
        }


    }
    View.OnClickListener getOnClickDo(final Button b)
    {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(finalArray.contains(b.getText().toString()))
                    finalArray.remove(b.getText().toString());
                else
                    finalArray.add(b.getText().toString());
                Log.v("array", finalArray.toString());
            }
        };
    }

    public void openPatientDetails(View view) {
        arr = finalArray.toArray(arr);
        Intent intent = new Intent(this, PatientDetails.class);
        intent.putExtra("clinic_name",title);
        intent.putExtra("date",dateText);
        intent.putExtra("time",timeText);
        intent.putExtra("services",finalArray);
        intent.putExtra("user_name", user_name);
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

