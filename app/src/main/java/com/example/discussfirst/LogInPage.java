package com.example.discussfirst;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.Nullable;

public class LogInPage extends AppCompatActivity {
    private TextInputEditText emailInpEditTxt;
    private TextInputEditText passwordInpEditTxt;
    private Button btnGoToRegister ;

    private Button btnLogIn;
    private TextView registerNowTextView;
    private dbConnect db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in_page);
        emailInpEditTxt = findViewById(R.id.EmailInputL);
        passwordInpEditTxt = findViewById(R.id.PasswordInputL);
        btnLogIn = findViewById(R.id.btnLoginL);
        registerNowTextView = findViewById(R.id.txtGoRegister);

        registerNowTextView.setOnClickListener(view -> {
            Intent i = new Intent(LogInPage.this, RegisterPage.class);
            startActivity(i);
        });
        btnGoToRegister = findViewById(R.id.goRegister);

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
        String encryptedPassword = Encryption.encrypt(password);
            loginUser(email, encryptedPassword);

    }


    private void loginUser(String email, String password) {
        try {
        boolean userExists = db.checkUser(email, password);
            Intent i = new Intent(LogInPage.this, EmailVerification.class);
            startActivity(i);


            if (userExists) {
            new AlertDialog.Builder(this)
                    .setTitle("Success")
                    .setMessage("Login successful!")
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setCancelable(false)
                    .show();
                db.close();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please check your email or password!")
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setCancelable(false)
                    .show();
        }
    }catch (Exception e) {
            Log.e("LoginError", "Gabim gjatë kontrollimit të përdoruesit", e);
        }}
}
