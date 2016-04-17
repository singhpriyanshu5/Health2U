package com.example.priyanshu.health2u.model.AppLogic;

/**
 * Created by priyanshu on 17/4/2016.
 */
public interface DataManagerInterface {
    public void update(String key, Object value, String type);
    public void add(String key, Object value, String type);
    public void delete(String key, String type);

}
