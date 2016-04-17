package com.example.priyanshu.health2u.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.priyanshu.health2u.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class UserNavigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {

    private static boolean isPharmacies=false;
    private String[] mTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    CharSequence mTitle;
    CharSequence mDrawerTitle;
    Toolbar toolbar;
    private android.support.v4.app.ActionBarDrawerToggle mDrawerToggle;
    private Location mLastLocation;
    private double user_lat, user_long;
    private boolean isOrigiin = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private RelativeLayout loadingPanel;
    private GoogleApiClient mGoogleApiClient;
    private GoogleApiClient client;
    private LocationRequest mLocationRequest;
    private TextFragment tf;
    private FrameLayout text_part;

    private String current_user_name = null;

    private FloatingActionButton fab =null;

    private AddressResultReceiver mResultReceiver = new AddressResultReceiver(null);

    String selected_clinic = "Raffles Clinic";

    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            final String AddressOutput = resultData.getString("message");
            final String est_time = resultData.getString("est_time");
            Log.d("asssss",AddressOutput);

            Log.d("GeoCoder", "address is : " + AddressOutput);

//            UpdateUI ui = new UpdateUI(AddressOutput);
//            ui.run();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    displayTextFragment(est_time,AddressOutput);
//stuff that updates ui

                }
            });


        }
    }






    private void displayTextFragment(String est_time,String AddressOutput){
        tf.setTextTv(est_time,selected_clinic,AddressOutput," ",isPharmacies,current_user_name);
        if(text_part.getVisibility()== View.GONE) {
            text_part.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_navigation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadingPanel = (RelativeLayout)findViewById(R.id.loadingPanel);

        Intent intent = getIntent();

        if(ParseUser.getCurrentUser()!=null) {
            current_user_name = ParseUser.getCurrentUser().getString("name");
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserNavigation.this, ClinicSearch.class);
                i.putExtra("user_name",current_user_name);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View nav_header = navigationView.inflateHeaderView(R.layout.nav_header_user_navigation);
        TextView tv_user_name = (TextView)nav_header.findViewById(R.id.nav_user_name);
        tv_user_name.setText(current_user_name);
        navigationView.setNavigationItemSelectedListener(this);

        isOrigiin = intent.getBooleanExtra("origin", false);
        if (isOrigiin == true) {
            //selectItem(1);
            toolbar.setTitle("Bookings list");
            Log.d("UserNavigation", toolbar.getTitle().toString());
            Fragment fragment = new BookingFragment();
            Bundle bundle = new Bundle();
            bundle.putString("user_name", current_user_name);
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
            linearLayout.setVisibility(View.GONE);
            FrameLayout fl = (FrameLayout) findViewById(R.id.content_frame);
            fl.setVisibility(View.VISIBLE);
            Log.d("DrawerActivity", "origin is true");

            fab.hide();
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

//        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
//        installation.put("missed_queue",true);
//        installation.saveInBackground();

//        OneSignal.sendTag("user_name", current_user_name);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
//            loadingPanel.setVisibility(View.VISIBLE);
//            ProgressDialog dialog = ProgressDialog.show(UserNavigation.this, "",
//                    "Logging out...", true);
            ParseUser.logOut();
            //finish();
//            Parse.initialize(this);
            //        ParseFacebookUtils.initialize(this);
//            ParseLoginBuilder builder = new ParseLoginBuilder(this);
//            startActivityForResult(builder.build(), 0);
//            finish();
            Intent i = new Intent(UserNavigation.this, SplashActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Drawer Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.priyanshu.health2u/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Drawer Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.priyanshu.health2u/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(user_lat, user_long), 14.0f));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);

        try {
            Log.v("xxx", String.valueOf(user_lat));
            JSONObject obj = new JSONObject(loadJSONFromAsset());
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

                float dist = newLocation.distanceTo(mLastLocation)/1000;
                if(dist<5) {

                    map.setOnMarkerClickListener(this);

                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(arr[0], arr[1]))
                            .title(name));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String loadJSONFromAsset() {
        String json = null;
        InputStream is = null;
        try {
            if(!isPharmacies) {
                is = this.getAssets().open("clinicsjson.json");
            }else {
                is = this.getAssets().open("pharmacyjson.json");
            }
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
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            user_lat = mLastLocation.getLatitude();
            user_long = mLastLocation.getLongitude();
            if(!isOrigiin)
                setMapFragment();


        }else{
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation=location;
        setMapFragment();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    protected void startIntentService(Location loc_data, String clinic_name) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra("RECEIVER", mResultReceiver);
        intent.putExtra("loc_data", loc_data);
        intent.putExtra("clinic_name",clinic_name);
        startService(intent);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String title=marker.getTitle();
        LatLng position = marker.getPosition();
        double latitude = position.latitude;
        double longitude = position.longitude;

        Log.d("latitude", String.valueOf(latitude));
        Log.d("longitude", String.valueOf(longitude));
        Location loc_data = new Location("");
        loc_data.setLatitude(latitude);
        loc_data.setLongitude(longitude);
        selected_clinic = title;

        startIntentService(loc_data,title);


        return false;
    }

    public void setMapFragment(){
        fab.show();
        MapFragment mMapFragment;
        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame2, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(UserNavigation.this);
        tf = new TextFragment();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame3, tf)
                .commit();
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear);
        linearLayout.setVisibility(View.VISIBLE);
        FrameLayout fl = (FrameLayout)findViewById(R.id.content_frame);
        fl.setVisibility(View.GONE);
        text_part = (FrameLayout) findViewById(R.id.content_frame3);
        text_part.setVisibility(View.GONE);
        ActionBar actionBar = getSupportActionBar();
        if(!isPharmacies) {
            toolbar.setTitle("Nearest Clinics");
        }else{
            toolbar.setTitle("Nearest Pharmacies");
        }
    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        int position=-1;

        if (id == R.id.nav_nearest_clinics) {
            isPharmacies=false;
            setMapFragment();
            position=0;
        } else if (id == R.id.nav_nearest_pharmacies) {
            isPharmacies=true;
            setMapFragment();
            position=1;

        } else if (id == R.id.nav_view_bookings) {

            fragment = new BookingFragment();
            Bundle bundle = new Bundle();
            bundle.putString("user_name", current_user_name);
            fragment.setArguments(bundle);
            toolbar.setTitle("Bookings list");

        }else if (id == R.id.nav_queue) {
            fragment = new QueueFragment();
            Bundle bundle = new Bundle();
            bundle.putString("user_name", current_user_name);
            fragment.setArguments(bundle);
            toolbar.setTitle("View Queues");

        } else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
            toolbar.setTitle("Settings");

        }

        if(position!=0 && position!=1) {
            fab.hide();
            android.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack(null)
                    .commit();
            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear);
            linearLayout.setVisibility(View.GONE);
            FrameLayout fl = (FrameLayout)findViewById(R.id.content_frame);
            fl.setVisibility(View.VISIBLE);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    public void setTitle(CharSequence title) {
//        mTitle = title;
//        getSupportActionBar().setTitle(mTitle);
//    }

}
