package com.example.priyanshu.health2u.model.AppLogic;

/**
 * Created by priyanshu on 16/4/2016.
 */
public class QueueManager {
    private int waitingTime;
    private int queueNo;
    private int[] queue;

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getQueueNo() {
        return queueNo;
    }

    public void setQueueNo(int queueNo) {
        this.queueNo = queueNo;
    }

    public int[] getQueue() {
        return queue;
    }

    public void setQueue(int[] queue) {
        this.queue = queue;
    }

    private int calcWaitingTime(){
        return 0;
    }

    public void markMiss(String ClinicName , int queueNo){

    }

    public void markAttended(String ClinicName , int queueNo){

    }
    public void addQueue(String Username,String patientName,String clinicName){

    }




}
