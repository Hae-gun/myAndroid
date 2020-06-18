package org.techtown.mybinderservice.VO;

import android.util.Log;

import java.io.BufferedReader;
import java.util.LinkedList;

public class SharedObject {
    LinkedList<String> list = new LinkedList<>();
    LinkedList<LatteMessage> msgList= new LinkedList<>();
    Object MONITOR= new Object();
    BufferedReader br;

    private static final SharedObject shared = new SharedObject();

    private SharedObject(){

    }

    public SharedObject(BufferedReader br){
        this.br = br;
    }

    public static SharedObject getInstance(){
        return shared;
    }


    public void put(String msg){
        synchronized (MONITOR){
            list.add(msg);
            Log.i("shared",msg);
            MONITOR.notifyAll();
        }
    }
    public void put (LatteMessage msg){
        synchronized (MONITOR){
            msgList.add(msg);
            MONITOR.notifyAll();
        }
    }

    public String pop(){
        String result ="";
        synchronized (MONITOR){
            if(list.isEmpty()){
                try {
                    MONITOR.wait();
                    result = list.removeFirst();
                }catch(Exception e){
                    Log.i("LatteTest",e.toString());
                }
            }else{
                result = list.removeFirst();
            }
        }
        return result;
    }

    public LatteMessage popMsg(){
        LatteMessage result =null;
        synchronized (MONITOR){
            if(msgList.isEmpty()){
                try {
                    MONITOR.wait();
                    result = msgList.removeFirst();
                }catch(Exception e){
                    Log.i("LatteTest",e.toString());
                }
            }else{
                result = msgList.removeFirst();
            }
        }
        return result;
    }


}
