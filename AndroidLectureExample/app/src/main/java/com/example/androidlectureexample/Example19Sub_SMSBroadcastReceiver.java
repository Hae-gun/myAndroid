package com.example.androidlectureexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Date;

/*
 * 보안설정이 잘 되어 잇으면
 * 특정 signal(Broadcast 가 전파되면)이 발생하면
 * 해당 Broadcast 를 받을 수 있다.
 * */
public class Example19Sub_SMSBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Broadcast 를 받으면 이 method 가 호출된다.
        // SMS 가 도착하면 해당 method 가 호출된다.
        Log.i("SMSTest", "SMS 가 도착했어요!!!");
        // 만약 SMS signal 을 받을 수 있으면
        // 전화번호, 문자내용을 뽑아서 Activity 에게 전달하면 된다.
        // 전달받은 Intent 안에 보낸사람의 전화번호, 메시지 내용, 날짜.
        // 정보가 들어있다.

        // Intent 안에 포함되어 있는 정보를 추출한다.
        Bundle bundle = intent.getExtras();

        // Bundle 안에 key,value ㅎ여태로 데이터가 여러개 저자되어 있다.
        // SMS 의 정보는 "pdus" 라는 key 값으로 저정되어 있다.
        // 거의 시간상 동시에 여러개의 SMS 가 도착할 수 있다.
        // 객체 1개가 메시지 1개를 의미.

        Object[] obj = (Object[]) bundle.get("pdus");

        SmsMessage[] message = new SmsMessage[obj.length];
        // 우리 예제에서는 1개의 SMS 만 전달된다고 가정하고 진행.
        // Object 객체 형태를 SmsMessage 객체 형태로 converting.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            message[0] = SmsMessage.createFromPdu((byte[]) obj[0], format);
        } else {
            message[0] = SmsMessage.createFromPdu((byte[]) obj[0]);
        }
        // 보낸사람 전화번호를 SmsMessage 객체에서 추출.
        String sender = message[0].getOriginatingAddress();
        // SMS 문자내용을 추출
        String msg = message[0].getMessageBody();
        // SMS 받은 시간을 추출.
        String reDate = new Date(message[0].getTimestampMillis()).toString();

        Log.i("SMSTest","전화번호 : " +sender );
        Log.i("SMSTest","내용 : " + msg);
        Log.i("SMSTest","시간 : " + reDate);

        // 데이터를 잘 받아왓으면 해당 Data 를 Activity 에게 전달한다.
        Intent i = new Intent(context,
                Example19_BRSMSActivity.class);
        // 기존에 이미 생성되어 있는 activity 에게 전다해야 하므로 flag 를 설정.
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Intent 에 Data 를 저장해서 보낸다.
        i.putExtra("sender",sender);
        i.putExtra("msg",msg);
        i.putExtra("reDate",reDate);

        context.startActivity(i); // context 를 이용해 activity 를 시작.
    }
}
