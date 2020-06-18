package com.example.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Example15_ServiceLifecycleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example15_service_lifecycle);

        Button _15_01_StartServiceBtn = (Button) findViewById(R.id._15_01_StartServiceBtn);
        Button _15_02_StopServiceBtn = (Button) findViewById(R.id._15_02_StopServiceBtn);

        _15_01_StartServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Service Component 를 시작.
                // Activity 와 유사함.
                Intent i = new Intent(getApplicationContext(),
                        Example15Sub_LifeCycleService.class);
                i.putExtra("MSG", "소리없는 아우성");
                startService(i);
                // onCreate() -> onStratcommnad() 호출
                // 만약 service 객체가 존재하고 있으면
                // onStratCommand() 만 호출
            }
        });
        _15_02_StopServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        Example15Sub_LifeCycleService.class);
                stopService(i);
                // 만약 service 객체가 없으면 생성하고 수행

            }
        });
    }

}
