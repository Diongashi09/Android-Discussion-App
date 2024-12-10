package com.example.discussfirst;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discussfirst.adapter.NewsAdapter;
import com.example.discussfirst.model.Article;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends AppCompatActivity {
    private dbConnect dbHelper;
    private NewsAdapter adapter;
    private List<Article> articles;
    private RecyclerView recyclerView;
    private View modalForm;
    private EditText modalTitleEditText, modalContentEditText;
    private Button saveButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new dbConnect(this);
        setContentView(R.layout.articles);

        showWelcomeDialog();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.newsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch articles from the database
        articles = getArticlesFromDatabase();

        // Set up the adapter
        adapter = new NewsAdapter(this, articles);
        recyclerView.setAdapter(adapter);

        // Initialize modal form views
        modalForm = findViewById(R.id.modalForm);
        modalTitleEditText = findViewById(R.id.modalTitleEditText);
        modalContentEditText = findViewById(R.id.modalContentEditText);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Add click listener to open modal from input layout


        // Add click listeners for modal buttons
        setupModalListeners();

        // Add button to open modal
        Button addArticleButton = findViewById(R.id.addArticleButton);
        addArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modalForm.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Fetches articles from the database.
     */
    private void showWelcomeDialog() {
        new AlertDialog.Builder(ArticleActivity.this)
                .setTitle("Mirë se vini")
                .setMessage("Mirë se vini në UniConnect, ne faqen ku ju mund te postoni problemet e juaja!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private List<Article> getArticlesFromDatabase() {
        List<Article> articles = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            Cursor cursor = db.query(
                    "Article",
                    new String[]{"title", "content", "createdAt"},
                    null,
                    null,
                    null,
                    null,
                    "createdAt DESC"
            );

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow("createdAt"));
                    articles.add(new Article(title, description, date));

                    Log.d("DB_DEBUG", "Article: " + title + ", " + description + ", " + date);
                }
                cursor.close();
            } else {
                Log.e("DB_DEBUG", "Cursor is null. Query may have failed.");
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error reading from database: ", e);
        } finally {
            db.close();
        }
        Log.d("DB_DEBUG", "Articles fetched: " + articles.size());
        return articles;
    }

    /**
     * Sets up listeners for the modal form buttons.
     */
    private void setupModalListeners() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = modalTitleEditText.getText().toString().trim();
                String content = modalContentEditText.getText().toString().trim();

                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(ArticleActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    saveArticleToDatabase(title, content);
                    modalForm.setVisibility(View.GONE);
                    modalTitleEditText.setText(""); // Clear input fields
                    modalContentEditText.setText("");
                }
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modalForm.setVisibility(View.GONE);
                modalTitleEditText.setText(""); // Clear input fields
                modalContentEditText.setText("");
            }
        });
    }

    /**
     * Saves an article to the database and updates the RecyclerView.
     */
    private void saveArticleToDatabase(String title, String content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("USER_ID", -1); // -1 is the default value if no id is found
        values.put("title", title); // Ensure 'title' is included
        values.put("content", content);
        values.put("category", "Hdadhadah"); // Example static category
        values.put("userId", userId); // Example static user ID

        long result = db.insert("Article", null, values);

        if (result != -1) {
            // Update RecyclerView with new data
            articles.clear();
            articles.addAll(getArticlesFromDatabase());
            adapter.notifyDataSetChanged();

            Toast.makeText(this, "Article added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("DB_ERROR", "Failed to insert article: " + values.toString());
            Toast.makeText(this, "Failed to add article", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
}
