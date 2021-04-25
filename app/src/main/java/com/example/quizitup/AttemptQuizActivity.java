package com.example.quizitup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizitup.adapters.QuestionAttemptAdapter;
import com.example.quizitup.pojos.Class;
import com.example.quizitup.pojos.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AttemptQuizActivity extends AppCompatActivity implements QuestionAttemptAdapter.OnRadioSelectedListener {

    private static final String TAG = "AttemptQuizActivity";

    CountDownTimer timer;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference classRef = FirebaseDatabase.getInstance().getReference("Classes");
    private DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("Students").child(mAuth.getUid());

    private TextView tvQuizTitle, tvQueCount, tvDuration, tvTimeRemaining;
    private Button btnStartQuiz, btnEndQuiz;
    private RecyclerView rvAttemptQuizQuestionsList;

    private QuestionAttemptAdapter adapter;
    private HashMap<String, Student.AnsweredQuestion> answeredQuestionHashMap = new HashMap<>();
    private HashMap<String, Class.Question> questionHashMap = new HashMap<>();
    private String classInviteCode, quizId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attempt_quiz);

//        disabling the screenshot
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        initViews();
        getData();
        fetchData();

        btnStartQuiz.setOnClickListener(v-> startQuiz());
        btnEndQuiz.setOnClickListener(v-> endQuiz());
    }

    private void endQuiz() {

        AlertDialog alertDialog = new AlertDialog.Builder(AttemptQuizActivity.this).create();
        alertDialog.setTitle("Confirm Exam Submit");
        alertDialog.setMessage("Are you sure you want to submit the exam ? You won't be able to give this exam again!");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "SUBMIT", (dialog, which) -> {
//            user wants to submit the exam

            Toast.makeText(this, "Exam Ended", Toast.LENGTH_SHORT).show();
            timer.cancel();

            finishExamAndSaveToDB();

            btnStartQuiz.setVisibility(View.GONE);
            btnEndQuiz.setEnabled(false);
            alertDialog.dismiss();
        });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "CANCEL", (dialog, which) -> {
//            user want to continue exam, do nothing
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    private void finishExamAndSaveToDB() {
        int score = 0;
        for (Map.Entry<String, Student.AnsweredQuestion> answeredQuestionEntry : answeredQuestionHashMap.entrySet()){
            Student.AnsweredQuestion answeredQuestion = answeredQuestionEntry.getValue();
            if (answeredQuestion.getSelectedOption().equals(answeredQuestion.getCorrectOption())){
                score++;
            }
        }

        Student.AttemptedQuiz attemptedQuiz = new Student.AttemptedQuiz(quizId, String.valueOf(score),answeredQuestionHashMap);
        studentRef.child("attemptedQuizzesMap")
                .child(classInviteCode).child(quizId)
                .setValue(attemptedQuiz)
                .addOnCompleteListener(task-> {
                    if (task.isSuccessful()){
                        Toast.makeText(this, "Exam submitted successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Something went wrong! You can try again!", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                });
    }

    private void startQuiz() {
        Toast.makeText(this, "Exam Started", Toast.LENGTH_SHORT).show();
        rvAttemptQuizQuestionsList.setVisibility(View.VISIBLE);
        timer.start();

        btnStartQuiz.setVisibility(View.GONE);
        btnEndQuiz.setVisibility(View.VISIBLE);
    }

    private void fetchData() {
        classRef.child(classInviteCode).child("quizzesMap").child(quizId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Class.Quiz quizData = snapshot.getValue(Class.Quiz.class);
                            tvQuizTitle.setText(quizData.getQuizTitle());
                            tvTimeRemaining.setText(quizData.getDuration() + ":00");
                            tvDuration.setText(quizData.getDuration());
                            btnStartQuiz.setEnabled(true);


//                            check whether student has already given exam ot not
                            studentRef.child("attemptedQuizzesMap").child(classInviteCode).child(quizId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (quizData.getQuestionMap() != null && quizData.getQuestionMap().size() > 0){
                                                questionHashMap = (HashMap<String, Class.Question>) quizData.getQuestionMap();
                                                tvQueCount.setText(String.valueOf(quizData.getQuestionMap().size()));

                                                if (snapshot.exists()){
//                                                    exam is submitted already
                                                    btnStartQuiz.setEnabled(false);
                                                    Log.d(TAG, "onDataChange: SNAPSHOT DATA: " +snapshot);
                                                    Student.AttemptedQuiz attemptedQuiz = snapshot.getValue(Student.AttemptedQuiz.class);
                                                    Log.d(TAG, "onDataChange: SCORE: " + attemptedQuiz.getScore());
                                                    Log.d(TAG, "onDataChange: QUIZ ID: " + attemptedQuiz.getQuizId());
                                                    Log.d(TAG, "onDataChange: QUE MAP: " + attemptedQuiz.getQuestionsMap());
                                                    if (attemptedQuiz.getQuestionsMap() == null || attemptedQuiz.getQuestionsMap().size() == 0){
                                                        Toast.makeText(AttemptQuizActivity.this, "Could not fetch your answers! Please retry!", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                    tvTimeRemaining.setText("Score: " + attemptedQuiz.getScore());
                                                    answeredQuestionHashMap.clear();
                                                    answeredQuestionHashMap = attemptedQuiz.getQuestionsMap();
                                                    rvAttemptQuizQuestionsList.setVisibility(View.VISIBLE);
                                                    Log.d(TAG, "onDataChange: EXAM SUBMITTED ALREADY!");
                                                    setupRV("submitted");
                                                } else {
//                                                    exam is not submitted already
//                                                    check if now the quiz is published or closed
                                                    if (quizData.getStatus().equals("Closed")){
//                                                      quiz is already closed from teacher
                                                        btnStartQuiz.setEnabled(false);
                                                        rvAttemptQuizQuestionsList.setVisibility(View.VISIBLE);
                                                        Toast.makeText(AttemptQuizActivity.this, "Exam is Closed by Teacher! You can still see answers!", Toast.LENGTH_SHORT).show();
                                                        Log.d(TAG, "onDataChange: EXAM CLOSED BY TEACHER ALREADY!");
                                                        setupRV("closed");
                                                    } else if (quizData.getStatus().equals("Published")){
                                                        btnStartQuiz.setEnabled(true);
                                                        initCountDown();
                                                        Log.d(TAG, "onDataChange: YOU CAN START EXAM");
                                                        setupRV("fresh");
                                                    }
                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.d(TAG, "onCancelled: " + error.getMessage());
                                            Toast.makeText(AttemptQuizActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else {
                            Toast.makeText(AttemptQuizActivity.this, "Quiz Not Found! Please retry!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: " + error.getMessage());
                        Toast.makeText(AttemptQuizActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupRV(String studentStatus) {
        if (studentStatus.equals("fresh")){
            adapter = new QuestionAttemptAdapter(questionHashMap, studentStatus, this);
        } else if(studentStatus.equals("submitted")){
            Log.d(TAG, "setupRV: SUBMITTED" + answeredQuestionHashMap.size());
            adapter = new QuestionAttemptAdapter(questionHashMap, answeredQuestionHashMap,studentStatus, this);
        } else {
            adapter = new QuestionAttemptAdapter(questionHashMap, studentStatus, this);
        }

        rvAttemptQuizQuestionsList.setAdapter(adapter);
        rvAttemptQuizQuestionsList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initCountDown() {
        timer = new CountDownTimer(Integer.parseInt(tvDuration.getText().toString())*60*1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                String timeRemaining = minutes + ":" + seconds;
                tvTimeRemaining.setText(timeRemaining);
            }

            @Override
            public void onFinish() {
                tvTimeRemaining.setText("00:00");
                finishExamAndSaveToDB();
            }
        };
    }

    private void getData() {
        classInviteCode = getIntent().getStringExtra("classInviteCode");
        quizId = getIntent().getStringExtra("quizData");
    }

    private void initViews() {
        tvQuizTitle = findViewById(R.id.tv_quiz_title_attempt_quiz);
        tvQueCount = findViewById(R.id.tv_que_count_attempt_quiz);
        tvDuration = findViewById(R.id.tv_attempt_quiz_duration);
        tvTimeRemaining = findViewById(R.id.tv_time_remaining);
        btnStartQuiz = findViewById(R.id.btn_start_quiz);
        btnEndQuiz = findViewById(R.id.btn_end_quiz);
        rvAttemptQuizQuestionsList = findViewById(R.id.rv_attempt_quiz_que_list);

        btnStartQuiz.setEnabled(false);
    }

    @Override
    public void onRadioSelected(int checkedId, int position) {
        if (checkedId == R.id.radio_option_1){
            Class.Question question = questionHashMap.get(position + "_id");
            answeredQuestionHashMap.put(position + "_id", new Student.AnsweredQuestion(position + "_id", question.getOption1(), question.getCorrectOption()));
        } else if (checkedId == R.id.radio_option_2){
            Class.Question question = questionHashMap.get(position + "_id");
            answeredQuestionHashMap.put(position + "_id", new Student.AnsweredQuestion(position + "_id", question.getOption2(), question.getCorrectOption()));
        } else if (checkedId == R.id.radio_option_3){
            Class.Question question = questionHashMap.get(position + "_id");
            answeredQuestionHashMap.put(position + "_id", new Student.AnsweredQuestion(position + "_id", question.getOption3(), question.getCorrectOption()));
        } else if (checkedId == R.id.radio_option_4){
            Class.Question question = questionHashMap.get(position + "_id");
            answeredQuestionHashMap.put(position + "_id", new Student.AnsweredQuestion(position + "_id", question.getOption4(), question.getCorrectOption()));
        }
    }
}