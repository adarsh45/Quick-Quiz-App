package com.adarsh45.quizitup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adarsh45.quizitup.adapters.QuestionAdapter;
import com.adarsh45.quizitup.pojos.Class;
import com.adarsh45.quizitup.utils.QuestionDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CreateQuizActivity extends AppCompatActivity implements QuestionAdapter.OnQuestionClickListener {

    private static final String TAG = "CreateQuizActivity";

    private EditText etQuizTitle, etQuizDuration;
    public static TextView tvQueCount, tvNoQuestions;
    private Spinner spinnerStatus;
    private Button btnSaveQuiz, btnShowSubmissions;
    public static RecyclerView rvQuestionsList;
    private FloatingActionButton fabAddQuestion;

    private String classInviteCode, quizId;
    private Class.Quiz quizData;
    public static QuestionAdapter questionAdapter;
    public static Map<String, Class.Question> questionsList = new HashMap<>();
    private String[] quizStatusOptions = {"Create Mode", "Published", "Closed"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

//      back button in action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        getClassData();

        ArrayAdapter<String> statusOptionsAdapter = new ArrayAdapter<>(CreateQuizActivity.this, android.R.layout.simple_spinner_item, quizStatusOptions);
        statusOptionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusOptionsAdapter);

        btnSaveQuiz.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etQuizTitle.getText())){
                etQuizTitle.setError("Quiz Title cannot be empty!");
                return;
            }
            if (TextUtils.isEmpty(etQuizTitle.getText())){
                etQuizTitle.setError("Quiz Title cannot be empty!");
                return;
            }
            String quizTitle = etQuizTitle.getText().toString();
            String quizDuration = etQuizDuration.getText().toString();
            String quizStatus = quizStatusOptions[spinnerStatus.getSelectedItemPosition()];

            Class.Quiz quizData = new Class.Quiz( quizId, quizTitle, quizStatus, quizDuration, questionsList);

            FirebaseDatabase.getInstance().getReference("Classes")
                    .child(classInviteCode).child("quizzesMap").child(quizId).setValue(quizData)
                    .addOnCompleteListener(task->{
                       if (task.isSuccessful()){
                           Toast.makeText(this, "Quiz added successfully!", Toast.LENGTH_SHORT).show();
                       } else {
                           Toast.makeText(this, "Something went wrong! Please retry!", Toast.LENGTH_SHORT).show();
                       }
                    });

        });

        btnShowSubmissions.setOnClickListener(v-> {
//            redirect to submissions activity
            Intent intent = new Intent(CreateQuizActivity.this, SubmissionsActivity.class);
            intent.putExtra("classInviteCode", classInviteCode);
            intent.putExtra("quizId", quizId);
            startActivity(intent);
        });

        fabAddQuestion.setOnClickListener(v-> {
            QuestionDialog questionDialog = new QuestionDialog(this, questionsList.size());
            questionDialog.show(getSupportFragmentManager(), "Add New Question");
        });
    }

    private void getClassData() {
        classInviteCode = getIntent().getStringExtra("classInviteCode");
        quizId = getIntent().getStringExtra("quizData");

        FirebaseDatabase.getInstance().getReference("Classes")
                .child(classInviteCode).child("quizzesMap").child(quizId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            quizData = snapshot.getValue(Class.Quiz.class);
                            etQuizTitle.setText(quizData.getQuizTitle());
                            etQuizDuration.setText(quizData.getDuration());
                            int spinnerPosition = 0;
                            for(int i=0; i<quizStatusOptions.length; i++){
                                if (quizStatusOptions[i].equals(quizData.getStatus())){
                                    spinnerPosition = i;
                                }
                            }
                            spinnerStatus.setSelection(spinnerPosition);

                            if (quizData.getQuestionMap() == null || quizData.getQuestionMap().size() == 0){
                                tvQueCount.setText("0");
                            } else {
                                questionsList = quizData.getQuestionMap();
                                tvQueCount.setText(String.valueOf(questionsList.size()));
                                tvNoQuestions.setVisibility(View.GONE);
                                rvQuestionsList.setVisibility(View.VISIBLE);
                            }
                        }
                        setupRV();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: " + error.getMessage());
                        Toast.makeText(CreateQuizActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupRV() {
        if (questionsList.size() == 0){
            tvNoQuestions.setVisibility(View.VISIBLE);
            rvQuestionsList.setVisibility(View.GONE);
        }
        questionAdapter = new QuestionAdapter(questionsList, this::onQuestionClick);
        rvQuestionsList.setAdapter(questionAdapter);
        rvQuestionsList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initViews() {
        etQuizTitle = findViewById(R.id.et_quiz_title);
        etQuizDuration = findViewById(R.id.et_quiz_duration);
        tvQueCount = findViewById(R.id.tv_que_count);
        tvNoQuestions = findViewById(R.id.tv_no_questions);
        spinnerStatus = findViewById(R.id.spinner_quiz_status);
        btnSaveQuiz = findViewById(R.id.btn_save_quiz);
        btnShowSubmissions = findViewById(R.id.btn_show_submissions);
        rvQuestionsList = findViewById(R.id.rv_questions_list);
        fabAddQuestion = findViewById(R.id.fab_add_question);
    }

    @Override
    public void onQuestionClick(int position) {
        QuestionDialog questionDialog = new QuestionDialog(this, position, questionsList.get(position + "_id"));
        questionDialog.show(getSupportFragmentManager(),"Edit Question");
    }

//    back button in action bar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}