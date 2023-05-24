package com.example.dreamdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// 상세보기 화면 or 작성하기 화면
public class DiaryDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvDate; // date 설정 텍스트
    private EditText mEtTitle, mEtContent; // 다이어리 제목, 다이어리 내용
    private RadioGroup mRgMood; // 기분

    private String mDetailMode = ""; // intent로 받아낸 게시글 모드

    private String mBeforeDate = ""; // intent로 받아낸 게시글 기존 작성 일자
    private String mSelectedUserDate = ""; // 선택된 date 값

    private int mSelectedMoodType = -1; // 선택된 기분 값 (1~6까지 존재)

    private DatabaseHelper mDatabaseHelper; // DB Util 객체

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);

        // DB 객체 생성
        mDatabaseHelper = new DatabaseHelper(this);

        mTvDate = findViewById(R.id.tv_date); // date 설정
        mEtTitle = findViewById(R.id.et_title); // 제목 입력
        mEtContent = findViewById(R.id.et_content); // 내용 입력
        mRgMood = findViewById(R.id.rg_mood); // 기분 선택 radio

        ImageView iv_back = findViewById(R.id.iv_back); // 뒤로가기 버튼
        ImageView iv_check = findViewById(R.id.iv_check); // 작성 완료 버튼

        // 클릭 기능 부여
        mTvDate.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_check.setOnClickListener(this);

        // 안드로이드 기능, 기본으로 설정할 날짜의 값을 지정 "----년/--월/--일 -요일". new Date() 디바이스 현재 날짜를 기준으로 가지고 와줌
        mSelectedUserDate = new SimpleDateFormat("yyyy/MM/dd E요일", Locale.KOREAN).format(new Date());
        mTvDate.setText(mSelectedUserDate);
    }

    @Override
    public void onClick(View view) {
        // setOnClickListener가 붙어있는 뷰들은 클릭이 발생하면 모두 이곳을 실행함

        if (view.getId() == R.id.iv_back) { // 뒤로 가기 버튼의 id
            finish(); // 현재 activity를 종료하고 이전 activity로 이동
        }
        else if(view.getId() == R.id.iv_check) { // 작성 완료 이미지뷰의 id
            // 현재 클릭되어 있는 moodType Radio 버튼의 id 값 가져오기
            mSelectedMoodType = mRgMood.indexOfChild(findViewById(mRgMood.getCheckedRadioButtonId()));

            // 입력 필드 작성란이 공백인지 체크
            if (mEtTitle.getText().length() == 0 || mEtContent.getText().length() ==0 ) {
                // text 글자수가 0이면 에러
                Toast.makeText(this,"내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                return; // 아래의 코드를 실행하지 않고 다시 되돌려 보냄
            }

            // 기분 선택이 되어있는지 체크
            if(mSelectedMoodType == -1) { // 위에서 mSelecteMoodType의 기본값을 -1로 했기 때문에 선택하지 않으면 -1
                Toast.makeText(this,"기분을 선택해주세요",Toast.LENGTH_SHORT).show();
                return;
            }

            // 위의 값들이 정상적으로 입력되어 있다면 데이터 저장
            String title = mEtTitle.getText().toString(); // 제목 입력 값
            String content = mEtContent.getText().toString(); // 내용 입력 값
            String userDate = mSelectedUserDate; // 유저가 선택한 Date 값

            String writeDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.KOREAN).format(new Date()); // DB에 저장하기 위한 Date값(중복 방지로 시간까지 기록)

            // DB 저장하기
            mDatabaseHelper.setInsertDiaryList(title, content, mSelectedMoodType, userDate, writeDate);
            Toast.makeText(this,"다이어리 저장이 완료 되었습니다!",Toast.LENGTH_SHORT).show();

            finish();
        }
        else if(view.getId() == R.id.tv_date) { // 날짜 선택 영역의 id

            // 달력 띄우기, 사용자에게 날짜 입력 받기
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                    // 달력에 선택 된 년,월,일을 가지고 와서 다시 캘린더 함수에 넣어준 후 사용자가 선택한 요일을 알아내기
                    Calendar innerCal = Calendar.getInstance();
                    innerCal.set(Calendar.YEAR, year);
                    innerCal.set(Calendar.MONTH, month);
                    innerCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    mSelectedUserDate = new SimpleDateFormat("yyyy/MM/dd E요일", Locale.KOREAN).format(innerCal.getTime());
                    mTvDate.setText(mSelectedUserDate);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            dialog.show(); // 달력 팝업 보여주기
        }
    }
}
