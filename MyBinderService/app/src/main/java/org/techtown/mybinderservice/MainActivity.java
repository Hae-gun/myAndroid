package org.techtown.mybinderservice;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.mybinderservice.MyService.MyBinder;
import org.techtown.mybinderservice.VO.SharedObject;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {


    private static final String DEVICE_ID = "Android01";
    private static final String DEVICE_TYPE = "USER";

    private MyService ms;
    boolean isService = false;
    private SharedObject shared = SharedObject.getInstance();
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pr;

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyBinder mb = (MyBinder) service;
            ms = mb.getService();

            isService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isService = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Button startService = findViewById(R.id.startService);
//        Button reqDataFromService= findViewById(R.id.reqDataFromService);
//        Button stopService = findViewById(R.id.stopService);
//        Button killForeground = findViewById(R.id.killForeground);
//        final TextView getData = findViewById(R.id.getDataFromService);

        Intent intent = new Intent(MainActivity.this,MyService.class);
//        unbindService(conn);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);

//        startService.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!isService){
//                    Toast.makeText(getApplicationContext(),"서비스중이 아님.",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                String num = ms.getMsg();
//                shared.put(num);
//                getData.append(num+"\n");
//
//            }
//        });
//
//        reqDataFromService.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ms.changetext("1111111");
//                shared.put("1111111");
//            }
//        });
//
//        stopService.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ms.changetext("2222222");
//                shared.put("2222222");
//            }
//        });
//
//
//        killForeground.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(),MyService.class);
//                if(Build.VERSION.SDK_INT>=26){
//                    ms.stopForegroundService();
//
//                }else{
//                    getApplicationContext().stopService(intent);
//                }
//            }
//        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Intent intent = new Intent(getApplicationContext(),MyService.class);
//        if(Build.VERSION.SDK_INT>=26){
//            getApplicationContext().startForegroundService(intent);
//        }else{
//            getApplicationContext().startService(intent);
//        }
    }

    public static String getDeviceId() {
        return DEVICE_ID;
    }

    public static String getDeviceType() {
        return DEVICE_TYPE;
    }
}
