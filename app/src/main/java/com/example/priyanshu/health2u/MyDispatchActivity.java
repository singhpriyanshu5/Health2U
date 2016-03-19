package com.example.priyanshu.health2u;

import com.parse.ui.ParseLoginDispatchActivity;

/**
 * Created by priyanshu on 19/3/2016.
 */
public class MyDispatchActivity extends ParseLoginDispatchActivity {
    @Override
    protected Class<?> getTargetClass() {
        return DrawerActivity.class;
    }

}

