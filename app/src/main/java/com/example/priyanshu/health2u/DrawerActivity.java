package com.example.priyanshu.health2u;


import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

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
import java.util.List;
import java.util.Locale;

public class DrawerActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {

    private static boolean isPharmacies=false;
    private String[] mTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    CharSequence mTitle;
    CharSequence mDrawerTitle;
    Toolbar app_toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private Location mLastLocation;
    private double user_lat, user_long;
    private boolean isOrigiin = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mGoogleApiClient;
    private GoogleApiClient client;
    private LocationRequest mLocationRequest;
    private TextFragment tf;
    private FrameLayout text_part;

    private String current_user_name = null;

    private FloatingActionButton fab =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.toolbar_drawer);
        app_toolbar = (Toolbar) findViewById(R.id.toolbar);
        app_toolbar.setTitle(R.string.app_name);
        setSupportActionBar(app_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        Intent intent = getIntent();
//        if (ParseUser.getCurrentUser() == null) {
//            ParseLoginBuilder builder = new ParseLoginBuilder(this);
//            startActivityForResult(builder.build(), 0);
//        }


        if(ParseUser.getCurrentUser()!=null) {
            current_user_name = ParseUser.getCurrentUser().getString("name");
            Log.d("DrawerActivity","Enter nameeeeeeeeeeeeeee");
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DrawerActivity.this, ClinicSearch.class);
                startActivity(i);
            }
        });

        mTitle = mDrawerTitle = getTitle();
        mTitles = new String[]{"Nearest Clinics", "Nearest Pharmacies","View Bookings", "Settings"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
            }
        };
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        isOrigiin = intent.getBooleanExtra("origin", false);
        if (isOrigiin == true) {
            //selectItem(1);
            Fragment fragment = new BookingFragment();
            Bundle bundle = new Bundle();
            Log.d("DrawerActivity", "user: " + current_user_name);
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


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            ParseUser.logOut();
            //finish();
//            Parse.initialize(this);
            //        ParseFacebookUtils.initialize(this);
//            ParseLoginBuilder builder = new ParseLoginBuilder(this);
//            startActivityForResult(builder.build(), 0);
            finish();
            Intent i = new Intent(DrawerActivity.this, SplashActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
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
                if(dist<3) {

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

    @Override
    public boolean onMarkerClick(Marker marker) {
        String title=marker.getTitle();
        LatLng position = marker.getPosition();
        double latitude = position.latitude;
        double longitude = position.longitude;
        Geocoder geocoder;
        List<Address> yourAddresses=null;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            yourAddresses= geocoder.getFromLocation(latitude, longitude, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        String yourAddress="address";
        String yourCity="city";
        String yourCountry="country";

        if (yourAddresses.size() > 0)
        {
            yourAddress = yourAddresses.get(0).getAddressLine(0);
            yourCity = yourAddresses.get(0).getAddressLine(1);
            yourCountry = yourAddresses.get(0).getAddressLine(2);
        }

        Log.d("GeoCoder", "address is : " + yourAddress + yourCity + yourCountry);

        //tf.setTextTv(title,yourAddress,yourCity,isPharmacies,current_user_name);
        if(text_part.getVisibility()== View.GONE) {
            text_part.setVisibility(View.VISIBLE);
        }
        return false;
    }



    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    public void setMapFragment(){
        fab.show();
        MapFragment mMapFragment;
        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame2, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(DrawerActivity.this);
        tf = new TextFragment();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame3, tf)
                .commit();
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear);
        linearLayout.setVisibility(linearLayout.VISIBLE);
        FrameLayout fl = (FrameLayout)findViewById(R.id.content_frame);
        fl.setVisibility(View.GONE);
        text_part = (FrameLayout) findViewById(R.id.content_frame3);
        text_part.setVisibility(View.GONE);
        ActionBar actionBar = getSupportActionBar();
        if(!isPharmacies) {
            actionBar.setTitle("Nearest Clinics");
        }else{
            actionBar.setTitle("Nearest Pharmacies");
        }
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = null;


        switch (position) {
            case 0:
                isPharmacies=false;
               setMapFragment();
                break;
            case 1:
                isPharmacies=true;
                setMapFragment();
                break;
            case 2:
                fragment = new BookingFragment();
                Bundle bundle = new Bundle();
                Log.d("DrawerActivity", "user: " + current_user_name);
                bundle.putString("user_name", current_user_name);
                fragment.setArguments(bundle);
                break;
            case 3:
                fragment = new SettingsFragment();
                break;

        }

        // Insert the fragment by replacing any existing fragment
        if(position!=0 && position!=1) {
            fab.hide();
            android.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear);
            linearLayout.setVisibility(View.GONE);
            FrameLayout fl = (FrameLayout)findViewById(R.id.content_frame);
            fl.setVisibility(View.VISIBLE);
        }

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mTitles[position]);

        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}