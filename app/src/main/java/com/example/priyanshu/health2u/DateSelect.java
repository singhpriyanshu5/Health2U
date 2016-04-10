package com.example.priyanshu.health2u;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateSelect extends AppCompatActivity implements com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener,
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener{

    final Calendar myCalendar = Calendar.getInstance();
//    DatePickerDialog.OnDateSetListener date = null;
//    TimePickerDialog.OnTimeSetListener t= null;
    private String title;
    private SimpleDateFormat sdfDate;
    private SimpleDateFormat sdfTime;
    private DatePickerDialog dpd=null;
    TimePickerDialog tpd =null;
    String user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_select);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Booking Details");
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        title = intent.getStringExtra("clinic_name");
        user_name = intent.getStringExtra("user_name");
        Calendar now = Calendar.getInstance();
        dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        Calendar[] cals = new Calendar[5];
        //SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
        Calendar startDate = Calendar.getInstance();
        cals[0]=startDate;
        Calendar secondDate = (Calendar) startDate.clone();
        secondDate.add(Calendar.DATE, 1);
        cals[1]=secondDate;
        Calendar thirdDate = (Calendar) startDate.clone();
        thirdDate.add(Calendar.DATE, 2);
        cals[2]=thirdDate;
        Calendar fourthDate = (Calendar) startDate.clone();
        fourthDate.add(Calendar.DATE, 3);
        cals[3] = fourthDate;
        Calendar fifthDate = (Calendar) startDate.clone();
        fifthDate.add(Calendar.DATE, 4);
        cals[4] = fifthDate;
        dpd.setSelectableDays(cals);
        tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
//         date = new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                TextView dateText = (TextView) findViewById(R.id.date_text);
//                String myFormat = "dd/MM/yy"; //In which you need put here
//                sdfDate = new SimpleDateFormat(myFormat, Locale.US);
//                dateText.setText(sdfDate.format(myCalendar.getTime()));
//            }
//
//        };
//
//        t =new TimePickerDialog.OnTimeSetListener() {
//            public void onTimeSet(TimePicker view, int hourOfDay,
//                                  int minute) {
//                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                myCalendar.set(Calendar.MINUTE, minute);
//                TextView timeText = (TextView) findViewById(R.id.time_text);
//                String myFormat = "HH:mm";
//                sdfTime = new SimpleDateFormat(myFormat, Locale.US);
//                timeText.setText(sdfTime.format(myCalendar.getTime()));
//            }
//        };


    }

    public void openDatePicker(View view) {
//        new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        dpd.show(getFragmentManager(), "Datepickerdialog");

    }

    public void openTimePicker(View view) {
//        new TimePickerDialog(this,
//                t,
//                myCalendar.get(Calendar.HOUR_OF_DAY),
//                myCalendar.get(Calendar.MINUTE),
//                true).show();
        tpd.show(getFragmentManager(), "Timepickerdialog");


    }



    public void openSelectServices(View view) {
        Intent intent = new Intent(this, SelectServices.class);
        intent.putExtra("clinic_name",title);
        intent.putExtra("date",sdfDate.format(myCalendar.getTime()));
        intent.putExtra("time",sdfTime.format(myCalendar.getTime()));
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

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        TextView dateText = (TextView) findViewById(R.id.date_text);
        String myFormat = "dd/MM/yy"; //In which you need put here
        sdfDate = new SimpleDateFormat(myFormat, Locale.US);
        dateText.setText(sdfDate.format(myCalendar.getTime()));
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

        myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        myCalendar.set(Calendar.MINUTE, minute);
        TextView timeText = (TextView) findViewById(R.id.time_text);
        String myFormat = "HH:mm";
        sdfTime = new SimpleDateFormat(myFormat, Locale.US);
        timeText.setText(sdfTime.format(myCalendar.getTime()));
    }
}
