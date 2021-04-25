package com.example.quizitup.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizitup.R;
import com.example.quizitup.pojos.Class;
import com.example.quizitup.pojos.Submission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmissionAdapter extends RecyclerView.Adapter<SubmissionAdapter.SubmissionViewHolder> {

    private ArrayList<Submission> submissions;

    public SubmissionAdapter(ArrayList<Submission> submissions){
        this.submissions = submissions;
    }

    @NonNull
    @Override
    public SubmissionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_submission,parent,false);
        return new SubmissionViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull SubmissionViewHolder holder, int position) {
        Submission submission = submissions.get(position);
        holder.tvStudentName.setText(submission.getStudentName());
        holder.tvMobile.setText(submission.getStudentMobile());
        holder.tvScore.setText(submission.getScore());
    }

    @Override
    public int getItemCount() {
        return submissions.size();
    }

    public static class SubmissionViewHolder extends RecyclerView.ViewHolder{

        public TextView tvStudentName, tvMobile, tvScore;
        public SubmissionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tv_sub_student_name);
            tvMobile = itemView.findViewById(R.id.tv_sub_student_mobile);
            tvScore = itemView.findViewById(R.id.tv_sub_student_score);
        }

    }
}
