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
import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements ClassAdapter.OnAddClassClickListener, ClassAdapter.OnClassCardClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference classesRef = FirebaseDatabase.getInstance().getReference("Classes");
    private ClassAdapter classAdapter;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private View header;
    private TextView tvNavInitialChar, tvNavName, tvNavMobile, tvNavEmail;

    private TextView tvName;
    private RecyclerView rvClassesList;

    private static final String TAG = "HomeActivity";

    private String[] classesNames = {"", "some", "other", "subject", "and", "again"};
    private ArrayList<Class> classesList = new ArrayList<>();

    private Student student;
    private Teacher teacher;
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (mAuth != null){
//            mAuth.signOut();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupDrawer();
        getTeacherOrStudent();
        setupRV();
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
        Query classesQuery = classesRef.orderByChild("createdBy").equalTo(mAuth.getUid());
        classesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    classesList.clear();
                    for (DataSnapshot snap: snapshot.getChildren()){
                        Class classObj = snap.getValue(Class.class);
                        classesList.add(classObj);
                    }
                    Log.d(TAG, "onDataChange: " + classesList.size());
                } else {
                    Log.d(TAG, "onDataChange: No classes found!");
                    classesList.clear();
                }
                classesList.add(new Class("dummy", "", "", ""));
                classAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error.getMessage());
                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTeacherOrStudent() {
        if (getIntent().getExtras().getByte("origin") == Utils.STUDENT_DATA){
            student = getIntent().getExtras().getParcelable("studentData");
            tvName.setText(student.getStudentName());
            tvNavInitialChar.setText(String.valueOf(student.getStudentName().charAt(0)));
            tvNavName.setText(student.getStudentName());
            tvNavMobile.setText(student.getMobile());
            tvNavEmail.setText(student.getEmail());
        } else if(getIntent().getExtras().getByte("origin") == Utils.TEACHER_DATA) {
            teacher = getIntent().getExtras().getParcelable("teacherData");
            tvName.setText(teacher.getTeacherName());

            tvNavInitialChar.setText(String.valueOf(teacher.getTeacherName().charAt(0)));
            tvNavName.setText(teacher.getTeacherName());
            tvNavMobile.setText(teacher.getMobile());
            tvNavEmail.setText(teacher.getEmail());
        }
    }

    private void setupRV() {
        if(classesNames.length <= 0){
            return;
        }
        classAdapter = new ClassAdapter(classesList, this::onAddClassClick, this::onClassCardClick);
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

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

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

    @Override
    public void onAddClassClick(int position) {
        if (teacher == null){
            Toast.makeText(this, "Teacher details not found! Please restart the app!", Toast.LENGTH_SHORT).show();
            return;
        }
        NewClassDialog newClassDialog = new NewClassDialog(this, teacher);
        newClassDialog.show(getSupportFragmentManager(), "Add New Class");
    }

    @Override
    public void onClassCardClick(int position) {
        Intent intent = new Intent(HomeActivity.this, QuizListActivity.class);
        intent.putExtra("classData", classesList.get(position));
        startActivity(intent);
    }


}