package org.techtown.testsocket;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;

public class MyService extends Service {

    private String serviceName="Myservice";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("WhatIDo","in onStartComnad");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        int NOTIFICATION_ID = (int)(System.currentTimeMillis()%10000);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Notification noti=new Notification()
            startForeground(NOTIFICATION_ID, (new Notification()).Builder(this).build());
        }
    }
}
