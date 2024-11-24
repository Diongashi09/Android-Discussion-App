package com.example.discussfirst;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    Button btnGoToLogin;
    Button btnGoToRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnGoToLogin = findViewById(R.id.goLogin);
        btnGoToRegister = findViewById(R.id.goRegister);

        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LogInPage.class);
                startActivity(i);
            }
    });
        dbConnect dbHelper  = dbConnect.getInstance(this);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.insertTestData();
        File backupFile = new File(getExternalFilesDir(null), "Backup/UniConnectDB.db");
        if (backupFile.exists()) {
            dbHelper.restoreDatabase(this);
            System.out.println("Database restored from backup.");
        } else {
            dbHelper.backupDatabase(this);
            System.out.println("Initial backup completed.");
        }
        btnGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RegisterPage.class);
                startActivity(i);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
}