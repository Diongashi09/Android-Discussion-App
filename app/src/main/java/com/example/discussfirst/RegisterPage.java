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
    private TextInputEditText firstNameInpEditTxt, lastNameInpEditTxt, emailInpEditTxt, passwordInpEditTxt, confirmPasswordInpEditTxt, phoneNumberInpEditTxt;
    private Button btnRegister;
    private dbConnect db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page); // Ensure this layout file matches the provided XML

        // Initialize database connection
        db = new dbConnect(RegisterPage.this);

        // Initialize UI components
        firstNameInpEditTxt = findViewById(R.id.firstNameInput);
        lastNameInpEditTxt = findViewById(R.id.lastNameInput);
        emailInpEditTxt = findViewById(R.id.emailInput);
        passwordInpEditTxt = findViewById(R.id.passwordInput);
        confirmPasswordInpEditTxt = findViewById(R.id.confirmPasswordInput);
        phoneNumberInpEditTxt = findViewById(R.id.phoneNumberInput);
        btnRegister = findViewById(R.id.btnRegister);

        // Register button listener
        btnRegister.setOnClickListener(view -> validateFields());
    }

    private void validateFields() {
        // Extract values from input fields
        String firstName = firstNameInpEditTxt.getText().toString().trim();
        String lastName = lastNameInpEditTxt.getText().toString().trim();
        String email = emailInpEditTxt.getText().toString().trim();
        String password = passwordInpEditTxt.getText().toString();
        String confirmPassword = confirmPasswordInpEditTxt.getText().toString();
        String phoneNumber = phoneNumberInpEditTxt.getText().toString().trim();
        String gender = "male";
        int departmentId = 1; // Set default or dynamic value
        int universityId = 1; // Set default or dynamic value
        String profileImage = "default_profile.jpg"; // Placeholder value
        boolean isBlocked = false;

        // Validate inputs
        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this, "Please enter your first name!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Please enter your last name!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please confirm your password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "Please enter your phone number!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Register user
        registerUser(firstName, lastName, email, password, phoneNumber, gender, departmentId, universityId, profileImage, isBlocked);
    }

    private void registerUser(String firstName, String lastName, String email, String password, String phoneNumber, String gender, int departmentId, int universityId, String profileImage, boolean isBlocked) {
        // Call the database function to register the user
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
