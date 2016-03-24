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

    public QueueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_queue, container, false);
        user_name = getArguments().getString("user_name");
        res = getResources();
        list = (ListView)rootView.findViewById(R.id.list_queue);
        ParseQuery<ParseObject> q = ParseQuery.getQuery("queue");
        q.whereEqualTo("user_name",user_name);
        q.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for(int i=0;i<objects.size();i++) {
                            String patient_name = objects.get(i).getString("patient_name");
                            String clinic_name = objects.get(i).getString("clinic_name");
                            int your_queue = objects.get(i).getInt("queue_number");
                            String objectId = objects.get(i).getObjectId();
                            Log.d("QueueFragment", "xxxxxxyyyyyy" + objectId);
                            CustomArr.add(setListData(patient_name, clinic_name, your_queue, objectId));
                        }
                        adapter = new CustomQueueAdapter(QueueFragment.this,CustomArr,res);
                        list.setAdapter(adapter);
                    } else {
                        Log.d("QueueFragment", "no queue data found");
                    }
                }
            }
        });



        return rootView;
    }

    public ListModelQueue setListData(String patient_name, String clinic_name, int your_queue, String objectId)
    {
        final ListModelQueue sched = new ListModelQueue();

        /******* Firstly take data in model object ******/
        sched.setPatient_name(patient_name);
        sched.setClinic_name(clinic_name);
        sched.setYour_queue(your_queue);
        sched.setCurrent_queue(your_queue - 30);
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
