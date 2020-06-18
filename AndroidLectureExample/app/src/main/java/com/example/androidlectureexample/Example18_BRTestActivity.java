package com.example.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Example18_BRTestActivity extends AppCompatActivity {

    /*
     * Android 에서는 Broadcast 라는 signal 이 존재한다.
     * 이 신호(signal)운 Android system 자체에서 발생할 수도 있고
     * 사용자 App 에서 임의로 발생시킬 수도 있다.
     *
     * 여러개의 일반적으로 정해져있는 Broadcast message 를 받고 싶다면
     * Broadcast Receiver 를 만들어서 등록해야 한다.
     *
     * 등록방법(2가지)
     *  1. AndroidManifext.xml file 에 명시.
     *  2. 코드상에서 Receiver 를 만들어서 등록
     * */
    private BroadcastReceiver bReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example18_br_test);

        Button _18_01_br_registerBtn = findViewById(R.id._18_01_br_registerBtn);
        _18_01_br_registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Broadcast Receiver 등록버튼 click 시 호출!
                // Broadcast Receiver 객체를 만들어서 IntentFilter 와 함께
                // 시스템에 등록!
                // 1. IntentFilter 부터 생성
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("MY_BROADCAST_SIGNAL"); // 어떤 신호를 받은 것인지 명시. Action 이름

                // 2. Broadcast Receiver 객체를 생성 (Field 변수로)
                bReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        // 이 Receiver 가 신호를 받게되면 이 부분이 호출된다.
                        // 로직처리를 여기에서 해야한다.
                        if (intent.getAction().equals("MY_BROADCAST_SIGNAL")) {
                            Toast.makeText(getApplicationContext(),
                                    "신호가 왔어요!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                // 3. Filter 와 함께 Broadcast Receiver 를 등록한다.
                registerReceiver(bReceiver, intentFilter); // intentFilter 로 걸러진 신호만 Receiver 가 받는다.


            }
        });

        // 등록 해제버튼을 눌렀을 때
        Button _18_02_br_unRegisterBtn = findViewById(R.id._18_02_br_unRegisterBtn);
        _18_02_br_unRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unregisterReceiver(bReceiver); // 해당 Receiver 를 해제. 신호가 발생해도 Receiver 가 받을수 없게됨.
            }
        });

        // Broadcast 를 발생시키는 버튼.
        Button _18_03_sendBroadcastBtn = findViewById(R.id._18_03_sendBroadcastBtn);
        _18_03_sendBroadcastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼을 누르면 Broadcast 를 임의로 발생!
                Intent i = new Intent("MY_BROADCAST_SIGNAL");
                // 신호를 발생시키는 것은 intent 를 갖고 신호를 발생시킨다. Intent 는 Action 을 부여하면서 생성가능함.
                sendBroadcast(i);
            }
        });
    }
}
