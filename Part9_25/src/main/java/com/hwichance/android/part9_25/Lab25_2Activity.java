package com.hwichance.android.part9_25;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class Lab25_2Activity extends AppCompatActivity {

    TextView titleView;
    TextView dateView;
    TextView contentView;
    NetworkImageView imageView;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab25_2);

        titleView = findViewById(R.id.lab2_title);
        dateView = findViewById(R.id.lab2_date);
        contentView = findViewById(R.id.lab2_content);


        imageView = findViewById(R.id.lab2_image);
        queue = Volley.newRequestQueue(this);

        // 문자열 Request 정보를 담은 StringRequest 객체 생성
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, "http://~~~~:8000/files/test.json",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // 서버 응답 후 사후 처리
                // JSONObject에서 데이터 획득
                try {
                    titleView.setText(response.getString("title"));
                    dateView.setText(response.getString("date"));
                    contentView.setText(response.getString("content"));

                    String imageFile = response.getString("file");
                    if (imageFile != null && !imageFile.equals("")) {
                        ImageLoader imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
                            @Override
                            public Bitmap getBitmap(String url) {
                                return null;
                            }

                            @Override
                            public void putBitmap(String url, Bitmap bitmap) {

                            }
                        });
                        // 이미지 요청
                        imageView.setImageUrl("http://~~~~:8000/files/" + imageFile, imageLoader);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonRequest);
    }
}
