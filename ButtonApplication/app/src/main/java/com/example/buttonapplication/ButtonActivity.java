package com.example.buttonapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.EventListener;

public class ButtonActivity extends AppCompatActivity {

// Event : 사용자에 의해서 발생되는 혹은 시스템에 의해서 발생되는 모든것.
//      * Event Handling을 해야 한다.
//      * Java는 Event delegation Model을 이용해서 event를 처리
//      * Event 처리에 관련된 3가지 객체
// 1. Event Source객체 : Event가 발생한 객체.
// 2. Event Handler객체(Listener) :  Event를 처리하는 객체.
// 3. Event 객체 : 발생된 Event에 대한 세부정보를 가지고 있는 객체.
// Event Source에 Event Handler를 부착시켜서 Event가 발생되면 부착된 Handler를 통해서 Event를 처리.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
        // 0. TextView 객체에 대한 reference를 얻어오기.
        final TextView tv = (TextView)findViewById(R.id.myText);

        // 1. Event Source 객체 얻어오기. (id를 이용)
        Button myBtn = (Button)findViewById(R.id.eventBtn); // return type = object. downcasting 해줌.(jdk 버전이 오르면서 필요없어지긴 함.)
        // 2. Event Source에 Event Handler 객체생성,부착.
        myBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                tv.setText("이것은 소리없는 아우성");
            }
        });
    }
}


// 열심히 Event Handler class를 만들었는데..
// 실제 구현에서는 anonymous inner class(익명 이너클래스)를 이용하기 때문에
// 아래와 같은 외부 class는 사용하지 않는다.

// 우리가 원하는 것은 일반 클래스의 객체가 아니라
// 이벤트를 처리할 수 있는 특수한 능력을 갖고 있는 리스너 객체가 필요
// 다시말하면, 우리가 작성하는 class는 특수한 interface를 구현한 클래스가 되어야 한다.
// 특수한 interface는 여러개가 존재한다. (이벤트 종류에 따라서 여러개가 존재한다.)

/*class MyEventHandler implements View.OnClickListener {
    private  TextView tv;

    MyEventHandler(){

    }

    MyEventHandler(TextView tv){
        this.tv = tv;
    }

    @Override
    public void onClick(View v) { // View v : 이벤트 객체.
        // 여기서 이벤트 처리코드를 작성
        //Log.i("MyEvent","버튼이 눌러졌어요!!");
        tv.setText("이것은 소리없는 아우성");
        //tv.setTextColor(Color.parseColor("#ff0000"));
    }
}*/
