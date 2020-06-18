package com.example.androidlectureexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Example14_ImplicitIntentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example14_implicit_intent);

        Button btn = (Button) findViewById(R.id._14_1_1_implicitIntentBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼을 클릭했을 때 새로운 Activity 를 실행 (묵시적 Intent를 이용)
                // implicit Intent(묵시적 인텐트)
                // 방금 생성한 Activity 를 호출하려고 한다.
                Intent i = new Intent();
                // Manifest 에서 지정한 Action 을 찾는다.
                i.setAction("MY_ACTION");
                // category 도 찾기. (DEFAULT 말고 직접 설정했던 name)
                // i.addCategory("INTENT_TEST");
                i.putExtra("SEND DATA", "안녕하세요");
                startActivity(i);
            }
        });

        Button btn2 = (Button) findViewById(R.id._14_1_2_dialBtn);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 전화걸기 Activity 를 호출하려면 2가지 중 1가지를 알아야 한다.
                // 1. 명확한 class 명을 알면 호출이 가능하다. (Explicit intent 이용)
                // 2. 묵시적 Intent 를 이용해서 알려져 있는 Action을 통해서 이용.
                Intent i = new Intent();
                i.setAction(Intent.ACTION_DIAL);
                // 전화걸기 화면에서 사용할 데이터. 전화번호가 들어감.
                // Uri 객체를 이용하여 문자열이 들어감.
                // 번호가 바로 들어가는 것이 아닌 접두어 tel 이 있어야 번호에 대한정보라는 것을 전달할 수 있다.
                i.setData(Uri.parse("tel:01043392652"));
                startActivity(i);
            }
        });

        Button btn3 = (Button) findViewById(R.id._14_1_3_browserBtn);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 특정 URL을 이용해서 browser를 실행.
                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://www.naver.com"));
                startActivity(i);
            }
        });

        /*
        먼저 전화걸기 기능을 사용하려면 AndroidManifext.xml 파일에 권한설정을 해야한다.
        안드로이드 6.0(마쉬멜로우) 이상에서는 `manifest` 파일에 기술하는것 이외에 사용자에게 권한을 따로 요청해야한다.
        권한자체가 크게 2가지로 분류되서 관리된다.
        1. 일반권한 (`normal permission`) : `Manifest` 에서 작업한것
        2. 위험 권한 (`dangerous permission`) : `위치정보, 전화걸기기능, 카메라, 마이크, 문자, 일정, 주소록, 센서`
        특정 앱을 사용할 때 앱이 사용자에게 권한을 요구하고 사용자가 권한을 허용하면 그 기능을 이용할 수 있다.
        설정 > 어플리케이션 > 앱 > 권한 부분을 이용하면 이미 허용한 권한을 취소할 수 있다.
         */
        Button callBtn = (Button) findViewById(R.id._14_1_4_callBtn);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자가 사용하는 안드로이드 버전이 "M(마쉬멜로우)" 버전 이상인지 확인.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 사용자의 Android version 이 6이상인 경우
                    // 사용자 권한 중 전화걸기 권한이 설정되어 있는지 확인.
                    int permissionResult = ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.CALL_PHONE);
                    // 권한을 이미 허용했는지 그렇지 않은지 비교해서 다르게 처리.
                    if (permissionResult == PackageManager.PERMISSION_DENIED) {
                        // 권한이 없는 경우
                        // 권한 설정을 거부한 적이 있ㄴ느닞 그렇지 않은지 확인.
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                            // 임의로 app의 권한을 끄거나 혹은
                            // 권한을 거부한 기록이 있는경우
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Example14_ImplicitIntentActivity.this);
                            dialog.setTitle("권한요청에 대한 Dialog");
                            dialog.setMessage("절화걸기 기능이 필요합니다. 수락 하시겠습니까?");
                            dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 권한 설정을 다시진행.
                                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                            1000);

                                }
                            });
                            dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 할일이 없음.
                                }
                            });
                            dialog.create().show();
                        } else {
                            // 권한을 거부한 적이 없는경우 (처음 어플에 들어온 경우)
                            // 권한설정 허용창에 대한 method 여러개의 권한을 물어볼 수 있다.
                            // method 인자 : 문자열 배열(권한상수), requestCode.
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                    1000);
                        }

                    } else {
                        // 권한이 있는 경우
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_CALL);
                        i.setData(Uri.parse("tel:01043392652"));
                        startActivity(i);
                    }

                } else {
                    // 사용자의 Android version 이 6미만인 경우
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:01043392652"));
                    startActivity(i);
                }
            }
        });
    } // End of onCreate()

    // 권한 허용 창에서 선택된 후 실행되는 method
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 위에서 수행한 권한 요청에 대한 응답인지를 확인.
        if (requestCode == 1000) {
            // 권한 요청의 응답개수가 1개 이상이고
            // 지금 상황에서 전화걸기 기능 1개만 물어봤으니 첫번째 배열에 그 결과가 담겨온다.
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한 허용을 누른경우
                Intent i = new Intent();
                i.setAction(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:01043392652"));
                startActivity(i);
            } else {

            }
        }
    }
}
