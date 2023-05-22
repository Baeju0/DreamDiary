package com.example.dreamdiary;

import java.io.Serializable;

// 다이어리 리스트 아이템을 구성하는 모델(다이어리 안에 표시될 부분)
public class DiaryModel implements Serializable { // Serializable 데이터를 한 묶음으로 전송
    int id; // 게시물 마다의 고유의 id값
    String title; // 게시물 제목
    String content; // 게시물 내용
    int moodTpye; // 기분 타입 (0:행복 1:슬픔 2:분노 3:두려움 4:들뜸 5:평온)
    String userDate; // 사용자가 지정한 기분에 대한 날짜
    String writeDate; // 게시글을 작성한 날짜


    // getter, setter 클래스에 대해서 데이터 넣기, 불러오기
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMoodTpye() {
        return moodTpye;
    }

    public void setMoodTpye(int moodTpye) {
        this.moodTpye = moodTpye;
    }

    public String getUserDate() {
        return userDate;
    }

    public void setUserDate(String userDate) {
        this.userDate = userDate;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }
}
