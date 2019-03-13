package com.lkersten.android.static_project;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class CustomBaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //initialize default firebase app
        FirebaseApp.initializeApp(this);
    }
}
