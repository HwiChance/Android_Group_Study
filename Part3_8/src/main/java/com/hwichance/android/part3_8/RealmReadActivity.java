package com.hwichance.android.part3_8;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import io.realm.Realm;

public class RealmReadActivity extends AppCompatActivity {

    TextView titleView;
    TextView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm_read);

        titleView = findViewById(R.id.realm_read_title);
        contentView = findViewById(R.id.realm_read_content);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

        Realm mRealm = Realm.getDefaultInstance();
        MemoVO vo = mRealm.where(MemoVO.class).equalTo("title", title).findFirst();
        titleView.setText(vo.title);
        contentView.setText(vo.content);
    }
}
