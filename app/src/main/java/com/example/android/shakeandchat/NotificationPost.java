package com.example.android.shakeandchat;

import android.os.AsyncTask;
import android.util.Log;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Pratama Agung on 2/27/2018.
 */

public class NotificationPost extends AsyncTask {

    private String sender;
    private String message;
    private String dest;

    public NotificationPost(String sender, String message, String dest) {
        this.sender = sender;
        this.message = message;
        this.dest = dest;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            String data = URLEncoder.encode("sender", "UTF-8")
                    + "=" + URLEncoder.encode(sender, "UTF-8");

            data += "&" + URLEncoder.encode("message", "UTF-8") + "="
                    + URLEncoder.encode(message, "UTF-8");

            data += "&" + URLEncoder.encode("dest", "UTF-8")
                    + "=" + URLEncoder.encode(dest, "UTF-8");

            URL url = new URL("https://vast-crag-97532.herokuapp.com/service/send-notif?" + data);

            // Send POST data request

            Log.d("Send Data", data);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();
            Log.d("Response", conn.getResponseMessage());
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            return null;
        }
    }
}
