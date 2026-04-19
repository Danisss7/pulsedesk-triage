package org.example.pulsedesk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 5000)
    private String text;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean analyzed;

    private Boolean shouldCreateTicket;

    @JsonIgnore
    @OneToOne(mappedBy = "comment", cascade = CascadeType.ALL)
    private Ticket ticket;

    public Comment() {}

    public Comment(String text, String source, LocalDateTime createdAt) {
        this.text = text;
        this.source = source;
        this.createdAt = createdAt;
        this.analyzed = false;
    }

    public Long getId() { return id; }
    public String getText() { return text; }
    public String getSource() { return source; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isAnalyzed() { return analyzed; }
    public Boolean getShouldCreateTicket() { return shouldCreateTicket; }
    public Ticket getTicket() { return ticket; }

    @Transient
    public Long getTicketId() {
        return ticket != null ? ticket.getId() : null;
    }

    public void setId(Long id) { this.id = id; }
    public void setText(String text) { this.text = text; }
    public void setSource(String source) { this.source = source; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setAnalyzed(boolean analyzed) { this.analyzed = analyzed; }
    public void setShouldCreateTicket(Boolean shouldCreateTicket) { this.shouldCreateTicket = shouldCreateTicket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }
}