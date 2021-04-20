package com.example.quizitup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizitup.adapters.QuizListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizListActivity extends AppCompatActivity implements QuizListAdapter.OnAddQuizClickListener, QuizListAdapter.OnQuizCardClickListener {

    private TextView tvClassHeading;
    private RecyclerView rvQuizList;

    String[] quizList = {"Quiz 1", "Test Quiz 2", "Sample quiz!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);

        initViews();
        setupRV();
    }

    private void setupRV() {
        QuizListAdapter adapter = new QuizListAdapter(Arrays.asList(quizList),this::onAddQuizClick, this::onQuizClick);
        rvQuizList.setAdapter(adapter);
        rvQuizList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initViews() {
        tvClassHeading = findViewById(R.id.tv_class_title);
        rvQuizList = findViewById(R.id.rv_quiz_list);
    }

    @Override
    public void onAddQuizClick(int position) {
        Intent intent = new Intent(QuizListActivity.this, CreateQuizActivity.class);
        startActivity(intent);
    }

    @Override
    public void onQuizClick(int position) {
        Intent intent = new Intent(QuizListActivity.this, CreateQuizActivity.class);
        startActivity(intent);
    }
}