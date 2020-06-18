package com.example.androidlectureexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Example24_ContactActivity extends AppCompatActivity {

    private TextView _24_resultTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example24_contact);

        // 결과가 출력될 TextView
        _24_resultTv = findViewById(R.id._24_resultTv);
        // 보안설정.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 만약 우리가 사용하는 기기가 M 이상이면
            // 사용자 권한 중 주소록읽기 권한이 설정되어 있는지 Check
            int permissionResult = ActivityCompat.checkSelfPermission(
                    getApplicationContext(),
                    Manifest.permission.READ_CONTACTS);
            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                // 권한이 없으면
                // 1. App 을 처음 실행해서 권한을 물어본 적이 없는경우.
                // 2. 권한 허용에 대해서 사용자에게 물어는 봤으나 사용자가
                // 거절을 선택한 경우

                // 왜 권한이 없을까? (2가지 Case)
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_CONTACTS)) {
                    // true => 권한을 거부한 적이 있는 경우.
                    // 일반적으로 dialog 같은걸 이용하여 다시 물어본다.
                    AlertDialog.Builder dialog =
                            new AlertDialog.Builder(Example24_ContactActivity.this);
                    dialog.setTitle("권한이 필요해요!");
                    dialog.setMessage("주소록 접근권한이 필요합니다. 수락할까요?");
                    dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(
                                    new String[]{android.Manifest.permission.READ_CONTACTS},
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
                            new String[]{Manifest.permission.READ_CONTACTS},
                            100);
                }

            } else {
                // 권한이 있으면
                Log.i("ContactTest", "보안설정 통과!!");
                processContact();
            }
        } else {
            // 만약 우리가 사용하는 기기가 M 미만이면
            Log.i("ContactTest", "보안설정 통과!!");
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 사용자가 권한을 설정하게 되면 이 부분이 마지막으로 출력된다.
        // 사용자가 권한설정을 하거나 그렇지 않거나 두가지 경우 모두 이 callback 매서드가 호출된다.
        if (requestCode == 100) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 사용자가 권한 허용을 눌렀을 경우.
                Log.i("ContactTest", "보안설정 통과!!");
                processContact();  // 주소록 가져오는 method 를 호출.

            }
        }
    }

    private void processContact() {
        // 주소록 가져오는 코드를 작성.
        // 1. Content Resolver 를 이용하여 데이터를 가져오면 된다.
        //    Select 계열을 사용.
        //    첫번째 인자 : URI -> Content Provider 를 찾기위함.
        //    주소록 접근 URI : ContactsContract.Contacts.CONTENT_URI
        //    어떻게 select 처리를 할지 인자를 작성.
        Cursor cursor =
                getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                        null, // null : 모든 컬럼 다 들고옴.
                        null, // 조건없이 다 들고옴.
                        null, // 조건절에 사용할 값.
                        null);// 정렬방향. 안하고 가져옴.
        while (cursor.moveToNext()) {
            // 각 사람의 이름과 ID 를 가져와야 한다.
            // 전화번호와 사람이름을 따로 관리한다. (한사람이 여러개의 전화번호를 갖고 있을수도 있으므로)
            // ID 를 이용하여 각 사람의 전화번호를 다시 얻어오는 방식으로 수행.

            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Log.i("ContactTest", "얻어온 ID : " + id);

            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            Log.i("ContactTest", "얻어온 이름 : " + name);
            String msg;
            Cursor mobileCursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, //id 값이 일치하는 값 가져옴.
                    null,
                    null);
            while (mobileCursor.moveToNext()) {
                String mobile = mobileCursor.getString(
                        mobileCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                msg = "이름 : " + name + ", " + "전화번호 : " + mobile;
                _24_resultTv.append(msg + "\n");
            }
        }
    }
}

