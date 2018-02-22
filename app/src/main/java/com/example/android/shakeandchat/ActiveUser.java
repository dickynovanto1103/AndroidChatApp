package com.example.android.shakeandchat;

/**
 * Created by Pratama Agung on 2/21/2018.
 */

public class ActiveUser {
    public String username;
    public String email;
    public String displayImage;
    public double latitude;
    public double longitude;

    public ActiveUser(){

    }

    public ActiveUser(String username, String email, String displayImage, double latitude, double longitude){
        this.username = username;
        this.email = email;
        this.displayImage = displayImage;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
