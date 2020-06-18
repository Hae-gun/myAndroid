package com.example.androidlectureexample;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Example12_SimpleBookSearchActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example12_simple_book_search);

        // 검색버튼에 대한 reference 획득 및 이벤트 처리.
        Button searchBtn = (Button) findViewById(R.id._12_02_searchBtn);
        // 검색입력상자에 대한 reference 획득.
        final EditText searchTitle = (EditText) findViewById(R.id._12_01_searchTitle);
        // 결과 ListView 에 대한 reference 획득.
        final ListView searchList = (ListView) findViewById(R.id._12_03_searchList);
        // Network 연결 (Web Application 호출)을 해야 하기 떄문에
        // UI Thread (Activity) 에서 이 작업을 하면 안된다.
        // => Thread 로 해결해야 한다.
        //=> Thread 와 데이터를 주고받기 위해서 Handler 이용.

        // Handler 객체를 생성.
//        @SuppressLint("HandlerLeak") final Handler handler2 = new Handler() {
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                super.handleMessage(msg);
//                Bundle bundle2 = msg.getData();
//                String[] bookDetail = (String[]) bundle2.get("BOOKDETAIL");
//                Log.i("title",bookDetail.toString());
////
//            }
//        };


        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
            // Thread 에 의해서 추후에 sendMessage 가 호출되는데
            // sendMessage 에 의해서 전달된 데이터를 처리하기 위해서
            // handleMessage 를 overriding 하면서 instance 를 생성.


            @Override
            public void handleMessage(@NonNull final Message msg) {
                super.handleMessage(msg);
                // Thread 가 보내준 데이터로 ListView 를 채우는 코드
                // Thread 가 보내준 Message 에서 Bundle을 꺼낸다.
                Bundle bundle = msg.getData();
                // Bundle 에서 key 값을 이용해 데이터를 추출.
                final String[] bookList = (String[]) bundle.get("BOOKLIST");

                // ListView 사용은 Spinner 사용과 비슷하다.
                // ListView 에 데이터를 설정하려면 Adapter 가 필요하다.
                // Adapter 에 데이터를 설정하여 이 Adapter 를 ListView 에 부착시킨다.
                ArrayAdapter adapter =
                        new ArrayAdapter(getApplicationContext(),
                                android.R.layout.simple_list_item_1, bookList);
                // ListView에 위에서 생성한 adapter를 부착.
                searchList.setAdapter(adapter);
                searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String Title = bookList[position];
                        Log.i("title", Title);

//                        Intent i = new Intent();e);i.setComponent(cname);
//////                        Thread t = new Thread(runnable);//                        i.putExtra("title",
//
////                        BookDetailSearchRunnable runnable = new BookDetailSearchRunnable(handler2,Titl Title);
////                        ComponentName cname = new ComponentName("com.example.androidlectureexample",
////                                "com.example.androidlectureexample.Example13_DetailBookSearchActivity");
////
////                        t.start();
//
//                        startActivity(i);


                    }
                });


            }
        };

        // Button 이벤트 처리
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thread 를 생성
                BookSearchRunnable runnable =
                        new BookSearchRunnable(handler, searchTitle.getText().toString());
                Thread t = new Thread(runnable);
                // Thread 시작하귀 위한 매서드 start() 호출
                t.start();
            }
        });


    }


}

// Thread 를 생성하기 위한 Runnable interface 를 구현한 class 를 정의
class BookSearchRunnable implements Runnable {
    private Handler handler;
    private String keyword;


    BookSearchRunnable() {
    } // default constructor

    BookSearchRunnable(Handler handler, String keyword) {
        this.handler = handler;
        this.keyword = keyword;
    }

    @Override
    public void run() {
        // Web Application 을 호출.
        // 결과를 받아서 그 결과를 예쁘게 만든 후 handler 를 통해서
        // activity 에게 전달.
        // 1. 접속할 URL 을 준비한다.
        String url = "";

        url = "http://70.12.60.97:8080/booksearch/searchTitlebyKeyword?keyword=" + keyword;

            // 2. Java Network 기능은 Exception 처리를 기본으로 해야한다.
        try {
            // 3. URL 객체를 생성해야 한다. (Java 의 URL class 를 이용해서)
            URL obj = new URL(url);
            // 4. URL 객체를 이용해서 접속을 시도해요.
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // 5. Web Application 의 호출방식을 설정 (GET, POST)
            con.setRequestMethod("GET");
            // 6. 정상적으로 접속이 되었는지를 확인!
            //    HTTP protocol 로 접속할 때 접속 결과에 대한 상태값.
            //    200 : 접속성공 ,404: not found ,500 : internal sever error
            //    403 : forbidden, ....

            int responseCode = con.getResponseCode();
            Log.i("BookSearch", "서버로 부터 전달된 code : " + responseCode);

            // 7. 보안설정에 문제가 있음.
            // Android App 은 특정기능을 사용하기 위해서 보안설정을 해야한다.
            //   네트워크 접속을 위해 보안설정이 필요하다.
            //   android 9(pie)버전부터는 보안이 강화되었다.
            //   웹 프로토콜에 대한 기본 protocol 이 http 에서 https 로 변경
            //   따라서 http protocol 을 이용하는 경우 특수한 설정이
            //   AndroidManifest.xml 파일에 있어야 한다.

            // 8. 서버와의 연결 객체를 이용하여 서버와의 데이터 통로를 하나 열어야 한다.(Java Stream)
            // 이 연결통로를 이용해서 데이터를 읽어들일 수 있다.
            // 이 통로를 하나 생성해야 한다.
            // 기본적인 연결 통로를 이용해서 조금더 효율적인 연결통로로 다시 만들어 사용.
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            // 서버가 보내주는 데이터를 읽어서 하나의 문자열로 만들어 준다.
            String readLine = "";
            StringBuffer responseTxt = new StringBuffer();
            while ((readLine = br.readLine()) != null) { // 서버와 연결하여 다음줄이 null 이 아니면 계속 저장.
                responseTxt.append(readLine);

            }
            br.close();     // 통로사용이 끝나으니 해당 resource 자원을 해제.
            Log.i("BookSearch", "얻어용 내용은 : " + responseTxt.toString());
            //  ["Head First Java: 뇌 회로를 ...,"뇌를 자극하는 Java 프로그래밍 ..."]
            // 가져온 데이터(문자열)를 자료구조화 시켜서 안드로이드 앱에 적용하는 방법을 생각해 보자.

            // 일반적으로 서버쪽 웹 프로그램은 XML 이나 혹읜 JSON 으로 결과 데이터를 제공해준다.
            // 우리가 받은 데이터도 JSON 형식이다.
            // 받은 `JSON` 을 `Java`의 자료구조로 변경해야 한다.
            // `JSON parsing library`를 가져다가 좀 편하고 쉽게 `JSON`을 `handling` 할수 있다.
            // 가장 대표적인 `JSON` 처리 `library`중 하나인 `JACKSON library`를 이용.

            // JSON library 가 있어야 사용할 수 있으니 라이브러리 설치해야 한다.
            // Gradle 을 이용하여 library 다운로드 받을수 있다.

            //JSON library 사용
            ObjectMapper mapper = new ObjectMapper();
            String[] resultArr = mapper.readValue(responseTxt.toString(), String[].class); // String array class 로 바꾸라는 형태.
            // 얻어온 JSON 문자열 데이터를 Java 의 String array 로 변환했다.

            // 최종 결과 데이터를 Activity 에게 넘겨주기. (`UI Thread`에게 전달해서 화면제어를 하기 위함.)
            // 데이터를 Activity 에게 전달하기 위해서.
            // 1. `Bundle` 에 전달할 데이터를 붙임.
            Bundle bundle = new Bundle();

            bundle.putStringArray("BOOKLIST", resultArr);

            // 2. `Message` 를 만들어서 `Bundle` 을 `Message`에 부착.
            Message msg = new Message();
            msg.setData(bundle);
            // 3. `Handler`를 이용하여 `Message`를 `Activity`에 전달.
            handler.sendMessage(msg);


        } catch (Exception e) {
            Log.i("BookSearch", e.toString());
        }
    }
}
//        //웹 어플리케이선 호출
//        //결과를 받아와서 그 결과를 예쁘게 만든 후 handler를 통해서 activity에게 전달
//
//        //1. 접속할url을 준비
//        String url = "http://70.12.60.97:8080/bookSearch/searchTitle?keyword=" + keyword;
//
//        //2. java network 기능은 제대로 수행이 안될 경우가 있음 - 서버 죽었거나..
//        //프로그램 자체를 다운되지 않기 위해 Exception 처리함
//        try {
//            //3. URL 객체를 생성한다
//            //JAVA의 URL class를 이용해서
//            //주소를 객체화 시키는 class = URLclass
//            URL obj = new URL(url);
//            //해당 문자열을 가지고 URL 객체 만들기
//            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//            //URL 객체를 이용해서 접속하기 - 캐스팅 필요
//            con.setRequestMethod("GET");
//            //GET 방식으로 접속할거야 (호출방식 : get,post)
//            //정상작동 확인
//
//            //202 : wjqthrtjdrhd, 404 : not found, 500 : internal server error // 403 forbidden
//            int responseCode = con.getResponseCode();
//            Log.i("BookSearch", "서버로부터 전달된 code : " + responseCode);
//
//
//
//        } catch (Exception e) {
//            Log.i("BookSearch", e.toString());
//
//        }

