package com.example.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

public class Example04_TouchEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example04__touch_event);
    }

    //onClick에서 view v는 클릭한 객체가 들어옴.
    // onTouchEvent에서는 Touch한 객체의 정보가 들어온다. (터치한 좌표 등.)

    //    private float preX,X,chk;
//    private String text;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Toast message를 이용해보자.


//        if(event.getAction()==MotionEvent.ACTION_DOWN){
//            preX = event.getX();
//        }else if(event.getAction()==MotionEvent.ACTION_UP){
//            X = event.getX();
//        }
//        chk = preX-X;
//
//        if(chk<0){ text = ">>>>>>>>>>>>";}
//        else if(chk>0){ text = "<<<<<<<<<<<<";}
//
//        Toast.makeText(this,text,
//                Toast.LENGTH_SHORT).show();

        Toast.makeText(this, "ㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ",
                Toast.LENGTH_SHORT).show(); // static method (Activity(컨택스트)객체, Character Sequence, 지속시간)
        // 지속시간은 Toast 객체가 상수로 갖고 있다. LENGTH_SHORT, LENGTH_LONG
        // 컨텍스트는 Activity가 갖고 있음. is a 관계.
        // show() 매서드를 통해 화면에 띄움!
        Log.i("mytest", "터치했다.");
        return super.onTouchEvent(event);
    }

}
