package com.zzvox.recycle;

import android.app.Application;

public class MyApplication extends Application {
    public static MyApplication appContext;


    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
    }

    public static MyApplication getInstance() {
        if (appContext == null) {
        }
        return appContext;
    }
}
