package com.diary.dreamdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.diary.dreamdiary.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // 전역 class
    RecyclerView RvDiary; // 리사이클러 뷰(리스트)
    DiaryListAdapter mAdapter; // 리사이클러 뷰와 연동할 어댑터
    ArrayList<DiaryModel> mLstDiary; // 리스트에 표현할 다이어리 데이터(배열)
    DatabaseHelper mDatabaseHelper; // DB 헬퍼 클래스

    // 화면이 실행될 때 가장 먼저 호출되는 곳
    @Override
    protected void onCreate(Bundle savedInstanceState) { // onCreate, 화면이 시작할 때 딱 1회만 실행
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DB 객체 초기화
        mDatabaseHelper = new DatabaseHelper(this);

        mLstDiary = new ArrayList<>();

        RvDiary = findViewById(R.id.list_diary);

        // 리사이클러 뷰 어댑터의 인스턴스 생성
        mAdapter = new DiaryListAdapter();

        // 다이어리 아이템 예시
//        DiaryModel item = new DiaryModel();
//        item.setId(0);
//        item.setTitle("Hello World!");
//        item.setContent("어렵고만..");
//        item.setUserDate("2023/05/21/일");
//        item.setWriteDate("2023/05/21/일");
//        item.setMoodTpye(0);
//        mLstDiary.add(item);
//
//        DiaryModel item2 = new DiaryModel();
//        item2.setId(0);
//        item2.setTitle("안녕하세요!");
//        item2.setContent("안드로이드...");
//        item2.setUserDate("2023/06/21/월");
//        item2.setWriteDate("2023/06/21/월");
//        item2.setMoodTpye(2);
//        mLstDiary.add(item2);
//
//        DiaryModel item3 = new DiaryModel();
//        item3.setId(0);
//        item3.setTitle("쉽지 않다...");
//        item3.setContent("스튜디오.....");
//        item3.setUserDate("2023/06/21/월");
//        item3.setWriteDate("2023/06/21/월");
//        item3.setMoodTpye(1);
//        mLstDiary.add(item3);

        RvDiary.setAdapter(mAdapter);

        // 버튼 가져오기
        FloatingActionButton floatingActionButton = findViewById(R.id.btn_write);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override // 작성하기 버튼을 누를 때 호출되는 부분
            public void onClick(View view) {
                
                // 작성하기 화면으로 이동
                Intent intent = new Intent(MainActivity.this, DiaryDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() { // activity를 시작할 때도 호출이 되고, 데이터의 변경 사항이 있을 때 activity를 다시 호출
        super.onResume();

        // get load list
        setLoadRecentList();
    }



    private void setLoadRecentList() {
        // 최근 DB 정보를 가지고 와서 리사이클러뷰에 갱신
        if (!mLstDiary.isEmpty()) {// DB리스트가 비어있는 상태가 아니라면
            mLstDiary.clear();
        }

        mLstDiary = mDatabaseHelper.getDiaryListFromDB(); // DB로부터 저장되어있는 DB를 확인하여 가져옴
        mAdapter.setListInit(mLstDiary);
    }
}