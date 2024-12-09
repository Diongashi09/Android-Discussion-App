package com.example.discussfirst.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discussfirst.ArticleDetailActivity;
import com.example.discussfirst.R;
import com.example.discussfirst.model.Article;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private final List<Article> articles;
    private final Context context;

    // Constructor
    public NewsAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles != null ? articles : new ArrayList<>();
    }

    // Inflate layout for each item
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article, parent, false);
        return new NewsViewHolder(view);
    }

    // Bind data to views
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.titleTextView.setText(article.getTitle());
        holder.descriptionTextView.setText(article.getDescription());
        holder.dateTextView.setText(article.getDate());

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ArticleDetailActivity.class);
            intent.putExtra("title", article.getTitle());
            intent.putExtra("content", article.getDescription());
            intent.putExtra("date", article.getDate());
             // Add this field to your Article model if needed
            context.startActivity(intent);
        });
    }

    // Return item count
    @Override
    public int getItemCount() {
        return articles.size();
    }

    // Method to update articles
    public void updateArticles(List<Article> newArticles) {
        articles.clear();
        if (newArticles != null) {
            articles.addAll(newArticles);
        }
        notifyDataSetChanged();
    }

    // ViewHolder class
    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, dateTextView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.newsTitle);
            descriptionTextView = itemView.findViewById(R.id.newsDescription);
            dateTextView = itemView.findViewById(R.id.newsDate);
        }
    }
}
