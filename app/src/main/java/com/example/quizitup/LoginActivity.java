package com.example.quizitup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    
    private static final String TAG = "LoginActivity";

    String mVerificationId;

//    private TabLayout tabLayout;
//    private TabItem loginTabItem, registerTabItem;
//    private ViewPager viewPager;
//    private PageAdapter pageAdapter;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("Students");
    private DatabaseReference teachersRef = FirebaseDatabase.getInstance().getReference("Teachers");

    private EditText etPhone, etOtp;
    private Button btnSendOtp, btnVerifyOtp;
    private LinearLayout layoutLoading, layoutMobile, layoutOtp;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            checkIfRegistered();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        btnSendOtp.setOnClickListener(v-> sendOtp());

        btnVerifyOtp.setOnClickListener(v-> verifyOtp());


//        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(pageAdapter);
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//
//                if (tab.getPosition() == 0 || tab.getPosition() == 1){
//                    pageAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) { }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) { }
//        });
//
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    private void verifyOtp() {
        if (TextUtils.isEmpty(etOtp.getText()) || etPhone.getText().length() < 6){
            etOtp.setError("Invalid Code! Please try again!");
            return;
        }
        layoutLoading.setVisibility(View.VISIBLE);
        String codeEntered = etOtp.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, codeEntered);
        signInWithPhoneAuthCredential(credential);
    }

    private void sendOtp() {
        if (TextUtils.isEmpty(etPhone.getText()) || etPhone.getText().length() != 10){
            etPhone.setError("Phone number is invalid!");
            return;
        }
        String phoneNumber = "+91" + etPhone.getText().toString();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallback)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

        layoutLoading.setVisibility(View.VISIBLE);
    }

//    CALLBACK
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
    @Override
    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
        // This callback will be invoked in two situations:
        // 1 - Instant verification. In some cases the phone number can be instantly
        //     verified without needing to send or enter a verification code.
        // 2 - Auto-retrieval. On some devices Google Play services can automatically
        //     detect the incoming verification SMS and perform verification without
        //     user action.
        Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);

        signInWithPhoneAuthCredential(phoneAuthCredential);
    }

    @Override
    public void onCodeSent(@NonNull String verificationId,
                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
        // The SMS verification code has been sent to the provided phone number, we
        // now need to ask the user to enter the code and then construct a credential
        // by combining the code with a verification ID.
        Log.d(TAG, "onCodeSent:" + verificationId);

        Toast.makeText(LoginActivity.this, "Code sent successfully!", Toast.LENGTH_SHORT).show();

//        hide mobile layout & show otp layout also hide loading layout
        layoutMobile.setVisibility(View.GONE);
        layoutOtp.setVisibility(View.VISIBLE);
        layoutLoading.setVisibility(View.GONE);

        // Save verification ID and resending token so we can use them later
        mVerificationId = verificationId;
//        mResendToken = token;
    }

    @Override
    public void onVerificationFailed(@NonNull FirebaseException e) {
        // This callback is invoked in an invalid request for verification is made,
        // for instance if the the phone number format is not valid.
        Log.w(TAG, "onVerificationFailed", e);

        if (e instanceof FirebaseAuthInvalidCredentialsException) {
            // Invalid request
            Log.d(TAG, "onVerificationFailed: INVALID REQUEST, " + e.getLocalizedMessage());

        } else if (e instanceof FirebaseTooManyRequestsException) {
            // The SMS quota for the project has been exceeded
            Log.d(TAG, "onVerificationFailed: SMS QUOTA Exceeded, " + e.getLocalizedMessage());
        }
        Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        layoutLoading.setVisibility(View.GONE);
    }
};

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    layoutLoading.setVisibility(View.GONE);
                   if (task.isSuccessful()){
                       Log.d(TAG, "signInWithPhoneAuthCredential: SIGN IN SUCCESSFUL!");
                       Toast.makeText(this, "Sign in successful!", Toast.LENGTH_SHORT).show();
//                       start profile activity
                       checkIfRegistered();
                   } else {
                       // Sign in failed, display a message and update the UI
                       Log.d(TAG, "signInWithCredential:failure", task.getException());
                       if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                           // The verification code entered was invalid
                           Toast.makeText(this, "Invalid Code, Please try again!", Toast.LENGTH_SHORT).show();
                       }
                   }
                });
    }

    private void checkIfRegistered() {
        layoutLoading.setVisibility(View.VISIBLE);
        String uid = mAuth.getUid();
        Query studentExistsQuery = studentRef.orderByChild("studentId").equalTo(uid);
        Query teacherExistsQuery = teachersRef.orderByChild("teacherId").equalTo(uid);

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
                    layoutLoading.setVisibility(View.GONE);
                    Log.d(TAG, "onDataChange: " + snapshot.toString());
                    Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    finish();
                } else {

                    teacherExistsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            layoutLoading.setVisibility(View.GONE);
                            if (snapshot.exists()){
                                Log.d(TAG, "onDataChange: " + snapshot.toString());
                                Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(homeIntent);
                                finish();
                            } else {
                                Log.d(TAG, "onDataChange: TEACHER SNAPSHOT DOESN'T EXISTS!");
                                Log.d(TAG, "onDataChange: STUDENT SNAPSHOT DOESN'T EXISTS!");
                                Intent profileIntent = new Intent(LoginActivity.this, ProfileActivity.class);
                                startActivity(profileIntent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d(TAG, "onCancelled: " + error.getMessage());
                            Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error.getMessage());
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initViews() {

        etPhone = findViewById(R.id.et_mobile_number);
        etOtp = findViewById(R.id.et_otp);
        btnSendOtp = findViewById(R.id.btn_send_otp);
        btnVerifyOtp = findViewById(R.id.btn_verify_otp);

        layoutMobile = findViewById(R.id.layout_mobile);
        layoutOtp = findViewById(R.id.layout_otp);
        layoutLoading = findViewById(R.id.layout_loading);

//        tabLayout = findViewById(R.id.tab_layout);
//        loginTabItem = findViewById(R.id.tab_item_login);
//        registerTabItem = findViewById(R.id.tab_item_register);
//        viewPager = findViewById(R.id.view_pager);
    }
}