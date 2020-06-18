package org.techtown.latteroomfinalproject;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;


public class RoomInfoFragment extends Fragment {

    private BufferedReader br;
    private PrintWriter pr;
    private SharedObject shared = SharedObject.getInstance();
    private Handler roomInfohandler;

    public RoomInfoFragment(){

    }

    public RoomInfoFragment(BufferedReader br, PrintWriter pr) {
        this.br = br;
        this.pr = pr;
    }

    private Thread getData = new Thread(()->{

        try {
            String data = "";
            while((data = br.readLine())!=null){

            }
        }catch (IOException e){
            Log.i("getDataThread",e.toString());
        }

    });
    @Override
    public void onStart() {
        super.onStart();

    }

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Thread sendData = new Thread(()->{
            while(true){
                String msg = shared.pop();
                pr.println(msg);
                pr.flush();
            }

        });
        sendData.start();
        shared.put("fragment Onstart");

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_room_info, container, false);
        final TextView fromServerData1 = rootView.findViewById(R.id.fromServerData1);
        final TextView fromServerData2 = rootView.findViewById(R.id.fromServerData2);
        final TextView fromServerData3 = rootView.findViewById(R.id.fromServerData3);
        EditText sendToServerEdit = rootView.findViewById(R.id.sendToServerEdit);
        Button sendToServerBtn = rootView.findViewById(R.id.sendToServerBtn);

//        roomInfohandler = new Handler(){
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                super.handleMessage(msg);
//
//
//            }
//        };


        // Inflate the layout for this fragment
        return rootView;
    }



}
