package org.techtown.latteroomfrag;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

public class SocketService extends Service {
    Socket socket;
    BufferedReader br;
    PrintWriter pr;
    public SharedObject shared = new SharedObject();

    public static class SharedObject {
        LinkedList<String> list = new LinkedList<>();
        Object MONITOR = new Object();

        public void put(String s) {
            synchronized (MONITOR) {
                list.addLast(s);
                Log.i("ArduinoTest", "공용객체에 데이터 입력");
                // 리스트 안에 문자열이 없어 대기하던 pop 매서드를 꺠워서 실행시킨다.
                MONITOR.notify();
            }
        }


        public String pop() {
            String result = "";

            synchronized (MONITOR) {
                if (list.isEmpty()) {
                    // 리스트 안에 문자열이 없으니까 일시 대기해야 한다.
                    try {
                        MONITOR.wait();
                        // 큐 구조에서 가져옴
                        result = list.removeFirst();
                    } catch (Exception e) {
                        Log.i("ArduinoTest", e.toString());
                    }
                } else {
                    result = list.removeFirst();
                    Log.i("ArduinoTest", "공용객체에서 데이터 추출");
                }
            }
            return result;
        }


    }



    public SocketService() {

        try {
            socket = new Socket("70.12.60.90", 55566);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pr = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Thread getMsg = new Thread(() -> {
            try {
                String msg = "";
                while ((msg = this.br.readLine()) != null) {
                    Log.i("msg", msg);
                }
            } catch (IOException e) {
                Log.i("error", e.toString());
            }
        });
        getMsg.start();
        Thread putMsg = new Thread(() -> {

            while (true) {
                String s = shared.pop();
                pr.println(s);
                pr.flush();
            }

        });
        putMsg.start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public SharedObject getShared() {
        return this.shared;
    }


}
