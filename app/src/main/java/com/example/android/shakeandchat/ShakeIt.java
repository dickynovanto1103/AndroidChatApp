package com.example.android.shakeandchat;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.android.shakeandchat.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShakeIt extends AppCompatActivity {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private TextView mStatusText;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_it);

        final Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        mStatusText = (TextView) findViewById(R.id.status_shake);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                if (count > 0){
                    findViewById(R.id.shake_logo).startAnimation(shake);
                    mStatusText.setText(R.string.shaking_status);
                } else {
                    findViewById(R.id.shake_logo).clearAnimation();
                    mStatusText.setText(R.string.waiting_shake_status);
                }
            }
        });

        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference("user");

        Log.d("FirebaseDB: ", mDBRef.toString());
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
