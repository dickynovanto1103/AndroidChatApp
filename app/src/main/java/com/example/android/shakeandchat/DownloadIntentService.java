package com.example.android.shakeandchat;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dicky on 27/02/18.
 */

public class DownloadIntentService extends IntentService {
    public static final int DOWNLOAD_SUCCESS = 11;
    public static final int DOWNLOAD_ERROR = 10;

    public DownloadIntentService() {
        super(DownloadIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("hi", "halo halo");

        String url = intent.getStringExtra("url");
        Log.d("hi", "url1: "+url);
//        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        downloadImage(url);
    }

    private void downloadImage(String urlString) {
        FileOutputStream fos = null;
        InputStream is = null;
        String message = "Download failed";
        Log.d("pesan", "message: 1"+message);
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = connection.getInputStream();
            String fileName = "gambar.jpg";
            fos = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/"+fileName);
            byte[] buffer = new byte[1024];
            int count;
            while((count = is.read(buffer)) > 0) {
                fos.write(buffer,0,count);
            }
            fos.flush();
            message = "Download completed";
            Log.d("pesan", "message: "+message);
        }catch (Exception e) {
            Log.d("masuk", "masuk sini jir");
            e.printStackTrace();
        }finally {
            if(fos!=null) {
                try {
                    fos.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(is!=null) {
                try {
                    is.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
