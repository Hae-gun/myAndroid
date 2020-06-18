package com.example.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Example16_ServiceDataTransferActivity extends AppCompatActivity {
    TextView _16_01_dataFormServiceTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example16_service_data_transfer);

        // 사용할 Component 의 Reference 를 획득.
        _16_01_dataFormServiceTv =
                (TextView) findViewById(R.id._16_01_dataFormServiceTv);
        final EditText _16_02_dataToServiceEt =
                (EditText) findViewById(R.id._16_02_dataToServiceEt);
        Button _16_03_dataToServiceBtn =
                (Button) findViewById(R.id._16_03_dataToServiceBtn);

        // Button 에 대한 click evnet 처리
        // (anonymous inner class 를 이용하여 event 처리)

        _16_03_dataToServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText 안에 사용자가 입력한 데이터를 가지고
                // Service 를 호출.
                Intent intent = new Intent(getApplicationContext(),
                        Example16Sub_Service.class);
                // Service 에게 데이터를 전달하려면
                // Intent 를 이용하여 데이터를 전달해야 한다.
                // key, value 의 형식으로 데이터를 Intent 에 붙인다.
                intent.putExtra("DATA",
                        _16_02_dataToServiceEt.getText().toString());
                startService(intent);
            }
        });


    }

    // Service 로부터 intent가 도착하면 method 가 호출
    @Override
    protected void onNewIntent(Intent intent) {
        // 여기서 Service 가 보내준 결과데이터를 생성.
        String result = intent.getExtras().getString("RESULT");
        // 추출한 결과를 TextView에 세팅
        _16_01_dataFormServiceTv.setText(result);
        super.onNewIntent(intent);

    }
}
