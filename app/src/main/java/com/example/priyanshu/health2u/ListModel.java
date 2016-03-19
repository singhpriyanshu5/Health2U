package com.example.priyanshu.health2u;

/**
 * Created by priyanshu on 12/3/2016.
 */
public class ListModel {
    private String clinic_name="";
    private String time_text="";
    private String date_text="";
    private String objectId="";

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getDate_text() {
        return date_text;
    }

    public void setDate_text(String date_text) {
        this.date_text = date_text;
    }

    public String getTime_text() {
        return time_text;
    }

    public void setTime_text(String time_text) {
        this.time_text = time_text;
    }

    public String getClinic_name() {
        return clinic_name;
    }

    public void setClinic_name(String clinic_name) {
        this.clinic_name = clinic_name;
    }
}
