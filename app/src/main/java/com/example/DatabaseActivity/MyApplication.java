package com.example.DatabaseActivity;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = this;
        Stetho.initializeWithDefaults(this);
    }

    public static Context getContext() {
        return context;
    }
}
