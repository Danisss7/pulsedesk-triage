package org.example.pulsedesk.dto;

import java.time.LocalDateTime;

public class TicketResponse {
    private Long id;
    private Long commentId;
    private String title;
    private String category;
    private String priority;
    private String summary;
    private LocalDateTime createdAt;

    public TicketResponse() {}

    public TicketResponse(Long id, Long commentId, String title, String category,
                          String priority, String summary, LocalDateTime createdAt) {
        this.id = id;
        this.commentId = commentId;
        this.title = title;
        this.category = category;
        this.priority = priority;
        this.summary = summary;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getCommentId() { return commentId; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getPriority() { return priority; }
    public String getSummary() { return summary; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setCommentId(Long commentId) { this.commentId = commentId; }
    public void setTitle(String title) { this.title = title; }
    public void setCategory(String category) { this.category = category; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setSummary(String summary) { this.summary = summary; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}