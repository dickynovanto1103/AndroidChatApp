package com.example.android.shakeandchat;

/**
 * Created by Pratama Agung on 2/21/2018.
 */

public class User {
    public String username;
    public String key;
    public double latitude;
    public double longitude;

    public User(){

    }

    public User(String username, String key, double latitude, double longitude){
        this.username = username;
        this.key = key;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
