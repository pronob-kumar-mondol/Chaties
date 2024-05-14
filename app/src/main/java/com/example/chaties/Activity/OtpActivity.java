package com.example.chaties.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chaties.R;
import com.example.chaties.Utils.AndroidUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class OtpActivity extends AppCompatActivity {

    String phoneNumber,verificationCode;
    Long timeOutSeconds=60L;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    EditText otpInput;
    AppCompatButton otpBtn;
    ProgressBar progressBar;
    TextView resendOtp;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otpInput = findViewById(R.id.otp_ET);
        otpBtn = findViewById(R.id.otp_nxt_btn);
        progressBar = findViewById(R.id.otp_progressbar);
        resendOtp = findViewById(R.id.resend_otp);


        phoneNumber= getIntent().getStringExtra("phone");

        sendOTP(phoneNumber, false);

        otpBtn.setOnClickListener(view -> {
            String enteredOtp = otpInput.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, enteredOtp);
            signIn(credential);
            setInProgress(true);
        });

        resendOtp.setOnClickListener(view -> {
            sendOTP(phoneNumber, true);
        });



    }


    void sendOTP(String phoneNumber, boolean isResend) {
        startResendTimer();
        setInProgress(true);
        PhoneAuthOptions.Builder builder=
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(timeOutSeconds, java.util.concurrent.TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signIn(phoneAuthCredential);
                                setInProgress(false);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                AndroidUtils.showToast(getApplicationContext(),"OTP Varification Failed");
                                setInProgress(false);
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                resendingToken = forceResendingToken;
                                AndroidUtils.showToast(getApplicationContext(),"OTP Sent Successfully");
                                setInProgress(false);
                            }
                        });
        if (isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    void startResendTimer() {
        resendOtp.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resendOtp.setText("Resend OTP in " + timeOutSeconds + " seconds");
                    }
                });
                timeOutSeconds--;
                if (timeOutSeconds <= 0) {
                    timeOutSeconds = 60L;
                    timer.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resendOtp.setEnabled(true);
                            resendOtp.setText("Resend OTP");
                        }
                    });
                }
            }
        }, 0, 1000); // Change the interval to 1000 milliseconds for 1-second updates
    }


    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        setInProgress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
            setInProgress(false);
            if (task.isSuccessful()){
                Intent intent=new Intent(OtpActivity.this,UsernameActivity.class);
                intent.putExtra("phone",phoneNumber);
                startActivity(intent);
            }else{
                AndroidUtils.showToast(getApplicationContext(),"OTP Varification Failed");
            }

        });
    }

    void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            otpBtn.setEnabled(false);
        }else{
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            otpBtn.setEnabled(true);
        }
    }
}