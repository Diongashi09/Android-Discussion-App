package com.example.discussfirst;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterPage extends AppCompatActivity {
    private TextInputEditText usernameInpEditTxt;
    private TextInputEditText emailInpEditTxt;
    private TextInputEditText passwordInpEditTxt;
    private TextInputEditText confirmPasswordInpEditTxt;
    private Button btnRegister;
    private TextView loginNowTextView;
    private dbConnect db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_page);

        db = new dbConnect(this); // Initialize database connection
        usernameInpEditTxt = findViewById(R.id.UserNameInputR);
        emailInpEditTxt = findViewById(R.id.EmailInputR);
        passwordInpEditTxt = findViewById(R.id.PasswordInputR);
        confirmPasswordInpEditTxt = findViewById(R.id.ConfirmPasswordInputR);
        btnRegister = findViewById(R.id.btnRegisterR);

        loginNowTextView = findViewById(R.id.txtGoLogin);
        loginNowTextView.setOnClickListener(view -> {
            Intent i = new Intent(RegisterPage.this, LogInPage.class);
            startActivity(i);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnRegister.setOnClickListener(view -> validateFields());
    }

    private void validateFields() {
        String username = usernameInpEditTxt.getText().toString();
        String firstName="test" ;
        String lastName="test" ;
        String phoneNumber = "044-555-555";
        String gender = "Male";

        String email = emailInpEditTxt.getText().toString();
        String password = passwordInpEditTxt.getText().toString();
        String confirmPassword = confirmPasswordInpEditTxt.getText().toString();
        int departmentId =1;
        int universityId = 1;
        String profileImage = "test";
        boolean isBlocked = false;
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please enter your username!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email!", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please enter your confirm password!", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
        } else {
            registerUser(firstName, lastName, email, password, phoneNumber, gender, departmentId, universityId, profileImage, isBlocked);
        }
    }

    private void registerUser(String firstName, String lastName, String email, String password, String phoneNumber, String gender, int departmentId, int universityId, String profileImage, boolean isBlocked) {
        if (db.registerUser(firstName, lastName, email, password, phoneNumber, gender, departmentId, universityId, profileImage, isBlocked)) {
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
            // Navigate to login page
            Intent intent = new Intent(RegisterPage.this, LogInPage.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Email may already be in use.", Toast.LENGTH_SHORT).show();
        }
    }

}
