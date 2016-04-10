package com.example.priyanshu.health2u;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by priyanshu on 12/3/2016.
 */
public class CustomAdapter extends BaseAdapter implements View.OnClickListener {

    private Activity activity;
    private Fragment fragment;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    ListModel tempValues=null;
    int i=0;
    public CustomAdapter(Fragment f, ArrayList d,Resources resLocal) {

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

    public static class ViewHolder{

        public TextView clinic_name;
        public TextView time_text;
        public TextView date_text;
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
            vi = inflater.inflate(R.layout.list_item, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.clinic_name = (TextView) vi.findViewById(R.id.clinic_name);
            holder.time_text=(TextView)vi.findViewById(R.id.time_text);
            holder.date_text=(TextView)vi.findViewById(R.id.date_text);
            holder.img_cancel = (ImageView)vi.findViewById(R.id.img_cancel);
            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.clinic_name.setText("No Data");
            holder.time_text.setText("No Data");
            holder.date_text.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = ( ListModel ) data.get( position );
            final String objectId = tempValues.getObjectId();
            Log.d("CustomAdapter", "id" + objectId);
            /************  Set Model values in Holder elements ***********/

            holder.clinic_name.setText( tempValues.getClinic_name() );
            holder.time_text.setText(tempValues.getTime_text());
            holder.date_text.setText(tempValues.getDate_text());
            holder.img_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open(position,objectId);


                }
            });

            /******** Set Item Click Listner for LayoutInflater for each row *******/



            vi.setOnClickListener(new MyItemClickListener( position ));
        }
        return vi;

    }

    private void cancelBooking(int position, String objectId){
        data.remove(position);
        notifyDataSetChanged();
        ParseQuery query = new ParseQuery("booking");
        query.whereEqualTo("objectId", objectId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> bookingList, ParseException e) {
                if (e == null) {
                    Log.d("CustomAdapter", "Retrieved " + bookingList.size() + " booking objects");
                    for (int i = 0; i < bookingList.size(); i++) {
                        ParseObject tempTest = bookingList.get(i);
                        tempTest.deleteInBackground();
                    }
                } else {
                    Log.d("CustomAdapter", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void open(final int position, final String objectId){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setIcon(R.drawable.doctor);
        alertDialogBuilder.setMessage("Are you sure you want to cancel this booking?");
        alertDialogBuilder.setTitle("Cancel booking");

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                doPositiveClick(position, objectId);            }
        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void doPositiveClick(int position, String objectId) {
        // Do stuff here.

        cancelBooking(position, objectId);
//        Intent intent = new Intent(PatientDetails.this, DrawerActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("origin", true);
//        startActivity(intent);
//        finish();
    }



    private class MyItemClickListener  implements View.OnClickListener {
        private int mPosition;

        MyItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {


              BookingFragment bookingFragment = (BookingFragment)fragment;

              bookingFragment.onItemClick(mPosition);

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
