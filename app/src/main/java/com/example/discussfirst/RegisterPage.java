package com.example.discussfirst;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.List;

public class RegisterPage extends AppCompatActivity {
    private TextInputEditText firstNameInpEditTxt, lastNameInpEditTxt, emailInpEditTxt, passwordInpEditTxt, confirmPasswordInpEditTxt, phoneNumberInpEditTxt;
    private Spinner gender, universitySpinner, departmentSpinner;

    private int selectedUniversityId, selectedDepartmentId ;
    private Button btnRegister;
    private TextView registerNowTextView;

    private dbConnect db;
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[A-Za-z]).{8,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        universitySpinner = findViewById(R.id.universitySpinner);
        departmentSpinner = findViewById(R.id.departmentSpinner);
        db = dbConnect.getInstance(this);
        loadUniversities();

        firstNameInpEditTxt = findViewById(R.id.firstNameInput);
        lastNameInpEditTxt = findViewById(R.id.lastNameInput);
        emailInpEditTxt = findViewById(R.id.emailInput);
        passwordInpEditTxt = findViewById(R.id.passwordInput);
        confirmPasswordInpEditTxt = findViewById(R.id.confirmPasswordInput);
        phoneNumberInpEditTxt = findViewById(R.id.phoneNumberInput);
        gender = findViewById(R.id.genderSpinner);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(view -> validateFields());

        registerNowTextView = findViewById(R.id.txtGoToLogin);
        registerNowTextView.setOnClickListener(view -> {
            Intent i = new Intent(RegisterPage.this, LogInPage.class);
            startActivity(i);
        });
    }

    private void loadUniversities() {
        try {
            SQLiteDatabase dbReadable = db.getReadableDatabase();
            Cursor cursor = dbReadable.rawQuery("SELECT * FROM " + dbConnect.getUniversityTableName(), null);

            List<String> universities = new ArrayList<>();
            List<Integer> universityIds = new ArrayList<>();

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(dbConnect.getUniversityID()));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(dbConnect.getUniversityName()));
                universities.add(name);
                universityIds.add(id);
            }
            cursor.close();

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, universities);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            universitySpinner.setAdapter(adapter);

            universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int selectedUniversityId = universityIds.get(position);
                    loadDepartments(selectedUniversityId);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        } catch (Exception e) {
            Log.e("loadUniversities", "Error loading universities", e);
            Toast.makeText(this, "Failed to load universities: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadDepartments(int universityId) {
        try {
            this.selectedUniversityId = universityId;
            SQLiteDatabase dbReadable = db.getReadableDatabase();
            Cursor cursor = dbReadable.rawQuery(
                    "SELECT * FROM " + dbConnect.getDepartamentTable() + " WHERE universityId = ?",
                    new String[]{String.valueOf(universityId)}
            );

            List<String> departments = new ArrayList<>();
            List<Integer> departmentIds = new ArrayList<>();

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("departmentId"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("departmentname"));
                departments.add(name);
                departmentIds.add(id);
            }
            cursor.close();

            if (departments.isEmpty()) {
                // No departments found for this university
                Toast.makeText(this, "No departments available for this university", Toast.LENGTH_SHORT).show();
            }

            ArrayAdapter<String> departmentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departments);
            departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            departmentSpinner.setAdapter(departmentAdapter);

            departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedDepartmentId = departmentIds.get(position);  // Set selected department ID
                    Log.d("Selected Department", "ID: " + selectedDepartmentId + ", Name: " + departments.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Log.d("Department", "No department selected.");
                }
            });

        } catch (Exception e) {
            Log.e("loadDepartments", "Error loading departments", e);
            Toast.makeText(this, "Failed to load departments: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void validateFields() {
        String firstName = firstNameInpEditTxt.getText().toString().trim();
        String lastName = lastNameInpEditTxt.getText().toString().trim();
        String email = emailInpEditTxt.getText().toString().trim();
        String password = passwordInpEditTxt.getText().toString();
        String confirmPassword = confirmPasswordInpEditTxt.getText().toString();
        String phoneNumber = phoneNumberInpEditTxt.getText().toString().trim();
        String gender = this.gender.getSelectedItem().toString();
        int universityId = this.selectedUniversityId;
        int departmentId = this.selectedDepartmentId;
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

        registerUser(firstName, lastName, email, password, phoneNumber, gender, departmentId, universityId, profileImage, isBlocked);
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
