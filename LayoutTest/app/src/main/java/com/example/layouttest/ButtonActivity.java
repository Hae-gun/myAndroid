package com.example.layouttest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ButtonActivity extends AppCompatActivity {


    // Event : 사용자에 의해서 발생되는 혹은 시스템에 의해서 발생되는 모든것.
    // Event Handling을 해야 한다.
    // Java는 Event delegation Model을 이용해서 event를 처리
    // Event 처리에 관련된 3가지 객체
    // 1. Event Source객체 : Event가 발생한 객체
    // 2. Event Handler객체(Listener) : Event를 처리하는 객체
    // 3.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
    }
}
