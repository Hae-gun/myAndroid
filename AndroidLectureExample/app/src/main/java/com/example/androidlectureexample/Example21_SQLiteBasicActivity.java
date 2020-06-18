package com.example.androidlectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Example21_SQLiteBasicActivity extends AppCompatActivity {


    // Android 는 Embedded 형태로 개발된 DBMS를 하나 내장하고 있다.
    // 경량화된 DBMS 로 SQLite 라고 불린다.
    // 파일로 이루어져 있기 때문에 Database 의 복사, 이동같은 처리가 쉽고
    // 속도가 빠르고 표준 SQL 을 지원한다.
    //
    // Database 를 생성하고 Table 을 만든 후 Data 를 insert 할 예정.
    // 입력된 데이터를 확인해야 하는데 외부 Tool 을 이용하여 확인한다.
    // http://sqlitebrowser.org .
    private EditText _21_dbNameEt; // database 이름입력 EditText
    private EditText _21_tableNameEt; // Table 이름입력 EditText
    private SQLiteDatabase database; // 생성된 database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example21_sqlite_basic);


        _21_dbNameEt = findViewById(R.id._21_dbNameEt);
        _21_tableNameEt = findViewById(R.id._21_tableNameEt);

        Button _21_dbCreateBtn = findViewById(R.id._21_dbCreateBtn);
        // db 생성
        _21_dbCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Database 생성
                // 파일로 만들어진다. "데이터베이스이름.db" 라는 파일로 생성.
                // /data/data/우리App/ 여기에 생성된다.
                String dbName = _21_dbNameEt.getText().toString();
                // MPDE_PRIVATE : 0 값 읽고 쓰기가 가능한 일반적인 형태의 DB 를 생성하거나 open하는 의미.
                database = openOrCreateDatabase(dbName,
                        MODE_PRIVATE, null);
                Log.i("DBTest", "DB 생성!");
            }
        });

        Button _21_tableCreateBtn = findViewById(R.id._21_tableCreateBtn);
        // create Table 처리
        _21_tableCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // table 이름을 들고온다.
                String tableName = _21_tableNameEt.getText().toString();

                if (database == null) {
                    Log.i("DBTest", "DB 먼저 생성해주세요");
                    return;
                }
                // 현재 Database 에 대한 reference 가 존재!
                // SQL 을 이용해서 Database 안에 Table 을 생성하면 된다.
                // SQL 문 관용적으로 예약어는 대문자로
                String sql = "CREATE TABLE IF NOT EXISTS " +
                        tableName + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT, " +
                        "age INTEGER, " +
                        "mobile TEXT)";
                // 완성된 SQL 을 어떤 database 에서 실행시킬 것인지를 결정해서 실행.
                database.execSQL(sql);

                Log.i("DBTest", "Table 생성 완료");
            }
        });

        final EditText _21_empNameEt = findViewById(R.id._21_empNameEt);
        final EditText _21_empAgeEt = findViewById(R.id._21_empAgeEt);
        final EditText _21_empMobileEt = findViewById(R.id._21_empMobileEt);

        // insert 처리
        Button _21_empInsertBtn = findViewById(R.id._21_empInsertBtn);
        _21_empInsertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력된 사용자를 table 에 insert
                String name = _21_empNameEt.getText().toString();
                int age = new Integer(_21_empAgeEt.getText().toString()).intValue();
                String mobile = _21_empMobileEt.getText().toString();

                if (database == null) {
                    Log.i("DBTest", "DB 먼저 생성해주세요");
                    return;
                }
                String sql = "INSERT INTO emp(name,age,mobile) VALUES " +
                        "('" + name + "'," + age + ",'" + mobile + "')";

                database.execSQL(sql);
                Log.i("DBTest", "데이터 입력 완료");

                _21_empNameEt.setText("");
                _21_empAgeEt.setText("");
                _21_empMobileEt.setText("");
            }
        });

        final TextView _21_resultTv = findViewById(R.id._21_resultTv);

        // select 처리
        Button _21_empSelectBtn = findViewById(R.id._21_empSelectBtn);
        _21_empSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _21_resultTv.setText("");
                String sql = "SELECT _id, name, age, mobile FROM person";
                // JDBC 와 상당히 유사하다.
                if (database == null) {
                    Log.i("DBTest", "DB 먼저 열어주세요");
                    return;
                }
                // execSQL() : select 계열이 아닌 SQL 문장을 실행할때 사용.
                // rawQuery() : select 계열의 SQL 문장을 실행할때 사용
                // 커서는 위치를 가르키는 포인터 => 컬럼을 가르키는 포인터
                Cursor cursor = database.rawQuery(sql, null);

                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    int age = cursor.getInt(2);
                    String mobile = cursor.getString(3);

                    String result = "레코드 : " + id + ", " + name + ", " + age + ", " + mobile;
                    _21_resultTv.append(result+"\n");
                }
            }
        });

    } // End Oncreate
}
