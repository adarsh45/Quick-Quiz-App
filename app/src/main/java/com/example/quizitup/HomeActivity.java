package com.example.quizitup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizitup.adapters.ClassAdapter;
import com.example.quizitup.utils.NewClassDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;

public class HomeActivity extends AppCompatActivity implements ClassAdapter.OnAddClassClickListener, ClassAdapter.OnClassCardClickListener {

//    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private RecyclerView rvClassesList;

    private String[] classesNames = {"", "some", "other", "subject", "and", "again"};

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
        setupRV();

    }

    private void setupRV() {
        if(classesNames.length <= 0){
            return;
        }
        ClassAdapter classAdapter = new ClassAdapter(Arrays.asList(classesNames), this::onAddClassClick, this::onClassCardClick);
        rvClassesList.setAdapter(classAdapter);
        rvClassesList.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void initViews() {
        rvClassesList = findViewById(R.id.rv_classes_list);
    }

    @Override
    public void onAddClassClick(int position) {
        NewClassDialog newClassDialog = new NewClassDialog(this);
        newClassDialog.show(getSupportFragmentManager(), "Add New Class");
    }

    @Override
    public void onClassCardClick(int position) {
//        TODO: create new activity for showing all quizzes
        Toast.makeText(this, "Position: "+ position, Toast.LENGTH_SHORT).show();
    }
}