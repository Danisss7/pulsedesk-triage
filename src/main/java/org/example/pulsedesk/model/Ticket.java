package org.example.pulsedesk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "comment_id", nullable = false, unique = true)
    private Comment comment;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String priority;

    @Column(nullable = false, length = 1000)
    private String summary;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Ticket() {}

    public Ticket(Comment comment, String title, String category, String priority, String summary, LocalDateTime createdAt) {
        this.comment = comment;
        this.title = title;
        this.category = category;
        this.priority = priority;
        this.summary = summary;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Comment getComment() { return comment; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getPriority() { return priority; }
    public String getSummary() { return summary; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setComment(Comment comment) { this.comment = comment; }
    public void setTitle(String title) { this.title = title; }
    public void setCategory(String category) { this.category = category; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setSummary(String summary) { this.summary = summary; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}