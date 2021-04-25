package com.example.quizitup.adapters;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizitup.R;
import com.example.quizitup.pojos.Class;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private Map<String, Class.Question> questionsList;
    private OnQuestionClickListener onQuestionClickListener;

    public QuestionAdapter(Map<String, Class.Question> questionsList, OnQuestionClickListener onQuestionClickListener){
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
        Class.Question question = questionsList.get(position + "_id");
        Log.d("TAG", "onBindViewHolder: " + question.getQuePosition());
        Log.d("TAG", "onBindViewHolder: " + question.getQuePosition().split("_")[0]);
        int quePosition = Integer.parseInt(question.getQuePosition().split("_")[0]) + 1;
        String queTitle = "Q." + quePosition + " " + question.getQueTitle();
        holder.tvQueTitle.setText(queTitle);
        holder.tvOp1.setText(question.getOption1());
        holder.tvOp2.setText(question.getOption2());
        holder.tvOp3.setText(question.getOption3());
        holder.tvOp4.setText(question.getOption4());

        holder.imgTick1.setVisibility(View.INVISIBLE);
        holder.imgTick2.setVisibility(View.INVISIBLE);
        holder.imgTick3.setVisibility(View.INVISIBLE);
        holder.imgTick4.setVisibility(View.INVISIBLE);

        if (question.getExplanation() != null && !TextUtils.isEmpty(question.getExplanation())){
            holder.tvExplanation.setText(question.getExplanation());
        }

        holder.checkExplanation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                holder.tvExplanation.setVisibility(View.VISIBLE);
            } else {
                holder.tvExplanation.setVisibility(View.GONE);
            }
        });

//        show check mark in front of correct option
        if (question.getCorrectOption().equals(question.getOption1())){
            holder.imgTick1.setVisibility(View.VISIBLE);
        }
        if (question.getCorrectOption().equals(question.getOption2())){
            holder.imgTick2.setVisibility(View.VISIBLE);
        }
        if (question.getCorrectOption().equals(question.getOption3())){
            holder.imgTick3.setVisibility(View.VISIBLE);
        }
        if (question.getCorrectOption().equals(question.getOption4())){
            holder.imgTick4.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public OnQuestionClickListener onQuizCardClickListener;
        public TextView tvQueTitle, tvOp1, tvOp2, tvOp3, tvOp4;
        public ImageView imgTick1, imgTick2, imgTick3, imgTick4;
        public TextView tvExplanation;
        public CheckBox checkExplanation;
        public QuestionViewHolder(@NonNull View itemView, OnQuestionClickListener onQuestionClickListener) {
            super(itemView);
            this.onQuizCardClickListener = onQuestionClickListener;
            tvQueTitle = itemView.findViewById(R.id.tv_que_title);
            tvOp1 = itemView.findViewById(R.id.tv_op_1);
            tvOp2 = itemView.findViewById(R.id.tv_op_2);
            tvOp3 = itemView.findViewById(R.id.tv_op_3);
            tvOp4 = itemView.findViewById(R.id.tv_op_4);
            imgTick1 = itemView.findViewById(R.id.img_tick_1);
            imgTick2 = itemView.findViewById(R.id.img_tick_2);
            imgTick3 = itemView.findViewById(R.id.img_tick_3);
            imgTick4 = itemView.findViewById(R.id.img_tick_4);

            tvExplanation = itemView.findViewById(R.id.tv_explanation_student);
            checkExplanation = itemView.findViewById(R.id.check_explanation_student);

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
