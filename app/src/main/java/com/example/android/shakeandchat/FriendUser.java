package com.example.android.shakeandchat;

import java.io.Serializable;

/**
 * Created by um on 02/22/18.
 */

public class FriendUser implements Serializable {
    public String name;
    public String email;
    public String photoURL;

    public FriendUser() {}

    public FriendUser(String name, String email, String photoURL) {
        this.name = name;
        this.email = email;
        this.photoURL = photoURL;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
