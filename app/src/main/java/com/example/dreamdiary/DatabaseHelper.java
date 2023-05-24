package com.example.dreamdiary;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

// 데이터베이스 관리 클래스
// SQLite = 안드로이드에서 지원하는 앱 내부의 DB 시스템
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "DreamDiary.db"; // DB 이름
    private static final int DB_VERSION = 1; // DB버전

    // 생성자 (constructor)
    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { // DB Create,
        // SQL은 DB에 접근하기 위해서 명령을 내리는 쿼리문

        // 테이블 생성 (최초 1회만 생성)
        // 만약 기존의 테이블이 존재하지 않으면 create해라! DiaryInfo를! (int타입의 PRIMARY KEY 고유의 키값을, 자동으로 1씩 증가)
        db.execSQL("CREATE TABLE IF NOT EXISTS DiaryInfo (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " title TEXT NOT NULL," + // title 데이터가 NULL이면 안 됨, 아래의 데이터들도!
                " content TEXT NOT NULL," +
                " moodType INTEGER NOT NULL," +
                " userDate TEXT NOT NULL," +
                " writeDate TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }


    // 다이어리 작성 데이터를 DB에 저장 (INSERTE)
    public void setInsertDiaryList(String _title, String _content, Integer _moodType, String _userDate, String _writeDate) {
        SQLiteDatabase db = getWritableDatabase(); // VALUES의 구조('" + _@@@ + "',)
        db.execSQL("INSERT INTO DiaryInfo (title, content, moodType, userDate, writeDate) VALUES('" + _title + "','" + _content + "','" + _moodType + "','" + _userDate + "','" + _writeDate + "')");

    }

}
