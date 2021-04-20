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

public class NewClassDialog extends DialogFragment {
    
    private static final String TAG = "NewClassDialog";

    private Activity activity;

    private View view;

    Button btnGenerateCode, btnCancelNewClass;
    EditText etClassTitle;
    TextView tvCode;
    ImageView imgShareCode, imgCopyCode;
    LinearLayout layoutInviteCode;

    public NewClassDialog(Activity activity){
        this.activity = activity;
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
        String classTitle = etClassTitle.getText().toString();

        Log.d(TAG, "onCreateView: CLASS TITLE: " + classTitle);
        tvCode.setText("5oG89Q");
        layoutInviteCode.setVisibility(View.VISIBLE);
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
