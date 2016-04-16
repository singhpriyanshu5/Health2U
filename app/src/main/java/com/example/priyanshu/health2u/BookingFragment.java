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
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookingFragment extends Fragment {

    private TextView tv_current, tv_past;
    private String user_name,patient_name;
    private Resources res;
    ListView list,list2;
    CustomAdapter adapter,adapter2;
    public ArrayList<ListModel> CustomArr = new ArrayList<ListModel>();
    public ArrayList<ListModel> CustomArr2 = new ArrayList<ListModel>();
    RelativeLayout loadingPanel;

    public BookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_booking, container, false);
        loadingPanel = (RelativeLayout) rootView.findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.VISIBLE);

        tv_current = (TextView) rootView.findViewById(R.id.current_nodata);
        tv_past = (TextView)rootView.findViewById(R.id.past_nodata);
        user_name = getArguments().getString("user_name");
        Log.d("bookings", user_name);
        res =getResources();
        list= ( ListView )rootView.findViewById( R.id.lv_1);
        list2= ( ListView )rootView.findViewById( R.id.lv_2);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("booking");
        query.whereEqualTo("user_name", user_name);
        query.whereEqualTo("attended", false);
        query.orderByDescending("updatedAt");
            query.findInBackground(new FindCallback<ParseObject>()

            {
                public void done (List < ParseObject > bookingList, ParseException e){
                if (e == null) {
                    if (bookingList != null) {
                        if(bookingList.size()>0) {
                            for (int i = 0; i < bookingList.size(); i++) {
                                String patient_name = bookingList.get(i).getString("patient_name");
                                String clinic_name = bookingList.get(i).getString("clinic_name");
                                String time_text = bookingList.get(i).getString("timeText");
                                String date_text = bookingList.get(i).getString("dateText");
                                String objectId = bookingList.get(i).getObjectId();
                                CustomArr.add(setListData(patient_name,clinic_name, time_text, date_text, objectId));

                                /**************** Create Custom Adapter *********/

                            }
                            Log.d("bookings", CustomArr.toString());
                            adapter = new CustomAdapter(BookingFragment.this, CustomArr, res);
                            list.setAdapter(adapter);
                        }else{
                            tv_current.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d("booking", "Error: " + e.getMessage());

                    }
                }
            }
            }

            );

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("booking");
        query2.whereEqualTo("user_name", user_name);
        query2.whereEqualTo("attended", true);
        query2.orderByDescending("updatedAt");
        query2.findInBackground(new FindCallback<ParseObject>()

                               {
                                   public void done (List < ParseObject > bookingList, ParseException e){
                                       if (e == null) {
                                           loadingPanel.setVisibility(View.GONE);
                                           if (bookingList != null) {
                                               if(bookingList.size()>0) {
                                                   for (int i = 0; i < bookingList.size(); i++) {
                                                       String patient_name = bookingList.get(i).getString("patient_name");
                                                       String clinic_name = bookingList.get(i).getString("clinic_name");
                                                       String time_text = bookingList.get(i).getString("timeText");
                                                       String date_text = bookingList.get(i).getString("dateText");
                                                       String objectId = bookingList.get(i).getObjectId();
                                                       Log.d("bookings", "id is " + objectId);
                                                       CustomArr2.add(setListData(patient_name,clinic_name, time_text, date_text, objectId));

                                                   }
                                                   Log.d("bookings", CustomArr2.toString());
                                                   adapter2 = new CustomAdapter(BookingFragment.this, CustomArr2, res);
                                                   list2.setAdapter(adapter2);
                                               }else{
                                                    tv_past.setVisibility(View.VISIBLE);
                                               }
                                           } else {
                                               Log.d("booking", "Error: " + e.getMessage());

                                           }
                                       }
                                   }
                               }

        );
            return rootView;
        }

    public ListModel setListData(String patient_name, String clinic_name, String time_text, String date_text, String objectId)
    {
        final ListModel sched = new ListModel();

        /******* Firstly take data in model object ******/
        sched.setPatient_name(patient_name);
        sched.setClinic_name(clinic_name);
        sched.setTime_text(time_text);
        sched.setDate_text(date_text);
        sched.setObjectId(objectId);
        return sched;
    }

    public void onItemClick(int mPosition)
    {
        ListModel tempValues = CustomArr.get(mPosition);


        Intent i = new Intent(getActivity(), BookingDetails.class);
//        i.putExtra("patient_name", tempValues.getPatient_name());
//        i.putExtra("clinic_name", tempValues.getClinic_name());
//        i.putExtra("time_text", tempValues.getTime_text());
//        i.putExtra("date_text", tempValues.getDate_text());
        i.putExtra("isAdmin", false);
        i.putExtra("objectId", tempValues.getObjectId());
        startActivity(i);

        // SHOW ALERT

//        Toast.makeText(getActivity(),
//                " " + tempValues.getClinic_name()
//                        + " " + tempValues.getTime_text() +
//                        " " + tempValues.getDate_text(),
//                Toast.LENGTH_SHORT)
//                .show();
    }

}
