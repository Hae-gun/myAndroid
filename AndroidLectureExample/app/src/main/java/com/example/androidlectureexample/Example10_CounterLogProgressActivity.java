package com.example.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Trace;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Example10_CounterLogProgressActivity extends AppCompatActivity {

    private TextView tv;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example10_counter_log_progress);
        tv = (TextView) findViewById(R.id._10_01_sumTv);
        pb = (ProgressBar) findViewById(R.id._10_04_counterProgress);

        Button startBtn = (Button) findViewById(R.id._10_02_startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thread를 만들어서 처리하도록 해야 해요.
                CounterRunnable runnable = new CounterRunnable();
                Thread t = new Thread(runnable);
                t.start();
            }
        });

    }
    // `Android UI component(Widget)`는 `thread safe` 하지 않는다.
    // `Android UI component`는 오직 `UI Thread( Activity )`에 의해서만 제어되어야 한다.
    // 화면제어(`UI component-widget`)는 오직 `UI Thread( Activity )`에 의해서만 제어될 수 있다.
    // 아래의 코드는 실행되긴 하지만 올바르지 않은 코드이다.
    // `Handler`를 이용해서 이 문제를 해결해야 한다.
    class CounterRunnable implements Runnable {

        @Override
        public void run() {
            // 숫자를 더해가면서 progressbar를 진행시킨다.
            long sum = 0;
            for (long i = 0; i < 10000000000L; i++) {
                sum += i;
                if (i % 100000000 == 0) {
                    //Long loop = i / 100000000;  // autoboxing
                    long loop = i / 100000000;
                    pb.setProgress((int) loop);
                }
            }
            tv.setText("합계는 : " + sum);

        }
    }
}
