package com.example.discussfirst;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class dialog_change_password extends AppCompatActivity {

    private EditText oldPasswordField, newPasswordField, confirmPasswordField;
    private Button submitButton;
    private String userEmail;

    // Reference to the database
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_change_password);

        oldPasswordField = findViewById(R.id.oldPassword);
        newPasswordField = findViewById(R.id.newPassword);
        confirmPasswordField = findViewById(R.id.confirmPassword);
        submitButton = findViewById(R.id.submitChange);

        // Initialize database connection
        dbConnect databaseHelper = dbConnect.getInstance(this);
        database = databaseHelper.getWritableDatabase();

        // Retrieve email passed through the Bundle
        if (getIntent().getExtras() != null) {
            userEmail = getIntent().getExtras().getString("USER_EMAIL");
        }

        // Handle submit button click
        submitButton.setOnClickListener(v -> changePassword());
    }

    /**
     * Change the password logic
     */
    private void changePassword() {
        String oldPassword = oldPasswordField.getText().toString().trim();
        String newPassword = newPasswordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the old password is correct
        if (!isOldPasswordCorrect(oldPassword)) {
            Toast.makeText(this, "Old password is incorrect!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the password in the database
        if (updatePasswordInDatabase(newPassword)) {
            Toast.makeText(this, "Password changed successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Close the dialog after password change
        } else {
            Toast.makeText(this, "Error: Unable to change password!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isOldPasswordCorrect(String oldPassword) {
        // Query to fetch the user's current encrypted password from the database
        String query = "SELECT password FROM User WHERE email = ?";
        Cursor cursor = database.rawQuery(query, new String[]{userEmail});

        if (cursor != null && cursor.moveToFirst()) {
            int passwordColumnIndex = cursor.getColumnIndex("password");
            if (passwordColumnIndex != -1) {
                String storedEncryptedPassword = cursor.getString(passwordColumnIndex);
                cursor.close();

                // Decrypt the stored encrypted password
                String decryptedPassword = Encryption.decrypt(storedEncryptedPassword);

                // Compare the decrypted password with the input old password
                return oldPassword.equals(decryptedPassword);
            } else {
                cursor.close();
                Toast.makeText(this, "Password column not found in the database.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            cursor.close();
            Toast.makeText(this, "No user found with the provided email.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * Update the new password in the database
     */
    private boolean updatePasswordInDatabase(String newPassword) {
        // Encrypt the new password before storing
        String encryptedPassword = Encryption.encrypt(newPassword);

        ContentValues values = new ContentValues();
        values.put("password", encryptedPassword);

        // Update the record where the email matches
        int rowsAffected = database.update("User", values, "email = ?", new String[]{userEmail});

        return rowsAffected > 0; // Return true if the update was successful
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database connection to prevent leaks
        if (database != null) {
            database.close();
        }
    }
}
