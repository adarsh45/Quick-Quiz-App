package com.example.quizitup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizitup.R;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private List<String> questionsList;
    private OnQuestionClickListener onQuestionClickListener;

    public QuestionAdapter(List<String> questionsList, OnQuestionClickListener onQuestionClickListener){
        this.questionsList = questionsList;
        this.onQuestionClickListener = onQuestionClickListener;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_question,parent,false);
        return new QuestionViewHolder(view, onQuestionClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public OnQuestionClickListener onQuizCardClickListener;
        public QuestionViewHolder(@NonNull View itemView, OnQuestionClickListener onQuestionClickListener) {
            super(itemView);
            this.onQuizCardClickListener = onQuestionClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onQuizCardClickListener.onQuestionClick(getAdapterPosition());
        }
    }

    public interface OnQuestionClickListener {
        void onQuestionClick(int position);
    }
}
