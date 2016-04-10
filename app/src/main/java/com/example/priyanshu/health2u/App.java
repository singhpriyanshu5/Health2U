package com.example.priyanshu.health2u;

import android.app.Application;

import com.onesignal.OneSignal;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this); // Your Application ID and Client Key are defined elsewhere
        ParseFacebookUtils.initialize(this);

//        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
//        installation.put("missed_queue", true);
//        installation.saveInBackground();



        OneSignal.startInit(this).init();
//        try {
//            OneSignal.sendTag("user_name",);
//            OneSignal.postNotification(new JSONObject("{'contents': {'en':'Test Message'}, 'include_player_ids': ['" + userId + "','" +userId2 + "']}"), null);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


    }
}

