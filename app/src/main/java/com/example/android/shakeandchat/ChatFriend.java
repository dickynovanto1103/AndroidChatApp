package com.example.android.shakeandchat;

import java.io.Serializable;

/**
 * Created by um on 02/23/18.
 */

class ChatFriend implements Serializable {

    public String name;
    public String photoURL;
    public String timeStamp;
    public String lastMessage;
    public boolean isOpen;

    public ChatFriend(String name, String photoURL, String timeStamp, String lastMessage, boolean isOpen) {
        this.name = name;
        this.photoURL = photoURL;
        this.timeStamp = timeStamp;
        this.lastMessage = lastMessage;
        this.isOpen = isOpen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }


}
