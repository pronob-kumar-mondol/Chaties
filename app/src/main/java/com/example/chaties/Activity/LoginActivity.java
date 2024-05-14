package com.example.chaties.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chaties.R;
import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    EditText phoneInput;
    AppCompatButton send_otp_btn;
    ProgressBar login_progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        countryCodePicker = findViewById(R.id.login_Countrycode_picker);
        phoneInput = findViewById(R.id.login_phone_number);
        send_otp_btn = findViewById(R.id.send_otp_btn);
        login_progressbar = findViewById(R.id.login_progressbar);
        login_progressbar.setVisibility(ProgressBar.INVISIBLE);

        countryCodePicker.registerCarrierNumberEditText(phoneInput);

        send_otp_btn.setOnClickListener(v -> {
            if (!countryCodePicker.isValidFullNumber()) {
                phoneInput.setError("Enter a valid phone number");
                return;
            }
            Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
            intent.putExtra("phone", countryCodePicker.getFullNumberWithPlus());
            startActivity(intent);

        });

    }
}