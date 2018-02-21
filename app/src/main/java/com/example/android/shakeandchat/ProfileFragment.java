package com.example.android.shakeandchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by um on 02/20/18.
 */

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    private TextView textView_nama;
    private TextView textView_email;
    private ImageView imageView_photo;
    private Button signOutButton;
    private GoogleApiClient mGoogleApiClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        Log.d(TAG, "onCreateView_Profile");

        textView_nama = view.findViewById(R.id.dp_nama);
        textView_email = view.findViewById(R.id.dp_email);
        imageView_photo = view.findViewById(R.id.photo_profile);

        signOutButton = (Button) view.findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Sign out dipencet");
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mGoogleApiClient = ((HomeActivity) this.getActivity()).getGoogleApiClient();
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
