package com.example.quizitup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizitup.R;

import java.util.List;

public class QuizListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> quizList;
    private OnAddQuizClickListener onAddQuizClickListener;
    private OnQuizCardClickListener onQuizCardClickListener;

    public QuizListAdapter(List<String> quizList, OnAddQuizClickListener onAddQuizClickListener, OnQuizCardClickListener onQuizCardClickListener){
        this.quizList = quizList;
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
        }
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public static class QuizCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public OnQuizCardClickListener onQuizCardClickListener;
        public QuizCardViewHolder(@NonNull View itemView, OnQuizCardClickListener onQuizCardClickListener) {
            super(itemView);
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
