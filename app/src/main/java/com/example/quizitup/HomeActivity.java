package com.example.quizitup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizitup.adapters.ClassAdapter;
import com.example.quizitup.pojos.Class;
import com.example.quizitup.pojos.Student;
import com.example.quizitup.pojos.Teacher;
import com.example.quizitup.utils.NewClassDialog;
import com.example.quizitup.utils.NewClassStudentDialog;
import com.example.quizitup.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements ClassAdapter.OnClassCardClickListener, ClassAdapter.OnAddClassClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference classesRef = FirebaseDatabase.getInstance().getReference("Classes");
    private DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("Students").child(mAuth.getUid());
    private DatabaseReference teacherRef = FirebaseDatabase.getInstance().getReference("Teachers").child(mAuth.getUid());
    private ClassAdapter classAdapter;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private View header;
    private TextView tvNavInitialChar, tvNavName, tvNavMobile, tvNavEmail;
    private byte origin = 0;

    private TextView tvName;
    private RecyclerView rvClassesList;

    private static final String TAG = "HomeActivity";

    private ArrayList<Class> classesList = new ArrayList<>();

    private Student student;
    private Teacher teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        getTeacherOrStudent();
        setupRV();
        setupDrawer();
        fetchClasses();

    }

    private void setupDrawer() {
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.menu_profile:
                    Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.menu_main_about:
//                    startActivity(new Intent(HomeActivity.this, AboutAppActivity.class));
                    break;
                case R.id.menu_logout:
                    mAuth.signOut();
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                    break;
            }

            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void fetchClasses() {
        if (origin == Utils.STUDENT_DATA){
            classesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    classesList.clear();
                    if (snapshot.exists()){
                        for (DataSnapshot snap: snapshot.getChildren()){
                            Class classObj = snap.getValue(Class.class);
                            if (classObj.getStudentsEnrolledMap() != null){
                                for (Map.Entry<String, Boolean> studentId : classObj.getStudentsEnrolledMap().entrySet()){
                                    if (studentId.getKey().equals(mAuth.getUid())){
                                        classesList.add(classObj);
                                    }
                                }
                            }
                        }
                    }
                    classesList.add(new Class());
                    classAdapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "onCancelled: " + error.getMessage());
                    Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (origin == Utils.TEACHER_DATA){
            Query classesQuery = classesRef.orderByChild("createdBy").equalTo(mAuth.getUid());
            classesQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    classesList.clear();
                    if (snapshot.exists()){
                        for (DataSnapshot snap: snapshot.getChildren()){
                            Class classObj = snap.getValue(Class.class);
                            classesList.add(classObj);
                        }
                        Log.d(TAG, "onDataChange: " + classesList.size());
                    } else {
                        Log.d(TAG, "onDataChange: No classes found!");
                    }
                    classesList.add(new Class());
                    classAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "onCancelled: " + error.getMessage());
                    Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getTeacherOrStudent() {
        origin = getIntent().getByteExtra("origin", Utils.STUDENT_DATA);
        if (origin == Utils.STUDENT_DATA){
            studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        Log.d(TAG, "onDataChange: STUDENT SNAP: " + snapshot);
                        student = snapshot.getValue(Student.class);
                        tvName.setText(student.getStudentName() + " (Student)");
                        tvNavInitialChar.setText(String.valueOf(student.getStudentName().charAt(0)));
                        tvNavName.setText(student.getStudentName()  + " (Student)");
                        tvNavMobile.setText(student.getMobile());
                        tvNavEmail.setText(student.getEmail());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "onCancelled: " + error.getMessage());
                    Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if(origin == Utils.TEACHER_DATA) {
            teacherRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        teacher = snapshot.getValue(Teacher.class);
                        tvName.setText(teacher.getTeacherName()  + " (Teacher)");
                        tvNavInitialChar.setText(String.valueOf(teacher.getTeacherName().charAt(0)));
                        tvNavName.setText(teacher.getTeacherName() + " (Teacher)");
                        tvNavMobile.setText(teacher.getMobile());
                        tvNavEmail.setText(teacher.getEmail());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "onCancelled: " + error.getMessage());
                    Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setupRV() {
        classAdapter = new ClassAdapter(classesList, origin, this, this);
        rvClassesList.setAdapter(classAdapter);
        rvClassesList.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void initViews() {
        //init drawer_layout and toggle_button
        mDrawerLayout = findViewById(R.id.layout_drawer_home);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout, R.string.open, R.string.close);
        navigationView = findViewById(R.id.nav_layout);
        header = navigationView.getHeaderView(0);

        tvNavInitialChar = header.findViewById(R.id.tv_nav_name_initial);
        tvNavName = header.findViewById(R.id.tv_nav_name);
        tvNavMobile = header.findViewById(R.id.tv_nav_mobile);
        tvNavEmail = header.findViewById(R.id.tv_nav_email);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvName = findViewById(R.id.tv_welcome_name);
        rvClassesList = findViewById(R.id.rv_classes_list);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    rv cards click methods (interface methods)

    @Override
    public void onAddClassClick(int position) {
//            click method for add class card
        if (origin == Utils.TEACHER_DATA){
            if (teacher == null){
                Toast.makeText(this, "Teacher details not found! Please restart the app!", Toast.LENGTH_SHORT).show();
                return;
            }
            NewClassDialog newClassDialog = new NewClassDialog(this, teacher);
            newClassDialog.show(getSupportFragmentManager(), "Add New Class");
        } else if (origin == Utils.STUDENT_DATA){
            if (student == null){
                Toast.makeText(this, "Student details not found! Please restart the app!", Toast.LENGTH_SHORT).show();
                return;
            }
            NewClassStudentDialog newClassStudentDialog = new NewClassStudentDialog(this, student);
            newClassStudentDialog.show(getSupportFragmentManager(), "Join New Class");
        }
    }

    @Override
    public void onClassCardClick(int position) {
//        click method for class card
        Intent intent = new Intent(HomeActivity.this, QuizListActivity.class);
        intent.putExtra("classInviteCode", classesList.get(position).getInviteCode());
        intent.putExtra("classTitle", classesList.get(position).getClassTitle());
        intent.putExtra("origin", origin);
        startActivity(intent);
    }

    @Override
    public void onDeleteClassClick(int position) {
        Class classObjToDelete = classesList.get(position);
        FirebaseDatabase.getInstance()
                .getReference("Classes")
                .child(classObjToDelete.getInviteCode())
                .removeValue((error, ref) -> {
           if (error == null){
               FirebaseDatabase.getInstance()
                       .getReference("inviteCodes")
                       .child(classObjToDelete.getInviteCode())
                       .removeValue((error1, ref1) -> {
                           if (error1 == null){
                               Toast.makeText(this, "Class Deleted Successfully!", Toast.LENGTH_SHORT).show();
                           } else {
                               Toast.makeText(this, error1.getMessage(), Toast.LENGTH_SHORT).show();
                           }
                       });

           } else {
               Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
           }
        });
    }

    @Override
    public void onEditClassClick(int position) {
        if (teacher == null){
            Toast.makeText(this, "Teacher details not found! Please restart the app!", Toast.LENGTH_SHORT).show();
            return;
        }
        NewClassDialog newClassDialog = new NewClassDialog(this, teacher, classesList.get(position).getClassTitle(), classesList.get(position).getInviteCode());
        newClassDialog.show(getSupportFragmentManager(), "Edit Class");
    }



}