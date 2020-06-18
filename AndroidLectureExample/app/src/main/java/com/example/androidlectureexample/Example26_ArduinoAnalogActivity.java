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
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

public class Example26_ArduinoAnalogActivity extends AppCompatActivity {
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pr;
    private String msg = "";

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

        public boolean lnCheck() {
            if (list.contains("\n")) {
                return true;
            }
            return false;
        }

        public boolean isEmpty() {
            if (list.isEmpty()) {
                return true;
            }
            return false;
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
        setContentView(R.layout.activity_example26_arduino_analog);
        final TextView reTv = findViewById(R.id._26_resultTv);
        final SeekBar bar = findViewById(R.id._26_SeekBar);
        Button _26_Btn = findViewById(R.id._26_Btn);

        final SharedObject shared = new SharedObject();

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


//                        if (shared.lnCheck()) {
//                            while (shared.isEmpty()) {
                        String msg = shared.pop();
                        // 데이터 보내는 부분.
                        pr.println(msg);
                        pr.flush();
//                            }
//                        }
                    }

                } catch (IOException e) {
                    Log.i("ArduinoTest", e.toString());
                }
            }
        };
        // 2. Thread 객체를 생성한 후 실행.
        Thread t = new Thread(r);
        t.start();

        Getdata2 r2 = new Getdata2(br,reTv);
        Thread t2 = new Thread(r2);
        t2.start();






        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int power = 0;
            private Handler handler;
//            Thread tget = new Thread(new Getdata2(handler,br));

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                result.setText("OnProgress Tracking: " + progress);
                shared.put(Integer.toString(progress));
                power = progress;

//                @SuppressLint("HandlerLeak") Handler handler = new Handler() {
//
//                    @Override
//                    public void handleMessage(@NonNull Message msg) {
//                        super.handleMessage(msg);
//
//                        Bundle bundle = msg.getData();
//
//                        String result = (String) bundle.get("result");
//                        if(result!=null && result!="\n") {
//                            int i = new Integer(result);
//                            if(i>0 && i <101)
//                                reTv.setText(result);
//                        }
//
//                    }
//                };




            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                result.setText("OnStart Tracking");
//                shared.put(""+power);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                result.setText("OnStop Tracking");
//                shared.put(""+power);

            }
        });


    }
}

class Getdata2 implements Runnable {
//    private Handler handler;
    private BufferedReader br;
    private TextView tv;
    Getdata2() {

    }

    Getdata2( BufferedReader br,TextView tv) {
//        this.handler = handler;
        this.br = br;
        this.tv = tv;
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
//                handler.sendMessage(msg);
                Log.i("result",result);

            }
        } catch (IOException e) {
            Log.i("AndroidTest",e.toString()) ;
        }
    }
}