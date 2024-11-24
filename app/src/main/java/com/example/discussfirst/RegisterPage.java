package com.example.discussfirst;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
    private Spinner gender;
    private Button btnRegister;
    private dbConnect db;
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[A-Za-z]).{8,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        db =  dbConnect.getInstance(this);


        firstNameInpEditTxt = findViewById(R.id.firstNameInput);
        lastNameInpEditTxt = findViewById(R.id.lastNameInput);
        emailInpEditTxt = findViewById(R.id.emailInput);
        passwordInpEditTxt = findViewById(R.id.passwordInput);
        confirmPasswordInpEditTxt = findViewById(R.id.confirmPasswordInput);
        phoneNumberInpEditTxt = findViewById(R.id.phoneNumberInput);
        gender =findViewById(R.id.genderSpinner)  ;
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(view -> validateFields());
    }

    private void validateFields() {
        String firstName = firstNameInpEditTxt.getText().toString().trim();
        String lastName = lastNameInpEditTxt.getText().toString().trim();
        String email = emailInpEditTxt.getText().toString().trim();
        String password = passwordInpEditTxt.getText().toString();
        String confirmPassword = confirmPasswordInpEditTxt.getText().toString();
        String phoneNumber = phoneNumberInpEditTxt.getText().toString().trim();
        String gender = this.gender.getSelectedItem().toString();
        int departmentId = 1;
        int universityId = 1;
        String profileImage = "default_profile.jpg";
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
        if (!password.matches(PASSWORD_PATTERN)) {
            Toast.makeText(this, "Password must be at least 8 characters long, contain at least 1 number, 1 special character, and 1 letter!", Toast.LENGTH_LONG).show();
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
        String encryptedPassword = Encryption.encrypt(password);

        registerUser(firstName, lastName, email, encryptedPassword, phoneNumber, gender, departmentId, universityId, profileImage, isBlocked);
    }

    private void registerUser(String firstName, String lastName, String email, String password, String phoneNumber, String gender, int departmentId, int universityId, String profileImage, boolean isBlocked) {
        if (db.registerUser(firstName, lastName, email, password, phoneNumber, gender, departmentId, universityId, profileImage, isBlocked)) {
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterPage.this, LogInPage.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Email may already be in use.", Toast.LENGTH_SHORT).show();
        }
    }
}
