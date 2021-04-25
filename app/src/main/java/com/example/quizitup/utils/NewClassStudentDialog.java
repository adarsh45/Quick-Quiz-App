package com.example.quizitup.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.quizitup.R;
import com.example.quizitup.pojos.Class;
import com.example.quizitup.pojos.Student;
import com.example.quizitup.pojos.Teacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class NewClassStudentDialog extends DialogFragment {

    private static final String TAG = "NewClassDialog";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference studentsRef = FirebaseDatabase.getInstance()
            .getReference("Students").child(Objects.requireNonNull(mAuth.getUid()));
    private DatabaseReference classesRef = FirebaseDatabase.getInstance()
            .getReference("Classes");
    private DatabaseReference inviteCodesRef = FirebaseDatabase.getInstance()
            .getReference("inviteCodes");

    private Activity activity;
    private View view;

    private HashMap<String, String> inviteCodes;

    LinearLayout layoutLoading;
    Button btnJoinClass, btnCancelJoinClass;
    EditText etJoinClassCode;

    Student student;

    public NewClassStudentDialog(Activity activity, Student student){
        this.activity = activity;
        this.student = student;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_new_class_student, container,false);

        initViews();

        btnJoinClass.setOnClickListener(v-> joinClass());

        btnCancelJoinClass.setOnClickListener(v-> dismiss());

        setCancelable(false);
        return view;
    }

    private void joinClass() {
        layoutLoading.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(etJoinClassCode.getText())){
            etJoinClassCode.setError("Invite code cannot be empty!");
            return;
        }
        String inviteCode = etJoinClassCode.getText().toString();
        DatabaseReference uniqueCodeRef = inviteCodesRef.child(inviteCode);
        uniqueCodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    studentsRef.child("enrolledClasses").child(inviteCode).setValue(true)
                            .addOnCompleteListener(task-> {
                                if (task.isSuccessful()){
                                    classesRef.child(inviteCode)
                                            .child("studentsEnrolledMap").child(mAuth.getUid()).setValue(true)
                                            .addOnCompleteListener(task1-> {
                                                if (task1.isSuccessful()){
                                                    Toast.makeText(activity, "Class Joined Successfully!", Toast.LENGTH_SHORT).show();
                                                    dismiss();
                                                } else {
                                                    Toast.makeText(activity, "Something went wrong! Please retry!", Toast.LENGTH_SHORT).show();
                                                }
                                                layoutLoading.setVisibility(View.GONE);
                                            });
                                } else {
                                    layoutLoading.setVisibility(View.GONE);
                                    Toast.makeText(activity, "Something went wrong! Please retry!", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    layoutLoading.setVisibility(View.GONE);
                    Toast.makeText(activity, "No class found! Please check invite code!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error.getMessage());
                Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
                layoutLoading.setVisibility(View.GONE);
            }
        });
    }

    private void initViews() {
        layoutLoading = view.findViewById(R.id.layout_loading_join_class);
        etJoinClassCode = view.findViewById(R.id.et_join_class_code);
        btnJoinClass = view.findViewById(R.id.btn_join_class);
        btnCancelJoinClass = view.findViewById(R.id.btn_cancel_join_class);
    }

}
