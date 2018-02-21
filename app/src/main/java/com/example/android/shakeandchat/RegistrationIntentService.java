package com.example.android.shakeandchat;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by dicky on 21/02/18.
 */

public class RegistrationIntentService extends IntentService {
    private static final String TAG = "RegIntentService";

    public RegistrationIntentService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent){
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "FCM Registration token: "+ token);
    }
}
