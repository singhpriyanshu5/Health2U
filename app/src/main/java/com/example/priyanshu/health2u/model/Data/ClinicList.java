package com.example.priyanshu.health2u.model.Data;

import android.content.Context;
import android.location.Location;

import com.example.priyanshu.health2u.model.AppLogic.localDataInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by priyanshu on 16/4/2016.
 */
public class ClinicList implements localDataInterface {



    private ArrayList<Clinic> cList = new ArrayList<Clinic>();



    public ClinicList(Context c){
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(c));
            JSONArray features_arr = obj.getJSONArray("features");
//            ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
//            HashMap<String, String> m_li;

            for (int i = 0; i < features_arr.length(); i++) {
                JSONObject j_obj = features_arr.getJSONObject(i);
                JSONObject geometry_obj = j_obj.getJSONObject("geometry");
                JSONArray coordinates_arr = geometry_obj.getJSONArray("coordinates");
                JSONObject properties_obj = j_obj.getJSONObject("properties");
                String name = properties_obj.getString("name");
                double arr[] = new double[2];
                arr[0] = coordinates_arr.getDouble(1);
                arr[1] = coordinates_arr.getDouble(0);

                Location newLocation = new Location("newLocation");
                newLocation.setLatitude(arr[0]);
                newLocation.setLongitude(arr[1]);

//                float dist = newLocation.distanceTo(mLastLocation)/1000;
//                if(dist<5) {
//
//                    map.setOnMarkerClickListener(this);
//
//                    map.addMarker(new MarkerOptions()
//                            .position(new LatLng(arr[0], arr[1]))
//                            .title(name));
//                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String loadJSONFromAsset(Context c) {
        String json = null;
        InputStream is = null;
        try {

            is = c.getAssets().open("clinicsjson.json");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }



    @Override
    public localDataInterface get(String key) {
        return null;
    }

    @Override
    public void put(String key, localDataInterface value) {

    }
}
