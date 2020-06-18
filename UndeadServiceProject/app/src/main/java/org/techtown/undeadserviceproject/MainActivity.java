package org.techtown.undeadserviceproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button startServie = findViewById(R.id.startService);

        startServie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MyService.class);
                if(Build.VERSION.SDK_INT>=26){
                    getApplicationContext().startForegroundService(intent);
                }else{
                    getApplicationContext().startService(intent);
                }
            }
        });

        Button stopService = findViewById(R.id.stopService);

        stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyService.class);
                getApplicationContext().stopService(intent);
            }
        });

    }

}
