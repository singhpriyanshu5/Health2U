package com.example.priyanshu.health2u.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.priyanshu.health2u.R;

public class MyDialogFragment extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setIcon(R.drawable.doctor)
                .setTitle("Selected time is already booked, Want to book for 15:30")
                .setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                              //  ((PatientDetails) getActivity()).doPositiveClick();
                            }
                        }
                )
                .setNegativeButton("no",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                              //  ((PatientDetails)getActivity()).doNegativeClick();
                            }
                        }
                )
                .create();
//        return builder.create();
    }
}
