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

public class NewClassDialog extends DialogFragment {
    
    private static final String TAG = "NewClassDialog";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference teacherRef = FirebaseDatabase.getInstance()
            .getReference("Teachers").child(Objects.requireNonNull(mAuth.getUid()));
    private DatabaseReference classesRef = FirebaseDatabase.getInstance()
            .getReference("Classes");

    private DatabaseReference inviteCodesRef = FirebaseDatabase.getInstance()
            .getReference("inviteCodes");

    private Activity activity;
    private View view;

    private HashMap<String, String> inviteCodes;

    LinearLayout layoutLoading;
    Button btnGenerateCode, btnCancelNewClass;
    EditText etClassTitle;
    TextView tvCode;
    ImageView imgShareCode, imgCopyCode;
    LinearLayout layoutInviteCode;

    Teacher teacher;

    public NewClassDialog(Activity activity, Teacher teacher){
        this.activity = activity;
        this.teacher = teacher;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_new_class, container,false);

        initViews();

        btnGenerateCode.setOnClickListener(v-> generateCode());

        btnCancelNewClass.setOnClickListener(v-> dismiss());
        
        imgCopyCode.setOnClickListener(v-> copyToClipBoard());
        imgShareCode.setOnClickListener(v-> shareInviteCode());

        setCancelable(false);
        return view;
    }

    private void initViews() {
        layoutLoading = view.findViewById(R.id.layout_loading_new_class);
        etClassTitle = view.findViewById(R.id.et_class_title);
        btnGenerateCode = view.findViewById(R.id.btn_generate_code);
        btnCancelNewClass = view.findViewById(R.id.btn_cancel_new_class);
        tvCode = view.findViewById(R.id.tv_invite_code);
        imgShareCode = view.findViewById(R.id.img_share_code);
        imgCopyCode = view.findViewById(R.id.img_copy_code);
        layoutInviteCode = view.findViewById(R.id.layout_copy_code);
    }

    private void generateCode() {
        if(TextUtils.isEmpty(etClassTitle.getText())){
            etClassTitle.setError("Class Name cannot be empty!");
            return;
        }
        btnGenerateCode.setEnabled(false);
        layoutLoading.setVisibility(View.VISIBLE);
        String classTitle = etClassTitle.getText().toString();
//        TODO: generate random string for invite code and check if it is present already

        createAndWriteInviteCode();
        Log.d(TAG, "onCreateView: CLASS TITLE: " + classTitle);

    }

    private void createAndWriteInviteCode() {
        String inviteCode = Utils.generateRandomCode(6);
        DatabaseReference uniqueCodeRef = inviteCodesRef.child(inviteCode);

        uniqueCodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
//                    invite code is already used create new one
                    Log.d(TAG, "onDataChange: " + snapshot.toString());
//                    WARNING: recursion
                    createAndWriteInviteCode();
                } else {
//                    invite code is unique, first update it with database
                    Log.d(TAG, "onDataChange: INVITE CODE NOT FOUND");
                    if(teacher == null){
                        Toast.makeText(activity, "Teacher Details not found! Please restart the app!", Toast.LENGTH_SHORT).show();
                        layoutLoading.setVisibility(View.GONE);
                        return;
                    }
                    inviteCodesRef.child(inviteCode).setValue(teacher.getTeacherId())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            tvCode.setText(inviteCode);
                            layoutInviteCode.setVisibility(View.VISIBLE);
                            Log.d(TAG, "onDataChange: INVITE CODE " + inviteCode + " added to DB");
                            
//                            creating lot of hashmap data
                            Teacher.ClassCreated classCreated = new Teacher.ClassCreated(inviteCode);
                            HashMap<String, Teacher.ClassCreated> classCreatedHashMap = new HashMap<>();
                            classCreatedHashMap.put(inviteCode, classCreated);
                            
                            teacher.setClassCreatedMap(classCreatedHashMap);
                            teacherRef.setValue(teacher)
                                    .addOnCompleteListener(taskComplete-> {
                                        if (taskComplete.isSuccessful()){
                                            Log.d(TAG, "onDataChange: Teacher updated!");
                                            Class classObj = new Class(inviteCode, etClassTitle.getText().toString(), teacher.getTeacherId(), teacher.getTeacherName());
                                            classesRef.child(inviteCode).setValue(classObj)
                                                    .addOnCompleteListener(taskClass-> {
                                                        layoutLoading.setVisibility(View.GONE);
                                                        if (taskClass.isSuccessful()){
                                                            Log.d(TAG, "onDataChange: CLASS NODE UPDATED!");
                                                        } else {
                                                            Log.d(TAG, "onDataChange: CLASS NODE NOT UPDATED");
                                                        }
                                                    });
                                        } else {
                                            layoutLoading.setVisibility(View.GONE);
                                            Log.d(TAG, "onDataChange: Teacher details not updated!");
                                        }
                                    });
                        } else {
                            layoutLoading.setVisibility(View.GONE);
                            Log.d(TAG, "onDataChange: CODE NOT ADDED TO DB");
                            Toast.makeText(activity, "Code could not be registered with DB, please try again!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                layoutLoading.setVisibility(View.GONE);
                Log.d(TAG, "onCancelled: " + error.getMessage());
                Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void shareInviteCode() {
        if(TextUtils.isEmpty(tvCode.getText())){
            Toast.makeText(activity, "Code not generated! Please try again!", Toast.LENGTH_SHORT).show();
            return;
        }
        /*Create an ACTION_SEND Intent*/
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        /*Applying information Subject and Body.*/
        String shareText = "Join the, " + etClassTitle.getText() + " by entering following invite code in Quiz It Up app\n" + tvCode.getText();
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
        /*Fire!*/
        startActivity(Intent.createChooser(intent, "Share Invite Code via..."));
    }

    private void copyToClipBoard() {
        if(TextUtils.isEmpty(tvCode.getText())){
            Toast.makeText(activity, "Code not generated! Please try again!", Toast.LENGTH_SHORT).show();
            return;
        }
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Invite Code", tvCode.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(activity, "Invite Code copied to clipboard!", Toast.LENGTH_SHORT).show();
    }
}
