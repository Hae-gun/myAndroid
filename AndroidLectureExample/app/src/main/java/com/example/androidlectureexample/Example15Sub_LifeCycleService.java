package com.example.androidlectureexample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class Example15Sub_LifeCycleService extends Service {

    private Thread myThread;

    public Example15Sub_LifeCycleService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 객체가 생성될 때 호출.
        Log.i("ServiceExam", "onCreate() 호출");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 실제 서비스 동작을 수행하는 부분.
        // onCreate() -> onStratCommand()
        // Service 수행할 일 .... 1초 간격으로 1부터 시작해서 10까지 숫자를 Log로 출력!
        myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Thread 가 시작되면 1초동안 잤다가 깨서 Log 를 이용해서 숫자 출력.
                for (int i = 0; i < 30; i++) {
                    try {
                        Thread.sleep(1000); // 1초 동안 정지 후 실행
                        // sleep 을 하려고 할때 만약 이 Thread 가 interrupt 가 걸려있으면
                        // Exception 이 발생한다.
                        Log.i("ServiceExam", "현재 숫자는 : " + i);
                    } catch (Exception e) {
                        Log.i("ServiceExam", e.toString());
                        break; // 가장 가까운 LOOP 벗어나는 키워드.
                    }
                }

            }
        });
        myThread.start();
        // start() 실행시 Thread 에서 run() method 가 실행.
        // 언젠가는 run() method 의 실행이 끝남.
        // 끝난 후 Thread 의 상태가 DEAD 상태가 된다. => 이 DEAD 상태에서 다시 실행 시킬 수 있는 방법이 없다.
        // 유일하게 다시 실행 시키는 방법은 Thread 를 다시 생성해서 실행해야 한다.

        Log.i("ServiceExam", "onStartCommand() 호출");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // stopService() 가 호출되었을 때 onDestroy() 가 호출됨
        // 현재 Service 에 의해서 동작하고 있는 모든 Thread 를 종료
        if(myThread != null && myThread.isAlive()){
            // Thread 가 존재하고 현재 Thread 가 실행중일때
            //myThread.stop(); // 옛날에는 이렇게 썼으나 지금은 불가능. deplicated
            myThread.interrupt(); // Thread 에게 멈추라는 명령을 주는 의미.
        }
        Log.i("ServiceExam", "onDestroy() 호출");

    }

    // 현재 코드에서는 사용 안함.
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

