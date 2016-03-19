package com.example.priyanshu.health2u;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this); // Your Application ID and Client Key are defined elsewhere
        ParseFacebookUtils.initialize(this);
    }
}

