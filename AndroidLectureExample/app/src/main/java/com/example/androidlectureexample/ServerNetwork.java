package com.example.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerNetwork extends AppCompatActivity {
    String msg;
    Thread t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_network);

        Button On = (Button)findViewById(R.id.OnBtn);
        Button Off = (Button)findViewById(R.id.OffBtn);

        On.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = "O";
                t = new Thread(new myRunnable(msg));
                t.start();
            }
        });

        Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = "X";
                t = new Thread(new myRunnable(msg));
                t.start();
            }
        });



    }
}

class myRunnable implements Runnable{
    String msg;
    Thread t;

    myRunnable(String msg){
        this.msg = msg;
        }
    @Override
    public void run() {
        t = new Thread(new NetworkStart(msg));
        t.start();



    }

}
class NetworkStart implements Runnable{
    Socket s;
    BufferedReader br;
    PrintWriter pr;
    String msg;
    NetworkStart(String msg){
        this.msg = msg;
        try {
            s = new Socket("70.12.60.97", 7534);
            Log.i("Rresult","Thread 생성");
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            pr = new PrintWriter(s.getOutputStream());
        }catch (Exception e){
            Log.i("Rresult",e.toString());
        }
    }


    @Override
    public void run() {
            try {
                pr.println(msg);
                pr.flush();
                Log.i("Rresult",msg);

                Thread.sleep(1000);
            } catch (Exception e) {
            }
    }

}




