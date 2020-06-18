package org.techtown.mybinderservice;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;

import org.techtown.mybinderservice.VO.SharedObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class MyService extends Service {

    private Thread t;

    private Socket socket;
    private BufferedReader br;
    private PrintWriter pr;

    private final String host = "70.12.60.97";
    private final int port = 55566;
    private SharedObject shared = SharedObject.getInstance();
    IBinder mBinder = new MyBinder();
    private String msg = "안바뀜";

    class MyBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    int getRan() {
        return new Random().nextInt();
    }

    public void changetext(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();

        Thread connectSocket = new Thread(() -> {
//            while (true) {
//                if (socket!=null&&socket.isClosed())
                    connectServer();
//            }
        });
        connectSocket.start();
        t = new Thread(() -> {
            while (true) {
                Log.i("MyTest", "Service is alive");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        t.start();
        startForegroundService();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void startForegroundService() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_service);

        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "snwodeer_service_channel";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "SnowDeer Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                    .createNotificationChannel(channel);

            builder = new Notification.Builder(this, CHANNEL_ID);
        } else {
            builder = new Notification.Builder(this);
        }
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContent(remoteViews)
                .setContentIntent(pendingIntent);

        startForeground(1, builder.build());
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.N)
    void stopForegroundService() {
        t.interrupt();
        stopForeground(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        t.interrupt();
    }

    public BufferedReader getBr() {
        return br;
    }

    public PrintWriter getPr() {
        return pr;
    }

    private void connectServer() {
        try {
            socket = new Socket(host, port);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pr = new PrintWriter(socket.getOutputStream());
            Thread getData = new Thread(()->{
                String msg = "";
                while(true){
                    try {
                        if((msg=br.readLine())!=null){
                            Log.i("EchoMsg",msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            getData.start();
            Thread sendData = new Thread(()->{
                while(true){
                    String sendMsg =shared.pop();
                    Log.i("sendData",sendMsg);
                    pr.println(sendMsg);
                    pr.flush();
                    Log.i("sendData",sendMsg);
                }
            });
            sendData.start();
        } catch (IOException e) {
            Log.i("Test",e.toString());
        }
    }
}
