package com.example.dreamdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity { // 시작 화면

    @Override
    protected void onCreate(Bundle savedInstanceState) { // onCreate()는 화면(Activity)이 시작할 때 가장 먼저 실행되는 곳, 안드로이드 액티비티 생명주기
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // res/layout/activity_splash 경로를 뜻하는 것, 연결 되어있다.

        // delay 발생시킨 후 SplashActivity 1~2초 간 보인 후 MainActivity로 이동시키기
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // 메인 액티비티로 이동 시키기
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class); // Intent(현재 activity, 이동하고 싶은 activity)
                startActivity(mainIntent); // 실행시킬 Activity
                finish(); // 현재 화면(SplashActivity)은 끝냄, 앱 실행 후 처음에만 나오기 때문에
            }
        }, 2000); // 1.5초
    }
}