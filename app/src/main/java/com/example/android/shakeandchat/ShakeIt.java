package com.example.android.shakeandchat;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShakeIt extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private TextView mStatusText;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST_PERMISSION = 99;
    private boolean firstShake;
    private Location myLocation;

    private void findingFriends(){
        final Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        findViewById(R.id.shake_logo).startAnimation(shake);
        mStatusText.setText(R.string.shaking_status);

        if (firstShake){
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        myLocation = location;
                        Log.d("Location: ", location.toString());
                    }
                }
            });
        }
    }

    private void stopFindFriends(){
        findViewById(R.id.shake_logo).clearAnimation();
        mStatusText.setText(R.string.waiting_shake_status);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_it);

        mStatusText = (TextView) findViewById(R.id.status_shake);
        firstShake = true;

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                if (count > 0){
                    findingFriends();
                    firstShake = false;
                } else {
                    stopFindFriends();
                    firstShake = true;
                }
            }
        });

        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference("user");
        Log.d("FirebaseDB: ", mDBRef.toString());


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_PERMISSION);
        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        if (requestCode == LOCATION_REQUEST_PERMISSION){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause(){
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
}
