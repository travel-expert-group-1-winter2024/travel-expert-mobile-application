package com.example.travelexpertmobileapplication;

import android.app.Application;

import com.example.travelexpertmobileapplication.config.FileLoggingTree;

import timber.log.Timber;


public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Log to Logcat
        Timber.plant(new Timber.DebugTree());

        // Log to File
        Timber.plant(new FileLoggingTree(this));
    }
}
