package com.example.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Example02_EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example02_event);

       Button _02_00_textChangeBtn = (Button)findViewById(R.id._02_02_clickBtn);

       _02_00_textChangeBtn.setOnClickListener(new View.OnClickListener(){



           final TextView tv =  (TextView)findViewById(R.id._02_01_textPosition);
           @Override
           public void onClick(View v) {
               tv.setText("변경후");
           }
       });

    }
}
