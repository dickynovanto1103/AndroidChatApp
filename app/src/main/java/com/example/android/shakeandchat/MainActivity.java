package com.example.android.shakeandchat;

import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.Serializable;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    SignInButton signInButton;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.activity_main);



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //result returned
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            //sign in success
            GoogleSignInAccount acct = result.getSignInAccount();




            //kirim data akun ke firebase database
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference reference = firebaseDatabase.getReference("listToken");


            String name = acct.getDisplayName();
            String email = acct.getEmail();
            String token = FirebaseInstanceId.getInstance().getToken();
            Log.i("FIREBASE", "FCM Registration token: "+ token);

            UserWithFirebaseToken userWithFirebaseToken = new UserWithFirebaseToken(name, email, token);

            reference.push().setValue(userWithFirebaseToken);

            Log.d("hasil", "berhasil masukkan");

            //testing baca dari database
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("test list", "list diterima");

//                    HashMap<String, UserWithFirebaseToken> mapTemp = new HashMap<>();
//
//                    int i = 0;
//                    for(DataSnapshot anak: dataSnapshot.getChildren()) {
//                        String hasil = anak.getValue().toString();
//                        //parse
//
//                        String name = String.valueOf(anak.child("name").getValue());
//                        String email = String.valueOf(anak.child("email").getValue());
//                        String firebaseToken = String.valueOf(anak.child("firebaseToken").getValue());
//                        UserWithFirebaseToken user = new UserWithFirebaseToken(name,email,firebaseToken);
//                        Integer indeks = new Integer(i);
//                        mapTemp.put(indeks.toString(), user);
//                        i++;
//                        Log.d("yang dipush: ", "i: "+ i +" yang dipush email: " + email);
//                    }


//                    for(i = 0; i < s.size(); i++) {
//                        List<HashMap<String, Object>> daftar = s.get(i);
//                        for(int j = 0; j < daftar.size(); j++) {
//                            HashMap<String, Object> map1 = daftar.get(i);
//
//                            Integer angka = new Integer(j);
//                            map.put(angka.toString(), map1.get(angka.toString()));
//                        }
//                    }
//                    Integer index = new Integer(i);
//                    mapTemp.put(index.toString(), data);
//                    Log.d("push", "yang dipush email: "+data.getEmail());
//                    reference.setValue(mapTemp);


//                    Log.d("hasil", "hasil retrieve: "+ dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("gagal", "gagal ambil data");
                }
            });


            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("Account", acct);
            startActivity(intent);
            finish();
        } else {
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

}
