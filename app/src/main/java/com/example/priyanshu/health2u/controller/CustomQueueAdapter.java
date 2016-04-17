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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.priyanshu.health2u.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by priyanshu on 12/3/2016.
 */
public class CustomQueueAdapter extends BaseAdapter implements View.OnClickListener {

    private Activity activity;
    private Fragment fragment;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    ListModelQueue tempValues=null;
    int i=0;
    public CustomQueueAdapter(Fragment f, ArrayList d,Resources resLocal) {

        /********** Take passed values **********/
        fragment = f;
        activity = f.getActivity();
        data=d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    @Override
    public void onClick(View v) {

    }

    public static class ViewHolder{

        public TextView clinic_name;
        public TextView current_queue_tv;
        public TextView your_queue_tv;
        public ImageView img_cancel;
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
            vi = inflater.inflate(R.layout.list_item_queue, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.clinic_name = (TextView) vi.findViewById(R.id.clinic_name);
            holder.current_queue_tv = (TextView) vi.findViewById(R.id.current_tv);
            holder.your_queue_tv=(TextView)vi.findViewById(R.id.your_tv);
            holder.img_cancel = (ImageView)vi.findViewById(R.id.img_cancel);
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.clinic_name.setText("No data");
            holder.current_queue_tv.setText("No Data");
            holder.your_queue_tv.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = ( ListModelQueue ) data.get( position );
            final String objectId = tempValues.getObjectId();

            /************  Set Model values in Holder elements ***********/

            holder.clinic_name.setText( tempValues.getClinic_name() );

            holder.your_queue_tv.setText(String.valueOf(tempValues.getYour_queue()));
            holder.current_queue_tv.setText(String.valueOf(tempValues.getCurrent_queue()));

            holder.img_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.remove(position);
                    notifyDataSetChanged();
                    ParseQuery query = new ParseQuery("queue");
                    query.whereEqualTo("objectId", objectId);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> queuelist, ParseException e) {
                            if (e == null) {
                                Log.d("CustomQueueAdapter", "Retrieved " + queuelist.size() + " queue objects");
                                for (int i = 0; i < queuelist.size(); i++) {
                                    ParseObject tempTest = queuelist.get(i);
                                    tempTest.deleteInBackground();
                                }
                            } else {
                                Log.d("CustomQueueAdapter", "Error: " + e.getMessage());
                            }
                        }
                    });

                }
            });

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


            QueueFragment queueFragment = (QueueFragment)fragment;

            queueFragment.onItemClick(mPosition);

//            ViewBooking2 sct = (ViewBooking2)activity;
//
//            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/
//
//            sct.onItemClick(mPosition);
        }
    }


}
