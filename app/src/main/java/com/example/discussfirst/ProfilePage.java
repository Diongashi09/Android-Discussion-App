package com.example.discussfirst;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProfilePage extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_page);

        Button editProfileButton = findViewById(R.id.editProfileButton);
        editProfileButton.setOnClickListener(view -> {
            Intent intent = new Intent(ProfilePage.this, EditProfile.class);
            intent.putExtra("email", "john.doe@example.com"); // Replace with the logged-in user's email
            startActivity(intent);
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        int userId = getIntent().getIntExtra("USER_ID",-1);
        dbConnect dbHelper = new dbConnect(this);


    }
    //sdsds

    // Navigate to ResetPassword Activity
    public void goToResetPassword(View view) {
        Intent intent = new Intent(this, ChangePassword.class);
        startActivity(intent);
    }
}
