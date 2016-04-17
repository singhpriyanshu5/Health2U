package com.example.priyanshu.health2u.model.AppLogic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.example.priyanshu.health2u.R;
import com.example.priyanshu.health2u.model.Data.Patient;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by priyanshu on 16/4/2016.
 */
public class BookingMaker {
    private String date,time,clinicName,dob,extra,patientName,selectedDoctor;
    private String[] services = new String[20];

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getdOb() {
        return dob;
    }

    public void setdOb(String dOb) {
        this.dob = dOb;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String[] getServices() {
        return services;
    }

    public void setServices(String[] services) {
        this.services = services;
    }

    public void makeBooking(final String username, String clinicName, final Patient p){
        ParseObject booking = new ParseObject("booking");
        booking.put("patient_name", patientName);
        booking.put("clinic_name", clinicName);
        booking.put("services", services);
        booking.put("dateText", date);
        booking.put("timeText", time);
        //String user_name = ParseUser.getCurrentUser().getString("user_name");
        //if(user_name == null) user_name = "admin";
        booking.put("user_name", username);
        booking.put("attended", false);
        booking.put("extra_comments", extra);
        booking.put("selected_doctor",selectedDoctor);
        booking.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("patient");
                    query.whereEqualTo("name", patientName);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                if (objects.size() == 0) {
                                    ParseObject patient = new ParseObject("patient");
                                    patient.put("name", p.getName());
                                    patient.put("contact", p.getContact());
                                    patient.put("email", p.getEmail());
                                    patient.put("dob", p.getDob());
                                    patient.put("nric", p.getNric());
                                    patient.saveInBackground();
                                } else {
                                    Log.d("PatientDetails", "xxxxxxxxxxxx patient data alredy there");
                                }
                            }
                        }
                    });

                }
            }
        });
            }

    public boolean checkAvailableTimeSlot(String date, final String time){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("booking");
        query.whereEqualTo("selected_doctor", selectedDoctor);
        query.whereEqualTo("clinic_name", clinicName);
        query.whereEqualTo("dateText", date);
        query.whereEqualTo("attended",false);
        query.orderByDescending("updatedAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    //is_clash = false;
                    if (objects.size() > 0) {
                        for (int i = 0; i < objects.size(); i++) {
                            if (objects.get(i).getString("timeText").equals(time)) {

                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                String time_string = "";
                                try {
                                    Date date = sdf.parse(objects.get(0).getString("timeText"));
                                    Calendar time = new GregorianCalendar();
                                    time.setTime(date);
                                    time.add(Calendar.MINUTE, 30);
                                    time_string = String.format("%2d:%2d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE));
                                } catch (java.text.ParseException e1) {
                                    e1.printStackTrace();
                                }
//                                is_clash = true;
                                //open(time_string);
                                break;
                            }
                        }

                    }
                }
            }
        });
        return false;
    }

    public void open(final String time_string, Context c){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
        alertDialogBuilder.setIcon(R.drawable.doctor);
        alertDialogBuilder.setMessage("Book for " + time_string +" instead?");
        alertDialogBuilder.setTitle("Selected  time slot already booked");

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                doPositiveClick(time_string);            }
        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doNegativeClick();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void doPositiveClick(String time_string) {
        Patient p=null;
        // Do stuff here.
        Log.d("FragmentAlertDialog", "Positive click!");
        time = time_string;
        //is_clash = false;
        makeBooking(patientName, clinicName, p);
//        Intent intent = new Intent(PatientDetails.this, DrawerActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("origin", true);
//        startActivity(intent);
//        finish();
    }

    public void doNegativeClick() {
        // is_clash=true;
        // Do stuff here.
        Log.d("FragmentAlertDialog", "Negative click!");
//        Intent i = new Intent(PatientDetails.this, DateSelect.class);
//        startActivity(i);
//        finish();
    }

}
