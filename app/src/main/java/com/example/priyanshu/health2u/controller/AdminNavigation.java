package com.example.priyanshu.health2u.controller;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.priyanshu.health2u.R;

public class AdminNavigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //private FrameLayout frame = null;
    private String clinic_name="";
    private String clinic_address="";

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_navigation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //frame = (FrameLayout) findViewById(R.id.fragment);
        Intent i = getIntent();
        clinic_name=i.getStringExtra("clinic_name");
        clinic_address = i.getStringExtra("clinic_address");
        View v = getLayoutInflater().inflate(R.layout.nav_header_admin_navigation,null);
//        TextView tv_clinic_name = (TextView) v.findViewById(R.id.nav_clinic_name);
        Log.d("AdminNav", clinic_name);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View nav_header = navigationView.inflateHeaderView(R.layout.nav_header_admin_navigation);
        TextView tv_clinic_name = (TextView)nav_header.findViewById(R.id.nav_clinic_name);
        TextView tv_clinic_address = (TextView)nav_header.findViewById(R.id.nav_clinic_address);
        tv_clinic_name.setText(clinic_name);
        tv_clinic_address.setText(clinic_address);
        navigationView.getMenu().getItem(0).setChecked(true);
        Fragment fragment = new AdminBookingFragment();
        Bundle bundle = new Bundle();
        Log.d("AdminNavigation", "user: " + clinic_name);
        bundle.putString("clinic_name", clinic_name);
        fragment.setArguments(bundle);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_admin, fragment)
                .commit();

        navigationView.setNavigationItemSelectedListener(this);


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
        getMenuInflater().inflate(R.menu.admin_navigation, menu);
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
            Intent i = new Intent(AdminNavigation.this, SplashActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_bookings) {
            toolbar.setTitle("Today's Bookings");
            fragment = new AdminBookingFragment();
            Bundle bundle = new Bundle();
            Log.d("AdminNavigation", "clinic: " + clinic_name);
            bundle.putString("clinic_name", clinic_name);
            fragment.setArguments(bundle);
        } else if (id == R.id.nav_booking_history) {
            toolbar.setTitle("Booking History");
            fragment = new AdminBookingFragment();
            Bundle bundle = new Bundle();
            Log.d("AdminNavigation", "clinic: " + clinic_name);
            bundle.putString("clinic_name", clinic_name);
            bundle.putString("isHistory","true");
            fragment.setArguments(bundle);

        } else if (id == R.id.nav_manage_queue) {
            toolbar.setTitle("Manage Queue");
            fragment = new ManageQueueFragment();
            Bundle bundle = new Bundle();
            Log.d("AdminNavigation", "clinic: " + clinic_name);
            bundle.putString("clinic_name", clinic_name);
            fragment.setArguments(bundle);

        } else if (id == R.id.nav_manage) {

        }

        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_admin, fragment)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
