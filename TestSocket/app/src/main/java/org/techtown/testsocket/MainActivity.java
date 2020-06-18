package org.techtown.testsocket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {





    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public ComponentName startForegroundService(Intent service) {
        return super.startForegroundService(service);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, MyService.class);
        if(Build.VERSION.SDK_INT>=26){
            getApplicationContext().startForegroundService(intent);
        }
        else{
            getApplicationContext().startService(intent);
        }
    }


}
