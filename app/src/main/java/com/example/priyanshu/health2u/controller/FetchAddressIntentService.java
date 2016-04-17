package com.example.priyanshu.health2u.controller;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class FetchAddressIntentService extends IntentService {

    protected ResultReceiver mReceiver;
    String clinic_name;
    String est_time;


    public FetchAddressIntentService() {
        super("worker1");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Geocoder geocoder;
        mReceiver = intent.getParcelableExtra("RECEIVER");
        clinic_name = intent.getStringExtra("clinic_name");
        List<Address> yourAddresses=null;
        Location location = intent.getParcelableExtra("loc_data");
        Log.d("FetchService", clinic_name);
        ParseQuery<ParseObject> q = ParseQuery.getQuery("usernameToClinic");
        q.whereEqualTo("clinic_name", clinic_name);
        q.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){
                        int est_int = 15*(objects.get(0).getInt("latest_queue") - objects.get(0).getInt("current_queue") + 1);
                        est_time = String.valueOf(est_int);
                        Log.d("FetchService",est_time);
                    }else{
                        Log.d("FetchService", "xxxx" + est_time);
                    }
                }else{
                    Log.d("FetchService", e.toString());
                }
            }
        });

        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            yourAddresses= geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

        } catch (IOException e) {
            Log.d("FetchAddress", e.toString());
        }
        if(yourAddresses==null || yourAddresses.size() == 0)
        deliverResult(0,"errrrooorrrr");
        else{
            String yourAddress = yourAddresses.get(0).getAddressLine(0);
            String yourCity = yourAddresses.get(0).getAddressLine(1);
            //String yourCountry = yourAddresses.get(0).getAddressLine(2);
            deliverResult(1, yourAddress + "," + yourCity);
        }





    }

    private void deliverResult(int resultCode, String message) {
        if(clinic_name.equals("Healthmark Pioneer Mall Clinic")) {
            est_time = "40";
        }else if(clinic_name.equals("Prohealth Medical Group @ Jurong West Pte Ltd")) {
            est_time = "35";
        }
        else if(clinic_name.equals("FOO CLINIC")){
            est_time = "30";
        }else{
            est_time = "50";
        }


        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        bundle.putString("est_time",est_time);
        mReceiver.send(resultCode, bundle);
    }
}
