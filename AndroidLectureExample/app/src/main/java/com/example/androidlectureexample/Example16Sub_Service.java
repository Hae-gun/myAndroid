package com.example.androidlectureexample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

//Service 가 실행되면
// 만약 Service 가 존재하지 않는다면 일단 Service 를 생성한다.
// Service 를 생성하기 위해 생성자가 호출
// 그 후 onCreate() 가 호출된후 onStartCommand() 호출되서 로직처리를 시작.
// 만약 Service 객체가 이미 존재하고 있으면
// onStartCommand() 만 호출되서 로직처리를 시작.

public class Example16Sub_Service extends Service {
    public Example16Sub_Service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // 기본 3개의 method 를 overriding

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 로직처리를 하는 부분
        // Activity 로 부터 전달된 intent 가 이 method 의 인자로 전달.
        String data = intent.getExtras().getString("DATA");

        // 전달받은 데이터를 이요해서 일반적인 로직처리를 진행.
        // 만약 로직처리가 길어지면(오래걸리면) Activity 가 block 된다.
        // 이런 경우를 방지하기 위해서 일반적으로 Thread 를 활용해서 로직처리를 한다.

        // 결과데이터를 Service 에서 생성.
        String resultData = data + " 를 받았습니다.";
        // 이 결과데이터를 Activity 에게 전달하고 Activity 는 화면에
        // 결과데이터를 TextView 로 출력.
        Intent resultIntent = new Intent(getApplicationContext(),
                Example16_ServiceDataTransferActivity.class);

        // 결과값을 intent 에 부착.
        resultIntent.putExtra("RESULT",resultData);

        // Service 에서 Activity 를 호출하려고 한다.
        // 화면이 없는 Service 에서 화면을 갖고있는 Activity 를 호출.
        // 이 경우 TASK 가 필요하다.
        // Activity 를 새로 생성하는게 아니라
        // 메모리에 존재하는 이전 Activity 를 찾아서 실행. => 플래그 2개를 추가.
        // 새로운 task 만드는 flag
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // 이전에 존재한것 찾는 flag
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(resultIntent);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
