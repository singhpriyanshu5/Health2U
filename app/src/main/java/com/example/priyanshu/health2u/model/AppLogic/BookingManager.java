package com.example.priyanshu.health2u.model.AppLogic;

import com.example.priyanshu.health2u.model.Data.Booking;

/**
 * Created by priyanshu on 16/4/2016.
 */
public class BookingManager {
    private Booking[] bookingList;

    public Booking[] getBookingList() {
        return bookingList;
    }

    public void setBookingList(Booking[] bookingList) {
        this.bookingList = bookingList;
    }

    public void setTime(String time ,Booking booking){

    }

    public void setDate(String date ,Booking booking){

    }

    public void DeleteBooing(Booking booking){

    }
    public void setPatientDetails(Booking booking,String dob, String extra , String patientName , String[] services){

    }
    public void notifyUserBookingTime(){

    }

    public void markMiss(Booking booking){

    }

    public void markAttended(Booking booking){

    }

}
