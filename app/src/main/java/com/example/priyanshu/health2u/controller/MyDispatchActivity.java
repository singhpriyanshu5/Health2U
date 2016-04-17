package com.example.priyanshu.health2u.controller;

import com.example.priyanshu.health2u.controller.UserNavigation;
import com.parse.ui.ParseLoginDispatchActivity;

/**
 * Created by priyanshu on 19/3/2016.
 */
public class MyDispatchActivity extends ParseLoginDispatchActivity {
    @Override
    protected Class<?> getTargetClass() {
        return UserNavigation.class;
    }

}

