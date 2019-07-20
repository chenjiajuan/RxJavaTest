package com.chenjiajuan.rxjavatest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chenjiajuan.home.HomeActivity;

public class QRCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        startActivity(new Intent(this, HomeActivity.class));
        // 触发一次构建测试
    }
}
