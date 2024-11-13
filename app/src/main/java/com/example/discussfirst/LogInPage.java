package com.example.discussfirst;

import android.annotation.SuppressLint;
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

public class LogInPage extends AppCompatActivity {
    private TextInputEditText emailInpEditTxt;//EmailInputL
    private TextInputEditText passwordInpEditTxt;//PasswordInputL
    private Button btnLogIn;//btnLoginL
    private TextView registerNowTextView;//txtGoRegister

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

        registerNowTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LogInPage.this, RegisterPage.class);
                startActivity(i);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnLogIn.setOnClickListener(view -> validateFields());
    }
    private void validateFields(){
        String email=emailInpEditTxt.getText().toString();
        String password=passwordInpEditTxt.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter your email!",Toast.LENGTH_SHORT).show();
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this,"Please enter a valid email address!",Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Please enter your password!",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"Success!",Toast.LENGTH_SHORT).show();
        }
    }
}