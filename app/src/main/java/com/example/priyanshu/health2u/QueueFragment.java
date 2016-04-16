package com.example.priyanshu.health2u;


import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class QueueFragment extends Fragment {

    private Resources res;
    CustomQueueAdapter adapter;
    ArrayList<ListModelQueue> CustomArr = new ArrayList<>();
    private String user_name;
    ListView list;
    RelativeLayout loadingPanel;

    public QueueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_queue, container, false);
        loadingPanel = (RelativeLayout)rootView.findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.VISIBLE);
        user_name = getArguments().getString("user_name");
        res = getResources();
        list = (ListView)rootView.findViewById(R.id.list_queue);
        ParseQuery<ParseObject> q = ParseQuery.getQuery("queue");
        q.whereEqualTo("user_name", user_name);
        q.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (int i = 0; i < objects.size(); i++) {
                            final String patient_name = objects.get(i).getString("patient_name");
                            final String clinic_name = objects.get(i).getString("clinic_name");
                            final int your_queue = objects.get(i).getInt("queue_number");
                            final String objectId = objects.get(i).getObjectId();

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("usernameToClinic");
                            query.whereEqualTo("clinic_name", clinic_name);
                            query.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null) {
                                        if (objects.size() > 0) {
                                            loadingPanel.setVisibility(View.GONE);
                                            int current_queue = objects.get(0).getInt("current_queue");
                                            Log.d("QueueFragment", "xxxxxxyyyyyy" + objectId);
                                            CustomArr.add(setListData(patient_name, clinic_name, your_queue, objectId, current_queue));

                                            adapter = new CustomQueueAdapter(QueueFragment.this, CustomArr, res);
                                            list.setAdapter(adapter);
                                        } else {
                                            Log.d("QueueFragment", "no such clinic");
                                        }

                                    }
                                }
                            });


                        }

                    } else {
                        Log.d("QueueFragment", "no queue data found");
                    }
                }
            }
        });



        return rootView;
    }

    public ListModelQueue setListData(final String patient_name, final String clinic_name, final int your_queue, final String objectId, final int current_queue)
    {
        final ListModelQueue sched = new ListModelQueue();

        sched.setPatient_name(patient_name);
        sched.setClinic_name(clinic_name);
        sched.setYour_queue(your_queue);
        sched.setCurrent_queue(current_queue);
        sched.setObjectId(objectId);


        return sched;

    }

    public void onItemClick(int mPosition)
    {
        ListModelQueue tempValues = CustomArr.get(mPosition);
        Log.d("QueueFragment", "xxxxxx"+tempValues.getObjectId());

        Intent i = new Intent(getActivity(), QueueDetails.class);
        i.putExtra("objectId", tempValues.getObjectId());
        startActivity(i);

    }


}
