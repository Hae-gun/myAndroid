package com.example.androidlectureexample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class Example17Sub_KAKAOBookSearchService extends Service {
    public Example17Sub_KAKAOBookSearchService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Activity 로 부터 전달된 intent 를 이용하여 keyword 를 얻어내기.
        String keyword = intent.getExtras().getString("KEYWORD");
        KakaoBookSearchRunnable runnable = new KakaoBookSearchRunnable(keyword);
        Thread t = new Thread(runnable);
        t.start();
        // Network 연결을 통해 Oepn API 를 호출하는 시간이 걸리는 작업을 수행.
        // Thread 를 이용하여 처리해야 한다.

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class KakaoBookSearchRunnable implements Runnable {
        private String keyword;

        KakaoBookSearchRunnable() {
        }

        KakaoBookSearchRunnable(String keyword) {
            this.keyword = keyword;
        }

        @Override
        public void run() {
            // Network 접속을 통해 Open API 를 호출.
            // KAKAO Developer 사이트에서 OEPN API 의 호출방식을 알아내야 한다.
            String url = "https://dapi.kakao.com/v3/search/book?target=title";
            url += ("&query=" + keyword);

            // 주소를 완성했으면 이 주소를 이용하여 network 연결을 해야한다.
            // Java 에서 network 연결은 예외상황이 발생할 여지가 있다.
            // Java 는 network 처리에 대해서 Exception Handling 을 강제하고 있다.
            try {
                // 1. HTTP 접속을 하기 위해 URL 객체를 생성.
                URL obj = new URL(url);
                // 2. URL Connection 을 연다.
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                // 3. 연결에 대한 설정이 들어가야 한다.
                // 대표적인 설정 : 호출방식(GET,POST), 인증에 대한 처리
                con.setRequestMethod("GET"); // API 를 보고 결정.
                con.setRequestProperty("Authorization",
                        "KakaoAK 56503484476b1750deebd867d290f618"); // 인증처리.
                // 일단 접속이 성공(정상적으로 처리가 되면)
                // 접속이 성공하면 결과 데이터를 JSON 으로 보내주게 되고
                // 데이터 연결통로(Stream)을 열어서 데이터를 읽어온다.
                // 데이터 연결통로는 기본적인 Stream 을 먼저 얻고 일르 이용해서
                // 조금 더 사용하기 편한 통로로 변경해서 사용한다..
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String line;
                StringBuffer sb = new StringBuffer();

                // 반복적으로 서버가 보내준 데이터를 읽는다.
                while ((line = br.readLine()) != null) { // 끝까지 읽으면 null 이나옴.
                    sb.append(line);
                }
                br.close();
                Log.i("KAKAO", sb.toString());
                // 데이터가 JSON 으로 정삭적으로 온걸 확인할 수 있다.
                // `documents : [ {책1권}, {책1권}, {책1권}, ...]`
                // JSON 을 처리해서 documents 라고 되어있는 key 값에 대해
                // Value 값을 객체화 하여 가져온다.
                ObjectMapper mapper = new ObjectMapper();
                // 먼저 JSON 을 읽어서 Map 객체화 시킨다. key : documents, value : 뒤 배열.
                // 뒤에 책 배열을 문자열로 꺼내올 방법이 없기 때문에 Map 객체화 하여 가져오는 것.
                Map<String, Object> map =
                        mapper.readValue(sb.toString(),
                                new TypeReference<Map<String, Object>>() {
                                });

                Object jsonObject = map.get("documents");
                // jsonObject => [ {책1권}, {책1권}, {책1권}, ...] 의 객체화.

                String jsonString = mapper.writeValueAsString(jsonObject);
                // jsonString => "[ {책1권}, {책1권}, {책1권}, ...] "

                Log.i("KAKAO", jsonString);
                // ArrayList 안에 책 객체가 들어있는 형태로 만든다. ( VO 를 만들어야 한다.)

                ArrayList<KAKAOBookVO> searchBooks =
                        mapper.readValue(jsonString,
                                new TypeReference<ArrayList<KAKAOBookVO>>() {
                                });

                ArrayList<String> resultData = new ArrayList<String>();

                for (KAKAOBookVO vo : searchBooks) {
                    resultData.add(vo.getTitle());
                }
                Intent i = new Intent(getApplicationContext(),
                        Example17_KAKAOBookSearchActivity.class);
                i.putExtra("BOOKRESULT",resultData);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);



                // 책 제목만 들어있는 ArrayList를 만들어 Activity 에게 보내주면 된다.

            } catch (Exception e) {
                Log.i("KAKAO", e.toString());
            }
        }

    }
}


class KAKAOBookVO {
    private ArrayList<String> authors;
    private ArrayList<String> translators;
    private String contents;
    private String datetime;
    private String isbn;
    private String price;
    private String publisher;
    private String sale_price;
    private String status;
    private String thumbnail;
    private String title;
    private String url;

    // 기본 constructor
    public KAKAOBookVO() {

    }

    // ahems field 를 이용하는 constructor
    public KAKAOBookVO(ArrayList<String> authors, ArrayList<String> translators, String contents, String datetime, String isbn, String price, String publisher, String sale_price, String status, String thumbnail, String title, String url) {
        this.authors = authors;
        this.translators = translators;
        this.contents = contents;
        this.datetime = datetime;
        this.isbn = isbn;
        this.price = price;
        this.publisher = publisher;
        this.sale_price = sale_price;
        this.status = status;
        this.thumbnail = thumbnail;
        this.title = title;
        this.url = url;
    }

    // getter & setter
    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public ArrayList<String> getTranslators() {
        return translators;
    }

    public void setTranslators(ArrayList<String> translators) {
        this.translators = translators;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}