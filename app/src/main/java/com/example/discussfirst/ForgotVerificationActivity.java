package com.example.discussfirst;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForgotVerificationActivity extends AppCompatActivity {

    private EditText inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6;
    private Button verifyButton;
    private String sentCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_verification);

        // Retrieve the email passed from the previous activity
        String emailFromPrevious = getIntent().getStringExtra("USER_EMAIL");

        // Initialize the views
        inputCode1 = findViewById(R.id.inputCode1);
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);
        inputCode5 = findViewById(R.id.inputCode5);
        inputCode6 = findViewById(R.id.inputCode6);
        verifyButton = findViewById(R.id.buttonVerify);

        // Retrieve the sent code
        sentCode = getIntent().getStringExtra("sentCode");

        verifyButton.setOnClickListener(v -> {
            // Combine the codes from the input fields
            String enteredCode = inputCode1.getText().toString().trim() +
                    inputCode2.getText().toString().trim() +
                    inputCode3.getText().toString().trim() +
                    inputCode4.getText().toString().trim() +
                    inputCode5.getText().toString().trim() +
                    inputCode6.getText().toString().trim();

            // Validate the entered code
            if (enteredCode.isEmpty() || enteredCode.length() != 6) {
                Toast.makeText(ForgotVerificationActivity.this, "Fut një kod të vlefshëm 6-shifror", Toast.LENGTH_SHORT).show();
            } else if (enteredCode.equals(sentCode)) {
                // If verification is successful
                Toast.makeText(ForgotVerificationActivity.this, "Kodi u verifikua me sukses!", Toast.LENGTH_SHORT).show();

                // Transition to ProfilePage with the email
                Intent intent = new Intent(ForgotVerificationActivity.this, newpassword.class);
                intent.putExtra("USER_EMAIL", emailFromPrevious); // Pass the email
                startActivity(intent); // Start the activity
                finish();
            } else {
                // If verification fails
                Toast.makeText(ForgotVerificationActivity.this, "Kodi i pasaktë i verifikimit", Toast.LENGTH_SHORT).show();
            }
        });
    }
}