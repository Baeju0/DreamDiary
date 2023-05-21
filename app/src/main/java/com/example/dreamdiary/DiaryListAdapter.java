package com.example.dreamdiary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


// 빨간 줄 Alt+Enter 매직,,,
public class DiaryListAdapter extends RecyclerView.Adapter<DiaryListAdapter.ViewHolder> {
    ArrayList<DiaryModel> mLstDiary; // 다이어리 데이터들을 들고 있는 배열
    Context mContext; // Context, 화면 단위에서 모든 데이터들을 닮고 있는 역할

    @NonNull
    @Override // onCreateViewHolder가 뭐지? 아이템 뷰가 최초로 생성이 될 때 호출되는 곳
    public DiaryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View holder = LayoutInflater.from(mContext).inflate(R.layout.list_item_diary, parent, false); // inflate list 붙이는 역할
        return new ViewHolder(holder);
    }

    // onBindViewHolder 생성된 아이템 뷰가 실제 연동이 되어지는 곳
    @Override
    public void onBindViewHolder(@NonNull DiaryListAdapter.ViewHolder holder, int position) {
        // 기분의 경우의 수
        int moodType = mLstDiary.get(position).getMoodTpye();
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
            tv_user_date = itemView.findViewById(R.id.tv_user_date); // 작성한 날짜
        }
    }

    public void setSampleList(ArrayList<DiaryModel> lstDiary) {
        mLstDiary = lstDiary;
    }
}
