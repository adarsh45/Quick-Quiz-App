package com.adarsh45.quizitup.utils;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.adarsh45.quizitup.CreateQuizActivity;
import com.adarsh45.quizitup.R;
import com.adarsh45.quizitup.pojos.Class;

import java.util.ArrayList;

public class QuestionDialog extends DialogFragment {

    private static final String TAG = "QuestionDialog";

    private Activity activity;

    private Class.Question question;
    private int quePosition;
    private ArrayList<String> options = new ArrayList<>();
    private ArrayAdapter<String> spinnerOptionsAdapter;

    View view;
    Button btnSaveOptions, btnAddQuestion, btnCancelQuestion;
    EditText etQueTitle;
    EditText etOption1, etOption2, etOption3, etOption4;
    EditText etExplanation;
    CheckBox checkExplanation;
    Spinner spinnerOptions;

    public QuestionDialog(Activity activity, int quePosition){
        this.activity = activity;
        this.quePosition = quePosition;
        spinnerOptionsAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, options);
    }

    public QuestionDialog(Activity activity, int quePosition, Class.Question question){
        this.activity = activity;
        this.quePosition = quePosition;
        this.question = question;
        spinnerOptionsAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, options);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_question, container,false);

        initViews();
        setupSpinner();
        setTexts();

        checkExplanation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                etExplanation.setVisibility(View.VISIBLE);
            } else {
                etExplanation.setVisibility(View.GONE);
            }
        });

        btnSaveOptions.setOnClickListener(v-> {
//            any one of the four edit texts should be non empty
            if(!(!TextUtils.isEmpty(etOption1.getText()) ||
                    !TextUtils.isEmpty(etOption2.getText()) ||
                    !TextUtils.isEmpty(etOption3.getText()) ||
                    !TextUtils.isEmpty(etOption4.getText()))) {
                Toast.makeText(activity, "At least one option should be non-empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] rawOptions = {etOption1.getText().toString(), etOption2.getText().toString(), etOption3.getText().toString(), etOption4.getText().toString()};
            options.clear();
            for (int i = 0; i<4; i++){
                if(!TextUtils.isEmpty(rawOptions[i])){
                    options.add(rawOptions[i]);
                }
            }
            spinnerOptionsAdapter.notifyDataSetChanged();

        });

        btnAddQuestion.setOnClickListener(v-> {
//            TODO: code for adding question to questions list
            if (TextUtils.isEmpty(etQueTitle.getText())){
                etQueTitle.setError("Que Title cannot be empty!");
                return;
            }
            String queTitle = etQueTitle.getText().toString();
            String option1 = etOption1.getText().toString();
            String option2 = etOption2.getText().toString();
            String option3 = etOption3.getText().toString();
            String option4 = etOption4.getText().toString();
            String correctOption = options.get(spinnerOptions.getSelectedItemPosition());
            String explanation = TextUtils.isEmpty(etExplanation.getText().toString()) ? "No explanation" : etExplanation.getText().toString();

            Log.d(TAG, "onCreateView: KEY: " + quePosition);
            question = new Class.Question(quePosition + "_id", queTitle,option1,option2,option3, option4,correctOption, explanation);
            CreateQuizActivity.questionsList.put(quePosition + "_id", question);
            CreateQuizActivity.tvQueCount.setText(String.valueOf(CreateQuizActivity.questionsList.size()));
            CreateQuizActivity.questionAdapter.notifyDataSetChanged();
            if (CreateQuizActivity.questionsList.size() > 0){
                CreateQuizActivity.rvQuestionsList.setVisibility(View.VISIBLE);
                CreateQuizActivity.tvNoQuestions.setVisibility(View.GONE);
            }
            Log.d(TAG, "onCreateView: LIST SIZE: " + CreateQuizActivity.questionsList.size());
            dismiss();
        });

        btnCancelQuestion.setOnClickListener(v-> dismiss());

        setCancelable(false);
        return view;
    }

    private void setupSpinner() {
        spinnerOptionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOptions.setAdapter(spinnerOptionsAdapter);
    }

    private void initViews() {
        etQueTitle = view.findViewById(R.id.et_question_title);
        etOption1 = view.findViewById(R.id.et_option_1);
        etOption2 = view.findViewById(R.id.et_option_2);
        etOption3 = view.findViewById(R.id.et_option_3);
        etOption4 = view.findViewById(R.id.et_option_4);
        spinnerOptions = view.findViewById(R.id.spinner_all_options);
        checkExplanation = view.findViewById(R.id.check_explanation);
        etExplanation = view.findViewById(R.id.et_explanation);

        btnSaveOptions = view.findViewById(R.id.btn_save_options);
        btnAddQuestion = view.findViewById(R.id.btn_add_question);
        btnCancelQuestion = view.findViewById(R.id.btn_cancel_question);
    }

    private void setTexts() {
        if(question != null){
            etQueTitle.setText(question.getQueTitle());
            etOption1.setText(question.getOption1());
            etOption2.setText(question.getOption2());
            etOption3.setText(question.getOption3());
            etOption4.setText(question.getOption4());
            int selectedOption = 0;
            options.clear();
            if (!TextUtils.isEmpty(question.getOption1())){
                options.add(question.getOption1());
            }
            if (!TextUtils.isEmpty(question.getOption2())){
                options.add(question.getOption2());
            }
            if (!TextUtils.isEmpty(question.getOption3())){
                options.add(question.getOption3());
            }
            if (!TextUtils.isEmpty(question.getOption4())){
                options.add(question.getOption4());
            }
            spinnerOptionsAdapter.notifyDataSetChanged();
            if (question.getCorrectOption().equals(question.getOption1())){
                selectedOption = 0;
            }
            if (question.getCorrectOption().equals(question.getOption2())){
                selectedOption = 1;
            }
            if (question.getCorrectOption().equals(question.getOption3())){
                selectedOption = 2;
            }
            if (question.getCorrectOption().equals(question.getOption4())){
                selectedOption = 3;
            }
            spinnerOptions.setSelection(selectedOption);
            if (question.getExplanation() != null && !TextUtils.isEmpty(question.getExplanation())){
                etExplanation.setText(question.getExplanation());
            }
        }
    }
}
