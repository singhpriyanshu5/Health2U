package com.example.priyanshu.health2u.controller;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.priyanshu.health2u.R;


public class TextFragment extends Fragment {

    TextView tv,tv2,tv3,tv4;
    LinearLayout ll;
   // boolean m_isPharmacies=false;

    public TextFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        ll=(LinearLayout)view.findViewById(R.id.linear1);
        tv=(TextView)view.findViewById(R.id.tv);
        tv2=(TextView)view.findViewById(R.id.tv2);
        tv3=(TextView)view.findViewById(R.id.tv3);
        tv4=(TextView)view.findViewById(R.id.tv4);
        return view;
    }

    public void setTextTv(String est_time, String text, String address, String city, boolean isPharmacies,String user_name){
        tv.setText(text);
        //m_isPharmacies=isPharmacies;
        if(!isPharmacies) {
            tv2.setText("Medical Clinic: " + address + " " + city);
        }else {
            tv2.setText("Pharmacy: " + address + " " + city);
        }
        tv3.setText("Waiting Time : " + est_time);
        tv4.setText("Consultation fee : 50SGD ");
        ll.setOnTouchListener(new LinearLayoutTouchListener(this,isPharmacies,address,city,user_name));


    }

    public String getTextTv(){
            return tv.getText().toString();

    }



}
