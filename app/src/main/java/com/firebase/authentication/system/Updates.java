package com.firebase.authentication.system;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class Updates extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseUser user;
        user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/all");


        if(user != null ){
            FirebaseMessaging.getInstance().subscribeToTopic("/topics/" + user.getUid());
        }

    }
}
