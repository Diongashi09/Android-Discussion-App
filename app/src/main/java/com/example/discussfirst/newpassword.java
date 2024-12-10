package com.example.discussfirst;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.discussfirst.dbConnect;

public class newpassword extends AppCompatActivity {

    private EditText newPasswordInput, confirmPasswordInput;
    private Button updatePasswordButton;
    private String emailFromPrevious;
    private dbConnect database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpassword);

        // Retrieve email from the previous activity
        emailFromPrevious = getIntent().getStringExtra("USER_EMAIL");
        Log.d("NewPassword", "Email from previous activity: " + emailFromPrevious);

        // Initialize the views
        newPasswordInput = findViewById(R.id.newPasswordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        updatePasswordButton = findViewById(R.id.updatePasswordButton);

        // Initialize the database helper class
        database = new dbConnect(this);

        // Handle the update button click
        updatePasswordButton.setOnClickListener(v -> {
            String newPassword = newPasswordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(newpassword.this, "Ju lutem plotësoni të gjitha fushat!", Toast.LENGTH_SHORT).show();
            } else if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(newpassword.this, "Fjalëkalimet nuk përputhen!", Toast.LENGTH_SHORT).show();
            } else {
                updatePassword(emailFromPrevious, newPassword);
            }
        });
    }

    private void updatePassword(String email, String newPassword) {
        // Encrypt the new password
        String encryptedPassword = Encryption.encrypt(newPassword);
        if (encryptedPassword == null) {
            Log.e("PasswordUpdate", "Encryption failed!");
            Toast.makeText(this, "Gabim në enkriptim të fjalëkalimit!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = database.getWritableDatabase();

            // Check if email exists
            cursor = db.query(
                    "User",
                    new String[]{"email"},
                    "email = ?",
                    new String[]{email},
                    null, null, null
            );

            if (cursor != null && cursor.moveToFirst()) {
                Log.d("PasswordUpdate", "Email found in database. Updating password...");

                // Prepare values for update
                ContentValues values = new ContentValues();
                values.put("password", encryptedPassword);

                // Perform update
                int rowsUpdated = db.update(
                        "User",
                        values,
                        "email = ?",
                        new String[]{email}
                );

                if (rowsUpdated > 0) {
                    Toast.makeText(this, "Fjalëkalimi u ndryshua me sukses!", Toast.LENGTH_SHORT).show();
                    Log.d("PasswordUpdate", "Password updated successfully");
                    Intent intent = new Intent(this, LogInPage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    finish(); // Close the activity
                } else {
                    Toast.makeText(this, "Ndodhi një problem gjatë përditësimit!", Toast.LENGTH_SHORT).show();
                    Log.e("PasswordUpdate", "Password update failed. No rows updated.");
                }
            } else {
                Toast.makeText(this, "Emaili nuk ekziston në sistem!", Toast.LENGTH_SHORT).show();
                Log.e("PasswordUpdate", "Email does not exist in the database.");
            }
        } catch (Exception e) {
            Log.e("PasswordUpdate", "Error while updating password: " + e.getMessage());
            Toast.makeText(this, "Gabim gjatë përditësimit të fjalëkalimit!", Toast.LENGTH_SHORT).show();
        } finally {
            // Close resources
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }
}
