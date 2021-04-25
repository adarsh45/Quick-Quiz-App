package com.example.quizitup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizitup.R;
import com.example.quizitup.pojos.Class;
import com.example.quizitup.pojos.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestionAttemptAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private HashMap<String, Class.Question> questions;
    private HashMap<String, Student.AnsweredQuestion> answeredQuestionHashMap;
    private String studentStatus;
    private OnRadioSelectedListener onRadioSelectedListener;

    public QuestionAttemptAdapter(HashMap<String, Class.Question> questions, String studentStatus, OnRadioSelectedListener onRadioSelectedListener){
        this.questions = questions;
        this.studentStatus = studentStatus;
        this.onRadioSelectedListener = onRadioSelectedListener;
    }

    public QuestionAttemptAdapter(HashMap<String, Class.Question> questions, HashMap<String, Student.AnsweredQuestion> answeredQuestionHashMap, String studentStatus, OnRadioSelectedListener onRadioSelectedListener){
        this.questions = questions;
        this.answeredQuestionHashMap = answeredQuestionHashMap;
        this.studentStatus = studentStatus;
        this.onRadioSelectedListener = onRadioSelectedListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         if (studentStatus.equals("submitted")){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_question, parent, false);
            return new ShowAnswerViewHolder(view);
        }
         if (studentStatus.equals("closed")){
             View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_question, parent, false);
             return new ShowAnswerViewHolder(view);
         }
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_question_attempt, parent, false);
         return new QuestionAttemptViewHolder(view, onRadioSelectedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Class.Question question = questions.get(position + "_id");
        int quePosition = Integer.parseInt(question.getQuePosition().split("_")[0]) + 1;
        String queTitle = "Q." + quePosition + " " + question.getQueTitle();
        if (studentStatus.equals("fresh")){
            QuestionAttemptViewHolder viewHolder = (QuestionAttemptViewHolder) holder;
            viewHolder.tvQueTitle.setText(queTitle);
            viewHolder.radioOption1.setText(question.getOption1());
            viewHolder.radioOption2.setText(question.getOption2());
            viewHolder.radioOption3.setText(question.getOption3());
            viewHolder.radioOption4.setText(question.getOption4());
        } else {
            ShowAnswerViewHolder viewHolder = (ShowAnswerViewHolder) holder;
            viewHolder.tvQueTitle.setText(queTitle);

            viewHolder.tvOp1.setText(question.getOption1());
            viewHolder.tvOp2.setText(question.getOption2());
            viewHolder.tvOp3.setText(question.getOption3());
            viewHolder.tvOp4.setText(question.getOption4());

            //        show check mark in front of correct option
            if (question.getCorrectOption().equals(question.getOption1())){
                viewHolder.imgTick1.setVisibility(View.VISIBLE);
            }
            if (question.getCorrectOption().equals(question.getOption2())){
                viewHolder.imgTick2.setVisibility(View.VISIBLE);
            }
            if (question.getCorrectOption().equals(question.getOption3())){
                viewHolder.imgTick3.setVisibility(View.VISIBLE);
            }
            if (question.getCorrectOption().equals(question.getOption4())){
                viewHolder.imgTick4.setVisibility(View.VISIBLE);
            }

            if (studentStatus.equals("submitted")){
                Student.AnsweredQuestion answeredQuestion = answeredQuestionHashMap.get(question.getQuePosition());
                if (!answeredQuestion.getCorrectOption().equals(answeredQuestion.getSelectedOption())){
//                students answer is wrong
                    if (answeredQuestion.getSelectedOption().equals(question.getOption1())){
                        viewHolder.imgTick1.setImageResource(R.drawable.ic_baseline_cancel_24);
                        viewHolder.imgTick1.setVisibility(View.VISIBLE);
                    }
                    if (answeredQuestion.getSelectedOption().equals(question.getOption2())){
                        viewHolder.imgTick2.setImageResource(R.drawable.ic_baseline_cancel_24);
                        viewHolder.imgTick2.setVisibility(View.VISIBLE);
                    }
                    if (answeredQuestion.getSelectedOption().equals(question.getOption3())){
                        viewHolder.imgTick3.setImageResource(R.drawable.ic_baseline_cancel_24);
                        viewHolder.imgTick3.setVisibility(View.VISIBLE);
                    }
                    if (answeredQuestion.getSelectedOption().equals(question.getOption4())){
                        viewHolder.imgTick4.setImageResource(R.drawable.ic_baseline_cancel_24);
                        viewHolder.imgTick4.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class ShowAnswerViewHolder extends RecyclerView.ViewHolder{

        public TextView tvQueTitle, tvOp1, tvOp2, tvOp3, tvOp4;
        public ImageView imgTick1, imgTick2, imgTick3, imgTick4;
        public ShowAnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQueTitle = itemView.findViewById(R.id.tv_que_title);
            tvOp1 = itemView.findViewById(R.id.tv_op_1);
            tvOp2 = itemView.findViewById(R.id.tv_op_2);
            tvOp3 = itemView.findViewById(R.id.tv_op_3);
            tvOp4 = itemView.findViewById(R.id.tv_op_4);
            imgTick1 = itemView.findViewById(R.id.img_tick_1);
            imgTick2 = itemView.findViewById(R.id.img_tick_2);
            imgTick3 = itemView.findViewById(R.id.img_tick_3);
            imgTick4 = itemView.findViewById(R.id.img_tick_4);
        }
    }

    public class QuestionAttemptViewHolder extends RecyclerView.ViewHolder implements RadioGroup.OnCheckedChangeListener {

        public TextView tvQueTitle;
        public RadioGroup radioGroupOptions;
        public RadioButton radioOption1, radioOption2, radioOption3, radioOption4;
        public OnRadioSelectedListener onRadioSelectedListener;
        public QuestionAttemptViewHolder(@NonNull View itemView, OnRadioSelectedListener onRadioSelectedListener) {
            super(itemView);
            tvQueTitle = itemView.findViewById(R.id.rv_tv_ques_title);
            radioGroupOptions = itemView.findViewById(R.id.rv_radio_group_question);
            radioOption1 = itemView.findViewById(R.id.radio_option_1);
            radioOption2 = itemView.findViewById(R.id.radio_option_2);
            radioOption3 = itemView.findViewById(R.id.radio_option_3);
            radioOption4 = itemView.findViewById(R.id.radio_option_4);

            radioGroupOptions.setOnCheckedChangeListener(this);
            this.onRadioSelectedListener = onRadioSelectedListener;
        }

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            onRadioSelectedListener.onRadioSelected(checkedId, getAdapterPosition());
        }
    }

    public interface OnRadioSelectedListener{
        void onRadioSelected(int checkedId, int position);
    }

}
