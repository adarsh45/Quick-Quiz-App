package com.example.quizitup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizitup.adapters.QuestionAdapter;
import com.example.quizitup.pojos.Class;
import com.example.quizitup.utils.QuestionDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CreateQuizActivity extends AppCompatActivity implements QuestionAdapter.OnQuestionClickListener {

    private static final String TAG = "CreateQuizActivity";

    private EditText etQuizTitle, etQuizDuration;
    public static TextView tvQueCount, tvNoQuestions;
    private Spinner spinnerStatus;
    private Button btnSaveQuiz;
    public static RecyclerView rvQuestionsList;
    private FloatingActionButton fabAddQuestion;

    private Class classData;
    private Class.Quiz quizData;
    public static QuestionAdapter questionAdapter;
    private HashMap<String, Class.Quiz> quizzesList;
    public static HashMap<String, Class.Question> questionsList = new HashMap<>();
    private String[] quizStatusOptions = {"Create Mode", "Published", "Closed"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        initViews();
        getClassData();
        setupRV();

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

            if (quizzesList == null || quizzesList.isEmpty()){
                Class.Quiz quizData = new Class.Quiz(String.valueOf(0), quizTitle, quizStatus, quizDuration, questionsList);
                quizzesList.put("0_id", quizData);
            } else {
                Class.Quiz quizData = new Class.Quiz(quizzesList.get(String.valueOf(quizzesList.size() - 1)).getQuizId(), quizTitle, quizStatus, quizDuration, questionsList);
                quizzesList.put(quizData.getQuizId(), quizData);
            }

            classData.setQuizzesMap(quizzesList);

            Log.d(TAG, "onCreate: INVITE CODE: " + classData.getInviteCode());

            FirebaseDatabase.getInstance().getReference("Classes").child(classData.getInviteCode()).setValue(classData)
                    .addOnCompleteListener(task->{
                       if (task.isSuccessful()){
                           Toast.makeText(this, "Quiz added successfully!", Toast.LENGTH_SHORT).show();
                       } else {
                           Toast.makeText(this, "Something went wrong! Please retry!", Toast.LENGTH_SHORT).show();
                       }
                    });

        });

        fabAddQuestion.setOnClickListener(v-> {
            QuestionDialog questionDialog = new QuestionDialog(this, questionsList.size());
            questionDialog.show(getSupportFragmentManager(), "Add New Question");
        });
    }

    private void getClassData() {
        classData = getIntent().getParcelableExtra("classData");
        if (classData != null){
            if (classData.getQuizzesMap() == null){
                quizzesList = new HashMap<>();
            } else {
                quizzesList = classData.getQuizzesMap();
            }
        } else {
            quizzesList = new HashMap<>();
        }
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
        rvQuestionsList = findViewById(R.id.rv_questions_list);
        fabAddQuestion = findViewById(R.id.fab_add_question);
    }

    @Override
    public void onQuestionClick(int position) {
        QuestionDialog questionDialog = new QuestionDialog(this, position, questionsList.get(position + "_id"));
        questionDialog.show(getSupportFragmentManager(),"Edit Question");
    }
}