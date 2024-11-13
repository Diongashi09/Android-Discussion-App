package com.example.discussfirst;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
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
    private TextInputEditText usernameInpEditTxt;//UserNameInputR
    private TextInputEditText emailInpEditTxt; //EmailInputR
    private TextInputEditText passwordInpEditTxt; //PasswordInputR
    private TextInputEditText confirmPasswordInpEditTxt; //ConfirmPasswordInputR
    private Button btnRegister; //btnRegisterR
    private TextView loginNowTextView; //txtGoLogin

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_page);


        usernameInpEditTxt = findViewById(R.id.UserNameInputR);
        emailInpEditTxt = findViewById(R.id.EmailInputR);
        passwordInpEditTxt = findViewById(R.id.PasswordInputR);
        confirmPasswordInpEditTxt = findViewById(R.id.ConfirmPasswordInputR);
        btnRegister = findViewById(R.id.btnRegisterR);

        loginNowTextView = findViewById(R.id.txtGoLogin);
        loginNowTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterPage.this, LogInPage.class);
                startActivity(i);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        btnRegister.setOnClickListener(view -> validateFields());
    }

    private void validateFields(){
        String username = usernameInpEditTxt.getText().toString();
        String email = emailInpEditTxt.getText().toString();
        String password = passwordInpEditTxt.getText().toString();
        String confirmPassword = confirmPasswordInpEditTxt.getText().toString();

        if (TextUtils.isEmpty(username)){
            Toast.makeText(this,"Please enter your username!",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this,"Please enter your email!",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Please enter your password!",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this,"Please enter your confirm password!",Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this,"Please enter a valid email address!",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"Success!",Toast.LENGTH_SHORT).show();
        }
    }
}