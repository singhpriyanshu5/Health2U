package com.example.priyanshu.health2u.controller;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.priyanshu.health2u.R;

import java.util.ArrayList;


public class CustomAdminAdapter extends BaseAdapter implements View.OnClickListener {

    private Activity activity;
    private Fragment fragment;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    ListModelAdmin tempValues=null;
    int i=0;
    public CustomAdminAdapter(Fragment f , ArrayList d,Resources resLocal) {

        fragment = f;
        activity = f.getActivity();
        data=d;
        res = resLocal;

        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public static class ViewHolder{

        public TextView patient_name;
        public TextView time_text;
        public TextView date_text;

    }


    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.list_item_admin, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.patient_name = (TextView) vi.findViewById(R.id.patient_name);
            holder.time_text=(TextView)vi.findViewById(R.id.time_text);
            holder.date_text=(TextView)vi.findViewById(R.id.date_text);
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.patient_name.setText("No Data");
            holder.time_text.setText("No Data");
            holder.date_text.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = ( ListModelAdmin ) data.get( position );
            final String objectId = tempValues.getObjectId();
            Log.d("CustomAdapter", "id" + objectId);

            holder.patient_name.setText( tempValues.getPatient_name() );
            holder.time_text.setText(tempValues.getTime_text());
            holder.date_text.setText(tempValues.getDate_text());


            /******** Set Item Click Listner for LayoutInflater for each row *******/



            vi.setOnClickListener(new MyItemClickListener( position ));
        }
        return vi;

    }

    private class MyItemClickListener  implements View.OnClickListener {
        private int mPosition;

        MyItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {


              AdminBookingFragment adminBookingFragment = (AdminBookingFragment)fragment;
              adminBookingFragment.onItemClick(mPosition);
//            ViewBooking2 sct = (ViewBooking2)activity;
//
//            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/
//
//            sct.onItemClick(mPosition);
        }
    }


    @Override
    public void onClick(View v) {

    }
}

