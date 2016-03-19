package com.example.priyanshu.health2u;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by priyanshu on 21/2/2016.
 */
public class LinearLayoutTouchListener implements View.OnTouchListener {

    boolean m_isPharmacies=false;
    String m_address="";
    String m_city="";
    static final String logTag = "ActivitySwipeDetector";
    private Activity activity;
    private TextFragment mtf;
    static final int MIN_DISTANCE = 100;// TODO change this runtime based on screen resolution. for 1920x1080 is to small the 100 distance
    private float downX, downY, upX, upY;

    // private MainActivity mMainActivity;

    public LinearLayoutTouchListener(TextFragment tf, boolean isPharmacies, String address, String city) {
        m_isPharmacies=isPharmacies;
        m_address=address;
        m_city=city;
        mtf=tf;
        activity = tf.getActivity();
    }

    public void onRightToLeftSwipe() {
        Log.i(logTag, "RightToLeftSwipe!");
        Toast.makeText(activity, "RightToLeftSwipe", Toast.LENGTH_SHORT).show();
        // activity.doSomething();
    }

    public void onLeftToRightSwipe() {
        Log.i(logTag, "LeftToRightSwipe!");
        Toast.makeText(activity, "LeftToRightSwipe", Toast.LENGTH_SHORT).show();
        // activity.doSomething();
    }

    public void onTopToBottomSwipe() {
        Log.i(logTag, "onTopToBottomSwipe!");
        Toast.makeText(activity, "onTopToBottomSwipe", Toast.LENGTH_SHORT).show();
        // activity.doSomething();
    }

    public void onBottomToTopSwipe() {
        Log.i(logTag, "onBottomToTopSwipe!");
        //Toast.makeText(activity, "onBottomToTopSwipe", Toast.LENGTH_SHORT).show();
        call_intent();
        // activity.doSomething();
    }

    public void call_intent(){
        Intent intent=null;
        if(!m_isPharmacies) {
            intent = new Intent(activity, ClinicDetail.class);
        }else{
            intent = new Intent(activity, PharmacyDetail.class);
        }
        String title =mtf.getTextTv();
        intent.putExtra("title",title);
        intent.putExtra("address", m_address);
        intent.putExtra("city",m_city);
        activity.startActivity(intent);
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe horizontal?
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    // left or right
                    if (deltaX < 0) {
                        this.onLeftToRightSwipe();
                        return false;
                    }
                    if (deltaX > 0) {
                        this.onRightToLeftSwipe();
                        return false;
                    }
                } else {
                    if (Math.abs(deltaY) < MIN_DISTANCE) {
                        call_intent();
                        Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long horizontally, need at least " + MIN_DISTANCE);
                    }
                    // return false; // We don't consume the event
                }

                // swipe vertical?
                if (Math.abs(deltaY) > MIN_DISTANCE) {
                    // top or down
                    if (deltaY < 0) {
                        this.onTopToBottomSwipe();
                        return false;
                    }
                    if (deltaY > 0) {
                        this.onBottomToTopSwipe();
                        return true;
                    }
                } else {
                    Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long vertically, need at least " + MIN_DISTANCE);
                    // return false; // We don't consume the event
                }

                return false; // no swipe horizontally and no swipe vertically
            }// case MotionEvent.ACTION_UP:
        }
        return false;
    }
}
