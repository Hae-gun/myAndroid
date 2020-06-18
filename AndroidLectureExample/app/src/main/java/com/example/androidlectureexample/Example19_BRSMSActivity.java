package com.example.androidlectureexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/*
 * 1. 우리 App 이 휴대단말로 온 문자메시지를 처리하려고 한다.
 *    문자메시지 처리는 상당히 개인적인 정보이다. => 보안처리가 필요함. (2가지)
 *    - AndroidManifest.xml 파일에 기본 보안에 대한 설정이 필요하다.
 *    <uses-permission android:name="android.permission.RECEIVE_SMS" /> 설정!
 *
 * 2. Activity 가 실행되면 보안설정 부터 해야한다.
 *    마쉬멜로우 버전이후 부터는 강화된 보안정책을 따라야 한다.
 *    보안처리 코드가 필요함. (전형적인 코드로 사용을 한다.)
 *
 * 3. Broadcast Receiver Component 를 하나 생성.
 *    코드상에서 생성하는게 아니라 AndroidManifest.xml 에 등록해서 생성.
 *    위보 Component 로 따로 생성되기 때문에 AndroidManifest.xml 에 자동등록이 된다.
 *    => 생성된 후 intent-filter 를 이용해서 어떤 broadcast signal 을 수신할지 설정한다.
 *    SMS 문자가 오면 이 문자를 Broadcast Receiver 가 받아서
 *    화면에 표현하기 위해 Activity 에게 전달한다.
 *
 * 4. Boradcast Receiver 에서 Activity 로 데이터가 전달되고..
 *    Intent 를 통해서 데이터가 전달된다.
 *    => activity 는 method 를 이용해서 Intent 를 받는다.
 *    => onNewIntent() 메서드를 통해 받는다.
 *
 * */
public class Example19_BRSMSActivity extends AppCompatActivity {
    private TextView _19_smsSenderTv;
    private TextView _19_smsMessageTv;
    private TextView _19_smsDateTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example19_br_sms);

        _19_smsSenderTv = findViewById(R.id._19_smsSenderTv);
        _19_smsMessageTv = findViewById(R.id._19_smsMessageTv);
        _19_smsDateTv = findViewById(R.id._19_smsDateTv);

        // 1. 사용자의 단말기 OS(Android Version) 이 마쉬멜로우 버전 이전인지
        //    이후인지를 Check.
        //   전형적인 코드.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 만약 우리가 사용하는 기기가 M 이상이면
            // 사용자 권한 중 눈 권한이 설정되어 있는지 Check
            int permissionResult = ActivityCompat.checkSelfPermission(
                    getApplicationContext(),
                    Manifest.permission.RECEIVE_SMS);
            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                // 권한이 없으면
                // 1. App 을 처음 실행해서 권한을 물어본 적이 없는경우.
                // 2. 권한 허용에 대해서 사용자에게 물어는 봤으나 사용자가
                // 거절을 선택한 경우

                // 왜 권한이 없을까? (2가지 Case)
                if (shouldShowRequestPermissionRationale(Manifest.permission.RECEIVE_SMS)) {
                    // true => 권한을 거부한 적이 있는 경우.
                    // 일반적으로 dialog 같은걸 이용하여 다시 물어본다.
                    AlertDialog.Builder dialog =
                            new AlertDialog.Builder(Example19_BRSMSActivity.this);
                    dialog.setTitle("권한이 필요해요!");
                    dialog.setMessage("SMS 수신기능이 필요합니다. 수락할까요?");
                    dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(
                                    new String[]{Manifest.permission.RECEIVE_SMS},
                                    100);

                        }
                    });
                    dialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 권한설정을 하지 않는다는 의미이므로
                            // 아무런 작업을 할 필요가 없다.
                        }
                    });
                    // 만들어진 dialog 를 화면에 띄운다.
                    dialog.create().show();

                } else {
                    // false => 한번도 물어본적이 없는 경우.
                    // 여러 권한을 동시에 사용자에게 요청할 수 있기때문에
                    // 인자가 String 배열로 들어간다.
                    requestPermissions(
                            new String[]{Manifest.permission.RECEIVE_SMS},
                            100);
                }

            } else {
                // 권한이 있으면
                Log.i("SMSTest", "보안설정 통과!!");
            }
        } else {
            // 만약 우리가 사용하는 기기가 M 미만이면
            Log.i("SMSTest", "보안설정 통과!!");
        }

    } // End Oncreate()

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 사용자가 권한을 설정하게 되면 이 부분이 마지막으로 출력된다.
        // 사용자가 권한설정을 하거나 그렇지 않거나 두가지 경우 모두 이 callback 매서드가 호출된다.
        if (requestCode == 100) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 사용자가 권한 허용을 눌렀을 경우.
                Log.i("SMSTest", "보안설정 통과!!");
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Broadcast receiver 가 보내준 intent 를 받는다.
        // intent 안에 들어있는 정보를 꺼내서 화면에 출력.

        String sender = intent.getExtras().getString("sender");
        String msg = intent.getExtras().getString("msg");
        String reDate = intent.getExtras().getString("reDate");

        _19_smsSenderTv.setText(sender);
        _19_smsMessageTv.setText(msg);
        _19_smsDateTv.setText(reDate);

    }
}
