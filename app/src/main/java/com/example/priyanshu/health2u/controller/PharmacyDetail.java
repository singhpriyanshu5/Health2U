package com.example.priyanshu.health2u.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.priyanshu.health2u.R;

public class PharmacyDetail extends AppCompatActivity {
    private String title,m_address,m_city;
    TextView tv_address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        tv_address = (TextView)findViewById(R.id.tv_address);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        m_address = intent.getStringExtra("address");
        m_city = intent.getStringExtra("city");
        tv_address.setText(m_address + " " + m_city);
        actionBar.setTitle(title);
    }

    public void openDateSelect(View view) {
        Intent intent = new Intent(this, DateSelect.class);
        intent.putExtra("clinic_name",title);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
