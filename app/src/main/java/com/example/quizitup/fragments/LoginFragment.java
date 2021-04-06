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
import android.widget.Toast;

import com.example.quizitup.HomeActivity;
import com.example.quizitup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private EditText etLoginEmail, etLoginPassword;
    private Button btnLogin;

    private FirebaseAuth mAuth;

    private static final String TAG = "LoginFragment";

    public LoginFragment() {}

    public static LoginFragment newInstance() {
        return new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        etLoginEmail = view.findViewById(R.id.et_login_email);
        etLoginPassword = view.findViewById(R.id.et_login_password);
        btnLogin = view.findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(v-> {
            if (TextUtils.isEmpty(etLoginEmail.getText())){
                etLoginEmail.setError("Email cannot be empty!");
                return;
            }
            if (TextUtils.isEmpty(etLoginPassword.getText()) || etLoginPassword.getText().length() < 6){
                etLoginPassword.setError("Password should contain at least 6 characters long!");
                return;
            }
            String email = etLoginEmail.getText().toString();
            String password = etLoginPassword.getText().toString();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Log.d(TAG, "login: success");
                    Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(homeIntent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onCreateView: failure. ", task.getException());
                }
            });
        });
        return view;
    }
}