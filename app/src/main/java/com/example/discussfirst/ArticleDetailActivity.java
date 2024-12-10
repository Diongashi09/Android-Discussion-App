package com.example.discussfirst;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ArticleDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        // Bind layout views to variables
        TextView titleTextView = findViewById(R.id.articleTitle);
        TextView contentTextView = findViewById(R.id.articleContent);

        // Get data from Intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");

        // Bind data to views
        titleTextView.setText(title);
        contentTextView.setText(content);
    }
}
