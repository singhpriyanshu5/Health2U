package com.example.priyanshu.health2u;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onesignal.OneSignal;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManageQueueFragment extends Fragment {

    TextView current_queue_tv,patient_name_tv,nric_tv, clinic_name_tv;
    String clinic_name;
    View rootView;
    int current_queue_no;
    String patient_name;
    Button next_number_btn,skip_btn;
    String objectId, queueObjectId;
    String user_name;
    String patient_name_deleted="";
    String clinic_name_deleted ="";
    String nric="";
    RelativeLayout loadingPanel;

    public ManageQueueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView =  inflater.inflate(R.layout.fragment_manage_queue, container, false);

        clinic_name_tv = (TextView)rootView.findViewById(R.id.clinic_name_tv);
        clinic_name = getArguments().getString("clinic_name");
        clinic_name_tv.setText(clinic_name);
        current_queue_tv = (TextView)rootView.findViewById(R.id.current_queue_tv);
        patient_name_tv = (TextView)rootView.findViewById(R.id.patient_name_tv);
        next_number_btn = (Button) rootView.findViewById(R.id.next_number_btn);
        skip_btn = (Button) rootView.findViewById(R.id.skip_btn);
        nric_tv = (TextView) rootView.findViewById(R.id.nric_tv);
        loadingPanel = (RelativeLayout)rootView.findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.VISIBLE);
        ParseQuery<ParseObject> q = ParseQuery.getQuery("usernameToClinic");
        q.whereEqualTo("clinic_name", clinic_name);
        q.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        objectId = objects.get(0).getObjectId();
                        current_queue_no = objects.get(0).getInt("current_queue");

                        ParseQuery<ParseObject> q = ParseQuery.getQuery("queue");
                        q.whereEqualTo("clinic_name", clinic_name);
                        q.whereEqualTo("queue_number", current_queue_no);
                        q.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    if (objects.size() > 0) {
                                        loadingPanel.setVisibility(View.GONE);
                                        user_name = objects.get(0).getString("user_name");
                                        queueObjectId = objects.get(0).getObjectId();
                                        patient_name = objects.get(0).getString("patient_name");
                                        nric = objects.get(0).getString("nric");
                                        nric_tv.setText(nric);
                                        patient_name_tv.setText(patient_name);
                                        current_queue_tv.setText(String.valueOf(current_queue_no));
                                    } else {
                                        Log.d("ManageQueue", "No queue data");
                                    }
                                }
                            }
                        });


                    } else {
                        Log.d("ManageQueue", "no clinic found");
                    }
                }
            }
        });

        next_number_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moveToNextQueue();

            }
        });

        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moveToNextQueue();
//                ParseQuery pushQuery = ParseInstallation.getQuery();
//                pushQuery.whereEqualTo("missed_queue", true);
////
//// Send push notification to query
//                ParsePush push = new ParsePush();
//                push.setQuery(pushQuery); // Set our Installation query
//                push.setMessage("Willie Hayes injured by own pop fly.");
//                push.sendInBackground();

                String userId = "37af541b-7f0c-495b-a3f0-a42257ec1999";
                //String userId2 = "a8d72100-ac0c-4648-a429-3067b97934e6";

                try {

                    OneSignal.postNotification(new JSONObject("{'contents': {'en':'Hi'"+patient_name_deleted+"' , you just missed your queue number at'" + clinic_name_deleted +"'. '}, 'include_player_ids': ['" + userId + "']}"), null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                NotificationCompat.Builder mBuilder =
//                        (NotificationCompat.Builder) new NotificationCompat.Builder(getActivity())
//                                .setSmallIcon(R.drawable.com_facebook_button_icon)
//                                .setContentTitle("My notification")
//                                .setContentText("Hello World!");
//
//                Intent resultIntent = new Intent(getActivity(), UserNavigation.class);
//// Because clicking the notification opens a new ("special") activity, there's
//// no need to create an artificial back stack.
//                PendingIntent resultPendingIntent =
//                        PendingIntent.getActivity(
//                                getActivity(),
//                                0,
//                                resultIntent,
//                                PendingIntent.FLAG_UPDATE_CURRENT
//                        );
//                mBuilder.setContentIntent(resultPendingIntent);
//
//// Sets an ID for the notification
//                int mNotificationId = 001;
//// Gets an instance of the NotificationManager service
//                NotificationManager mNotifyMgr =
//                        (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//// Builds the notification and issues it.
//                mNotifyMgr.notify(mNotificationId, mBuilder.build());

                //notify the patient in background to queue again

            }
        });


        return rootView;
    }

    private void moveToNextQueue(){
        ParseQuery<ParseObject> q2 = ParseQuery.getQuery("queue");
        q2.getInBackground(queueObjectId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    if (object != null) {
                        patient_name_deleted = object.getString("patient_name");
                        clinic_name_deleted = object.getString("clinic_name");

                        object.deleteInBackground();
                    } else {
                        Log.d("ManageQueue", "no clinic data for delete");
                    }
                }
            }
        });


        ParseQuery<ParseObject> q = ParseQuery.getQuery("usernameToClinic");
        q.getInBackground(objectId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    if (object != null) {
                        current_queue_no = current_queue_no + 1;
                        object.put("current_queue", current_queue_no);
                        object.saveInBackground();

                        ParseQuery<ParseObject> q3 = ParseQuery.getQuery("queue");
                        q3.whereEqualTo("clinic_name", clinic_name);
                        q3.whereEqualTo("queue_number", current_queue_no);
                        q3.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    if (objects.size() > 0) {
                                        queueObjectId = objects.get(0).getObjectId();
                                        patient_name = objects.get(0).getString("patient_name");
                                        patient_name_tv.setText(patient_name);
                                        nric = objects.get(0).getString("nric");
                                        Log.d("ManageQueue","vvvv"+nric);
                                        nric_tv.setText(nric);
                                        current_queue_tv.setText(String.valueOf(current_queue_no));
                                    } else {
                                        Log.d("ManageQueue", "No queue data");
                                    }
                                }
                            }
                        });
                    } else {
                        Log.d("ManageQueue", "no clinic data for update");
                    }
                }
            }
        });
    }

}
