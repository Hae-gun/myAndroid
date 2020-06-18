package com.example.androidlectureexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;

public class Example13_DetailBookSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example13_detail_book_search);

        // 사용하는 widget의 reference 획득.
        Button detailSearchBtn = (Button) findViewById(R.id._13_02_detailSearchBtn);
        final EditText detailSearchTitle =
                (EditText) findViewById(R.id._13_01_detailSearchTitle);
        final ListView detailSearchList =
                (ListView) findViewById(R.id._13_03_detailSearchList);

        // Web Application 과 연동해야 하는 Android App
        // Network 기능을 이용해서 Web Application 에서 데이터를 받아와야 한다.
        // Network 연결은 Activity 에서 작업하면 안되고 별도의 Thread 를 만들어서 처리해야 한다.
        // Activity 와 Thread 가 데이터를 주고받기 위해서 Handler 가 필요하다.
        // Thread 가 handler 를 통해서 Activity 에게 데이터를 전달하게된다. (sendMessage)
        // Activity 가 Data 를 받으면 Handler 안에 handlerMessage 가 호출되는 구조.

        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                final BookVO[] booklist = (BookVO[])bundle.getSerializable("BOOKLIST");

                // ArrayAdapter 를 만들어서 ListView 에 책제목만 출력하기.
                // 책제목에 대한 String[] 이 필요하다.
                // Book[] 로 부터 String[]를 뽑아내야 한다.
                // 책의 갯수만큼 String 배열을 만든다.
                String[] titles = new String[booklist.length];
                int i=0;
                // ArrayList 안의 VO 들을 반복하면서
                // 제목만 뽑아서 String[] 에 저장
                for(BookVO vo : booklist){
                    titles[i++] = vo.getBtitle();
                }
                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        titles);
                detailSearchList.setAdapter(adapter);

                detailSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.i("myitem", "선택한 위치는 : " + position);
                        Log.i("myitem", booklist[position].getBisbn());
                        Log.i("myitem", booklist[position].getBtitle());

                        Intent i = new Intent();
                        i.putExtra("ISBN",booklist[position].getBisbn());
                        ComponentName cname = new ComponentName("com.example.androidlectureexample",
                                "com.example.androidlectureexample.Example14_DetailBookInfo");
                        i.setComponent(cname);
                        startActivity(i);


                    }
                });
            }
        };
        // Button 을 클릭하면 Thread를 생성해서 일을 시킨다.
        detailSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thread 를 생성
                MyBookInfo runnable = new MyBookInfo(handler, detailSearchTitle.getText().toString());
                Thread t = new Thread(runnable);
                t.start();
            }
        });

    }

}

class MyBookInfo implements Runnable {

    private Handler handler;
    private String keyword;

    public MyBookInfo() {
    }

    public MyBookInfo(Handler handler, String keyword) {
        this.handler = handler;
        this.keyword = keyword;
    }

    @Override
    public void run() {
        // Thread 가 시작되면 수행하는 작업을 여기에 작성한다.
        // Web application 을 호출해서 JSON 결과를 가져와서 Activity 에게 전달.

        String url = "http://70.12.60.97:8080/booksearch/bookinfo?keyword="+keyword;
        try {
            // URL 객체생성.
            URL obj = new URL(url);
            // URL 갹체를 이용하여 접속.
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // Web Application 호출방식 설정.
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            Log.i("BookSearch", "서버로 부터 전달된 code : " + responseCode);

            // 연결 성공후 데이터를 받아오기 위한 통로 생성.
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            // 서버가 보내주는 데이터를 읽어서 하나의 문자열로 만든다.
            String readLine = "";
            StringBuffer responseTxt = new StringBuffer();
            while ((readLine = br.readLine()) != null) {
                responseTxt.append(readLine);
            }

            br.close();     // 통로사용이 끝났으니 해당 resource 자원을 해제.

            Log.i("BookSearch", "얻어용 내용은 : " + responseTxt.toString());

            // JACKSON library 사용
            ObjectMapper mapper = new ObjectMapper();
            BookVO[] resultArr = mapper.readValue(responseTxt.toString(), BookVO[].class); // String array class 로 바꾸라는 형태.
            Bundle bundle = new Bundle();

            // bundle 객체에 객체배열을 넣는다.
            bundle.putSerializable("BOOKLIST", resultArr);

            Message msg = new Message();
            msg.setData(bundle);
            handler.sendMessage(msg);


        } catch (Exception e) {
            Log.i("BookSearch", e.toString());
        }



    }
}

class BookVO implements Serializable {

    private String bisbn;
    private String btitle;
    private String bdata;
    private int bpage;
    private int price;
    private String bauthor;
    private String btranslator;
    private String bsupplement;
    private String bpulisher;
    private String bimgurl;
    private String bimgbase64;

    // constructor
    public BookVO() {
    } // 인자도 없고 하는일도 없는 default 생성자.
    // package에 상관없이 사용하도록 public을 붙여준다.



    // private field를 사용하기 위한 특수한 형태의 method 필요함. setter, getter

    public String getBisbn() {
        return bisbn;
    }

    public void setBisbn(String bisbn) {
        this.bisbn = bisbn;
    }

    public String getBtitle() {
        return btitle;
    }

    public void setBtitle(String btitle) {
        this.btitle = btitle;
    }

    public String getBdata() {
        return bdata;
    }

    public void setBdata(String bdata) {
        this.bdata = bdata;
    }

    public int getBpage() {
        return bpage;
    }

    public void setBpage(int bpage) {
        this.bpage = bpage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getBauthor() {
        return bauthor;
    }

    public void setBauthor(String bauthor) {
        this.bauthor = bauthor;
    }

    public String getBtranslator() {
        return btranslator;
    }

    public void setBtranslator(String btranslator) {
        this.btranslator = btranslator;
    }

    public String getBsupplement() {
        return bsupplement;
    }

    public void setBsupplement(String bsupplement) {
        this.bsupplement = bsupplement;
    }

    public String getBpulisher() {
        return bpulisher;
    }

    public void setBpulisher(String bpulisher) {
        this.bpulisher = bpulisher;
    }

    public String getBimgurl() {
        return bimgurl;
    }

    public void setBimgurl(String bimgurl) {
        this.bimgurl = bimgurl;
    }

    public String getBimgbase64() {
        return bimgbase64;
    }

    public void setBimgbase64(String bimgbase64) {
        this.bimgbase64 = bimgbase64;
    }

}