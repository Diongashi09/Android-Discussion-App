package com.example.discussfirst;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfilePage extends AppCompatActivity {

    private dbConnect database;
    private TextView firstNameField, lastNameField, emailField, numberField, genderField, departamentField, universityField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        // Initialize database and UI components
        database = dbConnect.getInstance(this);
        initializeFields();

        // Retrieve the email passed from the previous activity
        String userEmail = getIntent().getStringExtra("USER_EMAIL");
        if (userEmail != null && !userEmail.isEmpty()) {
            fetchUserData(userEmail);
        } else {
            Toast.makeText(this, "No email provided!", Toast.LENGTH_SHORT).show();
            finish(); // Exit the activity if no valid email
        }
    }

    private void initializeFields() {
        firstNameField = findViewById(R.id.textFirstName);
        lastNameField = findViewById(R.id.textLastName);
        emailField = findViewById(R.id.textEmail);
        numberField = findViewById(R.id.textNumber);
        genderField = findViewById(R.id.textGender);
        departamentField = findViewById(R.id.textDepartament);
        universityField = findViewById(R.id.textUniversity);
    }

    private void fetchUserData(String email) {
        SQLiteDatabase db = database.getReadableDatabase();
        String query = "SELECT u.firstName, u.lastName, u.email, u.phoneNumber, u.gender, d.departmentname, uni.universityname " +
                "FROM User u " +
                "JOIN Departament d ON u.departmentId = d.departmentId " +
                "JOIN University uni ON u.universityId = uni.universityId " +
                "WHERE email = ?";

        try (Cursor cursor = db.rawQuery(query, new String[]{email})) {
            if (cursor.moveToFirst()) {
                firstNameField.setText(cursor.getString(0));
                lastNameField.setText(cursor.getString(1));
                emailField.setText(cursor.getString(2));
                numberField.setText(cursor.getString(3));
                genderField.setText(cursor.getString(4));
                departamentField.setText(cursor.getString(5));
                universityField.setText(cursor.getString(6));
            } else {
                Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void goToResetPassword(View view) {
        String userEmail = emailField.getText().toString();
        Intent intent = new Intent(this, ChangePassword.class);
        intent.putExtra("USER_EMAIL", userEmail);
        startActivity(intent);
    }
}
