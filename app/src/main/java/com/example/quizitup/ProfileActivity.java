package com.example.quizitup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.quizitup.pojos.Student;
import com.example.quizitup.pojos.Teacher;
import com.example.quizitup.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private RadioGroup radioGroupRegister;
    private EditText etRegisterName, etRegisterEmail;
    private Button btnRegister;

    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        if (TextUtils.isEmpty(etRegisterName.getText())){
            etRegisterName.setError("Name cannot be empty!");
            return;
        }
        if (TextUtils.isEmpty(etRegisterEmail.getText())){
            etRegisterEmail.setError("Email cannot be empty!");
            return;
        }

        String uid = mAuth.getUid();
        String name = etRegisterName.getText().toString();
        String email = etRegisterEmail.getText().toString();
        String mobile = mAuth.getCurrentUser().getPhoneNumber();

        if (uid == null || mobile == null){
            Toast.makeText(this, "Error authenticating! Please restart the app!", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            return;
        }

        int selectedId = radioGroupRegister.getCheckedRadioButtonId();
        if (selectedId == R.id.radio_btn_register_teacher){
            registerTeacherToDB(uid, name, email, mobile);
        } else {
            registerStudentToDB(uid, name, email, mobile);
        }
    }

    private void registerStudentToDB(String uid, String name, String email, String mobile) {
        DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("Students");
        Student student = new Student(uid, name, email, mobile);
        studentRef.child(uid).setValue(student).addOnCompleteListener(task-> {
            if (task.isSuccessful()){
                Log.d(TAG, "registerStudentToDB: success");
                Intent homeIntent = new Intent(ProfileActivity.this, HomeActivity.class);
                homeIntent.putExtra("origin", Utils.STUDENT_DATA);
                Log.d(TAG, "onDataChange: " + student.getStudentName());
                startActivity(homeIntent);
                startActivity(homeIntent);
                finish();
            } else {
                Log.d(TAG, "registerStudentToDB: failure", task.getException());
                Toast.makeText(this, "Error while creating user!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void registerTeacherToDB(String uid, String name, String email, String mobile) {
        DatabaseReference teacherRef = FirebaseDatabase.getInstance().getReference("Teachers");
        Teacher teacher = new Teacher(uid, name, email, mobile);
        teacherRef.child(uid).setValue(teacher).addOnCompleteListener(task-> {
            if (task.isSuccessful()){
                Log.d(TAG, "registerTeacherToDB: success");
                Intent homeIntent = new Intent(ProfileActivity.this, HomeActivity.class);
                homeIntent.putExtra("origin", Utils.TEACHER_DATA);
                Log.d(TAG, "onDataChange: " + teacher.getTeacherName());
                startActivity(homeIntent);
                startActivity(homeIntent);
                finish();
            } else {
                Log.d(TAG, "registerTeacherToDB: failure", task.getException());
                Toast.makeText(this, "Error while creating user!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        radioGroupRegister = findViewById(R.id.radio_group_register);
        etRegisterName = findViewById(R.id.et_register_name);
        etRegisterEmail = findViewById(R.id.et_register_email);
        btnRegister = findViewById(R.id.btn_register);
    }
}