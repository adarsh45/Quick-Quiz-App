package com.example.quizitup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.quizitup.utils.QuestionDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreateQuizActivity extends AppCompatActivity {

    private TextView tvQueCount, tvNoQuestions;
    private RecyclerView rvQuestionsList;
    private FloatingActionButton fabAddQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        initViews();

        fabAddQuestion.setOnClickListener(v-> {
            QuestionDialog questionDialog = new QuestionDialog(this);
            questionDialog.show(getSupportFragmentManager(), "Add New Question");
        });
    }

    private void initViews() {
        tvQueCount = findViewById(R.id.tv_que_count);
        tvNoQuestions = findViewById(R.id.tv_no_questions);
        rvQuestionsList = findViewById(R.id.rv_questions_list);
        fabAddQuestion = findViewById(R.id.fab_add_question);
    }
}