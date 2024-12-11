package com.example.discussfirst;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class LogInPage extends AppCompatActivity {
    private TextInputEditText emailInpEditTxt;
    private TextInputEditText passwordInpEditTxt;
    private Button btnGoToRegister;
    private Button btnLogIn;
    private TextView registerNowTextView, forgotPasswordTextView;
    private dbConnect db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in_page);

        // Initialize UI components
        emailInpEditTxt = findViewById(R.id.EmailInputL);
        passwordInpEditTxt = findViewById(R.id.PasswordInputL);
        btnLogIn = findViewById(R.id.btnLoginL);
        registerNowTextView = findViewById(R.id.txtGoRegister);
        forgotPasswordTextView = findViewById(R.id.textView6); // Referenca për "Forgot password?"

        // Set click listener for registration page
        registerNowTextView.setOnClickListener(view -> {
            Intent i = new Intent(LogInPage.this, RegisterPage.class);
            startActivity(i);
        });

        // Set click listener for Forgot Password
        forgotPasswordTextView.setOnClickListener(view -> {
            Intent intent = new Intent(LogInPage.this, ForgotPassword.class);
            startActivity(intent);
        });

        // Apply system bar insets for edge-to-edge support
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = dbConnect.getInstance(this);

        btnLogIn.setOnClickListener(view -> validateFields());
    }

    private void validateFields() {
        String email = emailInpEditTxt.getText().toString();
        String password = passwordInpEditTxt.getText().toString();

        loginUser(email, password);
    }

    private void loginUser(String email, String password) {
        try {
            boolean userExists = db.checkUser(email, password);

            if (userExists) {
                Log.d("IS_EXIST", "User exists: ");
                int userId = db.getUserId(email);

                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("USER_ID", userId);
                editor.apply();

                new AlertDialog.Builder(this)
                        .setTitle("Success")
                        .setMessage("Login successful!")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .setCancelable(false)
                        .show();

                Intent i = new Intent(LogInPage.this, EmailVerification.class);
                i.putExtra("USER_EMAIL", email);
                startActivity(i);
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Please check your email or password!")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .setCancelable(false)
                        .show();
            }

            db.close();
        } catch (Exception e) {
            Log.e("LoginError", "Error while checking user", e);
        }
    }
}
