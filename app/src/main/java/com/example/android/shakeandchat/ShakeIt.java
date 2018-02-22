package com.example.android.shakeandchat;

import android.Manifest;
import android.content.Context;
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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShakeIt extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private TextView mStatusText;
    private DatabaseReference mDBActiveUser;
    private DatabaseReference mDBFriendList;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST_PERMISSION = 99;
    private boolean isShaking;
    private Location myLocation;
    private GoogleSignInAccount account;
    private ArrayList<ActiveUser> foundUser;
    private FriendAdapter friendAdapter;

    private void writeActiveUser(String name, String email, String displayImage) {
        ActiveUser activeUser = new ActiveUser(name, email, displayImage, myLocation.getLatitude(), myLocation.getLongitude());
        mDBActiveUser.child(name).setValue(activeUser);
    }

    private void removeInactiveUser(String name) {
        mDBActiveUser.child(name).removeValue();
    }

    private void findingFriends() {
        mStatusText.setText(R.string.shaking_status);

        if (!isShaking) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        myLocation = location;
                        Log.d("Location: ", location.toString());
                        String displayImage = account.getPhotoUrl().toString();
                        if (displayImage == null) displayImage = "default";
                        writeActiveUser(account.getDisplayName(), account.getEmail(), displayImage);
                    }
                }
            });
            final Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            findViewById(R.id.shake_logo).startAnimation(shake);
        }
    }

    private void stopFindFriends(){
        removeInactiveUser(account.getDisplayName());
        findViewById(R.id.shake_logo).clearAnimation();
        setDisplayFoundFriend(false);
        mStatusText.setText(R.string.waiting_shake_status);
    }

    private void setupSensor(){
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                if (count > 0){
                    findingFriends();
                    isShaking = true;
                } else {
                    stopFindFriends();
                    isShaking = false;
                }
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_PERMISSION);
        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        }
    }

    private void setupDB(){
        mDBActiveUser = FirebaseDatabase.getInstance().getReference("active_users");
        ChildEventListener userListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("shaking ", (Boolean.toString(isShaking)));
                ActiveUser addedUser = dataSnapshot.getValue(ActiveUser.class);
                if (addedUser.username != account.getDisplayName()){
                    foundUser.add(addedUser);
                    friendAdapter.notifyDataSetChanged();
                }
                if(foundUser.size() >= 1 && isShaking) setDisplayFoundFriend(true);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("onChildChanged: ", dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ActiveUser removedUser = dataSnapshot.getValue(ActiveUser.class);
                boolean found = false;
                int i = 0;
                while(!found && i < foundUser.size()){
                    if(foundUser.get(i).username == removedUser.username) {
                        foundUser.remove(i);
                        found = true;
                    }
                    i++;
                }
                if (found) friendAdapter.notifyDataSetChanged();
                if (foundUser.isEmpty()) setDisplayFoundFriend(false);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("onChildMoved: ", dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Error: ", "postComments:onCancelled", databaseError.toException());
            }
        };
        mDBActiveUser.addChildEventListener(userListener);

        mDBFriendList = FirebaseDatabase.getInstance().getReference("friendList/" + account.getId());
    }

    private void setDisplayFoundFriend(boolean view){
        if (view){
            findViewById(R.id.found_friend).setVisibility(View.VISIBLE);
            findViewById(R.id.shake_logo).clearAnimation();
            findViewById(R.id.shake_logo).setVisibility(View.GONE);
            findViewById(R.id.status_shake).setVisibility(View.GONE);
        } else {
            findViewById(R.id.found_friend).setVisibility(View.GONE);
            findViewById(R.id.shake_logo).setVisibility(View.VISIBLE);
            findViewById(R.id.status_shake).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_it);

        mStatusText = (TextView) findViewById(R.id.status_shake);
        isShaking = false;

        account = getIntent().getParcelableExtra("Account");
        setupSensor();
        setupDB();

        foundUser = new ArrayList<ActiveUser>();
        friendAdapter = new FriendAdapter(this, foundUser);
        ListView listView = (ListView) findViewById(R.id.found_friend);
        listView.setAdapter(friendAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ActiveUser clickedUser = foundUser.get(i);
                mDBFriendList.push().setValue(new FriendUser(clickedUser.username,
                        clickedUser.email, clickedUser.displayImage));
                if(isShaking) removeInactiveUser(account.getDisplayName());
                finish();
            }
        });
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
