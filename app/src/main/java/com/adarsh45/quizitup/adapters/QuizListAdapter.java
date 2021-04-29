package com.adarsh45.quizitup.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adarsh45.quizitup.R;
import com.adarsh45.quizitup.pojos.Class;
import com.adarsh45.quizitup.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class QuizListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Map<String, Class.Quiz> quizList;
    private byte origin;
    private String studentId;
    private String classInviteCode;
    private OnAddQuizClickListener onAddQuizClickListener;
    private OnQuizCardClickListener onQuizCardClickListener;

    public QuizListAdapter(Map<String, Class.Quiz> quizList, byte origin, String studentId, String classInviteCode, OnAddQuizClickListener onAddQuizClickListener, OnQuizCardClickListener onQuizCardClickListener){
        this.quizList = quizList;
        this.origin = origin;
        this.studentId = studentId;
        this.classInviteCode = classInviteCode;
        this.onAddQuizClickListener = onAddQuizClickListener;
        this.onQuizCardClickListener = onQuizCardClickListener;
    }

    @Override
    public int getItemViewType(int position) {
//        this will return 0 for last item & 1 for all the others
        if (position == quizList.size() - 1 ){
            return 0;
        }
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType != 0){
//            this is any item other than last
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_quiz_card,parent,false);
            return new QuizCardViewHolder(view, onQuizCardClickListener);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_add_quiz,parent,false);
        return new AddQuizViewHolder(view, onAddQuizClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() != 0){
//            this is any item other than last
            Class.Quiz quizData = quizList.get(position + "_id");

            if (origin == Utils.STUDENT_DATA){
                FirebaseDatabase.getInstance()
                        .getReference("Students").child(studentId)
                        .child("attemptedQuizzesMap").child(classInviteCode)
                        .child(position + "_id")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    ((QuizCardViewHolder)holder).tvQuizStatus.setText("Submitted");
                                    ((QuizCardViewHolder) holder).imgQuizStatus.setImageResource(R.drawable.ic_check_circle_solid);
                                } else {
                                    ((QuizCardViewHolder)holder).tvQuizStatus.setText("Pending");
                                    ((QuizCardViewHolder) holder).imgQuizStatus.setImageResource(R.drawable.ic_baseline_pending_24);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("TAG", "onCancelled: " + error.getMessage());
                            }
                        });

            } else if (origin == Utils.TEACHER_DATA){
                ((QuizCardViewHolder)holder).tvQuizStatus.setText(quizData.getStatus());

                if (quizData.getStatus().equals("Create Mode")){
                    ((QuizCardViewHolder) holder).imgQuizStatus.setImageResource(R.drawable.ic_baseline_pending_24);
                }
                if (quizData.getStatus().equals("Published")){
                    ((QuizCardViewHolder) holder).imgQuizStatus.setImageResource(R.drawable.ic_check_circle_solid);
                }
                if (quizData.getStatus().equals("Closed")){
                    ((QuizCardViewHolder) holder).imgQuizStatus.setImageResource(R.drawable.ic_baseline_cancel_24);
                }
            }

            ((QuizCardViewHolder)holder).tvQuizTitle.setText(quizData.getQuizTitle());

            if (quizData.getQuestionMap() == null || quizData.getQuestionMap().size() == 0){
                ((QuizCardViewHolder)holder).tvQuestionsCount.setText("Questions: 0");
            } else {
                ((QuizCardViewHolder)holder).tvQuestionsCount.setText("Questions: " + quizData.getQuestionMap().size());
            }

        } else if(origin == Utils.STUDENT_DATA){
            holder.itemView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public static class QuizCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public OnQuizCardClickListener onQuizCardClickListener;
        public TextView tvQuizTitle, tvQuestionsCount, tvQuizStatus;
        public ImageView imgQuizStatus;
        public QuizCardViewHolder(@NonNull View itemView, OnQuizCardClickListener onQuizCardClickListener) {
            super(itemView);

            tvQuizTitle = itemView.findViewById(R.id.tv_rv_quiz_title);
            tvQuestionsCount = itemView.findViewById(R.id.tv_rv_quiz_questions);
            tvQuizStatus = itemView.findViewById(R.id.tv_rv_quiz_status);
            imgQuizStatus = itemView.findViewById(R.id.img_rv_quiz_status);

            this.onQuizCardClickListener = onQuizCardClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onQuizCardClickListener.onQuizClick(getAdapterPosition());
        }
    }

    public static class AddQuizViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public OnAddQuizClickListener onAddQuizClickListener;
        public AddQuizViewHolder(@NonNull View itemView, OnAddQuizClickListener onAddQuizClickListener) {
            super(itemView);
            this.onAddQuizClickListener = onAddQuizClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onAddQuizClickListener.onAddQuizClick(getAdapterPosition());
        }
    }

    public interface OnAddQuizClickListener {
        void onAddQuizClick(int position);
    }

    public interface OnQuizCardClickListener {
        void onQuizClick(int position);
    }
}
