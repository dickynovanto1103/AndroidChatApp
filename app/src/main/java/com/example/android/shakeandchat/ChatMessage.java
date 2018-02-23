package com.example.android.shakeandchat;

/**
 * Created by um on 02/23/18.
 */

public class ChatMessage {

    private String senderID;
    private String message;
    private String destID;
    private String dateTime;
    private String type;

    public ChatMessage(){}

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String id) {
        this.senderID = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDestID() {
        return destID;
    }

    public void setDestID(String id) {
        this.destID = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

}
