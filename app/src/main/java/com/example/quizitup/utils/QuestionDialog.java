package com.example.quizitup.utils;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.quizitup.R;

public class QuestionDialog extends DialogFragment {

    private Activity activity;

    public QuestionDialog(Activity activity){
        this.activity = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_question, container,false);

        Button btnSaveOptions, btnAddQuestion, btnCancelQuestion;
        EditText etQueTitle;
        EditText etOption1, etOption2, etOption3, etOption4;
        Spinner spinnerOptions;

        etQueTitle = view.findViewById(R.id.et_question_title);
        etOption1 = view.findViewById(R.id.et_option_1);
        etOption2 = view.findViewById(R.id.et_option_2);
        etOption3 = view.findViewById(R.id.et_option_3);
        etOption4 = view.findViewById(R.id.et_option_4);

        spinnerOptions = view.findViewById(R.id.spinner_all_options);

        btnSaveOptions = view.findViewById(R.id.btn_save_options);
        btnAddQuestion = view.findViewById(R.id.btn_add_question);
        btnCancelQuestion = view.findViewById(R.id.btn_cancel_question);

        btnSaveOptions.setOnClickListener(v-> {
//            any one of the four edit texts should be non empty
            if(TextUtils.isEmpty(etOption1.getText()) ||
                    TextUtils.isEmpty(etOption2.getText()) ||
                    TextUtils.isEmpty(etOption3.getText()) ||
                    TextUtils.isEmpty(etOption4.getText())) {
                Toast.makeText(activity, "At least one option should be non-empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] options = {etOption1.getText().toString(), etOption2.getText().toString(), etOption3.getText().toString(), etOption4.getText().toString()};
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, options);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerOptions.setAdapter(arrayAdapter);

        });

        btnAddQuestion.setOnClickListener(v-> {
//            TODO: code for adding question to questions list
            dismiss();
        });

        btnCancelQuestion.setOnClickListener(v-> dismiss());

        setCancelable(false);
        return view;
    }
}
