package com.example.quizitup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizitup.R;
import com.example.quizitup.pojos.Class;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Class> classes;
    private OnAddClassClickListener onAddClassClickListener;
    private OnClassCardClickListener onClassCardClickListener;

    public ClassAdapter(List<Class> classes, OnAddClassClickListener onAddClassClickListener, OnClassCardClickListener onClassCardClickListener){
        this.classes = classes;
        this.onAddClassClickListener = onAddClassClickListener;
        this.onClassCardClickListener = onClassCardClickListener;
    }

    @Override
    public int getItemViewType(int position) {
//        this will return 0 for last item & 1 for all the others
        if (position == classes.size() - 1 ){
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_class_card,parent,false);
            return new ClassCardViewHolder(view, onClassCardClickListener);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_add_class_card,parent,false);
        return new AddClassViewHolder(view, onAddClassClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() != 0){
//            this is any item other than last
            ((ClassCardViewHolder) holder).tvClassTitle.setText(classes.get(position).getClassTitle());
        }
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    public static class ClassCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public OnClassCardClickListener onClassCardClickListener;
        public TextView tvClassTitle, tvStudentsCount, tvQuizzesCount;
        public ClassCardViewHolder(@NonNull View itemView, OnClassCardClickListener onClassCardClickListener) {
            super(itemView);
            this.onClassCardClickListener = onClassCardClickListener;
            tvClassTitle = itemView.findViewById(R.id.tv_class_title);
            tvStudentsCount = itemView.findViewById(R.id.tv_students_enrolled);
            tvQuizzesCount = itemView.findViewById(R.id.tv_total_quizzes);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClassCardClickListener.onClassCardClick(getAdapterPosition());
        }
    }

    public static class AddClassViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public OnAddClassClickListener onAddClassClickListener;
        public AddClassViewHolder(@NonNull View itemView, OnAddClassClickListener onAddClassClickListener) {
            super(itemView);
            this.onAddClassClickListener = onAddClassClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onAddClassClickListener.onAddClassClick(getAdapterPosition());
        }
    }

    public interface OnAddClassClickListener {
        void onAddClassClick(int position);
    }

    public interface OnClassCardClickListener {
        void onClassCardClick(int position);
    }
}
