package com.diary.dreamdiary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dreamdiary.R;

import java.util.ArrayList;


// 빨간 줄 Alt+Enter 매직,,,
public class DiaryListAdapter extends RecyclerView.Adapter<DiaryListAdapter.ViewHolder> {
    ArrayList<DiaryModel> mLstDiary; // 다이어리 데이터들을 들고 있는 배열
    Context mContext; // Context, 화면 단위에서 모든 데이터들을 담고 있는 역할
    DatabaseHelper mDatabaseHelper; // DB Helper 클래스

    @NonNull
    @Override // onCreateViewHolder가 뭐지? 아이템 뷰가 최초로 생성이 될 때 호출되는 곳
    public DiaryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mDatabaseHelper = new DatabaseHelper(mContext);
        View holder = LayoutInflater.from(mContext).inflate(R.layout.list_item_diary, parent, false); // inflate list 붙이는 역할
        return new ViewHolder(holder);
    }

    // onBindViewHolder 생성된 아이템 뷰가 실제 연동이 되어지는 곳
    @Override
    public void onBindViewHolder(@NonNull DiaryListAdapter.ViewHolder holder, int position) {
        // 기분의 경우의 수
        int moodType = mLstDiary.get(position).getMoodType();
        switch (moodType) {
            case 0: // 행복
                holder.iv_mood.setImageResource(R.drawable.happy);
                break;
            case 1: // 슬픔
                holder.iv_mood.setImageResource(R.drawable.sad);
                break;
            case 2: // 분노
                holder.iv_mood.setImageResource(R.drawable.angry);
                break;
            case 3: // 두려움
                holder.iv_mood.setImageResource(R.drawable.screaming);
                break;
            case 4: // 들뜸
                holder.iv_mood.setImageResource(R.drawable.exciting);
                break;
            case 5: // 평온
                holder.iv_mood.setImageResource(R.drawable.expressionless);
                break;
        }

        // 기분, 날짜
        String title = mLstDiary.get(position).getTitle();
        String userDate = mLstDiary.get(position).getUserDate();

        holder.tv_title.setText(title);
        holder.tv_user_date.setText(userDate);

    }

    // getItemCount 아이템 뷰의 총 갯수를 관리하는 곳
    @Override
    public int getItemCount() {
        return mLstDiary.size();
    }

    // ViewHolder 하나의 아이템 뷰
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_mood;
        TextView tv_title, tv_user_date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_mood = itemView.findViewById(R.id.iv_mood); // 기분 이미지
            tv_title = itemView.findViewById(R.id.tv_title); // 제목
            tv_user_date = itemView.findViewById(R.id.tv_user_date); // 사용자가 선택한 날짜

            // 클릭, 상세 보기
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 현재 클릭이 된 위치 (배열 개념, 0부터 세는 기준)
                    int currentPosition = getAdapterPosition(); // Adapter 클래스 상속

                    // 현재 클릭된 리스트 아이템의 정보를 받는 변수
                    DiaryModel diaryModel = mLstDiary.get(currentPosition);

                    // 화면 이동, 다이어리 데이터를 다음 화면으로 전달
                    Intent diaryDetailIntent = new Intent(mContext, DiaryDetailActivity.class);
                    diaryDetailIntent.putExtra("diaryModel",diaryModel); // 다이어리 데이터 넘기기
                    diaryDetailIntent.putExtra("mode","detail"); // 상세보기 모드로 설정
                    mContext.startActivity(diaryDetailIntent);
                }
            });

            // 선택지 옵션 팝업 (수정 & 삭제)
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // 현재 클릭이 된 위치 (배열 개념, 0부터 세는 기준)
                    int currentPosition = getAdapterPosition(); // Adapter 클래스 상속

                    // 현재 클릭된 리스트 아이템의 정보를 받는 변수
                    DiaryModel diaryModel = mLstDiary.get(currentPosition);

                    // 버튼 선택지 (수정, 삭제)
                    String[] strChoiceArray = {"수정 하기", "삭제 하기"};

                    // 팝업 화면 표시
                    new AlertDialog.Builder(mContext)
                            .setTitle("원하시는 동작을 선택하세요")
                            .setItems(strChoiceArray, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int position) {
                                    if (position == 0) {
                                        // 수정하기 버튼

                                        // 화면 이동, 다이어리 데이터를 다음 화면으로 전달
                                        Intent diaryDetailIntent = new Intent(mContext, DiaryDetailActivity.class);
                                        diaryDetailIntent.putExtra("diaryModel",diaryModel); // 다이어리 데이터 넘기기
                                        diaryDetailIntent.putExtra("mode","modify"); // 상세보기 모드로 설정
                                        mContext.startActivity(diaryDetailIntent);
                                    } else {
                                        // 삭제하기 버튼
                                        mDatabaseHelper.setDeleteDiaryList(diaryModel.getWriteDate()); // DB에서 삭제
                                        mLstDiary.remove(currentPosition); // mLstDiary.remove로 제거한 배열은 데이터만 제거된 것이기 때문에 UI에서 사라지지 않음
                                        notifyItemRemoved(currentPosition); // 그래서 notifyItemRemoved를 사용하여 리스트 UI까지 제거
                                    }
                                }
                            }).show();

                    return false;
                }
            });
        }
    }

    public void setListInit(ArrayList<DiaryModel> lstDiary) {
        // 데이터 리스트 업데이트
        mLstDiary = lstDiary;
        notifyDataSetChanged(); // 리스트 뷰 새로고침
    }
}
