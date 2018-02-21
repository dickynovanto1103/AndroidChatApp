package com.example.android.shakeandchat;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by um on 02/20/18.
 */

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    private TextView textView_nama;
    private TextView textView_email;
    private ImageView imageView_photo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        Log.d(TAG, "onCreateView_Profile");

        textView_nama = view.findViewById(R.id.dp_nama);
        textView_email = view.findViewById(R.id.dp_email);
        imageView_photo = view.findViewById(R.id.photo_profile);

        return view;
    }

    public void setProfile(String name, String email, String photoUrl) {
        Log.d(TAG, "Masuk setNameMail");
        textView_nama.setText(name);
        textView_email.setText(email);

        int radius = 30;
        int margin = 0;
        Glide.with(this).load(photoUrl)
            .thumbnail(0.5f)
            .crossFade()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .bitmapTransform(new RoundedCornersTransformation(getContext(), radius, margin))
            .into(imageView_photo);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
