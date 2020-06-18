package com.example.androidlectureexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Example11_CounterLogHandlerActivity extends AppCompatActivity {
   // private boolean threadOnOff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // threadOnOff=true;
        setContentView(R.layout.activity_example11_counter_log_handler);

        // TextView 에 대한 reference 를 가져와서
        final TextView tv = (TextView)findViewById(R.id._11_01_sumTv);
        // ProgressBar 에 대한 reference 를 가져와서
        final ProgressBar pb = (ProgressBar)findViewById(R.id._11_04_counterProgress);

        // 데이터를 주고받는 역할을 수행하는 Handler객체를 하나 생성.
        // Handler 객체는 메시지를 전달하는 method와
        // 메시지를 전달받아서 로직처리하는 method 2개를 사용.
        @SuppressLint("HandlerLeak")
        final Handler handler = new Handler(){ // 매서드를 Override 하면서 선언할 수 있음.
            @Override
            public void handleMessage(@NonNull Message msg) { // Handler 가 메시지를 받으면 이 메서드가 실행됨.
                super.handleMessage(msg);
                //받은 메시지를 이용해서 화면처리!
                Bundle bundle = msg.getData();
                String count = bundle.getString("count");
                pb.setProgress(Integer.parseInt(count));
            }
        };

        // 연산시작 버을 클릭했을 때 로직처리하는 Thread 를 생성해서 실행
        Button startBtn = (Button)findViewById(R.id._11_02_startBtn);
        startBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Thread 를 생성.
                // Thread 에게 Activity 와 데이터 통신을 할 수 있는 Handler 객체를 전달해야한다.

                MySumThread runnable = new MySumThread(handler);
                Thread t = new Thread(runnable);
                t.start();



            }
        });
    }
}
// 연산을 담당하는 Thread 를 위한 Runnable interface 구현한 class
class MySumThread implements Runnable{
    private Handler handler;

    public MySumThread(Handler handler){
        this.handler = handler;
    }

    @Override
    public void run() {
        // 로직처리
        // 숫자를 더해가면서 progressbar를 진행시킨다.
        long sum = 0;
        for (long i = 0; i <= 1000000000L; i++) {
            sum += i;
            if (i % 10000000 == 0) {
                //Long loop = i / 100000000;  // autoboxing
                long loop = i / 10000000;
                //pb.setProgress((int) loop);
                // msg 를 만들어서 handler 를 이용해 Activity 에게 msg 를 전달.
                // Bundle 객체를 하나 만들어야 한다. Map 구조.
                Bundle bundle = new Bundle();
                bundle.putString("count",String.valueOf(loop));
                // 이 Bundle 을 가질 수 있는 Message 객체를 생성.
                Message msg = new Message();
                msg.setData(bundle);
                handler.sendMessage(msg); // msg 전송
            }
        }
        //tv.setText("합계는 : " + sum);

    }
}
