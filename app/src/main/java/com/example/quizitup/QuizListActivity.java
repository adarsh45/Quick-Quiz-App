package com.example.quizitup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizitup.adapters.QuizListAdapter;
import com.example.quizitup.pojos.Class;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class QuizListActivity extends AppCompatActivity implements QuizListAdapter.OnAddQuizClickListener, QuizListAdapter.OnQuizCardClickListener {

    private TextView tvClassHeading;
    private RecyclerView rvQuizList;

    private Class classData;
    private HashMap<String, Class.Quiz> quizzesList;

    private static final String TAG = "QuizListActivity";
    String[] quizList = {"Quiz 1", "Test Quiz 2", "Sample quiz!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);

        initViews();
        getClassData();
        getQuizzes();
        setupRV();
    }

    private void getQuizzes() {
        FirebaseDatabase.getInstance().getReference("Classes").child(classData.getInviteCode())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot snap: snapshot.getChildren()){
                                classData = snap.getValue(Class.class);

                            }
                            if (classData != null && classData.getQuizzesMap() != null){
                                quizzesList = classData.getQuizzesMap();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: " + error.getMessage());
                        Toast.makeText(QuizListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getClassData() {
        classData = getIntent().getParcelableExtra("classData");
        if (classData != null){
            tvClassHeading.setText(classData.getClassTitle());
            if (classData.getQuizzesMap() != null){
                quizzesList = classData.getQuizzesMap();
            } else {
                quizzesList = new HashMap<>();
            }
        }
    }

    private void setupRV() {
        QuizListAdapter adapter = new QuizListAdapter(quizzesList,this::onAddQuizClick, this::onQuizClick);
        rvQuizList.setAdapter(adapter);
        rvQuizList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initViews() {
        tvClassHeading = findViewById(R.id.tv_class_heading);
        rvQuizList = findViewById(R.id.rv_quiz_list);
    }

    @Override
    public void onAddQuizClick(int position) {
        Intent intent = new Intent(QuizListActivity.this, CreateQuizActivity.class);
        intent.putExtra("classData", classData);
        Log.d(TAG, "onQuizClick: "  + classData);
        startActivity(intent);
    }

    @Override
    public void onQuizClick(int position) {
        Intent intent = new Intent(QuizListActivity.this, CreateQuizActivity.class);
        intent.putExtra("classData", classData);
        Log.d(TAG, "onQuizClick: "  + classData);
        startActivity(intent);
    }
}