package com.adarsh45.quizitup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adarsh45.quizitup.R;
import com.adarsh45.quizitup.pojos.Class;
import com.adarsh45.quizitup.utils.Utils;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Class> classes;
    private byte origin;
    private OnAddClassClickListener onAddClassClickListener;
    private OnClassCardClickListener onClassCardClickListener;

    public ClassAdapter(List<Class> classes, byte origin, OnAddClassClickListener onAddClassClickListener, OnClassCardClickListener onClassCardClickListener){
        this.classes = classes;
        this.origin = origin;
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
            Class classObj = classes.get(position);

            if (origin == Utils.TEACHER_DATA){
                ((ClassCardViewHolder) holder).layoutStudent.setVisibility(View.VISIBLE);
                ((ClassCardViewHolder) holder).layoutTeacher.setVisibility(View.GONE);

                if (classObj.getStudentsEnrolledMap() == null || classObj.getStudentsEnrolledMap().size() == 0){
                    ((ClassCardViewHolder) holder).tvStudentsCount.setText("0");
                } else {
                    ((ClassCardViewHolder) holder).tvStudentsCount.setText(String.valueOf(classObj.getStudentsEnrolledMap().size()));
                }

            }else if (origin == Utils.STUDENT_DATA){
                ((ClassCardViewHolder) holder).layoutStudent.setVisibility(View.GONE);
                ((ClassCardViewHolder) holder).layoutTeacher.setVisibility(View.VISIBLE);
                ((ClassCardViewHolder) holder).tvTeacherName.setText(classObj.getTeacherName());

                ((ClassCardViewHolder) holder).imgDeleteClass.setVisibility(View.GONE);
                ((ClassCardViewHolder) holder).imgEditClass.setVisibility(View.GONE);
            }

            ((ClassCardViewHolder) holder).tvClassTitle.setText(classObj.getClassTitle());
            if (classObj.getQuizzesMap() == null || classObj.getQuizzesMap().size() == 0){
                ((ClassCardViewHolder) holder).tvQuizzesCount.setText("0");
            } else {
                ((ClassCardViewHolder) holder).tvQuizzesCount.setText(String.valueOf(classObj.getQuizzesMap().size()));
            }

        }
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    public static class ClassCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public OnClassCardClickListener onClassCardClickListener;
        public TextView tvClassTitle, tvStudentsCount, tvQuizzesCount, tvTeacherName;
        public ImageView imgEditClass, imgDeleteClass;
        public LinearLayout layoutStudent, layoutTeacher;
        public ClassCardViewHolder(@NonNull View itemView, OnClassCardClickListener onClassCardClickListener) {
            super(itemView);
            this.onClassCardClickListener = onClassCardClickListener;
            tvClassTitle = itemView.findViewById(R.id.tv_class_title);
            tvStudentsCount = itemView.findViewById(R.id.tv_students_enrolled);
            tvQuizzesCount = itemView.findViewById(R.id.tv_total_quizzes);
            tvTeacherName = itemView.findViewById(R.id.tv_teacher_name);

            imgEditClass = itemView.findViewById(R.id.img_edit_class);
            imgDeleteClass = itemView.findViewById(R.id.img_delete_class);

            layoutStudent = itemView.findViewById(R.id.layout_student);
            layoutTeacher = itemView.findViewById(R.id.layout_teacher);

            itemView.setOnClickListener(this);
            imgEditClass.setOnClickListener(this);
            imgDeleteClass.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == itemView.getId()){
                onClassCardClickListener.onClassCardClick(getAdapterPosition());
            } else if (v.getId() == imgEditClass.getId()){
                onClassCardClickListener.onEditClassClick(getAdapterPosition());
            }
            else if (v.getId() == imgDeleteClass.getId()){
                onClassCardClickListener.onDeleteClassClick(getAdapterPosition());
            }
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
        void onDeleteClassClick(int position);
        void onEditClassClick(int position);
    }
}
