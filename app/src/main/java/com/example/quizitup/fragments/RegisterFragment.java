package com.example.quizitup.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.quizitup.HomeActivity;
import com.example.quizitup.R;
import com.example.quizitup.pojos.Student;
import com.example.quizitup.pojos.Teacher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterFragment extends Fragment {

    private RadioGroup radioGroupRegister;
    private EditText etRegisterName, etRegisterEmail, etRegisterRoll, etRegisterSubject, etRegisterPassword;
    private Button btnRegister;

    private FirebaseAuth mAuth;
    
    private static final String TAG = "RegisterFragment";

    public RegisterFragment() { }

    public static RegisterFragment newInstance(String param1, String param2) {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        radioGroupRegister = view.findViewById(R.id.radio_group_register);
        etRegisterName = view.findViewById(R.id.et_register_name);
        etRegisterEmail = view.findViewById(R.id.et_register_email);
        etRegisterRoll = view.findViewById(R.id.et_register_roll);
        etRegisterSubject = view.findViewById(R.id.et_register_subject);
        etRegisterPassword = view.findViewById(R.id.et_register_password);
        btnRegister = view.findViewById(R.id.btn_register);

        radioGroupRegister.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_btn_register_teacher){
                etRegisterSubject.setVisibility(View.VISIBLE);
                etRegisterRoll.setVisibility(View.GONE);
            } else {
                etRegisterSubject.setVisibility(View.GONE);
                etRegisterRoll.setVisibility(View.VISIBLE);
            }
        });

        btnRegister.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etRegisterName.getText())){
                etRegisterName.setError("Name cannot be empty!");
                return;
            }
            if (TextUtils.isEmpty(etRegisterEmail.getText())){
                etRegisterEmail.setError("Email cannot be empty!");
                return;
            }
            if (TextUtils.isEmpty(etRegisterPassword.getText()) || etRegisterPassword.getText().length() < 6){
                etRegisterPassword.setError("Password should contain at least 6 characters!");
                return;
            }

            String name = etRegisterName.getText().toString();
            String email = etRegisterEmail.getText().toString();
            String password = etRegisterPassword.getText().toString();
            
            int selectedId = radioGroupRegister.getCheckedRadioButtonId();
            if (selectedId == R.id.radio_btn_register_teacher){
                if (TextUtils.isEmpty(etRegisterSubject.getText())){
                    etRegisterSubject.setError("Subject cannot be empty!");
                    return;
                }
                String subject = etRegisterSubject.getText().toString();
                
                registerTeacher(name, email, subject, password);
            } else {
                if (TextUtils.isEmpty(etRegisterRoll.getText())){
                    etRegisterRoll.setError("Roll no cannot be empty!");
                    return;
                }
                String roll = etRegisterRoll.getText().toString();
                
                registerStudent(name, email, roll, password);
            }
        });
        return view;
    }

    private void registerStudent(String name, String email, String roll, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Log.d(TAG, "createUserWithEmail:success");
                        Toast.makeText(getActivity(), "Authentication success.",
                                Toast.LENGTH_SHORT).show();
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        registerStudentToDB(currentUser.getUid(), name, email, roll);
                    } else {
                        Log.d(TAG, "createUserWithEmail:failure" + task.getException().getLocalizedMessage());
                        Toast.makeText(getActivity(), task.getException().getLocalizedMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void registerStudentToDB(String uid, String name, String email, String roll) {
        DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("Students");
        Student student = new Student(uid, name, email, roll);
        studentRef.child(uid).setValue(student).addOnCompleteListener(task-> {
            if (task.isSuccessful()){
                Log.d(TAG, "registerStudentToDB: success");
                Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                startActivity(homeIntent);
                getActivity().finish();
            } else {
                Log.d(TAG, "registerStudentToDB: failure", task.getException());
                Toast.makeText(getActivity(), "Error while creating user!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerTeacher(String name, String email, String subject, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful()){
                       Log.d(TAG, "createUserWithEmail:success");
                       Toast.makeText(getActivity(), "Authentication success.",
                               Toast.LENGTH_SHORT).show();
                       FirebaseUser currentUser = mAuth.getCurrentUser();
                       registerTeacherToDB(currentUser.getUid(), name, email, subject);
                   } else {
                       Log.d(TAG, "createUserWithEmail:failure", task.getException());
                       Toast.makeText(getActivity(), "Authentication failed.",
                               Toast.LENGTH_SHORT).show();
                   }
                });
    }

    private void registerTeacherToDB(String uid, String name, String email, String subject) {
        DatabaseReference teacherRef = FirebaseDatabase.getInstance().getReference("Teachers");
        Teacher teacher = new Teacher(uid, name, email, subject);
        teacherRef.child(uid).setValue(teacher).addOnCompleteListener(task-> {
            if (task.isSuccessful()){
                Log.d(TAG, "registerTeacherToDB: success");
                Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                startActivity(homeIntent);
                getActivity().finish();
            } else {
                Log.d(TAG, "registerTeacherToDB: failure", task.getException());
                Toast.makeText(getActivity(), "Error while creating user!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}