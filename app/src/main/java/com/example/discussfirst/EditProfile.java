package com.example.discussfirst;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditProfile extends AppCompatActivity {
    private EditText firstNameEditText, lastNameEditText, emailEditText, universityEditText, departmentEditText;
    private Button saveButton;
    private dbConnect dbHelper;
    private String userEmail = "john.doe@example.com"; // Replace with the logged-in user's email


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        // Initialize views
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        universityEditText = findViewById(R.id.universityEditText);
        departmentEditText = findViewById(R.id.departmentEditText);
        saveButton = findViewById(R.id.saveButton);

        // Initialize database helper
        dbHelper = new dbConnect(this);

        // Populate fields with current user data
        populateUserData();

        // Set Save button listener
        saveButton.setOnClickListener(view -> saveChanges());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void populateUserData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Fetch user data based on email
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE email = ?", new String[]{userEmail});
        if (cursor.moveToFirst()) {
            firstNameEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow("firstName")));
            lastNameEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow("lastName")));
            emailEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));

            // Fetch University and Department dynamically
            int universityId = cursor.getInt(cursor.getColumnIndexOrThrow("uid"));
            int departmentId = cursor.getInt(cursor.getColumnIndexOrThrow("departamentid"));

            universityEditText.setText(getUniversityName(universityId));
            departmentEditText.setText(getDepartmentName(departmentId));
        }
        cursor.close();
    }

    private String getUniversityName(int universityId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT universityname FROM University WHERE universityId = ?",
                new String[]{String.valueOf(universityId)});
        String universityName = cursor.moveToFirst() ? cursor.getString(0) : "N/A";
        cursor.close();
        return universityName;
    }

    private String getDepartmentName(int departmentId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT departmentname FROM Departament WHERE departmentId = ?",
                new String[]{String.valueOf(departmentId)});
        String departmentName = cursor.moveToFirst() ? cursor.getString(0) : "N/A";
        cursor.close();
        return departmentName;
    }
    private void saveChanges() {
        String newFirstName = firstNameEditText.getText().toString();
        String newLastName = lastNameEditText.getText().toString();

        if (newFirstName.isEmpty() || newLastName.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the User table
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("firstName", newFirstName);
        values.put("lastName", newLastName);

        int rowsAffected = db.update("User", values, "email = ?", new String[]{userEmail});
        if (rowsAffected > 0) {
            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
        }
    }
}