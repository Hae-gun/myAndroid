package com.example.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Example08_ANRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example08_anr);

        // TextView 에 대한 reference 를 획득.
        final TextView sumTv = (TextView)findViewById(R.id._08_01_sumTv);

        // 첫번째 버튼에 대한 reference 획득 & 이벤트 처리.
        // 버튼을 누르면 상당히 오랜시간 연산이 수행.
        // 결과적으로 사용자와의 interaction 이 중지됨.
        // Activity 가 block 된 것처럼 보인다. (ANR)
        // 당연히 ANR 현상 은 피해줘야 한다.
        // Activity 의 최우선 작업은 사용자와의 interaction 이다.
        // Activity 에서는 시간이 오래 걸리는 작업은 하면 안된다.
        // Activity 는 Thread 로 동작한다. (UI Thread)
        // 로직처리는 background Thread 를 이용해서 처리해야 한다.
        Button startBtn = (Button)findViewById(R.id._08_02_startBtn);
        startBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                long sum = 0;
                for(long i=0;i<10000000000L;i++){
                    sum += i;
                }
                    Log.i("SumTest","연산된 결과는 "+sum);
            }
        });
        // 두번째 버튼에 대한 reference 획득 & 이벤트 처리.
        // Toast message 를 출력
        Button secondBtn = (Button)findViewById(R.id._08_03_startBtn);
        secondBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(Example08_ANRActivity.this,"버튼이 클릭되었다.",
                        Toast.LENGTH_SHORT).show();
            }
        });


    }
}
