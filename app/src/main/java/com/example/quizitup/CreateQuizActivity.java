package com.example.quizitup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizitup.adapters.QuestionAdapter;
import com.example.quizitup.utils.QuestionDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;

public class CreateQuizActivity extends AppCompatActivity implements QuestionAdapter.OnQuestionClickListener {

    private TextView tvQueCount, tvNoQuestions;
    private Spinner spinnerStatus;
    private RecyclerView rvQuestionsList;
    private FloatingActionButton fabAddQuestion;

    private String[] quizStatusOptions = {"Create Mode", "Published", "Closed"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        initViews();
        setupRV();

        ArrayAdapter<String> statusOptionsAdapter = new ArrayAdapter<>(CreateQuizActivity.this, android.R.layout.simple_spinner_item, quizStatusOptions);
        statusOptionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusOptionsAdapter);

        fabAddQuestion.setOnClickListener(v-> {
            QuestionDialog questionDialog = new QuestionDialog(this);
            questionDialog.show(getSupportFragmentManager(), "Add New Question");
        });
    }

    private void setupRV() {
        QuestionAdapter adapter = new QuestionAdapter(Arrays.asList(new String[]{"", ""}), this::onQuestionClick);
        rvQuestionsList.setAdapter(adapter);
        rvQuestionsList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initViews() {
        tvQueCount = findViewById(R.id.tv_que_count);
        tvNoQuestions = findViewById(R.id.tv_no_questions);
        spinnerStatus = findViewById(R.id.spinner_quiz_status);
        rvQuestionsList = findViewById(R.id.rv_questions_list);
        fabAddQuestion = findViewById(R.id.fab_add_question);
    }

    @Override
    public void onQuestionClick(int position) {
        Toast.makeText(this, "Position: "+ position, Toast.LENGTH_SHORT).show();
    }
}