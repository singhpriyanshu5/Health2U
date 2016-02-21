package com.example.priyanshu.health2u;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class TextFragment extends Fragment {

    TextView tv,tv2,tv3,tv4;

    public TextFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        tv=(TextView)view.findViewById(R.id.tv);
        tv2=(TextView)view.findViewById(R.id.tv2);
        tv3=(TextView)view.findViewById(R.id.tv3);
        tv4=(TextView)view.findViewById(R.id.tv4);
        return view;
    }

    public void setTextTv(String text){
        tv.setText(text);
        tv2.setText("Medical Clinic · #01-25 Rivervale Mall 545082, Rivervale Cres");
        tv3.setText("Waiting Time : 5 mins ");
        tv4.setText("Consultation fee : 50SGD ");

    }

}
