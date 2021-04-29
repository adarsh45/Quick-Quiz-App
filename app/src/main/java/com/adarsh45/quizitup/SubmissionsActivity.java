package com.adarsh45.quizitup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.adarsh45.quizitup.adapters.SubmissionAdapter;
import com.adarsh45.quizitup.pojos.Student;
import com.adarsh45.quizitup.pojos.Submission;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SubmissionsActivity extends AppCompatActivity {

    private TextView tvNoSubmissions;
    private RecyclerView rvSubmissions;
    private static final String TAG = "SubmissionsActivity";

    private DatabaseReference classesRef = FirebaseDatabase.getInstance().getReference("Classes");
    private DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("Students");

    private ArrayList<Submission> studentSubmissions = new ArrayList<>();
    private SubmissionAdapter submissionAdapter;
    private String classInviteCode, quizId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submissions);

//      back button in action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        setupRV();
        getData();
        fetchSubmissions();
    }

    private void getData() {
        classInviteCode = getIntent().getStringExtra("classInviteCode");
        quizId = getIntent().getStringExtra("quizId");
    }

    private void fetchSubmissions() {
        classesRef.child(classInviteCode).child("studentsEnrolledMap")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot snap: snapshot.getChildren()){

                                studentRef.child(snap.getKey())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot studentSnap) {
                                                if (studentSnap.exists()){
//                                                    student has given the exam
                                                    Student student = studentSnap.getValue(Student.class);
                                                    try {
                                                        if (student.getAttemptedQuizzesMap().get(classInviteCode).get(quizId) != null){
//                                                        student has given the exam
                                                            Log.d(TAG, "onDataChange: ");
                                                            String studentName = student.getStudentName();
                                                            String studentMobile = student.getMobile();
                                                            String score = student.getAttemptedQuizzesMap().get(classInviteCode).get(quizId).getScore();

                                                            studentSubmissions.add(new Submission(student.getStudentId(), studentName, studentMobile, score));
//                                                            show RV & hide text no submissions
                                                            tvNoSubmissions.setVisibility(View.GONE);
                                                            rvSubmissions.setVisibility(View.VISIBLE);
                                                            submissionAdapter.notifyDataSetChanged();
                                                            Log.d(TAG, "onDataChange: ST NAME: " + studentName);
                                                            Log.d(TAG, "onDataChange: ST MOBILE: " + studentMobile);
                                                            Log.d(TAG, "onDataChange: ST SCORE: " + score);
                                                        }
                                                    } catch (NullPointerException e){
                                                        Log.d(TAG, "onDataChange: NULL QUIZ NOT GIVEN");
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Log.d(TAG, "onCancelled: " + error.getMessage());
                                                Toast.makeText(SubmissionsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(SubmissionsActivity.this, "No Students Enrolled in this class!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: " + error.getMessage());
                        Toast.makeText(SubmissionsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupRV() {
//        declare adapter for submissions
        submissionAdapter = new SubmissionAdapter(studentSubmissions);
        rvSubmissions.setAdapter(submissionAdapter);
        rvSubmissions.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initViews() {
        tvNoSubmissions = findViewById(R.id.tv_no_submissions);
        rvSubmissions = findViewById(R.id.rv_submissions);
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