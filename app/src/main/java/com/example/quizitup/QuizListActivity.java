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

import java.util.HashMap;
import java.util.Map;

public class QuizListActivity extends AppCompatActivity implements QuizListAdapter.OnAddQuizClickListener, QuizListAdapter.OnQuizCardClickListener {

    private TextView tvClassHeading;
    private RecyclerView rvQuizList;

    private String classInviteCode;
    private Class classData;
    private Map<String, Class.Quiz> quizzesList = new HashMap<>();
    QuizListAdapter adapter;

    private static final String TAG = "QuizListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);

        initViews();
        getClassData();
        getQuizzes();
    }

    private void getQuizzes() {
        FirebaseDatabase.getInstance().getReference("Classes")
                .child(classInviteCode).child("quizzesMap")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Log.d(TAG, "onDataChange: " + snapshot.toString());
                            for (DataSnapshot snap: snapshot.getChildren()){
                                Class.Quiz quizData = snap.getValue(Class.Quiz.class);
                                quizzesList.put(quizData.getQuizId(), quizData);
                            }
                        }
                        setupRV();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: " + error.getMessage());
                        Toast.makeText(QuizListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getClassData() {
        classInviteCode = getIntent().getStringExtra("classInviteCode");
        tvClassHeading.setText(getIntent().getStringExtra("classTitle"));
    }

    private void setupRV() {
        quizzesList.put("dummy",new Class.Quiz());
        adapter = new QuizListAdapter(quizzesList,this::onAddQuizClick, this::onQuizClick);
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
        intent.putExtra("classInviteCode", classInviteCode);
        intent.putExtra("quizData", position + "_id");
        Log.d(TAG, "onQuizClick: "  + classInviteCode);
        startActivity(intent);
    }

    @Override
    public void onQuizClick(int position) {
        Intent intent = new Intent(QuizListActivity.this, CreateQuizActivity.class);
        intent.putExtra("classInviteCode", classInviteCode);
        intent.putExtra("quizData", position + "_id");
        Log.d(TAG, "onQuizClick: "  + classInviteCode);
        startActivity(intent);
    }
}