package com.example.androidlectureexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

public class Example14_DetailBookInfo extends AppCompatActivity {
    Handler handler1 = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example14_detail_book_info);

        Intent i = getIntent();
        String isbn = (String) i.getExtras().get("ISBN");
        Log.i("what", "ISBN : " + isbn);

        final TextView titleTv = (TextView) findViewById(R.id._14_01_title);
        final TextView isbnTv = (TextView) findViewById(R.id._14_02_isbn);
        final ImageView imgIv = (ImageView) findViewById(R.id._14_03_img);
        final TextView authorTv = (TextView) findViewById(R.id._14_04_author);
        final TextView priceTv = (TextView) findViewById(R.id._14_05_price);

        titleTv.setMovementMethod(new ScrollingMovementMethod());


        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                BookVO[] bookdetail = (BookVO[]) bundle.getSerializable("BOOKLIST");
                final String myUrl = bookdetail[0].getBimgurl();

                Thread t2 = new Thread(new Runnable(){
                    @Override
                    public void run() {    // 오래 거릴 작업을 구현한다
                        try {
                            // 걍 외우는게 좋다 -_-;
                            URL url = new URL(myUrl);
                            InputStream is = url.openStream();
                            final Bitmap bm = BitmapFactory.decodeStream(is);
                            handler1.post(new Runnable() {

                                @Override
                                public void run() {  // 화면에 그려줄 작업
                                    imgIv.setImageBitmap(bm);
                                }
                            });
                            imgIv.setImageBitmap(bm); //비트맵 객체로 보여주기
                        } catch (Exception e) {

                        }

                    }
            });

                t2.start();


                titleTv.setText(bookdetail[0].getBtitle());
                isbnTv.setText(bookdetail[0].getBisbn());
                authorTv.setText(bookdetail[0].getBauthor());
                priceTv.setText(String.valueOf(bookdetail[0].getPrice()) + "원");
                //imgIv.setImageBitmap();


            }
        };
        DetaliBookInfo runnable = new DetaliBookInfo(handler, isbn);
        Thread t = new Thread(runnable);
        t.start();


    }


}


class DetaliBookInfo implements Runnable {

    private Handler handler;
    private String keyword;

    public DetaliBookInfo() {
    }

    public DetaliBookInfo(Handler handler, String keyword) {
        this.handler = handler;
        this.keyword = keyword;
    }

    @Override
    public void run() {
        String url = "http://70.12.60.97:8080/booksearch/bookDetail?keyword=" + keyword;
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
