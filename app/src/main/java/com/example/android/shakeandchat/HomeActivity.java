package com.example.android.shakeandchat;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.lang.UCharacter;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, SensorEventListener {

    private static final String TAG = "HomeActivity";

    private SectionsPageAdapter mSectionsPageAdapter;
    public Toolbar mToolbar;
    private ViewPager mViewPager;
    public TabLayout tabLayout;
    private Button mButtonAddFriend;
    private int[] tabIcons_w = {
            R.drawable.icon_friends_w,
            R.drawable.icon_chats_w,
            R.drawable.icon_profile_w
    };

    GoogleSignInAccount account;
    SectionsPageAdapter adapter;
    GoogleApiClient mGoogleApiClient;

    SensorManager sensorManager;
    Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d(TAG, "onCreate: Bismillah");

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        mButtonAddFriend = findViewById(R.id.addFriend);
        mToolbar = findViewById(R.id.toolbar);
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        setupTabIcons();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

        account = getIntent().getParcelableExtra("Account");

        Button addFriend = (Button) findViewById(R.id.addFriend);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(view.getContext(), ShakeIt.class);
                intent.putExtra("Account", account);
                startActivity(intent);
            }
        });

        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons_w[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons_w[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons_w[2]);
    }

    private void setupProfile() {
        ProfileFragment prof_frag = (ProfileFragment) adapter.getItem(2);

        String acc_name = account.getDisplayName();
        String acc_email = account.getEmail();
        String photoUrl;
        Uri checkPhoto = account.getPhotoUrl();
        if (checkPhoto != null) {
            photoUrl = checkPhoto.toString();
        } else {
            photoUrl = "default";
        }
        Log.d(TAG, "photoURL: " + photoUrl + ", Nama: " + acc_name + ", Email: " + acc_email);

        prof_frag.setProfile(acc_name, acc_email, photoUrl);

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new FriendsFragment(), "Friends");
        adapter.addFragment(new ChatsFragment(), "Chats");
        adapter.addFragment(new ProfileFragment(), "Profile");

        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled-position: " + position);
                Log.d(TAG, "onPageScrolled-positionOffset: " + positionOffset);
                Log.d(TAG, "onPageScrolled-positionOffsetPixels: " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: " + position);
                switch (position) {
                    case 0:
                        mToolbar.setTitle("Friends");
                        mButtonAddFriend.setVisibility(View.VISIBLE);
                        tabLayout.getTabAt(0).setIcon(R.drawable.icon_friends);
                        tabLayout.getTabAt(1).setIcon(R.drawable.icon_chats_w);
                        tabLayout.getTabAt(2).setIcon(R.drawable.icon_profile_w);
                        break;
                    case 1:
                        mToolbar.setTitle("Chats");
                        mButtonAddFriend.setVisibility(View.INVISIBLE);
                        tabLayout.getTabAt(0).setIcon(R.drawable.icon_friends_w);
                        tabLayout.getTabAt(1).setIcon(R.drawable.icon_chats);
                        tabLayout.getTabAt(2).setIcon(R.drawable.icon_profile_w);
                        break;
                    case 2:
                        mToolbar.setTitle("Profile");
                        mButtonAddFriend.setVisibility(View.INVISIBLE);
                        tabLayout.getTabAt(0).setIcon(R.drawable.icon_friends_w);
                        tabLayout.getTabAt(1).setIcon(R.drawable.icon_chats_w);
                        tabLayout.getTabAt(2).setIcon(R.drawable.icon_profile);
                        setupProfile();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG, "onPageScrollStateChanged: " + state);
            }
        });
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void setupFriendList() {
        FriendsFragment friend_frag = (FriendsFragment) adapter.getItem(0);
        friend_frag.setFriendList(account);
//        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResumeHomeActivity");
        setupFriendList();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            Log.d(TAG, String.valueOf(sensorEvent.values[0]));
            View view = findViewById(R.id.background_view);
            if (sensorEvent.values[0] < 15) {
                Log.d(TAG, "Jadi gelap");
                view.setAlpha(1f);
            } else {
                Log.d(TAG, "Jadi terang");
                view.setAlpha(0f);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
