package com.example.discussfirst;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProfilePage extends AppCompatActivity {
    private ArticleAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        int userId = getIntent().getIntExtra("USER_ID",-1);
        dbConnect dbHelper = new dbConnect(this);
        List<Article> articles = dbHelper.getUserArticles(userId);

        adapter = new ArticleAdapter(articles, (article, position) -> {
            dbHelper.deleteArticle(article.getId());
            adapter.removeItem(position);
        });//koment

        recyclerView.setAdapter(adapter);
    }

    // Navigate to ResetPassword Activity
    public void goToResetPassword(View view) {
        Intent intent = new Intent(this, ChangePassword.class);
        startActivity(intent);
    }
}
