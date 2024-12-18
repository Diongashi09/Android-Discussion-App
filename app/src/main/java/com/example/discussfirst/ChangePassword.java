package com.example.discussfirst;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChangePassword extends AppCompatActivity {

    private EditText emailField, firstNameField, lastNameField;
    private Button saveProfileButton;

    // Reference to the database
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Initialize views
        emailField = findViewById(R.id.email);
        firstNameField = findViewById(R.id.firstName);
        lastNameField = findViewById(R.id.lastName);
        saveProfileButton = findViewById(R.id.saveProfileButton);

        // Initialize database connection
        dbConnect databaseHelper = dbConnect.getInstance(this); // Get the singleton instance
        database = databaseHelper.getWritableDatabase(); // Access the writable database

        // Retrieve email passed through Intent
        String userEmail = getIntent().getStringExtra("USER_EMAIL");
        if (userEmail != null && !userEmail.isEmpty()) {
            emailField.setText(userEmail);
        }

        // Set up the save button click listener
        saveProfileButton.setOnClickListener(v -> saveProfileData());
    }

    /**
     * Save the profile data to the database by updating the record
     */
    private void saveProfileData() {
        // Get user inputs
        String email = emailField.getText().toString().trim();
        String firstName = firstNameField.getText().toString().trim();
        String lastName = lastNameField.getText().toString().trim();

        // Validate inputs
        if (email.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put("firstName", firstName);
        values.put("lastName", lastName);

        try {
            // Update the record where the email matches
            int rowsAffected = database.update("User", values, "email = ?", new String[]{email});

            if (rowsAffected > 0) {
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No record found for the given email!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // Handle database operation errors
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database connection to prevent leaks
        if (database != null) {
            database.close();
        }
    }

    /**
     * Show the change password dialog
     */
    public void showChangePasswordDialog(View view) {
        String userEmail = getIntent().getStringExtra("USER_EMAIL");
        Intent intent = new Intent(this, dialog_change_password.class);
        intent.putExtra("USER_EMAIL", userEmail);
        startActivity(intent);
    }

}
