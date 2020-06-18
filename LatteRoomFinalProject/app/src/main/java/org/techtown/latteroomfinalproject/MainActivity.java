package org.techtown.latteroomfinalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private static final String host = "70.12.60.97";
    private static int port = 55566;

    private Socket socket;
    private BufferedReader br;
    private PrintWriter pr;

    private RoomInfoFragment roomInfoFragment;

    @Override
    protected void onStart() {
        super.onStart();
        Thread connect = new Thread(
                () -> {
                    serverConnection();
                });
        connect.start();

        Log.i("check",""+(roomInfoFragment==null));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        FragmentManager manager =
//        Log.i("check",""+(manager==null));

//        try {

            getSupportFragmentManager().beginTransaction().replace(R.id.container, roomInfoFragment).commit();
//        }catch (Exception e){
//
//        Log.i("check",e.toString());
//        }

    }

    public void serverConnection() {
        try {
            socket = new Socket(host, port);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pr = new PrintWriter(socket.getOutputStream());
            roomInfoFragment = new RoomInfoFragment(br, pr);
        } catch (IOException e) {
            Log.i("IOException", e.toString());
        }
    }

}
