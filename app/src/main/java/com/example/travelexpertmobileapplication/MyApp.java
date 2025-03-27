package com.example.travelexpertmobileapplication;

import android.app.Application;

import com.example.travelexpertmobileapplication.config.FileLoggingTree;

import timber.log.Timber;

public class MyApp  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new FileLoggingTree(this));
    }
}
