package com.example.layouttest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// Activity에 관해서 알아보고 있어요!
// Activity는 App을 구성하고 있는 화면을 지칭.

// 화면을 표현할 때는 `Activity`하나와 하나 이상의 `XML`파일이 필요.
// `Activity`내에서 자바코드로 화면구성을 다 할 수는 있으나 권장되지 않는다.
//         표현과 구현이 분리되지 않기 때문이다.
// 우리의 App은 일반적으로 여러개의 Activity로 구성

// XML에 여러가지 widget을 넣어서 사용자 component를 표현할 수 있다. => `LayoutManager`
// widget을 내가 원하는 크기로 원하는 위치에 표현하려면...
// widget을 관리해주는 component가 따로 필요하다. => `LayoutManager`
// Linear Layout, Grid Layout

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
