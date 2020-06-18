package com.example.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class Example17_KAKAOBookSearchActivity extends AppCompatActivity {
    ListView KAKAOBookList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example17_kakao_book_search);

        // Widget 에 대한 reference 얻어오기.
        final EditText kakaoEt = (EditText) findViewById(R.id._17_01_KAKAOEt);
        Button KAKAOSearchBtn = (Button) findViewById(R.id._17_02_KAKAOSearchBtn);
        KAKAOBookList = (ListView) findViewById(R.id._17_03_KAKAOBookList);

        // Button 에 대한 이벤트 처리
        KAKAOSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        Example17Sub_KAKAOBookSearchService.class);
                intent.putExtra("KEYWORD", kakaoEt.getText().toString());
                startService(intent);
            }
        });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ArrayList<String> booksTitle =
                (ArrayList<String>)intent.getExtras().get("BOOKRESULT");

        ArrayAdapter adpter = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                booksTitle);

        KAKAOBookList.setAdapter(adpter);
    }
}
