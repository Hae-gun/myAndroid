package com.example.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Example03_Event2Activity extends AppCompatActivity {

    private ImageView iv; //Field 변수. heap 영역에 저장됨.
    Boolean flag=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example03_event); // 상수화 하여 stack이 아닌 heap영역에 저장. 또는 필드로 지정.

        //final ImageView iv = (ImageView)findViewById(R.id._03_01_iv);
        iv = (ImageView)findViewById(R.id._03_01_iv);

        // Button에 대한 event처리를 해야 한다.
        Button btn = (Button)findViewById(R.id._03_02_imageChangeBtn);
        // findViewById앞에 this.이 생략되있음. 현재 Activity에서 찾음. 다른 Activity로 지정하면 다른 Activity에서 찾는다.
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(flag) {
                    iv.setImageResource(R.drawable.dog1);
                    // 이미지 변경 method. 매개변수 : 상수값.(이미지resource)
                }else{
                    iv.setImageResource(R.drawable.dog2);
                }
                flag=!flag;
            }
        });

    }

}
