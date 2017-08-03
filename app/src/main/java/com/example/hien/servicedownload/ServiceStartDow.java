package com.example.hien.servicedownload;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by hien on 8/2/17.
 */

public class ServiceStartDow extends Service {

    private static final String TAG = ServiceStartDow.class.getSimpleName();
    private String link;


    @Override
    public void onCreate() {

        super.onCreate();


        Log.d(TAG, "___onCreate!!!");



    }

    @Override
    public int onStartCommand(Intent intent,
                              int flags, int startId) {

        Log.d(TAG, "___onStartCommand!!!");


        Bundle bundle = intent.getExtras();

        if(bundle != null){
            link = bundle.getString("link");

        }

        Log.d(TAG, "link: " + link);


        DownloadImage downloadImage = new DownloadImage();

        downloadImage.execute(link);

        return START_NOT_STICKY;


    }

    private class DownloadImage extends AsyncTask<String, Void, String>{


        private String  path = Environment.getExternalStorageDirectory() + "/image.jpg";


        @Override
        protected String doInBackground(String... strings) {
            try {
                File file = new File(path);

                file.createNewFile();
                URL url = new URL(link);
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                OutputStream out = new FileOutputStream(file);

                byte[] bytes = new byte[1024];

                int length = inputStream.read(bytes);

                while (length >= 0){

                    out.write(bytes, 0, length);

                    length = inputStream.read(bytes);
                }

                inputStream.close();

                out.close();

                Log.d(TAG, "Download thành công!!!");

                return path;

            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Download không thành công!!!");
            return null;
        }


        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            Log.d(TAG, "Path: " + s);

            sendIntentBroadcast(s);


        }
    }



    private void sendIntentBroadcast(String path_file){

        Intent intent = new Intent();

        intent.setAction(Cons.ACTION_BROADCAST);

        intent.putExtra("path", path_file);

        sendBroadcast(intent);
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {

        Log.d(TAG, "onDestroy Service....");
        super.onDestroy();
    }


}
