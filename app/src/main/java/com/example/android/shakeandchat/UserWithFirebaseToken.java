package com.example.android.shakeandchat;

/**
 * Created by dicky on 22/02/18.
 */

public class UserWithFirebaseToken {
    private String name;
    private String email;
    private String firebaseToken;

    public UserWithFirebaseToken() {

    }

    public UserWithFirebaseToken(String name, String email, String firebaseToken){
        this.name = name;
        this.email = email;
        this.firebaseToken = firebaseToken;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }
}
