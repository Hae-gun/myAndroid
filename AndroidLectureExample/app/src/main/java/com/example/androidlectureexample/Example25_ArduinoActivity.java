package com.example.androidlectureexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

public class Example25_ArduinoActivity extends AppCompatActivity {

    private TextView _25_statusTv;
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pr;

    // 공용객체를 위해 생성자 소비자 패턴을 이용!
    // LinkedList Thread 에서는 LinkedList 의 pop 매서드를 계속 수행하지만
    // 안에 데이터가 없으면 해당 메서드에 Lock을 걸어주어 put 매서드가 수행되기 전까지 대기시킴.

    class SharedObject {
        private Object MONITOR = new Object();
        private LinkedList<String> list = new LinkedList<String>();

        SharedObject() {
        } // 생성자

        public void put(String s) {
            synchronized (MONITOR) {
                list.addLast(s);
                Log.i("ArduinoTest", "공용객체에 데이터 입력");
                // 리스트 안에 문자열이 없어 대기하던 pop 매서드를 꺠워서 실행시킨다.
                MONITOR.notify();
            }
        }

        public String pop() {
            String result = "";

            synchronized (MONITOR) {
                if (list.isEmpty()) {
                    // 리스트 안에 문자열이 없으니까 일시 대기해야 한다.
                    try {
                        MONITOR.wait();
                        // 큐 구조에서 가져옴
                        result = list.removeFirst();
                    } catch (Exception e) {
                        Log.i("ArduinoTest", e.toString());
                    }
                } else {
                    result = list.removeFirst();
                    Log.i("ArduinoTest", "공용객체에서 데이터 추출");
                }
            }
            return result;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example25_arduino);

        _25_statusTv = (TextView) findViewById(R.id._25_statusTv);

        // 공용객체 만들기
        final SharedObject shared = new SharedObject();

        Button _25_ledOnBtn = (Button) findViewById(R.id._25_ledOnBtn);
        Button _25_ledOffBtn = (Button) findViewById(R.id._25_ledOffBtn);

        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {

            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                Bundle bundle = msg.getData();

                String result = (String) bundle.get("result");
                if(result!=null) {
                    _25_statusTv.setText(result);
                }

            }
        };




        // Java Network Server 에 접속
        // Activity(UI Thread) 에서 Network 처리코드를 사용할 수 없다.
        // 별도의 Thread를 이용해서 처리해야 한다.
        // 1. Runnable 객체를 만든다
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("70.12.60.97", 1234);
                    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    pr = new PrintWriter(socket.getOutputStream());
                    Log.i("ArduinoTest", "서버에 접속 성공!");
                    while (true) {
                        // 공용객체의 메세지를 계속 받기!
                        String msg = shared.pop();
                        // 데이터 보내는 부분.
                        pr.println(msg);
                        pr.flush();
                    }

                } catch (IOException e) {
                    Log.i("ArduinoTest", e.toString());
                }
            }
        };
        // 2. Thread 객체를 생성한 후 실행.
        Thread t = new Thread(r);
        t.start();

        _25_ledOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thread 가 사용하는 공용객체를 이용해서 메세지를 보낼것.
                // 공용객체에 데이터를 입력
                shared.put("LED_ON");
                Thread tget = new Thread(new Getdata(handler,br));
                tget.start();
            }
        });

        _25_ledOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shared.put("LED_OFF");
                Thread tget = new Thread(new Getdata(handler,br));
                tget.start();
            }
        });


    }
}


class Getdata implements Runnable {
    private Handler handler;
    private BufferedReader br;

    Getdata() {

    }

    Getdata(Handler handler, BufferedReader br) {
        this.handler = handler;
        this.br = br;
    }

    @Override
    public void run() {
        String result;
        Bundle bundle = new Bundle();
        try {
            while((result = br.readLine())!=null) {
                Log.i("AndroidTest",result);


                    bundle.putString("result", result);



                Message msg = new Message();
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        } catch (IOException e) {
            Log.i("AndroidTest",e.toString()) ;
        }
    }
}