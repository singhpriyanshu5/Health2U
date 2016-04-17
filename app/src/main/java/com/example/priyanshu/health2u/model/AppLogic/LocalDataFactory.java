package com.example.priyanshu.health2u.model.AppLogic;

import android.content.Context;

import com.example.priyanshu.health2u.model.Data.ClinicList;
import com.example.priyanshu.health2u.model.Data.PharmacyList;

/**
 * Created by priyanshu on 17/4/2016.
 */
public class LocalDataFactory {
    private String type;
    public localDataInterface selectList(Context c){
        if(type.equals("clinic")){
            return new ClinicList(c);
        }else if(type.equals("pharmacy")){
            return new PharmacyList(c);
        }
        return null;
    }
}
