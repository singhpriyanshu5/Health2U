package com.example.priyanshu.health2u.controller;

public class ListModelQueue {
    private int current_queue;
    private int your_queue;
    private int estimated_waiting_time;
    private String clinic_name;
    private String objectId;
    private String patient_name;

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getClinic_name() {
        return clinic_name;
    }

    public void setClinic_name(String clinic_name) {
        this.clinic_name = clinic_name;
    }

    public int getCurrent_queue() {
        return current_queue;
    }

    public void setCurrent_queue(int current_queue) {
        this.current_queue = current_queue;
    }

    public int getYour_queue() {
        return your_queue;
    }

    public void setYour_queue(int your_queue) {
        this.your_queue = your_queue;
    }

    public int getEstimated_waiting_time() {
        return estimated_waiting_time;
    }

    public void setEstimated_waiting_time(int estimated_waiting_time) {
        this.estimated_waiting_time = estimated_waiting_time;
    }
}
