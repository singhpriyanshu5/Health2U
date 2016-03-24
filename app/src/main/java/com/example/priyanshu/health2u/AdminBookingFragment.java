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


public class AdminBookingFragment extends Fragment {

    private String clinic_name, isHistory;
    private Resources res;
    ListView list;
    CustomAdminAdapter adapter;
    public ArrayList<ListModelAdmin> CustomArr = new ArrayList<ListModelAdmin>();
    public AdminBookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin_booking, container, false);
        clinic_name = getArguments().getString("clinic_name");

        res =getResources();
        list= ( ListView )rootView.findViewById( R.id.lv_admin);

        if(isHistory=="true"){
            ParseQuery<ParseObject> query = ParseQuery.getQuery("booking");
            query.whereEqualTo("clinic_name", clinic_name);
            query.whereEqualTo("attended", true);
            query.findInBackground(new FindCallback<ParseObject>()

                                   {
                                       public void done (List< ParseObject > bookingList, ParseException e){
                                           if (e == null) {
                                               if (bookingList != null) {
                                                   if(bookingList.size()>0) {
                                                       for (int i = 0; i < bookingList.size(); i++) {
                                                           String clinic_name = bookingList.get(0).getString("clinic_name");
                                                           String patient_name = bookingList.get(i).getString("patient_name");
                                                           String time_text = bookingList.get(i).getString("timeText");
                                                           String date_text = bookingList.get(i).getString("dateText");
                                                           String objectId = bookingList.get(i).getObjectId();
                                                           CustomArr.add(setListData(clinic_name,patient_name, time_text, date_text, objectId));

                                                           /**************** Create Custom Adapter *********/

                                                       }
                                                       Log.d("bookings", CustomArr.toString());
                                                       adapter = new CustomAdminAdapter(AdminBookingFragment.this, CustomArr, res);
                                                       list.setAdapter(adapter);
                                                   }else{
                                                       Log.d("bookings", "xxxxxxxxxx admin bookinglist size 0");
                                                   }
                                               } else {
                                                   Log.d("booking", "Error: " + e.getMessage());

                                               }
                                           }
                                       }
                                   }

            );
        }else {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("booking");
            query.whereEqualTo("clinic_name", clinic_name);
            query.whereEqualTo("attended", false);
            query.findInBackground(new FindCallback<ParseObject>()

                                   {
                                       public void done(List<ParseObject> bookingList, ParseException e) {
                                           if (e == null) {
                                               if (bookingList != null) {
                                                   if (bookingList.size() > 0) {
                                                       for (int i = 0; i < bookingList.size(); i++) {
                                                           String clinic_name = bookingList.get(0).getString("clinic_name");
                                                           String patient_name = bookingList.get(i).getString("patient_name");
                                                           String time_text = bookingList.get(i).getString("timeText");
                                                           String date_text = bookingList.get(i).getString("dateText");
                                                           String objectId = bookingList.get(i).getObjectId();
                                                           CustomArr.add(setListData(clinic_name, patient_name, time_text, date_text, objectId));

                                                           /**************** Create Custom Adapter *********/

                                                       }
                                                       Log.d("bookings", CustomArr.toString());
                                                       adapter = new CustomAdminAdapter(AdminBookingFragment.this, CustomArr, res);
                                                       list.setAdapter(adapter);
                                                   } else {
                                                       Log.d("bookings", "xxxxxxxxxx admin bookinglist size 0");
                                                   }
                                               } else {
                                                   Log.d("booking", "Error: " + e.getMessage());

                                               }
                                           }
                                       }
                                   }

            );
        }

        return rootView;
    }

    public ListModelAdmin setListData(String clinic_name, String patient_name, String time_text, String date_text, String objectId)
    {
        final ListModelAdmin sched = new ListModelAdmin();

        sched.setClinic_name(clinic_name);
        sched.setPatient_name(patient_name);
        sched.setTime_text(time_text);
        sched.setDate_text(date_text);
        sched.setObjectId(objectId);
        return sched;
    }

    public void onItemClick(int mPosition)
    {
        ListModelAdmin tempValues = CustomArr.get(mPosition);

        Intent i = new Intent(getActivity(), BookingDetails.class);
        i.putExtra("patient_name", tempValues.getPatient_name());
        i.putExtra("clinic_name", tempValues.getClinic_name());
        i.putExtra("time_text", tempValues.getTime_text());
        i.putExtra("date_text", tempValues.getDate_text());
        i.putExtra("isAdmin", true);
        startActivity(i);

    }


}
