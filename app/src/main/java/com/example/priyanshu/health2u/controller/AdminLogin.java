package com.example.priyanshu.health2u.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.priyanshu.health2u.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class AdminLogin extends AppCompatActivity {
    Button clinic_login = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        clinic_login = (Button)findViewById(R.id.clinic_login);
        final EditText username = (EditText)findViewById(R.id.clinic_username);
        final EditText password = (EditText)findViewById(R.id.clinic_password);



        clinic_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog = ProgressDialog.show(AdminLogin.this, "",
                        "Logging In...", true);
                ParseQuery<ParseObject> query = ParseQuery.getQuery("usernameToClinic");
                final String clinic_username = username.getText().toString();
                final String clinic_password = password.getText().toString();
                query.whereEqualTo("clinic_username", clinic_username);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            if (objects != null) {
                                String correct_pass = objects.get(0).getString("password");
                                if (correct_pass.equals(clinic_password)) {
                                    Intent i = new Intent(AdminLogin.this, AdminNavigation.class);
                                    String clinic_name = objects.get(0).getString("clinic_name");
                                    String clinic_address = objects.get(0).getString("clinic_address");
                                    i.putExtra("clinic_address", clinic_address);
                                    i.putExtra("clinic_name",clinic_name);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }
                    }
                });
            }
        });

    }


}
