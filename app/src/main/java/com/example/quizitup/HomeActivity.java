package com.example.quizitup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

//    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FloatingActionButton fabCreateQuiz;

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (mAuth != null){
//            mAuth.signOut();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        fabCreateQuiz.setOnClickListener(v->{
            Intent intent = new Intent(HomeActivity.this, CreateQuizActivity.class);
            startActivity(intent);
        });

    }

    private void initViews() {
        fabCreateQuiz = findViewById(R.id.fab_create_quiz);
    }
}