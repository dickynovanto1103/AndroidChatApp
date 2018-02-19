package com.example.android.shakeandchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.android.shakeandchat.R;

public class ShakeIt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_it);

        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        findViewById(R.id.shake_logo).startAnimation(shake);
    }
}
