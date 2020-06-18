package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    // Activity의 life cycle
    // Activity는 사용자에 의해서 이벤트가 발생되면 그 상태가 변해요!
    // 그에 따라 call back method가 호출되는데 그 callback에 대해서 알아둬야 해요!

    //  1. `Activity`는 `class` 상태로 존재.
    //  2. `Activity`가 화면에 나타나려면 일단 객체화가 되어야함. (`instance` 화)
    //  3. `onCreate() mothod` 가 `callback` 된다. => 화면구성을 주로 함.
    //  4. 화면구성 이후 `onStart() method` 가 `callback` => `Activity`의 초기화 작업 진행.
    //  5. `Activity`가 `foreground`로 나타나면서 사용자와 `interaction`이 가능해짐.
    //  6. `onResume() method` 가 바로 `callback`됨.
    //  7. `Activity`의 상태가 `Running` 상태가 됨. App이 실행되고 있는 상태.
    //  8. 다른 `Activity`로 인해 `Activity`의 일부분이 보이지 않는상태가 될수 있다. (`Pasue`상태)
    //  9. `Pasue`상태가 되면 `onPause() method`가 `callback`


    // onCreate() : 특정시점이 되면 Android system에 의해서 자동적으로 호출됨.
    //              class의 instance가 생성될때
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //기본적으로 상위 클래스의 method호출.
        // activity의 화면을 구성하는 방식은 크게 2가지 방식이 있다.
        // 1. Java code로 widget component를 생성해서 화면에 붙이는 방식.
        // 2. xml 파일을 이용해서 화면구성을 xml을 이용해서 처리
        // xml 을 이용하는 방식은 표현(화면구성 UI)과 구현(로직처리)을 분리.\
        // 유지보수 측면에서 이 방식이 유리하다. 우리가 사용할 방식.
        setContentView(R.layout.activity_main); // R.layout.activity_main : 하나의 숫자값.

        // R.java : static class android에 의해서 자동으로 생성되는 class
        // layout : layout 폴더.
        // activity_main : layout 폴더에 있는 xml 파일 이름.
        // activity_main.xml 파일을 이용해서 activity의 View를 설정하는 method : setContentView()
        // xml 파일은 무조건!!!!!! 소문자!!!! 로 만들기.

        // Logcat을 이용해 보자.
        Log.i("MYTEST","onCreate() 가 호출되었어요.");

        // View 와 ViewGroup
        // View는 통삭적으로 눈에 보이는 Compomnent를 생각하면 된다.
        // -> Button, TextView(lable), ImageView(그림), ...
        // ViewGroup은 이런 View를 크기와 위치를 조절해서 설정해주는 놈
        // ViewGroup의 대표적인 녀석이 Layout
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MYTEST","onStart() 가 호출되었어요.");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MYTEST","onResume() 가 호출되었어요.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MYTEST","onPause() 가 호출되었어요.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MYTEST","onStop() 가 호출되었어요.");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("MYTEST","onRestart() 가 호출되었어요.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MYTEST","onDestroy() 가 호출되었어요.");
    }
}

//res 폴더!
// 1. drawble : application 내에서 사용하는 그림파일 저장.
// 2. layout : Activity 에서 사용할 화면구성을 위한 xml파일.
// 3. minmap : launcher icon 과 같은 이미지 자원을 저장.
// 4. values : 여러가지 문자열이나 컬러와 같은 다양한 자원에 대한 정보를 저장.