package com.example.quizitup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.quizitup.pojos.Student;
import com.example.quizitup.pojos.Teacher;
import com.example.quizitup.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("Students");
    private DatabaseReference teachersRef = FirebaseDatabase.getInstance().getReference("Teachers");
    private Intent splashIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            checkIfRegistered();
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }

    }

    private void checkIfRegistered() {
        String uid = mAuth.getUid();
        Query studentExistsQuery = studentRef.orderByChild("studentId").equalTo(uid);
        Query teacherExistsQuery = teachersRef.orderByChild("teacherId").equalTo(uid);

        Log.d(TAG, "checkIfRegistered: AT LEAST CHECKING IF REGISTERED");

//        here nested code is used that is second query is called inside of first query's else part
//        and final default code is called inside else part of second query
        /*
        studentQuery
                if yes -> go to home activity
                else -> teacherExistsQuery
                            if yes -> go to home activity
                            else -> go to profile activity
       */

        studentExistsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Student student = snapshot.getValue(Student.class);
                    Log.d(TAG, "onDataChange: " + snapshot.toString());
                    splashIntent = new Intent(SplashActivity.this, HomeActivity.class);
                    splashIntent.putExtra("origin", Utils.STUDENT_DATA);
                    Log.d(TAG, "onDataChange: " + student.getStudentName());
                    startActivity(splashIntent);
                    finish();
                } else {

                    teacherExistsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                Teacher teacher = snapshot.getValue(Teacher.class);
                                Log.d(TAG, "onDataChange: " + snapshot.toString());
                                splashIntent = new Intent(SplashActivity.this, HomeActivity.class);
                                splashIntent.putExtra("origin", Utils.TEACHER_DATA);
                                Log.d(TAG, "onDataChange: " + teacher.getTeacherName());
                            } else {
                                Log.d(TAG, "onDataChange: TEACHER SNAPSHOT DOESN'T EXISTS!");
                                Log.d(TAG, "onDataChange: STUDENT SNAPSHOT DOESN'T EXISTS!");
                                splashIntent = new Intent(SplashActivity.this, ProfileActivity.class);
                            }
                            startActivity(splashIntent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d(TAG, "onCancelled: " + error.getMessage());
                            Toast.makeText(SplashActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            splashIntent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(splashIntent);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error.getMessage());
                Toast.makeText(SplashActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Intent profileIntent = new Intent(SplashActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
            }
        });


    }
}