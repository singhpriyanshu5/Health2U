package com.example.priyanshu.health2u;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ViewBooking2 extends AppCompatActivity {
    private String user_name;
    private Resources res;
    ListView list;
    CustomAdapter adapter;
    public ArrayList<ListModel> CustomArr = new ArrayList<ListModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking_2);
        Intent intent =getIntent();
        user_name = intent.getStringExtra("user_name");


        res =getResources();
        list= ( ListView )findViewById( R.id.lv_2);  // List defined in XML ( See Below )

        ParseQuery<ParseObject> query = ParseQuery.getQuery("booking");
        query.whereEqualTo("user_name", user_name);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> bookingList, ParseException e) {
                if (e == null) {
                    Log.d("bookings", "Retrieved " + bookingList.size() + " booking " + bookingList.get(0).getString("nric"));
                    if (bookingList != null) {
                        for (int i = 0; i < bookingList.size(); i++) {
                            String clinic_name = bookingList.get(i).getString("clinic_name");
                            String time_text = bookingList.get(i).getString("timeText");
                            String date_text = bookingList.get(i).getString("dateText");
                            CustomArr.add(setListData(clinic_name, time_text, date_text));

                            /**************** Create Custom Adapter *********/

                        }
                        Log.d("bookings",CustomArr.toString());
                        adapter = new CustomAdapter(ViewBooking2.this, CustomArr, res);
                        list.setAdapter(adapter);
                    } else {
                        Log.d("booking", "Error: " + e.getMessage());

                    }
                }
            }
        });





    }

    public ListModel setListData(String clinic_name, String time_text, String date_text)
    {
            final ListModel sched = new ListModel();

            /******* Firstly take data in model object ******/
            sched.setClinic_name(clinic_name);
        sched.setTime_text(time_text);
            sched.setDate_text(date_text);

        return sched;
    }

    public void onItemClick(int mPosition)
    {
        ListModel tempValues = CustomArr.get(mPosition);


        // SHOW ALERT

        Toast.makeText(this,
                " " + tempValues.getClinic_name()
                        + " "+ tempValues.getTime_text()+
        " " + tempValues.getDate_text(),
        Toast.LENGTH_SHORT)
        .show();
    }


}
