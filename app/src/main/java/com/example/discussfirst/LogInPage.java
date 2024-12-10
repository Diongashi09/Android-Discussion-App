package com.example.discussfirst;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LogInPage extends AppCompatActivity {

    private TextInputEditText emailInpEditTxt;
    private TextInputEditText passwordInpEditTxt;
    private Button btnLogIn;
    private TextView registerNowTextView;
    private TextView forgotPasswordTextView;

    private dbConnect db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in_page);

        emailInpEditTxt = findViewById(R.id.EmailInputL);
        passwordInpEditTxt = findViewById(R.id.PasswordInputL);
        btnLogIn = findViewById(R.id.btnLoginL);
        registerNowTextView = findViewById(R.id.txtGoRegister);
        forgotPasswordTextView = findViewById(R.id.textView6);

        db = dbConnect.getInstance(this);

        registerNowTextView.setOnClickListener(view -> {
            Intent i = new Intent(LogInPage.this, RegisterPage.class);
            startActivity(i);
        });

        forgotPasswordTextView.setOnClickListener(view -> {
            Intent i = new Intent(LogInPage.this, ForgotPassword.class);
            startActivity(i);
        });

        btnLogIn.setOnClickListener(view -> validateFields());
    }

    private void validateFields() {
        String email = emailInpEditTxt.getText().toString().trim();
        String password = passwordInpEditTxt.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        loginUser(email, password);
    }

    private void loginUser(String email, String password) {
        try {
            boolean userExists = db.checkUser(email, password);
            boolean tempPasswordValid = db.verifyTemporaryPassword(email, password);

            if (userExists || tempPasswordValid) {
                if (tempPasswordValid) {
                    // Clear the temporary password if used
                    db.clearTemporaryPassword(email);
                    Toast.makeText(this, "Temporary password used. Update your password!", Toast.LENGTH_LONG).show();
                }

                new AlertDialog.Builder(this)
                        .setTitle("Success")
                        .setMessage("Login successful!")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .setCancelable(false)
                        .show();

                db.close();
                Intent i = new Intent(LogInPage.this, EmailVerification.class);
                i.putExtra("USER_EMAIL", email);
                startActivity(i);
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Invalid email or password!")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .setCancelable(false)
                        .show();
            }
        } catch (Exception e) {
            Log.e("LoginError", "Error while checking user", e);
        }
    }
}
