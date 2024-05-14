package com.example.chaties.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chaties.Model.UserModel;
import com.example.chaties.R;
import com.example.chaties.Utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class UsernameActivity extends AppCompatActivity {

    EditText name_ET;
    AppCompatButton let_me_in_btn;
    ProgressBar progressBar;
    String phoneNumber;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        name_ET = findViewById(R.id.name_ET);
        let_me_in_btn = findViewById(R.id.let_me_in_btn);
        progressBar = findViewById(R.id.letmein_progressbar);

        phoneNumber = getIntent().getStringExtra("phone");

        getUserName();

        let_me_in_btn.setOnClickListener(view -> {
            setUsersName();
        });

    }

    private void setUsersName() {
        String name = name_ET.getText().toString();
        if (name.isEmpty() || name.length() < 3) {
            name_ET.setError("Enter a valid name");
            return;
        }
        setInProgress(true);
        if (userModel != null) {
            userModel.setUserName(name);
        }else {
            userModel = new UserModel(phoneNumber, name, Timestamp.now());
        }
        FirebaseUtils.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);
                if (task.isSuccessful()) {
                    Intent intent=new Intent(UsernameActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }
        });


    }

    private void getUserName() {
        setInProgress(true);
        FirebaseUtils.currentUserDetails().get().addOnCompleteListener(task -> {
            setInProgress(false);
            if (task.isSuccessful()) {
                userModel = task.getResult().toObject(UserModel.class);
                if (userModel != null) {
                    name_ET.setText(userModel.getUserName());
                }
            }


        });
    }


    void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            let_me_in_btn.setEnabled(false);
        }else{
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            let_me_in_btn.setEnabled(true);
        }
    }
}