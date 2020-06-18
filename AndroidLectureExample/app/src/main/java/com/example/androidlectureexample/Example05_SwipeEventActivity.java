package com.example.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Example05_SwipeEventActivity extends AppCompatActivity {

    double x1, x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example05_swipe_event);

        LinearLayout myLayout = (LinearLayout) findViewById(R.id._05_01_myLinearLayout);

        myLayout.setOnTouchListener(new View.OnTouchListener() { // Touch에 대한 이벤트 처리. 이벤트 종류에 따라 method 변경.
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                String msg = ""; //지연변수
                //getAction() 매서드
                // 누를때 인지 땔때 인지 확인가능. 상수값.
                // 누를때 : MotionEvent.ACTION_DOWN
                // 땔때 : MotionEvent.ACTION_UP
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    x1 = event.getX(); // 누르는 Touch일 경우, 그때의 X좌표
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    x2 = event.getX(); // 때는 Touch일 경우, 그때의 X좌표
                    if (x1 < x2) {
                        msg = ">>>>>>>";
                    } else {
                        msg = "<<<<<<<";
                    }
                    Toast.makeText(Example05_SwipeEventActivity.this, msg, Toast.LENGTH_SHORT).show(); // 컨텍스트는 외부 Activity가 되어야함.
                }
                return true; // 이벤트 처리가 정상처리면 true. 이벤트 처리가 없으면 false로 리턴
            }
        });

    }


}
