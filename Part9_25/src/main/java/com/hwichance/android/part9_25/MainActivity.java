package com.hwichance.android.part9_25;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    TextView titleView;
    TextView dateView;
    TextView contentView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleView = findViewById(R.id.lab1_title);
        dateView = findViewById(R.id.lab1_date);
        contentView = findViewById(R.id.lab1_content);
        imageView = findViewById(R.id.lab1_image);

        // 서버에 전송할 데이터
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "kkang");

        //  문자열 http 요청
        HttpRequester httpRequester = new HttpRequester();
        httpRequester.request("http://~~~~:8000/files/test.json", map, httpCallback);
    }

    // 문자열 결과 획득 Callback 클래스
    HttpCallback httpCallback = new HttpCallback() {
        @Override
        public void onResult(String result) {
            // 결과 json 파싱
            try {
                JSONObject root = new JSONObject(result);
                titleView.setText(root.getString("title"));
                dateView.setText(root.getString("date"));
                contentView.setText(root.getString("content"));

                String imageFile = root.getString("file");
                if (imageFile != null && !imageFile.equals("")) {
                    // 결과 문자열에 이미지 파일 정보가 있다면 다시 이미지 데이터 요청
                    HttpImageRequester imageRequester = new HttpImageRequester();
                    imageRequester.request("http://~~~~:8000/files/" + imageFile, null, imageCallback);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    // 획득한 이미지 데이터를 받기 위한 Callback
    HttpImageCallback imageCallback = new HttpImageCallback() {
        @Override
        public void onResult(Bitmap d) {
            imageView.setImageBitmap(d);
        }
    };

}
