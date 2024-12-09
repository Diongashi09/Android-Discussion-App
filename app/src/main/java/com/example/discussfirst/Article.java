package com.example.discussfirst;
public class Article {
    private int id;
    private int userId;
    private String content;
    private String category;
    private String createdAt;
    private String title;

    // Constructor
    public Article(int id, int userId, String title, String content, String category, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.category = category;
        this.createdAt = createdAt;
    }

    // Getters

    //sdsd
    public int getId() { return this.id; }
    public int getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getCategory() { return category; }
    public String getCreatedAt() { return createdAt; }

    // Generate preview of content
    public String getPreviewContent() {
        return content.length() > 50 ? content.substring(0, 50) + "..." : content;
    }
}
