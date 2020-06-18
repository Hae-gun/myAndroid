package com.example.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;

import java.util.ArrayList;

public class Example07_DataFromActivity extends AppCompatActivity {
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example07_data_from);

        // Spinner 안에 표현될 데이터 만들기.
        // 우리 예제에서는 Spinner안에 표현될 데이터를 문자열로 지정.
        final ArrayList<String> list = new ArrayList<String>();
        list.add("포도");
        list.add("딸기");
        list.add("바나나");
        list.add("사과");

        // Spinner의 reference를 획득
        Spinner spinner = (Spinner) findViewById(R.id._07_01_mySpiner);
        // Spinner를 이용하기 위해 Adapter가 필요함.(Adapter의 종류가 다양하다.)
        // (컨텍스트객체 가져옴(뭔진 모르겠지만..), 스피너의 타입(상수),사용할데이터)
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, list);
        // Adapter를 통해 데이터를 Spinner에 부착.
        spinner.setAdapter(adapter);

        // spinner의 event처리.
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // position : 여러개의 아이템중 몇번째를 선택한지에 대한 위치.
                result = list.get(position);
                Log.i("SELECTED", result + "가 선택되었어요!!");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // 익명이너클래스로 선언했기 때문에 오버라이딩을 해야하지만
                // 사용하지 않아서 작성은 안함.
            }
        });

        Button sendBtn = (Button) findViewById(R.id._07_02_SendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("ResultValue", result);
                //Activity 결과로 셋팅하겠다는 의미.
                setResult(7000,
                        returnIntent); //resultCode, 전달할 Data
                Example07_DataFromActivity.this.finish(); // 그냥 닫으면 됨.
                // Main에서 startActivityForResult로 현재 Activity를 가져왔기 때문에 닫으면 Data가 전송됨.
            }
        });


    }
}
