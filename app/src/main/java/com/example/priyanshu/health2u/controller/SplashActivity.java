package com.example.priyanshu.health2u.controller;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.priyanshu.health2u.R;
import com.parse.ParseUser;


public class SplashActivity extends AppCompatActivity {

    Button userMode = null;
    Button adminMode = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(ParseUser.getCurrentUser()!=null){
            Intent i = new Intent(SplashActivity.this, UserNavigation.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
        setContentView(R.layout.activity_splash);
        userMode = (Button) findViewById(R.id.user_mode);
        adminMode = (Button) findViewById(R.id.admin_mode);

        userMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashActivity.this, MyDispatchActivity.class);
                startActivity(i);
//                if (ParseUser.getCurrentUser() == null) {
//                    ParseLoginBuilder builder = new ParseLoginBuilder(DrawerActivity.this);
//                    startActivityForResult(builder.build(), 0);
//        }
            }
        });

        adminMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashActivity.this, AdminLogin.class);
                startActivity(i);
            }
        });
    }


}

