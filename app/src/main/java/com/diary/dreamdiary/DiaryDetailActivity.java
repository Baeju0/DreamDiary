package com.diary.dreamdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.diary.dreamdiary.R;
import com.google.android.material.radiobutton.MaterialRadioButton;

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

        // 이전 액티비티로부터 값을 전달 받기
        Intent intent = getIntent();
        if (intent.getExtras() != null) { // intent안에 값이 존재하는지?
            // 비어있지 않다면(intent putExtra의 데이터가 존재한다면 수행)
            DiaryModel diaryModel = (DiaryModel) intent.getSerializableExtra("diaryModel"); // DiaryListAdapter 클래스에 있는 id 가져옴 (LongClick ~에 있는)
            mDetailMode = intent.getStringExtra("mode");
            mBeforeDate = diaryModel.getWriteDate(); // 게시글 DB UPDATE 쿼리문 처리를 위해 여기서 받아둠

            // 넘겨받은 값을 활용해서 각 필드들에 설정해주기
            mEtTitle.setText(diaryModel.getTitle());
            mEtContent.setText(diaryModel.getContent());
            int moodType = diaryModel.getMoodType();
            ((MaterialRadioButton) mRgMood.getChildAt(moodType)).setChecked(true); // getChildAt = Radio 그룹의 index를 가져오기, MaterialRadioButton = Radio버튼을 의미하는 건가? 확인차
            mSelectedUserDate = diaryModel.getUserDate();
            mTvDate.setText(diaryModel.getUserDate());

            if (mDetailMode.equals("modify")) {
                // 수정하기 모드
                TextView tv_header_title = findViewById(R.id.tv_header_title);
                tv_header_title.setText("일기 수정");

            } else if (mDetailMode.equals("detail")) {
                // 상세보기 모드
                TextView tv_header_title = findViewById(R.id.tv_header_title);
                tv_header_title.setText("일기 상세보기");

                // 읽기 전용 화면으로 표시
                mEtTitle.setEnabled(false); // setEnabled = 활성화 상태, 비활성화 상태를 줄 수 있음
                mEtContent.setEnabled(false);
                mTvDate.setEnabled(false);

                // radio 버튼 막기 (6개의 버튼을 for 반복문으로 막기)
                for (int i = 0; i < mRgMood.getChildCount(); i++) {
                    mRgMood.getChildAt(i).setEnabled(false);
                }

                // 작성 완료 체크 표시를 투명으로 처리하기
                iv_check.setVisibility(View.INVISIBLE);
            }

        }
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
            if (userDate.equals("")) {
                // 별도의 날짜를 지정하지 않고 작성 완료를 누를 경우
                userDate = mTvDate.getText().toString();
            }

            String writeDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.KOREAN).format(new Date()); // DB에 저장하기 위한 Date값(중복 방지로 시간까지 기록)

            // DB 저장하기 (Activity의 현재 모드에 따라서 저장 or 수정)
            if (mDetailMode.equals("modify")) {
                // 게시글 수정 모드
                mDatabaseHelper.setUpdateDiaryList(title, content, mSelectedMoodType, userDate, writeDate, mBeforeDate);
                Toast.makeText(this,"다이어리 수정이 완료 되었습니다!",Toast.LENGTH_SHORT).show();
            } else {
                // 게시글 작성 모드
                mDatabaseHelper.setInsertDiaryList(title, content, mSelectedMoodType, userDate, writeDate);
                Toast.makeText(this,"다이어리 저장이 완료 되었습니다!",Toast.LENGTH_SHORT).show();
            }
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
