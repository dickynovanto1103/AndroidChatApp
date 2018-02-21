package com.example.android.shakeandchat;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by dicky on 21/02/18.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        //get updated InstanceID token
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d(TAG,"Refreshed token: " + refreshedToken);
//
//        sendRegistrationToServer(refreshedToken);
        Log.d(TAG, "test");
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }

    private void sendRegistrationToServer(String token){
        // TODO
    }
}
