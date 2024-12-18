package com.example.discussfirst;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Article> articleList;
    private OnArticleDeleteListener deleteListener;
    private Context context; // Shto Context

    public ArticleAdapter(Context context, List<Article> articleList, OnArticleDeleteListener deleteListener) {
        this.context = context; // Ruaj kontekstin
        this.articleList = articleList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.title.setText(article.getTitle());
        holder.date.setText(article.getCreatedAt());
        holder.category.setText(article.getCategory());
        holder.preview.setText(article.getPreviewContent());

        // Funksionaliteti i butonit Delete
        holder.deleteButton.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(article, position);
            }
        });

        // Klikim për të hapur ProfileActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProfilePage.class);
            intent.putExtra("articleId", article.getId()); // Opsionale: dërgo ID-në e artikullit
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public void removeItem(int position) {
        articleList.remove(position);
        notifyItemRemoved(position);
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, category, preview;
        ImageButton deleteButton;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.articleTitle);
            date = itemView.findViewById(R.id.articleDate);
            category = itemView.findViewById(R.id.articleCategory);
            preview = itemView.findViewById(R.id.articlePreview);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    // Interface për callback të delete
    public interface OnArticleDeleteListener {
        void onDelete(Article article, int position);
    }
}
